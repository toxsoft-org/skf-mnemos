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
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
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
  static class AspRunActors
      extends AspActorsRunner {

    public AspRunActors( IVedScreen aVedScreen ) {
      super( aVedScreen );
    }

    @Override
    protected void doAfterActionHandled( String aActionId ) {
      // TODO when actors enabled, turn on editing, screen redraw, UNDO, SAVE, etc.
    }

    @Override
    protected void doBeforeActorsStopActorsRun() {
      // TODO when actors disabled, turn off editing, screen redraw, UNDO, SAVE, etc.
    }

  }

  // class AspLocal
  // extends MethodPerActionTsActionSetProvider {
  //
  // public AspLocal() {
  // defineAction( ACDEF_SAVE, this::doHandleSave, this::isEnabledSave );
  // defineAction( ACDEF_ENABLE_ACTORS_CHECK, this::doHandleEnableActors, IBooleanState.ALWAY_TRUE,
  // this::isCheckedEnableActors );
  // }
  //
  // void doHandleSave() {
  // if( externalHandler != null ) {
  // externalHandler.handleAction( ACTID_SAVE );
  // }
  // }
  //
  // boolean isEnabledSave() {
  // return isChanged();
  // }
  //
  // void doHandleEnableActors() {
  // boolean enable = !vedScreen.isActorsEnabled();
  // if( enable ) {
  // // TODO when actors enabled, turn on editing, screen redraw, UNDO, SAVE, etc.
  // skVedEnvironment.restart();
  // vedScreen.setActorsEnabled( true );
  // }
  // else {
  // // TODO when actors disabled, turn off editing, screen redraw, UNDO, SAVE, etc.
  // vedScreen.setActorsEnabled( false );
  // skVedEnvironment.close();
  // }
  // }
  //
  // boolean isCheckedEnableActors() {
  // return vedScreen.isActorsEnabled();
  // }
  //
  // }

  private final GenericChangeEventer mnemoChangedEventer;

  private final IVedScreen             vedScreen;
  private final VedPanelViselsList     panelVisels;
  private final VedPanelActorsList     panelActors;
  private final VedScreenItemInspector viselInspector;
  private final VedScreenItemInspector actorInspector;
  // private final IUndoRedoManager undoManager = new UndoManager();

  private final IVedViselSelectionManager     selectionManager;
  private final VedViselVertexSetManager      vertexSetManager;
  private final VedViselPositionManager       viselPositionManager;
  private final VedViselMultiselectionManager multiSelectionManager;
  private final VedViselContextMenuManager    viselCtxMenuManager;

  private final SkVedEnvironment skVedEnvironment;

  private final CompoundTsActionSetProvider actionsProvider;

  private final TabFolder westFolder;
  private final TabItem   tiObjTree;

  private final TsToolbar        toolbar;
  private final IVedItemsPalette vedPalette;
  private final Canvas           theCanvas;

  private final TabFolder eastFolder;
  private final TabItem   tiViselInsp;
  private final TabItem   tiActorInsp;

  private ITsActionHandler externalHandler           = null;
  private boolean          internalContentChangeFlag = false;

  private final VedUndoManager undoManager;

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
    vedScreen = new VedScreen( new TsGuiContext( tsContext() ) );
    ISkConnectionSupplier skConnSupp = tsContext().get( ISkConnectionSupplier.class );
    skVedEnvironment = new SkVedEnvironment( skConnSupp.defConn() );
    vedScreen.tsContext().put( ISkVedEnvironment.class, skVedEnvironment );
    selectionManager = new VedViselSelectionManager( vedScreen );
    vertexSetManager = new VedViselVertexSetManager( vedScreen, selectionManager );
    viselPositionManager = new VedViselPositionManager( vedScreen, selectionManager );
    multiSelectionManager = new VedViselMultiselectionManager( vedScreen, selectionManager );
    viselCtxMenuManager = new VedViselContextMenuManager( vedScreen, selectionManager );

    undoManager = new VedUndoManager( vedScreen );

    actionsProvider.addHandler( new AspSaveMnemo() );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new VedAspFileImpex( vedScreen ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new AspUndoManager( undoManager ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new VedAspCanvasActions( vedScreen ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new AspActorsRunner( vedScreen ) );
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
    vedPalette = new VedItemsSimplePaletteBar( centerBoard, SWT.BORDER, vedScreen, true );
    vedPalette.getControl().setLayoutData( BorderLayout.WEST );
    theCanvas = new Canvas( centerBoard, SWT.DOUBLE_BUFFERED | SWT.BORDER );
    theCanvas.setLayoutData( BorderLayout.CENTER );
    vedScreen.attachTo( theCanvas );
    // EAST
    eastFolder = new TabFolder( sfMain, SWT.BORDER );
    tiViselInsp = new TabItem( eastFolder, SWT.NONE );
    tiViselInsp.setText( STR_TAB_VISEL_INSP );
    tiViselInsp.setToolTipText( STR_TAB_VISEL_INSP_D );
    viselInspector = new VedScreenItemInspector( eastFolder, vedScreen );
    tiViselInsp.setControl( viselInspector );
    tiActorInsp = new TabItem( eastFolder, SWT.NONE );
    tiActorInsp.setText( STR_TAB_ACTOR_INSP );
    tiActorInsp.setToolTipText( STR_TAB_ACTOR_INSP_D );
    actorInspector = new VedScreenItemInspector( eastFolder, vedScreen );
    tiActorInsp.setControl( actorInspector );
    // setup
    initPalette();
    sfMain.setWeights( 2000, 6000, 2000 );
    VedScreenDropTarget dropTarget = new VedScreenDropTarget();
    dropTarget.attachToScreen( vedScreen );
    guiTimersService().quickTimers().addListener( vedScreen );
    guiTimersService().slowTimers().addListener( vedScreen );

    // установим обработчики пользовательского ввода
    vedScreen.model().screenHandlersBefore().add( vertexSetManager );
    vedScreen.model().screenHandlersBefore().add( multiSelectionManager );
    vedScreen.model().screenHandlersBefore().add( viselPositionManager );
    vedScreen.model().screenHandlersBefore().add( viselCtxMenuManager );

    selectionManager.genericChangeEventer().addListener( aSource -> whenSelectionManagerSelectionChanges() );
    toolbar.addListener( actionsProvider );
    vedScreen.model().actors().eventer().addListener( this::whenVedItemsChanged );
    vedScreen.model().visels().eventer().addListener( this::whenVedItemsChanged );
    panelVisels.addTsSelectionListener( ( src, sel ) -> whenPanelViselsSelectionChanges( sel ) );
    panelActors.addTsSelectionListener( ( src, sel ) -> whenPanelActorsSelectionChanges( sel ) );

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
   * Called when user selects VISEL in {@link #panelActors}.
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
   * Called when user selects actor in {@link #panelActors}.
   *
   * @param aActor {@link IVedActor} - selected actor or <code>null</code>
   */
  private void whenPanelActorsSelectionChanges( IVedActor aActor ) {
    actorInspector.setVedItem( aActor );
    if( aActor != null ) {
      eastFolder.setSelection( tiActorInsp );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation: toolbar
  //

  // private void processToolbarButton( String aActionId ) {
  // switch( aActionId ) {
  // case ACTID_SAVE: {
  // if( externalHandler != null ) {
  // externalHandler.handleAction( aActionId );
  // }
  // break;
  // }
  // case ACTID_ENABLE_ACTORS: {
  // boolean enable = !vedScreen.isActorsEnabled();
  // if( enable ) {
  // // TODO when actors enabled, turn on editing, screen redraw, UNDO, SAVE, etc.
  // skVedEnvironment.restart();
  // vedScreen.setActorsEnabled( true );
  // }
  // else {
  // // TODO when actors disabled, turn off editing, screen redraw, UNDO, SAVE, etc.
  // vedScreen.setActorsEnabled( false );
  // skVedEnvironment.close();
  // }
  // break;
  // }
  // default:
  // break;
  // }
  // updateActionsState();
  // }

  private void updateActionsState() {
    for( String aid : actionsProvider.listHandledActionIds() ) {
      toolbar.setActionEnabled( aid, actionsProvider.isActionEnabled( aid ) );
      toolbar.setActionChecked( aid, actionsProvider.isActionChecked( aid ) );
    }
  }

  @SuppressWarnings( "unused" )
  private void whenVedItemsChanged( IVedItemsManager<?> aSource, ECrudOp aOp, String aId ) {
    setChanged( true );
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
    return VedEditorUtils.getVedScreenConfig( vedScreen );
  }

  @Override
  public void setCurrentConfig( IVedScreenCfg aCfg ) {
    VedEditorUtils.setVedScreenConfig( vedScreen, aCfg );
    undoManager.reset();
    setChanged( false );
  }

  @Override
  public boolean isChanged() {
    return internalContentChangeFlag;
  }

  @Override
  public void setChanged( boolean aState ) {
    if( internalContentChangeFlag != aState ) {
      internalContentChangeFlag = aState;
      mnemoChangedEventer.fireChangeEvent();
      updateActionsState();
    }
  }

  @Override
  public IGenericChangeEventer mnemoChangedEventer() {
    return mnemoChangedEventer;
  }

}
