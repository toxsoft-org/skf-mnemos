package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.swt.widgets.*;

/**
 * @author vs
 */
public interface ISkoRecognizerCfgPanel {

  /**
   * Возвращает конфигурацию "распознавателя".
   *
   * @return {@link ISkoRecognizerCfg} - конфигурация "распознавателя"
   */
  ISkoRecognizerCfg config();

  /**
   * Задает конфигурацию "распознавателя".
   *
   * @param aCfg {@link ISkoRecognizerCfg} - конфигурация "распознавателя"
   */
  void setConfig( ISkoRecognizerCfg aCfg );

  /**
   * Возвращает "контроль" панели.
   *
   * @return {@link Control} - "контроль" панели
   */
  Control getControl();
}
