package org.toxsoft.skf.mnemo.skide.e4.uiparts;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.skide.glib.*;

public class UipartSkMnemoEditor
    extends MwsAbstractPart {

  IMnemoEditorPanel panel;

  @Override
  protected void doInit( Composite aParent ) {
    panel = new MnemoEditorPanel( aParent, tsContext() );

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
