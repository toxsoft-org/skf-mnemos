package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Менеджер "горячих" клавиш.
 *
 * @author vs
 */
public class VedHotKeysManager
    extends VedAbstractUserInputHandler {

  TsSelectionChangeEventHelper<HotKeyInfo> eventHelper;

  private final IListEdit<HotKeyInfo> keys = new ElemArrayList<>();

  /**
   * Конструктор.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактора
   */
  public VedHotKeysManager( IVedScreen aVedScreen ) {
    super( aVedScreen );
    eventHelper = new TsSelectionChangeEventHelper<>( this );
  }

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    for( HotKeyInfo keyInfo : keys ) {
      if( (keyInfo.code() == 0 || keyInfo.code() == aCode) && //
      // (keyInfo.character() == 0 || keyInfo.character() == aChar) && //
          (aState == 0 || keyInfo.state() == aState) ) {
        eventHelper.fireTsSelectionEvent( keyInfo );
        return true;
      }
    }
    return false;
  }

  /**
   * Возвращает обработчик пользовательского ввода.
   *
   * @return {@link VedAbstractUserInputHandler} - обработчик пользовательского ввода
   */
  public VedAbstractUserInputHandler inputHandler() {
    return this;
  }

  /**
   * Добавляет описание "горячей" клавиши.<br>
   * Если описание уже существует, то ничего не делает
   *
   * @param aHotKeyInfo - {@link HotKeyInfo} описание "горячей" клавиши
   */
  public void addHotKey( HotKeyInfo aHotKeyInfo ) {
    if( !keys.hasElem( aHotKeyInfo ) ) {
      keys.add( aHotKeyInfo );
    }
  }

  /**
   * Добавляет слушателя нажатия "горячей" клавиши.
   *
   * @param aListener ITsSelectionChangeListener&lt;HotKeyInfo> - слушатель "грячей" клавиши
   */
  public void addSelectionChangedListener( ITsSelectionChangeListener<HotKeyInfo> aListener ) {
    eventHelper.addTsSelectionListener( aListener );
  }

  /**
   * Добавляет слушателя нажатия "горячей" клавиши.
   *
   * @param aListener ITsSelectionChangeListener&lt;HotKeyInfo> - слушатель "грячей" клавиши
   */
  public void removeSelectionChangedListener( ITsSelectionChangeListener<HotKeyInfo> aListener ) {
    eventHelper.removeTsSelectionListener( aListener );
  }
}
