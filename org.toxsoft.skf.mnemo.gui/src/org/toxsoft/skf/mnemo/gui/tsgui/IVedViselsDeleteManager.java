package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;

/**
 * Обработчик удаления визуальных элементов.
 *
 * @author vs
 */
public interface IVedViselsDeleteManager
    extends IVedContextMenuCreator {

  /**
   * Удаляет соотвествующие визуальные элементы.<br>
   * Набор удаляемых элементов может зависить от текущего выделения, режима работы и т.д.
   */
  void deleteRelevant();

  /**
   * Добавляет процессор.<br>
   * Если такой процессор уже есть, то ничего не делает.
   *
   * @param aProcessor {@link IDeleteProcessor} - процессор обрабатывающий информацию при удалении
   */
  void addProcessor( IDeleteProcessor aProcessor );
}
