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
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

/**
 * Панель редактирования полей прямоугольной области.
 *
 * @author vs
 */
public class TableColumnLayoutDataPanel
    extends AbstractTsDialogPanel<TableColumnLayoutData, ITsGuiContext> {

  protected TableColumnLayoutDataPanel( Composite aParent,
      TsDialog<TableColumnLayoutData, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected TableColumnLayoutDataPanel( Composite aParent, ITsGuiContext aContext, TableColumnLayoutData aData,
      ITsGuiContext aEnviron, int aFlags ) {
    super( aParent, aContext, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( TableColumnLayoutData aData ) {
    ID2Margins d2m = new D2Margins();
    CellLayoutData cld = new CellLayoutData();
    if( aData != null ) {
      minMaxWidth = aData.widthRange();
      grabExcessSpace = aData.grabExcessSpace();
      cld = aData.cellData();
      d2m = cld.margins();
    }
    spinMin.setValue( minMaxWidth.left() );
    spinMax.setValue( minMaxWidth.right() );
    if( minMaxWidth.left().doubleValue() < 0 && minMaxWidth.right().doubleValue() < 0 ) {
      checkAutoSize.getControl().setSelection( true );
      spinMin.getControl().setEnabled( false );
      spinMax.getControl().setEnabled( false );
    }
    marginsPanel.setDataRecord( d2m );
    alignPanel.setDataRecord( cld.cellAlignment() );
    checkGrabSpace.getControl().setSelection( grabExcessSpace );
  }

  @Override
  protected TableColumnLayoutData doGetDataRecord() {
    ID2Margins d2m = marginsPanel.getDataRecord();
    CellAlignment cellAl = alignPanel.getDataRecord();

    grabExcessSpace = checkGrabSpace.getValue().booleanValue();
    minMaxWidth = new Pair<>( spinMin.getValue(), spinMax.getValue() );
    CellLayoutData cld = new CellLayoutData( d2m, cellAl, 1, 1 );
    return new TableColumnLayoutData( minMaxWidth, grabExcessSpace, cld );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private Pair<Double, Double> minMaxWidth = new Pair<>( Double.valueOf( -1. ), Double.valueOf( -1. ) );

  private boolean grabExcessSpace = false;

  ValedBooleanCheck  checkAutoSize;
  ValedDoubleSpinner spinMin;
  ValedDoubleSpinner spinMax;
  ValedBooleanCheck  checkGrabSpace;

  D2MarginsPanel     marginsPanel;
  CellAlignmentPanel alignPanel;

  void init() {
    // Подготовим контекст для ValedDoubleSpinner
    TsGuiContext valedCtx = new TsGuiContext( tsContext() );
    valedCtx.params().setValue( TSID_MIN_INCLUSIVE, IAtomicValue.NULL );
    valedCtx.params().setDouble( TSID_MIN_INCLUSIVE, -1. );
    valedCtx.params().setValue( TSID_MAX_INCLUSIVE, IAtomicValue.NULL );
    valedCtx.params().setDouble( TSID_MAX_INCLUSIVE, Float.MAX_VALUE );

    setLayout( new GridLayout( 2, false ) );

    Group g = new Group( this, SWT.NONE );
    g.setLayout( new GridLayout( 2, false ) );
    g.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );
    g.setText( "Ширина" );

    Group marginsGroup = new Group( this, SWT.NONE );
    marginsGroup.setLayout( new FillLayout() );
    marginsGroup.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 1, 1 ) );
    marginsGroup.setText( "Поля" );
    marginsPanel = new D2MarginsPanel( marginsGroup, new D2Margins(), valedCtx, SWT.HORIZONTAL );
    marginsPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    Group alignGroup = new Group( this, SWT.NONE );
    alignGroup.setLayout( new FillLayout() );
    alignGroup.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
    alignGroup.setText( "Выравнивание" );
    alignPanel = new CellAlignmentPanel( alignGroup, new CellAlignment(), valedCtx, SWT.HORIZONTAL );

    CLabel l;

    checkAutoSize = new ValedBooleanCheck( valedCtx );
    checkAutoSize.params().setStr( ValedBooleanCheck.OPID_TEXT, "определять автоматически" );
    Control ctrl = checkAutoSize.createControl( g );
    ctrl.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 2, 1 ) );
    checkAutoSize.eventer().addListener( ( aSource, aEditFinished ) -> {
      boolean autoSize = checkAutoSize.getControl().getSelection();
      if( autoSize ) {
        spinMin.setValue( Double.valueOf( -1. ) );
        spinMax.setValue( Double.valueOf( -1. ) );
      }
      else {
        spinMin.setValue( Double.valueOf( 24 ) );
        spinMax.setValue( Double.valueOf( 100 ) );
      }
      spinMin.getControl().setEnabled( !autoSize );
      spinMax.getControl().setEnabled( !autoSize );
    } );

    l = new CLabel( g, SWT.NONE );
    l.setText( "Минимальная: " );
    spinMin = new ValedDoubleSpinner( valedCtx );
    spinMin.createControl( g );

    l = new CLabel( g, SWT.NONE );
    l.setText( "Максимальная: " );
    spinMax = new ValedDoubleSpinner( valedCtx );
    spinMax.createControl( g );

    checkGrabSpace = new ValedBooleanCheck( valedCtx );
    checkGrabSpace.params().setStr( ValedBooleanCheck.OPID_TEXT, "занимать дополнительное пространство по ширине" );
    ctrl = checkGrabSpace.createControl( this );
    ctrl.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 2, 1 ) );

  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования конфигурации табличного контроллера размещения.
   *
   * @param aData TableColumnLayoutData - данные ячейки
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final TableColumnLayoutData edit( TableColumnLayoutData aData, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    IDialogPanelCreator<TableColumnLayoutData, ITsGuiContext> creator = TableColumnLayoutDataPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "DLG_T_CANVAS_CFG", "STR_MSG_CANVAS_CFG" );
    TsDialog<TableColumnLayoutData, ITsGuiContext> d = new TsDialog<>( dlgInfo, aData, aTsContext, creator );
    return d.execData();
  }

}
