package org.toxsoft.skf.mnemo.gui.tsgui;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;

/**
 * Информация о "горячей" клавише.
 *
 * @author vs
 */
public class HotKeyInfo {

  private final int  code;
  private final char character;
  private final int  state;

  /**
   * Конструтор.
   *
   * @param aCode int - key code (as specified in {@link SWT})
   * @param aChar char - corresponding character symbol as in {@link KeyEvent#character}
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   */
  public HotKeyInfo( int aCode, char aChar, int aState ) {
    code = aCode;
    character = aChar;
    state = aState;
  }

  /**
   * Return key code (as specified in {@link SWT})
   *
   * @return int - key code (as specified in {@link SWT})
   */
  public int code() {
    return code;
  }

  /**
   * Return corresponding character symbol as in {@link KeyEvent#character}
   *
   * @return int - corresponding character symbol as in {@link KeyEvent#character}
   */
  public char character() {
    return character;
  }

  /**
   * Return the state of the keyboard modifier keys as in {@link KeyEvent#stateMask}
   *
   * @return int - the state of the keyboard modifier keys as in {@link KeyEvent#stateMask}
   */
  public int state() {
    return state;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public boolean equals( Object aObj ) {
    if( aObj instanceof HotKeyInfo that ) {
      return (code == that.code) && (character == that.character) && (state == that.state);
    }
    return false;
  }

}
