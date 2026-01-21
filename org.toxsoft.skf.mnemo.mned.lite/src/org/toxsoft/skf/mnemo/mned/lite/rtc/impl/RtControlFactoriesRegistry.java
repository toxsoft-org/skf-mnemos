package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * {@link IRtControlFactoriesRegistry} implementation.
 *
 * @author vs
 */
public class RtControlFactoriesRegistry
    extends StridablesRegisrty<IRtControlFactory>
    implements IRtControlFactoriesRegistry {

  /**
   * Constructor.
   */
  public RtControlFactoriesRegistry() {
    super( IRtControlFactory.class );
  }
}
