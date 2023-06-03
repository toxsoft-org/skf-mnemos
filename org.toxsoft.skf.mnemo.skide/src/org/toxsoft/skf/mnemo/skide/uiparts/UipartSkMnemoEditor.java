package org.toxsoft.skf.mnemo.skide.uiparts;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.mnemo.lib.*;

public class UipartSkMnemoEditor
    extends MwsAbstractPart {

  @Override
  protected void doInit( Composite aParent ) {

    // TODO UipartSkMnemoEditor.doInit()

  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void setMnemoCfg( ISkMnemoCfg aCfg ) {
    // TODO UipartSkMnemoEditor.setMnemoCfg()

    TsDialogUtils.error( getShell(), "setMnemoCfg( mnemoCfg );" );
  }

}
