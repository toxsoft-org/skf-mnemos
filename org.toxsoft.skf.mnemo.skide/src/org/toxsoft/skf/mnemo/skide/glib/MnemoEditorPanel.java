package org.toxsoft.skf.mnemo.skide.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;
import static org.toxsoft.skf.mnemo.skide.glib.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
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
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.tools.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * The mnemoscheme editor panel.
 *
 * @author hazard157
 */
public class MnemoEditorPanel
    extends TsPanel
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

    public AspRunActors( IVedScreen aVedScreen ) {
      super( aVedScreen );
    }

    @Override
    protected void doBeforeActorsRun() {
      // TODO when actors enabled, turn off editing
      // TODO when actors enabled, turn off SAVE, etc.

      vedScreen.model().screenHandlersBefore().remove( vertexSetManager );
      vedScreen.model().screenHandlersBefore().remove( multiSelectionHandler );
      vedScreen.model().screenHandlersBefore().remove( viselsPositionHandler );
      vedScreen.model().screenHandlersBefore().remove( viselCtxMenuManager );
      vedScreen.model().screenHandlersBefore().remove( paletteSelectionManager );

      undoManager.setEnabled( false ); // when actors enabled, turn off UNDO/REDO
    }

    @Override
    protected void doBeforeActorsStop() {
      vedScreen.model().screenHandlersBefore().add( vertexSetManager );
      vedScreen.model().screenHandlersBefore().add( multiSelectionHandler );
      vedScreen.model().screenHandlersBefore().add( viselsPositionHandler );
      vedScreen.model().screenHandlersBefore().add( viselCtxMenuManager );
      vedScreen.model().screenHandlersBefore().add( paletteSelectionManager );
      skVedEnvironment.restart();
      // TODO when actors disabled, turn on editing
      // TODO when actors disabled, turn on SAVE, etc.
    }

    @Override
    protected void doAfterActorsStop() {
      undoManager.setEnabled( true ); // when actors disabled, turn on UNDO/REDO
    }

  }

  private final GenericChangeEventer mnemoChangedEventer;

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
  private final VedViselsCopyPasteManager             copyPasteManager;
  private final VedViselsLayoutManager                layoutManager;
  private final VedViselsDeleteManager                deleteManager;
  private final IVedViselsPositionManager             positionManager;

  private final MultiSelectionDecorator selectionDecorator;
  // private final MultiSelectionAndGroupsDecorator selectionDecorator;

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

  @SuppressWarnings( "unused" )
  private final MnemoScrollManager scrollManager;

  private CLabel labelMasterClass;

  private MnemoResolverConfig resolverConfig = new MnemoResolverConfig();

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MnemoEditorPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    actionsProvider = new CompoundTsActionSetProvider();
    actionsProvider.actionsStateEventer().addListener( s -> updateActionsState() );
    mnemoChangedEventer = new GenericChangeEventer( this );
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );
    //
    // vedScreen = new VedScreen( new TsGuiContext( tsContext() ) );
    vedScreen = new VedScreen( aContext );
    ISkConnectionSupplier skConnSupp = tsContext().get( ISkConnectionSupplier.class );
    skVedEnvironment = new SkVedEnvironment( skConnSupp.defConn() );
    vedScreen.tsContext().put( ISkVedEnvironment.class, skVedEnvironment );

    hotKeysManager = new VedHotKeysManager( vedScreen );
    toolsManager = new VedToolsManager( hotKeysManager, vedScreen );

    copyPasteManager = new VedViselsCopyPasteManager( vedScreen );
    deleteManager = new VedViselsDeleteManager( vedScreen );
    positionManager = new VedViselsPositionManager();

    undoManager = new VedUndoManager( vedScreen );
    // groupsManager = new VedViselGroupsManager( vedScreen.model() );
    // selectionManager = new VedViselSelectionGroupManager( vedScreen, groupsManager );
    selectionManager = new VedViselSelectionManager( vedScreen );

    toolsManager.addTool( new ZOrdererTool( selectionManager, vedScreen ) );

    copyPasteManager.addProcessor( new SelectionCopyPasteProcessor( vedScreen, selectionManager ) );

    masterSlaveManager = new VedViselsMasterSlaveRelationsManager( vedScreen, selectionManager );
    layoutManager = new VedViselsLayoutManager( vedScreen, IVedLayoutFactoriesProvider.DEFAULT, selectionManager,
        masterSlaveManager );
    copyPasteManager.addProcessor( new MasterSlaveCopyPasteProcessor( vedScreen, masterSlaveManager ) );

    deleteManager.addProcessor( new SelectionDeleteProcessor( vedScreen, selectionManager ) );
    deleteManager.addProcessor( new MasterSlaveDeleteProcessor( vedScreen, masterSlaveManager ) );

    positionManager.addProcessor( new SelectedViselsPositionManager( selectionManager ) );
    positionManager.addProcessor( new MasterSlavePositionProcessor( masterSlaveManager ) );

    // selectionManager = new VedViselSelectionManager( vedScreen );
    vertexSetManager = new VedViselVertexSetManager( vedScreen, selectionManager );
    viselsPositionHandler = new VedViselPositionHandler( vedScreen, positionManager );

    copyPasteHandler = new VedViselsCopyPasteHandler( vedScreen, copyPasteManager );
    deleteHandler = new VedViselsDeleteHandler( vedScreen, deleteManager );

    multiSelectionHandler = new VedViselMultiselectionHandler( vedScreen, selectionManager );
    selectionDecorator = new MultiSelectionDecorator( vedScreen, selectionManager );
    // selectionDecorator =
    // new MultiSelectionAndGroupsDecorator( vedScreen, (VedViselSelectionGroupManager)selectionManager );
    vedScreen.model().screenDecoratorsAfter().add( selectionDecorator );

    viselCtxMenuManager = new VedViselContextMenuManager( vedScreen, selectionManager );
    viselCtxMenuManager.addCustomMenuCreator( copyPasteManager );
    viselCtxMenuManager.addCustomMenuCreator( masterSlaveManager );
    viselCtxMenuManager.addCustomMenuCreator( layoutManager );
    viselCtxMenuManager.addCustomMenuCreator( deleteManager );
    // viselCtxMenuManager.addCustomMenuCreator( new VedAspGroupUngroup( vedScreen, selectionManager, groupsManager ) );

    actionsProvider.addHandler( new AspSaveMnemo() );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new VedAspFileImpex( vedScreen ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new AspUndoManager( undoManager ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new VedAspCanvasActions( vedScreen ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    // actionsProvider.addHandler( new AspActorsRunner( vedScreen ) );
    actionsProvider.addHandler( new AspRunActors( vedScreen ) );
    // WEST
    westFolder = new TabFolder( sfMain, SWT.TOP | SWT.BORDER );
    tiObjTree = new TabItem( westFolder, SWT.NONE );
    tiObjTree.setText( STR_TAB_OBJ_TREE );
    tiObjTree.setToolTipText( STR_TAB_OBJ_TREE_D );
    SashForm sfObjTree = new SashForm( westFolder, SWT.VERTICAL );
    panelVisels = new VedPanelViselsList( sfObjTree, new TsGuiContext( tsContext() ), vedScreen );
    panelActors = new VedPanelActorsList( sfObjTree, new TsGuiContext( tsContext() ), vedScreen );
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
    // Composite eastPanel = new Composite( sfMain, SWT.NONE );
    SashForm eastPanel = new SashForm( sfMain, SWT.VERTICAL );

    eastPanel.setLayout( new BorderLayout() );
    Composite eastTopPanel = new Composite( eastPanel, SWT.BORDER );
    eastTopPanel.setLayoutData( BorderLayout.NORTH );
    eastTopPanel.setLayout( new GridLayout( 2, false ) );
    CLabel l = new CLabel( eastTopPanel, SWT.NONE );
    // фыв

    l.setText( "Класс мастер-объекта: " );
    l.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false, 2, 1 ) );
    labelMasterClass = new CLabel( eastTopPanel, SWT.BORDER );
    labelMasterClass.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    Button btnSelectMaster = new Button( eastTopPanel, SWT.PUSH );
    btnSelectMaster.setImage( iconManager().loadStdIcon( ICONID_DOCUMENT_EDIT, EIconSize.IS_16X16 ) );
    btnSelectMaster.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        ISkClassInfo clsInfo = SkGuiUtils.selectClass( null, vedScreen.tsContext() );
        if( clsInfo != null ) {
          labelMasterClass.setText( clsInfo.nmName() );
          Gwid gwid = Gwid.createClass( clsInfo.id() );
          ICompoundResolverConfig resCfg = DirectGwidResolver.createResolverConfig( gwid );
          SubmasterConfig smCfg = SubmasterConfig.create( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID, new OptionSet(), resCfg );
          resolverConfig.subMasters().add( smCfg );
        }
      }
    } );

    PanelSubmastersList submastersPanel = new PanelSubmastersList( eastTopPanel, vedScreen.tsContext() );
    submastersPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );

    // eastFolder = new TabFolder( eastPanel, SWT.BORDER );
    eastFolder = new TabFolder( eastPanel, SWT.NONE );
    eastFolder.setLayoutData( BorderLayout.CENTER );
    // eastFolder = new TabFolder( sfMain, SWT.BORDER );
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
    Composite actMasterComp = new Composite( eastFolder, SWT.NONE );
    actMasterComp.setLayout( new BorderLayout() );
    tiActorInsp.setControl( actMasterComp );
    actorInspector = new VedScreenItemInspector( actMasterComp, vedScreen );
    actorInspector.setLayoutData( BorderLayout.CENTER );

    Composite bkComp = new Composite( actMasterComp, SWT.NONE );
    bkComp.setLayout( new GridLayout( 2, false ) );
    bkComp.setLayoutData( BorderLayout.NORTH );
    Button btn = new Button( bkComp, SWT.PUSH );
    btn.setText( "Мастера..." );
    btn.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        // nop
        SelectMasterPathPanel.edit( null, vedScreen.tsContext() );
      }
    } );
    eastPanel.setWeights( 2, 10 );

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
    String itemId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
    scrCfg.extraData().writeItem( itemId, resolverConfig, MnemoResolverConfig.KEEPER );
    return scrCfg;
  }

  @Override
  public void setCurrentConfig( IVedScreenCfg aCfg ) {
    VedScreenUtils.setVedScreenConfig( vedScreen, aCfg );

    String sectionId = VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF;
    // if( aCfg.extraData().hasSection( sectionId ) ) {
    resolverConfig.subMasters().clear();
    resolverConfig.clearActorSubmasterIds();
    IEntityKeeper<IMnemoResolverConfig> keeper = MnemoResolverConfig.KEEPER;
    resolverConfig = (MnemoResolverConfig)aCfg.extraData().readItem( sectionId, keeper, resolverConfig );
    if( resolverConfig.subMasters().hasKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID ) ) {
      SubmasterConfig smCfg = resolverConfig.subMasters().getByKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID );
      Gwid gwid = DirectGwidResolver.gwid( smCfg.resolverCfg().cfgs().first() );
      labelMasterClass.setText( gwid.classId() );
    }
    // }

    undoManager.reset();
    setChanged( false );
    scrollManager.setOrigin( 300, 300 );
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
