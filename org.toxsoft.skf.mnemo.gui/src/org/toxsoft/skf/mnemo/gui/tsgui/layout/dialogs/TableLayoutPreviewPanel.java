package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

class TableLayoutPreviewPanel {

  private static final String DID_CELL_LAYOUT   = "cellLayout";   //$NON-NLS-1$
  private static final String DID_COLUMN_LAYOUT = "columnLayout"; //$NON-NLS-1$
  private static final String DID_ROW_LAYOUT    = "rowLayout";    //$NON-NLS-1$

  private final Composite bkPanel;

  private final Color colorWhite = new Color( 255, 255, 255 );

  VedTableLayoutControllerConfig layoutCfg;

  private final ITsGuiContext tsContext;

  int cellCount = 0;

  final IListEdit<Button> rowButtons = new ElemArrayList<>();
  final IListEdit<Button> colButtons = new ElemArrayList<>();
  final IListEdit<Button> buttons    = new ElemArrayList<>();

  TableLayoutPreviewPanel( Composite aBkPanel, ITsGuiContext aTsContext ) {
    bkPanel = aBkPanel;
    bkPanel.setLayout( new org.toxsoft.core.tsgui.utils.layout.BorderLayout() );
    tsContext = aTsContext;
  }

  void setLayoutConfig( VedTableLayoutControllerConfig aLayoutCfg ) {
    layoutCfg = aLayoutCfg;
    init();
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
    IListEdit<TableColumnLayoutData> colCfgs = new ElemArrayList<>();
    for( Button btn : colButtons ) {
      TableColumnLayoutData cld = (TableColumnLayoutData)btn.getData( DID_COLUMN_LAYOUT );
      colCfgs.add( cld );
    }
    IListEdit<TableRowLayoutData> rowCfgs = new ElemArrayList<>();
    for( Button btn : rowButtons ) {
      TableRowLayoutData cld = (TableRowLayoutData)btn.getData( DID_ROW_LAYOUT );
      rowCfgs.add( cld );
    }
    return new VedTableLayoutControllerConfig( layoutCfg, colCfgs, rowCfgs, cellCfgs );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void init() {
    clear();
    int colCount = layoutCfg.columnCount();
    int rowCount = layoutCfg.rowCount();
    Composite cellsPanel = new Composite( bkPanel, SWT.NONE );
    cellsPanel.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.CENTER );
    cellsPanel.setLayout( new GridLayout( colCount, false ) );

    Composite columnPanel = new Composite( bkPanel, SWT.BORDER );
    columnPanel.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.NORTH );
    columnPanel.setLayout( new BorderLayout() );
    Composite leftComp = new Composite( columnPanel, SWT.NONE );
    leftComp.setLayoutData( BorderLayout.WEST );
    Composite columnComp = new Composite( columnPanel, SWT.NONE );
    columnComp.setLayoutData( BorderLayout.CENTER );
    columnComp.setLayout( new GridLayout( colCount, true ) );
    for( int i = 0; i < colCount; i++ ) {
      Button btn = new Button( columnComp, SWT.PUSH );
      btn.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
      btn.setText( "Column" + i );
      btn.setData( DID_COLUMN_LAYOUT, layoutCfg.columnDatas().get( i ) );
      colButtons.add( btn );
    }

    Composite rowPanel = new Composite( bkPanel, SWT.BORDER );
    rowPanel.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.WEST );
    rowPanel.setLayout( new GridLayout( 1, false ) );
    for( int i = 0; i < rowCount; i++ ) {
      Button btn = new Button( rowPanel, SWT.PUSH );
      btn.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
      btn.setText( "Row" + i );
      btn.setData( DID_ROW_LAYOUT, layoutCfg.rowDatas().get( i ) );
      rowButtons.add( btn );
    }

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
          CellLayoutData newCld = CellLayoutDataPanel.edit( (CellLayoutData)btn.getData( DID_CELL_LAYOUT ), tsContext );
          if( newCld != null ) {
            int oldOcupiedCells = oldCld.horSpan() * oldCld.verSpan();
            int newOcupiedCells = newCld.horSpan() * newCld.verSpan();
            int delta = newOcupiedCells - oldOcupiedCells;
            if( delta > 0 ) {
              for( int ii = 0; ii < delta; ii++ ) {
                Button b = buttons.last();
                b.dispose();
                buttons.removeByIndex( buttons.indexOf( b ) );
              }
            }
            else {
              int idx = buttons.indexOf( btn );
              delta = -delta;
              for( int ii = 0; ii < delta; ii++ ) {
                Button b = new Button( cellsPanel, SWT.PUSH );
                b.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );
                buttons.add( b );
                CellLayoutData cd = new CellLayoutData();
                btn.setData( DID_CELL_LAYOUT, cd );
                buttons.insertAll( idx + 1, b );
              }
            }
            btn.setData( DID_CELL_LAYOUT, newCld );
            btn.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, newCld.horSpan(), newCld.verSpan() ) );
            bkPanel.layout();
          }
        }
      } );
    }
    bkPanel.layout();
  }

  void clear() {
    buttons.clear();
    colButtons.clear();
    rowButtons.clear();
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
