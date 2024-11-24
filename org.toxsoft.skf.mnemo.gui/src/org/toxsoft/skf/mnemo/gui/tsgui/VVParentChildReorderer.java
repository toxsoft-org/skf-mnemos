package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.ved.incub.drag.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class VVParentChildReorderer
    implements IM5TreeDragReorderer {

  private final IVedScreen vedScreen;

  private final IVedViselsMasterSlaveRelationsManager msManager;

  public VVParentChildReorderer( IVedScreen aVedScreen, IVedViselsMasterSlaveRelationsManager aMsManager ) {
    vedScreen = aVedScreen;
    msManager = aMsManager;
  }

  @Override
  public void reorder( IM5TreeViewer<IVedVisel> aViewer, IListReorderer<IVedVisel> aReorderer, ITsNode aSource,
      ITsNode aTarget, ECollectionDropPlace aPlace ) {

    IListEdit<ITsNode> nodes = aSource.listAllTsNodesBelow( true );

    IVedVisel sourceVisel = (IVedVisel)aSource.entity();
    VedAbstractVisel targetVisel = null;
    if( aPlace != ECollectionDropPlace.NOTHING ) {
      targetVisel = (VedAbstractVisel)aTarget.entity();
    }
    String sourceMasterId = msManager.viselMasterId( sourceVisel.id() );
    String targetMasterId = null;
    if( targetVisel != null ) {
      targetMasterId = msManager.viselMasterId( targetVisel.id() );
    }

    if( sourceMasterId != null ) {
      msManager.freeVisel( sourceVisel.id() );
    }

    int idx = 0;
    switch( aPlace ) {
      case AFTER:
        idx = vedScreen.model().visels().list().indexOf( targetVisel ) + 1;
        targetVisel = (VedAbstractVisel)aTarget.entity();
        if( targetMasterId != null ) {
          msManager.enslaveVisel( sourceVisel.id(), targetMasterId );
        }
        break;
      case BEFORE:
        idx = vedScreen.model().visels().list().indexOf( targetVisel );
        targetVisel = (VedAbstractVisel)aTarget.entity();
        if( targetMasterId != null ) {
          msManager.enslaveVisel( sourceVisel.id(), targetMasterId );
        }
        break;
      case NOTHING: // перемещаем узел в конец списка
        idx = vedScreen.model().visels().list().size() - nodes.size();
        break;
      case ON:
        targetVisel = (VedAbstractVisel)aTarget.entity();
        idx = vedScreen.model().visels().list().indexOf( targetVisel );// + 1;
        msManager.enslaveVisel( sourceVisel.id(), targetVisel.id() );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

    if( targetMasterId != null && targetMasterId.equals( sourceMasterId )
        || targetMasterId == null && msManager.viselMasterId( sourceVisel.id() ) == null ) {
      for( ITsNode node : nodes ) {
        boolean result = aReorderer.move( (VedAbstractVisel)node.entity(), idx );
        // System.out.println( "Move result = " + result );
        idx++;
      }
      return;
    }

    IVedItemsManager<? extends IVedVisel> vm = vedScreen.model().visels();
    IList<? extends IVedVisel> items = vm.list();
    aViewer.treeMaker().makeRoots( aViewer.rootNode(), (IList<IVedVisel>)items );

  }

}
