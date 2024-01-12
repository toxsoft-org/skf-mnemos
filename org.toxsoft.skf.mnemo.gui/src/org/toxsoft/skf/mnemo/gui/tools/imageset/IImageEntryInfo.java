package org.toxsoft.skf.mnemo.gui.tools.imageset;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Описание вхождения в набор элемента типа - изображение.
 *
 * @author vs
 */
public interface IImageEntryInfo
    extends IStridable {

  /**
   * Возвращает описание способа создания изображения.
   *
   * @return {@link TsImageDescriptor} - описание способа создания изображения
   */
  TsImageDescriptor imageDescriptor();
}
