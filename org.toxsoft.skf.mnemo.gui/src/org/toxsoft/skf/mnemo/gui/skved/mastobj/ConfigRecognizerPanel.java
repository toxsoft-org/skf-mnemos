package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class ConfigRecognizerPanel
    extends AbstractTsDialogPanel<ISkObjectRecognizer, ITsGuiContext> {

  protected ConfigRecognizerPanel( Composite aParent, TsDialog<ISkObjectRecognizer, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected ConfigRecognizerPanel( Composite aParent, ISkObjectRecognizer aData, ITsGuiContext aEnviron, int aFlags ) {
    super( aParent, aEnviron, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ISkObjectRecognizer aData ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected ISkObjectRecognizer doGetDataRecord() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private ValedEnumCombo<ESkObjRecognizerKind> kindCombo;

  void init() {
    setLayout( new GridLayout( 2, false ) );

    CLabel l;

    l = new CLabel( this, SWT.NONE );
    l.setText( "Тип распознавателя: " );
    kindCombo = new ValedEnumCombo<>( environ(), ESkObjRecognizerKind.class, IStridable::nmName );
    kindCombo.createControl( this );
    kindCombo.getControl().addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        // TODO Auto-generated method stub
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров выравнивания содержимого ячейки.
   *
   * @param aData ISkObjectRecognizer - параметры выравнивания содержимого ячейки
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return {@link ISkObjectRecognizer} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final ISkObjectRecognizer edit( ISkObjectRecognizer aData, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    IDialogPanelCreator<ISkObjectRecognizer, ITsGuiContext> creator = ConfigRecognizerPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "\"Распознаватель\" объектов", "STR_MSG_CANVAS_CFG" );
    TsDialog<ISkObjectRecognizer, ITsGuiContext> d = new TsDialog<>( dlgInfo, aData, aTsContext, creator );
    return d.execData();
  }

}
