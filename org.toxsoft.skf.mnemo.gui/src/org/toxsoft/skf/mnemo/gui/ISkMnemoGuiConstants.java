package org.toxsoft.skf.mnemo.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkMnemoGuiConstants {

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";       //$NON-NLS-1$
  String ICONID_MNEMO              = "mnemo";         //$NON-NLS-1$
  String ICONID_MNEMOS_LIST        = "mnemos-list";   //$NON-NLS-1$
  String ICONID_MNEMO_EDIT         = "mnemo-edit";    //$NON-NLS-1$
  String ICONID_ENSLAVE            = "enslave";       //$NON-NLS-1$
  String ICONID_FREE               = "unlink";        //$NON-NLS-1$
  String ICONID_RT_ACTION_ACTOR    = "actors-dialog"; //$NON-NLS-1$
  String ICONID_DIAGNOSTICS        = "checklist";     //$NON-NLS-1$

  String ICONID_OBJECT  = "box";
  String ICONID_RIVET   = "clip";
  String ICONID_LINK    = "link";
  String ICONID_OBJECTS = "dialog-error";

  String ICONID_RESOLVED_OBJECT = "opened-box";

  String ICONID_VED_ACTOR_COLOR_DECORATOR = "ved-actor-color-decorator"; //$NON-NLS-1$

  String ICONID_VISEL_PANEL = "visel-panel"; //$NON-NLS-1$

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkMnemoGuiConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    //
  }

}
