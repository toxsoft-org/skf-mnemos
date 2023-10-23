package org.toxsoft.skf.mnemo.skide.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.skf.mnemo.skide.glib.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.incub.undoman.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
   */

  private final IVedScreen             vedScreen;
  private final VedObjectsTree         objTree;
  private final VedScreenItemInspector viselInspector;
  private final VedScreenItemInspector actorInspector;
  private final IUndoRedoManager       undoManager = new UndoManager();

  private final IVedViselSelectionManager selectionManager;
  private final VedViselVertexSetManager  vertexSetManager;

  private final TabFolder westFolder;
  private final TabItem   tiObjTree;

  private final TsToolbar        toolbar;
  private final IVedItemsPalette vedPalette;
  private final Canvas           theCanvas;

  private final TabFolder eastFolder;
  private final TabItem   tiViselInsp;
  private final TabItem   tiActorInsp;

  private ITsActionHandler externalHandler = null;
  private boolean          wasChanged      = false;

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
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    sfMain.setLayoutData( BorderLayout.CENTER );
    //
    vedScreen = new VedScreen( tsContext() );
    selectionManager = new VedViselSelectionManager( vedScreen );
    vertexSetManager = new VedViselVertexSetManager( vedScreen, selectionManager );
    // WEST
    westFolder = new TabFolder( sfMain, SWT.TOP | SWT.BORDER );
    tiObjTree = new TabItem( westFolder, SWT.NONE );
    tiObjTree.setText( STR_TAB_OBJ_TREE );
    tiObjTree.setToolTipText( STR_TAB_OBJ_TREE_D );
    objTree = new VedObjectsTree( westFolder, vedScreen );
    tiObjTree.setControl( objTree );
    // CENTER
    Composite centerBoard = new Composite( sfMain, SWT.BORDER );
    centerBoard.setLayout( new BorderLayout() );
    toolbar = TsToolbar.create( centerBoard, tsContext(), //
        ACDEF_SAVE, ACDEF_SEPARATOR, //
        ACDEF_UNDO, ACDEF_REDO, ACDEF_SEPARATOR, //
        ACDEF_ZOOM_IN, ACDEF_ZOOM_ORIGINAL, ACDEF_ZOOM_OUT, ACDEF_SEPARATOR, //
        ACDEF_SEPARATOR //
    );
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
    vedScreen.model().screenHandlersBefore().add( vertexSetManager );
    selectionManager.genericChangeEventer().addListener( aSource -> updateViselInspectorFromSingleSelection() );
    toolbar.addListener( this::processToolbarButton );
    vedScreen.model().actors().eventer().addListener( this::whenVedItemsChanged );
    vedScreen.model().visels().eventer().addListener( this::whenVedItemsChanged );

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

  private void updateViselInspectorFromSingleSelection() {
    String viselId = selectionManager.singleSelectedViselId();
    if( viselId != null ) {
      VedAbstractVisel currItem = vedScreen.model().visels().list().getByKey( viselId );
      viselInspector.setVedItem( currItem );
    }
    else {
      viselInspector.setVedItem( null );
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation: toolbar
  //

  private void processToolbarButton( String aActionId ) {
    switch( aActionId ) {
      case ACTID_SAVE: {
        if( externalHandler != null ) {
          externalHandler.handleAction( aActionId );
        }
        break;
      }
      // TODO MnemoEditorPanel.processToolbarButton()
      default:
        break;
    }
    updateActionsState();
  }

  private void updateActionsState() {
    toolbar.setActionEnabled( ACTID_SAVE, wasChanged );
    toolbar.setActionEnabled( ACTID_UNDO, undoManager.canUndo() );
    toolbar.setActionEnabled( ACTID_REDO, undoManager.canRedo() );
    // TODO MnemoEditorPanel.getControl()
  }

  private void whenVedItemsChanged( IVedItemsManager<?> aSource, ECrudOp aOp, String aId ) {
    wasChanged = true;
    updateActionsState();
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
    wasChanged = false;
    updateActionsState();
  }

}
