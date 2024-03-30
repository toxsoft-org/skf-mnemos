package org.toxsoft.skf.mnemo.gui.tsgui.layout.table;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

public class VedTableLayoutControllerConfig {

  // int rowsCount;
  //
  // int columnsCount;

  IList<TableRowLayoutData> rowDatas;

  IList<TableColumnLayoutData> columnDatas;

  IList<CellLayoutData> cellDatas;

  double hGap = 0;

  double vGap = 0;

  /**
   * Возвращает зазор между ячейками по горизонтали.
   *
   * @return double - зазор между ячейками по горизонтали
   */
  public double horizontalGap() {
    return hGap;
  }

  /**
   * Возвращает зазор между ячейками по вертикали.
   *
   * @return double - зазор между ячейками по вертикали
   */
  public double verticalGap() {
    return vGap;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  Pair<Integer, Integer> cellIndex2RowColumn( int aCellIndex ) {

    int row = aCellIndex / rowDatas.size();
    int column = aCellIndex - row * rowDatas.size();
    return new Pair<>( Integer.valueOf( row ), Integer.valueOf( column ) );
  }

}
