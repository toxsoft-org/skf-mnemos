package org.toxsoft.skf.mnemo.skide;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Application common constants.
 *
 * @author vs
 */
@SuppressWarnings( "javadoc" )
public interface ISkidePluginMnemoConstants {

  // ------------------------------------------------------------------------------------
  // E4
  //

  String PERSPID_MENMOS_EDITOR    = "org.toxsoft.skf.mnemo.persp.mnemos_editor"; //$NON-NLS-1$
  String PARTSTACKID_MNEMOS_STACK = "org.toxsoft.skf.mnemo.partstack.mnemos";    //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME       = "ICONID_";                  //$NON-NLS-1$
  String ICONID_SKIDE_PLUGIN_MNEMOS      = "skide-plugin-mnemos";      //$NON-NLS-1$
  String ICONID_SKIDE_UNIT_MNEMOS_EDITOR = "skide-unit-mnemos-editor"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkidePluginMnemoConstants.class,
        PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
