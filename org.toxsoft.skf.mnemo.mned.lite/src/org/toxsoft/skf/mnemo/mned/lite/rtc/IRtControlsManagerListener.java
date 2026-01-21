package org.toxsoft.skf.mnemo.mned.lite.rtc;

import org.toxsoft.core.tslib.coll.helpers.*;

/**
 * Listens changes in {@link IRtControlsManager}.
 *
 * @author vs
 */
public interface IRtControlsManagerListener {

  /**
   * Called when items list or item fields changes.
   *
   * @param aSource {@link IRtControlsManager} - the event source
   * @param aOp {@link ECrudOp} - the operation kind
   * @param aId String - affected item ID or <code>null</code> for {@link ECrudOp#LIST}
   */
  void onRtControlsListChange( IRtControlsManager aSource, ECrudOp aOp, String aId );
}
