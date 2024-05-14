package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.utils.ugwi.*;
import org.toxsoft.uskat.core.utils.ugwi.kind.*;

/**
 * Класс, который разрешает Gwid с помощью мастер-мастер объекта непосредственно в требуемый Gwid.
 * <p>
 * В качестве конфигурационной информации хранит абстрактный Gwid требуемой sk-сущности от класса мастер объекта.
 *
 * @author vs
 */
public class DirectGwidResolver
    extends AbstractSimpleResolver
    implements IGwidResolver {

  /**
   * ИД фабрики
   */
  public static final String FACTORY_ID = "directGwidResolverFactory"; //$NON-NLS-1$

  private static final IStridablesList<IDataDef> dataDefs = new StridablesList<>( PROP_GWID );

  /**
   * Фабрика создания "разрешителя"
   */
  public static final ISimpleResolverFactory FACTORY = new AbstractSimpleResolverFactory( //
      FACTORY_ID, new OptionSet(), dataDefs ) {

    @Override
    protected AbstractSimpleResolver doCreateResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
      // Gwid gwid = aResolverConfig.getValobj( PROP_GWID );
      // return new DirectGwidResolver( gwid, aSkConn );
      return new DirectGwidResolver( aResolverConfig, aSkConn );
    }
  };

  // private final Gwid abstractGwid;

  /**
   * Конструктор.
   *
   * @param aResolverConfig {@link IOptionSet} - конфигурация "разрешителя"
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   */
  public DirectGwidResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    super( new OptionSet(), aSkConn );
    // abstractGwid = aGwid;
  }

  // ------------------------------------------------------------------------------------
  // IGwidResolver
  //

  @Override
  public Gwid resolve( Skid aMasterSkid ) {
    Gwid abstractGwid = cfg().getValobj( PROP_GWID );
    return makeConcreteGwid( aMasterSkid.strid(), abstractGwid );
  }

  @Override
  protected Ugwi doResolve( Ugwi aMaster ) {
    String essence = aMaster.essence();
    Gwid gwid = Gwid.of( essence );
    gwid = resolve( gwid.skid() );
    return Ugwi.of( UgwiKindGwid.KIND_ID, gwid.canonicalString() );
  }

  // ------------------------------------------------------------------------------------
  // Implememntation
  //

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
