package org.toxsoft.skf.mnemo.gui.tsgui.comps;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Визуальный элемент, на котором могут быть размещены другие визуальные (дочерние) элементы.
 * <p>
 * По функциональности аналогичен {@link Composite}.
 *
 * @author vs
 */
public class ViselPanel
    extends VedAbstractVisel {

  private static final String PARAMID_ID = "currentId"; //$NON-NLS-1$

  private static final String PARAMID_OWNER = "onwerId"; //$NON-NLS-1$

  private static final String PARAMID_CHILDREN = "childrenIds"; //$NON-NLS-1$

  /**
   * The VISEL factory ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.Panel"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, "Panel", //
      TSID_DESCRIPTION, "Panel to place other VISELs", //
      TSID_ICON_ID, ICONID_VISEL_PANEL ) {

    // ------------------------------------------------------------------------------------
    // VedAbstractViselFactory
    //

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aScreen ) {
      return new ViselPanel( aCfg, propDefs(), aScreen );
    }

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_FULCRUM );
      fields.add( TFI_TEXT );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_FONT );
      fields.add( TFI_HOR_ALIGNMENT );
      // fields.add( TFI_VER_ALIGNMENT );

      fields.add( TFI_FG_COLOR );

      fields.add( TFI_LEFT_INDENT );
      // fields.add( TFI_TOP_INDENT );
      fields.add( TFI_RIGHT_INDENT );
      // fields.add( TFI_BOTTOM_INDENT );

      fields.add( TFI_BK_FILL );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TFI_IS_ACTIVE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselPanel.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setStr( PROPID_TEXT, "Name" ); //$NON-NLS-1$
      cfg.propValues().setDouble( PROPID_WIDTH, 200.0 );
      cfg.propValues().setDouble( PROPID_HEIGHT, 150.0 );
      cfg.propValues().setValobj( PROPID_HOR_ALIGNMENT, EHorAlignment.CENTER );
      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
    }

  };

  private final IStringListEdit childrenIds = new StringArrayList();

  IVedItemsManagerListener<VedAbstractVisel> vedItemsListener = ( aSource, aOp, aId ) -> {
    if( aOp == ECrudOp.CREATE ) {
      VedAbstractVisel visel = VedScreenUtils.findVisel( aId, vedScreen() );

      if( params().hasKey( PARAMID_ID ) && !params().getStr( PARAMID_ID ).equals( id() ) ) {
        doCopy( visel, aId );
      }

      if( !aId.equals( id() ) && visel != null && !visel.params().hasKey( PARAMID_OWNER ) ) {
        if( isMyChild( visel.bounds().a() ) ) {
          if( !childrenIds.hasElem( visel.id() ) ) {
            int idx = vedScreen().model().visels().list().ids().indexOf( id() );
            vedScreen().model().visels().reorderer().move( visel, idx );
            if( !visel.params().hasKey( PARAMID_OWNER ) ) {
              visel.params().setStr( PARAMID_ID, visel.id() );
              visel.params().setStr( PARAMID_OWNER, id() );
              childrenIds.add( visel.id() );
              params().setStr( PARAMID_CHILDREN, StridUtils.makeIdPath( childrenIds ) );
            }
          }
        }
      }
    }

    if( aOp == ECrudOp.REMOVE ) {
      onViselRemoved( aId );
    }
  };

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselPanel( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );

    if( !aConfig.params().hasKey( PARAMID_ID ) ) {
      params().setStr( PARAMID_ID, id() );
    }

    // vedScreen().model().visels().eventer().addListener( vedItemsListener );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    ID2Rectangle r = bounds();
    aPaintContext.gc().setLineWidth( 2 );
    aPaintContext.gc().setForeground( new Color( 0, 0, 0 ) );
    aPaintContext.setBorderInfo( props().getValobj( PROPID_BORDER_INFO ) );
    aPaintContext.drawRectBorder( 0, 0, (int)r.width(), (int)r.height() );
  }

  @Override
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    if( aNewValues.hasKey( PROPID_X ) || aNewValues.hasKey( PROPID_Y ) ) {
      double dx = 0;
      double dy = 0;
      if( aNewValues.hasKey( PROPID_X ) ) {
        dx = aNewValues.getDouble( PROPID_X ) - props().getDouble( PROPID_X );
      }
      if( aNewValues.hasKey( PROPID_Y ) ) {
        dy = aNewValues.getDouble( PROPID_Y ) - props().getDouble( PROPID_Y );
      }
      moveChildren( dx, dy );
    }
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    if( params().hasKey( PARAMID_CHILDREN ) ) {
      childrenIds.clear();
      String idsStr = params().getStr( PARAMID_CHILDREN );
      if( !idsStr.isBlank() ) {
        childrenIds.addAll( StridUtils.getComponents( idsStr ) );
      }
    }
    if( !params().hasKey( PARAMID_ID ) ) {
      params().setStr( PARAMID_ID, id() );
    }
  }

  @Override
  protected void doDispose() {
    vedScreen().model().visels().eventer().removeListener( vedItemsListener );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  boolean isMyChild( ID2Point aPoint ) {
    IStridablesList<VedAbstractVisel> visels = vedScreen().model().visels().list();
    int minIdx = visels.size() + 1;
    for( VedAbstractVisel v : visels ) {
      if( v.bounds().contains( aPoint ) ) {
        int idx = visels.ids().indexOf( v.id() );
        if( idx < minIdx ) {
          minIdx = idx;
        }
      }
    }
    return minIdx == visels.ids().indexOf( id() );
  }

  private void moveChildren( double aDx, double aDy ) {
    if( Double.compare( aDx, 0 ) == 0 && Double.compare( aDy, 0 ) == 0 ) {
      return;
    }
    for( String id : childrenIds ) {
      VedAbstractVisel visel = VedScreenUtils.findVisel( id, vedScreen() );
      visel.props().propsEventer().pauseFiring();
      double x = visel.props().getDouble( PROPID_X ) + aDx;
      double y = visel.props().getDouble( PROPID_Y ) + aDy;
      visel.props().setPropPairs( PROPID_X, avFloat( x ), PROPID_Y, avFloat( y ) );
      visel.props().propsEventer().resumeFiring( false );
    }
  }

  void onViselRemoved( String aId ) {
    if( !aId.equals( id() ) ) {
      childrenIds.remove( aId );
      String idPath = TsLibUtils.EMPTY_STRING;
      if( !childrenIds.isEmpty() ) {
        idPath = StridUtils.makeIdPath( childrenIds );
      }
      params().setStr( PARAMID_CHILDREN, idPath );
    }
    else {
      for( String id : new StringArrayList( childrenIds ) ) {
        vedScreen().model().visels().remove( id );
        for( String actorId : VedScreenUtils.viselActorIds( id, vedScreen() ) ) {
          vedScreen().model().actors().remove( actorId );
        }
      }
    }
  }

  void doCopy( VedAbstractVisel aVisel, String aViselId ) {
    if( aViselId.equals( id() ) ) { // это я
      for( VedAbstractVisel v : vedScreen().model().visels().list() ) {
        if( shouldProcessVisel( v ) ) {
          processChildVisel( v );
        }
      }
    }
  }

  private boolean shouldProcessVisel( VedAbstractVisel aVisel ) {
    if( aVisel.params().hasKey( PARAMID_ID ) ) {
      String oldId = aVisel.params().getStr( PARAMID_ID );
      if( !oldId.equals( aVisel.id() ) ) {
        if( aVisel.params().hasKey( PARAMID_OWNER ) ) {
          String oldOwnerId = aVisel.params().getStr( PARAMID_OWNER );
          if( params().getStr( PARAMID_ID ).equals( oldOwnerId ) ) {
            return true;
          }
        }
      }
    }
    return false;
  }

  void processChildVisel( VedAbstractVisel aVisel ) {
    String oldId = aVisel.params().getStr( PARAMID_ID );
    childrenIds.remove( oldId );
    childrenIds.add( aVisel.id() );
    params().setStr( PARAMID_CHILDREN, StridUtils.makeIdPath( childrenIds ) );
    aVisel.params().setStr( PARAMID_ID, aVisel.id() );
    aVisel.params().setStr( PARAMID_OWNER, id() );
  }
}
