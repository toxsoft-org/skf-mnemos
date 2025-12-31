package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skf.ext.mastobj.gui.skved.*;

public class MasterObjectDeleteProcessor
    implements IDeleteProcessor {

  private final IVedScreen vedScreen;

  public MasterObjectDeleteProcessor( IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "ved.delete.masterObject.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Корректор информации \"разрешителя\" мастер-объекта мнемосхемы при удалении";
  }

  @Override
  public String description() {
    return "Корректирует информацию \"разрешителя\" мастер-объекта мнемосхемы в соотвествии удаляемыми элементами";
  }

  // ------------------------------------------------------------------------------------
  // IDeleteProcessor
  //

  @Override
  public void editIdsForDelete( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams ) {
    // nop
  }

  @Override
  public void handlePostDeletion( IStringListEdit aViselIds, IStringListEdit aActorIds ) {
    MasterObjectUtils.deleteActorSubmasters( aActorIds, vedScreen );
  }

}
