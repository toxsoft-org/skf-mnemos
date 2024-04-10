package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

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

  protected CellLayoutDataPanel( Composite aParent, CellLayoutData aData, ITsGuiContext aEnviron, int aFlags ) {
    super( aParent, aEnviron, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( CellLayoutData aData ) {
    if( aData != null ) {
      EHorAlignment horAl = aData.horAlignment();
      boolean fillWidth = aData.fillCellWidth();
      EVerAlignment verAl = aData.verAlignment();
      boolean fillHeight = aData.fillCellHeight();
      CellAlignment cellAl = new CellAlignment( horAl, fillWidth, verAl, fillHeight );
      alignPanel.setDataRecord( cellAl );
      horSpanSpin.setValue( Integer.valueOf( aData.horSpan() ) );
      verSpanSpin.setValue( Integer.valueOf( aData.verSpan() ) );
      marginsPanel.setDataRecord( aData.margins() );
      margins = aData.margins();
    }
    else {
      CellAlignment cellAl = new CellAlignment( EHorAlignment.LEFT, false, EVerAlignment.TOP, false );
      alignPanel.setDataRecord( cellAl );
      horSpanSpin.setValue( Integer.valueOf( 1 ) );
      verSpanSpin.setValue( Integer.valueOf( 1 ) );
    }
  }

  @Override
  protected CellLayoutData doGetDataRecord() {
    CellAlignment cellAl = alignPanel.getDataRecord();
    int hSpan = horSpanSpin.getValue().intValue();
    int vSpan = verSpanSpin.getValue().intValue();
    ID2Margins d2m = marginsPanel.getDataRecord();
    return new CellLayoutData( d2m, cellAl, hSpan, vSpan );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  D2MarginsPanel      marginsPanel;
  CellAlignmentPanel  alignPanel;
  ValedIntegerSpinner horSpanSpin;
  ValedIntegerSpinner verSpanSpin;

  ID2Margins margins = new D2Margins();

  void init() {
    setLayout( new GridLayout( 2, false ) );
    Group g = new Group( this, SWT.NONE );
    g.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, false, true ) );
    g.setText( "Поля" );
    g.setLayout( new FillLayout() );
    marginsPanel = new D2MarginsPanel( g, margins, tsContext(), 0 );

    g = new Group( this, SWT.NONE );
    g.setLayoutData( new GridData( SWT.LEFT, SWT.TOP, false, true ) );
    g.setText( "Выравнивание" );
    g.setLayout( new GridLayout( 1, false ) );

    alignPanel = new CellAlignmentPanel( g, new CellAlignment(), tsContext(), 0 );

    CLabel l;

    l = new CLabel( this, SWT.NONE );
    l.setText( "Занять по горизонтали: " );
    horSpanSpin = new ValedIntegerSpinner( environ() );
    horSpanSpin.setLimits( 1, 1, 1, 1000 );
    horSpanSpin.setValue( 1 );
    horSpanSpin.createControl( this );

    l = new CLabel( this, SWT.NONE );
    l.setText( "Занять по вертикали: " );
    verSpanSpin = new ValedIntegerSpinner( environ() );
    verSpanSpin.setLimits( 1, 1, 1, 1000 );
    verSpanSpin.setValue( 1 );
    verSpanSpin.createControl( this );
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования конфигурации табличного контроллера размещения.
   *
   * @param aData CellLayoutData - данные ячейки
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final CellLayoutData edit( CellLayoutData aData, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    IDialogPanelCreator<CellLayoutData, ITsGuiContext> creator = CellLayoutDataPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "DLG_T_CANVAS_CFG", "STR_MSG_CANVAS_CFG" );
    TsDialog<CellLayoutData, ITsGuiContext> d = new TsDialog<>( dlgInfo, aData, aTsContext, creator );
    return d.execData();
  }

}
