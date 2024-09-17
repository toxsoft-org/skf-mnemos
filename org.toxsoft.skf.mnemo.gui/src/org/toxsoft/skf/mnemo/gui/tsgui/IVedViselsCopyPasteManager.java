package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;

/**
 * Менеджер операций копирования/вставки визуальных элементов.
 * <p>
 * Помимо визуальных элементов конкретная реализация менеджера может копировать сопутствующую информацию, например -
 * акторы.
 *
 * @author vs
 */
public interface IVedViselsCopyPasteManager
    extends IVedContextMenuCreator, ITsGuiContextable {

  /**
   * Копирует соответствующую информацию в буфер обмена.<br>
   * Состав копируемой информации зависит от текущего состояния редактора.
   */
  void copy();

  /**
   * Вставляет содержимое буфера в редактируемый файл.
   */
  void paste();

}
