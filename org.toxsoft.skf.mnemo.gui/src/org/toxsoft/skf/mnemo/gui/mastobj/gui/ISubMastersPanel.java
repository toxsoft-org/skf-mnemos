package org.toxsoft.skf.mnemo.gui.mastobj.gui;

import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;

public interface ISubMastersPanel
    extends IGenericEntityEditPanel<IStringMap<ICompoundResolverConfig>> {

  @Override
  void setEntity( IStringMap<ICompoundResolverConfig> aEntity );

  @Override
  ValidationResult canGetEntity();

  @Override
  IStringMap<ICompoundResolverConfig> getEntity();

}
