package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация интерфейса {@link IVedViselGroupsManager}.
 *
 * @author vs
 */
public class VedViselGroupsManager
    implements IVedViselGroupsManager {

  private static final String GROUP_ID_PREFIX = "vedGroup"; //$NON-NLS-1$

  private static final String PARAMID_GROUP = "vedGroupId"; //$NON-NLS-1$

  private final IListEdit<IVedGroupChangeListener> listeners = new ElemArrayList<>();

  private final IStringMapEdit<Integer> groupIds = new StringMap<>();

  private final IVedScreenModel screenModel;

  /**
   * Конструктор.
   *
   * @param aScreenModel {@link IVedScreenModel} - модель данных редактора
   */
  public VedViselGroupsManager( IVedScreenModel aScreenModel ) {
    screenModel = aScreenModel;
  }

  // ------------------------------------------------------------------------------------
  // IVedViselGroupsManager
  //

  @Override
  public IStringList groupIds() {
    return groupIds.keys();
  }

  @Override
  public IStringList listViselIds( String aGroupId ) {
    IStringListEdit result = new StringArrayList();
    for( VedAbstractVisel visel : screenModel.visels().list() ) {
      if( isInGroup( aGroupId, visel ) ) {
        result.add( visel.id() );
      }
    }
    return result;
  }

  @Override
  public IStringList viselGroupIds( String aViselId ) {
    String groupsIdPath = viselGroupPath( screenModel.visels().list().getByKey( aViselId ) );
    if( groupsIdPath.isBlank() ) {
      return IStringList.EMPTY;
    }
    return StridUtils.getComponents( groupsIdPath );
  }

  @Override
  public void groupVisels( IStringList aViselIds ) {
    TsNullArgumentRtException.checkNull( aViselIds );
    if( aViselIds.isEmpty() ) { // если список пустой
      return; // ничего не делаем
    }
    int gi = generateGroupInt();
    String groupId = GROUP_ID_PREFIX + gi;
    groupIds.put( groupId, Integer.valueOf( gi ) );
    IStridablesList<VedAbstractVisel> visels = screenModel.visels().list();
    for( String viselId : aViselIds ) {
      VedAbstractVisel visel = visels.getByKey( viselId );
      addToGroup( visel, groupId );
    }
    fireGroupEvent( groupId, aViselIds );
  }

  @Override
  public void ungroupVisels( String aGroupId ) {
    IStringList viselIds = listViselIds( aGroupId );
    groupIds.removeByKey( aGroupId );
    for( VedAbstractVisel visel : screenModel.visels().list() ) {
      removeFromGroup( visel, aGroupId );
    }
    fireUngroupEvent( aGroupId, viselIds );
  }

  @Override
  public void addGroupChangeListener( IVedGroupChangeListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeGroupChangeListener( IVedGroupChangeListener aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static void addToGroup( VedAbstractVisel aVisel, String aGroupId ) {
    String gPath = viselGroupPath( aVisel );
    if( !gPath.isBlank() ) {
      gPath = StridUtils.appendIdName( gPath, aGroupId );
    }
    else {
      gPath = aGroupId;
    }
    aVisel.params().setStr( PARAMID_GROUP, gPath );
  }

  private static void removeFromGroup( VedAbstractVisel aVisel, String aGroupId ) {
    String gPath = viselGroupPath( aVisel );
    if( !gPath.isBlank() ) {
      StridUtils.removeEndingIdPath( gPath, aGroupId );
    }
  }

  private static String viselGroupPath( VedAbstractVisel aVisel ) {
    if( aVisel.params().hasKey( PARAMID_GROUP ) ) {
      return aVisel.params().getStr( PARAMID_GROUP );
    }
    return TsLibUtils.EMPTY_STRING;
  }

  private static boolean isInGroup( String aGroupId, VedAbstractVisel aVisel ) {
    String groupIdPath = viselGroupPath( aVisel );
    if( groupIdPath.isBlank() ) {
      return false;
    }
    return StridUtils.endsWithIdPath( groupIdPath, aGroupId );
  }

  private int generateGroupInt() {
    int gi = 1;
    while( groupIds.values().hasElem( Integer.valueOf( gi ) ) ) {
      gi++;
    }
    return gi;
  }

  private void fireGroupEvent( String aGroupId, IStringList aViselIds ) {
    for( IVedGroupChangeListener l : listeners ) {
      l.onGroup( aGroupId, aViselIds );
    }
  }

  private void fireUngroupEvent( String aGroupId, IStringList aViselIds ) {
    for( IVedGroupChangeListener l : listeners ) {
      l.onUngroup( aGroupId, aViselIds );
    }
  }

}
