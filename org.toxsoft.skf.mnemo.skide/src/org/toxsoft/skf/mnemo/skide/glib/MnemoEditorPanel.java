package org.toxsoft.skf.mnemo.skide.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.skide.glib.ISkResources.*;
import static org.toxsoft.uskat.core.gui.ISkCoreGuiConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.incub.undoman.*;
import org.toxsoft.core.tsgui.ved.incub.undoman.tsgui.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.asp.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.tools.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.skf.mnemo.gui.utils.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.glib.*;

/**
 * The mnemoscheme editor panel.
 *
 * @author hazard157, vs
 */
public class MnemoEditorPanel
    extends SkPanel
    implements IMnemoEditorPanel {

  /**
   * TODO canvasCfg editing and wasChange=true<br>
   * TODO VISEL and canvas pop-up menu<br>
   */

  /**
   * Action: save mnemoscheme to the {@link ISkMnemosService} - calls external action handler.
   *
   * @author hazard157
   */
  class AspSaveMnemo
      extends AbstractSingleActionSetProvider {

    public AspSaveMnemo() {
      super( ACDEF_SAVE );
    }

    @Override
    public void run() {
      if( externalHandler != null ) {
        externalHandler.handleAction( ACTID_SAVE );
      }
    }

    @Override
    protected boolean doIsActionEnabled() {
      return isChanged();
    }

  }

  /**
   * Runs editor in "live" mode.
   *
   * @author hazard157
   */
  class AspRunActors
      extends AspActorsRunner {

    IVedScreenCfg oldConfig = null;

    public AspRunActors( IVedScreen aVedScreen ) {
      super( aVedScreen );
    }

    @Override
    protected void doBeforeActorsRun() {
      // TODO when actors enabled, turn off editing
      // TODO when actors enabled, turn off SAVE, etc.
      oldConfig = VedScreenUtils.getVedScreenConfig( vedScreen );
      String masterClassId = MasterObjectUtils.findMainMasterClassId( resolverConfig );
      if( masterClassId != null ) { // у мнемосхемы есть мастер-объект
        ISkObject masterObj = SkGuiUtils.selectObject( masterClassId, skConn(), tsContext() );
        if( masterObj != null ) {
          ISimpleResolverFactoriesRegistry resRegistry = tsContext().get( ISimpleResolverFactoriesRegistry.class );
          MnemoMasterObjectManager mmoManager = new MnemoMasterObjectManager( skConn(), resRegistry );
          Ugwi ugwi = UgwiKindSkSkid.makeUgwi( masterObj.classId(), masterObj.strid() );
          IVedScreenCfg scrCfg = VedScreenUtils.getVedScreenConfig( vedScreen );
          IVedScreenCfg newCfg = mmoManager.processMasterObject( ugwi, scrCfg, skConn() );
          VedScreenUtils.setVedScreenConfig( vedScreen, newCfg );
        }
      }

      vedScreen.model().screenHandlersBefore().remove( vertexSetManager );
      vedScreen.model().screenHandlersBefore().remove( multiSelectionHandler );
      vedScreen.model().screenHandlersBefore().remove( viselsPositionHandler );
      vedScreen.model().screenHandlersBefore().remove( viselCtxMenuManager );
      vedScreen.model().screenHandlersBefore().remove( paletteSelectionManager );

      skVedEnvironment.restart();

      undoManager.setEnabled( false ); // when actors enabled, turn off UNDO/REDO
    }

    @Override
    protected void doBeforeActorsStop() {
      vedScreen.model().screenHandlersBefore().add( vertexSetManager );
      vedScreen.model().screenHandlersBefore().add( multiSelectionHandler );
      vedScreen.model().screenHandlersBefore().add( viselsPositionHandler );
      vedScreen.model().screenHandlersBefore().add( viselCtxMenuManager );
      vedScreen.model().screenHandlersBefore().add( paletteSelectionManager );

      // TODO when actors disabled, turn on editing
      // TODO when actors disabled, turn on SAVE, etc.
      VedScreenUtils.setVedScreenConfig( vedScreen, oldConfig );
    }

    @Override
    protected void doAfterActorsStop() {
      undoManager.setEnabled( true ); // when actors disabled, turn on UNDO/REDO
    }

  }

  private final String ACTID_DIAGNOSTICS = "actMnemoDiagnostics"; //$NON-NLS-1$

  /**
   * Action: diagnoses mnemoscheme and show results as popup dialog.
   *
   * @author vs
   */
  class AspDiagnosticMnemo
      extends AbstractSingleActionSetProvider {

    private final IVedScreen vedScr;

    public AspDiagnosticMnemo( IVedScreen aVedScreen ) {
      super( TsActionDef.ofPush2( ACTID_DIAGNOSTICS, "Диагностика", "Диагностика мнемосхемы и отображение результатов",
          ICONID_DIAGNOSTICS ) );
      vedScr = aVedScreen;
    }

    @Override
    public void run() {
      IStridablesList<IVedItem> actors = VedScreenUtils.listHangedActors( vedScr );
      for( IVedItem actor : actors ) {
        System.out.println( "Висячий актор: " + actor.id() );
      }
      actors = MnemoUtils.listWrongUgwiActors( vedScr, coreApi() );
      for( IVedItem actor : actors ) {
        System.out.println( "Wrong UGWI actor: " + actor.id() );
      }

      IStridablesList<IVedVisel> visels = VedScreenUtils.listNonlinkedVisels( vedScr );
      for( IVedVisel visel : visels ) {
        System.out.println( "Не привязанный визель: " + visel.id() );
      }
    }

    @Override
    protected boolean doIsActionEnabled() {
      return true;
    }

  }

  private final GenericChangeEventer mnemoChangedEventer;

  private final IdChain suppliedConnectionId;

  private final IVedScreen             vedScreen;
  private final VedPanelViselsList     panelVisels;
  private final VedPanelActorsList     panelActors;
  private final VedScreenItemInspector viselInspector;
  private final VedScreenItemInspector actorInspector;

  // ------------------------------------------------------------------------------------
  // Handlers - обработчики пользовательского ввода
  //

  private final VedViselPositionHandler       viselsPositionHandler;
  private final VedViselMultiselectionHandler multiSelectionHandler;
  private final VedViselsCopyPasteHandler     copyPasteHandler;
  private final VedViselsDeleteHandler        deleteHandler;

  // ------------------------------------------------------------------------------------
  // Managers
  //

  private final VedToolsManager                       toolsManager;
  private final VedHotKeysManager                     hotKeysManager;
  private final IVedViselSelectionManager             selectionManager;
  private final IVedViselsMasterSlaveRelationsManager masterSlaveManager;
  private final VedViselVertexSetManager              vertexSetManager;
  private final VedViselContextMenuManager            viselCtxMenuManager;
  private final PaletteSelectionManager               paletteSelectionManager;
  private final VedViselsEditManager                  editManager;
  private final VedViselsCopyPasteManager             copyPasteManager;
  private final VedViselsLayoutManager                layoutManager;
  private final VedViselsDeleteManager                deleteManager;
  private final IVedViselsPositionManager             positionManager;

  private final MultiSelectionDecorator selectionDecorator;

  private final SkVedEnvironment skVedEnvironment;

  private final CompoundTsActionSetProvider actionsProvider;

  private final TabFolder westFolder;
  private final TabItem   tiObjTree;

  private final TsToolbar toolbar;
  // private final IVedItemsPalette vedPalette;
  private final VedItemsPaletteBar vedPalette;
  private final Canvas             theCanvas;

  private final TabFolder eastFolder;
  private final TabItem   tiViselInsp;
  private final TabItem   tiActorInsp;

  private ITsActionHandler externalHandler       = null;
  private boolean          internalIsChangedFlag = false;

  private final VedUndoManager undoManager;

  private final MnemoScrollManager scrollManager;

  // private CLabel labelMasterClass;

  private MnemoResolverConfig resolverConfig = new MnemoResolverConfig();

  private MnemoSubmastersPanel submastersPanel;

  private ActorSubmastersPanel actorSubmasters;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aSuppliedConnectionId {@link IdChain} - connection ID or <code>null</code> for default
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MnemoEditorPanel( Composite aParent, ITsGuiContext aContext, IdChain aSuppliedConnectionId ) {
    super( aParent, aContext );
    TsNullArgumentRtException.checkNull( aSuppliedConnectionId );
    suppliedConnectionId = aSuppliedConnectionId;
    actionsProvider = new CompoundTsActionSetProvider();
    actionsProvider.actionsStateEventer().addListener( s -> updateActionsState() );
    mnemoChangedEventer = new GenericChangeEventer( this );
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    sfMain.setSashWidth( 8 );
    sfMain.setLayoutData( BorderLayout.CENTER );
    //
    vedScreen = new VedScreen( aContext );
    ISkConnectionSupplier skConnSupp = tsContext().get( ISkConnectionSupplier.class );
    ISkConnection skConn = skConnSupp.getConn( suppliedConnectionId );
    skVedEnvironment = new SkVedEnvironment( skConn );
    vedScreen.tsContext().put( ISkVedEnvironment.class, skVedEnvironment );
    vedScreen.tsContext().put( ISkConnection.class, skConn );
    setCtxSkConnKey( tsContext(), aSuppliedConnectionId );

    hotKeysManager = new VedHotKeysManager( vedScreen );
    toolsManager = new VedToolsManager( hotKeysManager, vedScreen );

    copyPasteManager = new VedViselsCopyPasteManager( vedScreen );
    deleteManager = new VedViselsDeleteManager( vedScreen );
    positionManager = new VedViselsPositionManager();

    undoManager = new VedUndoManager( vedScreen );
    selectionManager = new VedViselSelectionManager( vedScreen );

    toolsManager.addTool( new ZOrdererTool( selectionManager, vedScreen ) );

    editManager = new VedViselsEditManager( vedScreen, selectionManager );

    copyPasteManager.addProcessor( new SelectionCopyPasteProcessor( vedScreen, selectionManager ) );
    copyPasteManager.addProcessor( new MasterObjectCopyPasteProcessor( vedScreen ) );

    masterSlaveManager = new VedViselsMasterSlaveRelationsManager( vedScreen, selectionManager );
    layoutManager = new VedViselsLayoutManager( vedScreen, IVedLayoutFactoriesProvider.DEFAULT, selectionManager,
        masterSlaveManager );
    copyPasteManager.addProcessor( new MasterSlaveCopyPasteProcessor( vedScreen, masterSlaveManager ) );

    deleteManager.addProcessor( new SelectionDeleteProcessor( vedScreen, selectionManager ) );
    deleteManager.addProcessor( new MasterSlaveDeleteProcessor( vedScreen, masterSlaveManager ) );
    deleteManager.addProcessor( new MasterObjectDeleteProcessor( vedScreen ) );

    positionManager.addProcessor( new SelectedViselsPositionManager( selectionManager ) );
    positionManager.addProcessor( new MasterSlavePositionProcessor( masterSlaveManager ) );

    vertexSetManager = new VedViselVertexSetManager( vedScreen, selectionManager );
    viselsPositionHandler = new VedViselPositionHandler( vedScreen, positionManager );

    copyPasteHandler = new VedViselsCopyPasteHandler( vedScreen, copyPasteManager );
    deleteHandler = new VedViselsDeleteHandler( vedScreen, deleteManager );

    multiSelectionHandler = new VedViselMultiselectionHandler( vedScreen, selectionManager );
    selectionDecorator = new MultiSelectionDecorator( vedScreen, selectionManager );
    vedScreen.model().screenDecoratorsAfter().add( selectionDecorator );

    viselCtxMenuManager = new VedViselContextMenuManager( vedScreen, selectionManager );
    viselCtxMenuManager.addCustomMenuCreator( editManager );
    viselCtxMenuManager.addCustomMenuCreator( copyPasteManager );
    viselCtxMenuManager.addCustomMenuCreator( masterSlaveManager );
    viselCtxMenuManager.addCustomMenuCreator( layoutManager );
    viselCtxMenuManager.addCustomMenuCreator( deleteManager );

    actionsProvider.addHandler( new AspSaveMnemo() );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new VedAspFileImpex( vedScreen ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new AspUndoManager( undoManager ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new VedAspCanvasActions( vedScreen ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new AspRunActors( vedScreen ) );
    actionsProvider.addHandler( new AspDiagnosticMnemo( vedScreen ) );
    // WEST
    westFolder = new TabFolder( sfMain, SWT.TOP | SWT.BORDER );
    tiObjTree = new TabItem( westFolder, SWT.NONE );
    tiObjTree.setText( STR_TAB_OBJ_TREE );
    tiObjTree.setToolTipText( STR_TAB_OBJ_TREE_D );
    SashForm sfObjTree = new SashForm( westFolder, SWT.VERTICAL );
    sfObjTree.setSashWidth( 8 );
    panelVisels = new VedPanelViselsList( sfObjTree, new TsGuiContext( tsContext() ), vedScreen );
    panelActors = new VedPanelActorsList( sfObjTree, new TsGuiContext( tsContext() ), vedScreen, selectionManager );
    sfObjTree.setWeights( 5500, 4500 );
    tiObjTree.setControl( sfObjTree );
    // CENTER
    Composite centerBoard = new Composite( sfMain, SWT.BORDER );
    centerBoard.setLayout( new BorderLayout() );
    toolbar = TsToolbar.create( centerBoard, tsContext(), //
        actionsProvider.listHandledActionDefs().toArray( new ITsActionDef[0] ) );
    toolbar.addListener( actionsProvider );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );
    // vedPalette = new VedItemsSimplePaletteBar( centerBoard, SWT.BORDER, vedScreen, true );
    vedPalette = new VedItemsPaletteBar( centerBoard, SWT.BORDER, vedScreen, true );
    vedPalette.getControl().setLayoutData( BorderLayout.WEST );
    paletteSelectionManager = new PaletteSelectionManager( vedScreen, vedPalette );

    theCanvas = new Canvas( centerBoard, SWT.DOUBLE_BUFFERED | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
    theCanvas.setLayoutData( BorderLayout.CENTER );

    vedScreen.attachTo( theCanvas );
    // EAST
    SashForm eastPanel = new SashForm( sfMain, SWT.VERTICAL );
    eastPanel.setSashWidth( 8 );

    eastPanel.setLayout( new BorderLayout() );
    submastersPanel = new MnemoSubmastersPanel( eastPanel, vedScreen, SWT.BORDER );

    eastFolder = new TabFolder( eastPanel, SWT.NONE );
    eastFolder.setLayoutData( BorderLayout.CENTER );

    tiViselInsp = new TabItem( eastFolder, SWT.NONE );
    tiViselInsp.setText( STR_TAB_VISEL_INSP );
    tiViselInsp.setToolTipText( STR_TAB_VISEL_INSP_D );
    tiViselInsp.setImage( iconManager().loadStdIcon( EVedItemKind.VISEL.iconId(), EIconSize.IS_16X16 ) );
    viselInspector = new VedScreenItemInspector( eastFolder, vedScreen );
    tiViselInsp.setControl( viselInspector );

    tiActorInsp = new TabItem( eastFolder, SWT.NONE );
    tiActorInsp.setText( STR_TAB_ACTOR_INSP );
    tiActorInsp.setToolTipText( STR_TAB_ACTOR_INSP_D );
    tiActorInsp.setImage( iconManager().loadStdIcon( EVedItemKind.ACTOR.iconId(), EIconSize.IS_16X16 ) );

    // Composite actMasterComp = new Composite( eastFolder, SWT.NONE );
    SashForm actMasterComp = new SashForm( eastFolder, SWT.VERTICAL );
    // actMasterComp.setLayout( new BorderLayout() );
    actMasterComp.setSashWidth( 8 );
    actorSubmasters = new ActorSubmastersPanel( actMasterComp, vedScreen, SWT.BORDER );
    eastPanel.setWeights( 3, 10 );

    tiActorInsp.setControl( actMasterComp );
    actorInspector = new VedScreenItemInspector( actMasterComp, vedScreen );
    actMasterComp.setWeights( 3, 10 );

    // actorInspector.setLayoutData( BorderLayout.CENTER );

    // Composite bkComp = new Composite( actMasterComp, SWT.NONE );
    // bkComp.setLayout( new GridLayout( 2, false ) );
    // bkComp.setLayoutData( BorderLayout.NORTH );
    // Button btn = new Button( bkComp, SWT.PUSH );
    // btn.setText( "Мастера..." );
    // btn.addSelectionListener( new SelectionAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent aEvent ) {
    // // nop
    // PanelCompoundResolverConfig.edit( null, vedScreen.tsContext() );
    // }
    // } );

    // actorInspector = new VedScreenItemInspector( eastFolder, vedScreen );
    // tiActorInsp.setControl( actorInspector );
    // setup
    initPalette();
    sfMain.setWeights( 2000, 6000, 2000 );
    VedScreenDropTarget dropTarget = new VedScreenDropTarget();
    dropTarget.attachToScreen( vedScreen );
    guiTimersService().quickTimers().addListener( vedScreen );
    guiTimersService().slowTimers().addListener( vedScreen );

    // add VED snippets: user input handler for editing needs
    vedScreen.model().screenHandlersBefore().add( hotKeysManager.inputHandler() );
    vedScreen.model().screenHandlersBefore().add( vertexSetManager );
    vedScreen.model().screenHandlersBefore().add( multiSelectionHandler );
    vedScreen.model().screenHandlersBefore().add( viselsPositionHandler );
    vedScreen.model().screenHandlersBefore().add( copyPasteHandler );
    vedScreen.model().screenHandlersBefore().add( deleteHandler );
    vedScreen.model().screenHandlersBefore().add( viselCtxMenuManager );
    vedScreen.model().screenHandlersBefore().add( paletteSelectionManager );

    selectionManager.genericChangeEventer().addListener( aSource -> whenSelectionManagerSelectionChanges() );
    toolbar.addListener( actionsProvider );
    vedScreen.model().actors().eventer().addListener( ( src, op, id ) -> whenVedItemChanged() );
    vedScreen.model().visels().eventer().addListener( ( src, op, id ) -> whenVedItemChanged() );
    vedScreen.view().configChangeEventer().addListener( src -> setChanged( true ) );
    panelVisels.addTsSelectionListener( ( src, sel ) -> whenPanelViselsSelectionChanges( sel ) );
    panelActors.addTsSelectionListener( ( src, sel ) -> whenPanelActorsSelectionChanges( sel ) );

    scrollManager = new MnemoScrollManager( vedScreen );

    vedScreen.setActorsEnabled( false );
    updateActionsState();
  }

  @Override
  protected void doDispose() {
    skVedEnvironment.close();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Adds all items from the VISELs and actors factory registries to the {@link #vedPalette}.
   */
  private void initPalette() {
    IVedViselFactoriesRegistry vfReg = tsContext().get( IVedViselFactoriesRegistry.class );
    for( IVedViselFactory f : vfReg.items() ) {
      for( IVedItemsPaletteEntry e : f.paletteEntries() ) {
        vedPalette.addEntry( e );
      }
    }
    IVedActorFactoriesRegistry afReg = tsContext().get( IVedActorFactoriesRegistry.class );
    for( IVedActorFactory f : afReg.items() ) {
      for( IVedItemsPaletteEntry e : f.paletteEntries() ) {
        vedPalette.addEntry( e );
      }
    }
  }

  /**
   * Called when any VED item (VISEL, actor) property changes.
   * <p>
   * Calls {@link #setChanged(boolean) setChanged(true)} if necessary.
   */
  private void whenVedItemChanged() {
    if( !vedScreen.isActorsEnabled() ) {
      setChanged( true );
    }
  }

  /**
   * When VISEL selection changes need to update inspector and left list of VISELs.
   */
  private void whenSelectionManagerSelectionChanges() {
    // update VISEL inspector from single selection
    VedAbstractVisel selVisel = null;
    String viselId = selectionManager.singleSelectedViselId();
    if( viselId != null ) {
      selVisel = vedScreen.model().visels().list().getByKey( viselId );
      viselInspector.setVedItem( selVisel );
      eastFolder.setSelection( tiViselInsp );
    }
    else {
      viselInspector.setVedItem( null );
    }
    // update panelVisels selection
    panelVisels.setSelectedItem( selVisel );
  }

  /**
   * Called when user selects VISEL in {@link #panelVisels}, tell news to the selection manager.
   *
   * @param aVisel {@link IVedVisel} - selected VISEL or <code>null</code>
   */
  private void whenPanelViselsSelectionChanges( IVedVisel aVisel ) {
    String viselId = aVisel != null ? aVisel.id() : null;
    if( selectionManager.selectionKind() != ESelectionKind.MULTI ) {
      selectionManager.setSingleSelectedViselId( viselId );
    }
  }

  /**
   * Called when user selects actor in {@link #panelActors}, tell news to the actor inspector.
   *
   * @param aActor {@link IVedActor} - selected actor or <code>null</code>
   */
  private void whenPanelActorsSelectionChanges( IVedActor aActor ) {
    actorInspector.setVedItem( aActor );
    actorSubmasters.setActor( aActor );
    if( aActor != null ) {
      eastFolder.setSelection( tiActorInsp );
    }
  }

  /**
   * Updates toolbar actions state.
   */
  private void updateActionsState() {
    for( String aid : actionsProvider.listHandledActionIds() ) {
      toolbar.setActionEnabled( aid, actionsProvider.isActionEnabled( aid ) );
      toolbar.setActionChecked( aid, actionsProvider.isActionChecked( aid ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // IMnemoEditorPanel
  //

  @Override
  public void setExternelHandler( ITsActionHandler aHandler ) {
    externalHandler = aHandler;
  }

  @Override
  public IVedScreenCfg getCurrentConfig() {
    VedScreenCfg scrCfg = VedScreenUtils.getVedScreenConfig( vedScreen );
    return scrCfg;
  }

  @Override
  public void setCurrentConfig( IVedScreenCfg aCfg ) {
    ISimpleResolverFactoriesRegistry resRegistry = tsContext().get( ISimpleResolverFactoriesRegistry.class );
    MnemoMasterObjectManager mmoManager = new MnemoMasterObjectManager( skConn(), resRegistry );
    Ugwi ugwi = UgwiKindSkSkid.makeUgwi( "gbh.TurboCompressor", "turboCompressor1" );
    // IVedScreenCfg newCfg = mmoManager.processMasterObject( ugwi, aCfg, skConn );
    // VedScreenUtils.setVedScreenConfig( vedScreen, newCfg );

    VedScreenUtils.setVedScreenConfig( vedScreen, aCfg );

    resolverConfig = MasterObjectUtils.readMnemoResolverConfig( aCfg );
    submastersPanel.setMnemoResolverConfig( resolverConfig );

    undoManager.reset();
    setChanged( false );
    scrollManager.setOrigin( 0, 0 );
  }

  @Override
  public boolean isChanged() {
    return internalIsChangedFlag;
  }

  @Override
  public void setChanged( boolean aState ) {
    if( internalIsChangedFlag != aState ) {
      internalIsChangedFlag = aState;
      mnemoChangedEventer.fireChangeEvent();
      updateActionsState();
    }
  }

  @Override
  public IGenericChangeEventer mnemoChangedEventer() {
    return mnemoChangedEventer;
  }

}
