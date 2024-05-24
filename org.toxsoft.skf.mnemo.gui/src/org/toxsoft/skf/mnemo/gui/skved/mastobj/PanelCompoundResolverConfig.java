package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;
import org.toxsoft.uskat.core.utils.ugwi.*;

/**
 * Панель создания/редактирования конфигурации составного "разрешителя" {@link Ugwi}.
 * <p>
 *
 * @author vs
 */
public class PanelCompoundResolverConfig
    extends AbstractTsDialogPanel<ICompoundResolverConfig, ITsGuiContext> {

  protected PanelCompoundResolverConfig( Composite aParent,
      TsDialog<ICompoundResolverConfig, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected PanelCompoundResolverConfig( Composite aParent, ICompoundResolverConfig aData, ITsGuiContext aEnviron,
      int aFlags ) {
    super( aParent, aEnviron, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ICompoundResolverConfig aData ) {
    if( aData != null ) {
    }
    else {
    }
  }

  @Override
  protected ICompoundResolverConfig doGetDataRecord() {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MasterPathViewer viewer;

  void init() {
    setLayout( new BorderLayout() );

    String moClsId = "SkObject"; //$NON-NLS-1$
    if( dataRecordInput() != null ) {
      SimpleResolverCfg cfg = dataRecordInput().cfgs().first();
      if( DirectGwidResolver.hasGwid( cfg ) ) {
        Gwid gwid = DirectGwidResolver.gwid( cfg );
        moClsId = gwid.classId();
      }
    }

    viewer = new MasterPathViewer( this, moClsId, environ() );
    viewer.setLayoutData( BorderLayout.CENTER );
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров выравнивания содержимого ячейки.
   *
   * @param aData ICompoundResolverConfig - параметры выравнивания содержимого ячейки
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final ICompoundResolverConfig edit( ICompoundResolverConfig aData, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    IDialogPanelCreator<ICompoundResolverConfig, ITsGuiContext> creator = PanelCompoundResolverConfig::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "DLG_T_SELECT_MASTER_PATH", "STR_MSG_SELECT_MASTER_PATH" );
    TsDialog<ICompoundResolverConfig, ITsGuiContext> d = new TsDialog<>( dlgInfo, aData, aTsContext, creator );
    return d.execData();
  }

}
