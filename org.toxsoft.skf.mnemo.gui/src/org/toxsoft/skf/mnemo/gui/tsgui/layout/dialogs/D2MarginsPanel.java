package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

/**
 * Панель редактирования полей прямоугольной области.
 *
 * @author vs
 */
public class D2MarginsPanel
    extends AbstractTsDialogPanel<D2Margins, ITsGuiContext> {

  protected D2MarginsPanel( Composite aParent, TsDialog<D2Margins, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected D2MarginsPanel( Composite aParent, ITsGuiContext aContext, D2Margins aData, ITsGuiContext aEnviron,
      int aFlags ) {
    super( aParent, aContext, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( D2Margins aData ) {
    if( aData != null ) {
      leftSpiner.setValue( Double.valueOf( aData.left() ) );
      topSpiner.setValue( Double.valueOf( aData.top() ) );
      rightSpiner.setValue( Double.valueOf( aData.right() ) );
      bottomSpiner.setValue( Double.valueOf( aData.bottom() ) );
    }
  }

  @Override
  protected D2Margins doGetDataRecord() {
    double left = leftSpiner.getValue().doubleValue();
    double top = topSpiner.getValue().doubleValue();
    double right = rightSpiner.getValue().doubleValue();
    double bottom = bottomSpiner.getValue().doubleValue();
    return new D2Margins( left, right, top, bottom );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  ValedDoubleSpinner leftSpiner;
  ValedDoubleSpinner topSpiner;
  ValedDoubleSpinner rightSpiner;
  ValedDoubleSpinner bottomSpiner;

  void init() {
    setLayout( new GridLayout( 2, false ) );
    CLabel l;

    l = new CLabel( this, SWT.NONE );
    l.setText( "Лево: " );
    leftSpiner = new ValedDoubleSpinner( environ() );
    leftSpiner.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Верх: " );
    topSpiner = new ValedDoubleSpinner( environ() );
    topSpiner.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Право: " );
    rightSpiner = new ValedDoubleSpinner( environ() );
    rightSpiner.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Низ: " );
    bottomSpiner = new ValedDoubleSpinner( environ() );
    bottomSpiner.createControl( this );
  }

}
