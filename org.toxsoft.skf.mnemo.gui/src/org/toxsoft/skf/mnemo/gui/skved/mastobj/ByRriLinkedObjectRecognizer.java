package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.*;

/**
 * "Распознаватель" объекта по наличию определенного НСИ связанного объекта.
 * <p>
 * Например, из множества однотипных объектов выбери тот, у которого по НСИ связи color есть объект {clsColor,red}.
 *
 * @author vs
 */
public class ByRriLinkedObjectRecognizer
    implements ISkObjectRecognizer {

  private final String sectionId;

  private final String linkId;

  private final Skid objSkid;

  /**
   * Конструктор.
   *
   * @param aRriSectionId String - ИД НСИ секции
   * @param aLinkId String - ИД связи
   * @param aObjSkid {@link Skid} - ИД объекта, который должен быть связан
   */
  public ByRriLinkedObjectRecognizer( String aRriSectionId, String aLinkId, Skid aObjSkid ) {
    sectionId = aRriSectionId;
    linkId = aLinkId;
    objSkid = aObjSkid;
  }

  // ------------------------------------------------------------------------------------
  // ISkObjectRecognizer
  //

  @Override
  public boolean recognize( Skid aObjSkid, ISkCoreApi aCoreApi ) {
    ISkRegRefInfoService service = aCoreApi.getService( ISkRriServiceHardConstants.SERVICE_ID );
    ISkRriSection section = service.getSection( sectionId );
    ISkidList skids = section.getLinkParamValue( aObjSkid, linkId );
    return skids.hasElem( objSkid );
  }

}
