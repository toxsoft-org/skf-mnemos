package org.toxsoft.skf.mnemo.gui.tsgui.layout.column;

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
 * Конфигурация контроллера размещения "по столбцам".
 *
 * @author vs
 */
public class VedColumnLayoutControllerConfig {

  /**
   * ИД типа контроллера размещения
   */
  public final static String LAYOUT_KIND = "ved.layout.column"; //$NON-NLS-1$

  private final static String PARAMID_MARGINS = "margins"; //$NON-NLS-1$

  private final static String PARAMID_H_GAP = "hGap"; //$NON-NLS-1$

  private final static String PARAMID_V_GAP = "vGap"; //$NON-NLS-1$

  private final static String PARAMID_COLUMN_DATAS = "columnDatas"; //$NON-NLS-1$

  private final static String KW_COLUMNS = "columns"; //$NON-NLS-1$

  ID2Margins margins = new D2Margins();

  double hGap = 0;

  double vGap = 0;

  IListEdit<TableColumnLayoutData> columnDatas;

  /**
   * Конструктор.
   */
  public VedColumnLayoutControllerConfig() {
    columnDatas = new ElemArrayList<>();
    columnDatas.add( new TableColumnLayoutData() );
  }

  /**
   * Конструктор.
   *
   * @param aCfg IVedLayoutControllerConfig - конфигурация менеджера размещени не <code>null</code>
   */
  public VedColumnLayoutControllerConfig( IVedLayoutControllerConfig aCfg ) {
    TsNullArgumentRtException.checkNull( aCfg );
    margins = aCfg.propValues().getValobj( PARAMID_MARGINS );
    hGap = aCfg.propValues().getDouble( PARAMID_H_GAP );
    vGap = aCfg.propValues().getDouble( PARAMID_V_GAP );

    ICharInputStream is = new CharInputStreamString( aCfg.propValues().getStr( PARAMID_COLUMN_DATAS ) );
    IStrioReader sr = new StrioReader( is );
    columnDatas = StrioUtils.readCollection( sr, KW_COLUMNS, TableColumnLayoutData.KEEPER );
  }

  /**
   * Возвращает конфигурацию контроллера.
   *
   * @return IVedLayoutControllerConfig - конфигурация контроллера
   */
  public IVedLayoutControllerConfig config() {
    IOptionSetEdit props = new OptionSet();
    props.setValobj( PARAMID_MARGINS, margins );
    props.setDouble( PARAMID_H_GAP, hGap );
    props.setDouble( PARAMID_V_GAP, vGap );

    CharArrayWriter writer = new CharArrayWriter();
    IStrioWriter sw = new StrioWriter( new CharOutputStreamWriter( writer ) );
    StrioUtils.writeCollection( sw, KW_COLUMNS, columnDatas, TableColumnLayoutData.KEEPER );
    StringBuilder sb = new StringBuilder();
    sb.append( writer.toCharArray() );
    props.setStr( PARAMID_COLUMN_DATAS, sb.toString() );

    VedLayoutControllerConfig cfg = new VedLayoutControllerConfig( LAYOUT_KIND, props );
    return cfg;
  }

  /**
   * Взвращает поля области размещения.
   *
   * @return {@link ID2Margins} - поля области размещения
   */
  public ID2Margins margins() {
    return margins;
  }

  public void setMargins( ID2Margins aMargins ) {
    margins = aMargins;
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

  public void setVerticalGap( double aVerGap ) {
    vGap = aVerGap;
  }

  public void setHorizontalGap( double aHorGap ) {
    hGap = aHorGap;
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
   * Вовзращает конфигурации столбцов.
   *
   * @return IList&lt;TableColumnLayoutData> - срисок конфигураций стролбцов
   */
  public IList<TableColumnLayoutData> columnDatas() {
    return columnDatas;
  }

  /**
   * Добавляет колонки.<br>
   * Каждая добавляемая колонка имеет конфигурацию по умолчанию.
   *
   * @param aCount int - кол-во добавляемых колонок
   */
  public void addColumns( int aCount ) {
    for( int i = 0; i < aCount; i++ ) {
      columnDatas.add( new TableColumnLayoutData() );
    }
  }

  /**
   * Удаляет колонки.<br>
   *
   * @param aCount int - кол-во удаляемых колонок
   */
  public void removeColumns( int aCount ) {
    for( int i = 0; i < aCount; i++ ) {
      columnDatas.removeByIndex( columnDatas.size() - 1 );
    }
  }

  /**
   * Меняет конфигурацию столбца.
   *
   * @param aOldData TableColumnLayoutData - заменяемая конфигурацию
   * @param aNewData TableColumnLayoutData - новая конфигурация
   */
  public void replaceColumnData( TableColumnLayoutData aOldData, TableColumnLayoutData aNewData ) {
    int idx = columnDatas.indexOf( aOldData );
    columnDatas.removeByIndex( idx );
    columnDatas.insert( idx, aNewData );
  }

}
