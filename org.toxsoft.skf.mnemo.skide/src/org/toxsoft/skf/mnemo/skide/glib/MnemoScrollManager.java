package org.toxsoft.skf.mnemo.skide.glib;

import org.eclipse.swt.events.*;
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
      EVpBoundingStrategy.CONTENT, //
      new TsPoint( 10, 10 ), //
      false //
  ) );

  private final IVedScreen vedScreen;

  private final Canvas canvas;

  private final ScrollBar hBar;

  private final ScrollBar vBar;

  private final IVedScreenView vedView;

  MnemoScrollManager( IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
    vedView = vedScreen.view();

    vedView.configChangeEventer().addListener( aSource -> {
      ID2Point size = vedView.canvasConfig().size();
      vpCalc.setContentSize( new D2Size( size.x(), size.y() ) );
      vpCalc.queryConversionChange( vedView.getConversion() );
    } );

    canvas = (Canvas)vedView.getControl();

    hBar = canvas.getHorizontalBar();
    hBar.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        ScrollBarSettings sbs1 = vpCalc.output().horBarSettings();
        ScrollBarSettings sbs2 = ScrollBarSettings.of( hBar );
        int queryDelta = sbs2.selection() - sbs1.selection();
        vpCalc.queryToShift( queryDelta, 0 );
      }

    } );

    vBar = canvas.getVerticalBar();
    vBar.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        ScrollBarSettings sbs1 = vpCalc.output().verBarSettings();
        ScrollBarSettings sbs2 = ScrollBarSettings.of( vBar );
        int queryDelta = sbs2.selection() - sbs1.selection();
        vpCalc.queryToShift( 0, queryDelta );
      }

    } );

    canvas.addControlListener( new ControlListener() {

      @Override
      public void controlResized( ControlEvent aE ) {
        ITsRectangle vpBounds = TsGraphicsUtils.tsFromRect( canvas.getClientArea() );
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

  private void whenCalculatorOutputChanges() {
    vpCalc.output().horBarSettings().applyTo( hBar );
    vpCalc.output().verBarSettings().applyTo( vBar );
    vedView.configChangeEventer().pauseFiring();
    vedView.setConversion( vpCalc.output().conversion() );
    vedView.redraw();
    vedView.configChangeEventer().resumeFiring( false );
  }

}
