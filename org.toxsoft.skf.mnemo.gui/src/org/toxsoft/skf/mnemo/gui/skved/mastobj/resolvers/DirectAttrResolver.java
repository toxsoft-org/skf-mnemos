package org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers;

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
 * В качестве конфигурационной информации хранит абстрактный Ugwi атрибута.
 *
 * @author vs
 */
public class DirectAttrResolver
    extends AbstractSimpleResolver {

  /**
   * ИД фабрики
   */
  public static final String FACTORY_ID = "directAttrResolverFactory"; //$NON-NLS-1$

  private static final IStridablesList<IDataDef> dataDefs = new StridablesList<>( PROP_UGWI );

  /**
   * Фабрика создания "разрешителя"
   */
  public static final ISimpleResolverFactory FACTORY = new AbstractSimpleResolverFactory( //
      FACTORY_ID, new OptionSet(), dataDefs ) {

    @Override
    protected AbstractSimpleResolver doCreateResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
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
  // AbstractSimpleResolver
  //

  @Override
  protected Ugwi doResolve( Ugwi aMaster ) {
    if( aMaster.kindId().equals( UgwiKindSkSkid.KIND_ID ) ) {
      Skid masterSkid = UgwiKindSkSkid.getSkid( aMaster );
      Ugwi ugwi = cfg().getValobj( PROPID_UGWI );
      return createAttrUgwi( masterSkid, ugwi );
    }
    return null;
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

  // ------------------------------------------------------------------------------------
  // Implememntation
  //

  private static Ugwi createAttrUgwi( Skid aObjSkid, Ugwi aUgwi ) {
    if( aUgwi.kindId().equals( UgwiKindSkAttrInfo.KIND_ID ) ) {
      return UgwiKindSkAttrInfo.makeUgwi( aObjSkid, UgwiKindSkAttrInfo.getAttrId( aUgwi ) );
    }
    return null;
  }

}
