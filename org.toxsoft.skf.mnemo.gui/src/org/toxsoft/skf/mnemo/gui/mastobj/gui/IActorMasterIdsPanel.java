package org.toxsoft.skf.mnemo.gui.mastobj.gui;

import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

public interface IActorMasterIdsPanel
    extends IGenericEntityEditPanel<IStringMap<String>> {

  @Override
  void setEntity( IStringMap<String> aEntity );

  @Override
  ValidationResult canGetEntity();

  @Override
  IStringMap<String> getEntity();

}
