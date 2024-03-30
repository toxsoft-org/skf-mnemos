package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Фабрика создания конкретных экземпляров "контроллеров размещений" по их конфигурации.
 *
 * @author vs
 */
public interface IVedLayoutControllerFactory
    extends IStridableParameterized {

  /**
   * Создает и возвращает контроллер размещения.
   *
   * @param aCfg IVedLayoutControllerConfig - конфигурация контроллера размещения
   * @param aVedScreen {@link IVedScreen} - экран редктора
   * @return IVedViselsLayoutController - контроллер размещения
   */
  IVedViselsLayoutController create( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen );

  /**
   * Возвращает отредактированную конфигурацию контроллера размещения.
   *
   * @param aCfg {@link IVedLayoutControllerConfig} - конфигурация контроллера размещения или <code>null</code>
   * @param aVedScreen {@link IVedScreen} - экран редктора
   * @return {@link IVedLayoutControllerConfig} - новая конфигурация контроллера размещения
   */
  IVedLayoutControllerConfig editConfig( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen );
}
