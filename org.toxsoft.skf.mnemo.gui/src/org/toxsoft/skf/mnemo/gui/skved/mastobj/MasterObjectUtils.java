package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

/**
 * Утилитные методы для работы с мастер-объектами.
 * <p>
 *
 * @author vs
 */
public class MasterObjectUtils {

  /**
   * ИД опции класс ИДа, в который разрешается входной Ugwi
   */
  public static final String PROPID_RESOLVER_OUTPUT_CLASS_ID = "resolver.output.classId"; //$NON-NLS-1$

  /**
   * Возвращает конфигурацию для {@link DirectGwidResolver}.
   *
   * @param aGwid Gwid - Gwid объекта м.б. абстрактным
   * @return {@link ICompoundResolverConfig} - конфигурацию для {@link DirectGwidResolver}
   */
  public static ICompoundResolverConfig createDirectGwidResolverConfig( Gwid aGwid ) {
    IOptionSetEdit opSet = new OptionSet();
    opSet.setValobj( PROPID_GWID, aGwid );
    SimpleResolverCfg simpleCfg = new SimpleResolverCfg( DirectGwidResolver.FACTORY_ID, opSet );
    ICompoundResolverConfig cfg = new CompoundResolverConfig( new ElemArrayList<>( simpleCfg ) );
    return cfg;
  }

  /**
   * Считывает конфигурацию "разрешителя" мастер-объекта для мнемосхемы из конфигурации экрана.<br>
   * Если информация о "рарешителе" отсутствует - возвращает новую (пустую) конфигурацию.
   *
   * @param aScreen {@link IVedScreen} - экран мнемосхемы
   * @return {@link MnemoResolverConfig} - конфигурацию "разрешителя" мастер-объекта для мнемосхемы
   */
  public static MnemoResolverConfig readMnemoResolverConfig( IVedScreen aScreen ) {
    return readMnemoResolverConfig( VedScreenUtils.getVedScreenConfig( aScreen ) );
  }

  /**
   * Считывает конфигурацию "разрешителя" мастер-объекта для мнемосхемы из конфигурации экрана.<br>
   * Если информация о "рарешителе" отсутствует - возвращает новую (пустую) конфигурацию.
   *
   * @param aScreenCfg {@link IVedScreenCfg} - конфигурация экрана мнемосхемы
   * @return {@link MnemoResolverConfig} - конфигурацию "разрешителя" мастер-объекта для мнемосхемы
   */
  public static MnemoResolverConfig readMnemoResolverConfig( IVedScreenCfg aScreenCfg ) {
    MnemoResolverConfig resolverConfig = new MnemoResolverConfig();
    String sectionId = VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF;
    if( aScreenCfg.extraData().hasSection( sectionId ) ) {
      resolverConfig.subMasters().clear();
      resolverConfig.clearActorSubmasterIds();
      IEntityKeeper<IMnemoResolverConfig> keeper = MnemoResolverConfig.KEEPER;
      IKeepablesStorageRo ks = aScreenCfg.extraData();
      resolverConfig = (MnemoResolverConfig)ks.readItem( sectionId, keeper, resolverConfig );
    }
    return resolverConfig;
  }

  /**
   * Находит и возвращает описание класса мастер-объекта мнемосхемы или <code>null</code>.
   *
   * @param aResolverConfig {@link MnemoResolverConfig} - конфигурация "разрешителя" мнемосхемы
   * @param aCoreApi {@link ISkCoreApi} - API сервера
   * @return {@link ISkClassInfo} - описание класса мастер-объекта мнемосхемы или <code>null</code>
   */
  public static ISkClassInfo findMainMasterClassInfo( MnemoResolverConfig aResolverConfig, ISkCoreApi aCoreApi ) {
    String classId = findMainMasterClassId( aResolverConfig );
    if( classId != null ) {
      return aCoreApi.sysdescr().findClassInfo( classId );
    }
    return null;
  }

  /**
   * Находит и возвращает описание класса мастер-объекта мнемосхемы или <code>null</code>.
   *
   * @param aResolverConfig {@link MnemoResolverConfig} - конфигурация "разрешителя" мнемосхемы
   * @return {@link ISkClassInfo} - описание класса мастер-объекта мнемосхемы или <code>null</code>
   */
  public static String findMainMasterClassId( MnemoResolverConfig aResolverConfig ) {
    if( aResolverConfig.subMasters().hasKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID ) ) {
      SubmasterConfig smCfg = aResolverConfig.subMasters().getByKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID );
      if( smCfg.resolverCfg().cfgs().first().params().hasKey( PROPID_UGWI ) ) {
        Ugwi ugwi = smCfg.resolverCfg().cfgs().first().params().getValobj( PROPID_UGWI );
        if( ugwi.kindId().equals( UgwiKindSkClassInfo.KIND_ID ) ) {
          return UgwiKindSkClassInfo.getClassId( ugwi );
        }
      }
    }
    return null;
  }

  /**
   * Находит и возвращает ИД класса объекта в который разрешается подмастер или <code>null</code>.
   *
   * @param aSubmasterCfg {@link MnemoResolverConfig} - конфигурация подмастера
   * @return {@link ISkClassInfo} - описание класса мастер-объекта мнемосхемы или или <code>null</code>
   */
  public static String outputSubmasterClassId( SubmasterConfig aSubmasterCfg ) {
    if( aSubmasterCfg.params().hasKey( PROPID_RESOLVER_OUTPUT_CLASS_ID ) ) {
      return aSubmasterCfg.params().getStr( PROPID_RESOLVER_OUTPUT_CLASS_ID );
    }
    return null;
  }

  /**
   * Возвращает ИД класса объекта в который разрешается мастер-объект.
   *
   * @param aResolverCfg ICompoundResolverConfig - конфигурация "разрешителя"
   * @return String ИД класса объекта в который разрешается мастер-объект или пустая строка
   */
  public static String resolverTargetClassId( ICompoundResolverConfig aResolverCfg ) {
    TsNullArgumentRtException.checkNull( aResolverCfg );
    SimpleResolverCfg simpleCfg = aResolverCfg.cfgs().last();
    if( simpleCfg.params().hasKey( PROPID_RTD_UGWI ) ) {
      Ugwi ugwi = simpleCfg.params().getValobj( PROPID_RTD_UGWI );
      if( ugwi.kindId().equals( UgwiKindSkRtdata.KIND_ID ) ) {
        return UgwiKindSkRtdata.getClassId( ugwi );
      }
      if( ugwi.kindId().equals( UgwiKindSkRtDataInfo.KIND_ID ) ) {
        return UgwiKindSkRtDataInfo.getClassId( ugwi );
      }
      return TsLibUtils.EMPTY_STRING;
    }
    if( simpleCfg.params().hasKey( PROPID_UGWI ) ) {
      Ugwi ugwi = simpleCfg.params().getValobj( PROPID_UGWI );
      return switch( ugwi.kindId() ) {
        case UgwiKindSkAttrInfo.KIND_ID -> UgwiKindSkAttrInfo.getClassId( ugwi );
        case UgwiKindSkAttr.KIND_ID -> UgwiKindSkAttr.getClassId( ugwi );
        case UgwiKindRriAttrInfo.KIND_ID -> UgwiKindRriAttrInfo.getClassId( ugwi );
        case UgwiKindRriAttr.KIND_ID -> UgwiKindRriAttr.getClassId( ugwi );
        default -> throw new TsNotAllEnumsUsedRtException();
      };
    }
    if( simpleCfg.params().hasKey( PROPID_CMD_UGWI ) ) {
      Ugwi ugwi = simpleCfg.params().getValobj( PROPID_CMD_UGWI );
      return switch( ugwi.kindId() ) {
        case UgwiKindSkCmdInfo.KIND_ID -> UgwiKindSkCmdInfo.getClassId( ugwi );
        case UgwiKindSkCmd.KIND_ID -> UgwiKindSkCmd.getClassId( ugwi );
        default -> throw new TsNotAllEnumsUsedRtException();
      };
    }
    return TsLibUtils.EMPTY_STRING;
  }

  /**
   * Возвращает ИД параметра объекта в который разрешается мастер-объект.
   *
   * @param aResolverCfg ICompoundResolverConfig - конфигурация "разрешителя"
   * @return String ИД параметра объекта в который разрешается мастер-объект или пустая строка
   */
  public static String resolverTargetParamId( ICompoundResolverConfig aResolverCfg ) {
    TsNullArgumentRtException.checkNull( aResolverCfg );
    SimpleResolverCfg simpleCfg = aResolverCfg.cfgs().last();
    if( simpleCfg.params().hasKey( PROPID_RTD_UGWI ) ) {
      Ugwi ugwi = simpleCfg.params().getValobj( PROPID_RTD_UGWI );
      if( ugwi.kindId().equals( UgwiKindSkRtdata.KIND_ID ) ) {
        return UgwiKindSkRtdata.getRtdataId( ugwi );
      }
      if( ugwi.kindId().equals( UgwiKindSkRtDataInfo.KIND_ID ) ) {
        return UgwiKindSkRtDataInfo.getRtDataId( ugwi );
      }
      return TsLibUtils.EMPTY_STRING;
    }

    if( simpleCfg.params().hasKey( PROPID_UGWI ) ) {
      Ugwi ugwi = simpleCfg.params().getValobj( PROPID_UGWI );
      switch( ugwi.kindId() ) {
        case UgwiKindSkAttrInfo.KIND_ID:
          return UgwiKindSkAttrInfo.getAttrId( ugwi );
        case UgwiKindSkAttr.KIND_ID:
          return UgwiKindSkAttr.getAttrId( ugwi );
        default:
          break;
      }
    }
    if( simpleCfg.params().hasKey( PROPID_CMD_UGWI ) ) {
      Ugwi ugwi = simpleCfg.params().getValobj( PROPID_CMD_UGWI );
      switch( ugwi.kindId() ) {
        case UgwiKindSkCmdInfo.KIND_ID:
          return UgwiKindSkCmdInfo.getCmdId( ugwi );
        case UgwiKindSkCmd.KIND_ID:
          return UgwiKindSkCmd.getCmdId( ugwi );
        default:
          break;
      }
    }
    return TsLibUtils.EMPTY_STRING;
  }

  /**
   * Возвращает конфигурацию sub-мастера актора или <code>null</code>.
   *
   * @param aActorId String - ИД актора
   * @param aResolverConfig {@link MnemoResolverConfig} - конфигурация "разрешителя" мастер-объекта мнемосхемы
   * @return {@link SubmasterConfig} - конфигурация sub-мастера актора
   */
  public static SubmasterConfig actorSubmaster( String aActorId, MnemoResolverConfig aResolverConfig ) {
    if( aResolverConfig.actorSubmasterIds().hasKey( aActorId ) ) {
      String smId = aResolverConfig.actorSubmasterIds().getByKey( aActorId );
      return aResolverConfig.subMasters().getByKey( smId );
    }
    return null;
  }

  /**
   * Удалят ИДы подмастеров для указанных акторов.
   *
   * @param aActorIds {@link IStringList} - ИДы акторов
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   */
  public static void deleteActorSubmasters( IStringList aActorIds, IVedScreen aVedScreen ) {
    MnemoResolverConfig mrCfg = MasterObjectUtils.readMnemoResolverConfig( aVedScreen );
    IStringMap<String> actorSmIds = new StringMap<>( mrCfg.actorSubmasterIds() );
    mrCfg.clearActorSubmasterIds();
    for( String actorId : actorSmIds.keys() ) {
      if( !aActorIds.hasElem( actorId ) ) {
        String submasterId = actorSmIds.getByKey( actorId );
        mrCfg.defineActorSubmaster( actorId, submasterId );
      }
    }
    updateMnemoResolverConfig( mrCfg, aVedScreen );
  }

  /**
   * Обновляет список подмастеров мнемосхемы.
   *
   * @param aSubmasters IStridablesList&lt;SubmasterConfig> - список подмастеров
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   */
  public static void updateSubmastersList( IStridablesList<SubmasterConfig> aSubmasters, IVedScreen aVedScreen ) {
    MnemoResolverConfig mnemoResolverCfg = readMnemoResolverConfig( aVedScreen );
    mnemoResolverCfg.subMasters().clear();
    mnemoResolverCfg.subMasters().addAll( aSubmasters );

    IStringMap<String> actorSubmasters = new StringMap<>( mnemoResolverCfg.actorSubmasterIds() );
    mnemoResolverCfg.clearActorSubmasterIds();

    for( String id : actorSubmasters.keys() ) {
      String submasterId = actorSubmasters.getByKey( id );
      if( mnemoResolverCfg.subMasters().hasKey( submasterId ) ) {
        mnemoResolverCfg.defineActorSubmaster( id, submasterId );
      }
    }
    updateMnemoResolverConfig( mnemoResolverCfg, aVedScreen );
  }

  /**
   * Обновляет конфигурацию "разрешителя" мнемосхемы.
   *
   * @param aMnemoResolverCfg {@link MnemoResolverConfig} - конфигурация "разрешителя" мнемосхемы
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   */
  public static void updateMnemoResolverConfig( MnemoResolverConfig aMnemoResolverCfg, IVedScreen aVedScreen ) {
    String itemId = VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF;
    aVedScreen.model().extraData().writeItem( itemId, aMnemoResolverCfg, MnemoResolverConfig.KEEPER );
    // MnemoResolverConfig msc = MasterObjectUtils.readMnemoResolverConfig( aVedScreen );
    // System.out.println( msc.toString() );
  }

  /**
   * Запрет на создание экземпляров
   */
  private MasterObjectUtils() {
    // nop
  }

}
