package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Обработчик выделения визуальных элементов, поддерживающий группы.
 *
 * @author vs
 */
public class VedViselSelectionGroupManager
    extends VedViselSelectionManager
    implements IVedViselSelectionGroupManager {

  private final IVedViselGroupsManager groupManager;

  private final IStringListEdit selGroupIds = new StringArrayList();

  public VedViselSelectionGroupManager( IVedScreen aVedScreen, IVedViselGroupsManager aGroupManager ) {
    super( aVedScreen );
    groupManager = aGroupManager;
  }

  // ------------------------------------------------------------------------------------
  // IVedViselSelectionManager
  //

  @Override
  public void setSingleSelectedViselId( String aViselId ) {
    if( aViselId == null ) {
      super.setSingleSelectedViselId( aViselId );
      return;
    }
    IStringList groupIds = groupManager.viselGroupIds( aViselId );
    if( groupIds.isEmpty() ) {
      super.setSingleSelectedViselId( aViselId );
      return;
    }
    selIdsList.clear();
    setViselSelection( aViselId, true );
  }

  @Override
  public void setViselSelection( String aViselId, boolean aSelection ) {
    IStringList groupIds = groupManager.viselGroupIds( aViselId );
    if( groupIds.isEmpty() ) {
      super.setViselSelection( aViselId, aSelection );
    }
    else {
      IStringList vIds = groupManager.listViselIds( groupIds.last() );
      if( aSelection ) {
        for( String vId : vIds ) {
          selIdsList.add( vId );
        }
      }
      else {
        for( String vId : vIds ) {
          selIdsList.remove( vId );
        }
      }
      eventer.fireChangeEvent();
    }
  }

  @Override
  public void deselectAll() {
    selGroupIds.clear();
    selIdsList.clear();
  }

  // ------------------------------------------------------------------------------------
  // IVedViselSelectionGroupManager
  //

  @Override
  public IVedViselGroupsManager groupsManager() {
    return groupManager;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

}
