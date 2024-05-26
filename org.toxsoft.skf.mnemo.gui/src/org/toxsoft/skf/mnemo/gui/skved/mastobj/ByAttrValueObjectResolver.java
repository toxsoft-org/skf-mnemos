package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * "Разрешитель" в один объект из множества, посредством сравнения со значением конкретного атрибута.
 * <p>
 *
 * @author vs
 */
public class ByAttrValueObjectResolver
    extends AbstractSimpleResolver
    implements IGwidResolver {

  /**
   * Конструктор.
   *
   * @param aResolverConfig {@link IOptionSet} - конфигурация "разрешителя"
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   */
  public ByAttrValueObjectResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    super( aResolverConfig, aSkConn );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSimpleResolver
  //

  @Override
  public Gwid resolve( Skid aMasterSkid ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected Ugwi doResolve( Ugwi aMaster ) {
    // TODO Auto-generated method stub
    return null;
  }

}
