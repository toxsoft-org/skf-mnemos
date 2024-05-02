package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;

/**
 * "Распознаватель" объекта по наличию определенного связанного объекта.
 * <p>
 * Например, из множества однотипных объектов выбери тот, у которого по связи color есть объект {clsColor,red}.
 *
 * @author vs
 */
public class ByLinkedObjectRecognizer
    implements ISkObjectRecognizer {

  private final String linkId;

  private final Skid objSkid;

  /**
   * Конструктор.
   *
   * @param aLinkId String - ИД связи
   * @param aObjSkid {@link Skid} - ИД объекта, который должен быть связан
   */
  public ByLinkedObjectRecognizer( String aLinkId, Skid aObjSkid ) {
    linkId = aLinkId;
    objSkid = aObjSkid;
  }

  // ------------------------------------------------------------------------------------
  // ISkObjectRecognizer
  //

  @Override
  public boolean recognize( Skid aObjSkid, ISkCoreApi aCoreApi ) {
    IDtoLinkFwd linkInfo = aCoreApi.linkService().getLinkFwd( aObjSkid, linkId );
    return linkInfo.rightSkids().hasElem( objSkid );
  }

}
