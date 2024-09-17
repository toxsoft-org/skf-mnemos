package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

public class SelectionDeleteProcessor
    implements IDeleteProcessor {

  private final IVedViselSelectionManager selectionManager;

  private final IVedScreen vedScreen;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   * @param aSelectionManager {@link IVedViselSelectionManager} - менеджер удаления визуальных элементов
   */
  public SelectionDeleteProcessor( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager ) {
    vedScreen = aVedScreen;
    selectionManager = aSelectionManager;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "ved.delete.selection.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Удалить выделенное";
  }

  @Override
  public String description() {
    return "Удаляет выделеные визуальные элементы и связанные с ними акторы";
  }

  // ------------------------------------------------------------------------------------
  // IDeleteProcessor
  //

  @Override
  public void editIdsForDelete( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams ) {
    aViselIds.addAll( selectionManager.selectedViselIds() );
    for( String viselId : aViselIds ) {
      aActorIds.addAll( VedScreenUtils.viselActorIds( viselId, vedScreen ) );
    }
  }

  @Override
  public void handlePostDeletion( IStringListEdit aViselIds, IStringListEdit aActorIds ) {
    // nop
  }

}
