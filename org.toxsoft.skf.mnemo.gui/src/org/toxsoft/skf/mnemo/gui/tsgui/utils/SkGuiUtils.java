package org.toxsoft.skf.mnemo.gui.tsgui.utils;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.skf.reports.gui.panels.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.km5.sgw.*;

/**
 * Sk GUI utility methods.
 *
 * @author vs
 */
public class SkGuiUtils {

  /**
   * Возвращает соединение из переданного контекста.
   *
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return {@link ISkConnection} - соединение с сервером
   */
  public static ISkConnection getConnection( ITsGuiContext aTsContext ) {
    ISkConnectionSupplier connSup = aTsContext.get( ISkConnectionSupplier.class );
    // проверяем в контекте наличие информации о соединении
    if( aTsContext.params().hasKey( PanelGwidSelector.OPDEF_CONN_ID_CHAIN.id() ) ) {
      IdChain idChain = PanelGwidSelector.OPDEF_CONN_ID_CHAIN.getValue( aTsContext.params() ).asValobj();
      return connSup.getConn( idChain );
    }
    return connSup.defConn();
  }

  /**
   * Возвращает API сервера из переданного контекста.
   *
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return {@link ISkCoreApi} - API сервера
   */
  public static ISkCoreApi getCoreApi( ITsGuiContext aTsContext ) {
    return getConnection( aTsContext ).coreApi();
  }

  /**
   * Вызывает диалог выбора класса.
   *
   * @param aSelectedClass {@link ISkClassInfo} - описание класса или <code>null</code>
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return {@link ISkClassInfo} - описание класса
   */
  public static ISkClassInfo selectClass( ISkClassInfo aSelectedClass, ITsGuiContext aTsContext ) {
    ISkConnection conn = getConnection( aTsContext );
    // тут получаем KM5 модель ISkClassInfo
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkClassInfo> model = m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );
    IM5LifecycleManager<ISkClassInfo> lm = model.getLifecycleManager( conn );

    // IM5CollectionPanel<ISkClassInfo> panel;
    // panel = model.panelCreator().createCollViewerPanel( aTsContext, lm.itemsProvider() );

    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( aTsContext, "Выбор класса", "Выберите соответствующий класс и нажмите \"ОК\"" );
    return M5GuiUtils.askSelectItem( dlgInfo, model, aSelectedClass, lm.itemsProvider(), null );
  }

  /**
   * No subclasses.
   */
  private SkGuiUtils() {
    // nop
  }

}
