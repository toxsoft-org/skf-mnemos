package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Изменяемая допустимая реализация {@link ID2Margins}.
 *
 * @author vs
 */
public sealed class D2Margins
    implements ID2Margins permits D2GridMargins {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "D2Margins"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ID2Margins> KEEPER =
      new AbstractEntityKeeper<>( ID2Margins.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ID2Margins aEntity ) {
          aSw.writeDouble( aEntity.left() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.right() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.top() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.bottom() );
          aSw.writeSeparatorChar();
        }

        @Override
        protected ID2Margins doRead( IStrioReader aSr ) {
          double left = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double right = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double top = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double bottom = aSr.readDouble();
          aSr.ensureSeparatorChar();
          return new D2Margins( left, top, right, bottom );
        }

      };

  private double left   = 0;
  private double right  = 0;
  private double top    = 0;
  private double bottom = 0;

  /**
   * Constructor.
   * <p>
   * Sets every option to default value 0.
   */
  public D2Margins() {
    this( 0 );
  }

  /**
   * Constructor.
   *
   * @param aInitVal double - initial value of all options
   */
  public D2Margins( double aInitVal ) {
    left = aInitVal;
    right = aInitVal;
    top = aInitVal;
    bottom = aInitVal;
  }

  /**
   * Constructor.
   *
   * @param aLeft double - distance between the internals and the left edge of the panel
   * @param aRight double - distance between the internals and the right edge of the panel
   * @param aTop double - distance between the internals and the top edge of the panel
   * @param aBottom double - distance between the internals and the bottom edge of the panel
   */
  public D2Margins( double aLeft, double aRight, double aTop, double aBottom ) {
    left = aLeft;
    right = aRight;
    top = aTop;
    bottom = aBottom;
  }

  // ------------------------------------------------------------------------------------
  // ID2Margins
  //

  @Override
  public double left() {
    return left;
  }

  @Override
  public double right() {
    return right;
  }

  @Override
  public double top() {
    return top;
  }

  @Override
  public double bottom() {
    return bottom;
  }

}
