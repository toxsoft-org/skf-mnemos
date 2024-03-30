package org.toxsoft.skf.mnemo.gui.tsgui;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class MultiSelectionAndGroupsDecorator
    extends VedAbstractDecorator {

  class SelectionListener
      implements IGenericChangeListener {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      vedScreen().view().redraw();
      vedScreen().view().update();
      // if( multiSelection ) {
      // vedScreen().view().redraw();
      // vedScreen().view().update();
      // }
      // if( selectionManager.selectedViselIds().isEmpty() ) {
      // multiSelection = false;
      // }
    }
  }

  private static final Color                   colorWhite = new Color( 255, 0, 255 );
  private final IVedViselSelectionGroupManager selectionManager;
  private final SelectionListener              selectionListener;

  private static final int selLineWidth = 2;

  // ------------------------------------------------------------------------------------
  // VedAbstractDecorator
  //

  public MultiSelectionAndGroupsDecorator( IVedScreen aScreen, IVedViselSelectionGroupManager aSelectionManager ) {
    super( aScreen );
    selectionManager = TsNullArgumentRtException.checkNull( aSelectionManager );
    selectionListener = new SelectionListener();
    selectionManager.genericChangeEventer().addListener( selectionListener );
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    if( selectionManager.selectionKind() == ESelectionKind.MULTI ) {
      aPaintContext.gc().setAdvanced( true );
      // aPaintContext.gc().setLineWidth( selLineWidth );
      aPaintContext.gc().setForeground( colorWhite );
      aPaintContext.gc().setXORMode( true );
      ID2Conversion d2Conv = vedScreen().view().getConversion();
      Transform screenTransform = D2TransformUtils.d2ConversionToTransfrom( aPaintContext.gc(), d2Conv );
      for( String id : selectionManager.selectedViselIds() ) {
        VedAbstractVisel visel = vedScreen().model().visels().list().getByKey( id );
        setViselTransform( aPaintContext, visel, screenTransform );
        paintViselSelection( aPaintContext, visel );
      }
      aPaintContext.gc().setTransform( screenTransform );
      for( String id : selectionManager.groupsManager().groupIds() ) {
        paintGroupSelection( aPaintContext, id );
      }
      aPaintContext.gc().setXORMode( false );
      screenTransform.dispose();
    }
  }

  @Override
  public boolean isYours( double aX, double aY ) {
    return false;
  }

  @Override
  public ID2Rectangle bounds() {
    return new D2Rectangle( 0, 0, 1, 1 );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void setViselTransform( ITsGraphicsContext aGc, VedAbstractVisel aVisel, Transform aDefaultTransform ) {
    ID2Conversion viselConv = aVisel.getConversion();
    if( viselConv == ID2Conversion.NONE ) {
      aGc.gc().setTransform( aDefaultTransform );
      return;
    }
    ID2Conversion d2Conv = vedScreen().view().getConversion();
    Transform itemTransform = D2TransformUtils.d2ConversionToTransfrom( aGc.gc(), d2Conv );
    D2TransformUtils.convertItemTransfrom( itemTransform, viselConv, aVisel.rotationX(), aVisel.rotationY() );
    aGc.gc().setTransform( itemTransform );
    itemTransform.dispose();
  }

  private static void paintViselSelection( ITsGraphicsContext aPaintContext, VedAbstractVisel aVisel ) {
    ID2Rectangle d2r = aVisel.bounds();
    int x = -selLineWidth;
    int y = -selLineWidth;
    int w = (int)(d2r.width() + 2 * selLineWidth);
    int h = (int)(d2r.height() + 2 * selLineWidth);
    aPaintContext.gc().setLineWidth( selLineWidth );
    aPaintContext.gc().drawRectangle( x, y, w, h );
  }

  private void paintGroupSelection( ITsGraphicsContext aPaintContext, String aGroupId ) {
    ID2Rectangle d2r = calcGroupScreenRect( aGroupId );
    int x = (int)(d2r.x1() - selLineWidth);
    int y = (int)(d2r.y1() - selLineWidth);
    int w = (int)(d2r.width() + 2 * selLineWidth);
    int h = (int)(d2r.height() + 2 * selLineWidth);
    aPaintContext.gc().drawRectangle( x, y, w, h );
  }

  ID2Rectangle calcGroupScreenRect( String aGroupId ) {
    ID2Rectangle result = null;
    IStringList viselIds = selectionManager.groupsManager().listViselIds( aGroupId );
    IStridablesList<VedAbstractVisel> visels = vedScreen().model().visels().list();
    for( String id : viselIds ) {
      VedAbstractVisel visel = visels.getByKey( id );
      ID2Rectangle vr = calcScreenRect( visel );
      if( result == null ) {
        result = vr;
      }
      else {
        result = D2GeometryUtils.union( vr, result );
      }
    }
    return result;
  }

  ID2Rectangle calcScreenRect( VedAbstractVisel aVisel ) {
    ID2Rectangle vr = aVisel.bounds();
    IVedCoorsConverter converter = vedScreen().view().coorsConverter();
    ID2Point[] points = new ID2Point[4];
    points[0] = converter.visel2Screen( 0, 0, aVisel );
    points[1] = converter.visel2Screen( vr.width(), 0, aVisel );
    points[2] = converter.visel2Screen( vr.width(), vr.height(), aVisel );
    points[3] = converter.visel2Screen( 0, vr.height(), aVisel );

    double minX = points[0].x();
    double minY = points[0].y();
    double maxX = points[0].x();
    double maxY = points[0].y();

    for( int i = 0; i < 4; i++ ) {
      if( points[i].x() < minX ) {
        minX = points[i].x();
      }
      if( points[i].y() < minY ) {
        minY = points[i].y();
      }
      if( points[i].x() > maxX ) {
        maxX = points[i].x();
      }
      if( points[i].y() > maxY ) {
        maxY = points[i].y();
      }
    }
    return new D2Rectangle( minX, minY, maxX - minX, maxY - minY );
  }

}
