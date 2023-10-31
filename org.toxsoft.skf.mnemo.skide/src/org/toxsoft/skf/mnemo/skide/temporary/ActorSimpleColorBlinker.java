package org.toxsoft.skf.mnemo.skide.temporary;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Simple blinker switches VISEL's property color ON/OFF with specified PERIOD.
 *
 * @author hazard157
 */
public class ActorSimpleColorBlinker
    extends VedAbstractActor {

  private static final String PROPID_PERIOD    = "hz.Period";   //$NON-NLS-1$
  private static final String PROPID_ON_COLOR  = "hz.onColor";  //$NON-NLS-1$
  private static final String PROPID_OFF_COLOR = "hz.offColor"; //$NON-NLS-1$

  private static final IDataDef PROP_PERIOD = DataDef.create( PROPID_PERIOD, INTEGER, //
      TSID_NAME, "Period (1/10 sec)", //
      TSID_DESCRIPTION, "The blinking period specified in 1/10th of secodns (that is in 100 msec units)", //
      TSID_MIN_INCLUSIVE, AV_1, // 0,1 second
      TSID_MAX_INCLUSIVE, avInt( 50 ), // 5 seconds
      TSID_DEFAULT_VALUE, avInt( 5 ) //
  );

  private static final IDataDef PROP_ON_COLOR = DataDef.create3( PROPID_ON_COLOR, DT_COLOR_RGBA, //
      TSID_NAME, "ON color", //
      TSID_DESCRIPTION, "The blinking color in ON state", //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.RED.rgba() ) //
  );

  private static final IDataDef PROP_OFF_COLOR = DataDef.create3( PROPID_OFF_COLOR, DT_COLOR_RGBA, //
      TSID_NAME, "OFF color", //
      TSID_DESCRIPTION, "The blinking color in OFF state", //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.DARK_GRAY.rgba() ) //
  );

  private static final ITinFieldInfo TFI_PERIOD    = new TinFieldInfo( PROP_PERIOD, TTI_AT_INTEGER );
  private static final ITinFieldInfo TFI_ON_COLOR  = new TinFieldInfo( PROP_ON_COLOR, TtiRGBA.INSTANCE );
  private static final ITinFieldInfo TFI_OFF_COLOR = new TinFieldInfo( PROP_OFF_COLOR, TtiRGBA.INSTANCE );

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = "hz.SimpleColorBlinker"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, "SimpleColorBlinker", //
      TSID_DESCRIPTION, "Switches specified VISEL's specified color property periodicaly between two colors", //
      TSID_ICON_ID, ICONID_VED_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_PERIOD );
      fields.add( TFI_ON_COLOR );
      fields.add( TFI_OFF_COLOR );
      return new PropertableEntitiesTinTypeInfo<>( fields, ActorSimpleColorBlinker.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ActorSimpleColorBlinker( aCfg, propDefs(), aVedScreen );
    }

  };

  private boolean state            = false;
  private long    cachedDeltaMsecs = 1_000_000L; // the half-period
  private long    lastTimestamp    = 0L;

  private ActorSimpleColorBlinker( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_PERIOD ) ) {
      cachedDeltaMsecs = 100 * props().getInt( PROP_PERIOD ) / 2;
    }
  }

  // @Override
  // public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
  // String viselId = props().getStr( PROP_VISEL_ID );
  // VedAbstractVisel visel = findVisel( viselId );
  // if( visel != null ) {
  // ID2Point mouseCoors = coorsConverter().swt2Visel( aCoors, visel );
  // if( visel.isYours( mouseCoors ) ) {
  // vedScreen().view().setCursor( cursorManager().getCursor( ECursorType.HAND ) );
  // }
  // else {
  // vedScreen().view().setCursor( null );
  // }
  // }
  // return false;
  // }

  // @Override
  // public boolean onMouseUp( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
  //
  // TsTestUtils.pl( "MOUSE UP" );
  //
  // String viselId = props().getStr( PROP_VISEL_ID );
  // IVedVisel visel = findVisel( viselId );
  // if( visel != null ) {
  // /**
  // * TODO here we need methods of the base class to convert coordinates from virtual to SWT and vice versa<br>
  // * TODO determine if mouse is in bounds of the visel
  // */
  //
  // boolean isInVisel = true;
  // double x = 30.0 + visel.props().getDouble( PROP_X );
  // double y = 20.0 + visel.props().getDouble( PROP_Y );
  // ID2Conversion d2c = visel.getConversion();
  // d2c = new D2Conversion( D2Angle.ofDegrees( d2c.rotation().degrees() + 16 ), d2c.zoomFactor() * 1.1,
  // new D2Point( x, y ) );
  // if( x > 300.0 ) {
  // x = 180.0;
  // y = 100.0;
  // d2c = ID2Conversion.NONE;
  // }
  // visel.props().setPropPairs( //
  // PROP_X, x, //
  // PROP_Y, y, //
  // PROP_TRANSFORM, avValobj( d2c ) //
  // );
  //
  // }
  // return false;
  // }

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    if( aRtTime - lastTimestamp > cachedDeltaMsecs ) {
      state = !state;
      IAtomicValue avColor;
      if( state ) {
        avColor = props().getValue( PROPID_ON_COLOR );
      }
      else {
        avColor = props().getValue( PROPID_OFF_COLOR );
      }
      if( setStdViselPropValue( avColor ) ) {
        vedScreen().view().redrawVisel( props().getStr( PROP_VISEL_ID ) );
      }
      lastTimestamp = aRtTime;
    }
  }

}
