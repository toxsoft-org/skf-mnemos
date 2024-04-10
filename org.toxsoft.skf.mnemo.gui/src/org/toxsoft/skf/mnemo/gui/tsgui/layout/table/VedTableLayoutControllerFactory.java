package org.toxsoft.skf.mnemo.gui.tsgui.layout.table;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.line.*;

/**
 * Фабрика создания контроллеров размещения типа {@link VedRowLayoutController}.
 *
 * @author vs
 */
public class VedTableLayoutControllerFactory
    implements IVedLayoutControllerFactory {

  /**
   * Конструктор.
   */
  public VedTableLayoutControllerFactory() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IStridableParameterized
  //

  @Override
  public String id() {
    return VedTableLayoutController.LAYOUT_KIND;
  }

  @Override
  public String nmName() {
    return "Таблица";
  }

  @Override
  public String description() {
    return "Контроллер размещения, который располагает подконтрольные элементы в ячейках таблицы";
  }

  @Override
  public IOptionSet params() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedViselsLayoutController create( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    return new VedRowLayoutController( aCfg, aVedScreen );
  }

  @Override
  public IVedLayoutControllerConfig editConfig( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    return TableLayoutDataPanel.edit( aCfg, aVedScreen );
  }

}
