package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Обработчик информации о составе удаляемых элементах редактора {@link IVedItem}.
 *
 * @author vs
 */
public interface IDeleteProcessor
    extends IStridable {

  /**
   * Редактирует, изменяет (если необходимо) список идентификаторов удаляемых элементов редактора {@link IVedItem}.
   *
   * @param aViselIds {@link IStringListEdit} - текущий список удаляемых идентификаторов визуальных элементов
   * @param aActorIds {@link IStringListEdit} - текущий список удаляемых идентификаторов акторов
   * @param aParams {@link IOptionSetEdit} - редактируемый набор параметров
   */
  void editIdsForDelete( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams );

  /**
   * Осуществляет необходимую дополнительную обработку после фактического удаления элементов.<br>
   * Данную обаботку невозможно произвести в методе
   * {@linkplain #editIdsForDelete(IStringListEdit, IStringListEdit, IOptionSetEdit)}, так как другие процессоры могут
   * корректировать список удаляемых элементов.
   *
   * @param aViselIds {@link IStringListEdit} - текущий список удаляемых идентификаторов визуальных элементов
   * @param aActorIds {@link IStringListEdit} - текущий список удаляемых идентификаторов акторов
   */
  void handlePostDeletion( IStringListEdit aViselIds, IStringListEdit aActorIds );
}
