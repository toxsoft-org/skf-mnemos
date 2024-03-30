package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tsgui.graphics.*;

/**
 * Параметры размещениия одной ячейки и её содержимого.
 *
 * @author vs
 */
public class CellLayoutData {

  private final D2Margins margins;

  private final EHorAlignment horAlignment;

  private final EVerAlignment verAlignment;

  private final boolean fillCellWidth;

  private final boolean fillCellHeight;

  /**
   * Конструктор.
   */
  public CellLayoutData() {
    margins = new D2Margins( 0 );
    horAlignment = EHorAlignment.CENTER;
    verAlignment = EVerAlignment.CENTER;
    fillCellWidth = false;
    fillCellHeight = false;
  }

  /**
   * Конструктор с указанием полей и выравниванием.
   *
   * @param aMargins {@link D2Margins} - поля
   * @param aHorAlign {@link EHorAlignment} - выравнивание по горизонтли
   * @param aVerAlign {@link EVerAlignment} - выравнивание по вертикали
   */
  public CellLayoutData( D2Margins aMargins, EHorAlignment aHorAlign, EVerAlignment aVerAlign ) {
    margins = aMargins;
    horAlignment = aHorAlign;
    verAlignment = aVerAlign;
    fillCellWidth = false;
    fillCellHeight = false;
  }

  /**
   * Конструктор со всеми вариантами.
   *
   * @param aMargins {@link D2Margins} - поля
   * @param aHorAlign {@link EHorAlignment} - выравнивание по горизонтли
   * @param aVerAlign {@link EVerAlignment} - выравнивание по вертикали
   * @param aFillWidth boolean - признак того, что нужно по возможности заполнить всю ширину ячейки
   * @param aFillHeight boolean - признак того, что нужно по возможности заполнить всю высоту ячейки
   */
  public CellLayoutData( D2Margins aMargins, EHorAlignment aHorAlign, EVerAlignment aVerAlign, boolean aFillWidth,
      boolean aFillHeight ) {
    margins = aMargins;
    horAlignment = aHorAlign;
    verAlignment = aVerAlign;
    fillCellWidth = aFillWidth;
    fillCellHeight = aFillHeight;
  }

  /**
   * Возвращает выравнивание содержимого каждой ячейки колонки по горизонтали.
   *
   * @return EHorAlignment - выравнивание содержимого ячейки колонки по горизонтали
   */
  public EHorAlignment horAlignment() {
    return horAlignment;
  }

  /**
   * Возвращает выравнивание содержимого каждой ячейки колонки по вертикали.
   *
   * @return EVerAlignment - выравнивание содержимого ячейки колонки по вертикали
   */
  public EVerAlignment verAlignment() {
    return verAlignment;
  }

  /**
   * Возвращает признак того, нужно ли заполнить по ширине всю ячейку.
   *
   * @return <b>true</b> - нужно заполнить всю ширину<br>
   *         <b>false</b> - не нужно
   */
  public boolean fillCellWidth() {
    return fillCellWidth;
  }

  /**
   * Возвращает признак того, нужно ли заполнить по высоте всю ячейку.
   *
   * @return <b>true</b> - нужно заполнить всю высоту<br>
   *         <b>false</b> - не нужно
   */
  public boolean fillCellHeight() {
    return fillCellHeight;
  }

  /**
   * Возвращает значения полей внутри ячейки.
   *
   * @return {@link D2Margins} - значения полей внутри ячейки
   */
  public D2Margins margins() {
    return margins;
  }

}
