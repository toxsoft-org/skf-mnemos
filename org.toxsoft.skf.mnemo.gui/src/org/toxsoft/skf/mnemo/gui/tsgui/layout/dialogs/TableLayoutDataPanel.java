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
    previewPanel.setLayoutConfig( controllerConfig );
    colCount = controllerConfig.columnCount();
    rowCount = controllerConfig.rowCount();
    colSpin.setValue( Integer.valueOf( colCount ) );
    rowSpin.setValue( Integer.valueOf( rowCount ) );
  }

  @Override
  protected IVedLayoutControllerConfig doGetDataRecord() {
    // return controllerConfig.config();
    VedTableLayoutControllerConfig cfg = previewPanel.layoutConfig();
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

  TableLayoutPreviewPanel previewPanel;

  int colCount = 1;
  int rowCount = 1;

  void init() {
    setLayout( new GridLayout( 1, false ) );
    Composite topComp = new Composite( this, SWT.NONE );
    topComp.setLayout( new GridLayout( 4, false ) );

    CLabel l = new CLabel( topComp, SWT.NONE );
    l.setText( "Столбцов: " );
    colSpin = new ValedIntegerSpinner( environ().tsContext() );
    colSpin.setLimits( 1, 1, 1, 1000 );
    colSpin.setValue( 2 );
    colSpin.createControl( topComp );
    colSpin.eventer().addListener( ( aSource, aEditFinished ) -> {
      if( aEditFinished ) {
        onColumnCountChanged( (Integer)aSource.getValue() );
      }
    } );

    l = new CLabel( topComp, SWT.NONE );
    l.setText( "Строк: " );
    rowSpin = new ValedIntegerSpinner( environ().tsContext() );
    rowSpin.setValue( 1 );
    rowSpin.setLimits( 1, 1, 1, 1000 );
    rowSpin.createControl( topComp );
    rowSpin.eventer().addListener( ( aSource, aEditFinished ) -> {
      if( aEditFinished ) {
        onRowCountChanged( (Integer)aSource.getValue() );
      }
    } );

    Composite previewComp = new Composite( this, SWT.NONE );
    previewComp.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 4, 1 ) );
    previewPanel = new TableLayoutPreviewPanel( previewComp, tsContext() );
  }

  void onColumnCountChanged( Integer aValue ) {
    int delta = aValue.intValue() - colCount;
    if( delta > 0 ) {
      controllerConfig.addColumns( delta );
    }
    else {
      controllerConfig.removeColumns( -delta );
    }
    colCount = aValue.intValue();
    previewPanel.setLayoutConfig( controllerConfig );
  }

  void onRowCountChanged( Integer aValue ) {
    int delta = aValue.intValue() - rowCount;
    if( delta > 0 ) {
      controllerConfig.addRows( delta );
    }
    else {
      controllerConfig.removeRows( -delta );
    }
    rowCount = aValue.intValue();
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
