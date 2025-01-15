package org.toxsoft.skf.mnemo.gui.tsgui;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Создает дерево визуальных элементов редактора {@link IVedVisel} в соотвествии с отношениями master-slave и
 * соответствующим z-order'ом.
 *
 * @author vs
 */
public class VedViselsParentChildTreeMaker
    implements ITsTreeMaker<IVedVisel> {

  private final IVedScreen vedScreen;

  private final IVedViselsMasterSlaveRelationsManager msManager;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактора
   * @param aMsManager {@link IVedViselsMasterSlaveRelationsManager} - менеджер отношений
   */
  public VedViselsParentChildTreeMaker( IVedScreen aVedScreen, IVedViselsMasterSlaveRelationsManager aMsManager ) {
    vedScreen = aVedScreen;
    msManager = aMsManager;
  }

  // ------------------------------------------------------------------------------------
  // ITsTreeMaker
  //

  @Override
  public IList<ITsNode> makeRoots( ITsNode aRootNode, IList<IVedVisel> aItems ) {
    sort(); // отсортируем визели так, чтобы z-order соответствовал отношениям master-slave
    IListEdit<DefaultTsNode<IVedVisel>> nodes = new ElemArrayList<>();
    if( aRootNode.parent() == null ) { // самый корневой узел
      for( IVedVisel v : aItems ) {
        String masterId = msManager.viselMasterId( v.id() );
        if( masterId == null ) {
          nodes.add( createNode( aRootNode, v ) );
        }
      }
      for( DefaultTsNode<IVedVisel> vn : nodes ) {
        processNode( vn );
      }
    }
    else {
      processNode( (DefaultTsNode<IVedVisel>)aRootNode );
    }
    IListEdit<ITsNode> retNodes = new ElemArrayList<>();
    for( DefaultTsNode<IVedVisel> vn : nodes ) {
      retNodes.add( vn );
    }
    return retNodes;
  }

  @Override
  public boolean isItemNode( ITsNode aNode ) {
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void processNode( DefaultTsNode<IVedVisel> aParentNode ) {
    IList<DefaultTsNode<IVedVisel>> children = listOwnChildren( aParentNode );
    for( DefaultTsNode<IVedVisel> vn : children ) {
      aParentNode.addNode( vn );
      processNode( vn );
    }
  }

  private IList<DefaultTsNode<IVedVisel>> listOwnChildren( DefaultTsNode<IVedVisel> aParentNode ) {
    IListEdit<DefaultTsNode<IVedVisel>> nodes = new ElemArrayList<>();
    IVedVisel visel = aParentNode.entity();
    if( visel != null ) {
      IStringList slaveIds = msManager.listSlaveViselIds( visel.id() );
      for( String id : slaveIds ) {
        IVedVisel v = VedScreenUtils.findVisel( id, vedScreen );
        if( v == null ) {
          msManager.freeVisel( id );
        }
        else {
          nodes.add( createNode( aParentNode, v ) );
        }
      }
    }
    return nodes;
  }

  private static final ITsNodeKind<IVedVisel> NK_VISEL = new TsNodeKind<>( "nodeKindVisel", IVedVisel.class, true ); //$NON-NLS-1$

  private DefaultTsNode<IVedVisel> createNode( ITsNode aParentNode, IVedVisel aVisel ) {
    DefaultTsNode<IVedVisel> node = new DefaultTsNode<>( NK_VISEL, aParentNode, aVisel );
    node.setName( aVisel.nmName() );

    boolean hasChildren = !msManager.listSlaveViselIds( aVisel.id() ).isEmpty();
    String iconId = hasChildren ? ICONID_FOLDER : ICONID_VED_VISEL;
    node.setIconId( iconId );

    return node;
  }

  private void sort() {
    try {
      vedScreen.model().visels().eventer().pauseFiring();
      IStridablesListEdit<VedAbstractVisel> dest = new StridablesList<>();
      addChildren( null, vedScreen.model().visels().list(), dest );
      int idx = 0;
      for( VedAbstractVisel v : dest ) {
        vedScreen.model().visels().reorderer().move( v, idx );
        idx++;
      }
    }
    finally {
      vedScreen.model().visels().eventer().resumeFiring( false );
    }
  }

  private void addChildren( String aParentId, IList<VedAbstractVisel> aItems,
      IStridablesListEdit<VedAbstractVisel> aDest ) {
    // для всех детей добавим их в z-порядке
    for( VedAbstractVisel v : aItems ) {
      if( isChild( aParentId, v.id() ) ) {
        aDest.add( v );
        addChildren( v.id(), aItems, aDest );
      }
    }
  }

  private boolean isChild( String aParentId, String aViselId ) {
    String masterId = msManager.viselMasterId( aViselId );
    if( aParentId == null ) {
      return masterId == null;
    }
    return aParentId.equals( masterId );
  }

}
