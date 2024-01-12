package org.toxsoft.skf.mnemo.gui.tools.imageset;

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
    extends IStridable {

  /**
   * Пустой набор изображений
   */
  IMnemoImageSetInfo EMPTY = new InternalEmptyImageSetInfo();

  /**
   * Возвращает список описаний изображений.
   *
   * @return IStridablesList&lt;IImageEntryInfo> - список описаний изображений
   */
  IStridablesList<IImageEntryInfo> imageInfoes();

  // /**
  // * Возвращает размер изображения.
  // *
  // * @return {@link ITsPoint} - размер изображения
  // */
  // ITsPoint imageSize();
}

class InternalEmptyImageSetInfo
    implements IMnemoImageSetInfo {

  @Override
  public String id() {
    return "emptyImageSet"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Empty"; //$NON-NLS-1$
  }

  @Override
  public String description() {
    return "Empty image set"; //$NON-NLS-1$
  }

  @Override
  public IStridablesList<IImageEntryInfo> imageInfoes() {
    return IStridablesList.EMPTY;
  }

  @Override
  public String toString() {
    return nmName();
  }
}
