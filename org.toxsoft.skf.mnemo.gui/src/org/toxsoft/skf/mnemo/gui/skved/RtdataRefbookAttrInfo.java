package org.toxsoft.skf.mnemo.gui.skved;

import org.toxsoft.core.tslib.bricks.strid.more.*;

/**
 * Информация, позволяющая получить значение атрибута элемента справочника по знчению ключа.
 * <p>
 * Format is 3-branches {@link IdChain}: "RefbookId/keyAttrId/valueAttrId".
 *
 * @author vs
 */
public class RtdataRefbookAttrInfo {

  /**
   * The index of refbook ID in the {@link IdChain}.
   */
  public static final int IDX_REFBOOK_ID = 0;

  /**
   * The index of refbook attr ID containing key value in the {@link IdChain}.
   */
  public static final int IDX_KEY_ID = 1;

  /**
   * The index of refbook attr ID containing required value in the {@link IdChain}.
   */
  public static final int IDX_VALUE_ID = 2;

  // ------------------------------------------------------------------------------------
  // API
  //

}
