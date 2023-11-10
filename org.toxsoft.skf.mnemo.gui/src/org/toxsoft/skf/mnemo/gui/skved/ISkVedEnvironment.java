package org.toxsoft.skf.mnemo.gui.skved;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Environment for the VED screen content to access to the USkat runtime functionality.
 * <p>
 * Environment instance one per VED screen must be placed in {@link IVedScreen#tsContext()}.
 * <p>
 * After use, the instance must be closed. However this is restartable entity - {@link #restart()} may be called
 * multiple times, even after {@link #close()}. Event more, {@link #restart()} <b>must</b> be called after any
 * {@link #registerActor(AbstractSkVedActor)} or {@link #unregisterActor(AbstractSkVedActor)} calls.
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

  /**
   * Sends the USkat command.
   * <p>
   * Sender may poll command state via {@link ISkCommand#state()} and {@link ISkCommand#isComplete()}.
   *
   * @param aCmdGwid {@link Gwid} - concrete GWID of the command
   * @param aAuthorSkid {@link Skid} - command sender ID
   * @param aArgs {@link IOptionSet} - command arguments
   * @return {@link ISkCommand} - the sent command
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException illegal or non-existing command GWID
   * @throws TsIllegalArgumentRtException illegal or non-existing author SKID
   * @throws AvTypeCastRtException incompatible argument type
   */
  ISkCommand sendCommand( Gwid aCmdGwid, Skid aAuthorSkid, IOptionSet aArgs );

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

  /**
   * Restarts the environment so that changes in actors take effect.
   * <p>
   * May be called multiple times even after {@link #close()}.
   */
  void restart();

  /**
   * Returns the connection to work with.
   *
   * @return {@link ISkConnection} - the connection
   */
  ISkConnection skConn();

}
