package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Управляет созданием и расформированием групп визуальных элементов.
 * <p>
 *
 * @author vs
 */
public interface IVedViselGroupsManager {

  /**
   * Возвращает список идентфикаторов групп.
   *
   * @return {@link IStringList} - список идентфикаторов групп
   */
  IStringList groupIds();

  /**
   * Возвращает список идентификаторов сгруппированных визуальных элементов.
   *
   * @param aGroupId String - ИД группы
   * @return {@link IStringList} -список идентификаторов сгруппированных визуальных элементов
   */
  IStringList listViselIds( String aGroupId );

  /**
   * Возращает список идентификаторов групп, в которые входит визуальный элемент.
   *
   * @param aViselId String - ИД визуального элемента
   * @return IStringList - список идентификаторов групп, в которые входит визуальный элемент
   */
  IStringList viselGroupIds( String aViselId );

  /**
   * Группирует визуальные элементы.
   *
   * @param aViselIds IStringList - идентификаторы группируемых визуальных элементов
   */
  void groupVisels( IStringList aViselIds );

  /**
   * Расформировывает группу визуальных элементов.
   *
   * @param aGroupId String - идентификатор группы
   */
  void ungroupVisels( String aGroupId );

  /**
   * Добавляет слушателя изменения состава группы.<br>
   * Если такой слушатель уже существует, то ничего не делает.
   *
   * @param aListener IVedGroupChangeListener - слушатель изменения состава группы
   */
  void addGroupChangeListener( IVedGroupChangeListener aListener );

  /**
   * Удалаяет слушателя.<br>
   * Если слушатель отсутствовал, то гичего не делает.
   *
   * @param aListener IVedGroupChangeListener - слушатель изменения состава группы
   */
  void removeGroupChangeListener( IVedGroupChangeListener aListener );
}
