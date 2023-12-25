package org.toxsoft.skf.mnemo.mws.simple.e4.uiparts;

import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.skf.mnemo.gui.glib.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.mws.simple.e4.main.*;
import org.toxsoft.uskat.core.*;
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
    ISkMnemoCfg cfg = panel.getMnemoConfig();
    if( cfg == null ) {
      return;
    }
    // update panel on mnemoscheme change
    if( aOp == ECrudOp.EDIT && cfg.strid().equals( aMnemoId ) ) {
      panel.setMnemoConfig( cfg );
    }
  }

  private ISkMnemosService mnemoServ() {
    return coreApi().getService( ISkMnemosService.SERVICE_ID );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the configuration of the mnemoscheme to display.
   *
   * @param aCfg {@link ISkMnemoCfg} - the mnemoscheme config
   */
  public void showMnemoscheme( ISkMnemoCfg aCfg ) {
    panel.setMnemoConfig( aCfg );
    if( aCfg != null ) {
      panel.resume();
    }
    else {
      panel.pause();
    }
  }

}
