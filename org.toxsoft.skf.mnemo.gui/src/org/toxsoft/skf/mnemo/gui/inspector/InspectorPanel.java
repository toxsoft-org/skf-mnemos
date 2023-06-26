package org.toxsoft.skf.mnemo.gui.inspector;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.sandbox.test.vs.e4.services.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.dtypes.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.impl.inspector.ved.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.impl.inspector1.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.impl.inspector3.*;

public class InspectorPanel
    extends TsPanel {

  private final ICurrentVedSelectionService currSelectionService;

  private final InspectorWidget inspector;

  public InspectorPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    aParent.setLayout( new BorderLayout() );

    inspector = new InspectorWidget( aContext );
    inspector.createControl( aParent );
    inspector.getControl().setLayoutData( BorderLayout.CENTER );

    currSelectionService = aContext.get( ICurrentVedSelectionService.class );

    currSelectionService.addCurrentEntityChangeListener( aCurrent -> {
      if( aCurrent.isSingle() ) {
        // IMesItem item = aCurrent.selectedItem();
        // VedInspectorComposer vic = new VedInspectorComposer( tsContext() );
        // IInspectorDataNode rootNode = vic.extractInspectorData( item ).rootNode();

        // treeViewer.setInput( rootNode );

        InspectorMetaBuilder builder = new InspectorMetaBuilder();
        FieldInfo root = builder.root();
        FieldInfo colorNode = builder.addChildNode( root, VdtColor.dataType, VdtColor.dataType, true );

        builder.addChildNode( colorNode, "red", "red", IVedDataTypeConstants.DT_COLOR_COMP, true );
        builder.addChildNode( colorNode, "green", "green", IVedDataTypeConstants.DT_COLOR_COMP, true );
        builder.addChildNode( colorNode, "blue", "blue", IVedDataTypeConstants.DT_COLOR_COMP, true );

        // inspector.setMetaInfo( builder.buildMetaInfo() );

        InspectorDataBuilder dataBuilder = new InspectorDataBuilder();
        FieldValue rootValue = dataBuilder.root();

        FieldValue v = dataBuilder.addValue( VdtColor.dataType.id(), rootValue, avValobj( new RGB( 1, 2, 3 ) ), false );
        dataBuilder.addValue( "red", v, avInt( 10 ), true );
        dataBuilder.addValue( "green", v, avInt( 20 ), true );
        dataBuilder.addValue( "blue", v, avInt( 30 ), true );

        // inspector.setData( dataBuilder.buildData() );

        // ColorFieldInfo colorFi = new ColorFieldInfo( "fgColor", "Цвет рисования" );
        // RootInspectorNode root = new RootInspectorNode();
        // // InspectorNode cn = new InspectorNode( root, colorFi );
        // root.addChild( colorFi );
        // createChildren( root );
        // treeViewer.setInput( root );

        RootInspectorNode rn = new RootInspectorNode();
        DataDef dd = DataDef.create3( "textColor", VdtColor.dataType, TSID_NAME, avStr( "Цвет текста" ) );
        FieldInfo fi = new FieldInfo( dd, VdtColor.dataType, true, false );
        ColorNode cn = new ColorNode( fi, null, avValobj( new RGB( 64, 128, 255 ) ) );

        dd = DataDef.create3( "X", VdtFloating.dataType, TSID_NAME, avStr( "X" ) );
        fi = new FieldInfo( dd, VdtFloating.dataType, true, false );
        DoubleNode dn = new DoubleNode( fi, null, avFloat( 0 ) );

        dd = DataDef.create3( "Location", VdtD2Point.dataType, TSID_NAME, "Положение" );
        fi = new FieldInfo( dd, VdtD2Point.dataType, true, false );
        D2PointNode d2pn = new D2PointNode( fi, null, avValobj( new D2Point( 100, 200 ) ) );

        rn.addChild( dn );
        rn.addChild( cn );
        rn.addChild( d2pn );

        inspector.iTree.setInput( rn );
        return;
      }
      inspector.setMetaInfo( null );
      return;
    } );

  }

}
