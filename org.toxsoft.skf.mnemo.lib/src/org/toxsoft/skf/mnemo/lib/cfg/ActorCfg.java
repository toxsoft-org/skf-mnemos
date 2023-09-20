package org.toxsoft.skf.mnemo.lib.cfg;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * {@link IActorCfg} implementation.
 *
 * @author -hazard157
 */
public final class ActorCfg
    extends StridableParameterized
    implements IActorCfg {

  public static final String PROPID_VISEL_ID = "viselId";

  /**
   * The keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<IActorCfg> KEEPER =
      new AbstractEntityKeeper<>( IActorCfg.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IActorCfg aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.factoryId() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.propValues() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
        }

        @Override
        protected IActorCfg doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String factoryId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          ActorCfg ac = new ActorCfg( id, factoryId, params );
          ac.propValues().setAll( propValues );
          return ac;
        }
      };

  private final String         factoryId;
  private final IOptionSetEdit propValues = new OptionSet();

  /**
   * Constructor.
   *
   * @param aId String - VISEL identifier
   * @param aFactoryId {@link String} - the VISEL factory ID
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ActorCfg( String aId, String aFactoryId, IOptionSet aParams ) {
    super( aId, aParams );
    StridUtils.checkValidIdPath( aFactoryId );
    factoryId = aFactoryId;
  }

  // ------------------------------------------------------------------------------------
  // IViselCfg
  //

  @Override
  public String factoryId() {
    return factoryId;
  }

  @Override
  public IOptionSetEdit propValues() {
    return propValues;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public String viselId() {
    if( propValues.hasKey( PROPID_VISEL_ID ) ) {
      IAtomicValue v = propValues.getValue( PROPID_VISEL_ID );
      if( v.isAssigned() ) {
        return v.asString();
      }
    }
    return TsLibUtils.EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "%s", factoryId.toString() ); //$NON-NLS-1$
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + factoryId.hashCode();
    result = TsLibUtils.PRIME * result + propValues.hashCode();
    return result;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof ActorCfg that ) {
      return this.factoryId.equals( that.factoryId ) && this.propValues.equals( that.propValues );
    }
    return false;
  }

  @Override
  public IKeepablesStorage extraData() {
    // TODO реализовать ActorCfg.extraData()
    throw new TsUnderDevelopmentRtException( "ActorCfg.extraData()" );
  }

}
