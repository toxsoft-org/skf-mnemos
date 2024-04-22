package org.toxsoft.skf.mnemo.skide.glib;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.vpcalc.*;
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

  private final IViewportCalculator vpCalc = new ViewportCalculator( new CalculationStrategySettings( //
      ETsFulcrum.LEFT_TOP, //
      EVpFulcrumUsageStartegy.INSIDE, //
      // EVpBoundingStrategy.NONE, //
      // EVpBoundingStrategy.CONTENT, //
      EVpBoundingStrategy.VIEWPORT, //
      // new TsPoint( 10, 10 ), //
      new TsPoint( 0, 0 ), //
      false //
  ) );

  private final IVedScreen vedScreen;

  private final Canvas canvas;

  private final ScrollBar hBar;

  private final ScrollBar vBar;

  private final IVedScreenView vedView;

  boolean horPositive = true;
  boolean verPositive = true;

  MnemoScrollManager( IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
    vedView = vedScreen.view();
    canvas = (Canvas)vedView.getControl();

    vedView.configChangeEventer().addListener( aSource -> {
      ID2Point size = vedView.canvasConfig().size();
      ID2Conversion d2conv = vedView.getConversion();
      vpCalc.setContentSize( new D2Size( size.x(), size.y() ) );
      Rectangle r = canvas.getClientArea();
      if( r.width > 0 && r.height > 0 ) {
        vpCalc.setViewportBounds( new TsRectangle( r.x, r.y, r.width, r.height ) );
      }
      vpCalc.queryConversionChange( d2conv );
      vpCalc.queryToChangeOrigin( 0, 0 );
    } );

    hBar = canvas.getHorizontalBar();
    hBar.setMinimum( 0 );
    hBar.setMinimum( 100 );
    hBar.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        vpCalc.queryToChangeOriginByScrollBars( hBar.getSelection(), vBar.getSelection() );
      }

    } );

    vBar = canvas.getVerticalBar();
    vBar.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        vpCalc.queryToChangeOriginByScrollBars( hBar.getSelection(), vBar.getSelection() );
      }

    } );

    canvas.addControlListener( new ControlListener() {

      @Override
      public void controlResized( ControlEvent aE ) {
        ITsRectangle vpBounds = TsGraphicsUtils.tsFromRect( canvas.getClientArea() );
        // onViewportSizeChanged( vpBounds ); // Sol++
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
    vpCalc.queryToChangeOrigin( aX, aY );
  }

  private void whenCalculatorOutputChanges() {
    vpCalc.output().horBarSettings().applyTo( hBar );
    hBar.setSelection( vpCalc.output().horBarSettings().selection() );
    vpCalc.output().verBarSettings().applyTo( vBar );
    vedView.configChangeEventer().pauseFiring();
    vedView.setConversion( vpCalc.output().conversion() );

    vedView.redraw();
    vedView.configChangeEventer().resumeFiring( false );
  }

  // ------------------------------------------------------------------------------------
  // Sol++ Implementation
  //

  void onViewportSizeChanged( ITsRectangle aVpBounds ) {
    vpCalc.setViewportBounds( aVpBounds );
  }

}
