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
import org.toxsoft.skf.mnemo.gui.tsgui.layout.column.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

/**
 * Панель редактирования конфигурации контроллера размещения "по столбцам".
 *
 * @author vs
 */
public class ColumnLayoutDataPanel
    extends AbstractTsDialogPanel<IVedLayoutControllerConfig, IVedScreen> {

  VedColumnLayoutControllerConfig controllerConfig;

  protected ColumnLayoutDataPanel( Composite aParent, TsDialog<IVedLayoutControllerConfig, IVedScreen> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected ColumnLayoutDataPanel( Composite aParent, ITsGuiContext aContext, IVedLayoutControllerConfig aData,
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
      controllerConfig = new VedColumnLayoutControllerConfig( aData );
    }
    else {
      controllerConfig = new VedColumnLayoutControllerConfig();
    }
    marginsPanel.setDataRecord( controllerConfig.margins() );
    previewPanel.setLayoutConfig( controllerConfig );
    colCount = controllerConfig.columnCount();
    colSpin.setValue( Integer.valueOf( colCount ) );
    hGapSpin.setValue( Integer.valueOf( (int)controllerConfig.horizontalGap() ) );
    vGapSpin.setValue( Integer.valueOf( (int)controllerConfig.verticalGap() ) );
  }

  @Override
  protected IVedLayoutControllerConfig doGetDataRecord() {
    controllerConfig.setMargins( marginsPanel.getDataRecord() );
    return controllerConfig.config();
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
  ValedIntegerSpinner hGapSpin;
  ValedIntegerSpinner vGapSpin;

  ColumnLayoutPreviewPanel previewPanel;

  int colCount = 1;

  void init() {
    setLayout( new GridLayout( 1, false ) );

    Composite headerComp = new Composite( this, SWT.NONE );
    headerComp.setLayout( new GridLayout( 2, false ) );

    Composite topComp = new Composite( headerComp, SWT.NONE );
    topComp.setLayout( new GridLayout( 2, false ) );
    topComp.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    CLabel l = new CLabel( topComp, SWT.NONE );
    l.setText( "Количество столбцов: " );
    colSpin = new ValedIntegerSpinner( environ().tsContext() );
    colSpin.setLimits( 1, 1, 1, 1000 );
    colSpin.setValue( 2 );
    colSpin.createControl( topComp );
    colSpin.eventer().addListener( ( aSource, aEditFinished ) -> {
      if( aEditFinished ) {
        int newVal = ((Integer)aSource.getValue()).intValue();
        if( newVal != colCount ) {
          onColumnCountChanged( (Integer)aSource.getValue() );
          layout();
        }
      }
    } );

    l = new CLabel( topComp, SWT.NONE );
    l.setText( "Зазор по горизонтали: " );
    hGapSpin = new ValedIntegerSpinner( environ().tsContext() );
    hGapSpin.setLimits( 1, 1, 0, 1000 );
    hGapSpin.setValue( 5 );
    hGapSpin.createControl( topComp );
    hGapSpin.eventer().addListener( ( aSource, aEditFinished ) -> onHorGapChanged() );

    l = new CLabel( topComp, SWT.NONE );
    l.setText( "Зазор по вертикали: " );
    vGapSpin = new ValedIntegerSpinner( environ().tsContext() );
    vGapSpin.setLimits( 1, 1, 0, 1000 );
    vGapSpin.setValue( 5 );
    vGapSpin.createControl( topComp );
    vGapSpin.eventer().addListener( ( aSource, aEditFinished ) -> onVerGapChanged() );

    Composite marginsComp = new Composite( headerComp, SWT.NONE );
    marginsComp.setLayout( new FillLayout() );
    marginsComp.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    Group marginsGroup = new Group( marginsComp, SWT.NONE );
    marginsGroup.setLayout( new FillLayout() );
    marginsGroup.setText( "Поля" );
    marginsPanel = new D2MarginsPanel( marginsGroup, new D2Margins(), environ().tsContext(), SWT.HORIZONTAL );

    Composite previewComp = new Composite( this, SWT.NONE );
    previewComp.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
    previewPanel = new ColumnLayoutPreviewPanel( previewComp, tsContext() );
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

  void onHorGapChanged() {
    controllerConfig.setHorizontalGap( hGapSpin.getValue().doubleValue() );
    previewPanel.setLayoutConfig( controllerConfig );
  }

  void onVerGapChanged() {
    controllerConfig.setVerticalGap( vGapSpin.getValue().doubleValue() );
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
    IDialogPanelCreator<IVedLayoutControllerConfig, IVedScreen> creator = ColumnLayoutDataPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aVedScreen.tsContext(), "DLG_T_CANVAS_CFG", "STR_MSG_CANVAS_CFG" );
    TsDialog<IVedLayoutControllerConfig, IVedScreen> d = new TsDialog<>( dlgInfo, aCfg, aVedScreen, creator );
    return d.execData();
  }

}
