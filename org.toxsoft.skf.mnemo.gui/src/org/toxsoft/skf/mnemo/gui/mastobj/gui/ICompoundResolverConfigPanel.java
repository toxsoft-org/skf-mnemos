package org.toxsoft.skf.mnemo.gui.mastobj.gui;

import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;

public interface ICompoundResolverConfigPanel
    extends IGenericEntityEditPanel<ICompoundResolverConfig> {

  @Override
  void setEntity( ICompoundResolverConfig aEntity );

  @Override
  ValidationResult canGetEntity();

  @Override
  ICompoundResolverConfig getEntity();

}
