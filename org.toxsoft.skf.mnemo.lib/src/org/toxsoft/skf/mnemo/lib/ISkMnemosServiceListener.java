package org.toxsoft.skf.mnemo.lib;

import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.uskat.core.*;

/**
 * {@link ISkMnemosService} listener.
 *
 * @author hazard157
 */
public interface ISkMnemosServiceListener {

  /**
   * Called when any change in mnemoCfgs occur.
   *
   * @param aCoreApi {@link ISkCoreApi} - the event source
   * @param aOp {@link ECrudOp} - the kind of change
   * @param aMnemoId String - affected mnemoCfg login or <code>null</code> for batch changes {@link ECrudOp#LIST}
   */
  void onMnemoCfgChanged( ISkCoreApi aCoreApi, ECrudOp aOp, String aMnemoId );

}
