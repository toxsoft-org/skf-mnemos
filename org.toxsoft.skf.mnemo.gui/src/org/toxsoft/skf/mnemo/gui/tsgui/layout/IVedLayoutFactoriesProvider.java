package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.column.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.line.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

/**
 * Поставщик фабрик контроллеров размещений.
 *
 * @author vs
 */
public interface IVedLayoutFactoriesProvider {

  /**
   * Поставщик фабрик контроллеров размещений по-умолчанию (встроенных в библиотеку)
   */
  IVedLayoutFactoriesProvider DEFAULT = new BuiltInProvider();

  /**
   * Вовращает список фабрик контроллеров размещений.
   *
   * @return IStridablesList&lt;IVedLayoutControllerFactory> - список фабрик
   */
  IStridablesList<IVedLayoutControllerFactory> factories();
}

class BuiltInProvider
    implements IVedLayoutFactoriesProvider {

  private final IStridablesListEdit<IVedLayoutControllerFactory> factories = new StridablesList<>();

  BuiltInProvider() {
    factories.add( new VedRowLayoutControllerFactory() );
    factories.add( new VedColumnLayoutControllerFactory() );
    factories.add( new VedTableLayoutControllerFactory() );
  }

  @Override
  public IStridablesList<IVedLayoutControllerFactory> factories() {
    return factories;
  }

}
