package org.toxsoft.skf.mnemo.gui.utils;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * @author vs
 */
public class MnemoUtils {

  /**
   * Возвращает соединение с сервером, с которым работает экран редактирования мнемосхем.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактирования мнемосхемы
   * @return {@link ISkConnection} - соединение с сервером
   */
  public static ISkConnection vedScreenConnection( IVedScreen aVedScreen ) {
    ISkVedEnvironment vedEnv = aVedScreen.tsContext().get( ISkVedEnvironment.class );
    return vedEnv.skConn();
  }

  /**
   * Возвращает признак существования сущности, на которую указывает {@link Ugwi}.<br>
   * Имеет смысл для тех {@link Ugwi}, которые ссылаются на одну сущность, например объект, атрибут, данное и т.п.
   *
   * @param aUgwi {@link Ugwi} - ИД сущности
   * @param aCoreApi {@link ISkCoreApi} - API сервера
   * @return <b>true</b> - сущность есть<br>
   *         <b>false</b> - сущность отсутствует
   */
  public static boolean isEntityExists( Ugwi aUgwi, ISkCoreApi aCoreApi ) {
    ISkUgwiKind ugwiKind = aCoreApi.ugwiService().getKind( aUgwi );
    return ugwiKind.isContent( aUgwi );
  }

  /**
   * Возвращает {@link Ugwi} из набора значений по идентификатору.<br>
   * Не порождает исключений - в случае отсутствия возвращает <code>null</code>
   *
   * @param aPropId String - ИД опции
   * @param aValues {@link IOptionSet} - набор значений
   * @return {@link Ugwi} - Ugwi или <code>null</code>
   */
  public static Ugwi findUgwi( String aPropId, IOptionSet aValues ) {
    if( aValues.hasKey( aPropId ) ) {
      IAtomicValue av = aValues.getByKey( aPropId );
      if( av.isAssigned() ) {
        return av.asValobj();
      }
    }
    return null;
  }

  /**
   * Возвращает список акторов с неверными или отсутствующими {@link Ugwi}.<br>
   *
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   * @param aCoreApi {@link ISkCoreApi} - API сервера
   * @return IStridablesList&lt;IVedItem> - список акторов не привязанных к визелям.
   */
  public static IStridablesList<IVedItem> listWrongUgwiActors( IVedScreen aVedScreen, ISkCoreApi aCoreApi ) {
    IStridablesListEdit<IVedItem> result = new StridablesList<>();
    for( IVedActor actor : aVedScreen.model().actors().list() ) {
      for( IDataDef dd : actor.props().propDefs() ) {
        if( dd.keeperId() != null && dd.keeperId().equals( Ugwi.KEEPER_ID ) ) {
          Ugwi ugwi = findUgwi( dd.id(), actor.props() );
          // ISkCoreApi coreApi = SkGuiUtils.getCoreApi( aVedScreen.tsContext() );
          if( ugwi == null || ugwi == Ugwi.NONE || !isEntityExists( ugwi, aCoreApi ) ) {
            result.add( actor );
          }
        }
      }
    }
    return result;
  }

  /**
   * No subclasses.
   */
  private MnemoUtils() {
    // nop
  }
}
