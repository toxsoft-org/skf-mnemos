package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;
import org.toxsoft.uskat.core.api.objserv.*;

public class RecognizerCellEditor
    extends DialogCellEditor {

  ISkoRecognizerCfg recognizerCfg = null;

  private final IStridablesList<ISkObject> objects;

  private final ITsGuiContext tsContext;

  /**
   * The label widget showing the command to execute.
   */
  private Label cmdGwidLabel;

  RecognizerCellEditor( Composite aParent, IStridablesList<ISkObject> aObjects, ITsGuiContext aTsContext ) {
    super( aParent, SWT.NONE );
    objects = aObjects;
    tsContext = aTsContext;
  }

  @Override
  protected Control createContents( Composite cell ) {
    Color bg = cell.getBackground();
    Composite composite = new Composite( cell, getStyle() );
    composite.setLayout( new BorderLayout() );
    composite.setBackground( bg );

    cmdGwidLabel = new Label( composite, SWT.LEFT );
    cmdGwidLabel.setBackground( bg );
    cmdGwidLabel.setFont( cell.getFont() );
    cmdGwidLabel.setLayoutData( BorderLayout.CENTER );
    return composite;
  }

  @Override
  protected Object openDialogBox( Control aCellEditorWindow ) {
    return ConfigRecognizerPanel.edit( recognizerCfg, objects, tsContext );
  }

}
