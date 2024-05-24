package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;

/**
 * Узел пути к мастер-объекту при его выборе в {@link MasterPathViewer}.
 *
 * @author vs
 */
public interface IMasterPathNode
    extends IStridable {

  /**
   * Родительский узел.
   *
   * @return {@link IMasterPathNode} - родительский узел
   */
  IMasterPathNode parent();

  /**
   * Возвращает список дочерних узлов.
   *
   * @return IList&lt;? extends IMasterPathNode>
   */
  IList<? extends IMasterPathNode> children();

  /**
   * Возвращает кофигурацию "разрешителя" для данного узла.
   *
   * @return {@link ICompoundResolverConfig} - кофигурация "разрешителя" для данного узла
   */
  ICompoundResolverConfig resolverConfig();

}
