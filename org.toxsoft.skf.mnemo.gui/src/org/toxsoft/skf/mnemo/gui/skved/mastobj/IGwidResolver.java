package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;

/**
 * "Разрешитель" Gwid'a.
 *
 * @author vs
 */
public interface IGwidResolver {

  /**
   * Возвращает конкретный Gwid для указанного мастер-объекта.
   *
   * @param aMasterSkid {@link Skid} - ИД мастре-объекта
   * @return {@link Gwid} - конкретный Gwid Sk-сущности
   */
  Gwid resolve( Skid aMasterSkid );
}
