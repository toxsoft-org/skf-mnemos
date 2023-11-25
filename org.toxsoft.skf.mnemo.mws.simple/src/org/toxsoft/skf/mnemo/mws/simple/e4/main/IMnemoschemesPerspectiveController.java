package org.toxsoft.skf.mnemo.mws.simple.e4.main;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Handles everything concerning mnemoschemes perspective.
 * <p>
 *
 * @author hazard157
 */
public interface IMnemoschemesPerspectiveController {

  /**
   * Opens the UIpart with the specified mnemoscheme or activates already opened one.
   *
   * @param aMnemoId String - the mnemoscheme ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such mnemoscheme found
   */
  void openMnemoscheme( String aMnemoId );

}
