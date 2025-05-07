package org.toxsoft.skf.mnemo.mws.simple.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.glib.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.mws.simple.e4.main.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.gui.e4.uiparts.*;

/**
 * Simple UIpart displays the specified mnemoscheme.
 * <p>
 * TODO This implementation always uses the default connection for the mnemoschemes list monitoring.
 * <p>
 * There may be problem if {@link MnemoschemesPerspectiveController} uses other connection.
 *
 * @author hazard157
 */
public class UipartMnemoscheme
    extends SkMwsAbstractPart {

  ISkMnemoCfg skMnemoCfg = null; // currently displayed mnemoscheme object

  IRuntimeMnemoPanel panel;

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    panel = new RuntimeMnemoPanel( aParent, new TsGuiContext( tsContext() ) );
    panel.pause();
    // listen to mnemos changes and immediately update panel
    mnemoServ().eventer().addListener( this::whenMnemoCfgChanged );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenMnemoCfgChanged( @SuppressWarnings( "unused" ) ISkCoreApi aCoreApi, ECrudOp aOp, String aMnemoId ) {
    if( skMnemoCfg != null ) {
      // update panel on mnemoscheme change
      if( aOp == ECrudOp.EDIT && skMnemoCfg.strid().equals( aMnemoId ) ) {
        showMnemoscheme( skMnemoCfg );
      }
    }
  }

  private ISkMnemosService mnemoServ() {
    return coreApi().getService( ISkMnemosService.SERVICE_ID );
  }

  private IVedScreenCfg applyMasterObjectIfNeeded( IVedScreenCfg aCfg ) {
    MnemoResolverConfig resolverConfig = MasterObjectUtils.readMnemoResolverConfig( aCfg );
    String masterClassId = MasterObjectUtils.findMainMasterClassId( resolverConfig );
    // if mnemoscheme does not requires master object then simply return initial configuration
    if( masterClassId == null ) {
      return aCfg;
    }
    // invoke master object selection dialog
    ISkObject masterObj = SkGuiUtils.selectObject( masterClassId, skConn(), tsContext() );
    if( masterObj == null ) {
      return null; // user cancelled - show no mnemo
    }
    // apply master object
    ISimpleResolverFactoriesRegistry resRegistry = tsContext().get( ISimpleResolverFactoriesRegistry.class );
    MnemoMasterObjectManager mmoManager = new MnemoMasterObjectManager( skConn(), resRegistry );
    Ugwi ugwi = UgwiKindSkSkid.makeUgwi( masterObj.classId(), masterObj.strid() );
    return mmoManager.processMasterObject( ugwi, aCfg, skConn() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the configuration of the mnemoscheme to display.
   *
   * @param aSkMnemoCfg {@link ISkMnemoCfg} - the mnemoscheme config
   */
  public void showMnemoscheme( ISkMnemoCfg aSkMnemoCfg ) {
    skMnemoCfg = aSkMnemoCfg;
    if( aSkMnemoCfg != null ) {
      IVedScreenCfg cfg = applyMasterObjectIfNeeded( VedScreenCfg.cfgFromString( aSkMnemoCfg.cfgData() ) );
      panel.setMnemoConfig( cfg );
      panel.resume();
    }
    else {
      panel.setMnemoConfig( null );
      panel.pause();
    }
  }

}
