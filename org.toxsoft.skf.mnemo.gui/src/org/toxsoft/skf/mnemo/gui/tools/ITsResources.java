package org.toxsoft.skf.mnemo.gui.tools;

import org.toxsoft.skf.mnemo.gui.tools.imageset.*;
import org.toxsoft.skf.mnemo.gui.tools.rgbaset.*;

/**
 * Локализуемые реурсы.
 *
 * @author vs
 */
public interface ITsResources {

  /**
   * {@link PanelRgbaSetEditor}
   */
  String DLG_T_RGBA_SET    = "Набор цветов";
  String STR_MSG_RGBA_SET  = "Отредактируйте набор цветов и нажмите ОК";
  String STR_L_IDENTIFIER  = "Идентификатор: ";
  String STR_L_NAME        = "Наименование: ";
  String STR_L_DESCRIPTION = "Описание: ";

  String STR_B_ADD    = "Добавить...";
  String STR_B_REMOVE = "Удалить";
  String STR_B_UP     = "Вверх";
  String STR_B_DOWN   = "Вниз";

  String STR_ERR_WRONG_SET_ID = "Недопустимый идентификатор набора";

  /**
   * {@link PanelImageSetEditor}
   */
  String STR_C_IMGAGE         = "Изображение";
  String DLG_T_IMG_SET_INFO   = "Набор изображений";
  String STR_MSG_IMG_SET_INFO = "Отредактируйте необходимые параметры и нажмите ОК";

  /**
   * {@link PanelImageSetEntryEditor}
   */
  String DLG_T_IMG_SET_ENTRY_INFO   = "Элемент набора изображений";
  String STR_MSG_IMG_SET_ENTRY_INFO = "Выберите и нажмите ОК";

}
