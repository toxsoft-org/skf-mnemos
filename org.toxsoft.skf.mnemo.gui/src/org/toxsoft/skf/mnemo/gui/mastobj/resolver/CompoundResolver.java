package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.uskat.core.connection.*;

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
      if( ugwi == null ) {
        String str = ur.toString();
        if( ur instanceof AbstractSimpleResolver asr ) {
          str = OptionSetKeeper.KEEPER.ent2str( asr.cfg() );
        }
        LoggerUtils.errorLogger().error( "Resolve failed. Resolver: \"%s\"", str ); //$NON-NLS-1$
        return null;
      }
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
