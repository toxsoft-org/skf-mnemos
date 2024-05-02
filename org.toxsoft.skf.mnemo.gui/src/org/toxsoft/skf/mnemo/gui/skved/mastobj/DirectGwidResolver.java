package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;

/**
 * Класс, который разрешает ИД мастер-мастер объекта непосредственно в требуемый Gwid.
 * <p>
 * В качестве конфигурациооной информации хранит абстрактный Gwid требуемой sk-сущности от класса мастер объекта.
 *
 * @author vs
 */
public class DirectGwidResolver
    implements IGwidResolver {

  private final Gwid abstractGwid;

  /**
   * Конструктор.
   *
   * @param aGwid {@link Gwid} - абстрактный ИД требуемой Sk-сущности
   */
  public DirectGwidResolver( Gwid aGwid ) {
    abstractGwid = aGwid;
  }

  // ------------------------------------------------------------------------------------
  // IGwidResolver
  //

  @Override
  public Gwid resolve( Skid aMasterSkid ) {
    return makeConcreteGwid( aMasterSkid.strid(), abstractGwid );
  }

  /**
   * Создает "конкретный" Gwid из абстрактного.<br>
   * Более точно - задает новому Gwid'у указанный ИД оъекта, сохраняя все остальные свойства.
   *
   * @param aObjId String - ИД объекта
   * @param aGwid Gwid - исходный Gwid
   * @return Gwid с указанным идентификатором объекта
   */
  public static Gwid makeConcreteGwid( String aObjId, Gwid aGwid ) {
    String classId = aGwid.classId();
    String propSectId = aGwid.propSectId();
    String propId = aGwid.propId();
    String subPropSectId = aGwid.subPropSectId();
    String subPropId = aGwid.subPropId();
    return Gwid.create( classId, aObjId, propSectId, propId, subPropSectId, subPropId );
  }

}
