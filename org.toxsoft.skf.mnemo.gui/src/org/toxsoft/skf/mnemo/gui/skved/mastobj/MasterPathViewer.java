package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Контроль в виде дерева для выбора пути к мастер-объекту.
 *
 * @author vs
 */
public class MasterPathViewer
    extends TsPanel {

  enum EMpvNodeKind {
    RIVET,
    LINK,
    MULTI,
    OBJECT,
  }

  abstract static class BaseNode
      implements IMasterPathNode {

    private final BaseNode parent;
    private final String   id;

    private String    name;
    private String    description;
    protected boolean asked = false;

    protected IListEdit<BaseNode> children = new ElemArrayList<>();

    private final EMpvNodeKind kind;

    BaseNode( EMpvNodeKind aKind, BaseNode aParent, String aId, String aName, String aDescription ) {
      kind = aKind;
      parent = aParent;
      id = aId;
      name = aName;
      description = aDescription;
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

    final EMpvNodeKind kind() {
      return kind;
    }

    @Override
    public BaseNode parent() {
      return parent;
    }

    @Override
    public IList<? extends BaseNode> children() {
      if( !asked ) {
        asked = true;
        fillChildren();
      }
      return children;
    }

    @Override
    public boolean isObject() {
      return false;
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
      super( EMpvNodeKind.OBJECT, aParent, aClassInfo.id(), aClassInfo.nmName(), aClassInfo.description() );
      classInfo = aClassInfo;
      imgBox = iconManager().loadStdIcon( ICONID_OBJECT, EIconSize.IS_16X16 );
    }

    @Override
    protected void fillChildren() {
      IStridablesList<IDtoRivetInfo> rivetsList = classInfo.rivets().list();
      IStridablesList<IDtoLinkInfo> linksList = classInfo.links().list();

      for( IDtoRivetInfo ri : rivetsList ) {
        RivetNode ln = new RivetNode( classInfo.id(), ri, this );
        children.add( ln );
      }

      for( IDtoLinkInfo li : linksList ) {
        LinkNode ln = new LinkNode( classInfo.id(), li, this );
        children.add( ln );
      }
    }

    @Override
    protected Image image() {
      return imgBox;
    }

    @Override
    public SimpleResolverCfg resolverConfig() {
      BaseNode parent = parent();
      if( parent.kind() == EMpvNodeKind.LINK ) {
        LinkNode ln = (LinkNode)parent;
        IDtoLinkInfo li = ln.linkInfo;
        Ugwi ugwi = UgwiKindSkLinkInfo.makeUgwi( ln.classId(), li.id() );
        IOptionSetEdit opSet = new OptionSet();
        opSet.setValobj( PROP_UGWI, ugwi );
        return new SimpleResolverCfg( LinkInfoResolver.FACTORY_ID, opSet );
      }
      if( parent.kind() == EMpvNodeKind.RIVET ) {
        RivetNode rn = (RivetNode)parent;
        IDtoRivetInfo ri = rn.rivetInfo;
        Ugwi ugwi = UgwiKindSkRivetInfo.makeUgwi( rn.classId(), ri.id() );
        IOptionSetEdit opSet = new OptionSet();
        opSet.setValobj( PROP_UGWI, ugwi );
        return new SimpleResolverCfg( RivetInfoResolver.FACTORY_ID, opSet );
      }
      return null;
    }

    @Override
    public boolean isObject() {
      return true;
    }

    @Override
    public String classId() {
      return classInfo.id();
    }
  }

  class MultiObjectsNode
      extends BaseNode {

    private final Image imgObjects;
    private final Image imgResolvedObject;

    private final ISkClassInfo classInfo;

    private ISkoRecognizerCfg recognizerCfg = null;

    MultiObjectsNode( ISkClassInfo aClassInfo, BaseNode aParent ) {
      super( EMpvNodeKind.MULTI, aParent, aClassInfo.id(), aClassInfo.nmName(), aClassInfo.description() );
      classInfo = aClassInfo;
      imgObjects = iconManager().loadStdIcon( ICONID_OBJECTS, EIconSize.IS_16X16 );
      imgResolvedObject = iconManager().loadStdIcon( ICONID_RESOLVED_OBJECT, EIconSize.IS_16X16 );
    }

    @Override
    protected void fillChildren() {
      if( recognizerCfg == null ) {
        children.clear();
        return;
      }
      IStridablesList<IDtoRivetInfo> rivetsList = classInfo.rivets().list();
      IStridablesList<IDtoLinkInfo> linksList = classInfo.links().list();

      for( IDtoRivetInfo ri : rivetsList ) {
        RivetNode ln = new RivetNode( classInfo.id(), ri, this );
        children.add( ln );
      }

      for( IDtoLinkInfo li : linksList ) {
        LinkNode ln = new LinkNode( classInfo.id(), li, this );
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

    @Override
    public boolean isObject() {
      if( recognizerCfg != null ) {
        return true;
      }
      return false;
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

    @Override
    public SimpleResolverCfg resolverConfig() {
      BaseNode parent = parent();
      if( parent.kind() == EMpvNodeKind.LINK ) {
        LinkNode ln = (LinkNode)parent;
        IDtoLinkInfo li = ln.linkInfo;
        Ugwi ugwi = UgwiKindSkLinkInfo.makeUgwi( ln.classId(), li.id() );
        IOptionSetEdit opSet = new OptionSet();
        opSet.setValobj( PROP_UGWI, ugwi );
        opSet.setValobj( LinkInfoResolver.PROPID_RECOGNIZER_CFG, recognizerCfg );
        return new SimpleResolverCfg( LinkInfoResolver.FACTORY_ID, opSet );
      }
      if( parent.kind() == EMpvNodeKind.RIVET ) {
        RivetNode rn = (RivetNode)parent;
        IDtoRivetInfo ri = rn.rivetInfo;
        Ugwi ugwi = UgwiKindSkRivetInfo.makeUgwi( rn.classId(), ri.id() );
        IOptionSetEdit opSet = new OptionSet();
        opSet.setValobj( PROP_UGWI, ugwi );
        opSet.setValobj( RivetInfoResolver.PROPID_RECOGNIZER_CFG, recognizerCfg );
        return new SimpleResolverCfg( RivetInfoResolver.FACTORY_ID, opSet );
      }
      return null;
    }

    IStridablesList<ISkObject> listObjects() {
      if( parent().kind() == EMpvNodeKind.LINK ) {
        return ((LinkNode)parent()).linkedObjects();
      }
      return IStridablesList.EMPTY;
    }

    @Override
    public String classId() {
      return classInfo.id();
    }

  }

  class RivetNode
      extends BaseNode {

    private final Image imgRivet;

    private final String classId;

    private final IDtoRivetInfo rivetInfo;

    RivetNode( String aClassId, IDtoRivetInfo aRivetInfo, BaseNode aParent ) {
      super( EMpvNodeKind.RIVET, aParent, aRivetInfo.id(), aRivetInfo.nmName(), aRivetInfo.description() );
      classId = aClassId;
      rivetInfo = aRivetInfo;
      imgRivet = iconManager().loadStdIcon( ICONID_RIVET, EIconSize.IS_16X16 );
    }

    @Override
    protected void fillChildren() {
      ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( rivetInfo.rightClassId() );
      ObjectNode objNode = new ObjectNode( classInfo, this );
      children.add( objNode );
    }

    @Override
    protected Image image() {
      return imgRivet;
    }

    @Override
    public SimpleResolverCfg resolverConfig() {
      throw new TsUnsupportedFeatureRtException(); // нельзя вызывать этот метод для данного узла
    }

    @Override
    public String classId() {
      return classId;
    }
  }

  class LinkNode
      extends BaseNode {

    private final Image imgLink;

    private final String classId;

    private final IDtoLinkInfo linkInfo;

    LinkNode( String aClassId, IDtoLinkInfo aLinkInfo, BaseNode aParent ) {
      super( EMpvNodeKind.LINK, aParent, aLinkInfo.id(), aLinkInfo.nmName(), aLinkInfo.description() );
      classId = aClassId;
      linkInfo = aLinkInfo;
      imgLink = iconManager().loadStdIcon( ICONID_LINK, EIconSize.IS_16X16 );
    }

    @Override
    protected void fillChildren() {
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

    @Override
    public SimpleResolverCfg resolverConfig() {
      throw new TsUnsupportedFeatureRtException(); // нельзя вызывать этот метод для данного узла
    }

    @Override
    public String classId() {
      return classId;
    }

    IStridablesList<ISkObject> linkedObjects() {
      if( parent().kind() == EMpvNodeKind.OBJECT ) {
        ObjectNode objNode = (ObjectNode)parent();
        ISkObject parentObj = coreApi.objService().listObjs( objNode.classInfo.id(), true ).first();
        IDtoLinkFwd lfwd = coreApi.linkService().getLinkFwd( parentObj.skid(), linkInfo.id() );
        IList<ISkObject> skObjs = coreApi.objService().getObjs( lfwd.rightSkids() );
        return new StridablesList<>( skObjs );
      }
      return IStridablesList.EMPTY;
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

      // IList<ISkObject> objs = coreApi.objService().listObjs( node.classInfo.id(), true );
      // IStridablesList<ISkObject> objects = new StridablesList<>( objs );
      IStridablesList<ISkObject> objects = node.listObjects();
      return new RecognizerCellEditor( viewer.getTree(), objects, tsContext() );
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
      if( node.recognizerCfg() == null || !node.recognizerCfg().equals( aValue ) ) {
        node.setRecognizerCfg( (ISkoRecognizerCfg)aValue );
        node.fillChildren();
        viewer.update( node, null );

        BaseNode bn = node;
        while( bn != null ) {
          viewer.setExpandedElements( bn );
          bn = bn.parent();
        }
      }
    }

  }

  TreeViewer viewer;

  ISkCoreApi coreApi;

  private final String masterClassId;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aMasterClassId String - ИД класса мастер-объекта
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   */
  public MasterPathViewer( Composite aParent, String aMasterClassId, ITsGuiContext aContext ) {
    super( aParent, aContext );

    masterClassId = aMasterClassId;
    setLayout( new FillLayout() );

    coreApi = aContext.get( ISkConnectionSupplier.class ).defConn().coreApi();

    viewer = new TreeViewer( this, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL );
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

    ISkClassInfo classInfo = coreApi.sysdescr().findClassInfo( masterClassId );
    BaseNode node = new ObjectNode( classInfo, null );
    Object[] input = new Object[1];
    input[0] = node;
    viewer.setInput( input );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает выделенный узел дерева или <code>null</code>.
   *
   * @return {@link IMasterPathNode} - выделенный узел дерева или <code>null</code>
   */
  public IMasterPathNode selectedNode() {
    return selectedBaseNode();
  }

  /**
   * Возвращает конфигурацию "разрешителя", который разрешает {@link Skid} мастер-объекта в {@link Ugwi} типа
   * {@link UgwiKindSkSkid} требемого объекта.
   *
   * @return {@link ICompoundResolverConfig} - конфигурация "разрешителя"
   */
  public ICompoundResolverConfig resolverConfig() {
    IListEdit<SimpleResolverCfg> simpleConfigs = new ElemArrayList<>();
    IMasterPathNode node = selectedNode();
    while( node.isObject() && node.parent() != null ) {
      SimpleResolverCfg cfg = node.resolverConfig();
      simpleConfigs.insert( 0, cfg );
      node = node.parent().parent();
    }
    return new CompoundResolverConfig( simpleConfigs );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  BaseNode selectedBaseNode() {
    IStructuredSelection sel = (IStructuredSelection)viewer.getSelection();
    if( !sel.isEmpty() ) {
      return (BaseNode)sel.getFirstElement();
    }
    return null;
  }

}
