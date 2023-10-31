package org.toxsoft.skf.mnemo.gui.skved;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.rtdserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * {@link ISkVedEnvironment} implementation.
 *
 * @author hazard157
 */
public class SkVedEnvironment
    implements ISkVedEnvironment, ISkConnected {

  private final IStridablesListEdit<AbstractSkVedActor> actorsList = new StridablesList<>();

  private final ISkConnection                          skConn;
  private final IMapEdit<Gwid, ISkReadCurrDataChannel> rtdChannels = new ElemMap<>();

  private boolean started = false;

  /**
   * Constructor.
   *
   * @param aSkConn {@link ISkConnected} - the connection to the USkat
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkVedEnvironment( ISkConnection aSkConn ) {
    TsNullArgumentRtException.checkNull( aSkConn );
    skConn = aSkConn;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void internalStart() {
    // collect list of unique used GWIDs
    GwidList allUsedGwids = new GwidList();
    for( AbstractSkVedActor actor : actorsList ) {
      for( Gwid g : actor.doListUsedGwids() ) {
        if( !allUsedGwids.hasElem( g ) ) {
          allUsedGwids.add( g );
        }
      }
    }
    // make list of RTDATA GWIDs
    GwidList rtdGwids = new GwidList();
    for( Gwid g : allUsedGwids ) {
      if( !g.isAbstract() && g.kind() == EGwidKind.GW_RTDATA ) {
        rtdGwids.add( g );
      }
    }
    // init read RTDATA channels
    IMap<Gwid, ISkReadCurrDataChannel> map = skRtdataServ().createReadCurrDataChannels( rtdGwids );
    rtdChannels.setAll( map );
  }

  private void internalClose() {
    // close read data channels
    while( !rtdChannels.isEmpty() ) {
      ISkReadCurrDataChannel ch = rtdChannels.removeByKey( rtdChannels.keys().first() );
      ch.close();
    }
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

  // ------------------------------------------------------------------------------------
  // ISkVedEnvironment
  //

  @Override
  public IAtomicValue getRtDataValue( Gwid aGwid ) {
    ISkReadCurrDataChannel ch = rtdChannels.findByKey( aGwid );
    if( ch != null ) {
      return ch.getValue();
    }
    return IAtomicValue.NULL;
  }

  @Override
  public String sendCommand( Gwid aCmdGwid, Skid aAuthorSkid, IOptionSet aArgs ) {
    // TODO реализовать SkVedEnvironment.sendCommand()
    throw new TsUnderDevelopmentRtException( "SkVedEnvironment.sendCommand()" );
  }

  @Override
  public boolean isCommandRunning( String aCmdInstanceId ) {
    // TODO реализовать SkVedEnvironment.isCommandRunning()
    throw new TsUnderDevelopmentRtException( "SkVedEnvironment.isCommandRunning()" );
  }

  @Override
  public SkCommandState getCommandState( String aCmdInstanceId ) {
    // TODO реализовать SkVedEnvironment.getCommandState()
    throw new TsUnderDevelopmentRtException( "SkVedEnvironment.getCommandState()" );
  }

  @Override
  public void registerActor( AbstractSkVedActor aActor ) {
    TsNullArgumentRtException.checkNull( aActor );
    TsIllegalStateRtException.checkTrue( started );
    TsItemAlreadyExistsRtException.checkTrue( actorsList.hasKey( aActor.id() ) );
    actorsList.add( aActor );
  }

  @Override
  public void unregisterActor( AbstractSkVedActor aActor ) {
    actorsList.remove( aActor );
  }

  @Override
  public void restart() {
    close();
    internalStart();
    started = true;
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    if( started ) {
      internalClose();
      started = false;
    }
  }

}
