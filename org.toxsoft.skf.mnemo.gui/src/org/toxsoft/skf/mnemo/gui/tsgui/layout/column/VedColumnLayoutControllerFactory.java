package org.toxsoft.skf.mnemo.gui.tsgui.layout.column;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs.*;

/**
 * Фабрика создания контроллеров размещения типа {@link VedColumnLayoutController}.
 *
 * @author vs
 */
public class VedColumnLayoutControllerFactory
    implements IVedLayoutControllerFactory {

  /**
   * Конструктор.
   */
  public VedColumnLayoutControllerFactory() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IStridableParameterized
  //

  @Override
  public String id() {
    return VedColumnLayoutControllerConfig.LAYOUT_KIND;
  }

  @Override
  public String nmName() {
    return "По столбцам";
  }

  @Override
  public String description() {
    return "Контроллер размещения, который располагает подконтрольные элементы по столбцам";
  }

  @Override
  public IOptionSet params() {
    return null;
  }

  @Override
  public IVedViselsLayoutController create( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    return new VedColumnLayoutController( aCfg, aVedScreen );
  }

  @Override
  public IVedLayoutControllerConfig editConfig( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    return ColumnLayoutDataPanel.edit( aCfg, aVedScreen );
  }

}
