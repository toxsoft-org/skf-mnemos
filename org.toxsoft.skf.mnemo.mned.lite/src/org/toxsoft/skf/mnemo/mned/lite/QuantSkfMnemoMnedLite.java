package org.toxsoft.skf.mnemo.mned.lite;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.skf.mnemo.mned.lite.actors.*;
import org.toxsoft.skf.mnemo.mned.lite.m5.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.impl.*;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantSkfMnemoMnedLite
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkfMnemoMnedLite() {
    super( QuantSkfMnemoMnedLite.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    RtControlFactoriesRegistry rtcFact = new RtControlFactoriesRegistry();
    aAppContext.set( IRtControlFactoriesRegistry.class, rtcFact );
    rtcFact.register( RtcRectangle.FACTORY );
    rtcFact.register( RtcEllipse.FACTORY );
    rtcFact.register( RtcLine.FACTORY );
    rtcFact.register( RtcLinearGauge.FACTORY );
    rtcFact.register( RtcLabel.FACTORY );
    rtcFact.register( RtcFancyLamp.FACTORY );
    rtcFact.register( RtcRectLamp.FACTORY );
    rtcFact.register( RtcCircleLamp.FACTORY );
    rtcFact.register( RtcPictureLamp.FACTORY );
    rtcFact.register( RtcAttrValueView.FACTORY );
    rtcFact.register( RtcAttrInput.FACTORY );
    rtcFact.register( RtcRtdataValueView.FACTORY );
    rtcFact.register( RtcRtdataInput.FACTORY );
    rtcFact.register( RtcImage.FACTORY );
    rtcFact.register( RtcCmdCheckbox.FACTORY );
    rtcFact.register( RtcCmdButton.FACTORY );

    rtcFact.register( RtcAttrCheckbox.FACTORY );
    rtcFact.register( RtcRtdataCheckbox.FACTORY );
    rtcFact.register( RtcCmdInput.FACTORY );

    IVedActorFactoriesRegistry actReg = aAppContext.get( IVedActorFactoriesRegistry.class );
    actReg.register( LiteActorLamp.FACTORY );
    actReg.register( LiteActorPictureLamp.FACTORY );
    actReg.register( LiteActorAttrCheckbox.FACTORY );
    actReg.register( LiteActorRtdataCheckbox.FACTORY );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkfMnemMnedLiteConstants.init( aWinContext );

    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new RtControlM5Model() );
  }

}
