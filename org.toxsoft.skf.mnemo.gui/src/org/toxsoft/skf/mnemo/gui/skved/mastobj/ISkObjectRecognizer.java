package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.*;

/**
 * "Распознаватель" объектов.
 * <p>
 * Например если по связи находятся несколько объектов, то "распознаватель" позволяет выбрать только один из них.
 *
 * @author vs
 */
public interface ISkObjectRecognizer {

  /**
   * Возвращает признак того, был ли требуемый объект распознан или нет.
   *
   * @param aObjSkid {@link Skid} - ИД sk-объекта
   * @param aCoreApi {@link ISkCoreApi} - API сервера
   * @return <b>true</b> - объект был распознан<br>
   *         <b>false</b> - это другой объект
   */
  boolean recognize( Skid aObjSkid, ISkCoreApi aCoreApi );
}
