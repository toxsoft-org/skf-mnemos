package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

/**
 * Панель редактирования полей прямоугольной области.
 *
 * @author vs
 */
public class D2MarginsPanel
    extends AbstractTsDialogPanel<ID2Margins, ITsGuiContext> {

  protected D2MarginsPanel( Composite aParent, TsDialog<ID2Margins, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected D2MarginsPanel( Composite aParent, ID2Margins aData, ITsGuiContext aEnviron, int aFlags ) {
    super( aParent, aEnviron, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ID2Margins aData ) {
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
    int columnCount = 2;
    if( (panelFlags() & SWT.HORIZONTAL) != 0 ) {
      columnCount = 4;
    }
    setLayout( new GridLayout( columnCount, false ) );
    CLabel l;

    l = new CLabel( this, SWT.NONE );
    l.setText( "Лево: " );
    TsGuiContext valedContext = new TsGuiContext( environ() );
    valedContext.params().setValue( TSID_MIN_INCLUSIVE, IAtomicValue.NULL );
    valedContext.params().setDouble( TSID_MIN_INCLUSIVE, 0. );
    valedContext.params().setValue( TSID_MAX_INCLUSIVE, IAtomicValue.NULL );
    valedContext.params().setDouble( TSID_MAX_INCLUSIVE, 1000. );

    leftSpiner = new ValedDoubleSpinner( valedContext );
    leftSpiner.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Верх: " );
    topSpiner = new ValedDoubleSpinner( valedContext );
    topSpiner.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Право: " );
    rightSpiner = new ValedDoubleSpinner( valedContext );
    rightSpiner.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Низ: " );
    bottomSpiner = new ValedDoubleSpinner( valedContext );
    bottomSpiner.createControl( this );
  }

}
