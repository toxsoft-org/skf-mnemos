package org.toxsoft.skf.mnemo.skide.main;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoConstants.*;
import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skide.core.api.*;

/**
 * Плагин Skide: Мнемосхемы.
 *
 * @author vs
 */
public class SkidePluginMnemo
    extends AbstractSkidePlugin {

  /**
   * The plugin ID.
   */
  public static final String SKIDE_PLUGIN_ID = SKIDE_FULL_ID + ".plugin.template"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final AbstractSkidePlugin INSTANCE = new SkidePluginMnemo();

  SkidePluginMnemo() {
    super( SKIDE_PLUGIN_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_SKIDE_MNEMOS_UNIT, //
        TSID_DESCRIPTION, STR_SKIDE_MNEMOS_UNIT_D, //
        TSID_ICON_ID, ICONID_SKIDE_PLUGIN_MNEMOS //
    ) );
  }

  @Override
  protected void doCreateUnits( ITsGuiContext aContext, IStridablesListEdit<ISkideUnit> aUnitsList ) {
    aUnitsList.add( new SkideUnitMnemo( aContext, this ) );
    // aUnitsList.add( new SkideUnitTemplate2( aContext, this ) );
  }

}
