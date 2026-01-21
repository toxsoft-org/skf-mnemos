package org.toxsoft.skf.mnemo.mned.lite.rtc;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * RtControls palette category is optional means to visually group entries in the palette panel.
 *
 * @author vs
 */
public interface IRtControlsPaletteCategory
    extends IStridableParameterized {

  /**
   * Returns entries in this category.
   *
   * @return {@link IStridablesList}&lt;{@link IRtControlsPaletteEntry}&gt; - the entries list
   */
  IStridablesList<IRtControlsPaletteEntry> listEntries();
}
