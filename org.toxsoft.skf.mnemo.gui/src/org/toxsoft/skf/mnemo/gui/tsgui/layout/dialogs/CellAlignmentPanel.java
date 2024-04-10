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
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

/**
 * Панель редактирования параметров выравнивания содержимого ячейки контроллеров размещения.
 *
 * @author vs
 */
public class CellAlignmentPanel
    extends AbstractTsDialogPanel<CellAlignment, ITsGuiContext> {

  protected CellAlignmentPanel( Composite aParent, TsDialog<CellAlignment, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected CellAlignmentPanel( Composite aParent, CellAlignment aData, ITsGuiContext aEnviron, int aFlags ) {
    super( aParent, aEnviron, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( CellAlignment aData ) {
    if( aData != null ) {
      horAlignCombo.setValue( aData.horAlignment() );
      verAlignCombo.setValue( aData.verAlignment() );
      horFillCheck.setValue( Boolean.valueOf( aData.shouldFillWidth() ) );
      vertFillCheck.setValue( Boolean.valueOf( aData.shouldFillHeight() ) );
    }
    else {
      horAlignCombo.setValue( EHorAlignment.LEFT );
      verAlignCombo.setValue( EVerAlignment.TOP );
      horFillCheck.setValue( Boolean.valueOf( false ) );
      vertFillCheck.setValue( Boolean.valueOf( false ) );
    }
  }

  @Override
  protected CellAlignment doGetDataRecord() {
    EHorAlignment horAl = horAlignCombo.getValue();
    EVerAlignment vertAl = verAlignCombo.getValue();
    boolean fillWidh = horFillCheck.getValue().booleanValue();
    boolean fillHeight = vertFillCheck.getValue().booleanValue();
    return new CellAlignment( horAl, fillWidh, vertAl, fillHeight );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  ValedEnumCombo<EHorAlignment> horAlignCombo;
  ValedBooleanCheck             horFillCheck;
  ValedEnumCombo<EVerAlignment> verAlignCombo;
  ValedBooleanCheck             vertFillCheck;

  void init() {
    int columnCount = 2;
    int horSpan = 2;
    if( (panelFlags() & SWT.HORIZONTAL) != 0 ) {
      columnCount = 3;
      horSpan = 1;
    }
    setLayout( new GridLayout( columnCount, false ) );
    CLabel l;

    l = new CLabel( this, SWT.NONE );
    l.setText( "По горизонтали: " );
    horAlignCombo = new ValedEnumCombo<>( environ(), EHorAlignment.class, IStridable::nmName );
    horAlignCombo.createControl( this );

    horFillCheck = new ValedBooleanCheck( environ() );
    horFillCheck.params().setStr( ValedBooleanCheck.OPID_TEXT, "Заполнять ширину" );
    horFillCheck.createControl( this );
    horFillCheck.getControl().setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, horSpan, 1 ) );

    l = new CLabel( this, SWT.NONE );
    l.setText( "По вертикали: " );
    verAlignCombo = new ValedEnumCombo<>( environ(), EVerAlignment.class, IStridable::nmName );
    verAlignCombo.createControl( this );

    vertFillCheck = new ValedBooleanCheck( environ() );
    vertFillCheck.params().setStr( ValedBooleanCheck.OPID_TEXT, "Заполнять высоту" );
    vertFillCheck.createControl( this );
    vertFillCheck.getControl().setLayoutData( new GridData( SWT.LEFT, SWT.CENTER, false, false, horSpan, 1 ) );
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров выравнивания содержимого ячейки.
   *
   * @param aData CellAlignment - параметры выравнивания содержимого ячейки
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final CellAlignment edit( CellAlignment aData, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    IDialogPanelCreator<CellAlignment, ITsGuiContext> creator = CellAlignmentPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "DLG_T_CANVAS_CFG", "STR_MSG_CANVAS_CFG" );
    TsDialog<CellAlignment, ITsGuiContext> d = new TsDialog<>( dlgInfo, aData, aTsContext, creator );
    return d.execData();
  }

}
