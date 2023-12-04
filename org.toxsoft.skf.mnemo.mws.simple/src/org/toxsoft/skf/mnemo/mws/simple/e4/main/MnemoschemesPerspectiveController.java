package org.toxsoft.skf.mnemo.mws.simple.e4.main;

import static org.toxsoft.skf.mnemo.mws.simple.IMnemoMwsSimpleConstants.*;
import static org.toxsoft.skf.mnemo.mws.simple.IMnemoMwsSimpleSharedResources.*;

import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.commands.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.e4.ui.workbench.renderers.swt.*;
import org.eclipse.jface.action.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.mws.e4.helpers.partman.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.lib.impl.*;
import org.toxsoft.skf.mnemo.mws.simple.*;
import org.toxsoft.skf.mnemo.mws.simple.e4.uiparts.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * {@link IMnemoschemesPerspectiveController} implementation.
 *
 * @author hazard157
 */
public class MnemoschemesPerspectiveController
    implements IMnemoschemesPerspectiveController, ISkConnected, ITsGuiContextable {

  private final ISkConnection skConn;
  private final ITsGuiContext tsContext;

  private final ITsPartStackManager partStackManager;

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - used connection
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MnemoschemesPerspectiveController( ISkConnection aConn, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aConn, aContext );
    skConn = aConn;
    tsContext = aContext;
    partStackManager = new TsPartStackManager( eclipseContext(), PARTSTACKID_MNEMOS_MAIN );
    skConn.addConnectionListener( this::whenConnStateChanged );
    if( skConn.state().isOpen() ) {
      processConnectionGetsOpen();
    }
    updateMnemoschemesListGui();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ISkMnemosService mnemoServ() {
    if( !coreApi().services().hasKey( ISkMnemosService.SERVICE_ID ) ) {
      coreApi().addService( SkMnemosService.CREATOR );
    }
    return coreApi().getService( ISkMnemosService.SERVICE_ID );
  }

  @SuppressWarnings( "restriction" ) // needed for MenuManagerRenderer, see comment below. in method body
  private void refreshMainMenuMnemoschemesList( IList<ISkMnemoCfg> aList ) {
    // find main menu item
    MApplication mApp = tsContext.find( MApplication.class );
    if( mApp == null ) { // TODO WTF when closing the application ? without this check an exception occurs
      return; // to avoid exception when application closes
    }
    MWindow mainWindow = tsContext.get( MWindow.class );
    EModelService modelService = tsContext.get( EModelService.class );
    MMenu mmnuMnemosList =
        e4Helper().findElement( mainWindow, MMENUID_MNEMOS_LIST, MMenu.class, EModelService.IN_MAIN_MENU );
    // first, clear whole sub-menu
    mmnuMnemosList.getChildren().clear(); // clear the E4 menu
    /**
     * GOGA: there is the E4 bug - when removing menu items from E4 MMenu, they remain in SWT Menu. SO we need to clear
     * SWT menu also. That's why MenuanagerRenderer is used here. <br>
     * Note: at early stage of application initialization renderer is <code>null</code>. <br>
     * Links:<br>
     * https://stackoverflow.com/questions/32160827/change-menus-and-menu-items-programmatically-in-eclipse-e4 <br>
     * https://bugs.eclipse.org/bugs/show_bug.cgi?id=365724
     */
    MenuManagerRenderer renderer = (MenuManagerRenderer)mmnuMnemosList.getRenderer();
    if( renderer != null ) { // also clear SWT menu
      MenuManager mgr = renderer.getManager( mmnuMnemosList );
      mgr.removeAll();
    }
    // if no mnemos exist, just add one disable menu item
    if( aList.isEmpty() ) {
      MHandledMenuItem mItem = modelService.createModelElement( MHandledMenuItem.class );
      mItem.setLabel( STR_WARN_NO_MNEMOS_IN_USKAT );
      mmnuMnemosList.getChildren().add( 0, mItem );
      return;
    }
    for( ISkMnemoCfg mnemoCfg : aList ) {
      MHandledMenuItem mItem = modelService.createModelElement( MHandledMenuItem.class );
      MCommand cmd = e4Helper().findElement( mApp, CMDID_OPEN_MNEMO_BY_ID, MCommand.class, EModelService.ANYWHERE );
      mItem.setCommand( cmd );
      MParameter cmdParam = modelService.createModelElement( MParameter.class );
      cmdParam.setElementId( CMDARGID_MNEMO_ID );
      cmdParam.setName( CMDARGID_MNEMO_ID );
      cmdParam.setValue( mnemoCfg.strid() );
      mItem.getParameters().add( cmdParam );
      // mItem.setIconURI( makeTsguiIconUri( ICONID_DOCUMENT_SAVE_AS, INITIAL_MENU_ICON_SIZE ) );
      mItem.setLabel( mnemoCfg.nmName() );
      mItem.setTooltip( StridUtils.printf( StridUtils.FORMAT_ID_DESCRITPTION, mnemoCfg ) );
      mmnuMnemosList.getChildren().add( mItem );
    }
  }

  private void updateMnemoschemesListGui() {
    IList<ISkMnemoCfg> mnemosList = IList.EMPTY;
    if( skConn.state().isActive() ) {
      mnemosList = mnemoServ().listMnemosCfgs();
    }
    refreshMainMenuMnemoschemesList( mnemosList );
  }

  private void processConnectionGetsOpen() {
    mnemoServ().eventer().addListener( ( aCoreApi, aOp, aMnemoId ) -> updateMnemoschemesListGui() );
  }

  private void whenConnStateChanged( ISkConnection aSource, ESkConnState aOldState ) {
    ESkConnState state = aSource.state();
    if( state.isOpen() && state.isOpen() != aOldState.isOpen() ) { // when connection becomes open
      processConnectionGetsOpen();
    }
    updateMnemoschemesListGui();
  }

  // ------------------------------------------------------------------------------------
  // IMnemoschemesPerspectiveController
  //

  @Override
  public void openMnemoscheme( String aMnemoId ) {
    e4Helper().switchToPerspective( PERSPID_MENMOS_MAIN, null );
    ISkMnemoCfg mnemoCfg = mnemoServ().getMnemo( aMnemoId );
    String partId = mnemoCfg.skid().strid();
    MPart part = partStackManager.findPart( partId );
    if( part == null ) {
      UIpartInfo partInfo = new UIpartInfo( partId );
      partInfo.setCloseable( true );
      partInfo.setContributionUri( Activator.PLUGIN_ID, UipartMnemoscheme.class );
      partInfo.setLabel( mnemoCfg.nmName() );
      partInfo.setTooltip( mnemoCfg.description() );
      part = partStackManager.createPart( partInfo );
    }
    UipartMnemoscheme msPart = (UipartMnemoscheme)part.getObject();
    msPart.showMnemoscheme( mnemoCfg );
    e4Helper().switchToPerspective( PERSPID_MENMOS_MAIN, partId );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

}
