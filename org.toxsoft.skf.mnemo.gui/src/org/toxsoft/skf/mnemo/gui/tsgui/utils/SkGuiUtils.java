package org.toxsoft.skf.mnemo.gui.tsgui.utils;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.skf.reports.gui.panels.*;
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

  public static ISkClassInfo selectClass( ISkClassInfo aSelectedClass, ITsGuiContext aTsContext ) {
    ISkConnection conn;
    ISkConnectionSupplier connSup = aTsContext.get( ISkConnectionSupplier.class );
    // проверяем в контекте наличие информации о соединении
    if( aTsContext.params().hasKey( PanelGwidSelector.OPDEF_CONN_ID_CHAIN.id() ) ) {
      IdChain idChain = PanelGwidSelector.OPDEF_CONN_ID_CHAIN.getValue( aTsContext.params() ).asValobj();
      conn = connSup.getConn( idChain );
    }
    else {
      conn = connSup.defConn();
    }
    // тут получаем KM5 модель ISkClassInfo
    IM5Domain m5 = conn.scope().get( IM5Domain.class );
    IM5Model<ISkClassInfo> model = m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );
    IM5LifecycleManager<ISkClassInfo> lm = model.getLifecycleManager( conn );

    IM5CollectionPanel<ISkClassInfo> panel =
        model.panelCreator().createCollViewerPanel( aTsContext, lm.itemsProvider() );

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
