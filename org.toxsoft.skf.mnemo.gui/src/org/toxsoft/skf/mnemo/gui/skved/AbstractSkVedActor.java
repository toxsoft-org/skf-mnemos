package org.toxsoft.skf.mnemo.gui.skved;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * USkat-specific VISELs base class.
 * <p>
 * Introduces {@link ISkVedEnvironment}.
 *
 * @author hazard157, vs
 */
public abstract class AbstractSkVedActor
    extends VedAbstractActor
    implements ISkConnected {

  private final ISkVedEnvironment skVedEnv;

  protected AbstractSkVedActor( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    skVedEnv = tsContext().get( ISkVedEnvironment.class );
    skVedEnv.registerActor( this );
  }

  // ------------------------------------------------------------------------------------
  // ISkConnected
  //

  @Override
  public ISkConnection skConn() {
    return skVedEnv.skConn();
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the SkVED environment for VED screen.
   *
   * @return {@link ISkVedEnvironment} - the SkVED environment
   */
  public ISkVedEnvironment skVedEnv() {
    return skVedEnv;
  }

  @Override
  protected void doDispose() {
    skVedEnv().unregisterActor( this );
    super.doDispose();
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation must return the used GWIDs list.
   *
   * @return {@link IGwidList} - the GWIDs used by the actor
   */
  protected abstract IGwidList doListUsedGwids();

  // ------------------------------------------------------------------------------------
  // Static methods
  //

  protected static void removeWrongUgwi( String aPropId, String aUgwiKindId, IOptionSetEdit aValues ) {
    if( aValues.hasKey( aPropId ) ) {
      IAtomicValue av = aValues.getValue( aPropId );
      if( !av.isAssigned() ) {
        aValues.remove( aPropId );
      }
      else {
        Ugwi ug = av.asValobj();
        if( ug != Ugwi.NONE && !ug.kindId().equals( aUgwiKindId ) ) {
          aValues.remove( aPropId );
        }
      }
    }
  }

}
