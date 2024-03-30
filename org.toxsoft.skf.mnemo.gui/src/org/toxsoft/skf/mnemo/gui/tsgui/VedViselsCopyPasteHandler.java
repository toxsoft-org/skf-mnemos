package org.toxsoft.skf.mnemo.gui.tsgui;

import org.eclipse.swt.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;

/**
 * Обработчик пользовательского ввода при операциях копирования и вставки.
 *
 * @author vs
 */
public class VedViselsCopyPasteHandler
    extends VedAbstractUserInputHandler {

  private final IVedViselsCopyPasteManager cpManager;

  /**
   * Конструктор.
   *
   * @param aScreen {@link IVedScreen} - экран редактирования
   * @param aCpManager {@link IVedViselsCopyPasteManager} - менеджер удаления
   */
  public VedViselsCopyPasteHandler( IVedScreen aScreen, IVedViselsCopyPasteManager aCpManager ) {
    super( aScreen );
    cpManager = aCpManager;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    if( (aState & SWT.MODIFIER_MASK) == SWT.CTRL ) {
      if( aCode == 99 ) { // key code for symbol "C"
        cpManager.copy();
      }
      if( aCode == 118 ) { // key code for symbol "V"
        cpManager.paste();
      }
    }
    return false;
  }

}
