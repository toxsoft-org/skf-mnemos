package org.toxsoft.skf.mnemo.mned.lite.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.ved.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Standard handling of the VED screen canvas transform related actions.
 * <p>
 * Handles the actions:
 * <ul>
 * <li>{@link ITsStdActionDefs#ACTID_ZOOM_IN};</li>
 * <li>{@link ITsStdActionDefs#ACTID_ZOOM_ORIGINAL};</li>
 * <li>{@link ITsStdActionDefs#ACTID_ZOOM_OUT};</li>
 * <li>{@link ITsguiVedConstants#ACTID_OBJECT_ROTATE_LEFT};</li>
 * <li>{@link ITsguiVedConstants#ACTID_OBJECT_ROTATE_ORIGINAL};</li>
 * <li>{@link ITsguiVedConstants#ACTID_OBJECT_ROTATE_RIGHT};</li>
 * <li>TODO more actions ???.</li>
 * </ul>
 *
 * @author hazard157
 */
public class VedAspZoomActions
    extends MethodPerActionTsActionSetProvider {

  /**
   * Zoom factor increase/decrease amount.
   */
  private static final double deltaZoom = Math.sqrt( Math.sqrt( 2.0 ) ); // 4 times zoom means 2 times larger/smaller

  /**
   * Rotation step amount in degrees.
   */
  private static final double DELTA_ANGLE_DEG = 15.0;

  private final IVedScreen vedScreen;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to handle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAspZoomActions( IVedScreen aVedScreen ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    defineAction( ACDEF_ZOOM_IN, this::doHandleZoomIn, this::doIsEnabledZoomIn );
    defineAction( ACDEF_ZOOM_ORIGINAL, this::doHandleZoomOriginal, this::doIsEnabledZoomOriginal );
    defineAction( ACDEF_ZOOM_OUT, this::doHandleZoomOut, this::doIsEnabledZoomOut );
    defineAction( ACDEF_ZOOM_EXPAND_TO_FIT, this::doHandleZoomOut, this::doIsEnabledZoomOut );
    defineAction( ACDEF_ZOOM_FIT_BEST, this::doHandleZoomOut, this::doIsEnabledZoomOut );
    defineAction( ACDEF_ZOOM_FIT_FILL, this::doHandleZoomOut, this::doIsEnabledZoomOut );
    defineAction( ACDEF_ZOOM_FIT_HEIGHT, this::doHandleZoomOut, this::doIsEnabledZoomOut );
    defineAction( ACDEF_ZOOM_FIT_WIDTH, this::doHandleZoomOut, this::doIsEnabledZoomOut );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void doHandleZoomIn() {
    D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
    d2conv.setZoomFactor( D2Utils.ZOOM_RANGE.inRange( d2conv.zoomFactor() * deltaZoom ) );
    vedScreen.view().setConversion( d2conv );
    vedScreen.view().redraw();
  }

  boolean doIsEnabledZoomIn() {
    double zoomFactor = vedScreen.view().getConversion().zoomFactor();
    return zoomFactor < D2Utils.ZOOM_RANGE.maxValue();
  }

  void doHandleZoomOriginal() {
    double originalZoom = vedScreen.view().canvasConfig().conversion().zoomFactor();
    D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
    d2conv.setZoomFactor( D2Utils.ZOOM_RANGE.inRange( originalZoom ) );
    vedScreen.view().setConversion( d2conv );
    vedScreen.view().redraw();
  }

  boolean doIsEnabledZoomOriginal() {
    double zoomFactor = vedScreen.view().getConversion().zoomFactor();
    double originalZoom = vedScreen.view().canvasConfig().conversion().zoomFactor();
    return zoomFactor != originalZoom;
  }

  void doHandleZoomOut() {
    D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
    d2conv.setZoomFactor( D2Utils.ZOOM_RANGE.inRange( d2conv.zoomFactor() / deltaZoom ) );
    vedScreen.view().setConversion( d2conv );
    vedScreen.view().redraw();
  }

  boolean doIsEnabledZoomOut() {
    double zoomFactor = vedScreen.view().getConversion().zoomFactor();
    return zoomFactor > D2Utils.ZOOM_RANGE.minValue();
  }

  void doHandleRotateLeft() {
    D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
    d2conv.rotation().setDeg( d2conv.rotation().degrees() - DELTA_ANGLE_DEG );
    vedScreen.view().setConversion( d2conv );
    vedScreen.view().redraw();
  }

  void doHandleRotateOriginal() {
    D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
    d2conv.rotation().setDeg( vedScreen.view().canvasConfig().conversion().rotation().degrees() );
    vedScreen.view().setConversion( d2conv );
    vedScreen.view().redraw();
  }

  boolean doIsEnabledRotateOriginal() {
    ID2Angle rotationAngle = vedScreen.view().getConversion().rotation();
    ID2Angle originalAngle = vedScreen.view().canvasConfig().conversion().rotation();
    return D2Utils.compareDoubles( rotationAngle.degrees(), originalAngle.degrees() ) != 0;
  }

  void doHandleRotateRight() {
    D2ConversionEdit d2conv = new D2ConversionEdit( vedScreen.view().getConversion() );
    d2conv.rotation().setDeg( d2conv.rotation().degrees() + DELTA_ANGLE_DEG );
    vedScreen.view().setConversion( d2conv );
    vedScreen.view().redraw();
  }

}
