package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Класс, который разрешает Ugwi типа {@link UgwiKindSkSkid} с помощью мастер-мастер объекта в Ugwi типа
 * {@link UgwiKindSkAttr}.
 * <p>
 * В качестве конфигурационной информации хранит абстрактный Gwid требуемой sk-сущности от класса мастер объекта.
 *
 * @author vs
 */
public class DirectAttrResolver
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
      return new DirectAttrResolver( aResolverConfig, aSkConn );
    }
  };

  /**
   * Конструктор.
   *
   * @param aResolverConfig {@link IOptionSet} - конфигурация "разрешителя"
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   */
  public DirectAttrResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    super( new OptionSet(), aSkConn );
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
    // FIXME return Ugwi.of( UgwiKindGwid.KIND_ID, gwid.canonicalString() );
    return Ugwi.of( "gwid", gwid.canonicalString() );
  }

  // ------------------------------------------------------------------------------------
  // Static methods
  //

  /**
   * Возвращает конфигурацию для {@link DirectAttrResolver}.
   *
   * @param aGwid Gwid - Gwid объекта м.б. абстрактным
   * @return {@link ICompoundResolverConfig} - конфигурацию для {@link DirectAttrResolver}
   */
  public static ICompoundResolverConfig createResolverConfig( Gwid aGwid ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setValobj( PROPID_GWID, aGwid );
    SimpleResolverCfg simpleCfg = new SimpleResolverCfg( FACTORY_ID, opSet );
    IList<SimpleResolverCfg> simpleConfigs = new ElemArrayList<>( simpleCfg );
    CompoundResolverConfig cfg = new CompoundResolverConfig( simpleConfigs );
    return cfg;
  }

  /**
   * Возвращает признак того, есть ли в парметрах {@link Gwid}.
   *
   * @param aCfg {@link SimpleResolverCfg} - конфигурация "разрешителя"
   * @return <b>true</b> - Gwid есть<br>
   *         <b>false</b> - Gwid'a нет
   */
  public static boolean hasGwid( SimpleResolverCfg aCfg ) {
    return aCfg.params().hasKey( PROPID_GWID );
  }

  /**
   * Возвращает {@link Gwid} - мастер объекта, который м.б. разрешен с помощью Strid'a
   *
   * @param aCfg {@link SimpleResolverCfg} - конфигурация "разрешителя"
   * @return Gwid - gwid мастер-объекта
   */
  public static Gwid gwid( SimpleResolverCfg aCfg ) {
    return aCfg.params().getValobj( PROPID_GWID );
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
