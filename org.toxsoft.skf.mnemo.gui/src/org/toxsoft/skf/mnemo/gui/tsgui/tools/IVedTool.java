package org.toxsoft.skf.mnemo.gui.tsgui.tools;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.snippets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.gui.*;

/**
 * Понятие "инструмент" визуального редактора.
 * <p>
 * Примерами инструментов можут служить:
 * <ul>
 * <li>Указатель</li>
 * <li>Ножницы</li>
 * <li>Ластик</li>
 * </ul>
 *
 * @author vs
 */
public interface IVedTool
    extends IVedSnippet, IStridable, IIconIdable {

  /**
   * Возвращает признак того, можно ли активировать данный "инструмент".
   *
   * @return <b>true</b> - можно активировать<br>
   *         <b>false</b> - нельзя активировать
   */
  boolean canActivate();

  /**
   * Признак того, что нужно захватить пользовательский ввод.
   *
   * @return <b>true</b> - весь пользовтельский ввод будет орабатываться только данным инструментом<br>
   *         <b>false</b> - после обработки пользовательского ввода он будет передан остальным обработчикам
   */
  boolean captureInput();

  /**
   * Возвращает обработчик пользовательского ввода.
   *
   * @return {@link VedAbstractUserInputHandler} - обработчик пользовательского ввода
   */
  VedAbstractUserInputHandler inputHandler();

  /**
   * Возвращает курсор специфичный для данного инструмента или <code>null</code>.
   *
   * @return {@link Cursor} - курсор специфичный для данного инструмента или <code>null</code>
   */
  Cursor cursor();

  /**
   * Оповещатель изменения активности "инструмента".
   *
   * @return {@link IGenericChangeEventer} - оповещатель изменения активности "инструмента"
   */
  IGenericChangeEventer eventer();
}
