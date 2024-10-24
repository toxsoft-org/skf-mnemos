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
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

/**
 * Панель редактирования конфигурации табличного контроллера размещения.
 *
 * @author vs
 */
public class TableLayoutDataPanel
    extends AbstractTsDialogPanel<IVedLayoutControllerConfig, IVedScreen> {

  VedTableLayoutControllerConfig controllerConfig;

  protected TableLayoutDataPanel( Composite aParent, TsDialog<IVedLayoutControllerConfig, IVedScreen> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected TableLayoutDataPanel( Composite aParent, ITsGuiContext aContext, IVedLayoutControllerConfig aData,
      IVedScreen aEnviron, int aFlags ) {
    super( aParent, aContext, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( IVedLayoutControllerConfig aData ) {
    if( aData != null ) {
      controllerConfig = new VedTableLayoutControllerConfig( aData );
    }
    else {
      controllerConfig = new VedTableLayoutControllerConfig();
      controllerConfig.init( 6, 4 );
    }
    colCount = controllerConfig.columnCount();
    rowCount = controllerConfig.rowCount();
    colSpin.setValue( Integer.valueOf( colCount ) );
    rowSpin.setValue( Integer.valueOf( rowCount ) );
    hGapSpin.setValue( (int)controllerConfig.horizontalGap() );
    vGapSpin.setValue( (int)controllerConfig.verticalGap() );
    marginsPanel.setDataRecord( controllerConfig.margins() );
    previewPanel.setLayoutConfig( controllerConfig );
  }

  @Override
  protected IVedLayoutControllerConfig doGetDataRecord() {
    VedTableLayoutControllerConfig cfg = previewPanel.layoutConfig();
    cfg.setMargins( marginsPanel.getDataRecord() );
    cfg.setHGap( hGapSpin.getValue().intValue() );
    cfg.setVGap( vGapSpin.getValue().intValue() );
    return cfg.config();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  D2MarginsPanel                marginsPanel;
  ValedEnumCombo<EHorAlignment> horAlignCombo;
  ValedBooleanCheck             horFillCheck;
  ValedEnumCombo<EVerAlignment> verAlignCombo;
  ValedBooleanCheck             vertFillCheck;

  ValedIntegerSpinner colSpin;
  ValedIntegerSpinner rowSpin;

  ValedIntegerSpinner hGapSpin;
  ValedIntegerSpinner vGapSpin;

  TableLayoutPreviewPanel previewPanel;

  int colCount = 1;
  int rowCount = 1;

  void init() {
    setLayout( new GridLayout( 1, false ) );
    Composite topComp = new Composite( this, SWT.NONE );
    topComp.setLayout( new GridLayout( 3, false ) );

    Group tableGroup = new Group( topComp, SWT.NONE );
    tableGroup.setLayout( new GridLayout( 2, false ) );
    tableGroup.setText( "Таблица" );

    CLabel l = new CLabel( tableGroup, SWT.NONE );
    l.setText( "Столбцов: " );
    colSpin = new ValedIntegerSpinner( environ().tsContext() );
    colSpin.setLimits( 1, 1, 1, 1000 );
    colSpin.setValue( 2 );
    colSpin.createControl( tableGroup );
    colSpin.eventer().addListener( ( aSource, aEditFinished ) -> {
      if( aEditFinished ) {
        if( colCount != ((Integer)aSource.getValue()).intValue() ) {
          onColumnCountChanged( (Integer)aSource.getValue() );
        }
      }
    } );

    l = new CLabel( tableGroup, SWT.NONE );
    l.setText( "Строк: " );
    rowSpin = new ValedIntegerSpinner( environ().tsContext() );
    rowSpin.setValue( 1 );
    rowSpin.setLimits( 1, 1, 1, 1000 );
    rowSpin.createControl( tableGroup );
    rowSpin.eventer().addListener( ( aSource, aEditFinished ) -> {
      if( aEditFinished ) {
        if( rowCount != ((Integer)aSource.getValue()).intValue() ) {
          onRowCountChanged( (Integer)aSource.getValue() );
        }
      }
    } );

    // Button btnEqualWidth = new Button( tableGroup, SWT.CHECK );
    // btnEqualWidth.setText( "Одинаковая ширина столбцов" );
    // btnEqualWidth.setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, 4, 1 ) );

    // Composite marginsComp = new Composite( topComp, SWT.NONE );
    // marginsComp.setLayout( new FillLayout() );
    // marginsComp.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    Group marginsGroup = new Group( topComp, SWT.NONE );
    marginsGroup.setLayout( new FillLayout() );
    marginsGroup.setText( "Поля" );
    marginsPanel = new D2MarginsPanel( marginsGroup, new D2Margins(), environ().tsContext(), SWT.HORIZONTAL );

    Group gapGroup = new Group( topComp, SWT.NONE );
    gapGroup.setLayout( new GridLayout( 2, false ) );
    gapGroup.setText( "Зазоры" );

    l = new CLabel( gapGroup, SWT.NONE );
    l.setText( "По горизонтали: " );
    hGapSpin = new ValedIntegerSpinner( environ().tsContext() );
    hGapSpin.setLimits( 1, 1, 1, 1000 );
    hGapSpin.setValue( 2 );
    hGapSpin.createControl( gapGroup );

    l = new CLabel( gapGroup, SWT.NONE );
    l.setText( "По вертикали: " );
    vGapSpin = new ValedIntegerSpinner( environ().tsContext() );
    vGapSpin.setLimits( 1, 1, 1, 1000 );
    vGapSpin.setValue( 2 );
    vGapSpin.createControl( gapGroup );

    Composite previewComp = new Composite( this, SWT.NONE );
    previewComp.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 4, 1 ) );
    previewPanel = new TableLayoutPreviewPanel( previewComp, tsContext() );
  }

  void onColumnCountChanged( Integer aValue ) {
    // int delta = aValue.intValue() - colCount;
    // if( delta > 0 ) {
    // controllerConfig.addColumns( delta );
    // }
    // else {
    // controllerConfig.removeColumns( -delta );
    // }
    colCount = aValue.intValue();
    controllerConfig.init( colCount, rowCount );
    // controllerConfig.cellDatas().clear();
    // for( int i = 0; i < colCount * controllerConfig.rowCount(); i++ ) {
    // controllerConfig.cellDatas().add( new CellLayoutData() );
    // }
    // controllerConfig.columnDatas().clear();
    // for( int i = 0; i < colCount; i++ ) {
    // controllerConfig.columnDatas().add( new TableColumnLayoutData() );
    // }
    previewPanel.setLayoutConfig( controllerConfig );
  }

  void onRowCountChanged( Integer aValue ) {
    // int delta = aValue.intValue() - rowCount;
    // if( delta > 0 ) {
    // controllerConfig.addRows( delta );
    // for( int i = 0; i < controllerConfig.columnCount(); i++ ) {
    // controllerConfig.cellDatas().add( new CellLayoutData() );
    // }
    // }
    // else {
    // controllerConfig.removeRows( -delta );
    // }

    rowCount = aValue.intValue();
    controllerConfig.init( colCount, rowCount );
    // controllerConfig.cellDatas().clear();
    // for( int i = 0; i < controllerConfig.columnCount() * rowCount; i++ ) {
    // controllerConfig.cellDatas().add( new CellLayoutData() );
    // }
    // controllerConfig.rowDatas().clear();
    // for( int i = 0; i < rowCount; i++ ) {
    // controllerConfig.rowDatas().add( new TableRowLayoutData() );
    // }
    previewPanel.setLayoutConfig( controllerConfig );
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования конфигурации табличного контроллера размещения.
   *
   * @param aCfg VedTableLayoutControllerConfig -
   * @param aVedScreen IVedScreen - экран редактирования
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final IVedLayoutControllerConfig edit( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    IDialogPanelCreator<IVedLayoutControllerConfig, IVedScreen> creator = TableLayoutDataPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aVedScreen.tsContext(), "DLG_T_CANVAS_CFG", "STR_MSG_CANVAS_CFG" );
    TsDialog<IVedLayoutControllerConfig, IVedScreen> d = new TsDialog<>( dlgInfo, aCfg, aVedScreen, creator );
    return d.execData();
  }

}
