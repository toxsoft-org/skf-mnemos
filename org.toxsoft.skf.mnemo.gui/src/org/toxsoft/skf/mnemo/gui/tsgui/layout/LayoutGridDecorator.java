package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;

/**
 * Декоратор отображающий сетку размещения визуальных элементов.
 *
 * @author vs
 */
public class LayoutGridDecorator
    extends VedAbstractDecorator {

  private final String viselId;

  private final IVedViselsLayoutManager layoutManager;

  private final IVedViselsMasterSlaveRelationsManager msManager;

  Color lineColor = new Color( 128, 0, 128 );

  /**
   * Constructor.
   *
   * @param aViselId String - ИД визеля
   * @param aLayoutManager {@link IVedViselsLayoutManager} - менеджер размещений
   * @param aMsManager {@link IVedViselsMasterSlaveRelationsManager} - менеджер отношений родитель-ребенок
   * @param aScreen {@link IVedScreen} - экран мнемосхемы
   */
  public LayoutGridDecorator( String aViselId, IVedViselsLayoutManager aLayoutManager,
      IVedViselsMasterSlaveRelationsManager aMsManager, IVedScreen aScreen ) {
    super( aScreen );
    viselId = TsNullArgumentRtException.checkNull( aViselId );
    layoutManager = aLayoutManager;
    msManager = aMsManager;
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractDecorator
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    IVedVisel visel = VedScreenUtils.findVisel( viselId, vedScreen() );
    if( visel != null ) {
      ID2Rectangle bounds = visel.bounds();
      IVedViselsLayoutController controller = layoutManager.layoutController( viselId );
      IList<ID2Rectangle> cellRects = controller.calcCellRects( viselId, msManager.listSlaveViselIds( viselId ) );

      aPaintContext.gc().setLineWidth( 1 );
      aPaintContext.gc().setLineStyle( SWT.LINE_DASH );
      aPaintContext.gc().setForeground( lineColor );
      for( ID2Rectangle r : cellRects ) {
        aPaintContext.gc().drawRectangle( (int)(r.x1() - bounds.x1()), (int)(r.y1() - bounds.y1()), (int)r.width(),
            (int)r.height() );
      }
    }

  }

  @Override
  public boolean isYours( double aX, double aY ) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public ID2Rectangle bounds() {
    // TODO Auto-generated method stub
    return null;
  }

}
