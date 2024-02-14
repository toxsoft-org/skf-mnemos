package org.toxsoft.skf.mnemo.gui.skved;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.strid.*;

public class StridableTableViewer {

  private final TableViewer viewer;

  protected StridableTableViewer( Composite aParent, int aStyle, int aIdWidth, int aNameWidth, int aDescrWidth ) {
    viewer = new TableViewer( aParent, aStyle );

    viewer.getTable().setHeaderVisible( true );
    viewer.getTable().setLinesVisible( true );

    TableViewerColumn columnId = new TableViewerColumn( viewer, SWT.NONE );
    columnId.getColumn().setWidth( aIdWidth );
    columnId.getColumn().setText( "ИД" );
    columnId.setLabelProvider( new ColumnLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IStridable s = (IStridable)aCell.getElement();
        aCell.setText( s.id() );
      }

      @Override
      public String getToolTipText( Object aElement ) {
        return ((IStridable)aElement).description();
      }

    } );

    TableViewerColumn columnName = new TableViewerColumn( viewer, SWT.NONE );
    columnName.getColumn().setWidth( aNameWidth );
    columnName.getColumn().setText( "Наименование" );
    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IStridable s = (IStridable)aCell.getElement();
        aCell.setText( s.nmName() );
      }

      @Override
      public String getToolTipText( Object aElement ) {
        return ((IStridable)aElement).description();
      }

    } );

    if( aDescrWidth > 0 ) {
      TableViewerColumn columnDescr = new TableViewerColumn( viewer, SWT.NONE );
      columnDescr.getColumn().setWidth( aDescrWidth );
      columnDescr.getColumn().setText( "Описание" );
      columnDescr.setLabelProvider( new CellLabelProvider() {

        @Override
        public void update( ViewerCell aCell ) {
          IStridable s = (IStridable)aCell.getElement();
          aCell.setText( s.description() );
        }

      } );
    }

    ColumnViewerToolTipSupport.enableFor( viewer );
    viewer.setContentProvider( new ArrayContentProvider() );
  }

  TableViewer viewer() {
    return viewer;
  }

}
