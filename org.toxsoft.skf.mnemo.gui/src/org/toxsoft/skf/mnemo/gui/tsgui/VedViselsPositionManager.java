package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

public class VedViselsPositionManager
    implements IVedViselsPositionManager {

  private final IStridablesListEdit<IViselsPositionProcessor> processors = new StridablesList<>();

  /**
   * Конструктор.
   */
  public VedViselsPositionManager() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IVedViselsPositionManager
  //

  @Override
  public IStringList listViselIds2Move( String aViselId ) {
    IStringListEdit viselIds = new StringArrayList();
    if( aViselId != null ) {
      viselIds.add( aViselId );
    }
    for( IViselsPositionProcessor p : processors ) {
      p.editIdsForMove( viselIds, null );
    }
    return viselIds;
  }

  @Override
  public void addProcessor( IViselsPositionProcessor aProcessor ) {
    if( !processors.hasKey( aProcessor.id() ) ) {
      processors.add( aProcessor );
    }
  }

  @Override
  public void moveOnSwtIncrement( IStringList aViselIds, int aSwtDx, int aSwtDy ) {

    IStringListEdit viselIds = new StringArrayList( aViselIds );

    for( IViselsPositionProcessor p : processors ) {
      p.editIdsForMove( viselIds, null );
    }

  }

}
