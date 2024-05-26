package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * "Разрешитель" единственного sk-объекта по связи.
 * <p>
 * Имеется ввиду, что по указанной связи находится только один объект.
 *
 * @author vs
 */
public class ByLinkSingleObjectResolver
    extends AbstractSimpleResolver
    implements IGwidResolver {

  /**
   * ИД фабрики
   */
  public static final String FACTORY_ID = "byLinkSingleObjectResolverFactory"; //$NON-NLS-1$

  private static final IStridablesList<IDataDef> dataDefs = new StridablesList<>( PROP_GWID );

  /**
   * Фабрика создания "разрешителя"
   */
  public static final ISimpleResolverFactory FACTORY = new AbstractSimpleResolverFactory( //
      FACTORY_ID, new OptionSet(), dataDefs ) {

    @Override
    protected AbstractSimpleResolver doCreateResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
      return new ByLinkSingleObjectResolver( aResolverConfig, aSkConn );
    }
  };

  /**
   * Конструктор.
   *
   * @param aResolverConfig {@link IOptionSet} - конфигурация "разрешителя"
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   */
  public ByLinkSingleObjectResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    super( new OptionSet(), aSkConn );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSimpleResolver
  //

  @Override
  public Gwid resolve( Skid aMasterSkid ) {
    Gwid abstractGwid = cfg().getValobj( PROP_GWID );
    return DirectGwidResolver.makeConcreteGwid( aMasterSkid.strid(), abstractGwid );
  }

  @Override
  protected Ugwi doResolve( Ugwi aMaster ) {
    // FIXME TsIllegalArgumentRtException.checkFalse( aMaster.kindId().equals( UgwiKindGwid.KIND_ID ) );
    TsIllegalArgumentRtException.checkFalse( aMaster.kindId().equals( "gwid" ) );
    Gwid gwid = Gwid.of( aMaster.essence() );
    return null;
  }

}
