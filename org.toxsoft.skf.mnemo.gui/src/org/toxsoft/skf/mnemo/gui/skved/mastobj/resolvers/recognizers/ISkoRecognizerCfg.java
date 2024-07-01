package org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.*;

/**
 * Конфигурационная информация "распознавателя" sk-объектов.
 *
 * @author vs
 */
public sealed interface ISkoRecognizerCfg
    extends IStridable permits SkoRecognizerCfg {

  /**
   * Тип "распознавателя".
   *
   * @return {@link ESkoRecognizerKind} - тип "распознавателя"
   */
  ESkoRecognizerKind kind();

  /**
   * ИД фабрики создания "распознавателя".
   *
   * @return String - ИД фабрики создания "распознавателя"
   */
  String factoryId();

  /**
   * Значения свойств "распознавателя".
   *
   * @return {@link IOptionSet} - значения свойств "распознавателя"
   */
  IOptionSet propValues();

}
