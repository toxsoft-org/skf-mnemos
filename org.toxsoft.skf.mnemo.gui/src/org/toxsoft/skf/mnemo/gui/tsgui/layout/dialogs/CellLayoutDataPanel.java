package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

/**
 * Панель редактирования полей прямоугольной области.
 *
 * @author vs
 */
public class CellLayoutDataPanel
    extends AbstractTsDialogPanel<CellLayoutData, ITsGuiContext> {

  protected CellLayoutDataPanel( Composite aParent, TsDialog<CellLayoutData, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected CellLayoutDataPanel( Composite aParent, ITsGuiContext aContext, CellLayoutData aData,
      ITsGuiContext aEnviron, int aFlags ) {
    super( aParent, aContext, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( CellLayoutData aData ) {
    if( aData != null ) {
      horAlignCombo.setValue( aData.horAlignment() );
      verAlignCombo.setValue( aData.verAlignment() );
      horFillCheck.setValue( Boolean.valueOf( aData.fillCellWidth() ) );
      vertFillCheck.setValue( Boolean.valueOf( aData.fillCellHeight() ) );
    }
  }

  @Override
  protected CellLayoutData doGetDataRecord() {
    EHorAlignment horAlign = horAlignCombo.getValue();
    boolean fillHor = horFillCheck.getValue().booleanValue();
    EVerAlignment verAlign = verAlignCombo.getValue();
    boolean fillVert = vertFillCheck.getValue().booleanValue();
    return new CellLayoutData( null, horAlign, verAlign, fillVert, fillHor );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  D2MarginsPanel                marginsPanel;
  ValedEnumCombo<EHorAlignment> horAlignCombo;
  ValedBooleanCheck             horFillCheck;
  ValedEnumCombo<EVerAlignment> verAlignCombo;
  ValedBooleanCheck             vertFillCheck;

  void init() {
    setLayout( new GridLayout( 2, false ) );
    Group g = new Group( this, SWT.NONE );
    g.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, false, true ) );
    g.setText( "Поля" );
    g.setLayout( new FillLayout() );
    marginsPanel = new D2MarginsPanel( g, environ(), dataRecordInput().margins(), null, 0 );

    g = new Group( this, SWT.NONE );
    g.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, false, true ) );
    g.setText( "Выравнивание" );
    g.setLayout( new GridLayout( 2, false ) );

    CLabel l;

    l = new CLabel( g, SWT.NONE );
    l.setText( "По горизонтали: " );
    horAlignCombo = new ValedEnumCombo<>( environ() );
    horAlignCombo.createControl( g );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Заполнить гор.: " );
    horFillCheck = new ValedBooleanCheck( environ() );
    horFillCheck.createControl( g );

    l = new CLabel( this, SWT.NONE );
    l.setText( "По вертикали: " );
    verAlignCombo = new ValedEnumCombo<>( environ() );
    verAlignCombo.createControl( g );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Заполнить верт.: " );
    vertFillCheck = new ValedBooleanCheck( environ() );
    vertFillCheck.createControl( g );
  }

}
