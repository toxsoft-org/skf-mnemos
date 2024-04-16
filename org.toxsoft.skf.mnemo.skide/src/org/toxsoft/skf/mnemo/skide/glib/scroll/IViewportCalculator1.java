package org.toxsoft.skf.mnemo.skide.glib.scroll;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.vpcalc.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Calculates content positioning and transformation parameters depending on various parameters.
 * <p>
 * TODO describe what the viewport / content is
 *
 * @author hazard157
 */
public interface IViewportCalculator1 {

  /**
   * Returns the calculation strategy settings as specified at calculator creation.
   *
   * @return {@link CalculationStrategySettings} - the calculation strategy parameters
   */
  CalculationStrategySettings1 settings();

  /**
   * Returns the fit mode.
   *
   * @return {@link ERectFitMode} - fit mode
   */
  ERectFitMode fitMode();

  /**
   * Determines if small objects are expanded to fit viewport.
   *
   * @return boolean - <code>true</code> to expand small images in {@link ERectFitMode#isAdaptiveScale()} modes
   */
  boolean isExpandToFit();

  /**
   * Sets the content fitiing parameters.
   *
   * @param aFitMode {@link ERectFitMode} - fit mode
   * @param aExpandToFit boolean - <code>true</code> to expand small images in adaptive scale modes
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean setFitParams( ERectFitMode aFitMode, boolean aExpandToFit );

  /**
   * Informs calculator about size of the un-transformed content.
   * <p>
   * This method must be called every time when content size changes, eg when new image is loaded.
   *
   * @param aContentSize {@link ID2Size} - the content size in pixels
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean setContentSize( ID2Size aContentSize );

  /**
   * Informs calculator about viewport change.
   * <p>
   * Method must be called every time when viewport changes, eg when control is resized.
   * <p>
   * Coordinates of the viewport rectangle is given in the painting {@link GC} coordinates space.
   *
   * @param aViewportBounds {@link ITsRectangle} - viewport bounds rectangle in pixels
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean setViewportBounds( ITsRectangle aViewportBounds );

  /**
   * Queries to change the content transformation.
   *
   * @param aConversion {@link ID2Conversion} - the content transformation
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean queryConversionChange( ID2Conversion aConversion );

  /**
   * Query to re-locate the content in the viewport.
   * <p>
   * Request is done to place the content drawing top-left point at the specified screen (canvas) coordinates.
   *
   * @param aX int - new screen (drawing) X coordinate in the pixels
   * @param aY int - new screen (drawing) Y coordinate in the pixels
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean queryToChangeOrigin( int aX, int aY );

  /**
   * Query to re-locate the content in the viewport.
   * <p>
   * Request is done to shift the content drawing top-left point at the specified screen (canvas) coordinates on the
   * specified amount of pixels.
   *
   * @param aDeltaX int - origin horizontal shift value in pixels (>0 move right, <0 move left)
   * @param aDeltaY int - origin vertical shift value in pixels (>0 move down, <0 move up)
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean queryToShift( int aDeltaX, int aDeltaY );

  /**
   * Query to re-locate the content in the viewport.
   *
   * @param aHorMove {@link ETsCollMove} - horizontal location settings
   * @param aVerMove {@link ETsCollMove} - vertical location settings
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean queryToMove( ETsCollMove aHorMove, ETsCollMove aVerMove );

  /**
   * Queries to change the content zoom factor keeping the content point at the specified place.
   * <p>
   * This method works as if user stops mouse cursor at <code>(aX,aY)</code> point and CTRL+wheel action requests the
   * zoom factor change, while user expects that image point under cursor remain on it's place.
   *
   * @param aX int - screen (drawing) X coordinate in pixels
   * @param aY int - screen (drawing) Y coordinate in pixels
   * @param aZoomMultiplier double - value to multiply current zoom factor on
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean queryToZoomFromPoint( int aX, int aY, double aZoomMultiplier );

  /**
   * Returns the output (calculated) parameters of the content painting.
   * <p>
   * Note: calculation results has an eventer to observe any changes in drawing paremeters.
   *
   * @return {@link IViewportOutput} - calculation results
   */
  IViewportOutput output();

  ITsPoint underlay2Viewport( ITsPoint aUnderlayPoint );

  ITsPoint viewport2Underlay( ITsPoint aViewportPoint );
}
