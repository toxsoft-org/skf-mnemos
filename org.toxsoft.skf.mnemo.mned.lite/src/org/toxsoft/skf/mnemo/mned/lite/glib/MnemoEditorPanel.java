package org.toxsoft.skf.mnemo.mned.lite.glib;

import static org.toxsoft.uskat.core.gui.ISkCoreGuiConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.incub.undoman.*;
import org.toxsoft.core.tsgui.ved.incub.undoman.tsgui.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.asp.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.impl.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.glib.*;

/**
 * Панель, содержащая все элементы, необходимые для загрузки, редактирования и сохранения мнемосхемы.
 *
 * @author vs
 */
public class MnemoEditorPanel
    extends SkPanel
    implements IMnemoEditorPanel {

  private final GenericChangeEventer mnemoChangedEventer;

  private final IdChain suppliedConnectionId;

  private final IVedScreen vedScreen;

  private final SkVedEnvironment skVedEnvironment;

  private boolean internalIsChangedFlag = false;

  private final CompoundTsActionSetProvider actionsProvider;

  private TsToolbar toolbar;

  private RtControlsPaletteBar rtcPalette;

  private Canvas theCanvas;

  private ITsActionHandler externalHandler = null;

  private final VedUndoManager undoManager;

  private RtControlInspector rtControlInspector;

  // ------------------------------------------------------------------------------------
  // Managers
  //

  // private final VedToolsManager toolsManager;
  // private final VedHotKeysManager hotKeysManager;
  private final IVedViselSelectionManager selectionManager;
  // private final IVedViselsMasterSlaveRelationsManager masterSlaveManager;
  private final VedViselVertexSetManager vertexSetManager;
  // private final VedViselContextMenuManager viselCtxMenuManager;
  private final PaletteSelectionManager paletteSelectionManager;
  // private final VedViselsEditManager editManager;
  // private final VedViselsCopyPasteManager copyPasteManager;
  // private final VedViselsLayoutManager layoutManager;
  private final VedViselsDeleteManager    deleteManager;
  private final IVedViselsPositionManager positionManager;
  private final IRtControlsManager        rtControlsManager;

  // ------------------------------------------------------------------------------------
  // Handlers - обработчики пользовательского ввода
  //

  private final VedViselPositionHandler viselsPositionHandler;
  private final VedViselsDeleteHandler  deleteHandler;

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
    suppliedConnectionId = TsNullArgumentRtException.checkNull( aSuppliedConnectionId );
    vedScreen = new VedScreen( aContext );
    ISkConnectionSupplier skConnSupp = tsContext().get( ISkConnectionSupplier.class );
    ISkConnection skConn = skConnSupp.getConn( suppliedConnectionId );
    skVedEnvironment = new SkVedEnvironment( skConn );
    vedScreen.tsContext().put( ISkVedEnvironment.class, skVedEnvironment );
    vedScreen.tsContext().put( ISkConnection.class, skConn );
    setCtxSkConnKey( tsContext(), aSuppliedConnectionId );

    // hotKeysManager = new VedHotKeysManager( vedScreen );
    deleteManager = new VedViselsDeleteManager( vedScreen );

    undoManager = new VedUndoManager( vedScreen );
    rtControlsManager = new RtControlsManager( (VedScreen)vedScreen );

    mnemoChangedEventer = new GenericChangeEventer( this );
    actionsProvider = new CompoundTsActionSetProvider();

    actionsProvider.addHandler( new AspSaveMnemo( this ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new VedAspFileImpex( vedScreen ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new AspUndoManager( undoManager ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );
    actionsProvider.addHandler( new VedAspCanvasActions( vedScreen ) );
    actionsProvider.addHandler( SeparatorTsActionSetProvider.INSTANCE );

    createContent();

    selectionManager = new VedViselSelectionManager( vedScreen );
    vertexSetManager = new VedViselVertexSetManager( vedScreen, selectionManager );
    paletteSelectionManager = new PaletteSelectionManager( vedScreen, rtcPalette, rtControlsManager );

    positionManager = new VedViselsPositionManager();

    viselsPositionHandler = new VedViselPositionHandler( vedScreen, positionManager );

    deleteManager.addProcessor( new SelectionDeleteProcessor( vedScreen, selectionManager ) );
    deleteManager.addProcessor( new RtControlDeleteProcessor( rtControlsManager ) );
    deleteHandler = new VedViselsDeleteHandler( vedScreen, deleteManager );

    // vedScreen.model().screenHandlersBefore().add( hotKeysManager.inputHandler() );
    vedScreen.model().screenHandlersBefore().add( vertexSetManager );
    vedScreen.model().screenHandlersBefore().add( deleteHandler );
    vedScreen.model().screenHandlersBefore().add( viselsPositionHandler );
    vedScreen.model().screenHandlersBefore().add( paletteSelectionManager );

    selectionManager.genericChangeEventer().addListener( aSource -> whenSelectionManagerSelectionChanges() );

  }

  // ------------------------------------------------------------------------------------
  // MnemoEditorPanel
  //

  @Override
  public ITsActionHandler externalHandler() {
    return externalHandler;
  }

  @Override
  public void setExternalHandler( ITsActionHandler aHandler ) {
    externalHandler = aHandler;
  }

  @Override
  public IVedScreenCfg getCurrentConfig() {
    VedScreenCfg scrCfg = VedScreenUtils.getVedScreenConfig( vedScreen );
    ((RtControlsManager)rtControlsManager).updateScreenCfg( scrCfg );
    return scrCfg;
  }

  @Override
  public void setCurrentConfig( IVedScreenCfg aCfg ) {
    // TODO Auto-generated method stub
    VedScreenUtils.setVedScreenConfig( vedScreen, aCfg );
    ((RtControlsManager)rtControlsManager).setScreenCfg( aCfg );
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

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Создает содержимое панели.
   */
  private void createContent() {
    this.setLayout( new BorderLayout() );
    SashForm sfMain = new SashForm( this, SWT.HORIZONTAL );
    sfMain.setSashWidth( 8 );
    sfMain.setLayoutData( BorderLayout.CENTER );

    // CENTER
    Composite centerBoard = new Composite( sfMain, SWT.BORDER );
    centerBoard.setLayout( new BorderLayout() );
    ITsActionDef[] actionDefs = actionsProvider.listHandledActionDefs().toArray( new ITsActionDef[0] );
    toolbar = TsToolbar.create( centerBoard, tsContext(), actionDefs );
    toolbar.addListener( actionsProvider );
    toolbar.getControl().setLayoutData( BorderLayout.NORTH );

    rtcPalette = new RtControlsPaletteBar( centerBoard, SWT.BORDER, vedScreen, true, EIconSize.IS_32X32 );
    rtcPalette.getControl().setLayoutData( BorderLayout.WEST );

    theCanvas = new Canvas( centerBoard, SWT.DOUBLE_BUFFERED | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
    theCanvas.setLayoutData( BorderLayout.CENTER );
    vedScreen.attachTo( theCanvas );

    // EAST
    SashForm eastPanel = new SashForm( sfMain, SWT.VERTICAL );
    eastPanel.setSashWidth( 8 );
    // Composite eastComp = new Composite( eastPanel, SWT.BORDER );
    rtControlInspector = new RtControlInspector( eastPanel, vedScreen );

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

  /**
   * When VISEL selection changes need to update inspector and left list of VISELs.
   */
  private void whenSelectionManagerSelectionChanges() {
    // update VISEL inspector from single selection
    String viselId = selectionManager.singleSelectedViselId();
    IRtControl rtc = null;
    if( viselId != null ) {
      rtc = rtControlsManager.getRtControlByViselId( viselId );
      // selVisel = vedScreen.model().visels().list().getByKey( viselId );
    }
    rtControlInspector.setRtControl( rtc );
    // eastFolder.setSelection( tiViselInsp );
  }

}
