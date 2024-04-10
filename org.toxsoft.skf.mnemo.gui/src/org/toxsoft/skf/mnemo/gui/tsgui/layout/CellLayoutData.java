package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Параметры размещениия одной ячейки и её содержимого.
 *
 * @author vs
 */
public class CellLayoutData {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "CellLayoutData"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<CellLayoutData> KEEPER =
      new AbstractEntityKeeper<>( CellLayoutData.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, CellLayoutData aEntity ) {
          D2Margins.KEEPER.write( aSw, aEntity.margins );
          aSw.writeSeparatorChar();
          CellAlignment.KEEPER.write( aSw, aEntity.cellAlignment() );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.horSpan );
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.verSpan );
        }

        @Override
        protected CellLayoutData doRead( IStrioReader aSr ) {
          ID2Margins d2m = D2Margins.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          CellAlignment cellAl = CellAlignment.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          int hSpan = aSr.readInt();
          aSr.ensureSeparatorChar();
          int vSpan = aSr.readInt();

          return new CellLayoutData( d2m, cellAl, hSpan, vSpan );
        }

      };

  private final ID2Margins margins;

  private final CellAlignment cellAlignment;

  private final int horSpan;

  private final int verSpan;

  ID2Point widthRange = new D2Point( -1., -1. );

  ID2Point heightRange = new D2Point( -1., -1. );

  /**
   * Конструктор.
   */
  public CellLayoutData() {
    margins = new D2Margins( 0 );
    cellAlignment = new CellAlignment();
    horSpan = 1;
    verSpan = 1;
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aMargins {@link ID2Margins} - поля
   * @param aCellAlign {@link CellAlignment} - параметры выравнивания содержимого ячейки
   * @param aHorSpan int - кол-во ячеек объединенных по горизонтали
   * @param aVerSpan int - кол-во ячеек объединенных по вертикали
   */
  public CellLayoutData( ID2Margins aMargins, CellAlignment aCellAlign, int aHorSpan, int aVerSpan ) {
    margins = aMargins;
    cellAlignment = aCellAlign;
    horSpan = aHorSpan;
    verSpan = aVerSpan;
  }

  /**
   * Возвращает выравнивание содержимого каждой ячейки колонки по горизонтали.
   *
   * @return EHorAlignment - выравнивание содержимого ячейки колонки по горизонтали
   */
  public EHorAlignment horAlignment() {
    return cellAlignment.horAlignment();
  }

  /**
   * Возвращает выравнивание содержимого каждой ячейки колонки по вертикали.
   *
   * @return EVerAlignment - выравнивание содержимого ячейки колонки по вертикали
   */
  public EVerAlignment verAlignment() {
    return cellAlignment.verAlignment();
  }

  /**
   * Возвращает признак того, нужно ли заполнить по ширине всю ячейку.
   *
   * @return <b>true</b> - нужно заполнить всю ширину<br>
   *         <b>false</b> - не нужно
   */
  public boolean fillCellWidth() {
    return cellAlignment.shouldFillWidth();
  }

  /**
   * Возвращает признак того, нужно ли заполнить по высоте всю ячейку.
   *
   * @return <b>true</b> - нужно заполнить всю высоту<br>
   *         <b>false</b> - не нужно
   */
  public boolean fillCellHeight() {
    return cellAlignment.shouldFillHeight();
  }

  /**
   * Возвращает значения полей внутри ячейки.
   *
   * @return {@link ID2Margins} - значения полей внутри ячейки
   */
  public ID2Margins margins() {
    return margins;
  }

  /**
   * Возвращает параметры выравнивания содержимого ячейки.
   *
   * @return {@link CellAlignment} - параметры выравнивания содержимого ячейки
   */
  public CellAlignment cellAlignment() {
    return cellAlignment;
  }

  /**
   * Возвращает кол-во объединенных ячеек по горизонтали.
   *
   * @return int - кол-во объединенных ячеек по горизонтали
   */
  public int horSpan() {
    return horSpan;
  }

  /**
   * Возвращает кол-во объединенных ячеек по вертикали.
   *
   * @return int - кол-во объединенных ячеек по вертикали
   */
  public int verSpan() {
    return verSpan;
  }

}
