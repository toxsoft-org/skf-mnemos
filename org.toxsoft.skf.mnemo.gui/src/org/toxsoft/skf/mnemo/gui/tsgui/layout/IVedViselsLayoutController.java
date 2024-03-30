package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Интерфейс классов способных размещать подконтрольные визуальные элементы в соответствии со своими правилами.
 * <p>
 * Под размещением понимается не только положение элементов, но и возможно их размеры.
 *
 * @author vs
 */
public interface IVedViselsLayoutController {

  /**
   * Идентификатор типа, например: border, grid, fill, row и т.д
   *
   * @return String - идентификатор типа
   */
  String kindId();

  /**
   * Возвращает текущую конфигурацию контроллера.
   *
   * @return {@link IVedLayoutControllerConfig} - текущая конфигурация контроллера
   */
  IVedLayoutControllerConfig getConfiguration();

  /**
   * Осуществляет размещение подконтрольных визуальных элементов в соответствии со своими правилами.
   *
   * @param aMasterId String - ИД визуального элемента на котором осуществляется размещение
   * @param aSlaveIds {@link IStringList} - список ИДов размещаемых элементов
   */
  void doLayout( String aMasterId, IStringList aSlaveIds );

  // /**
  // * Возвращает ИД визуального элемента, на котором осуществляется размещение подконтрольных элементов.
  // *
  // * @return String - ИД визуального элемента, на котором осуществляется размещение подконтрольных элементов
  // */
  // String viselId();
}
