package org.toxsoft.skf.mnemo.gui.tsgui.tools;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;

/**
 * Инструмент "указатель".
 *
 * @author vs
 */
public class PointerTool
    extends VedAbstractTool {

  /**
   * Конструктор.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактора
   */
  public PointerTool( IVedScreen aVedScreen ) {
    super( "toolPointer", "Pointer", "Free user input handler", aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractTool
  //

  @Override
  protected boolean doCanActivate() {
    return true;
  }

  @Override
  public boolean captureInput() {
    return false;
  }

  @Override
  public Cursor cursor() {
    return null;
  }

  @Override
  public VedAbstractUserInputHandler inputHandler() {
    return null;
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  public String iconId() {
    return null;
  }

}
