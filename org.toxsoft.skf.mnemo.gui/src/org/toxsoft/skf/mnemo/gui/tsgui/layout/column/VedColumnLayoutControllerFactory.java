package org.toxsoft.skf.mnemo.gui.tsgui.layout.column;

import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

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
    return VedColumnLayoutController.LAYOUT_KIND;
  }

  @Override
  public String nmName() {
    return "В столбик";
  }

  @Override
  public String description() {
    return "Контроллер размещения, который располагает подконтрольные элементы в одну колонку";
  }

  @Override
  public IOptionSet params() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedViselsLayoutController create( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    return new VedColumnLayoutController( aCfg, aVedScreen );
  }

  @Override
  public IVedLayoutControllerConfig editConfig( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    TsDialogUtils.info( aVedScreen.getShell(), "Column layout diaog" );
    return null;
  }

}
