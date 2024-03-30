package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Интерфейс менеджера отношений типа "мастер-подчиненный" между визуальными элементами.
 * <p>
 *
 * @author vs
 */
public interface IVedViselsMasterSlaveRelationsManager
    extends IVedContextMenuCreator {

  /**
   * Возвращает ИД визульного элемента, который является "мастером", указанного визуального элемента или
   * <code>null</code>.
   *
   * @param aViselId String - ИД видимого элемента
   * @return String - ИД "мастера" или <code>null</code>
   */
  String viselMasterId( String aViselId );

  /**
   * Возвращает ИД визульного элемента, который является "мастером", по конфигурации визуального элемента или
   * <code>null</code>.
   *
   * @param aCfg IVedItemCfg - ИД видимого элемента
   * @return String - ИД "мастера" или <code>null</code>
   */
  String viselMasterId( IVedItemCfg aCfg );

  /**
   * Возвращает список ИДов подчиненных элементов.
   *
   * @param aCfg IVedItemCfg - конфигурация визуального элемента
   * @return IStringList - список подчиненных ИДов
   */
  IStringList listViselSlaveIds( IVedItemCfg aCfg );

  /**
   * Возвращает список ИДов непосредственно подчиненных визуальных элементов.
   *
   * @param aMasterId String - ИД визуального элемента
   * @return {@link IStringList} - список ИДов подчиненных визуальных элементов м.б. {@link IStringList#EMPTY}
   */
  IStringList listSlaveViselIds( String aMasterId );

  /**
   * Возвращает список ИДов всех подчиненных визуальных элементов.
   *
   * @param aMasterId String - ИД визуального элемента
   * @return {@link IStringList} - список ИДов подчиненных визуальных элементов м.б. {@link IStringList#EMPTY}
   */
  IStringList listAllSlaveViselIds( String aMasterId );

  /**
   * Подчиняет визуальный элемент указанному "мастеру".
   *
   * @param aSubId String - ИД подчиненного визуального элемента
   * @param aMasterId String - ИД визульного элемента, который является "мастером"
   */
  void enslaveVisel( String aSubId, String aMasterId );

  /**
   * Удаляет подчиненый визуальный (освобождает) элемент из списка подчиненных матеру "мастеру".
   *
   * @param aSubId String - ИД подчиненного визуального элемента
   * @param aMasterId String - ИД визульного элемента, который является "мастером"
   */
  void freeVisel( String aSubId, String aMasterId );

  /**
   * Удаляет подчиненый визуальный (освобождает) элемент из списка подчиненных матеру "мастеру".
   *
   * @param aSlaveId String - ИД подчиненного визуального элемента
   */
  void freeVisel( String aSlaveId );

  /**
   * Задает новый идентификатор "мастера"
   *
   * @param aCfg {@link VedItemCfg} - конфигурация визуального элемента
   * @param aMasterId String - ИД "Мастера"
   */
  void setMasterId( VedItemCfg aCfg, String aMasterId );

  /**
   * Задает новый набор ИДов подчиненных элементов.
   *
   * @param aCfg {@link VedItemCfg} - конфигурация визуального элемента
   * @param aSlaveIds {@link IStringList} - новый набор ИДов подчиненных элементов
   */
  void setSlaveIds( VedItemCfg aCfg, IStringList aSlaveIds );

  /**
   * Возвращает признак того, является ли визуальный элемент "мастером".
   *
   * @param aViselId String - ИД визуального элемента
   * @return <b>true</b> - элемент является "мастером"<br>
   *         <b>false</b>- у элемента нет подчиненных элементов
   */
  default boolean isMaster( String aViselId ) {
    return listSlaveViselIds( aViselId ).size() > 0;
  }

}
