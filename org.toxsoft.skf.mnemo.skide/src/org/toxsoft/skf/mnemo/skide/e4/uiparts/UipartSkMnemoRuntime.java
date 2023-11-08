package org.toxsoft.skf.mnemo.skide.e4.uiparts;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.skf.mnemo.gui.glib.*;

/**
 * Mnemoscheme editor UIpart.
 *
 * @author hazard157
 */
public class UipartSkMnemoRuntime
    extends MwsAbstractPart {

  IRuntimeMnemoPanel panel;

  @Override
  protected void doInit( Composite aParent ) {
    aParent.setLayout( new BorderLayout() );
    panel = new RuntimeMnemoPanel( aParent, tsContext() );
  }

}
