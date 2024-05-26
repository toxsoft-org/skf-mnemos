package org.toxsoft.skf.mnemo.gui.mastobj;

import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The UGWI resolver calculates UGWI from the master UGWI.
 *
 * @author hazard157
 */
public interface IUgwiResolver {

  /**
   * Calculates UGWI from the master UGWI.
   *
   * @param aMaster {@link Ugwi} - the master UGWI
   * @return {@link Ugwi} - calculated (resolved) UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  Ugwi resolve( Ugwi aMaster );

}
