package org.toxsoft.skf.mnemo.skide.uiparts;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.skf.mnemo.gui.inspector.*;

public class UipartSkMnemoObjInspector
    extends MwsAbstractPart {

  VedInspectorPanel inspectorPanel;

  @Override
  protected void doInit( Composite aParent ) {

    aParent.setLayout( new BorderLayout() );
    inspectorPanel = new VedInspectorPanel( aParent, tsContext() );
    inspectorPanel.setLayoutData( BorderLayout.CENTER );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

}
