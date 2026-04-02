package org.toxsoft.skf.mnemo.mned.lite.actors;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.uskat.core.*;

/**
 * Base class for checkbox actors.
 *
 * @author vs
 */
public abstract class AbstractLiteActorCheckbox
    extends AbstractSkVedButtonActor {

  private Gwid gwid = null;

  GwidList gwidList = new GwidList();

  private IAtomicValue lastValue = IAtomicValue.NULL;

  protected final ISkCoreApi coreApi;

  IVedVisel visel = null;

  IButtonClickHandler buttonHandler = aVisel -> {
    if( visel != null ) {
      boolean checked = visel.props().getBool( PROPID_ON_OFF_STATE );
      setValue( gwid, avBool( !checked ) );
    }
  };

  protected AbstractLiteActorCheckbox( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );
    coreApi = aVedScreen.tsContext().get( ISkVedEnvironment.class ).skConn().coreApi();
    setButtonClickHandler( buttonHandler );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_GWID ) ) {
      gwid = null;
      gwidList.clear();
      IAtomicValue av = aChangedValues.getValue( PROPID_GWID );
      if( av.isAssigned() ) {
        gwid = av.asValobj();
        if( gwid != null ) {
          gwidList.add( gwid );
        }
      }
      onGwidChanged( gwid );
    }
    if( aChangedValues.hasKey( PROPID_VISEL_ID ) ) {
      String viselId = aChangedValues.getStr( PROPID_VISEL_ID );
      if( vedScreen().model().visels().list().hasKey( viselId ) ) {
        visel = vedScreen().model().visels().list().getByKey( viselId );
      }
    }
  }

  @Override
  protected IGwidList doListUsedGwids() {
    return gwidList;
  }

  @Override
  public final void whenRealTimePassed( long aRtTime ) {
    if( gwid != null ) {
      IAtomicValue newValue = getValue( gwid );
      if( !newValue.equals( lastValue ) ) {
        lastValue = newValue;
        doOnValueChanged( newValue );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected abstract IAtomicValue getValue( Gwid aGwid );

  protected abstract void setValue( Gwid aGwid, IAtomicValue aBoolValue );

  protected abstract void onGwidChanged( Gwid aGwid );

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  protected void doOnValueChanged( IAtomicValue aNewValue ) {
    String viselId = props().getStr( PROP_VISEL_ID );
    String viselPropId = ViselCheckbox.TFI_CHECKED.id();
    setViselPropValue( viselId, viselPropId, aNewValue );
  }

}
