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
          CellLayoutData.KEEPER.write( aSw, aEntity.cellData );
        }

        @Override
        protected TableColumnLayoutData doRead( IStrioReader aSr ) {
          Double left = Double.valueOf( aSr.readDouble() );
          aSr.ensureSeparatorChar();
          Double right = Double.valueOf( aSr.readDouble() );
          aSr.ensureSeparatorChar();
          boolean grabSpace = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          CellLayoutData cld = CellLayoutData.KEEPER.read( aSr );
          return new TableColumnLayoutData( new Pair<>( left, right ), grabSpace, cld );
        }

      };

  private final Pair<Double, Double> minMaxWidth;

  private final boolean grabExcessSpace;

  private final CellLayoutData cellData;

  /**
   * Конструктор
   */
  public TableColumnLayoutData() {
    minMaxWidth = new Pair<>( Double.valueOf( -1. ), Double.valueOf( -1. ) );
    grabExcessSpace = false;
    cellData = new CellLayoutData();
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aMinMaxWidth Pair&lt;Double, Double> - диапазон допустимых значений ширины колонки не <code>null</code>
   * @param aGrabExcessSpace boolean - признак того, нужно ли захватывать избыточную ширину
   * @param aCellData CellLayoutData - параметры размещения ячеек столбца
   */
  public TableColumnLayoutData( Pair<Double, Double> aMinMaxWidth, boolean aGrabExcessSpace,
      CellLayoutData aCellData ) {
    TsNullArgumentRtException.checkNull( aMinMaxWidth );
    minMaxWidth = aMinMaxWidth;
    grabExcessSpace = aGrabExcessSpace;
    cellData = aCellData;
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
   * Возвращает информацию о размещении ячеек столбца.
   *
   * @return {@link CellLayoutData} - информация о размещении ячеек столбца
   */
  public CellLayoutData cellData() {
    return cellData;
  }

  /**
   * Возвращает фиксированную ширину колонки, если заданная минимальная ширина равна максимальной. В противном случае
   * возвращает значение < 0.
   *
   * @return double фиксированную ширину колонки или отрицательное значение если фиксированная ширина не задана
   */
  public double fixedWidth() {
    double min = minMaxWidth.left().doubleValue();
    double max = minMaxWidth.right().doubleValue();
    if( minMaxWidth != null && Double.compare( min, max ) == 0 ) {
      return minMaxWidth.left().doubleValue();
    }
    return -1;
  }
}
