package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tslib.coll.*;

/**
 * Compound resolver configuration.
 *
 * @author hazard157
 */
public interface ICompoundResolverConfig {

  /**
   * Returns the list of simple resolver configuration in the order of resolve calls.
   *
   * @return {@link IList}&lt;{@link SimpleResolverCfg}&gt; - simple configurations ordered list
   */
  IList<SimpleResolverCfg> cfgs();

}
