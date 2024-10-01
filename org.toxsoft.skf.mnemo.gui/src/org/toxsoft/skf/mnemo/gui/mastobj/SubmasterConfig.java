package org.toxsoft.skf.mnemo.gui.mastobj;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;

/**
 * Description how to create sub-master UGWI from the master UGWI.
 * <p>
 * This is final immutable class.
 *
 * @author hazard157
 */
public final class SubmasterConfig
    implements IStridableParameterized {

  /**
   * Пустая конфигурация
   */
  public static final SubmasterConfig EMPTY =
      new SubmasterConfig( IStridable.NONE_ID, IOptionSet.NULL, CompoundResolverConfig.NONE );

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "SubmasterConfig"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<SubmasterConfig> KEEPER =
      new AbstractEntityKeeper<>( SubmasterConfig.class, EEncloseMode.ENCLOSES_BASE_CLASS, EMPTY ) {

        @Override
        protected void doWrite( IStrioWriter aSw, SubmasterConfig aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params );
          aSw.writeSeparatorChar();
          CompoundResolverConfig.KEEPER.write( aSw, aEntity.resolverCfg );
        }

        @Override
        protected SubmasterConfig doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          ICompoundResolverConfig resolverConfig = CompoundResolverConfig.KEEPER.read( aSr );
          return new SubmasterConfig( id, params, resolverConfig );
        }
      };

  private final String                  id;
  private final IOptionSet              params;
  private final ICompoundResolverConfig resolverCfg;

  private SubmasterConfig( String aId, IOptionSet aParams, ICompoundResolverConfig aResolverConfig ) {
    id = aId;
    params = aParams;
    resolverCfg = aResolverConfig;
  }

  /**
   * Static constructor.
   *
   * @param aId String - sub-master ID (an IDpath)
   * @param aParams - values of {@link #params()}, including name and description
   * @param aResolverConfig {@link ICompoundResolverConfig} - the sub-master resolver configuration
   * @return {@link SubmasterConfig} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not and IDpath
   */
  public static SubmasterConfig create( String aId, IOptionSet aParams, ICompoundResolverConfig aResolverConfig ) {
    StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aParams, aResolverConfig );
    return new SubmasterConfig( aId, new OptionSet( aParams ), aResolverConfig );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return DDEF_NAME.getValue( params ).asString();
  }

  @Override
  public String description() {
    return DDEF_DESCRIPTION.getValue( params ).asString();
  }

  // ------------------------------------------------------------------------------------
  // Parameterized
  //

  @Override
  public IOptionSet params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the configuration to create sub-master resolver.
   *
   * @return {@link ICompoundResolverConfig} - sub-master resolver configuration
   */
  public ICompoundResolverConfig resolverCfg() {
    return resolverCfg;
  }

}
