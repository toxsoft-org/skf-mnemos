package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * @author vs
 */
public class RtControlPaletteEntry
    extends StridableParameterized
    implements IRtControlsPaletteEntry {

  private final IRtControlCfg itemCfg;

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @param aCfg {@link IRtControlCfg} - the configuration of the item to be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public RtControlPaletteEntry( String aId, IOptionSet aParams, IRtControlCfg aCfg ) {
    super( aId, aParams );
    TsNullArgumentRtException.checkNull( aCfg );
    itemCfg = aCfg;
  }

  // ------------------------------------------------------------------------------------
  // IVedItemsPaletteEntry
  //

  @Override
  public IRtControlCfg itemCfg() {
    return itemCfg;
  }

}
