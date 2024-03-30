package org.toxsoft.skf.mnemo.gui.tsgui;

import org.eclipse.swt.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;

/**
 * Обработчик пользовательского ввода при операциях копирования и вставки.
 *
 * @author vs
 */
public class VedViselsDeleteHandler
    extends VedAbstractUserInputHandler {

  private final IVedViselsDeleteManager deleteManager;

  /**
   * Конструктор.
   *
   * @param aScreen {@link IVedScreen} - экран редактирования
   * @param aDeleteManager {@link IVedViselsDeleteManager} - менеджер удаления
   */
  public VedViselsDeleteHandler( IVedScreen aScreen, IVedViselsDeleteManager aDeleteManager ) {
    super( aScreen );
    deleteManager = aDeleteManager;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    if( (aState & SWT.MODIFIER_MASK) == 0 && aCode == SWT.DEL ) {
      deleteManager.deleteRelevant();
    }
    return false;
  }

}
