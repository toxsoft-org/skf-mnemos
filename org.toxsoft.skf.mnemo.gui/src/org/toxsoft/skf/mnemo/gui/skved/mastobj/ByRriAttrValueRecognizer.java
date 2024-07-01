package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.*;

/**
 * "Распознаватель" объекта по значению НСИ атрибута.
 * <p>
 * Например, из множества однотипных объектов выбери тот, у которого значение НСИ атрибута number == 12.
 *
 * @author vs
 */
public class ByRriAttrValueRecognizer
    implements ISkObjectRecognizer {

  private final String rriSectionId;

  private final String attrId;

  private final IAtomicValue attrValue;

  /**
   * Конструктор.
   *
   * @param aRriSectionId String - ИД секции НСИ
   * @param aAttrId String - ИД атрибута
   * @param aAttrValue {@link IAtomicValue} - требуемое значение атрибута
   */
  public ByRriAttrValueRecognizer( String aRriSectionId, String aAttrId, IAtomicValue aAttrValue ) {
    rriSectionId = aRriSectionId;
    attrId = aAttrId;
    attrValue = aAttrValue;
  }

  // ------------------------------------------------------------------------------------
  // ISkObjectRecognizer
  //

  @Override
  public boolean recognize( Skid aObjSkid, ISkCoreApi aCoreApi ) {
    ISkRegRefInfoService service = aCoreApi.getService( ISkRriServiceHardConstants.SERVICE_ID );
    ISkRriSection section = service.getSection( rriSectionId );
    IAtomicValue value = section.getAttrParamValue( aObjSkid, attrId );
    return value.equals( attrValue );
  }

}
