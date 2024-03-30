package org.toxsoft.skf.mnemo.gui.tsgui.layout.column;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

/**
 * Контроллер размещения, который располагает подконтрольные элементы в один столбец.
 * <p>
 *
 * @author vs
 */
public class VedColumnLayoutController
    implements IVedViselsLayoutController {

  /**
   * ИД типа контроллера размещения
   */
  public static final String LAYOUT_KIND = "ved.layout.column"; //$NON-NLS-1$

  private final IVedScreen vedScreen;

  D2GridMargins margins = new D2GridMargins( 4 );

  int cellsQtty = 1;

  /**
   * Конструктор.
   *
   * @param aCfg {@link IVedLayoutControllerConfig} - конфигурация контроллера
   * @param aVedScreen {@link IVedScreen} - экран редактора
   */
  public VedColumnLayoutController( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    if( aCfg != null ) {
      TsIllegalArgumentRtException.checkFalse( aCfg.kindId().equals( LAYOUT_KIND ) );
    }
    vedScreen = aVedScreen;
  }

  // ------------------------------------------------------------------------------------
  // IVedViselsLayoutController
  //

  @Override
  public String kindId() {
    return LAYOUT_KIND;
  }

  @Override
  public void doLayout( String aMasterId, IStringList aSlaveIds ) {
    // TODO Auto-generated method stub

  }

  @Override
  public IVedLayoutControllerConfig getConfiguration() {
    // TODO Auto-generated method stub
    return null;
  }

}
