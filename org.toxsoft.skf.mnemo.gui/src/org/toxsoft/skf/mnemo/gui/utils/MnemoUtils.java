package org.toxsoft.skf.mnemo.gui.utils;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.ugwis.*;

/**
 * @author vs
 */
public class MnemoUtils {

  /**
   * Возвращает список акторов с неверными или отсутствующими {@link Ugwi}.<br>
   *
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   * @return IStridablesList&lt;IVedItem> - список акторов не привязанных к визелям.
   */
  public static IStridablesList<IVedItem> listWrongUgwiActors( IVedScreen aVedScreen ) {
    IStridablesListEdit<IVedItem> result = new StridablesList<>();
    for( IVedActor actor : aVedScreen.model().actors().list() ) {
      for( IDataDef dd : actor.props().propDefs() ) {
        if( dd.keeperId() != null && dd.keeperId().equals( Ugwi.KEEPER_ID ) ) {
          Ugwi ugwi = SkUgwiUtils.findUgwi( dd.id(), actor.props() );
          ISkCoreApi coreApi = SkGuiUtils.getCoreApi( aVedScreen.tsContext() );
          if( ugwi == null || ugwi == Ugwi.NONE || !SkUgwiUtils.isEntityExists( ugwi, coreApi ) ) {
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
