package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Обработчик удаления визуальных элементов.
 *
 * @author vs
 */
public interface IVedViselsDeleteManager
    extends IVedContextMenuCreator, ITsGuiContextable {

  /**
   * Удаляет соотвествующие визуальные элементы.<br>
   * Набор удаляемых элементов может зависить от текущего выделения, режима работы и т.д.
   */
  void deleteRelevant();

  /**
   * Удаляет визуальный элемент с указанным идентификатором.<br>
   * Если элемент отсутствует, то ничего не делает.
   *
   * @param aViselId String - ИД визуального элемента
   */
  void deleteVisel( String aViselId );

  /**
   * Удаляет визуальные элементы с идентификаторами из переданного списка.
   *
   * @param aViselIds {@link IStringList} - список ИДов визуальных элементов
   */
  void deleteVisels( IStringList aViselIds );

  /**
   * Удаляет акторы с идентификаторами из переданного списка.
   *
   * @param aActorIds {@link IStringList} - список ИДов акторов
   */
  void deleteActors( IStringList aActorIds );

  /**
   * Добавляет процессор.<br>
   * Если такой процессор уже есть, то ничего не делает.
   *
   * @param aProcessor {@link IDeleteProcessor} - процессор обрабатывающий информацию при удалении
   */
  void addProcessor( IDeleteProcessor aProcessor );
}
