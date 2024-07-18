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
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
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
   * @return {@link ISkClassInfo} - описание класса мастер-объекта мнемосхемы или null
   */
  public static ISkClassInfo findMainMasterClassId( MnemoResolverConfig aResolverConfig, ISkCoreApi aCoreApi ) {
    if( aResolverConfig.subMasters().hasKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID ) ) {
      SubmasterConfig smCfg = aResolverConfig.subMasters().getByKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID );
      if( smCfg.resolverCfg().cfgs().first().params().hasKey( PROPID_UGWI ) ) {
        Ugwi ugwi = smCfg.resolverCfg().cfgs().first().params().getValobj( PROPID_UGWI );
        if( ugwi.kindId().equals( UgwiKindSkClassInfo.KIND_ID ) ) {
          return aCoreApi.sysdescr().findClassInfo( UgwiKindSkClassInfo.getClassId( ugwi ) );
        }
      }
    }
    return null;
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
