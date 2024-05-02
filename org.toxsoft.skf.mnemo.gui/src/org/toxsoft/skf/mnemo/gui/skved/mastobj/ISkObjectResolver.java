package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.gw.skid.*;

/**
 * "Разрешитель" одного объекта в другой.
 * <p>
 * Типичный случай, когда от переданного объекта, по указанной связи, необходимо взять другой объект.
 *
 * @author vs
 */
public interface ISkObjectResolver {

  /**
   * "Разрешает" переданный объект в другой.
   *
   * @param aSkid Skid - объект, который нужно разрешить.
   * @return Skid - ИД "разрешенного" объекта
   */
  Skid resolve( Skid aSkid );
}
