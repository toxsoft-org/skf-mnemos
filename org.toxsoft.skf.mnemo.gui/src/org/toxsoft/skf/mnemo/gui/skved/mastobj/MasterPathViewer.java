package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.ConfigRecognizerPanel.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.gui.conn.*;

public class MasterPathViewer
    extends TsPanel {

  abstract static class BaseNode
      implements IStridable {

    private final BaseNode parent;
    private final String   id;

    private String    name;
    private String    description;
    protected boolean asked = false;

    protected IListEdit<BaseNode> children = new ElemArrayList<>();

    BaseNode( BaseNode aParent, String aId, String aName, String aDescription ) {
      parent = aParent;
      id = aId;
      name = aName;
      description = aDescription;
    }

    BaseNode parent() {
      return parent;
    }

    @Override
    public String id() {
      return id;
    }

    @Override
    public String nmName() {
      return name;
    }

    @Override
    public String description() {
      return description;
    }

    IList<? extends BaseNode> children() {
      if( !asked ) {
        asked = true;
        fillChildren();
      }
      return children;
    }

    public boolean editable() {
      return false;
    }

    protected abstract Image image();

    protected abstract void fillChildren();
  }

  class ObjectNode
      extends BaseNode {

    private final Image imgBox;

    private final ISkClassInfo classInfo;

    ObjectNode( ISkClassInfo aClassInfo, BaseNode aParent ) {
      super( aParent, aClassInfo.id(), aClassInfo.nmName(), aClassInfo.description() );
      classInfo = aClassInfo;
      imgBox = iconManager().loadStdIcon( ICONID_OBJECT, EIconSize.IS_16X16 );
    }

    @Override
    protected void fillChildren() {
      IStridablesList<IDtoRivetInfo> rivetsList = classInfo.rivets().list();
      IStridablesList<IDtoLinkInfo> linksList = classInfo.links().list();

      for( IDtoRivetInfo ri : rivetsList ) {
        RivetNode ln = new RivetNode( ri, this );
        children.add( ln );
      }

      for( IDtoLinkInfo li : linksList ) {
        LinkNode ln = new LinkNode( li, this );
        children.add( ln );
      }
    }

    @Override
    protected Image image() {
      return imgBox;
    }
  }

  class MultiObjectsNode
      extends BaseNode {

    private final Image imgObjects;
    private final Image imgResolvedObject;

    private final ISkClassInfo classInfo;

    private ISkoRecognizerCfg recognizerCfg = null;

    MultiObjectsNode( ISkClassInfo aClassInfo, BaseNode aParent ) {
      super( aParent, aClassInfo.id(), aClassInfo.nmName(), aClassInfo.description() );
      classInfo = aClassInfo;
      imgObjects = iconManager().loadStdIcon( ICONID_OBJECTS, EIconSize.IS_16X16 );
      imgResolvedObject = iconManager().loadStdIcon( ICONID_RESOLVED_OBJECT, EIconSize.IS_16X16 );
    }

    @Override
    protected void fillChildren() {
      IStridablesList<IDtoRivetInfo> rivetsList = classInfo.rivets().list();
      IStridablesList<IDtoLinkInfo> linksList = classInfo.links().list();

      for( IDtoRivetInfo ri : rivetsList ) {
        RivetNode ln = new RivetNode( ri, this );
        children.add( ln );
      }

      for( IDtoLinkInfo li : linksList ) {
        LinkNode ln = new LinkNode( li, this );
        children.add( ln );
      }
    }

    @Override
    protected Image image() {
      if( recognizerCfg != null ) {
        return imgResolvedObject;
      }
      return imgObjects;
    }

    @Override
    public boolean editable() {
      return true;
    }

    ISkoRecognizerCfg recognizerCfg() {
      return recognizerCfg;
    }

    public void setRecognizerCfg( ISkoRecognizerCfg aRecognizerCfg ) {
      recognizerCfg = aRecognizerCfg;
      if( aRecognizerCfg == null ) {
        children.clear();
      }
    }
  }

  class RivetNode
      extends BaseNode {

    private final Image imgRivet;

    private final IDtoRivetInfo rivetInfo;

    RivetNode( IDtoRivetInfo aRivetInfo, BaseNode aParent ) {
      super( aParent, aRivetInfo.id(), aRivetInfo.nmName(), aRivetInfo.description() );
      rivetInfo = aRivetInfo;
      imgRivet = iconManager().loadStdIcon( ICONID_RIVET, EIconSize.IS_16X16 );
    }

    @Override
    protected void fillChildren() {
      ISkConnectionSupplier conSup = tsContext().get( ISkConnectionSupplier.class );
      ISkCoreApi coreApi = conSup.defConn().coreApi();
      ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( rivetInfo.rightClassId() );
      ObjectNode objNode = new ObjectNode( classInfo, this );
      children.add( objNode );
    }

    @Override
    protected Image image() {
      return imgRivet;
    }
  }

  class LinkNode
      extends BaseNode {

    private final Image imgLink;

    private final IDtoLinkInfo linkInfo;

    LinkNode( IDtoLinkInfo aLinkInfo, BaseNode aParent ) {
      super( aParent, aLinkInfo.id(), aLinkInfo.nmName(), aLinkInfo.description() );
      linkInfo = aLinkInfo;
      imgLink = iconManager().loadStdIcon( ICONID_LINK, EIconSize.IS_16X16 );
    }

    @Override
    protected void fillChildren() {
      ISkConnectionSupplier conSup = tsContext().get( ISkConnectionSupplier.class );
      ISkCoreApi coreApi = conSup.defConn().coreApi();
      ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( linkInfo.rightClassIds().first() );
      if( linkInfo.linkConstraint().isSingle() ) {
        ObjectNode objNode = new ObjectNode( classInfo, this );
        children.add( objNode );
      }
      else {
        MultiObjectsNode objectsNode = new MultiObjectsNode( classInfo, this );
        children.add( objectsNode );
      }
    }

    @Override
    protected Image image() {
      return imgLink;
    }
  }

  ITreeContentProvider contentProvider = new ITreeContentProvider() {

    @Override
    public boolean hasChildren( Object aElement ) {
      return ((BaseNode)aElement).children().size() > 0;
    }

    @Override
    public Object getParent( Object aElement ) {
      return ((BaseNode)aElement).parent();
    }

    @Override
    public Object[] getElements( Object aInputElement ) {
      return (Object[])aInputElement;
    }

    @Override
    public Object[] getChildren( Object aParentElement ) {
      return ((BaseNode)aParentElement).children().toArray();
    }
  };

  class RecognizerEditingSupport
      extends EditingSupport {

    public RecognizerEditingSupport( ColumnViewer aViewer ) {
      super( aViewer );
      // TODO Auto-generated constructor stub
    }

    @Override
    protected CellEditor getCellEditor( Object aElement ) {
      MultiObjectsNode node = (MultiObjectsNode)aElement;

      Skid objSkid = coreApi.objService().listObjs( node.classInfo.id(), true ).first().skid();
      PanelCtx ctx = new PanelCtx( objSkid, MasterPathViewer.this.tsContext() );
      return new RecognizerCellEditor( viewer.getTree(), ctx );
    }

    @Override
    protected boolean canEdit( Object aElement ) {
      return ((BaseNode)aElement).editable();
    }

    @Override
    protected Object getValue( Object aElement ) {
      MultiObjectsNode node = (MultiObjectsNode)aElement;
      return node.recognizerCfg();
    }

    @Override
    protected void setValue( Object aElement, Object aValue ) {
      MultiObjectsNode node = (MultiObjectsNode)aElement;
      node.setRecognizerCfg( (ISkoRecognizerCfg)aValue );
      viewer.update( node, null );
    }

  }

  TreeViewer viewer;

  ISkCoreApi coreApi;

  public MasterPathViewer( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    setLayout( new FillLayout() );

    coreApi = aContext.get( ISkConnectionSupplier.class ).defConn().coreApi();

    viewer = new TreeViewer( this, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
    viewer.getTree().setHeaderVisible( true );
    viewer.getTree().setLinesVisible( true );

    TreeViewerColumn columnId = new TreeViewerColumn( viewer, SWT.NONE );
    columnId.getColumn().setText( "Идентификатор" );
    columnId.getColumn().setWidth( 300 );
    columnId.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        BaseNode node = (BaseNode)aCell.getElement();
        aCell.setText( node.id );
        aCell.setImage( node.image() );
      }
    } );
    columnId.setEditingSupport( new RecognizerEditingSupport( viewer ) );

    TreeViewerColumn columnName = new TreeViewerColumn( viewer, SWT.NONE );
    columnName.getColumn().setText( "Имя" );
    columnName.getColumn().setWidth( 300 );
    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        BaseNode node = (BaseNode)aCell.getElement();
        String str = node.nmName();
        if( str.isBlank() ) {
          str = node.id();
        }
        aCell.setText( str );
      }
    } );

    viewer.setContentProvider( contentProvider );

    ISkConnectionSupplier conSup = tsContext().get( ISkConnectionSupplier.class );
    ISkCoreApi coreApi = conSup.defConn().coreApi();
    ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( "gbh.TurboCompressor" );
    BaseNode node = new ObjectNode( classInfo, null );
    Object[] input = new Object[1];
    input[0] = node;
    viewer.setInput( input );
  }

}
