package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Изменяемая допустимая реализация {@link ID2GridMargins}.
 *
 * @author vs
 */
public final class D2GridMargins
    extends D2Margins
    implements ID2GridMargins {

  /**
   * The registered keeper ID.
   */
  @SuppressWarnings( "hiding" )
  public static final String KEEPER_ID = "TsGrisMargins"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<ID2GridMargins> KEEPER =
      new AbstractEntityKeeper<>( ID2GridMargins.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ID2GridMargins aEntity ) {
          aSw.writeDouble( aEntity.left() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.right() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.top() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.bottom() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.horGap() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.verGap() );
          aSw.writeSeparatorChar();
        }

        @Override
        protected ID2GridMargins doRead( IStrioReader aSr ) {
          double left = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double right = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double top = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double bottom = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double horGap = aSr.readDouble();
          aSr.ensureSeparatorChar();
          double verGap = aSr.readDouble();
          aSr.ensureSeparatorChar();
          return new D2GridMargins( left, right, top, bottom, horGap, verGap );
        }
      };

  private double horGap = 0;
  private double verGap = 0;

  /**
   * Constructor.
   * <p>
   * Sets every option to default value 0.
   */
  public D2GridMargins() {
    this( 0 );
  }

  /**
   * Constructor.
   *
   * @param aInitVal double - initial value of all options
   */
  public D2GridMargins( double aInitVal ) {
    super( aInitVal );
    horGap = aInitVal;
    verGap = aInitVal;
  }

  /**
   * Constructor.
   *
   * @param aLeft double - distance between the internals and the left edge of the panel
   * @param aRight double - distance between the internals and the right edge of the panel
   * @param aTop double - distance between the internals and the top edge of the panel
   * @param aBottom double - distance between the internals and the bottom edge of the panel
   * @param aHorGap double - horizontal distance between grid cells
   * @param aVerGap double - vertical distance between grid cells
   */
  public D2GridMargins( double aLeft, double aRight, double aTop, double aBottom, double aHorGap, double aVerGap ) {
    super( aLeft, aRight, aTop, aBottom );
    horGap = aHorGap;
    verGap = aVerGap;
  }

  // ------------------------------------------------------------------------------------
  // ID2GridMargins
  //

  @Override
  public double horGap() {
    return horGap;
  }

  @Override
  public double verGap() {
    return verGap;
  }

}
