package org.toxsoft.skf.mnemo.gui.tsgui.comps;

import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Отрисовщик состояний кнопки, который для отрисовки кнопки в различных состояниях использует соотвествующее
 * изображение из набора.
 *
 * @author vs
 */
public class ImagesetButtonRenderer
    extends AbstractButtonRenderer {

  private final IMapEdit<EButtonViselState, TsImage> images = new ElemMap<>();

  private final static ID2Point EMPTY_IMG_SIZE = new D2Point( 32, 32 );

  private ID2Point imageSize = EMPTY_IMG_SIZE;

  /**
   * Constructor.
   *
   * @param aButton IViselButton - визуальная кнопка
   */
  public ImagesetButtonRenderer( IViselButton aButton ) {
    super( aButton );
  }

  void setImages( IMap<EButtonViselState, TsImageDescriptor> aImages, ITsImageManager aImageManager ) {
    imageSize = EMPTY_IMG_SIZE;
    for( EButtonViselState state : aImages.keys() ) {
      TsImageDescriptor imd = aImages.getByKey( state );
      TsImage img = aImageManager.getImage( imd );
      images.put( state, img );
    }
    if( aImages.size() > 0 ) {
      ITsPoint tsp = images.values().first().imageSize();
      imageSize = new D2Point( tsp.x(), tsp.y() );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractButtonRenderer
  //

  @Override
  public ID2Point getPackedSize( double aWidth, double aHeight ) {
    return imageSize;
  }

  @Override
  protected void doUpdate() {
    // nop
  }

  @Override
  protected void paintBackground( ITsGraphicsContext aPaintContext ) {

    EButtonViselState state = buttonState();
    // System.out.println( state.nmName() );
    if( images.hasKey( state ) ) {
      aPaintContext.gc().drawImage( images.getByKey( state ).image(), 0, 0 );
    }
    else {
      aPaintContext.fillRect( 0, 0, 32, 32 );
    }
  }

}
