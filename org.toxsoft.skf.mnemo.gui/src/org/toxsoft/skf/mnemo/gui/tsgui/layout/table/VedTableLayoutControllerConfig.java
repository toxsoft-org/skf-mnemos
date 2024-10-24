package org.toxsoft.skf.mnemo.gui.tsgui.layout.table;

import java.io.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

/**
 * Конфигурация табличного контроллера размещения.
 *
 * @author vs
 */
public class VedTableLayoutControllerConfig {

  /**
   * ИД типа контроллера размещения
   */
  private final static String LAYOUT_TABLE = "layoutTable"; //$NON-NLS-1$

  private final static String PARAMID_MARGINS = "margins"; //$NON-NLS-1$

  private final static String PARAMID_H_GAP = "hGap"; //$NON-NLS-1$

  private final static String PARAMID_V_GAP = "vGap"; //$NON-NLS-1$

  private final static String PARAMID_ROW_DATAS = "rowDatas"; //$NON-NLS-1$

  private final static String PARAMID_COLUMN_DATAS = "columnDatas"; //$NON-NLS-1$

  private final static String PARAMID_CELL_DATAS = "cellDatas"; //$NON-NLS-1$

  private final static String KW_ROWS = "rows"; //$NON-NLS-1$

  private final static String KW_COLUMNS = "columns"; //$NON-NLS-1$

  private final static String KW_CELLS = "cells"; //$NON-NLS-1$

  IListEdit<TableRowLayoutData> rowDatas;

  IListEdit<TableColumnLayoutData> columnDatas;

  IListEdit<CellLayoutData> cellDatas;

  ID2Margins margins = new D2Margins();

  double hGap = 0;

  double vGap = 0;

  /**
   * Конструктор по умолчанию.
   */
  public VedTableLayoutControllerConfig() {
    columnDatas = new ElemArrayList<>();
    for( int i = 0; i < 2; i++ ) {
      columnDatas.add( new TableColumnLayoutData() );
    }
    rowDatas = new ElemArrayList<>();
    for( int i = 0; i < 2; i++ ) {
      rowDatas.add( new TableRowLayoutData() );
    }
    cellDatas = new ElemArrayList<>();
    for( int i = 0; i < 2 * 2; i++ ) {
      cellDatas.add( new CellLayoutData() );
    }
  }

  /**
   * Constructor со всеми инвариантами.<br>
   *
   * @param aSrc {@link VedTableLayoutControllerConfig} - конфигурация для копирования
   * @param aColDatas IList&lt;TableColumnLayoutData> - список информации о размещении колонок
   * @param aRowDatas IList&lt;TableRowLayoutData> - список информации о размещении строк
   * @param aCellDatas IList&lt;CellLayoutData> - список информации о размещении ячеек
   */
  public VedTableLayoutControllerConfig( VedTableLayoutControllerConfig aSrc, IList<TableColumnLayoutData> aColDatas,
      IList<TableRowLayoutData> aRowDatas, IList<CellLayoutData> aCellDatas ) {
    columnDatas = new ElemArrayList<>( aColDatas );
    rowDatas = new ElemArrayList<>( aRowDatas );
    cellDatas = new ElemArrayList<>( aCellDatas );
    ID2Margins m = aSrc.margins;
    margins = new D2Margins( m.left(), m.top(), m.right(), m.bottom() );
    vGap = aSrc.vGap;
    hGap = aSrc.hGap;
  }

  /**
   * Конструктор копирования.
   *
   * @param aSrc VedTableLayoutControllerConfig - копируемая конфигурация не <code>null</code>
   */
  public VedTableLayoutControllerConfig( VedTableLayoutControllerConfig aSrc ) {
    TsNullArgumentRtException.checkNull( aSrc );
    hGap = aSrc.hGap;
    vGap = aSrc.vGap;
    rowDatas = new ElemArrayList<>( aSrc.rowDatas() );
    columnDatas = new ElemArrayList<>( aSrc.columnDatas() );
    cellDatas = new ElemArrayList<>( aSrc.cellDatas() );
  }

  /**
   * Конструктор.
   *
   * @param aCfg IVedLayoutControllerConfig - конфигурация менеджера размещени не <code>null</code>
   */
  public VedTableLayoutControllerConfig( IVedLayoutControllerConfig aCfg ) {
    TsNullArgumentRtException.checkNull( aCfg );
    if( aCfg.propValues().hasKey( PARAMID_MARGINS ) ) {
      margins = aCfg.propValues().getValobj( PARAMID_MARGINS );
    }
    hGap = aCfg.propValues().getDouble( PARAMID_H_GAP );
    vGap = aCfg.propValues().getDouble( PARAMID_V_GAP );

    ICharInputStream is;
    IStrioReader sr;
    if( aCfg.propValues().hasKey( PARAMID_COLUMN_DATAS ) ) {
      is = new CharInputStreamString( aCfg.propValues().getStr( PARAMID_ROW_DATAS ) );
      sr = new StrioReader( is );
      rowDatas = StrioUtils.readCollection( sr, KW_ROWS, TableRowLayoutData.KEEPER );
    }
    else {
      rowDatas = new ElemArrayList<>();
      for( int i = 0; i < 2; i++ ) {
        rowDatas.add( new TableRowLayoutData() );
      }
    }

    if( aCfg.propValues().hasKey( PARAMID_ROW_DATAS ) ) {
      is = new CharInputStreamString( aCfg.propValues().getStr( PARAMID_COLUMN_DATAS ) );
      sr = new StrioReader( is );
      columnDatas = StrioUtils.readCollection( sr, KW_COLUMNS, TableColumnLayoutData.KEEPER );
    }
    else {
      columnDatas = new ElemArrayList<>();
      for( int i = 0; i < 2; i++ ) {
        columnDatas.add( new TableColumnLayoutData() );
      }
    }

    is = new CharInputStreamString( aCfg.propValues().getStr( PARAMID_CELL_DATAS ) );
    sr = new StrioReader( is );
    cellDatas = StrioUtils.readCollection( sr, KW_CELLS, CellLayoutData.KEEPER );

  }

  public void init( int aColCount, int aRowCount ) {
    columnDatas = new ElemArrayList<>();
    for( int i = 0; i < aColCount; i++ ) {
      columnDatas.add( new TableColumnLayoutData() );
    }
    rowDatas = new ElemArrayList<>();
    for( int i = 0; i < aRowCount; i++ ) {
      rowDatas.add( new TableRowLayoutData() );
    }
    cellDatas = new ElemArrayList<>();
    for( int i = 0; i < aColCount * aRowCount; i++ ) {
      cellDatas.add( new CellLayoutData() );
    }
  }

  /**
   * Возвращает конфигурацию контроллера.
   *
   * @return IVedLayoutControllerConfig - конфигурация контроллера
   */
  public IVedLayoutControllerConfig config() {
    IOptionSetEdit props = new OptionSet();
    props.setDouble( PARAMID_H_GAP, hGap );
    props.setDouble( PARAMID_V_GAP, vGap );
    props.setValobj( PARAMID_MARGINS, margins );

    CharArrayWriter writer = new CharArrayWriter();
    IStrioWriter sw = new StrioWriter( new CharOutputStreamWriter( writer ) );
    StrioUtils.writeCollection( sw, KW_ROWS, rowDatas, TableRowLayoutData.KEEPER );
    StringBuilder sb = new StringBuilder();
    sb.append( writer.toCharArray() );
    props.setStr( PARAMID_ROW_DATAS, sb.toString() );

    writer = new CharArrayWriter();
    sw = new StrioWriter( new CharOutputStreamWriter( writer ) );
    StrioUtils.writeCollection( sw, KW_COLUMNS, columnDatas, TableColumnLayoutData.KEEPER );
    sb = new StringBuilder();
    sb.append( writer.toCharArray() );
    props.setStr( PARAMID_COLUMN_DATAS, sb.toString() );

    writer = new CharArrayWriter();
    sw = new StrioWriter( new CharOutputStreamWriter( writer ) );
    StrioUtils.writeCollection( sw, KW_CELLS, cellDatas, CellLayoutData.KEEPER );
    sb = new StringBuilder();
    sb.append( writer.toCharArray() );
    props.setStr( PARAMID_CELL_DATAS, sb.toString() );

    VedLayoutControllerConfig cfg = new VedLayoutControllerConfig( LAYOUT_TABLE, props );
    return cfg;
  }

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

  /**
   * Возвращает размеры полей области для размещения контролей.
   *
   * @return {@link ID2Margins} - размеры полей области для размещения контролей
   */
  public ID2Margins margins() {
    return margins;
  }

  /**
   * Взвращает количество столбцов.
   *
   * @return int - количество столбцов
   */
  public int columnCount() {
    return columnDatas.size();
  }

  /**
   * Взвращает количество срок.
   *
   * @return int - количество строк
   */
  public int rowCount() {
    return rowDatas.size();
  }

  /**
   * Вовзращает конфигурации строк.
   *
   * @return IList&lt;TableRowLayoutData> - срисок конфигураций строк
   */
  public IListEdit<TableRowLayoutData> rowDatas() {
    return rowDatas;
  }

  /**
   * Вовзращает конфигурации столбцов.
   *
   * @return IList&lt;TableColumnLayoutData> - срисок конфигураций стролбцов
   */
  public IListEdit<TableColumnLayoutData> columnDatas() {
    return columnDatas;
  }

  /**
   * Вовзращает конфигурации ячеек.
   *
   * @return IListEdit&lt;CellLayoutData> - срисок конфигураций ячеек
   */
  public IListEdit<CellLayoutData> cellDatas() {
    return cellDatas;
  }

  /**
   * Добавляет строки.<br>
   * Каждая добавляемая строка имеет конфигурацию по умолчанию.
   *
   * @param aCount int - кол-во добавляемых строк
   */
  public void addRows( int aCount ) {
    init( columnCount(), rowCount() + aCount );
    // for( int i = 0; i < aCount; i++ ) {
    // rowDatas.add( new TableRowLayoutData() );
    // }
  }

  /**
   * Удаляет строки.<br>
   *
   * @param aCount int - кол-во удаляемых строк
   */
  public void removeRows( int aCount ) {
    int count = rowCount() - aCount;
    if( count <= 0 ) {
      count = 1;
    }
    init( columnCount(), count );
    // for( int i = 0; i < aCount; i++ ) {
    // rowDatas.removeByIndex( rowDatas.size() - 1 );
    // }
  }

  /**
   * Добавляет колонки.<br>
   * Каждая добавляемая колонка имеет конфигурацию по умолчанию.
   *
   * @param aCount int - кол-во добавляемых колонок
   */
  public void addColumns( int aCount ) {
    init( columnCount() + aCount, rowCount() );
  }

  /**
   * Удаляет колонки.<br>
   *
   * @param aCount int - кол-во удаляемых колонок
   */
  public void removeColumns( int aCount ) {
    int count = columnCount() - aCount;
    if( count <= 0 ) {
      count = 1;
    }
    init( count, rowCount() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void setMargins( ID2Margins aMargins ) {
    margins = aMargins;
  }

  public void setHGap( int aHGap ) {
    hGap = aHGap;
  }

  public void setVGap( int aVGap ) {
    vGap = aVGap;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // Pair<Integer, Integer> cellIndex2RowColumn( int aCellIndex ) {
  //
  // int row = aCellIndex / rowDatas.size();
  // int column = aCellIndex - row * rowDatas.size();
  // return new Pair<>( Integer.valueOf( row ), Integer.valueOf( column ) );
  // }

}
