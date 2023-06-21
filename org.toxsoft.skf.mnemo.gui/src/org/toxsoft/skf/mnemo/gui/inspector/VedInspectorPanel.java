package org.toxsoft.skf.mnemo.gui.inspector;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.sandbox.test.vs.e4.services.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.api.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.api.inspector.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.impl.inspector.*;

public class VedInspectorPanel
    extends TsPanel {

  ITreeContentProvider contentProvider = new ITreeContentProvider() {

    @Override
    public Object[] getElements( Object aInputElement ) {
      if( aInputElement == null || !(aInputElement instanceof IInspectorDataNode node) ) {
        return null;
      }
      return node.children().toArray();
    }

    @Override
    public Object[] getChildren( Object aParentElement ) {
      IInspectorDataNode node = (IInspectorDataNode)aParentElement;
      return node.children().toArray();
    }

    @Override
    public Object getParent( Object aElement ) {
      IInspectorDataNode node = (IInspectorDataNode)aElement;
      return node.parent();
    }

    @Override
    public boolean hasChildren( Object aElement ) {
      IInspectorDataNode node = (IInspectorDataNode)aElement;
      return node.hasChildren();
    }

  };

  TreeViewer treeViewer;

  private final ICurrentVedSelectionService currSelectionService;

  public VedInspectorPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    currSelectionService = aContext.get( ICurrentVedSelectionService.class );

    setLayout( new BorderLayout() );

    treeViewer = new TreeViewer( this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION );

    treeViewer.getTree().setHeaderVisible( true );
    treeViewer.getTree().setLinesVisible( true );
    treeViewer.getTree().addListener( SWT.MeasureItem, event -> event.height = 40 );

    TreeViewerColumn columnName = new TreeViewerColumn( treeViewer, SWT.LEFT );
    columnName.getColumn().setText( "Название" );
    columnName.getColumn().setWidth( 200 );
    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IInspectorDataNode node = (IInspectorDataNode)aCell.getElement();
        aCell.setText( node.nmName() );
      }
    } );

    TreeViewerColumn columnValue = new TreeViewerColumn( treeViewer, SWT.LEFT );
    columnValue.getColumn().setText( "Значение" );
    columnValue.getColumn().setWidth( 200 );
    columnValue.setEditingSupport( new InspectorValueEditingSupport( treeViewer, tsContext() ) );
    columnValue.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IInspectorDataNode node = (IInspectorDataNode)aCell.getElement();
        aCell.setText( node.valueTextRepresentation() );
      }
    } );

    treeViewer.setContentProvider( contentProvider );
    treeViewer.getTree().setLayoutData( BorderLayout.CENTER );

    currSelectionService.addCurrentEntityChangeListener( aCurrent -> {
      if( aCurrent.isSingle() ) {
        IMesItem item = aCurrent.selectedItem();
        VedInspectorComposer vic = new VedInspectorComposer( tsContext() );
        IInspectorDataNode rootNode = vic.extractInspectorData( item ).rootNode();
        treeViewer.setInput( rootNode );
        return;
      }
      clear();
      return;
    } );

  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void clear() {
    treeViewer.setInput( null );
  }
}
