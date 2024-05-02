package org.toxsoft.skf.mnemo.gui.tsgui.layout.column;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

/**
 * Контроллер размещения, который располагает подконтрольные элементы в один столбец.
 * <p>
 *
 * @author vs
 */
public class VedColumnLayoutController
    implements IVedViselsLayoutController {

  // /**
  // * ИД типа контроллера размещения
  // */
  // public static final String LAYOUT_KIND = "ved.layout.column"; //$NON-NLS-1$

  private final IVedScreen vedScreen;

  D2GridMargins margins = new D2GridMargins( 4 );

  int cellsQtty = 1;

  private final VedColumnLayoutControllerConfig config;

  /**
   * Конструктор.
   *
   * @param aCfg {@link IVedLayoutControllerConfig} - конфигурация контроллера
   * @param aVedScreen {@link IVedScreen} - экран редактора
   */
  public VedColumnLayoutController( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    if( aCfg != null ) {
      TsIllegalArgumentRtException.checkFalse( aCfg.kindId().equals( kindId() ) );
    }
    vedScreen = aVedScreen;
    config = new VedColumnLayoutControllerConfig( aCfg );
  }

  // ------------------------------------------------------------------------------------
  // IVedViselsLayoutController
  //

  @Override
  public String kindId() {
    return VedColumnLayoutControllerConfig.LAYOUT_KIND;
  }

  @Override
  public void doLayout( String aMasterId, IStringList aSlaveIds ) {
    IVedVisel masterVisel = VedScreenUtils.findVisel( aMasterId, vedScreen );
    ID2Rectangle r = masterVisel.bounds();
    // IStridablesListEdit<IVedVisel> visels = new StridablesList<>();
    // for( String id : aSlaveIds ) {
    // visels.add( VedScreenUtils.findVisel( id, vedScreen ) );
    // }
    IStridablesList<IVedVisel> visels = VedScreenUtils.listVisels( aSlaveIds, vedScreen );

    // подсчитаем ширины колонок
    IListEdit<Double> widthList = new ElemArrayList<>();
    double fullWidth = 0;
    for( int i = 0; i < config.columnCount(); i++ ) {
      IStridablesList<IVedVisel> columnVisels = listColumnVisels( i, visels );
      double w = calcSingleColumnWidth( i, columnVisels );
      fullWidth += w;
      widthList.add( Double.valueOf( w ) );
    }

    // подсчитаем свободную ширину
    ID2Margins d2m = config.margins();
    double freeWidth = r.width() - fullWidth - config.hGap * (config.columnCount() - 1) - d2m.left() - d2m.right();
    if( freeWidth > 0 ) { // есть свободная ширина
      // подсчитаем размер добавляемой ширины в каждую колонку
      int expCount = calcExpandedColumnsCount();
      double additionalWidth = freeWidth / expCount;
      // добавим ширину к соотвествующим колонкам
      for( int i = 0; i < config.columnCount(); i++ ) {
        if( config.columnDatas.get( i ).grabExcessSpace() ) {
          Double colWidth = widthList.get( i );
          widthList.removeByIndex( i );
          widthList.insert( i, Double.valueOf( colWidth.doubleValue() + additionalWidth ) );
        }
      }
    }

    IListEdit<Double> xList = new ElemArrayList<>();
    double x = config.margins().left();
    for( int i = 0; i < config.columnCount(); i++ ) {
      xList.add( Double.valueOf( x ) );
      x += widthList.get( i ).doubleValue() + config.horizontalGap();
    }

    double rowHeight = calcRowHeight( aSlaveIds );

    // разместим визуальные элементы
    int idx = 0;
    for( IVedVisel visel : visels ) {
      int colIdx = idx % config.columnCount();
      int rowIdx = idx / config.columnCount();
      // visel.setLocation( r.x1() + xList.get( colIdx ).doubleValue(), d2m.top() + r.y1() + rowIdx * rowHeight );
      double cellX = r.x1() + xList.get( colIdx ).doubleValue();
      double cellY = d2m.top() + r.y1() + rowIdx * (rowHeight + config.verticalGap());
      ID2Rectangle cellRect = new D2Rectangle( cellX, cellY, widthList.get( colIdx ).doubleValue(), rowHeight );
      layoutCellContent( cellRect, config.columnDatas.get( colIdx ).cellData(), visel );
      idx++;
    }

  }

  @Override
  public IVedLayoutControllerConfig getConfiguration() {
    return config.config();
  }

  @Override
  public IList<ID2Rectangle> calcCellRects( String aMasterId, IStringList aSlaveIds ) {
    IVedVisel masterVisel = VedScreenUtils.findVisel( aMasterId, vedScreen );
    ID2Rectangle r = masterVisel.bounds();
    IStridablesList<IVedVisel> visels = VedScreenUtils.listVisels( aSlaveIds, vedScreen );

    // подсчитаем ширины колонок
    IListEdit<Double> widthList = new ElemArrayList<>();
    double fullWidth = 0;
    for( int i = 0; i < config.columnCount(); i++ ) {
      IStridablesList<IVedVisel> columnVisels = listColumnVisels( i, visels );
      double w = calcSingleColumnWidth( i, columnVisels );
      fullWidth += w;
      widthList.add( Double.valueOf( w ) );
    }

    // подсчитаем свободную ширину
    ID2Margins d2m = config.margins();
    double freeWidth = r.width() - fullWidth - config.hGap * (config.columnCount() - 1) - d2m.left() - d2m.right();
    if( freeWidth > 0 ) { // есть свободная ширина
      // подсчитаем размер добавляемой ширины в каждую колонку
      int expCount = calcExpandedColumnsCount();
      double additionalWidth = freeWidth / expCount;
      // добавим ширину к соотвествующим колонкам
      for( int i = 0; i < config.columnCount(); i++ ) {
        if( config.columnDatas.get( i ).grabExcessSpace() ) {
          Double colWidth = widthList.get( i );
          widthList.removeByIndex( i );
          widthList.insert( i, Double.valueOf( colWidth.doubleValue() + additionalWidth ) );
        }
      }
    }

    IListEdit<Double> xList = new ElemArrayList<>();
    double x = config.margins().left();
    for( int i = 0; i < config.columnCount(); i++ ) {
      xList.add( Double.valueOf( x ) );
      x += widthList.get( i ).doubleValue() + config.horizontalGap();
    }

    double rowHeight = calcRowHeight( aSlaveIds );

    IListEdit<ID2Rectangle> cellRects = new ElemArrayList<>();
    for( int i = 0; i < visels.size(); i++ ) {
      int colIdx = i % config.columnCount();
      int rowIdx = i / config.columnCount();
      double cellX = r.x1() + xList.get( colIdx ).doubleValue();
      double cellY = d2m.top() + r.y1() + rowIdx * (rowHeight + config.verticalGap());
      ID2Rectangle cellRect = new D2Rectangle( cellX, cellY, widthList.get( colIdx ).doubleValue(), rowHeight );
      cellRects.add( cellRect );
    }
    return cellRects;
  }

  // ------------------------------------------------------------------------------------
  // Implementaton
  //

  IStridablesList<IVedVisel> listColumnVisels( int aColumnIdx, IStridablesList<IVedVisel> aVisels ) {
    IStridablesListEdit<IVedVisel> result = new StridablesList<>();
    int idx = 0;
    for( IVedVisel visel : aVisels ) {
      if( idx == aColumnIdx ) {
        result.add( visel );
      }
      idx = (idx + 1) % config.columnCount();
    }
    return result;
  }

  double calcSingleColumnWidth( int aColumnIdx, IStridablesList<IVedVisel> aVisels ) {
    TableColumnLayoutData cld = config.columnDatas().get( aColumnIdx );
    double minWidth = cld.widthRange().left().doubleValue();
    double maxWidth = cld.widthRange().right().doubleValue();
    if( (minWidth >= 0) && (maxWidth >= 0) && (Double.compare( minWidth, maxWidth ) == 0) ) {
      return minWidth;
    }

    double columnWidth = 0.;
    for( IVedVisel visel : aVisels ) {
      double width = calcCellWidth( visel, cld.cellData() );
      if( width > columnWidth ) {
        columnWidth = width;
      }
    }

    if( minWidth >= 0 && columnWidth < minWidth ) {
      return minWidth;
    }

    if( maxWidth >= 0 && columnWidth > maxWidth ) {
      return maxWidth;
    }

    return columnWidth;
  }

  double calcCellWidth( IVedVisel aVisel, CellLayoutData aCellData ) {
    ID2Rectangle r = aVisel.bounds();
    ID2Margins d2m = aCellData.margins();
    return r.width() + d2m.left() + d2m.right();
  }

  double calcRowHeight( IStringList aSlaveIds ) {
    IStridablesList<IVedVisel> visels = VedScreenUtils.listVisels( aSlaveIds, vedScreen );
    return calcSingleRowHeight( visels );
  }

  double calcSingleRowHeight( IStridablesList<IVedVisel> aVisels ) {
    double rowHeight = 0.;
    int i = 0;
    for( IVedVisel visel : aVisels ) {
      if( i >= config.columnCount() ) {
        break;
      }
      CellLayoutData cellData = config.columnDatas().get( i ).cellData();
      double height = calcCellHeight( visel, cellData );
      if( height > rowHeight ) {
        rowHeight = height;
      }
      i++;
    }
    return rowHeight;
  }

  double calcCellHeight( IVedVisel aVisel, CellLayoutData aCellData ) {
    ID2Rectangle r = aVisel.bounds();
    return r.height() + aCellData.margins().top() + aCellData.margins().bottom();
  }

  int calcExpandedColumnsCount() {
    int count = 0;
    for( int i = 0; i < config.columnCount(); i++ ) {
      if( config.columnDatas.get( i ).grabExcessSpace() ) {
        count++;
      }
    }
    return count;
  }

  void layoutCellContent( ID2Rectangle aCellBounds, CellLayoutData aLayoutData, IVedVisel aVisel ) {
    ID2Rectangle clientRect = calcClientRect( aCellBounds, aLayoutData.margins() );
    if( aLayoutData.fillCellWidth() ) {

    }
    if( aLayoutData.fillCellHeight() ) {

    }

    double viselX;
    double viselY;

    viselX = switch( aLayoutData.cellAlignment().horAlignment() ) {
      case CENTER -> clientRect.x1() + (clientRect.width() - aVisel.bounds().width()) / 2.;
      case LEFT -> clientRect.x1();
      case RIGHT -> clientRect.x1() + clientRect.width() - aVisel.bounds().width();
      case FILL -> throw new IllegalArgumentException(
          "Unexpected value: " + aLayoutData.cellAlignment().horAlignment() );
    };

    viselY = switch( aLayoutData.cellAlignment().verAlignment() ) {
      case CENTER -> clientRect.y1() + (clientRect.height() - aVisel.bounds().height()) / 2.;
      case TOP -> clientRect.y1();
      case BOTTOM -> clientRect.y1() + clientRect.height() - aVisel.bounds().height();
      case FILL -> throw new IllegalArgumentException(
          "Unexpected value: " + aLayoutData.cellAlignment().horAlignment() );
    };
    aVisel.setLocation( viselX, viselY );
  }

  ID2Rectangle calcClientRect( ID2Rectangle aBounds, ID2Margins aMargins ) {
    double x = aBounds.x1() + aMargins.left();
    double y = aBounds.y1() + aMargins.top();
    double width = aBounds.width() - aMargins.left() - aMargins.right();
    if( width < 1 ) {
      width = 1;
    }
    double height = aBounds.height() - aMargins.top() - aMargins.bottom();
    if( height < 1 ) {
      height = 1;
    }
    return new D2Rectangle( x, y, width, height );
  }

}
