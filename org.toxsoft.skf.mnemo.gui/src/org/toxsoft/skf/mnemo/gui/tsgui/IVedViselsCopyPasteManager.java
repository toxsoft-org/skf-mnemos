package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;

/**
 * Менеджер операций копирования/вставки визуальных элементов.
 *
 * @author vs
 */
public interface IVedViselsCopyPasteManager
    extends IVedContextMenuCreator, ITsGuiContextable {

  void copy();

  void paste();

}
