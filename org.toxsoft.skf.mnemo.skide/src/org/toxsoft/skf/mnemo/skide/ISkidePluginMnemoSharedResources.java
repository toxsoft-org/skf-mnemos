package org.toxsoft.skf.mnemo.skide;

import org.toxsoft.skf.mnemo.skide.e4.uiparts.*;
import org.toxsoft.skf.mnemo.skide.main.*;
import org.toxsoft.skf.mnemo.skide.tasks.codegen.*;
import org.toxsoft.skf.mnemo.skide.tasks.upload.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc", "nls" } )
public interface ISkidePluginMnemoSharedResources {

  /**
   * {@link ISkidePluginMnemoSharedResources}
   */
  String STR_EDIT_MNEMO_AS_LITE              = Messages.getString( "STR_EDIT_MNEMO_AS_LITE" );
  String STR_EDIT_MNEMO_AS_LITE_D            = Messages.getString( "STR_EDIT_MNEMO_AS_LITE_D" );
  String STR_EDIT_MNEMO_AS_PRO               = Messages.getString( "STR_EDIT_MNEMO_AS_PRO" );
  String STR_EDIT_MNEMO_AS_PRO_D             = Messages.getString( "STR_EDIT_MNEMO_AS_PRO_D" );
  String STR_PREF_BINDLE_MNEMOS              = Messages.getString( "STR_PREF_BINDLE_MNEMOS" );
  String STR_PREF_BINDLE_MNEMOS_D            = Messages.getString( "STR_PREF_BINDLE_MNEMOS_D" );
  String STR_APPREF_IS_DEFAULT_EDITOR_LITE   = Messages.getString( "STR_APPREF_IS_DEFAULT_EDITOR_LITE" );
  String STR_APPREF_IS_DEFAULT_EDITOR_LITE_D = Messages.getString( "STR_APPREF_IS_DEFAULT_EDITOR_LITE_D" );

  /**
   * {@link SkidePluginMnemo}
   */
  String STR_SKIDE_MNEMOS_PLUGIN   = Messages.getString( "STR_SKIDE_MNEMOS_PLUGIN" );
  String STR_SKIDE_MNEMOS_PLUGIN_D = Messages.getString( "STR_SKIDE_MNEMOS_PLUGIN_D" );

  /**
   * {@link SkideUnitMnemo}
   */
  String STR_SKIDE_MNEMOS_UNIT   = Messages.getString( "STR_SKIDE_MNEMOS_UNIT" );
  String STR_SKIDE_MNEMOS_UNIT_D = Messages.getString( "STR_SKIDE_MNEMOS_UNIT_D" );

  /**
   * org.toxsoft.skf.mnemo.skide.tasks.codegen.IPackageConstants
   */
  String STR_OPDEF_GW_MNEMOS_INTERFACE_NAME   = Messages.getString( "STR_OPDEF_GW_MNEMOS_INTERFACE_NAME" );
  String STR_OPDEF_GW_MNEMOS_INTERFACE_NAME_D = Messages.getString( "STR_OPDEF_GW_MNEMOS_INTERFACE_NAME_D" );

  /**
   * {@link TaskMnemosCodegen}
   */
  String FMT_INFO_JAVA_INTERFACE_WAS_GENERATED = Messages.getString( "FMT_INFO_JAVA_INTERFACE_WAS_GENERATED" );

  /**
   * {@link UipartSkMnemoEditorLite}, {@link UipartSkMnemoEditorPro}
   */
  String STR_ASK_ACCEPT_MNEMO_CORRECTIONS = Messages.getString( "STR_ASK_ACCEPT_MNEMO_CORRECTIONS" );

  /**
   * {@link TaskMnemosUpload}
   */
  String STR_CLEAR_MNEMOS_BEFORE_UPLOAD   = Messages.getString( "STR_CLEAR_MNEMOS_BEFORE_UPLOAD" );
  String STR_CLEAR_MNEMOS_BEFORE_UPLOAD_D = Messages.getString( "STR_CLEAR_MNEMOS_BEFORE_UPLOAD_D" );
  String FMT_INFO_MNEMOSCHEMES_UPLOADED   = Messages.getString( "FMT_INFO_MNEMOSCHEMES_UPLOADED" );

}
