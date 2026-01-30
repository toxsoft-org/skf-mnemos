package org.toxsoft.skf.mnemo.skide;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;
import static org.toxsoft.skf.mnemo.mned.pro.ISkfMnemMnedProConstants.*;
import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoSharedResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

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

  // ------------------------------------------------------------------------------------
  // actions
  //

  String ACTID_EDIT_MNEMO_AS_PRO   = MNEMO_ACT_ID + ".OpenInEditorPro";        //$NON-NLS-1$
  String ACTID_EDIT_MNEMO_AS_LITE  = MNEMO_ACT_ID + ".OpenInEditorLite";       //$NON-NLS-1$
  String ACTID_EDIT_MNEMO_MENU_BTN = MNEMO_ACT_ID + ".OpenInEditorMenuButton"; //$NON-NLS-1$

  ITsActionDef ACDEF_EDIT_MNEMO_AS_PRO = TsActionDef.ofPush2( ACTID_EDIT_MNEMO_AS_PRO, //
      STR_EDIT_MNEMO_AS_PRO, STR_EDIT_MNEMO_AS_PRO_D, ICONID_MNEMO_EDIT_PRO );

  ITsActionDef ACDEF_EDIT_MNEMO_AS_LITE = TsActionDef.ofPush2( ACTID_EDIT_MNEMO_AS_LITE, //
      STR_EDIT_MNEMO_AS_LITE, STR_EDIT_MNEMO_AS_LITE_D, ICONID_MNEMO_EDIT_LITE );

  ITsActionDef ACDEF_EDIT_MNEMO_MENU_BTN = TsActionDef.ofMenu2( ACTID_EDIT_MNEMO_MENU_BTN, //
      STR_EDIT_MNEMO_AS_LITE, STR_EDIT_MNEMO_AS_LITE_D, ICONID_MNEMO_EDIT_LITE );

  // ------------------------------------------------------------------------------------
  // Application preferences

  String PREFBUNDLEID_BINDLE_MNEMOS = PERSPID_MENMOS_EDITOR;

  String APREFID_IS_DEFAULT_EDITOR_LITE = "IsDefaultEditorLite"; //$NON-NLS-1$

  IDataDef APPREF_FOO_1 = DataDef.create( APREFID_IS_DEFAULT_EDITOR_LITE, BOOLEAN, ///
      TSID_NAME, STR_APPREF_IS_DEFAULT_EDITOR_LITE, ///
      TSID_DESCRIPTION, STR_APPREF_IS_DEFAULT_EDITOR_LITE_D, ///
      TSID_DEFAULT_VALUE, AV_TRUE ///
  );

  IStridablesList<IDataDef> SHOWN_APPREFS_LIST = new StridablesList<>( //
      APPREF_FOO_1 //
  );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkidePluginMnemoConstants.class,
        PREFIX_OF_ICON_FIELD_NAME );
    // register application preference option available for user to edit via preferences GUI dialog
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PREFBUNDLEID_BINDLE_MNEMOS, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_PREF_BINDLE_MNEMOS, //
        TSID_DESCRIPTION, STR_PREF_BINDLE_MNEMOS_D, //
        TSID_ICON_ID, ICONID_MNEMO //
    ) );
    for( IDataDef dd : SHOWN_APPREFS_LIST ) {
      pb.defineOption( dd );
    }
  }

}
