package org.toxsoft.skf.mnemo.skide.uiparts;

import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.sandbox.test.vs.mnemo.gui.*;
import org.toxsoft.uskat.core.gui.e4.uiparts.*;

/**
 * UIpart: Редактор мнемосхем.
 *
 * @author vs
 */
public class UipartMnemoEditor
    extends SkMwsAbstractPart {

  private MnemoEditorPanel editorPanel = null;

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    editorPanel = new MnemoEditorPanel( aParent, tsContext() );
  }

}
