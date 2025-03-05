package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

public class SelectionCopyPasteProcessor
    implements ICopyPasteProcessor {

  private final IVedScreen vedScreen;

  private final IVedViselSelectionManager selectionManager;

  /**
   * Конструктор.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   * @param aSelectionManager {@link IVedViselSelectionManager} - менеджер выделения
   */
  public SelectionCopyPasteProcessor( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager ) {
    vedScreen = aVedScreen;
    selectionManager = aSelectionManager;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "ved.copyPaste.selection.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Копировать выделенное";
  }

  @Override
  public String description() {
    return "Осуществляет определение необходимости копирования вставки в зависимости от текущего выделения";
  }

  // ------------------------------------------------------------------------------------
  // ICopyPasteProcessor
  //

  @Override
  public void editIdsForCopy( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams ) {
    IStringList selIds = selectionManager.selectedViselIds();
    if( !aViselIds.isEmpty() && selIds.hasElem( aViselIds.first() ) ) {
      aViselIds.clear();
    }
    if( aViselIds.isEmpty() ) {
      aViselIds.addAll( selIds );
    }

    for( String vid : aViselIds ) {
      IStringList actIds = VedScreenUtils.viselActorIds( vid, vedScreen );
      aActorIds.addAll( actIds );
    }
  }

  @Override
  public void editConfigsForPaste( IStridablesListEdit<VedItemCfg> aVisConfs, IListEdit<VedItemCfg> aActConfs,
      IStringMap<String> aViselsMap, IStringMap<String> aActorsMap, IOptionSet aParams ) {
    // nop - никакие правки не требуются
  }
}
