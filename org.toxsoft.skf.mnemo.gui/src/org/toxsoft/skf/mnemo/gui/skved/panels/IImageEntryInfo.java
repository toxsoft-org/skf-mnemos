package org.toxsoft.skf.mnemo.gui.skved.panels;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Описание вхождения в набор элемента типа - изображение.
 *
 * @author vs
 */
public interface IImageEntryInfo
    extends IStridableParameterized {

  /**
   * Возвращает описание способа создания изображения.
   *
   * @return {@link TsImageDescriptor} - описание способа создания изображения
   */
  TsImageDescriptor imageDescriptor();
}
