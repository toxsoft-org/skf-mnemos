package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.ugwi.*;

/**
 * {@link ICompoundResolver} implementation.
 *
 * @author hazard157
 */
public class CompoundResolver
    implements ICompoundResolver {

  private final IListEdit<IUgwiResolver> resolversList = new ElemArrayList<>();

  private CompoundResolver() {
    // nop
  }

  /**
   * Static constructor from the configuration data.
   *
   * @param aCfg {@link ICompoundResolverConfig} - resolver configuration data
   * @param aSkConn {@link ISkConnection} - active SK-connection used for resolver configuration
   * @param aRegistry {@link ISimpleResolverFactory} - the simple reolvers registry to use
   * @return {@link CompoundResolver} - created resolver
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static CompoundResolver create( ICompoundResolverConfig aCfg, ISkConnection aSkConn,
      ISimpleResolverFactoriesRegistry aRegistry ) {
    TsNullArgumentRtException.checkNulls( aCfg, aSkConn, aRegistry );
    CompoundResolver resolver = new CompoundResolver();
    for( SimpleResolverCfg srCfg : aCfg.cfgs() ) {
      ISimpleResolverFactory f = aRegistry.get( srCfg.kindId() );
      IUgwiResolver sinpleResolver = f.createResolver( srCfg.params(), aSkConn );
      resolver.resolversList.add( sinpleResolver );
    }
    return resolver;
  }

  // ------------------------------------------------------------------------------------
  // IUgwiResolver
  //

  @Override
  public Ugwi resolve( Ugwi aMaster ) {
    TsNullArgumentRtException.checkNulls( aMaster );
    Ugwi ugwi = aMaster;
    for( IUgwiResolver ur : resolversList ) {
      ugwi = ur.resolve( ugwi );
    }
    return ugwi;
  }

  // ------------------------------------------------------------------------------------
  // ICompoundUgwiResolver
  //

  @Override
  public IList<IUgwiResolver> resolversList() {
    return resolversList;
  }

  @Override
  public void addResolver( IUgwiResolver aResolver ) {
    resolversList.add( aResolver );
  }

}
