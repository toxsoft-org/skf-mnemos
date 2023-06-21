package org.toxsoft.skf.mnemo.gui.inspector;

import static org.toxsoft.core.tsgui.valed.controls.enums.IValedEnumConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.api.inspector.*;

public class InspectorValueEditingSupport
    extends EditingSupport {

  private final TreeViewer viewer;

  private final ITsGuiContext tsContext;

  ValedCellEditor<IAtomicValue> cellEditor;

  public InspectorValueEditingSupport( TreeViewer aViewer, ITsGuiContext aTsContext ) {
    super( aViewer );
    viewer = aViewer;
    tsContext = aTsContext;
  }

  @Override
  protected CellEditor getCellEditor( Object aElement ) {
    IInspectorDataNode node = (IInspectorDataNode)aElement;
    String factoryId = node.editorFactoryId();
    if( factoryId == null || factoryId.isBlank() ) {
      cellEditor = null;
    }
    else {
      IValedControlFactoriesRegistry fr = tsContext.get( IValedControlFactoriesRegistry.class );
      // fr.fi
      TsGuiContext ctx = new TsGuiContext( tsContext );
      if( node.value().isAssigned() ) {
        ctx.params().setStr( OPID_ENUM_CLASS_NAME, node.value().asValobj().getClass().getCanonicalName() );
        if( node.dataType().keeperId() != null ) {
          ctx.params().setStr( TSID_KEEPER_ID, node.dataType().keeperId() );
        }
      }
      IValedControlFactory valedFactory = fr.getFactory( factoryId );
      cellEditor = new ValedCellEditor<>( valedFactory.createEditor( ctx ), viewer, ctx );
    }
    return cellEditor;
  }

  @Override
  protected boolean canEdit( Object aElement ) {
    IInspectorDataNode node = (IInspectorDataNode)aElement;
    return node.canEdit();
  }

  @Override
  protected Object getValue( Object aElement ) {
    IInspectorDataNode node = (IInspectorDataNode)aElement;
    return node.value();
  }

  @Override
  protected void setValue( Object aElement, Object aValue ) {
    IInspectorDataNode node = (IInspectorDataNode)aElement;
    node.updateValue( (IAtomicValue)aValue );
    viewer.refresh( node );
    viewer.update( node, null );
    IInspectorDataNode parent = node.parent();
    while( parent != null ) {
      viewer.refresh( parent );
      parent = parent.parent();
    }
  }

}
