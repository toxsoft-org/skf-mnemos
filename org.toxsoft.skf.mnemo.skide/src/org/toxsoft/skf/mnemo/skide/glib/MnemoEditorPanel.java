package org.toxsoft.skf.mnemo.skide.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.skf.mnemo.skide.glib.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
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
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
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

  /**
   * Zoom factor increase/decrease amount.
   */
  private static final double deltaZoom = Math.sqrt( Math.sqrt( 2.0 ) ); // 4 times zoom means 2 times larger/smaller

  private final GenericChangeEventer mnemoChangedEventer;

  private final IVedScreen             vedScreen;
  private final VedPanelViselsList     panelVisels;
  private final VedPanelActorsList     panelActors;
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

  private ITsActionHandler externalHandler           = null;
  private boolean          internalContentChangeFlag = false;

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
    mnemoChangedEventer = new GenericChangeEventer( this );
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
    SashForm sfObjTree = new SashForm( westFolder, SWT.VERTICAL );
    panelVisels = new VedPanelViselsList( sfObjTree, new TsGuiContext( tsContext() ), vedScreen );
    panelActors = new VedPanelActorsList( sfObjTree, new TsGuiContext( tsContext() ), vedScreen );
    sfObjTree.setWeights( 5500, 4500 );
    tiObjTree.setControl( sfObjTree );
    // CENTER
    Composite centerBoard = new Composite( sfMain, SWT.BORDER );
    centerBoard.setLayout( new BorderLayout() );
    toolbar = TsToolbar.create( centerBoard, tsContext(), //
        ACDEF_SAVE, ACDEF_SEPARATOR, //
        ACDEF_UNDO, ACDEF_REDO, ACDEF_SEPARATOR, //
        ACDEF_ZOOM_IN, ACDEF_ZOOM_ORIGINAL_PUSHBUTTON, ACDEF_ZOOM_OUT, ACDEF_SEPARATOR, //
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
    panelVisels.addTsSelectionListener( ( src, sel ) -> whenPanelViselsSelectionChanges( sel ) );
    panelActors.addTsSelectionListener( ( src, sel ) -> whenPanelActorsSelectionChanges( sel ) );

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
      eastFolder.setSelection( tiViselInsp );
    }
    else {
      viselInspector.setVedItem( null );
    }
  }

  /**
   * Called when user selects VISEL in {@link #panelActors}.
   *
   * @param aVisel {@link IVedVisel} - selected VISEL or <code>null</code>
   */
  private void whenPanelViselsSelectionChanges( IVedVisel aVisel ) {
    String viselId = aVisel != null ? aVisel.id() : null;
    selectionManager.setSingleSelectedViselId( viselId );
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

  private void processToolbarButton( String aActionId ) {
    switch( aActionId ) {
      case ACTID_SAVE: {
        if( externalHandler != null ) {
          externalHandler.handleAction( aActionId );
        }
        break;
      }
      case ACTID_UNDO: {
        if( undoManager.canUndo() ) {
          undoManager.undo();
        }
        break;
      }
      case ACTID_REDO: {
        if( undoManager.canRedo() ) {
          undoManager.redo();
        }
        break;
      }
      case ACTID_ZOOM_IN: {
        D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
        d2conv.setZoomFactor( D2Utils.ZOOM_RANGE.inRange( d2conv.zoomFactor() * deltaZoom ) );
        vedScreen.view().setConversion( d2conv );
        vedScreen.view().redraw();
        break;
      }
      case ACTID_ZOOM_ORIGINAL: {
        double originalZoom = vedScreen.view().canvasConfig().conversion().zoomFactor();
        D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
        d2conv.setZoomFactor( D2Utils.ZOOM_RANGE.inRange( originalZoom ) );
        vedScreen.view().setConversion( d2conv );
        vedScreen.view().redraw();
        break;
      }
      case ACTID_ZOOM_OUT: {
        D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
        d2conv.setZoomFactor( D2Utils.ZOOM_RANGE.inRange( d2conv.zoomFactor() / deltaZoom ) );
        vedScreen.view().setConversion( d2conv );
        vedScreen.view().redraw();
        break;
      }
      default:
        break;
    }
    updateActionsState();
  }

  private void updateActionsState() {
    toolbar.setActionEnabled( ACTID_SAVE, isChanged() );
    toolbar.setActionEnabled( ACTID_UNDO, undoManager.canUndo() );
    toolbar.setActionEnabled( ACTID_REDO, undoManager.canRedo() );
    double zoomFactor = vedScreen.view().getConversion().zoomFactor();
    double originalZoom = vedScreen.view().canvasConfig().conversion().zoomFactor();
    toolbar.setActionEnabled( ACTID_ZOOM_IN, zoomFactor < D2Utils.ZOOM_RANGE.maxValue() );
    toolbar.setActionEnabled( ACTID_ZOOM_ORIGINAL, zoomFactor != originalZoom );
    toolbar.setActionEnabled( ACTID_ZOOM_OUT, zoomFactor > D2Utils.ZOOM_RANGE.minValue() );
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
