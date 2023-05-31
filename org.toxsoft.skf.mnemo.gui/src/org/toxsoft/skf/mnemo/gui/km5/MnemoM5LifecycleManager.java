package org.toxsoft.skf.mnemo.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.skf.mnemo.lib.*;

public class MnemoM5LifecycleManager
    extends M5LifecycleManager<ISkMnemoCfg, ISkMnemosService> {

  public MnemoM5LifecycleManager( IM5Model<ISkMnemoCfg> aModel, ISkMnemosService aMaster ) {
    super( aModel, true, true, true, true, aMaster );
    // TODO Auto-generated constructor stub
  }

}
