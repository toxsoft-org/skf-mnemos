package org.toxsoft.skf.mnemo.gui.tsgui.layout.line;

import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

/**
 * Фабрика создания контроллеров размещения типа {@link VedRowLayoutController}.
 *
 * @author vs
 */
public class VedRowLayoutControllerFactory
    implements IVedLayoutControllerFactory {

  /**
   * Конструктор.
   */
  public VedRowLayoutControllerFactory() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IStridableParameterized
  //

  @Override
  public String id() {
    return VedRowLayoutController.LAYOUT_KIND;
  }

  @Override
  public String nmName() {
    return "В строку";
  }

  @Override
  public String description() {
    return "Контроллер размещения, который располагает подконтрольные элементы в одну линию";
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
    TsDialogUtils.info( aVedScreen.getShell(), "Row layout diaog" );
    return null;
  }

}
