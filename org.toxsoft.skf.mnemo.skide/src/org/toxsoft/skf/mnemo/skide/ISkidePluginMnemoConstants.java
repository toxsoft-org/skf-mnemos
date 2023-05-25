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
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_"; //$NON-NLS-1$
  // String ICONID_SKIDE_PLUGIN = "skide-plugin"; //$NON-NLS-1$
  String ICONID_MNEMOS_PLUGIN        = "mnemo-editor";          //$NON-NLS-1$
  String ICONID_MNEMOS_EDITOR_PLUGIN = "app-mnemoshema-editor"; //$NON-NLS-1$

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
