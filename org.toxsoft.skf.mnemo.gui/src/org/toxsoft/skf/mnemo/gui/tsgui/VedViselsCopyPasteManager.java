package org.toxsoft.skf.mnemo.gui.tsgui;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.dnd.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Менеджер копирования/вставки визуальных элементов.
 *
 * @author vs
 */
public class VedViselsCopyPasteManager
    implements IVedViselsCopyPasteManager {

  class CopyPasteActionsProvider
      extends MethodPerActionTsActionSetProvider {

    /**
     * ID of action {@link #ACDEF_COPY}.
     */
    public static final String ACTID_COPY = "ved.copy"; //$NON-NLS-1$

    /**
     * ID of action {@link #ACDEF_PASTE}.
     */
    public static final String ACTID_PASTE = "ved.paste"; //$NON-NLS-1$

    /**
     * Action: copy selected visels and associated actors to the internal buffer.
     */
    public static final ITsActionDef ACDEF_COPY = ofPush2( ACTID_COPY, //
        "Копировать", "Копирует в буфер обмена соответствующие элементы", ICONID_EDIT_COPY );

    /**
     * Action: paste selected visels and associated actors from the internal buffer.
     */
    public static final ITsActionDef ACDEF_PASTE = ofPush2( ACTID_PASTE, //
        "Вставить", "Создает элементы в соответствии с информацией из буффера обмена", ICONID_EDIT_PASTE );

    CopyPasteActionsProvider() {
      defineAction( ACDEF_COPY, VedViselsCopyPasteManager.this::copy );
      defineAction( ACDEF_PASTE, VedViselsCopyPasteManager.this::paste );
    }

    @Override
    protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
      if( aActionDef.id().equals( ACTID_COPY ) ) {
        if( clickedVisel == null && !hasIds4copy() ) {
          return false;
        }
      }
      return true;
    }

  }

  private static Clipboard clipboard;

  private final IVedScreen vedScreen;

  private final IStridablesListEdit<ICopyPasteProcessor> processors = new StridablesList<>();

  private final IStridablesListEdit<IVedItemCfg> visels2paste = new StridablesList<>();

  private final IStridablesListEdit<IVedItemCfg> actors2paste = new StridablesList<>();

  private final IOptionSetEdit params = new OptionSet();

  private final String vedScreenId;

  private final CopyPasteActionsProvider actionsProvider;

  private final MenuCreatorFromAsp menuCreator;

  private VedAbstractVisel clickedVisel = null;

  private ITsPoint pastePoint = null;

  /**
   * Конструктор.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   */
  public VedViselsCopyPasteManager( IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
    String vid = vedScreen.toString();
    vedScreenId = StridUtils.str2id( vid );
    actionsProvider = new CopyPasteActionsProvider();
    menuCreator = new MenuCreatorFromAsp( actionsProvider, vedScreen.tsContext() );
    clipboard = new Clipboard( getDisplay() );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IVedContextMenuCreator
  //

  @Override
  public boolean fillMenu( Menu aMenu, VedAbstractVisel aClickedVisel, ITsPoint aSwtCoors ) {
    clickedVisel = aClickedVisel;
    pastePoint = aSwtCoors;
    return menuCreator.fillMenu( aMenu );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает поставщик действий.
   *
   * @return ITsActionSetProvider - поставщик действий
   */
  public ITsActionSetProvider actionsProvides() {
    return actionsProvider;
  }

  /**
   * Добавляет процессор.<br>
   * Если такой процессор уже есть, то ничего не делает.
   *
   * @param aProcessor {@link ICopyPasteProcessor} - процессор обрабатывающий информацию при копировании/вставке
   */
  public void addProcessor( ICopyPasteProcessor aProcessor ) {
    if( !processors.hasKey( aProcessor.id() ) ) {
      processors.add( aProcessor );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  String DLG_CAP_PASTE_FAILED = "Вставка невозможна";
  String DLG_TIT_NO_FACTORIES = "Отсутсвуют фабрики элементов";

  @Override
  public void copy() {
    visels2paste.clear();
    actors2paste.clear();
    params.clear();

    IStringListEdit viselIds = new StringArrayList();
    if( clickedVisel != null ) {
      viselIds.add( clickedVisel.id() );
    }
    IStringListEdit actorIds = new StringArrayList();

    for( ICopyPasteProcessor p : processors ) {
      p.editIdsForCopy( viselIds, actorIds, params );
    }

    visels2paste.addAll( VedScreenUtils.listViselConfigs( viselIds, vedScreen ) );
    actors2paste.addAll( VedScreenUtils.listActorConfigs( actorIds, vedScreen ) );

    // write data to Clipboard
    TextTransfer textTransfer = TextTransfer.getInstance();
    DtoCopyPasteInfo cbInfo = new DtoCopyPasteInfo( vedScreenId, visels2paste, actors2paste, params );
    String textData = DtoCopyPasteInfo.KEEPER.ent2str( cbInfo );
    clipboard.setContents( new Object[] { textData }, new Transfer[] { textTransfer } );

    clickedVisel = null;
  }

  String STR_ERR_WRONG_CONTENT = "Неверное содержимое буфера";

  @Override
  public void paste() {

    IValResList resList = canPaste();
    if( !resList.isEmpty() ) {
      if( resList.results().size() == 1 ) {
        TsDialogUtils.validationResult( getShell(), resList.results().first() );
      }
      else {
        TsDialogInfo di = new TsDialogInfo( tsContext(), DLG_CAP_PASTE_FAILED, DLG_TIT_NO_FACTORIES );
        TsDialogUtils.showValResList( di, resList );
      }
      pastePoint = null;
      clickedVisel = null;
      return;
    }

    TextTransfer transfer = TextTransfer.getInstance();
    String data = (String)clipboard.getContents( transfer );
    try {
      DtoCopyPasteInfo cbInfo = DtoCopyPasteInfo.KEEPER.str2ent( data );
      visels2paste.clear();
      visels2paste.addAll( cbInfo.viselConfigs() );
      actors2paste.clear();
      actors2paste.addAll( cbInfo.actorConfigs() );
    }
    catch( @SuppressWarnings( "unused" ) Throwable e ) {
      TsDialogUtils.error( getShell(), STR_ERR_WRONG_CONTENT );
      return;
    }

    ID2Point p = calctDeltaPasteLocation();
    double dx = p.x();
    double dy = p.y();

    IListEdit<VedItemCfg> newViselConfigs = new StridablesList<>();
    IListEdit<VedItemCfg> newActorConfigs = new StridablesList<>();

    IVedViselFactoriesRegistry viselFactoriesRegistry = tsContext().get( IVedViselFactoriesRegistry.class );
    IVedActorFactoriesRegistry actorFactoriesRegistry = tsContext().get( IVedActorFactoriesRegistry.class );

    IStridablesListEdit<IVedItemCfg> allViselConfigs = new StridablesList<>();
    IStridablesListEdit<IVedItemCfg> allActorsConfigs = new StridablesList<>();
    allViselConfigs.addAll( VedScreenUtils.listViselConfigs( vedScreen.model().visels().list().ids(), vedScreen ) );
    allActorsConfigs.addAll( VedScreenUtils.listActorConfigs( vedScreen.model().actors().list().ids(), vedScreen ) );

    IStringMapEdit<String> viselsMap = new StringMap<>();
    IStringMapEdit<String> actorsMap = new StringMap<>();

    for( IVedItemCfg oldViselCfg : visels2paste ) {
      IVedViselFactory factory = viselFactoriesRegistry.get( oldViselCfg.factoryId() );
      Pair<String, String> newId = VedScreenUtils.generateIdForItemConfig( oldViselCfg, allViselConfigs, factory );
      VedItemCfg newViselCfg = new VedItemCfg( newId.left(), oldViselCfg );
      newViselCfg.propValues().setStr( PROPID_NAME, newId.right() );
      newViselCfg.propValues().setDouble( PROPID_X, newViselCfg.propValues().getDouble( PROPID_X ) + dx );
      newViselCfg.propValues().setDouble( PROPID_Y, newViselCfg.propValues().getDouble( PROPID_Y ) + dy );
      newViselConfigs.add( newViselCfg );
      allViselConfigs.add( newViselCfg );
      viselsMap.put( oldViselCfg.id(), newViselCfg.id() );

      for( IVedItemCfg cfg : actors2paste ) {
        // Вставим только акторы для текущего визеля
        if( cfg.propValues().getStr( PROPID_VISEL_ID ).equals( oldViselCfg.id() ) ) {
          IVedActorFactory actFactory = actorFactoriesRegistry.get( cfg.factoryId() );
          Pair<String, String> newActId = VedScreenUtils.generateIdForItemConfig( cfg, allActorsConfigs, actFactory );
          VedItemCfg newActorCfg = new VedItemCfg( newActId.left(), cfg );
          newActorCfg.propValues().setStr( PROPID_NAME, newActId.right() );
          String str = newViselCfg.id();
          newActorCfg.propValues().setStr( PROPID_VISEL_ID, str );
          newActorConfigs.add( newActorCfg );
          allActorsConfigs.add( newActorCfg );
          actorsMap.put( cfg.id(), newActorCfg.id() );
        }
      }
    }

    for( ICopyPasteProcessor proc : processors ) {
      proc.editConfigsForPaste( newViselConfigs, newActorConfigs, viselsMap, actorsMap, params );
    }

    // for( VedItemCfg cfg : newActorConfigs ) { // поправим ИДы визелей у акторов
    // if( cfg.propValues().hasKey( PROPID_VISEL_ID ) ) {
    // cfg.propValues().setStr( PROPID_VISEL_ID, actorsMap.getByKey( cfg.propValues().getStr( PROPID_VISEL_ID ) ) );
    // }
    // }

    vedScreen.model().visels().eventer().pauseFiring();
    vedScreen.model().actors().eventer().pauseFiring();

    for( IVedItemCfg cfg : newViselConfigs ) {
      vedScreen.model().visels().create( cfg );
    }
    for( IVedItemCfg cfg : newActorConfigs ) {
      vedScreen.model().actors().create( cfg );
    }

    vedScreen.model().visels().eventer().resumeFiring( true );
    vedScreen.model().actors().eventer().resumeFiring( true );

    pastePoint = null;
    clickedVisel = null;
  }

  String STR_FMT_BUFFER_IS_EMPY   = "Буфер пустой";
  String STR_FMT_NO_VISEL_FACTORY = "Отсутствует фабрика визеля";
  String STR_FMT_NO_ACTOR_FACTORY = "Отсутствует фабрика актора";

  IValResList canPaste() {
    ValResList resList = new ValResList();
    String data = (String)clipboard.getContents( TextTransfer.getInstance() );
    if( data == null ) {
      resList.add( ValidationResult.error( STR_FMT_BUFFER_IS_EMPY ) );
      return resList;
    }
    IVedViselFactoriesRegistry viselFactoriesRegistry = tsContext().get( IVedViselFactoriesRegistry.class );
    IVedActorFactoriesRegistry actorFactoriesRegistry = tsContext().get( IVedActorFactoriesRegistry.class );

    for( IVedItemCfg cfg : visels2paste ) {
      if( viselFactoriesRegistry.find( cfg.factoryId() ) == null ) {
        resList.add( ValidationResult.error( STR_FMT_NO_VISEL_FACTORY, cfg.factoryId() ) );
        LoggerUtils.errorLogger().error( STR_FMT_NO_VISEL_FACTORY + "%s", cfg.factoryId() ); //$NON-NLS-1$
      }
    }
    for( IVedItemCfg cfg : actors2paste ) {
      if( actorFactoriesRegistry.find( cfg.factoryId() ) == null ) {
        resList.add( ValidationResult.error( STR_FMT_NO_ACTOR_FACTORY, cfg.factoryId() ) );
        LoggerUtils.errorLogger().error( STR_FMT_NO_ACTOR_FACTORY + "%s", cfg.factoryId() ); //$NON-NLS-1$
      }
    }
    return resList;
  }

  ID2Point calctDeltaPasteLocation() {
    double dx = 16.;
    double dy = 16.;

    if( pastePoint != null ) {
      // IStringListEdit ids = new StringArrayList();
      // for( IVedItemCfg cfg : visels2paste ) {
      // ids.add( cfg.id() );
      // }
      // ID2Rectangle r = VedScreenUtils.calcGroupScreenRect( ids, vedScreen );
      ID2Rectangle r = VedScreenUtils.calcGroupScreenRect( visels2paste, vedScreen );
      ID2Point sp = vedScreen.view().coorsConverter().swt2Screen( pastePoint );
      dx = sp.x() - r.x1();
      dy = sp.y() - r.y1();
    }

    return new D2Point( dx, dy );
  }

  boolean hasIds4copy() {
    IStringListEdit viselIds = new StringArrayList();
    IStringListEdit actorIds = new StringArrayList();

    for( ICopyPasteProcessor p : processors ) {
      p.editIdsForCopy( viselIds, actorIds, params );
    }
    return viselIds.size() > 0;
  }

}
