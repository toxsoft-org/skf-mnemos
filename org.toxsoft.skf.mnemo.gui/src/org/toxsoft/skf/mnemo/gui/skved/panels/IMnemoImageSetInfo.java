package org.toxsoft.skf.mnemo.gui.skved.panels;

import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Описание набора изображений, используемых для отображения РВ-данного типа - "перечисление".
 * <p>
 * Все изображения в наборе имеют одинаковый размер, так как при выполнении все они будут отображаться по одним и тем же
 * координатам.
 *
 * @author vs
 */
public interface IMnemoImageSetInfo
    extends IStridableParameterized {

  /**
   * Возвращает список описаний изображений.
   *
   * @return IStridablesList&lt;IImageEntryInfo> - список описаний изображений
   */
  IStridablesList<IImageEntryInfo> imageInfoes();

  /**
   * Возвращает размер изображения.
   *
   * @return {@link ITsPoint} - размер изображения
   */
  ITsPoint imageSize();
}
