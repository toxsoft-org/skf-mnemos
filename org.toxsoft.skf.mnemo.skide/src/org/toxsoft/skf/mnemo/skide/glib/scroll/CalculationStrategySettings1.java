package org.toxsoft.skf.mnemo.skide.glib.scroll;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.vpcalc.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Parameters determining how {@link IViewportCalculator} works.
 * <p>
 * This is an immutable class.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public final class CalculationStrategySettings1 {

  /**
   * Singleton of the settings: centered image (content).
   */
  public static final CalculationStrategySettings1 CENTERED_IMAGE = new CalculationStrategySettings1( //
      ETsFulcrum.CENTER, //
      EVpFulcrumUsageStartegy.INSIDE, //
      EVpBoundingStrategy1.VIEWPORT, //
      new TsPoint( 10, 10 ), //
      false //
  );

  private final ETsFulcrum              fulcrum;
  private final EVpFulcrumUsageStartegy fulcrumUsageStartegy;
  private final EVpBoundingStrategy1    boundingStrategy;
  private final ITsPoint                boundingMargins;
  private final boolean                 unlockFitMode;

  /**
   * Constructor.
   *
   * @param aFulcrum {@link ETsFulcrum} - placement fulcrum
   * @param aFulcrumUsageStartegy {@link EVpFulcrumUsageStartegy} - how to use fulcrum
   * @param aBoundingStrategy {@link EVpBoundingStrategy} - how to restrict content location
   * @param aBoundingMargins {@link ITsPoint} - gap values when restricting location
   * @param aUnlockFitMode boolean - <code>true</code> unlock fit mode when zoom or move is requested
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CalculationStrategySettings1( ETsFulcrum aFulcrum, EVpFulcrumUsageStartegy aFulcrumUsageStartegy,
      EVpBoundingStrategy1 aBoundingStrategy, ITsPoint aBoundingMargins, boolean aUnlockFitMode ) {
    TsNullArgumentRtException.checkNulls( aBoundingMargins, aFulcrum, aFulcrumUsageStartegy, aBoundingStrategy );
    fulcrum = aFulcrum;
    fulcrumUsageStartegy = aFulcrumUsageStartegy;
    boundingStrategy = aBoundingStrategy;
    boundingMargins = new TsPoint( aBoundingMargins );
    unlockFitMode = aUnlockFitMode;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public ETsFulcrum fulcrum() {
    return fulcrum;
  }

  public EVpFulcrumUsageStartegy fulcrumUsageStartegy() {
    return fulcrumUsageStartegy;
  }

  public EVpBoundingStrategy1 boundingStrategy() {
    return boundingStrategy;
  }

  public ITsPoint boundingMargins() {
    return boundingMargins;
  }

  public boolean isFitModeUnlocked() {
    return unlockFitMode;
  }

}
