package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Обработчик информации о составе копируемых и вставляемых элементах редактора {@link IVedItem}.
 *
 * @author vs
 */
public interface ICopyPasteProcessor
    extends IStridable {

  /**
   * Редактирует, изменяет (если необходимо) список идентификаторов элементов редактора {@link IVedItem}, информация о
   * которых будет скопирована в буфер обмена.
   *
   * @param aViselIds {@link IStringListEdit} - текущий список идентификаторов визуальных элементов
   * @param aActorIds {@link IStringListEdit} - текущий список идентификаторов акторов
   * @param aParams {@link IOptionSetEdit} - редактируемый набор параметров
   */
  void editIdsForCopy( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams );

  /**
   * Редактирует, изменяет (если необходимо) список конфигураций элементов редактора.
   *
   * @param aVisConfs IStridablesListEdit&lt;VedItemCfg> - текущий список конфигураций визуальных элементов
   * @param aActConfs IListEdit&lt;VedItemCfg> - текущий список конфигураций акторов
   * @param aViselsMap {@link IStringMap} - карта сответствия ИДов Visel'ей ключ - старый, значение - новый
   * @param aActorsMap {@link IStringMap} - карта сответствия ИДов акторов ключ - старый, значение - новый
   * @param aParams {@link IOptionSet} - набор дополнительных параметров
   */
  void editConfigsForPaste( IStridablesListEdit<VedItemCfg> aVisConfs, IListEdit<VedItemCfg> aActConfs, //
      IStringMap<String> aViselsMap, IStringMap<String> aActorsMap, IOptionSet aParams );
}
