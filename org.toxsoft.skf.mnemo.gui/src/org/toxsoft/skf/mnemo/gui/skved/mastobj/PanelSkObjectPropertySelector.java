package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.skf.mnemo.gui.skved.*;

/**
 * Панель выбора свойства sk-объекта.
 *
 * @author vs
 */
public class PanelSkObjectPropertySelector
    extends TsPanel {

  StridableTableViewer attrsViewer;

  public PanelSkObjectPropertySelector( Composite aParent, ITsGuiContext aContext, int aStyle ) {
    super( aParent, aContext, aStyle );
    // TODO Auto-generated constructor stub
  }

  public void setObjectSkid() {

  }

  // StridableTableViewer createAttrsViewer(Composite aParent) {
  // int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL;
  // attrsViewer = new StridableTableViewer( aParent, style, 80, 200, -1 );
  // }

}
