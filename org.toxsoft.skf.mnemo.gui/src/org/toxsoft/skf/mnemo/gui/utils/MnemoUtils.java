package org.toxsoft.skf.mnemo.gui.utils;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

/**
 * @author vs
 */
public class MnemoUtils {

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

    return switch( aUgwi.kindId() ) {
      case UgwiKindSkAttr.KIND_ID -> UgwiKindSkAttr.isEntityExists( aUgwi, aCoreApi );
      case UgwiKindRriAttr.KIND_ID -> UgwiKindRriAttr.isEntityExists( aUgwi, aCoreApi );
      case UgwiKindSkRtdata.KIND_ID -> UgwiKindSkRtdata.isEntityExists( aUgwi, aCoreApi );
      case UgwiKindSkSkid.KIND_ID -> UgwiKindSkSkid.isEntityExists( aUgwi, aCoreApi );
      case UgwiKindSkLink.KIND_ID -> UgwiKindSkLink.isEntityExists( aUgwi, aCoreApi );
      case UgwiKindSkRivet.KIND_ID -> UgwiKindSkRivet.isEntityExists( aUgwi, aCoreApi );
      case UgwiKindSkCmd.KIND_ID -> UgwiKindSkCmd.isEntityExists( aUgwi, aCoreApi );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
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
