package org.toxsoft.skf.mnemo.skide.e4.addons;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.uskat.core.gui.e4.uiparts.*;

/**
 * UIpart: Редактор мнемосхем.
 *
 * @author vs
 */
public class UipartMnemoEditor
    extends SkMwsAbstractPart {

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    CLabel l = new CLabel( aParent, SWT.CENTER );
    l.setText( "Редактор мнемосхем" );
  }

}
