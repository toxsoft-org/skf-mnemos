package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Менеджер мастер-объектов и путей к ним.
 * <p>
 *
 * @author vs
 */
public class SkVedMasterObjectsManager {

  private String masterClassId = TsLibUtils.EMPTY_STRING;

  private final GenericChangeEventer eventer;

  /**
   * Конструктор.
   */
  public SkVedMasterObjectsManager() {
    eventer = new GenericChangeEventer( this );
  }

  /**
   * Возвращает ИД класса мастер-объекта мнемосхемы или пустую строку если он не установлен.
   *
   * @return String - ИД класса мастер-объекта мнемосхемы или пустую строку если он не установлен
   */
  public String masterClassId() {
    return masterClassId;
  }

  /**
   * Задает ИД класса мастер-объекта мнемосхемы
   *
   * @param aMasterClassId String - ИД класса мастер-объекта мнемосхемы
   */
  public void setMastwrClassId( String aMasterClassId ) {
    if( !aMasterClassId.equals( masterClassId ) ) {
      masterClassId = aMasterClassId;
      eventer.fireChangeEvent();
    }
  }

  /**
   * Генератор события изменения.
   *
   * @return IGenericChangeEventer - генератор события изменения
   */
  public IGenericChangeEventer eventer() {
    return eventer;
  }
}
