package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

public class SelectMasterPathPanel
    extends AbstractTsDialogPanel<CellAlignment, ITsGuiContext> {

  protected SelectMasterPathPanel( Composite aParent, TsDialog<CellAlignment, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected SelectMasterPathPanel( Composite aParent, CellAlignment aData, ITsGuiContext aEnviron, int aFlags ) {
    super( aParent, aEnviron, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( CellAlignment aData ) {
    if( aData != null ) {
    }
    else {
    }
  }

  @Override
  protected CellAlignment doGetDataRecord() {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MasterPathViewer viewer;

  void init() {
    setLayout( new BorderLayout() );
    viewer = new MasterPathViewer( this, environ() );
    viewer.setLayoutData( BorderLayout.CENTER );
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
    IDialogPanelCreator<CellAlignment, ITsGuiContext> creator = SelectMasterPathPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "DLG_T_SELECT_MASTER_PATH", "STR_MSG_SELECT_MASTER_PATH" );
    TsDialog<CellAlignment, ITsGuiContext> d = new TsDialog<>( dlgInfo, aData, aTsContext, creator );
    return d.execData();
  }

}
