package org.toxsoft.skf.mnemo.gui.mastobj;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMnemoResolverConfig} editable implementation.
 *
 * @author hazard157
 */
public class MnemoResolverConfig
    implements IMnemoResolverConfig {

  /**
   * The keeper singleton.
   * <p>
   * Returned value may be safely casted to {@link MnemoResolverConfig}.
   */
  public static final IEntityKeeper<IMnemoResolverConfig> KEEPER =
      new AbstractEntityKeeper<>( IMnemoResolverConfig.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IMnemoResolverConfig aEntity ) {
          aSw.incNewLine();
          SubmasterConfig.KEEPER.writeColl( aSw, aEntity.subMasters(), true );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          StrioUtils.writeStringMap( aSw, EMPTY_STRING, aEntity.actorSubmasterIds(), StringKeeper.KEEPER, true );
          aSw.decNewLine();
        }

        @Override
        protected IMnemoResolverConfig doRead( IStrioReader aSr ) {
          MnemoResolverConfig cfg = new MnemoResolverConfig();
          SubmasterConfig.KEEPER.readColl( aSr, cfg.subMasters() );
          aSr.ensureSeparatorChar();
          IStringMap<String> mm = StrioUtils.readStringMap( aSr, EMPTY_STRING, StringKeeper.KEEPER );
          for( String id : mm.keys() ) {
            cfg.defineActorSubmaster( id, mm.getByKey( id ) );
          }
          return cfg;
        }
      };

  private final IStridablesListEdit<SubmasterConfig> subMasters        = new StridablesList<>();
  private final IStringMapEdit<String>               actorSubmasterIds = new StringMap<>();

  /**
   * Constructor.
   */
  public MnemoResolverConfig() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IMnemoResolverConfig
  //

  @Override
  public IStridablesListEdit<SubmasterConfig> subMasters() {
    return subMasters;
  }

  @Override
  public IStringMap<String> actorSubmasterIds() {
    return actorSubmasterIds;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Defines sub-master to be used for VED actor properties resolving.
   * <p>
   * Note: method does <b>not</b> checks if referred actor or sub-master exists.
   *
   * @param aActorId String - actor ID
   * @param aSubMasterId String - sub-master ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public void defineActorSubmaster( String aActorId, String aSubMasterId ) {
    StridUtils.checkValidIdPath( aActorId );
    StridUtils.checkValidIdPath( aSubMasterId );
    actorSubmasterIds.put( aActorId, aSubMasterId );
  }

  /**
   * Removes entry from {@link #actorSubmasterIds()} if exists.
   *
   * @param aActorId String - the actor ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void removeActor( String aActorId ) {
    actorSubmasterIds.removeByKey( aActorId );
  }

  /**
   * removes all entires from the map {@link #actorSubmasterIds()}.
   */
  public void clearActorSubmasterIds() {
    actorSubmasterIds.clear();
  }

}
