package org.toxsoft.skf.mnemo.gui.tsgui;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

public class VedViselsDeleteManager
    implements IVedViselsDeleteManager, ITsGuiContextable {

  class DeleteActionProvider
      extends MethodPerActionTsActionSetProvider {

    DeleteActionProvider() {
      defineAction( ACDEF_REMOVE, VedViselsDeleteManager.this::deleteRelevant );
    }

    @Override
    protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
      if( clickedVisel == null && !hasIds4delete() ) {
        return false;
      }
      return true;
    }

  }

  private final IVedScreen vedScreen;

  private final MenuCreatorFromAsp menuCreator;

  private final DeleteActionProvider actionsProvider;

  private VedAbstractVisel clickedVisel = null;

  private final IOptionSetEdit params = new OptionSet();

  private final IStridablesListEdit<IDeleteProcessor> processors = new StridablesList<>();

  public VedViselsDeleteManager( IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
    actionsProvider = new DeleteActionProvider();
    menuCreator = new MenuCreatorFromAsp( actionsProvider, vedScreen.tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiConextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IVedContextMenuCreator
  //

  @Override
  public boolean fillMenu( Menu aMenu, VedAbstractVisel aClickedVisel, ITsPoint aSwtCoors ) {
    clickedVisel = aClickedVisel;
    return menuCreator.fillMenu( aMenu );
  }

  // ------------------------------------------------------------------------------------
  // IVedViselsDeleteManager
  //

  @Override
  public void deleteRelevant() {
    vedScreen.model().visels().eventer().pauseFiring();
    vedScreen.model().actors().eventer().pauseFiring();

    params.clear();
    IStringListEdit viselIds = new StringArrayList();
    IStringListEdit actorIds = new StringArrayList();

    if( clickedVisel != null ) {
      viselIds.add( clickedVisel.id() );
    }
    for( IDeleteProcessor p : processors ) {
      p.editIdsForDelete( viselIds, actorIds, params );
    }
    deleteActors( actorIds );
    deleteVisels( viselIds );
    for( IDeleteProcessor p : processors ) {
      p.handlePostDeletion( viselIds, actorIds );
    }

    vedScreen.model().visels().eventer().resumeFiring( !viselIds.isEmpty() );
    vedScreen.model().actors().eventer().resumeFiring( !actorIds.isEmpty() );
  }

  // @Override
  // public void deleteVisel( String aViselId ) {
  // vedScreen.model().visels().remove( aViselId );
  // }

  // @Override
  public void deleteVisels( IStringList aViselIds ) {
    for( String id : aViselIds ) {
      vedScreen.model().visels().remove( id );
    }
  }

  // @Override
  public void deleteActors( IStringList aActorIds ) {
    for( String id : aActorIds ) {
      vedScreen.model().actors().remove( id );
    }
  }

  @Override
  public void addProcessor( IDeleteProcessor aProcessor ) {
    if( !processors.hasKey( aProcessor.id() ) ) {
      processors.add( aProcessor );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  boolean hasIds4delete() {
    IStringListEdit viselIds = new StringArrayList();
    IStringListEdit actorIds = new StringArrayList();

    for( IDeleteProcessor p : processors ) {
      p.editIdsForDelete( viselIds, actorIds, params );
    }
    return viselIds.size() > 0;
  }

  // void doRemove() {
  // vedScreen.model().visels().eventer().pauseFiring();
  // vedScreen.model().actors().eventer().pauseFiring();
  //
  // params.clear();
  // IStringListEdit viselIds = new StringArrayList();
  // IStringListEdit actorIds = new StringArrayList();
  //
  // for( IDeleteProcessor p : processors ) {
  // p.editIdsForDelete( viselIds, actorIds, params );
  // }
  // deleteActors( actorIds );
  // deleteVisels( viselIds );
  //
  // vedScreen.model().visels().eventer().resumeFiring( !viselIds.isEmpty() );
  // vedScreen.model().actors().eventer().resumeFiring( !actorIds.isEmpty() );
  // }

}
