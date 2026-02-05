package org.toxsoft.skf.mnemo.mned.lite;

import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.skf.ext.mastobj.gui.main.*;

/**
 * Plugin common constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkfMnemMnedLiteConstants {

  String MNED_LITE = "mned.lite"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // plugin constants prefixes

  String XXX_FULL_ID = "org.toxsoft.skf.mnemo.mned.lite"; //$NON-NLS-1$ general full ID prefix (IDpath)
  String XXX_ID      = "mned.lite";                       //$NON-NLS-1$ general short ID prefix (IDname)
  String XXX_ACT_ID  = XXX_ID + ".act";                   //$NON-NLS-1$ prefix of the ITsActionDef IDs
  String XXX_M5_ID   = XXX_ID + ".m5";                    //$NON-NLS-1$ perfix of M5-model IDs

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";          //$NON-NLS-1$
  String ICONID_RTC_RECTANGLE      = "rtc-rectangle";    //$NON-NLS-1$
  String ICONID_RTC_ELLIPSE        = "rtc-ellipse";      //$NON-NLS-1$
  String ICONID_RTC_LINE           = "rtc-line";         //$NON-NLS-1$
  String ICONID_RTC_LINEAR_GAUGE   = "rtc-linear-gauge"; //$NON-NLS-1$
  String ICONID_RTC_LABEL          = "rtc-label";        //$NON-NLS-1$
  String ICONID_RTC_SIMPLE_ARROW   = "rtc-simple-arrow"; //$NON-NLS-1$

  String ICONID_RTC_ATTR_VALUE_VIEW   = "rtc-attr-value";   //$NON-NLS-1$
  String ICONID_RTC_RTDATA_VALUE_VIEW = "rtc-rtdata-value"; //$NON-NLS-1$
  String ICONID_RTC_RRI_VALUE_VIEW    = "rtc-rri-value";    //$NON-NLS-1$

  String ICONID_RTC_INPUT_FIELD = "rtc-input-field"; //$NON-NLS-1$

  String ICONID_RTC_CIRCLE_LAMP = "rtc-circle-lamp"; //$NON-NLS-1$
  String ICONID_RTC_RECT_LAMP   = "rtc-rect-lamp";   //$NON-NLS-1$

  String ICONID_RTC_IMAGE      = "rtc-image";      //$NON-NLS-1$
  String ICONID_RTC_CHECKBOX   = "rtc-checkbox";   //$NON-NLS-1$
  String ICONID_RTC_CMD_BUTTON = "rtc-cmd-button"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Palette categories

  String PARAMID_CATEGORY = "paletteCategory"; //$NON-NLS-1$

  String CATID_GEOMETRY     = "geometry";   //$NON-NLS-1$
  String CATID_BOOLEAN_LAMP = "boolenLamp"; //$NON-NLS-1$
  String CATID_GAUGE        = "gauge";      //$NON-NLS-1$
  String CATID_TEXT         = "text";       //$NON-NLS-1$
  String CATID_ARROW        = "arrow";      //$NON-NLS-1$
  String CATID_LAMP         = "lamp";       //$NON-NLS-1$

  String CATID_NUMERIC_VALUE_VIEW = "numericValueView"; //$NON-NLS-1$
  String CATID_NUMERIC_VALUE_EDIT = "numericValueEdit"; //$NON-NLS-1$

  /**
   * Section ID in {@link IVedScreenCfg#extraData()} to store instance of type {@link IMnemoResolverConfig}.
   * <p>
   * Section with this ID contains {@link IMnemoResolverConfig} keeper textual representation. The method
   * {@link IKeepablesStorageRo#readItem(String, IEntityKeeper, Object)} must be used to read the content of the
   * section.
   */
  String VED_SCREEN_EXTRA_DATA_ID_RTCONTROLS_MANAGER_CONGIF = USKAT_FULL_ID + ".RtControlsManagerConfig"; //$NON-NLS-1$

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
