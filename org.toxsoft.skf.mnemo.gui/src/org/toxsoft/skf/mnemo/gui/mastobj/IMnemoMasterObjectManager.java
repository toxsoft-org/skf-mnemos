package org.toxsoft.skf.mnemo.gui.mastobj;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.ugwi.*;

/**
 * The service to resolve VED item's resolvable properties.
 * <p>
 * TODO comment everything:
 * <p>
 * <ul>
 * <li>resolvable property - ;</li>
 * <li>xxx;</li>
 * <li>zzz.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IMnemoMasterObjectManager {

  /**
   * Resolves the specified VED screen against the specified master object.
   * <p>
   * Returns the new instance of the same VED screen configuration with resolved VED actor properties.
   *
   * @param aMasterObject {@link Ugwi} - the mnemoscheme master object UGWI
   * @param aCfg {@link IVedScreenCfg} - the configuration of the mnemoscheme to resolve
   * @param aSkConn {@link ISkConnection} - SK-connection used for UGWO resolving
   * @return {@link IVedScreenCfg} - resolved mnemoscheme configuration
   */
  IVedScreenCfg processMasterObject( Ugwi aMasterObject, IVedScreenCfg aCfg, ISkConnection aSkConn );

}
