package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Информация, необходимая при вычисления ширины колонки для многоколоночных типов размещений.
 *
 * @author vs
 */
public class TableColumnLayoutData {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "TableColumnLayoutData"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<TableColumnLayoutData> KEEPER =
      new AbstractEntityKeeper<>( TableColumnLayoutData.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, TableColumnLayoutData aEntity ) {
          aSw.writeDouble( aEntity.widthRange().left().doubleValue() );
          aSw.writeSeparatorChar();
          aSw.writeDouble( aEntity.widthRange().right().doubleValue() );
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aEntity.grabExcessSpace() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.span() );
        }

        @Override
        protected TableColumnLayoutData doRead( IStrioReader aSr ) {
          Double left = Double.valueOf( aSr.readDouble() );
          aSr.ensureSeparatorChar();
          Double right = Double.valueOf( aSr.readDouble() );
          aSr.ensureSeparatorChar();
          boolean grabSpace = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          int span = aSr.readInt();
          return new TableColumnLayoutData( new Pair<>( left, right ), grabSpace, span );
        }

      };

  private final Pair<Double, Double> minMaxWidth;

  private final boolean grabExcessSpace;

  private final int span;

  /**
   * Конструктор
   */
  public TableColumnLayoutData() {
    minMaxWidth = new Pair<>( Double.valueOf( -1. ), Double.valueOf( -1. ) );
    grabExcessSpace = false;
    span = 1;
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aMinMaxWidth Pair&lt;Double, Double> - диапазон допустимых значений ширины колонки не <code>null</code>
   * @param aGrabExcessSpace boolean - признак того, нужно ли захватывать избыточную ширину
   * @param aSpan int - количество перекрытых строк
   */
  public TableColumnLayoutData( Pair<Double, Double> aMinMaxWidth, boolean aGrabExcessSpace, int aSpan ) {
    TsNullArgumentRtException.checkNull( aMinMaxWidth );
    minMaxWidth = aMinMaxWidth;
    grabExcessSpace = aGrabExcessSpace;
    span = aSpan;
  }

  /**
   * Диапазон допустимых значений ширины колонки.<br>
   * <code>null</code> - означает отсутствие ограничений на значение ширины, левое значение меньшее 0 - означает
   * отсутствие ограничений на минимальное значение ширины, правое - на максимальное.
   *
   * @return Pair&lt;Double, Double> - диапазон допустимых значений ширины колонки
   */
  public Pair<Double, Double> widthRange() {
    return minMaxWidth;
  }

  /**
   * Возвращает признак того, нужно ли захватывать избыточную ширину.
   *
   * @return <b>true</b> - нужно захватить избыточную ширину<br>
   *         <b>false</b> - не нужно
   */
  public boolean grabExcessSpace() {
    return grabExcessSpace;
  }

  /**
   * Количество перекрытых колонок.
   *
   * @return int - количество перекрытых колонок
   */
  int span() {
    return span;
  }

}
