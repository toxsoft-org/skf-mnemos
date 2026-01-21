package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Implementation {@link IRtControlsPaletteCategory}.
 *
 * @author vs
 */
public class RtControlsPaletteCategory
    extends StridableParameterized
    implements IRtControlsPaletteCategory {

  private final IStridablesListEdit<IRtControlsPaletteEntry> entries = new StridablesList<>();

  RtControlsPaletteCategory( String aId ) {
    super( aId );
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IRtControlsPaletteCategory
  //

  @Override
  public IStridablesListEdit<IRtControlsPaletteEntry> listEntries() {
    return entries;
  }

}
