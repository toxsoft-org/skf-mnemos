package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Параметры выравнивания содержимого ячейки для контроллеров размещения.
 *
 * @author vs
 */
public class CellAlignment {

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "CellAlignment"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<CellAlignment> KEEPER =
      new AbstractEntityKeeper<>( CellAlignment.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, CellAlignment aEntity ) {
          EHorAlignment.KEEPER.write( aSw, aEntity.horAlignment );
          aSw.writeSeparatorChar();
          EVerAlignment.KEEPER.write( aSw, aEntity.verAlignment );
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aEntity.fillWidth );
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aEntity.fillHeight );
        }

        @Override
        protected CellAlignment doRead( IStrioReader aSr ) {
          EHorAlignment hAl = EHorAlignment.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          EVerAlignment vAl = EVerAlignment.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          boolean fillW = aSr.readBoolean();
          aSr.ensureSeparatorChar();
          boolean fillH = aSr.readBoolean();

          return new CellAlignment( hAl, fillW, vAl, fillH );
        }

      };

  private final EHorAlignment horAlignment;

  private final EVerAlignment verAlignment;

  private final boolean fillWidth;

  private final boolean fillHeight;

  /**
   * Конструктор.
   */
  public CellAlignment() {
    horAlignment = EHorAlignment.LEFT;
    fillWidth = false;
    verAlignment = EVerAlignment.TOP;
    fillHeight = false;
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aHorAlignment EHorAlignment - выравнивание по горизонтали
   * @param aFillWidth boolean - признак того, что нужно заполнить всю ячейку по ширине
   * @param aVerAlignment EVerAlignment - выравнивание по вертикали
   * @param aFillHeight boolean - признак того, что нужно заполнить всю ячейку по высоте
   */
  public CellAlignment( EHorAlignment aHorAlignment, boolean aFillWidth, EVerAlignment aVerAlignment,
      boolean aFillHeight ) {
    horAlignment = aHorAlignment;
    fillWidth = aFillWidth;
    verAlignment = aVerAlignment;
    fillHeight = aFillHeight;
  }

  /**
   * Возвращает выравнивание по горизонтали.
   *
   * @return EHorAlignment - выравнивание по горизонтали
   */
  public EHorAlignment horAlignment() {
    return horAlignment;
  }

  /**
   * Возвращает выравнивание по вертикали.
   *
   * @return EHorAlignment - выравнивание по вертикали
   */
  public EVerAlignment verAlignment() {
    return verAlignment;
  }

  /**
   * Возвращает признак того, нужно ли (по возможности) заполнить всю ширину ячейки.
   *
   * @return <b>true</b> - нужно (по возможности) заполнить всю ширину ячейки<br>
   *         <b>false</b> - заполнять не нужно
   */
  public boolean shouldFillWidth() {
    return fillWidth;
  }

  /**
   * Возвращает признак того, нужно ли (по возможности) заполнить всю ширину ячейки.
   *
   * @return <b>true</b> - нужно (по возможности) заполнить всю ширину ячейки<br>
   *         <b>false</b> - заполнять не нужно
   */
  public boolean shouldFillHeight() {
    return fillHeight;
  }
}
