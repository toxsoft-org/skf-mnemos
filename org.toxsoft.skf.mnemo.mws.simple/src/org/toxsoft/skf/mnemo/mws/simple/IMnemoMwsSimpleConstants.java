package org.toxsoft.skf.mnemo.mws.simple;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * The plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IMnemoMwsSimpleConstants {

  // ------------------------------------------------------------------------------------
  // E4
  //

  String PERSPID_MENMOS_MAIN     = "org.toxsoft.skf.mnemo.mws.simple.persp.main";        //$NON-NLS-1$
  String PARTSTACKID_MNEMOS_MAIN = "org.toxsoft.skf.mnemo.mws.simple.partstack.main";    //$NON-NLS-1$
  String MMENUID_MNEMOS_LIST     = "org.toxsoft.skf.mnemo.mws.simple.mmenu.mnemos_list"; //$NON-NLS-1$

  String CMDID_OPEN_MNEMO_BY_ID = "org.toxsoft.skf.mnemo.mws.simple.cmd.open_mnemo_by_id"; //$NON-NLS-1$
  String CMDARGID_MNEMO_ID      = "org.toxsoft.skf.mnemo.mws.simple.cmdarg.mnemo_id";      //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_"; //$NON-NLS-1$
  // String ICONID_XXX = "xxx"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IMnemoMwsSimpleConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
