package org.toxsoft.skf.mnemo.skide.main;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoConstants.*;
import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * SkiDE unit: unit template 1.
 *
 * @author hazard157
 */
public class SkideUnitMnemo
    extends AbstractSkideUnit {

  /**
   * The plugin ID.
   */
  public static final String UNIT_ID = SKIDE_FULL_ID + ".unit.mnemos"; //$NON-NLS-1$

  SkideUnitMnemo( ITsGuiContext aContext, AbstractSkidePlugin aCreator ) {
    super( UNIT_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_MNEMOS_UNIT, //
        TSID_DESCRIPTION, STR_SKIDE_MNEMOS_UNIT_D, //
        TSID_ICON_ID, ICONID_MNEMOS_EDITOR_PLUGIN //
    ), aContext, aCreator );
    unitActions().add( ACDEF_ABOUT );
  }

  @Override
  protected void doHandleAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_ABOUT: {
        // TODO display complete info about unit
        TsDialogUtils.info( getShell(), id() + '\n' + nmName() + '\n' + description() );
        break;
      }
      default: {
        // TODO display info about known but unhandled action
        TsDialogUtils.info( getShell(), aActionId );
      }
    }
  }

  @Override
  protected AbstractSkideUnitPanel doCreateUnitPanel( ITsGuiContext aContext ) {
    return new SkideUnitMnemoPanel( aContext, this );
  }

}
