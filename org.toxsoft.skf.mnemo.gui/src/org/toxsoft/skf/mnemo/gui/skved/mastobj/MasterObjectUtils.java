package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;

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
   * Запрет на создание экземпляров
   */
  private MasterObjectUtils() {
    // nop
  }

}
