package org.toxsoft.skf.mnemo.gui.tsgui;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Реализация интерфейса {@link IVedViselsMasterSlaveRelationsManager}.
 *
 * @author vs
 */
public class VedViselsMasterSlaveRelationsManager
    implements IVedViselsMasterSlaveRelationsManager, IVedItemPropertyChangeInterceptor<VedAbstractVisel> {

  class MasterSlaveActionsProvider
      extends MethodPerActionTsActionSetProvider {

    // /**
    // * ID of action {@link #ACDEF_ENSLAVE}.
    // */
    // public static final String ACTID_ENSLAVE = "ved.enslave"; //$NON-NLS-1$

    /**
     * ID of action {@link #ACDEF_FREE}.
     */
    public static final String ACTID_FREE = "ved.free"; //$NON-NLS-1$

    // /**
    // * Action: copy selected visels and associated actors to the internal buffer.
    // */
    // public static final ITsActionDef ACDEF_ENSLAVE = ofPush2( ACTID_ENSLAVE, //
    // "Усыновить", "Делает родителем элемент на котором находится данный элемент", ICONID_ENSLAVE );

    /**
     * Action: paste selected visels and associated actors from the internal buffer.
     */
    public static final ITsActionDef ACDEF_FREE = TsActionDef.ofPush2( ACTID_FREE, //
        "Осиротить", "Лишает родителя родительских прав, делая элемент сиротой", ICONID_FREE );

    MasterSlaveActionsProvider() {
      // defineAction( ACDEF_ENSLAVE, VedViselsMasterSlaveRelationsManager.this::enslave );
      defineAction( ACDEF_FREE, VedViselsMasterSlaveRelationsManager.this::freeSlave );
    }

    @Override
    protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
      if( selectionManager.selectedViselIds().size() > 1 ) {
        return true;
      }
      if( clickedVisel != null ) {
        // if( aActionDef.id().equals( ACTID_ENSLAVE ) ) {
        // if( clickedVisel.params().hasKey( PARAMID_MASTER_ID ) ) {
        // IAtomicValue val = clickedVisel.params().getValue( PARAMID_MASTER_ID );
        // if( val == IAtomicValue.NULL ) {
        // return true;
        // }
        // return false;
        // }
        // return true;
        // }
        if( aActionDef.id().equals( ACTID_FREE ) ) {
          IAtomicValue masterId = IAtomicValue.NULL;
          if( clickedVisel.params().hasKey( PARAMID_MASTER_ID ) ) {
            masterId = clickedVisel.params().getValue( PARAMID_MASTER_ID );
          }
          return masterId != IAtomicValue.NULL;
        }
      }
      return false;
    }

  }

  /**
   * ИД параметра визеля, значение которого является ИДом мастера
   */
  private static final String PARAMID_MASTER_ID = "ved.masterId"; //$NON-NLS-1$

  /**
   * ИД параметра визеля, значение которого является списком подчиненных ИДов
   */
  private static final String PARAMID_SLAVE_IDS = "ved.slaveIds"; //$NON-NLS-1$

  private final IVedScreen vedScreen;

  private final MasterSlaveActionsProvider actionsProvider;

  private final MenuCreatorFromAsp menuCreator;

  private final IVedViselSelectionManager selectionManager;

  /**
   * Конструктор.
   *
   * @param aVedScreen {@link IVedScreen} - эккран редактирования
   * @param aSelectionManager {@link IVedViselSelectionManager} - менеджер выделения
   */
  public VedViselsMasterSlaveRelationsManager( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager ) {
    vedScreen = aVedScreen;
    selectionManager = aSelectionManager;
    actionsProvider = new MasterSlaveActionsProvider();
    menuCreator = new MenuCreatorFromAsp( actionsProvider, vedScreen.tsContext() );

    vedScreen.model().visels().eventer().addListener( ( aSource, aOp, aId ) -> {
      internalUpdate();
    } );
  }

  // ------------------------------------------------------------------------------------
  // IVedViselsMasterSlaveRelationsManager
  //

  @Override
  public String viselMasterId( String aViselId ) {
    VedAbstractVisel visel = vedScreen.model().visels().list().getByKey( aViselId );
    if( visel.params().hasKey( PARAMID_MASTER_ID ) ) {
      IAtomicValue value = visel.params().getValue( PARAMID_MASTER_ID );
      if( value != IAtomicValue.NULL ) {
        String masterId = value.asString();
        if( !masterId.isBlank() ) {
          return masterId;
        }
      }
    }
    return null;
  }

  @Override
  public String viselMasterId( IVedItemCfg aCfg ) {
    if( aCfg.params().hasKey( PARAMID_MASTER_ID ) ) {
      IAtomicValue value = aCfg.params().getValue( PARAMID_MASTER_ID );
      if( value.isAssigned() ) {
        String str = value.asString();
        if( !str.equals( IStridable.NONE_ID ) && !str.equals( "None" ) ) {
          return str;
        }
      }
    }
    return null;
  }

  @Override
  public IStringList listSlaveViselIds( String aMasterId ) {
    if( vedScreen.model().visels().list().hasKey( aMasterId ) ) {
      VedAbstractVisel visel = vedScreen.model().visels().list().getByKey( aMasterId );
      if( visel.params().hasKey( PARAMID_SLAVE_IDS ) ) {
        // return visel.params().getValobj( PARAMID_SLAVE_IDS );
        return _listSlaveIds( visel.params() );
      }
    }
    return IStringList.EMPTY;
  }

  @Override
  public IStringList listViselSlaveIds( IVedItemCfg aCfg ) {
    IStringList slaveIds = IStringList.EMPTY;
    if( aCfg.params().hasKey( PARAMID_SLAVE_IDS ) ) {
      // return aCfg.params().getValobj( PARAMID_SLAVE_IDS );
      return _listSlaveIds( aCfg.params() );
    }
    return slaveIds;
  }

  private IStringList _listSlaveIds( IOptionSet aOptions ) {
    IStringListEdit result = new StringArrayList();
    if( aOptions.hasKey( PARAMID_SLAVE_IDS ) ) {
      IStringList ids = aOptions.getValobj( PARAMID_SLAVE_IDS );
      return VedScreenUtils.sortViselIdsByZorder( ids, vedScreen );
    }
    return result;
  }

  @Override
  public IStringList listAllSlaveViselIds( String aMasterId ) {
    IStringListEdit result = new StringArrayList();
    addSlaveViselIds( aMasterId, result, new StringArrayList() );
    return result;
  }

  @Override
  public boolean areTheySiblings( IStringList aIds ) {
    String masterId = viselMasterId( aIds.first() );
    for( String id : aIds ) {
      String mi = viselMasterId( id );
      if( mi != null && !mi.equals( masterId ) ) {
        return false;
      }
      if( mi == null && masterId != null ) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void enslaveVisel( String aSubId, String aMasterId ) {
    StringArrayList ids = new StringArrayList( listSlaveViselIds( aMasterId ) );
    ids.add( aSubId );
    // String idsStr = StridUtils.makeIdPath( ids );
    VedAbstractVisel visel = vedScreen.model().visels().list().getByKey( aMasterId );
    // visel.params().setStr( PARAMID_SLAVE_IDS, idsStr );

    visel.params().setValobj( PARAMID_SLAVE_IDS, ids );
    visel.addInterceptor( this );
    visel = vedScreen.model().visels().list().getByKey( aSubId );
    visel.params().setStr( PARAMID_MASTER_ID, aMasterId );

    int masterIdx = vedScreen.model().visels().list().ids().indexOf( aMasterId );
    // vedScreen.model().visels().reorderer().move( visel, masterIdx + 1 );

    System.out.println( "К мастеру: " + aMasterId + " добавлен " + aSubId );
  }

  @Override
  public void freeVisel( String aSubId, String aMasterId ) {
    StringArrayList ids = new StringArrayList( listSlaveViselIds( aMasterId ) );
    ids.remove( aSubId );
    VedAbstractVisel visel = vedScreen.model().visels().list().findByKey( aMasterId );
    if( visel != null ) {
      visel.params().setValobj( PARAMID_SLAVE_IDS, ids );
    }
    visel = VedScreenUtils.findVisel( aSubId, vedScreen );
    if( visel != null ) {
      visel.params().setValue( PARAMID_MASTER_ID, IAtomicValue.NULL );
    }
    VedAbstractVisel subVisel = VedScreenUtils.findVisel( aSubId, vedScreen );
    subVisel.params().setValue( PARAMID_MASTER_ID, IAtomicValue.NULL );

    IStridablesList<VedAbstractVisel> visels = vedScreen.model().visels().list();
    for( VedAbstractVisel v : visels ) {
      StringArrayList subIds = new StringArrayList( listSlaveViselIds( v.id() ) );
      subIds.remove( aSubId );
      v.params().setValobj( PARAMID_SLAVE_IDS, subIds );
    }
  }

  @Override
  public void freeVisel( String aSlaveId ) {
    VedAbstractVisel visel = vedScreen.model().visels().list().findByKey( aSlaveId );
    if( visel != null && visel.params().hasKey( PARAMID_MASTER_ID ) ) {
      IAtomicValue av = visel.params().getValue( PARAMID_MASTER_ID );
      if( av != IAtomicValue.NULL ) {
        String masterId = visel.params().getStr( PARAMID_MASTER_ID );
        freeVisel( aSlaveId, masterId );
      }
    }
  }

  @Override
  public void setMasterId( VedItemCfg aCfg, String aMasterId ) {
    aCfg.params().setStr( PARAMID_MASTER_ID, aMasterId );
  }

  @Override
  public void addSlaveId( VedItemCfg aMasterCfg, String aSlaveId ) {
    IStringListEdit slaveIds = new StringArrayList();
    if( aMasterCfg.params().hasKey( PARAMID_SLAVE_IDS ) ) {
      slaveIds.addAll( (IStringList)aMasterCfg.params().getValobj( PARAMID_SLAVE_IDS ) );
    }
    slaveIds.add( aSlaveId );
    aMasterCfg.params().setValobj( PARAMID_SLAVE_IDS, slaveIds );
  }

  @Override
  public void addSlaveId( String aMasterId, String aSlaveId ) {
    IStringListEdit slaveIds = new StringArrayList();
    VedAbstractVisel master = VedScreenUtils.findVisel( aMasterId, vedScreen );
    if( master != null ) {
      if( master.params().hasKey( PARAMID_SLAVE_IDS ) ) {
        slaveIds.addAll( (IStringList)master.params().getValobj( PARAMID_SLAVE_IDS ) );
      }
      slaveIds.add( aSlaveId );
      master.params().setValobj( PARAMID_SLAVE_IDS, slaveIds );
    }
  }

  @Override
  public void setSlaveIds( VedItemCfg aCfg, IStringList aSlaveIds ) {
    if( !aCfg.params().hasKey( PARAMID_SLAVE_IDS ) && aSlaveIds == IStringList.EMPTY ) {
      return; // если подчиненных не было и новый список пуст, то ничего не делаем
    }
    aCfg.params().setValobj( PARAMID_SLAVE_IDS, aSlaveIds );
  }

  // ------------------------------------------------------------------------------------
  // IVedItemPropertyChangeInterceptor
  //

  @Override
  public void interceptPropsChange( VedAbstractVisel aSource, IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    if( aSource.params().hasKey( PARAMID_SLAVE_IDS ) ) {
      // это родитель
      if( aNewValues.hasKey( PROPID_X ) || aNewValues.hasKey( PROPID_Y ) ) {
        double oldX = aSource.props().getDouble( PROPID_X );
        double oldY = aSource.props().getDouble( PROPID_Y );
        double newX = oldX;
        double newY = oldY;
        if( aNewValues.hasKey( PROPID_X ) ) {
          newX = aNewValues.getDouble( PROPID_X );
        }
        if( aNewValues.hasKey( PROPID_Y ) ) {
          newY = aNewValues.getDouble( PROPID_Y );
        }
        // System.out.println( "interception!!!" );
        IStringList slaveIds = aSource.params().getValobj( PARAMID_SLAVE_IDS );
        for( String slaveId : slaveIds ) {
          VedAbstractVisel visel = VedScreenUtils.findVisel( slaveId, vedScreen );
          if( visel != null ) {
            moveVisel( visel, newX - oldX, newY - oldY );
          }
        }
      }
      if( aNewValues.hasKey( PROPID_ANGLE ) ) {
        ID2Angle angle = aNewValues.getValobj( PROPID_ANGLE );
        ID2Angle oldAngle = aSource.props().getValobj( PROPID_ANGLE );
        rotateChildren( aSource, angle, oldAngle );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedContextMenuCreator
  //

  @Override
  public boolean fillMenu( Menu aMenu, VedAbstractVisel aClickedVisel, ITsPoint aSwtCoors ) {
    clickedVisel = aClickedVisel;
    if( clickedVisel != null ) {
      return menuCreator.fillMenu( aMenu );
    }
    return false;
  }

  @Override
  public void checkAndFix() {
    for( VedAbstractVisel visel : vedScreen.model().visels().list() ) {
      cleanSlaveIds( visel );
    }

  }

  void cleanSlaveIds( VedAbstractVisel aVisel ) {
    IStringList slaveIds = IStringList.EMPTY;
    if( aVisel.params().hasKey( PARAMID_SLAVE_IDS ) ) {
      slaveIds = _listSlaveIds( aVisel.params() );
    }
    if( !slaveIds.isEmpty() ) {
      IStridablesList<VedAbstractVisel> visels = vedScreen.model().visels().list();
      IStringListEdit newSlaveIds = new StringArrayList( slaveIds );
      for( String slaveId : slaveIds ) {
        if( !visels.hasKey( slaveId ) ) {
          newSlaveIds.remove( slaveId );
        }
      }
      if( newSlaveIds.size() != slaveIds.size() ) {
        aVisel.params().setValobj( PARAMID_SLAVE_IDS, newSlaveIds );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает список визуальных элементов в z-порядке соответствующем отношениям родитель-ребенок.<br>
   * Так, например, ребенок не может находится ниже родителя.
   *
   * @return IStridablesListlt;VedAbstractVisel> - список визуальных элементов в z-порядке
   */
  IStridablesList<VedAbstractVisel> listViselsInZorder() {
    IStridablesList<VedAbstractVisel> visels = vedScreen.model().visels().list();
    IStridablesListEdit<VedAbstractVisel> source = new StridablesList<>( vedScreen.model().visels().list() );
    IStridablesListEdit<VedAbstractVisel> result = new StridablesList<>();
    while( source.size() > 0 ) {
      VedAbstractVisel visel = source.first();
      String masterId = viselMasterId( visel.id() );
      if( masterId == null ) {
        result.add( visel );
      }
      else { // ребенок находится раньше родителя
        VedAbstractVisel mv = visels.findByKey( masterId );
        if( mv == null ) { // ошибка - родитель не существует
          freeVisel( visel.id() );
          result.add( visel );
        }
      }
      source.remove( visel );
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  VedAbstractVisel findOwner( String aViselId ) {
    VedAbstractVisel visel = VedScreenUtils.findVisel( aViselId, vedScreen );
    if( visel != null ) {
      return findOwner( visel );
    }
    return null;
  }

  VedAbstractVisel findOwner( VedAbstractVisel aVisel ) {
    IVedCoorsConverter converter = vedScreen.view().coorsConverter();
    IStridablesList<VedAbstractVisel> visels = vedScreen.model().visels().list();
    Iterable<VedAbstractVisel> it = new ListBackwardIterator<>( visels );
    ITsPoint tsp = converter.visel2Swt( ID2Point.ZERO, aVisel );
    for( VedAbstractVisel item : it ) {
      if( !item.id().equals( aVisel.id() ) ) {
        ID2Point d2p = converter.swt2Visel( tsp.x(), tsp.y(), item );
        if( d2p.x() >= 0 && d2p.y() >= 0 && item.bounds().width() > d2p.x() && item.bounds().height() > d2p.y() ) {
          // TODO проверить может ли визель быть owner'ом
          return item;
        }
      }
    }
    return null;
  }

  boolean addSlaveViselIds( String aMasterId, IStringListEdit aViselIds, IStringListEdit aProcessedIds ) {
    if( aProcessedIds.hasElem( aMasterId ) ) {
      System.out.println( "Циклическая ссылка!!!: " + aMasterId );
      return false;
    }
    aProcessedIds.add( aMasterId );
    IStringList slaveIds = listSlaveViselIds( aMasterId );
    aViselIds.addAll( slaveIds );
    for( String id : slaveIds ) {
      if( !addSlaveViselIds( id, aViselIds, aProcessedIds ) ) {
        return false;
      }
    }
    return true;
  }

  void onXZdeleted() {
    // internalUpdate();
  }

  void internalUpdate() {
    for( VedAbstractVisel visel : vedScreen.model().visels().list() ) {
      if( visel.params().hasKey( PARAMID_SLAVE_IDS ) ) {
        visel.addInterceptor( this );
      }
    }
  }

  void moveVisel( VedAbstractVisel aVisel, double aDx, double aDy ) {
    double x = aVisel.props().getDouble( PROPID_X );
    double y = aVisel.props().getDouble( PROPID_Y );
    aVisel.props().setPropPairs( PROPID_X, avFloat( x + aDx ), PROPID_Y, avFloat( y + aDy ) );
  }

  ID2Point rotatePoint( double aX, double aY, double aAlpha, double aCenterX, double aCenterY ) {
    double xNew = aCenterX + (aX - aCenterX) * Math.cos( aAlpha ) - (aY - aCenterY) * Math.sin( aAlpha );
    double yNew = aCenterY + (aX - aCenterX) * Math.sin( aAlpha ) + (aY - aCenterY) * Math.cos( aAlpha );
    return new D2Point( xNew, yNew );
  }

  void enslave() {
    IStringListEdit ids = new StringArrayList();
    IStringList selIds = selectionManager.selectedViselIds();
    if( selIds.size() > 0 ) {
      ids.addAll( selIds );
      clickedVisel = null;
    }
    else {
      if( clickedVisel != null ) {
        ids.add( clickedVisel.id() );
      }
    }
    for( String id : ids ) {
      VedAbstractVisel owner = findOwner( id );
      if( owner != null && owner != clickedVisel ) {
        enslaveVisel( id, owner.id() );
      }
    }
    // vedScreen.model().visels().eventer().fi
  }

  void freeSlave() {
    IStringListEdit ids = new StringArrayList();
    IStringList selIds = selectionManager.selectedViselIds();
    if( selIds.size() > 0 ) {
      ids.addAll( selIds );
    }
    else {
      if( clickedVisel != null ) {
        ids.add( clickedVisel.id() );
      }
    }
    for( String id : ids ) {
      freeVisel( id ); // Удалим ИД мастера из конфигурации раба
    }
    removeSlaveIdsFromMasters( selIds );
  }

  private void removeSlaveIdsFromMasters( IStringList aSlaveIds ) {
    for( VedAbstractVisel visel : vedScreen.model().visels().list() ) {
      if( visel.params().hasKey( PARAMID_SLAVE_IDS ) ) {
        IStringList slaveIds = visel.params().getValobj( PARAMID_SLAVE_IDS );
        IStringListEdit newIds = new StringArrayList();
        boolean changed = false;
        for( String slaveId : aSlaveIds ) {
          if( !slaveIds.hasElem( slaveId ) ) {
            newIds.add( slaveId );
          }
          else {
            changed = true;
          }
        }
        if( changed ) {
          visel.params().setValobj( PARAMID_SLAVE_IDS, newIds );
        }
      }
    }
  }

  VedAbstractVisel clickedVisel = null;

  private void rotateChildren( VedAbstractVisel aVisel, ID2Angle aAngle, ID2Angle aOldAngle ) {
    IStringList slaveIds = aVisel.params().getValobj( PARAMID_SLAVE_IDS );
    ID2Point rp = vedScreen.view().coorsConverter().visel2Screen( aVisel.rotationX(), aVisel.rotationY(), aVisel );
    for( String slaveId : slaveIds ) {
      rotateChild( slaveId, rp.x(), rp.y(), aAngle, aOldAngle );
    }
  }

  private void rotateChild( String aChildId, double aCenterX, double aCenterY, ID2Angle aAngle, ID2Angle aOldAngle ) {
    VedAbstractVisel visel = VedScreenUtils.findVisel( aChildId, vedScreen );
    ID2Point p = vedScreen.view().coorsConverter().visel2Screen( visel.rotationX(), visel.rotationY(), visel );
    p = rotatePoint( p.x(), p.y(), aAngle.radians() - aOldAngle.radians(), aCenterX, aCenterY );
    ID2Point rp = vedScreen.view().coorsConverter().visel2Screen( p.x(), p.y(), visel );
    p = vedScreen.view().coorsConverter().screen2Visel( rp.x(), rp.y(), visel );
    visel.setLocation( p.x(), p.y() );
    visel.props().setValobj( PROP_ANGLE, aAngle );
  }

}
