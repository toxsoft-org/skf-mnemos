package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Информация, необходимая при вычисления высоты строки для многострочных типов размещений.
 *
 * @author vs
 */
public class TableRowLayoutData {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "TableRowLayoutData"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<TableRowLayoutData> KEEPER =
      new AbstractEntityKeeper<>( TableRowLayoutData.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TableRowLayoutData aEntity ) {
          aSw.writeDouble( aEntity.heightRange().left().doubleValue() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.heightRange().right().doubleValue() );
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aEntity.grabExcessSpace() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.span() );
        }

        @Override
        protected TableRowLayoutData doRead( IStrioReader aSr ) {
          Double left = Double.valueOf( aSr.readDouble() );
          aSr.ensureSeparatorChar();
          Double right = Double.valueOf( aSr.readDouble() );
          aSr.ensureSeparatorChar();
          boolean grabSpace = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          int span = aSr.readInt();
          return new TableRowLayoutData( new Pair<>( left, right ), grabSpace, span );
        }

      };

  private final Pair<Double, Double> minMaxHeight;

  private final boolean grabExcessSpace;

  private final int span;

  /**
   * Конструктор
   */
  public TableRowLayoutData() {
    minMaxHeight = new Pair<>( Double.valueOf( -1. ), Double.valueOf( -1. ) );
    grabExcessSpace = false;
    span = 1;
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aMinMaxHeight Pair&lt;Double, Double> - диапазон допустимых значений высоты колонки не <code>null</code>
   * @param aGrabExcessSpace boolean - признак того, нужно ли захватывать избыточную высоту
   * @param aSpan int - количество перекрытых строк
   */
  public TableRowLayoutData( Pair<Double, Double> aMinMaxHeight, boolean aGrabExcessSpace, int aSpan ) {
    TsNullArgumentRtException.checkNull( aMinMaxHeight );
    minMaxHeight = aMinMaxHeight;
    grabExcessSpace = aGrabExcessSpace;
    span = aSpan;
  }

  /**
   * Диапазон допустимых значений высоты колонки.<br>
   * <code>null</code> - означает отсутствие ограничений на значение высоты, левое значение меньшее 0 - означает
   * отсутствие ограничений на минимальное значение высоты, правое - на максимальное.
   *
   * @return Pair&lt;Double, Double> - диапазон допустимых значений высоты колонки
   */
  public Pair<Double, Double> heightRange() {
    return minMaxHeight;
  }

  /**
   * Возвращает признак того, нужно ли захватывать избыточную высоту.
   *
   * @return <b>true</b> - нужно захватить избыточную высоты<br>
   *         <b>false</b> - не нужно
   */
  public boolean grabExcessSpace() {
    return grabExcessSpace;
  }

  /**
   * Количество перекрытых строк.
   *
   * @return int - количество перекрытых строк
   */
  int span() {
    return span;
  }
}
