package org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * "Распознаватель" объекта по значению атрибута.
 * <p>
 * Например, из множества однотипных объектов выбери тот, у которого значение атрибута "number" == 12.
 *
 * @author vs
 */
public class ByAttrValueRecognizer
    implements ISkObjectRecognizer {

  /**
   * ИД свойства, содержащего ИД атрибута
   */
  public static final String PROPID_ATTRID = "attrId"; //$NON-NLS-1$

  /**
   * ИД свойства, содержащего значение атрибута
   */
  public static final String PROPID_ATTR_VALUE = "attrValue"; //$NON-NLS-1$

  private final String attrId;

  private final IAtomicValue attrValue;

  /**
   * Конструктор.
   *
   * @param aAttrId String - ИД атрибута
   * @param aAttrValue {@link IAtomicValue} - требуемое значение атрибута
   */
  public ByAttrValueRecognizer( String aAttrId, IAtomicValue aAttrValue ) {
    attrId = aAttrId;
    attrValue = aAttrValue;
  }

  // ------------------------------------------------------------------------------------
  // ISkObjectRecognizer
  //

  @Override
  public boolean recognize( Skid aObjSkid, ISkCoreApi aCoreApi ) {
    ISkObject obj = aCoreApi.objService().find( aObjSkid );
    IAtomicValue value = obj.attrs().getValue( attrId );
    return value.equals( attrValue );
  }

}
