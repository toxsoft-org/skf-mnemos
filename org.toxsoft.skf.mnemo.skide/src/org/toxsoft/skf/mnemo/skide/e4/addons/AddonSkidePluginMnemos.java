package org.toxsoft.skf.mnemo.skide.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.mnemo.skide.*;
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
  protected void initApp( IEclipseContext aAppContext ) {
    ISkideEnvironment skEnv = aAppContext.get( ISkideEnvironment.class );
    skEnv.pluginsRegistrator().registerPlugin( SkidePluginMnemo.INSTANCE );
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ISkidePluginMnemoConstants.init( aWinContext );
    //
  }

}