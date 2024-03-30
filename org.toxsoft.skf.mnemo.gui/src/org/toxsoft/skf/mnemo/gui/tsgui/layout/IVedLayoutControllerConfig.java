package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Конфигурация контроллера размещения.
 *
 * @author vs
 */
public sealed interface IVedLayoutControllerConfig
    extends IStridableParameterized permits VedLayoutControllerConfig {

  /**
   * Идентификатор типа контроллера размещения.
   *
   * @return String - идентификатор типа контроллера размещения
   */
  String kindId();

  /**
   * Returns the values of the properties used to build the VedLayoutController instance.
   *
   * @return {@link IOptionSet} - property values for VedLayoutController instance creation
   */
  IOptionSet propValues();

}
