package org.toxsoft.skf.mnemo.gui.skved;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Environment instance one per VED screen to be placed in {@link IVedScreen#tsContext()}.
 *
 * @author hazard157
 */
public interface ISkVedEnvironment
    extends ICloseable {

  /**
   * Returns the current value of the specified RTDATA.
   * <p>
   * Argument must be one of the used GWIDs {@link AbstractSkVedActor#doListUsedGwids()} of previously registered actor.
   * For unregistered or invalid GWIDs returns {@link IAtomicValue#NULL}.
   *
   * @param aGwid {@link Gwid} - concrete GWID of the {@link EGwidKind#GW_RTDATA} kind
   * @return {@link IAtomicValue} - the current value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IAtomicValue getRtDataValue( Gwid aGwid );

  String sendCommand( Gwid aCmdGwid, Skid aAuthorSkid, IOptionSet aArgs );

  boolean isCommandRunning( String aCmdInstanceId );

  SkCommandState getCommandState( String aCmdInstanceId );

  /**
   * Registers the actor.
   * <p>
   * Actors not registered before {@link #restart()} will be ignored.
   *
   * @param aActor {@link AbstractSkVedActor} - the actor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void registerActor( AbstractSkVedActor aActor );

  /**
   * Unregisters previously registered actor, if any.
   *
   * @param aActor {@link AbstractSkVedActor} - the actor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void unregisterActor( AbstractSkVedActor aActor );

  void restart();

  /**
   * Returns the connection to work with.
   *
   * @return {@link ISkConnection} - the connection
   */
  ISkConnection skConn();

}
