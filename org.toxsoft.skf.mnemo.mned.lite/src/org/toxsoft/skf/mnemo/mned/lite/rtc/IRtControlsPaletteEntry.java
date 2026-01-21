package org.toxsoft.skf.mnemo.mned.lite.rtc;

import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * The RtControl palette entry allows to create RtControl item with the specified initial configuration.
 * <p>
 * The entries are returned by the factory with method {@link IRtControlFactory#paletteEntries()}.
 *
 * @author vs
 */
public interface IRtControlsPaletteEntry
    extends IStridableParameterized {

  /**
   * Returns item configuration filled with default values.
   *
   * @return {@link IRtControlCfg} - configuration of the item
   */
  IRtControlCfg itemCfg();
}
