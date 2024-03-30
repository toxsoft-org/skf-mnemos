package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Менеджер положения визуальных элементов на основе выделения.
 *
 * @author vs
 */
public class SelectedViselsPositionManager
    implements IViselsPositionProcessor {

  private final IVedViselSelectionManager selectionManager;

  /**
   * Конструктор.
   *
   * @param aSelectionManager IVedViselSelectionManager - менеджер выделения
   */
  public SelectedViselsPositionManager( IVedViselSelectionManager aSelectionManager ) {
    selectionManager = aSelectionManager;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "ved.selection.position.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Переместить выделенное";
  }

  @Override
  public String description() {
    return "Премещает выделенные элементы";
  }

  // ------------------------------------------------------------------------------------
  // IViselsPositionProcessor
  //

  @Override
  public void editIdsForMove( IStringListEdit aViselIds, IOptionSetEdit aParams ) {
    IStringList selIds = selectionManager.selectedViselIds();
    if( aViselIds.size() == 1 ) {
      if( !selIds.hasElem( aViselIds.first() ) ) {
        return; // нужно переместить один не выделенны элемент, так что список не правим
      }
    }
    for( String id : selIds ) {
      if( !aViselIds.hasElem( id ) ) {
        aViselIds.add( id );
      }
    }
  }

}
