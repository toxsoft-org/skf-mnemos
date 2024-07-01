package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Configuration of the simple UGWI resolver.
 * <p>
 * This is final immutable class.
 *
 * @author hazard157
 */
public final class SimpleResolverCfg
    implements IParameterized {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "SimpleResolverCfg"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<SimpleResolverCfg> KEEPER =
      new AbstractEntityKeeper<>( SimpleResolverCfg.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, SimpleResolverCfg aEntity ) {
          aSw.writeAsIs( aEntity.kindId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
        }

        @Override
        protected SimpleResolverCfg doRead( IStrioReader aSr ) {
          String kindId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          return new SimpleResolverCfg( kindId, params );
        }
      };

  private final String     kindId;
  private final IOptionSet params;

  /**
   * Constructor.
   *
   * @param aKindId String - resolver kind ID
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public SimpleResolverCfg( String aKindId, IOptionSet aParams ) {
    kindId = StridUtils.checkValidIdPath( aKindId );
    params = new OptionSet( aParams );
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSet params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the kind ID of the resolver.
   * <p>
   * Kind ID is used to find the factory {@link ISimpleResolverFactory} in the registry
   * {@link ISimpleResolverFactoriesRegistry}.
   *
   * @return String - the resolver kind ID
   */
  public String kindId() {
    return kindId;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return kindId + ": " + params.toString(); //$NON-NLS-1$
  }

}
