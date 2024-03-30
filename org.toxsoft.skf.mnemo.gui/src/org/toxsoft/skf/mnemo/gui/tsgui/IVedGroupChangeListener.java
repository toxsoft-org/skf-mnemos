package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Слушатель событий изменения состава группы визуальных элементов.
 *
 * @author vs
 */
public interface IVedGroupChangeListener {

  /**
   * Вызывается после того как визуальные элементы были сформированы в новую группу.
   *
   * @param aGroupId String - ИД группы
   * @param aViselIds {@link IStringList} - список ИДов сгруппированных элементов
   */
  void onGroup( String aGroupId, IStringList aViselIds );

  /**
   * Вызывается после того как группа визуальных элементов была разгруппирована.
   *
   * @param aGroupId String - ИД группы
   * @param aViselIds {@link IStringList} - список ИДов разгруппированных элементов
   */
  void onUngroup( String aGroupId, IStringList aViselIds );
}
