package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.layout.*;

public class RecognizerCellEditor
    extends DialogCellEditor {

  ITsGuiContext tsContext;

  ISkObjectRecognizer recognizer = null;

  /**
   * The label widget showing the command to execute.
   */
  private Label cmdGwidLabel;

  RecognizerCellEditor( Composite aParent, ITsGuiContext aTsContext ) {
    super( aParent, SWT.NONE );
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
    return ConfigRecognizerPanel.edit( recognizer, tsContext );
  }

}
