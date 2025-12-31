package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

/**
 * Базовый класс акторов, которые потребляют единственное данное реального времени.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractSkActorSingleRtDataConsumer
    extends AbstractSkVedActor {

  private Ugwi         ugwi      = null;
  private Gwid         gwid      = null;
  private IUgwiList    ugwiList  = IUgwiList.EMPTY;
  private IAtomicValue lastValue = null;

  protected AbstractSkActorSingleRtDataConsumer( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs,
      VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractActor
  //

  @Override
  protected final void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // check and don't allow to set invalid UGWI
    removeWrongUgwi( PROPID_RTD_UGWI, UgwiKindSkRtdata.KIND_ID, aValuesToSet, coreApi() );
    doDoInterceptPropsChange( aNewValues, aValuesToSet );
  }

  @Override
  protected final void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_RTD_UGWI ) ) {
      gwid = null;
      ugwi = Ugwi.NONE;
      ugwiList = IUgwiList.EMPTY;
      IAtomicValue av = aChangedValues.getValue( PROPID_RTD_UGWI );
      if( av.isAssigned() ) {
        ugwi = av.asValobj();
        if( ugwi != null && ugwi != Ugwi.NONE ) {
          gwid = UgwiKindSkRtdata.INSTANCE.getGwid( ugwi );
          ugwiList = UgwiList.createDirect( new ElemArrayList<>( ugwi ) );
        }
      }
    }
    doDoUpdateCachesAfterPropsChange( aChangedValues );
  }

  @Override
  public final void whenRealTimePassed( long aRtTime ) {
    if( gwid != null ) {
      IAtomicValue newValue = skVedEnv().getRtDataValue( gwid );
      if( !newValue.equals( lastValue ) ) {
        lastValue = newValue;
        doOnValueChanged( newValue );
      }
    }
    doWhenRealTimePassed( aRtTime );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected final IGwidList doListUsedGwids() {
    GwidList gl = new GwidList();
    for( Ugwi u : ugwiList.items() ) {
      gl.add( UgwiKindSkRtdata.INSTANCE.getGwid( u ) );
    }
    return gl;
  }

  // ------------------------------------------------------------------------------------
  // To use
  //

  protected Ugwi ugwi() {
    return ugwi;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract void doOnValueChanged( IAtomicValue aNewValue );

  // ------------------------------------------------------------------------------------
  // To override
  //

  @SuppressWarnings( "unused" )
  protected void doWhenRealTimePassed( long aRtTime ) {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doDoUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doDoInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // nop
  }
}
