package org.toxsoft.skf.mnemo.lib;

import org.toxsoft.skf.mnemo.lib.impl.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkMnemosServiceSharedResources {

  /**
   * {@link ISkMnemosServiceHardConstants}
   */
  String STR_CLB_MNEMO_CFG_DATA            = Messages.getString( "STR_CLB_MNEMO_CFG_DATA" );            //$NON-NLS-1$
  String STR_CLB_MNEMO_CFG_DATA_D          = Messages.getString( "STR_CLB_MNEMO_CFG_DATA_D" );          //$NON-NLS-1$
  String STR_ABKIND_MNEMO                  = Messages.getString( "STR_ABKIND_MNEMO" );                  //$NON-NLS-1$
  String STR_ABKIND_MNEMO_D                = Messages.getString( "STR_ABKIND_MNEMO_D" );                //$NON-NLS-1$
  String STR_ABILITY_MNEMO_EDIT_PARAMS     = Messages.getString( "STR_ABILITY_MNEMO_EDIT_PARAMS" );     //$NON-NLS-1$
  String STR_ABILITY_MNEMO_EDIT_PARAMS_D   = Messages.getString( "STR_ABILITY_MNEMO_EDIT_PARAMS_D" );   //$NON-NLS-1$
  String STR_ABILITY_MNEMO_SEND_COMMANDS   = Messages.getString( "STR_ABILITY_MNEMO_SEND_COMMANDS" );   //$NON-NLS-1$
  String STR_ABILITY_MNEMO_SEND_COMMANDS_D = Messages.getString( "STR_ABILITY_MNEMO_SEND_COMMANDS_D" ); //$NON-NLS-1$

  /**
   * {@link SkMnemosService}
   */
  String FMT_ERR_MNEMO_EXISTS      = Messages.getString( "FMT_ERR_MNEMO_EXISTS" );      //$NON-NLS-1$
  String MSG_WARN_NAME_NOT_SET     = Messages.getString( "MSG_WARN_NAME_NOT_SET" );     //$NON-NLS-1$
  String FMT_ERR_MNEMO_NOT_EXISTS  = Messages.getString( "FMT_ERR_MNEMO_NOT_EXISTS" );  //$NON-NLS-1$
  String FMT_WARN_MNEMO_NOT_EXISTS = Messages.getString( "FMT_WARN_MNEMO_NOT_EXISTS" ); //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // USkat entities are defined only in English, l10n done via USkat localization service

  String STR_MNEMOSCHEME_CLASS   = Messages.getString( "STR_MNEMOSCHEME_CLASS" );   //$NON-NLS-1$
  String STR_MNEMOSCHEME_CLASS_D = Messages.getString( "STR_MNEMOSCHEME_CLASS_D" ); //$NON-NLS-1$

}
