package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;

/**
 * Manages VISELs selection (including multi-selections) and group selection (including group/ungroup operations) on the
 * screen.
 *
 * @author vs
 */
public interface IVedViselSelectionGroupManager
    extends IVedViselSelectionManager {

  /**
   * Возвращает менеджер групп.
   *
   * @return {@link IVedViselGroupsManager} - менеджер групп
   */
  IVedViselGroupsManager groupsManager();
}
