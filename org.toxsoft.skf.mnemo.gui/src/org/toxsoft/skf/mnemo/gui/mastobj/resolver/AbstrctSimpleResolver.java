package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.utils.*;
import org.toxsoft.uskat.core.utils.*;
import org.toxsoft.uskat.core.utils.ugwi.*;

/**
 * Base implementation of the simple UGWI resolver created by {@link ISimpleResolverFactory}.
 *
 * @author hazard157
 */
public abstract class AbstrctSimpleResolver
    implements IUgwiResolver, ISkGuiContextable {

  private final IOptionSet    resolverConfig;
  private final ISkConnection skConn;

  /**
   * Constructor.
   *
   * @param aResolverConfig {@link IOptionSet} - resolver configuration, never is <code>null</code>
   * @param aSkConn {@link ISkConnection} - SK-connection, never is <code>null</code>
   */
  public AbstrctSimpleResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    resolverConfig = aResolverConfig;
    skConn = aSkConn;
  }

  // ------------------------------------------------------------------------------------
  // IUgwiResolver
  //

  @Override
  public Ugwi resolve( Ugwi aMaster ) {
    TsNullArgumentRtException.checkNull( aMaster );
    return doResolve( aMaster );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skConn;
  }

  // ------------------------------------------------------------------------------------
  // ISkGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return skConn.scope().get( ITsGuiContext.class );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the resolver configuration,
   *
   * @return {@link IOptionSet} - the resolver configuration
   */
  public IOptionSet cfg() {
    return resolverConfig;
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Subclass must calculate (resolve) the master UGWI,
   * <p>
   * To make implementation easier, this class implements {@link ISkConnected}, {@link ISkGuiContextable} and provides
   * resolver configuratuin by the method {@link #cfg()}.
   *
   * @param aMaster {@link Ugwi} - the master UGWI, never is <code>null</code>
   * @return {@link Ugwi} - calculated (resolved) UGWI
   */
  protected abstract Ugwi doResolve( Ugwi aMaster );
}
