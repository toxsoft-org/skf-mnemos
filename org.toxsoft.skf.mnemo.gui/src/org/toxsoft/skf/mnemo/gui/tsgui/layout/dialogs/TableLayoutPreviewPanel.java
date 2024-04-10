package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

class TableLayoutPreviewPanel {

  private final Composite bkPanel;

  private final Color colorWhite = new Color( 255, 255, 255 );

  VedTableLayoutControllerConfig layoutCfg;

  TableLayoutPreviewPanel( Composite aBkPanel ) {
    bkPanel = aBkPanel;
  }

  void setLayoutConfig( VedTableLayoutControllerConfig aLayoutCfg ) {
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
    int rowCount = layoutCfg.rowCount();
    bkPanel.setLayout( new GridLayout( colCount + 1, false ) );
    CLabel l = new CLabel( bkPanel, SWT.NONE );
    GridData gd = new GridData();
    gd.minimumWidth = 50;
    gd.widthHint = 50;
    l.setLayoutData( gd );

    for( int i = 0; i < colCount; i++ ) {
      Button btn = new Button( bkPanel, SWT.PUSH );
      btn.setText( "Col" + (i + 1) );
      btn.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aE ) {

        }

      } );
    }

    for( int i = 0; i < rowCount; i++ ) {
      Button btn = new Button( bkPanel, SWT.PUSH );
      btn.setText( "Row" + (i + 1) );
      for( int j = 0; j < colCount; j++ ) {
        Button btn1 = new Button( bkPanel, SWT.PUSH );
        btn1.setText( "Cell" + (i + 1) + "" + (j + 1) );
        btn1.setBackground( colorWhite );
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
