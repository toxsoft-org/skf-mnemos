package org.toxsoft.skf.mnemo.gui.mastobj;

import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * {@link IMnemoMasterObjectManager} implementation.
 *
 * @author hazard157
 */
public class MnemoMasterObjectManager
    implements IMnemoMasterObjectManager, ISkConnected {

  private final ISkConnection                    skConn;
  private final ISimpleResolverFactoriesRegistry registry;

  /**
   * Constructor.
   *
   * @param aSkConn {@link ISkConnection} - the Sk-connection
   * @param aRegistry {@link ISimpleResolverFactoriesRegistry} - registry used for esolvers creation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MnemoMasterObjectManager( ISkConnection aSkConn, ISimpleResolverFactoriesRegistry aRegistry ) {
    TsNullArgumentRtException.checkNulls( aSkConn, aRegistry );
    skConn = aSkConn;
    registry = aRegistry;
  }

  // ------------------------------------------------------------------------------------
  // IMnemoMasterObjectManager
  //

  @Override
  public IVedScreenCfg processMasterObject( Ugwi aMasterObject, IVedScreenCfg aCfg, ISkConnection aSkConn ) {
    TsNullArgumentRtException.checkNulls( aMasterObject, aCfg );
    // prepare new config
    VedScreenCfg cfg = new VedScreenCfg();
    cfg.canvasCfg().copyFrom( aCfg.canvasCfg() );
    cfg.extraData().copyFrom( aCfg.extraData() );
    cfg.viselCfgs().setAll( aCfg.viselCfgs() );
    // extract screen resolver configuration
    IMnemoResolverConfig mrConfig = aCfg.extraData().readItem( VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF, //
        MnemoResolverConfig.KEEPER, new MnemoResolverConfig() );
    // resolve sub-masters
    IStringMapEdit<Ugwi> subMastersMap = new StringMap<>();
    for( SubmasterConfig subCfg : mrConfig.subMasters() ) {
      ICompoundResolverConfig crCfg = subCfg.resolverCfg();
      IUgwiResolver ugwiResolver = CompoundResolver.create( crCfg, aSkConn, registry );
      Ugwi subMasterUgwi = ugwiResolver.resolve( aMasterObject );
      if( subMasterUgwi != null ) {
        subMastersMap.put( subCfg.id(), subMasterUgwi );
      }
    }
    // iterate over all actors to resolve it's properties
    for( IVedItemCfg srcCfg : aCfg.actorCfgs() ) {
      // read property resolver configs
      IStringMap<ICompoundResolverConfig> propResolverConfigsMap =
          srcCfg.extraData().readStridMap( VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS, CompoundResolverConfig.KEEPER );
      // if nothing to resolve then just copy source VED item config to destination
      if( propResolverConfigsMap.isEmpty() ) {
        cfg.actorCfgs().add( srcCfg );
        continue;
      }
      // find master UGWI for this actor
      String submasterId = mrConfig.actorSubmasterIds().findByKey( srcCfg.id() );
      Ugwi destMaster = null;
      if( submasterId != null ) {
        destMaster = subMastersMap.findByKey( submasterId );
      }
      // Ugwi destMaster = subMastersMap.findByKey( srcCfg.id() );
      if( destMaster == null ) {
        destMaster = aMasterObject;
      }
      VedItemCfg destCfg = new VedItemCfg( srcCfg );
      // resolve all resolvable properties
      for( String propId : propResolverConfigsMap.keys() ) {
        ICompoundResolverConfig crc = propResolverConfigsMap.getByKey( propId );
        IUgwiResolver resolver = CompoundResolver.create( crc, aSkConn, registry );
        Ugwi destUgwi = resolver.resolve( destMaster );
        if( destUgwi != null ) {
          destCfg.propValues().setValobj( propId, destUgwi );
        }
        else {
          LoggerUtils.errorLogger().error( "Resolve failed for property: \"%s\"", propId ); //$NON-NLS-1$
          destCfg.propValues().setValue( propId, IAtomicValue.NULL );
        }
      }
      cfg.actorCfgs().add( destCfg );
    }
    cfg.extraData().writeItem( VED_SCREEN_MAIN_MNEMO_MASTER_UGWI, aMasterObject, Ugwi.KEEPER );
    return cfg;
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

}
