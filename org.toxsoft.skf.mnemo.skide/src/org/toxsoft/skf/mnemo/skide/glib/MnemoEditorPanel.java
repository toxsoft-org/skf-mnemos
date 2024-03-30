package org.toxsoft.skf.mnemo.skide.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.skf.mnemo.skide.glib.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
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
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.lib.*;
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

  // private final IVedViselGroupsManager groupsManager;

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

    copyPasteManager = new VedViselsCopyPasteManager( vedScreen );
    deleteManager = new VedViselsDeleteManager( vedScreen );
    positionManager = new VedViselsPositionManager();

    undoManager = new VedUndoManager( vedScreen );
    // groupsManager = new VedViselGroupsManager( vedScreen.model() );
    // selectionManager = new VedViselSelectionGroupManager( vedScreen, groupsManager );
    selectionManager = new VedViselSelectionManager( vedScreen );
    copyPasteManager.addProcessor( new SelectionCopyPasteProcessor( vedScreen, selectionManager ) );

    masterSlaveManager = new VedViselsMasterSlaveRelationsManager( vedScreen );
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
    eastFolder = new TabFolder( sfMain, SWT.BORDER );
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
    actorInspector = new VedScreenItemInspector( eastFolder, vedScreen );
    tiActorInsp.setControl( actorInspector );
    // setup
    initPalette();
    sfMain.setWeights( 2000, 6000, 2000 );
    VedScreenDropTarget dropTarget = new VedScreenDropTarget();
    dropTarget.attachToScreen( vedScreen );
    guiTimersService().quickTimers().addListener( vedScreen );
    guiTimersService().slowTimers().addListener( vedScreen );

    // add VED snippets: user input handler for editing needs
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
    return VedScreenUtils.getVedScreenConfig( vedScreen );
  }

  @Override
  public void setCurrentConfig( IVedScreenCfg aCfg ) {
    VedScreenUtils.setVedScreenConfig( vedScreen, aCfg );
    undoManager.reset();
    setChanged( false );
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
