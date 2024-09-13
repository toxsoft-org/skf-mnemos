package org.toxsoft.skf.mnemo.skide.glib;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.vpcalc2.*;
import org.toxsoft.core.tsgui.graphics.vpcalc2.impl.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * Вспомогательный класс для реализации скроллинга мнемосхем.
 *
 * @author vs
 */
public class MnemoScrollManager {

  private final IVpCalc vpCalc = new VpCalc();

  // private final IViewportCalculator vpCalc = ViewportCalculator.create( new CalculationStrategySettings( //
  // ETsFulcrum.LEFT_TOP, //
  // EVpFulcrumUsageStartegy.INSIDE, //
  // // EVpBoundingStrategy.NONE, //
  // // EVpBoundingStrategy.CONTENT, //
  // EVpBoundingStrategy.VIEWPORT, //
  // // new TsPoint( 10, 10 ), //
  // new TsPoint( 0, 0 ), //
  // false //
  // ) );

  private final IVedScreen vedScreen;

  private final Canvas canvas;

  private final ScrollBar hBar;

  private final ScrollBar vBar;

  private final IVedScreenView vedView;

  boolean horPositive = true;
  boolean verPositive = true;

  private TsMargins margins = new TsMargins( 8, 8, 8, 8 );

  MnemoScrollManager( IVedScreen aVedScreen ) {

    vpCalc.cfg().setFulcrum( ETsFulcrum.LEFT_TOP );
    vpCalc.cfg().setFitMode( ERectFitMode.FIT_NONE );
    vpCalc.cfg().setMargins( margins );
    vpCalc.cfg().setBoundingStrategy( EVpBoundingStrategy.CONTENT );
    vpCalc.cfg().setFulcrumStartegy( EVpFulcrumStartegy.HINT );

    vedScreen = aVedScreen;
    vedView = vedScreen.view();
    canvas = (Canvas)vedView.getControl();
    hBar = canvas.getHorizontalBar();
    vBar = canvas.getVerticalBar();

    vedView.configChangeEventer().addListener( aSource -> {
      ID2Point size = vedView.canvasConfig().size();
      ID2Conversion d2conv = vedView.getConversion();
      vpCalc.setContentSize( new D2Size( Math.max( 1, size.x() ), Math.max( 1, size.y() ) ) );
      Rectangle r = canvas.getClientArea();
      if( r.width > 0 && r.height > 0 ) {
        vpCalc.setViewportBounds( new TsRectangle( r.x + margins.left(), r.y + margins.top(),
            r.width - +margins.left() - margins.right(), r.height - margins.top() - margins.bottom() ) );
      }
      // GOGA vpCalc.queryConversionChange( d2conv );
      vpCalc.setAngle( d2conv.rotation() );
      vpCalc.setDesiredZoom( d2conv.zoomFactor() );

      // GOGA vpCalc.queryToChangeOrigin( 0, 0 );
      // vpCalc.setDesiredOrigin( ITsPoint.ZERO );
      // ITsPoint p = new TsPoint( hBar.getSelection() + vpCalc.cfg().margins().left(),
      // vBar.getSelection() + vpCalc.cfg().margins().top() );
      // vpCalc.setDesiredOrigin( p );
    } );

    hBar.setMinimum( 0 );
    hBar.setMinimum( 100 );
    hBar.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        changeOriginByScrollbars();
      }

    } );

    vBar.setMinimum( 0 );
    vBar.setMinimum( 100 );
    vBar.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        changeOriginByScrollbars();
      }

    } );

    canvas.addControlListener( new ControlListener() {

      @Override
      public void controlResized( ControlEvent aE ) {
        ITsRectangle vpBounds = TsGraphicsUtils.tsFromRect( canvas.getClientArea() );
        // onViewportSizeChanged( vpBounds ); // Sol++
        TsMarginUtils.applyMargins( vpBounds, margins );
        if( vpCalc.setViewportBounds( vpBounds ) ) {
          vedScreen.view().redraw();
        }
      }

      @Override
      public void controlMoved( ControlEvent aE ) {
        ITsRectangle vpBounds = TsGraphicsUtils.tsFromRect( canvas.getClientArea() );
        if( vpCalc.setViewportBounds( vpBounds ) ) {
          vedScreen.view().redraw();
        }
      }
    } );

    vpCalc.output().genericChangeEventer().addListener( s -> whenCalculatorOutputChanges() );
  }

  public void setOrigin( int aX, int aY ) {
    // GOGA vpCalc.queryToChangeOrigin( aX, aY );
    vpCalc.setDesiredOrigin( new TsPoint( aX, aY ) );
  }

  private void whenCalculatorOutputChanges() {
    // GOGA vpCalc.output().horBarSettings().applyTo( hBar );
    ScrollBarCfg.applyTo( vpCalc.output().horBar(), hBar );

    // GOGA hBar.setSelection( vpCalc.output().horBarSettings().selection() );
    // GOGA vpCalc.output().verBarSettings().applyTo( vBar );
    ScrollBarCfg.applyTo( vpCalc.output().verBar(), vBar );

    vedView.configChangeEventer().pauseFiring();
    vedView.setConversion( vpCalc.output().d2Conv() );

    vedView.redraw();
    vedView.configChangeEventer().resumeFiring( false );
  }

  // ------------------------------------------------------------------------------------
  // Sol++ Implementation
  //

  void onViewportSizeChanged( ITsRectangle aVpBounds ) {
    vpCalc.setViewportBounds( aVpBounds );
  }

  /**
   * Безусловно меняет origin в соответствии со scrollbars, так как при конфигурировании scrollbars все огранияения были
   * применены и все проверки сделаны.
   */
  void changeOriginByScrollbars() {
    int x = hBar.getSelection();
    int y = vBar.getSelection();
    ID2Conversion conv = vedView.getConversion();
    D2ConversionEdit d2conv = new D2ConversionEdit( conv );
    d2conv.origin().setPoint( -x + vpCalc.cfg().margins().left(), -y + vpCalc.cfg().margins().top() );
    vedView.configChangeEventer().pauseFiring();
    vedView.setConversion( d2conv );
    vedView.configChangeEventer().resumeFiring( false );
    vedView.redraw();
  }
}
