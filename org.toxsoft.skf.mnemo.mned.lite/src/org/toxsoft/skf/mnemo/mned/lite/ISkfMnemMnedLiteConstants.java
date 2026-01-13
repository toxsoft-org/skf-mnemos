package org.toxsoft.skf.mnemo.mned.lite;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin common constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkfMnemMnedLiteConstants {

  // ------------------------------------------------------------------------------------
  // plugin constants prefixes

  String XXX_FULL_ID = "org.toxsoft.skf.mnemo.mned.lite"; //$NON-NLS-1$ general full ID prefix (IDpath)
  String XXX_ID      = "mned.lite";                       //$NON-NLS-1$ general short ID prefix (IDname)
  String XXX_ACT_ID  = XXX_ID + ".act";                   //$NON-NLS-1$ prefix of the ITsActionDef IDs
  String XXX_M5_ID   = XXX_ID + ".m5";                    //$NON-NLS-1$ perfix of M5-model IDs

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";         //$NON-NLS-1$
  String ICONID_MNEMO_EDIT_LITE    = "mnemo-edit-lite"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    // register plug-in built-in icons
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkfMnemMnedLiteConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
