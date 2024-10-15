package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

class TableLayoutPreviewPanel {

  private static final String DID_CELL_LAYOUT = "cellLayout"; //$NON-NLS-1$

  private final Composite bkPanel;

  private final Color colorWhite = new Color( 255, 255, 255 );

  VedTableLayoutControllerConfig layoutCfg;

  private final ITsGuiContext tsContext;

  int cellCount = 0;

  final IListEdit<Button> buttons = new ElemArrayList<>();

  TableLayoutPreviewPanel( Composite aBkPanel, ITsGuiContext aTsContext ) {
    bkPanel = aBkPanel;
    bkPanel.setLayout( new org.toxsoft.core.tsgui.utils.layout.BorderLayout() );
    tsContext = aTsContext;
  }

  void setLayoutConfig( VedTableLayoutControllerConfig aLayoutCfg ) {
    layoutCfg = aLayoutCfg;
    update();
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Возвращает новую конфигурацию табличного менеджера размещений.
   *
   * @return {@link VedTableLayoutControllerConfig} - новая конфигурация табличного менеджера размещений
   */
  VedTableLayoutControllerConfig layoutConfig() {
    IListEdit<CellLayoutData> cellCfgs = new ElemArrayList<>();
    for( Button btn : buttons ) {
      CellLayoutData cld = (CellLayoutData)btn.getData( DID_CELL_LAYOUT );
      cellCfgs.add( cld );
    }

    double hGap = layoutCfg.horizontalGap();
    double vGap = layoutCfg.verticalGap();
    return new VedTableLayoutControllerConfig( cellCfgs, layoutCfg.rowDatas(), layoutCfg.columnDatas(), vGap, hGap );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void update() {
    clear();
    int colCount = layoutCfg.columnCount();
    int rowCount = layoutCfg.rowCount();
    Composite cellsPanel = new Composite( bkPanel, SWT.NONE );
    cellsPanel.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.CENTER );
    cellsPanel.setLayout( new GridLayout( colCount, false ) );

    Composite columnPanel = new Composite( bkPanel, SWT.BORDER );
    columnPanel.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.NORTH );
    Composite rowPanel = new Composite( bkPanel, SWT.BORDER );
    rowPanel.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.WEST );

    for( int i = 0; i < layoutCfg.cellDatas().size(); i++ ) {
      Button btn = new Button( cellsPanel, SWT.PUSH );
      buttons.add( btn );
      CellLayoutData cld = layoutCfg.cellDatas().get( i );
      btn.setData( DID_CELL_LAYOUT, cld );
      btn.setText( "Cell" + (i % colCount) + "" + (i / colCount) );
      btn.setBackground( colorWhite );
      btn.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, cld.horSpan(), cld.verSpan() ) );
      btn.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          CellLayoutData oldCld = (CellLayoutData)btn.getData( DID_CELL_LAYOUT );
          CellLayoutData cld = CellLayoutDataPanel.edit( (CellLayoutData)btn.getData( DID_CELL_LAYOUT ), tsContext );
          if( cld != null ) {
            int oldOcupiedCells = oldCld.horSpan() * oldCld.verSpan();
            int newOcupiedCells = cld.horSpan() * cld.verSpan();
            int delta = newOcupiedCells - oldOcupiedCells;
            if( delta > 0 ) {
              for( int ii = 0; ii < delta; ii++ ) {
                Button b = buttons.last();
                b.dispose();
                buttons.removeByIndex( buttons.indexOf( b ) );
              }
            }
            btn.setData( DID_CELL_LAYOUT, cld );
            btn.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, cld.horSpan(), cld.verSpan() ) );
            bkPanel.layout();
          }
        }
      } );
    }
    bkPanel.layout();
  }

  // void update() {
  // clear();
  // int colCount = layoutCfg.columnCount();
  // int rowCount = layoutCfg.rowCount();
  // bkPanel.setLayout( new GridLayout( colCount + 1, false ) );
  // CLabel l = new CLabel( bkPanel, SWT.NONE );
  // GridData gd = new GridData();
  // gd.minimumWidth = 50;
  // gd.widthHint = 50;
  // l.setLayoutData( gd );
  //
  // for( int i = 0; i < colCount; i++ ) {
  // Button btn = new Button( bkPanel, SWT.PUSH );
  // btn.setText( "Col" + (i + 1) );
  // btn.addSelectionListener( new SelectionAdapter() {
  //
  // @Override
  // public void widgetSelected( SelectionEvent aE ) {
  //
  // }
  //
  // } );
  // }
  //
  // for( int i = 0; i < rowCount; i++ ) {
  // Button btn = new Button( bkPanel, SWT.PUSH );
  // btn.setText( "Row" + (i + 1) );
  // for( int j = 0; j < colCount; j++ ) {
  // Button btn1 = new Button( bkPanel, SWT.PUSH );
  // btn1.setText( "Cell" + (i + 1) + "" + (j + 1) );
  // btn1.setBackground( colorWhite );
  // btn1.addSelectionListener( new SelectionAdapter() {
  //
  // @Override
  // public void widgetSelected( SelectionEvent aEvent ) {
  // CellLayoutData cld = CellLayoutDataPanel.edit( (CellLayoutData)btn1.getData( "layoutData" ), tsContext );
  // if( cld != null ) {
  // btn1.setData( "layoutData", cld );
  // }
  // }
  // } );
  // }
  // }
  // bkPanel.layout();
  // }

  void clear() {
    buttons.clear();
    for( Control ctrl : bkPanel.getChildren() ) {
      ctrl.dispose();
    }
  }

  int calcCellsCount( IList<GridData> aDatas ) {
    int count = aDatas.size();
    for( GridData gd : aDatas ) {
      int delta = (gd.horizontalSpan * gd.verticalSpan);
      count = count - (delta - 1);
    }
    return count;
  }

}
