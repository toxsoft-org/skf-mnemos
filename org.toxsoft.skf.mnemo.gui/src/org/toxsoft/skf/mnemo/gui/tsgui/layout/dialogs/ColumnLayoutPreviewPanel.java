package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.column.*;

class ColumnLayoutPreviewPanel {

  private final Composite bkPanel;

  private final Color colorWhite = new Color( 255, 255, 255 );

  private final ITsGuiContext tsContext;

  private final Image imgEdit;

  VedColumnLayoutControllerConfig layoutCfg;

  ColumnLayoutPreviewPanel( Composite aBkPanel, ITsGuiContext aTsContext ) {
    bkPanel = aBkPanel;
    tsContext = aTsContext;
    ITsIconManager iconManager = tsContext.get( ITsIconManager.class );
    imgEdit = iconManager.loadStdIcon( ICONID_DOCUMENT_EDIT, EIconSize.IS_16X16 );
  }

  void setLayoutConfig( VedColumnLayoutControllerConfig aLayoutCfg ) {
    layoutCfg = aLayoutCfg;
    update();
    bkPanel.redraw();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void update() {
    clear();
    int colCount = layoutCfg.columnCount();
    GridLayout gl = new GridLayout( colCount, false );
    gl.horizontalSpacing = (int)layoutCfg.horizontalGap();
    gl.verticalSpacing = (int)layoutCfg.verticalGap();
    bkPanel.setLayout( gl );

    for( int i = 0; i < colCount; i++ ) {
      Button btn = new Button( bkPanel, SWT.PUSH );
      btn.setImage( imgEdit );
      btn.setText( "Column" + (i + 1) + " ..." );
      btn.setData( layoutCfg.columnDatas().get( i ) );
      btn.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aE ) {
          TableColumnLayoutData currData = null;
          if( btn.getData() != null && btn.getData() instanceof TableColumnLayoutData ) {
            currData = (TableColumnLayoutData)btn.getData();
          }
          TableColumnLayoutData ld = TableColumnLayoutDataPanel.edit( currData, tsContext );
          if( ld != null ) {
            layoutCfg.replaceColumnData( currData, ld );
            btn.setData( ld );
          }
        }

      } );
    }

    for( int i = 0; i < 5; i++ ) {
      for( int j = 0; j < colCount; j++ ) {
        CLabel l = new CLabel( bkPanel, SWT.BORDER );
        l.setBackground( colorWhite );
        l.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
      }
    }
    bkPanel.layout();
  }

  void clear() {
    for( Control ctrl : bkPanel.getChildren() ) {
      ctrl.dispose();
    }
  }

}
