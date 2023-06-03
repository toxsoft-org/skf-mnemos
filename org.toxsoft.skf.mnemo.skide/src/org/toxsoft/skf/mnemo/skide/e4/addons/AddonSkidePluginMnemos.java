package org.toxsoft.skf.mnemo.skide.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.mnemo.gui.*;
import org.toxsoft.skf.mnemo.gui.e4.services.*;
import org.toxsoft.skf.mnemo.skide.*;
import org.toxsoft.skf.mnemo.skide.Activator;
import org.toxsoft.skf.mnemo.skide.e4.services.*;
import org.toxsoft.skf.mnemo.skide.main.*;
import org.toxsoft.skide.core.api.*;

/**
 * Plugin addon.
 *
 * @author vs
 */
public class AddonSkidePluginMnemos
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonSkidePluginMnemos() {
    super( Activator.PLUGIN_ID );
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    aQuantRegistrator.registerQuant( new QuantSkMnemoGui() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skEnv = aAppContext.get( ISkideEnvironment.class );
    skEnv.pluginsRegistrator().registerPlugin( SkidePluginMnemo.INSTANCE );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkidePluginMnemoConstants.init( aWinContext );
    //
    ISkMnemoEditService vss = new SkMnemoEditService( new TsGuiContext( aWinContext ) );
    aWinContext.set( ISkMnemoEditService.class, vss );
  }

}
