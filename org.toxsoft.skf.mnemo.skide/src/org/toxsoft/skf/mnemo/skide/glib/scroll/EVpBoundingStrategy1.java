package org.toxsoft.skf.mnemo.skide.glib.scroll;

import org.toxsoft.core.tsgui.graphics.vpcalc.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * How the content placement will be limited by the viewport bounds.
 *
 * @author hazard157
 */
public enum EVpBoundingStrategy1
    implements IStridable {

  /**
   * Content may be located anywhere even if it is not visible through the viewport.
   */
  NONE( "none", "STR_BS_NONE", "STR_BS_NONE_D", false ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect,
        ITsPoint aMargins ) {
      return aOrigin;
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      TsRectangle r =
          new TsRectangle( -aContentRect.x1(), -aContentRect.y1(), aContentRect.width(), aContentRect.height() );
      return TsGeometryUtils.union( aVpRect, r ).size();
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      ITsRectangle r = TsGeometryUtils.union( aVpRect, aContentRect );
      return new TsPoint( aContentRect.x1() - r.x1(), aContentRect.y1() - r.y1() );
    }
  },

  /**
   * Content edge location is limited by the respective edge of the viewport.
   */
  VIEWPORT( "none", "STR_BS_VIEWPORT", "STR_BS_VIEWPORT_D", true ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect,
        ITsPoint aMargins ) {
      // adjust X
      ITsPoint us = calcUnderLayingSize( aVpRect, aContentRect, aMargins );
      ITsPoint shift = calcContentShift( aVpRect, aContentRect, aMargins );

      int x = (int)aOrigin.x();
      int y = (int)aOrigin.y();
      int maxX = us.x() - aVpRect.width();
      int maxY = us.y() - aVpRect.height();

      if( x + shift.x() < 0 ) {
        x = -shift.x();
      }
      if( x + shift.x() > maxX ) {
        x = maxX - shift.x();
      }

      if( y + shift.y() < 0 ) {
        y = -shift.y();
      }
      if( y + shift.y() > maxY ) {
        y = maxY - shift.y();
      }
      return new D2Point( x, y );
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      int width = aContentRect.width();
      if( aContentRect.width() < aVpRect.width() ) { // содержимое меньше окна просмотра по ширине
        int dx = aVpRect.width() - aContentRect.width();
        width = aVpRect.width() + dx;
      }
      int height = aContentRect.height();
      if( aContentRect.height() < aVpRect.height() ) { // содержимое меньше окна просмотра по высоте
        int dy = aVpRect.height() - aContentRect.height();
        height = aVpRect.height() + dy;
      }
      return new TsPoint( width, height );
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      int dx = 0;
      int dy = 0;
      if( aContentRect.width() < aVpRect.width() ) { // содержимое меньше окна просмотра по ширине
        dx = aVpRect.width() - aContentRect.width() - aMargins.x();
      }
      if( aContentRect.height() < aVpRect.height() ) { // содержимое меньше окна просмотра по высоте
        dy = aVpRect.height() - aContentRect.height() - aMargins.y();
      }
      return new TsPoint( dx, dy );
    }
  },

  /**
   * Content edge location is limited by the opposite edge of the viewport.
   */
  CONTENT( "none", "STR_BS_CONTENT", "STR_BS_CONTENT_D", true ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect,
        ITsPoint aMargins ) {
      ITsPoint us = calcUnderLayingSize( aVpRect, aContentRect, aMargins );
      ITsPoint shift = calcContentShift( aVpRect, aContentRect, aMargins );

      int x = (int)aOrigin.x();
      int y = (int)aOrigin.y();
      int maxX = us.x() - aVpRect.width();
      int maxY = us.y() - aVpRect.height();

      if( x + shift.x() < 0 ) {
        x = -shift.x();
      }
      if( x + shift.x() > maxX ) {
        x = maxX - shift.x();
      }

      if( y + shift.y() < 0 ) {
        y = -shift.y();
      }
      if( y + shift.y() > maxY ) {
        y = maxY - shift.y();
      }
      return new D2Point( x, y );
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      int width = aContentRect.width();
      if( aContentRect.width() < aVpRect.width() ) { // содержимое меньше окна просмотра по ширине
        width = 2 * aVpRect.width() + aContentRect.width() - 32;
      }
      int height = aContentRect.height();
      if( aContentRect.height() < aVpRect.height() ) { // содержимое меньше окна просмотра по высоте
        height = 2 * aVpRect.height() + aContentRect.height() - 32;
      }
      return new TsPoint( width, height );
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
      int dx = 0;
      if( aContentRect.width() < aVpRect.width() ) { // содержимое меньше окна просмотра по ширине
        dx = aVpRect.width() - aMargins.x() - 16;
      }
      int dy = 0;
      if( aContentRect.height() < aVpRect.height() ) { // содержимое меньше окна просмотра по высоте
        dy = aVpRect.height() - aMargins.y() - 16;
      }
      return new TsPoint( dx, dy );
    }

  };

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "EVpBoundingStrategy"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<EVpBoundingStrategy1> KEEPER =
      new StridableEnumKeeper<>( EVpBoundingStrategy1.class );

  private static IStridablesListEdit<EVpBoundingStrategy1> list = null;

  private final String  id;
  private final String  name;
  private final String  description;
  private final boolean bounded;

  EVpBoundingStrategy1( String aId, String aName, String aDescription, boolean aBounded ) {
    id = aId;
    name = aName;
    description = aDescription;
    bounded = aBounded;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if any kind of bounding has to be applied.
   *
   * @return boolean - <code>true</code> content is bounded, <code>false</code> - no limits
   */
  public boolean isBounded() {
    return bounded;
  }

  /**
   * Calculates the bounded origin of the content for current strategy.
   *
   * @param aOrigin {@link ID2Point} - the requested origin
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentRect {@link ITsRectangle} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated origin to be applied
   */
  public ID2Point calcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
    TsNullArgumentRtException.checkNulls( aOrigin, aVpRect, aContentRect, aMargins );
    return doCalcOrigin( aOrigin, aVpRect, aContentRect, aMargins );
  }

  /**
   * Вычисляет размеры прокручиваемой подложки.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentRect {@link ITsRectangle} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated size
   */
  public ITsPoint calcUnderLayingSize( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
    return doCalcUnderlayingSize( aVpRect, aContentRect, aMargins );
  }

  /**
   * Вычисляет размеры прокручиваемой подложки.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentRect {@link ITsRectangle} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated size
   */
  public ITsPoint calcContentShift( ITsRectangle aVpRect, ITsRectangle aContentRect, ITsPoint aMargins ) {
    return doCalcContentShift( aVpRect, aContentRect, aMargins );
  }

  protected abstract ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsRectangle aContentRect,
      ITsPoint aMargins );

  protected abstract ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsRectangle aContentSize,
      ITsPoint aMargins );

  protected abstract ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsRectangle aContentSize, ITsPoint aMargins );

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link EVpBoundingStrategy} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<EVpBoundingStrategy1> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link EVpBoundingStrategy} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static EVpBoundingStrategy1 getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EVpBoundingStrategy} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EVpBoundingStrategy1 findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( EVpBoundingStrategy1 item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link EVpBoundingStrategy} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static EVpBoundingStrategy1 getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
