package org.toxsoft.skf.mnemo.mned.lite;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
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
    rtcFact.register( RtcLinearGauge.FACTORY );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkfMnemMnedLiteConstants.init( aWinContext );
  }

}
