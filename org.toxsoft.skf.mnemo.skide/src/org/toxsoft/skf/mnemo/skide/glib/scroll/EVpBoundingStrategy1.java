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
import org.toxsoft.core.tslib.math.*;
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
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize,
        ITsPoint aMargins ) {
      return aOrigin;
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
      // TODO Auto-generated method stub
      return null;
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
      // TODO Auto-generated method stub
      return null;
    }
  },

  /**
   * Content edge location is limited by the respective edge of the viewport.
   */
  VIEWPORT( "none", "STR_BS_VIEWPORT", "STR_BS_VIEWPORT_D", true ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize,
        ITsPoint aMargins ) {
      // adjust X
      ITsPoint us = calcUnderLayingSize( aVpRect, aContentSize, aMargins );
      ITsPoint shift = calcContentShift( aVpRect, aContentSize, aMargins );

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
      //
      //
      // int x = (int)aOrigin.x();
      // IntRange horRange;
      // if( aContentSize.x() > aVpRect.width() ) {
      // horRange = new IntRange( aVpRect.x2() - aContentSize.x(), aVpRect.x1() );
      // }
      // else {
      // horRange = new IntRange( aVpRect.x1(), aVpRect.x1() + aVpRect.x2() - aContentSize.x() );
      // }
      // x = adaptToRange( x, horRange );
      //
      // int y = (int)aOrigin.y();
      // IntRange verRange;
      // if( aContentSize.y() > aVpRect.height() ) {
      // verRange = new IntRange( aVpRect.y2() - aContentSize.y(), aVpRect.y1() );
      // }
      // else {
      // verRange = new IntRange( aVpRect.y1(), aVpRect.y1() + aVpRect.y2() - aContentSize.y() );
      // }
      // y = adaptToRange( y, verRange );
      return new D2Point( x, y );
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
      int width = aContentSize.x();
      if( aContentSize.x() < aVpRect.width() ) { // содержимое меньше окна просмотра по ширине
        int dx = aVpRect.width() - aContentSize.x();
        width = aVpRect.width() + dx;
      }
      int height = aContentSize.y();
      if( aContentSize.y() < aVpRect.height() ) { // содержимое меньше окна просмотра по высоте
        int dy = aVpRect.height() - aContentSize.y();
        height = aVpRect.height() + dy;
      }
      return new TsPoint( width, height );
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
      int dx = 0;
      int dy = 0;
      if( aContentSize.x() < aVpRect.width() ) { // содержимое меньше окна просмотра по ширине
        dx = aVpRect.width() - aContentSize.x() - aMargins.x();
      }
      if( aContentSize.y() < aVpRect.height() ) { // содержимое меньше окна просмотра по высоте
        dy = aVpRect.height() - aContentSize.y() - aMargins.y();
      }
      return new TsPoint( dx, dy );
    }
  },

  /**
   * Content edge location is limited by the opposite edge of the viewport.
   */
  CONTENT( "none", "STR_BS_CONTENT", "STR_BS_CONTENT_D", true ) { //$NON-NLS-1$

    @Override
    protected ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize,
        ITsPoint aMargins ) {
      ITsPoint us = calcUnderLayingSize( aVpRect, aContentSize, aMargins );
      ITsPoint shift = calcContentShift( aVpRect, aContentSize, aMargins );

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

      // int x = (int)aOrigin.x();
      // // IntRange horRange = getVerRange( aVpRect, aMargins );
      // IntRange horRange = getHorRange( aVpRect, aMargins );
      // if( horRange.isLeft( x + aContentSize.x() ) ) {
      // x = horRange.minValue() - aContentSize.x();
      // }
      // else {
      // if( horRange.isRight( x ) ) {
      // x = horRange.maxValue();
      // }
      // }
      // // check Y
      // int y = (int)aOrigin.y();
      // IntRange verRange = getVerRange( aVpRect, aMargins );
      // if( verRange.isLeft( y + aContentSize.y() ) ) {
      // y = verRange.minValue() - aContentSize.y();
      // }
      // else {
      // if( verRange.isRight( y ) ) {
      // y = verRange.maxValue();
      // }
      // }
      // if( x == aOrigin.x() && y == aOrigin.y() ) {
      // return aOrigin;
      // }
      return new D2Point( x, y );
    }

    @Override
    protected ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
      int width = aContentSize.x();
      if( aContentSize.x() < aVpRect.width() ) { // содержимое меньше окна просмотра по ширине
        width = 2 * aVpRect.width() + aContentSize.x() - 32;
      }
      int height = aContentSize.y();
      if( aContentSize.y() < aVpRect.height() ) { // содержимое меньше окна просмотра по высоте
        height = 2 * aVpRect.height() + aContentSize.y() - 32;
      }
      return new TsPoint( width, height );
    }

    @Override
    protected ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
      int dx = 0;
      if( aContentSize.x() < aVpRect.width() ) { // содержимое меньше окна просмотра по ширине
        dx = aVpRect.width() - aMargins.x() - 16;
      }
      int dy = 0;
      if( aContentSize.y() < aVpRect.height() ) { // содержимое меньше окна просмотра по высоте
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

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IntRange getHorRange( ITsRectangle aVpRect, ITsPoint aMargins ) {
    if( aMargins.x() >= aVpRect.width() / 2 ) { // for small viewport do NOT apply margins
      return new IntRange( aVpRect.x1(), aVpRect.x2() );
    }
    return new IntRange( aVpRect.x1() + aMargins.x(), aVpRect.x2() - aMargins.y() );
  }

  private static IntRange getVerRange( ITsRectangle aVpRect, ITsPoint aMargins ) {
    if( aMargins.y() >= aVpRect.height() / 2 ) { // for small viewport do NOT apply margins
      return new IntRange( aVpRect.y1(), aVpRect.y2() );
    }
    return new IntRange( aVpRect.y1() + aMargins.y(), aVpRect.y2() - aMargins.y() );
  }

  private static int adaptToRange( int aValue, IntRange aRange ) {
    if( aValue < aRange.minValue() ) {
      return aRange.minValue();
    }
    if( aValue > aRange.maxValue() ) {
      return aRange.maxValue();
    }
    return aValue;
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
   * @param aContentSize {@link ITsPoint} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated origin to be applied
   */
  public ID2Point calcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
    TsNullArgumentRtException.checkNulls( aOrigin, aVpRect, aContentSize, aMargins );
    return doCalcOrigin( aOrigin, aVpRect, aContentSize, aMargins );
  }

  /**
   * Вычисляет размеры прокручиваемой подложки.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentSize {@link ITsPoint} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated size
   */
  public ITsPoint calcUnderLayingSize( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
    return doCalcUnderlayingSize( aVpRect, aContentSize, aMargins );
  }

  /**
   * Вычисляет размеры прокручиваемой подложки.
   *
   * @param aVpRect {@link ITsRectangle} - the viewport
   * @param aContentSize {@link ITsPoint} - the content bounding rectangle size
   * @param aMargins {@link ITsPoint} - margins to apply when limiting content
   * @return {@link ID2Point} - calculated size
   */
  public ITsPoint calcContentShift( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins ) {
    return doCalcContentShift( aVpRect, aContentSize, aMargins );
  }

  protected abstract ID2Point doCalcOrigin( ID2Point aOrigin, ITsRectangle aVpRect, ITsPoint aContentSize,
      ITsPoint aMargins );

  protected abstract ITsPoint doCalcUnderlayingSize( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins );

  protected abstract ITsPoint doCalcContentShift( ITsRectangle aVpRect, ITsPoint aContentSize, ITsPoint aMargins );

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
