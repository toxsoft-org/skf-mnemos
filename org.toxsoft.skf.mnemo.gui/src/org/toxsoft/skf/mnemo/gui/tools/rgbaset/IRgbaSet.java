package org.toxsoft.skf.mnemo.gui.tools.rgbaset;

import java.io.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Набор цветов с доступом по индексу.
 *
 * @author vs
 */
public interface IRgbaSet
    extends IStridable, Serializable {

  /**
   * Возвращает количество цветов в наборе.
   *
   * @return int - количество цветов в наборе
   */
  int size();

  /**
   * Возвращает цвет в виде {@link RGBA} по индексу.
   *
   * @param aIndex int - индекс цвета в наборе
   * @return {@link RGBA} - цвет
   */
  RGBA getRgba( int aIndex );

}
