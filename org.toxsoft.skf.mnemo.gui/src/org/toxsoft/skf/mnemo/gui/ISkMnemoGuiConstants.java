package org.toxsoft.skf.mnemo.gui;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.IMnemoGuiSharedResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.cmd.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Plugin constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkMnemoGuiConstants {

  // ------------------------------------------------------------------------------------
  // plugin constants prefixes

  String MNEMO_FULL_ID = "org.toxsoft.skf.mnemo"; //$NON-NLS-1$ general full ID prefix (IDpath)
  String MNEMO_ID      = "mnemo";                 //$NON-NLS-1$ general short ID prefix (IDname)
  String MNEMO_ACT_ID  = MNEMO_ID + ".act";       //$NON-NLS-1$ prefix of the ITsActionDef IDs
  String MNEMO_M5_ID   = MNEMO_ID + ".m5";        //$NON-NLS-1$ perfix of M5-model IDs

  // ------------------------------------------------------------------------------------
  // Mouse cursors

  String CURSOR_IMG_STOP   = "cursors/cur_stop.png";   //$NON-NLS-1$
  String CURSOR_IMG_ZORDER = "cursors/cur_zorder.png"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME  = "ICONID_";          //$NON-NLS-1$
  String ICONID_MNEMO               = "mnemo";            //$NON-NLS-1$
  String ICONID_MNEMOS_LIST         = "mnemos-list";      //$NON-NLS-1$
  String ICONID_MNEMO_EDIT          = "mnemo-edit";       //$NON-NLS-1$
  String ICONID_MNEMO_EDIT_LITE     = "mnemo-edit-lite";  //$NON-NLS-1$
  String ICONID_MNEMO_EDIT_PRO      = "mnemo-edit-pro";   //$NON-NLS-1$
  String ICONID_ENSLAVE             = "enslave";          //$NON-NLS-1$
  String ICONID_FREE                = "unlink";           //$NON-NLS-1$
  String ICONID_VED_RRI_CHECK_ACTOR = "actors-nsi-check"; //$NON-NLS-1$
  String ICONID_RT_ACTION_ACTOR     = "actors-dialog";    //$NON-NLS-1$
  String ICONID_DIAGNOSTICS         = "checklist";        //$NON-NLS-1$
  String ICONID_USER_ACTION         = "user-action";      //$NON-NLS-1$

  String ICONID_OBJECT  = "box";
  String ICONID_RIVET   = "clip";
  String ICONID_LINK    = "link";
  String ICONID_OBJECTS = "dialog-error";

  String ICONID_RESOLVED_OBJECT = "opened-box";

  String ICONID_VED_ACTOR_COLOR_DECORATOR = "ved-actor-color-decorator"; //$NON-NLS-1$

  String ICONID_VISEL_PANEL     = "visel-panel";           //$NON-NLS-1$
  String ICONID_IMAGESET_BUTTON = "visel-imageset-button"; //$NON-NLS-1$

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
  // Sk-abilities
  //

  /**
   * Create id ability to access mnemos
   */
  String ABILITYID_ACCESS_MNEMOS = ISkMnemosService.SERVICE_ID + ".ability.mnemos"; //$NON-NLS-1$

  /**
   * Create ability to access values editor
   */
  IDtoSkAbility ABILITY_ACCESS_MNEMOS = DtoSkAbility.create( ABILITYID_ACCESS_MNEMOS,
      ISkMnemosServiceHardConstants.ABKINDID_MNEMO, STR_ABILITY_ACCESS_MNEMOS, STR_ABILITY_ACCESS_MNEMOS_D );

  /**
   * Data types
   */

  IDataType DT_CMD_ARG_VALUES_SET = DataType.create( VALOBJ, //
      TSID_NAME, STR_CMD_ARG_VALUES_SET, //
      TSID_DESCRIPTION, STR_CMD_ARG_VALUES_SET, //
      TSID_KEEPER_ID, CmdArgValuesSet.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjCmdArgValuesSet.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  // ------------------------------------------------------------------------------------
  // Application preferences

  String PBID_BUNDLE_MNEMOS = MNEMO_FULL_ID + ".gui.mnemos"; //$NON-NLS-1$

  String APREFID_IS_DEFAULT_EDITOR_LITE = "IsDefaultEditorLite"; //$NON-NLS-1$

  IDataDef APPREF_IS_DEFAULT_EDITOR_LITE = DataDef.create( APREFID_IS_DEFAULT_EDITOR_LITE, BOOLEAN, ///
      TSID_NAME, STR_APPREF_IS_DEFAULT_EDITOR_LITE, ///
      TSID_DESCRIPTION, STR_APPREF_IS_DEFAULT_EDITOR_LITE_D, ///
      TSID_DEFAULT_VALUE, AV_TRUE ///
  );

  IStridablesList<IDataDef> SHOWN_APPREFS_LIST = new StridablesList<>( //
  // this pref is not for user editing: APPREF_IS_DEFAULT_EDITOR_LITE //
  );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, ISkMnemoGuiConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    // register application preference option available for user to edit via preferences GUI dialog
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PBID_BUNDLE_MNEMOS, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_PREF_BUNDLE_MNEMOS, //
        TSID_DESCRIPTION, STR_PREF_BUNDLE_MNEMOS_D, //
        TSID_ICON_ID, ICONID_MNEMO //
    ) );
    for( IDataDef dd : SHOWN_APPREFS_LIST ) {
      pb.defineOption( dd );
    }
  }

}
