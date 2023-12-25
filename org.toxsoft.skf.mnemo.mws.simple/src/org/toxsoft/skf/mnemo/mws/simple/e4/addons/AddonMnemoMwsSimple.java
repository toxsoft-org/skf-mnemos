package org.toxsoft.skf.mnemo.mws.simple.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.mnemo.lib.impl.*;
import org.toxsoft.skf.mnemo.mws.simple.*;
import org.toxsoft.skf.mnemo.mws.simple.e4.main.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The plugin addon.
 *
 * @author hazard157
 */
public class AddonMnemoMwsSimple
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonMnemoMwsSimple() {
    super( Activator.PLUGIN_ID );
    SkCoreUtils.registerSkServiceCreator( SkMnemosService.CREATOR );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IMnemoMwsSimpleConstants.init( aWinContext );
    //
    ISkConnectionSupplier connectionSupplier = aWinContext.get( ISkConnectionSupplier.class );

    /**
     * TODO This implementation always uses the default connection for the mnemoschemes list monitoring.
     * <p>
     * There may be problem if UIpart UipartMnemoscheme uses other connection.
     */

    ISkConnection conn = connectionSupplier.defConn();
    IMnemoschemesPerspectiveController pc =
        new MnemoschemesPerspectiveController( conn, new TsGuiContext( aWinContext ) );
    aWinContext.set( IMnemoschemesPerspectiveController.class, pc );

  }

}
