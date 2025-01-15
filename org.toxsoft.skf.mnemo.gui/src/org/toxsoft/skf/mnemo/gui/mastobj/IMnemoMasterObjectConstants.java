package org.toxsoft.skf.mnemo.gui.mastobj;

import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;

/**
 * Constants used by menmoschemes master object support subsystem.
 *
 * @author hazard157
 */
public interface IMnemoMasterObjectConstants {

  /**
   * Section ID in {@link IVedScreenCfg#extraData()} to store instance of type {@link IMnemoResolverConfig}.
   * <p>
   * Section with this ID contains {@link IMnemoResolverConfig} keeper textual representation. The method
   * {@link IKeepablesStorageRo#readItem(String, IEntityKeeper, Object)} must be used to read the content of the
   * section.
   */
  String VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF = USKAT_FULL_ID + ".MnemoResolverConfig"; //$NON-NLS-1$

  /**
   * ID of {@link IVedItemCfg#extraData()} section with VED item properties resolvers map.
   * <p>
   * Section with this ID contains {@link IStringMap} of {@link ICompoundResolverConfig} keepables. The method
   * {@link IKeepablesStorageRo#readStridMap(String, IEntityKeeper)} must be used to read the content of the section..
   */
  String VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS = USKAT_FULL_ID + ".UgwiPropertiesResolversMap"; //$NON-NLS-1$

  /**
   * ИД "разрешителя" для главного мастер-объекта мнемосхемы.
   */
  String VED_SCREEN_MAIN_MNEMO_RESOLVER_ID = "ved.screen.main.mnemo.resolver.id"; //$NON-NLS-1$

  /**
   * ИД Ugwi главного мастер-объекта мнемосхемы.
   */
  String VED_SCREEN_MAIN_MNEMO_MASTER_UGWI = "ved.screen.main.mnemo.masterUgwi"; //$NON-NLS-1$
}
