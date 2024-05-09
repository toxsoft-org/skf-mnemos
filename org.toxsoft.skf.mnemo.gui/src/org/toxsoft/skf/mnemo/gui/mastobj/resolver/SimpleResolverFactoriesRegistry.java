package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tslib.bricks.strid.more.*;

/**
 * {@link ISimpleResolverFactoriesRegistry} implementation.
 *
 * @author hazard157
 */
public class SimpleResolverFactoriesRegistry
    extends StridablesRegisrty<ISimpleResolverFactory>
    implements ISimpleResolverFactoriesRegistry {

  /**
   * Constructor.
   */
  public SimpleResolverFactoriesRegistry() {
    super( ISimpleResolverFactory.class );
  }

  // ------------------------------------------------------------------------------------
  // ISimpleResolverFactoriesRegistry
  //

}
