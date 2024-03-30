package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;

/**
 * Менеджер контроллеров расположения визуальных элементов.
 *
 * @author vs
 */
public interface IVedViselsLayoutManager
    extends IVedContextMenuCreator, ITsGuiContextable {

  /**
   * Устанавливает контроллер расположения указанному визуальному элементу.
   *
   * @param aViselId String - ИД визуального элемента
   * @param aLayoutController {@link IVedViselsLayoutController} - контроллер расположения или <code>null</code>
   */
  void setLayoutController( String aViselId, IVedViselsLayoutController aLayoutController );

}
