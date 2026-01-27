package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Оработчик удаления RtConrol'ей.
 *
 * @author vs
 */
public class RtControlDeleteProcessor
    implements IDeleteProcessor {

  private final IRtControlsManager manager;

  /**
   * Constructor.
   *
   * @param aManager {@link IRtControlsManager} - менеджер RtCntrol'ей
   */
  public RtControlDeleteProcessor( IRtControlsManager aManager ) {
    manager = aManager;
  }

  @Override
  public String id() {
    return "rtControls.delete.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String description() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void editIdsForDelete( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams ) {
    // nop
  }

  @Override
  public void handlePostDeletion( IStringListEdit aViselIds, IStringListEdit aActorIds ) {
    for( String viselId : aViselIds ) {
      IRtControl rtc = manager.getRtControlByViselId( viselId );
      if( rtc != null ) {
        manager.remove( rtc.id() );
      }
    }
  }

}
