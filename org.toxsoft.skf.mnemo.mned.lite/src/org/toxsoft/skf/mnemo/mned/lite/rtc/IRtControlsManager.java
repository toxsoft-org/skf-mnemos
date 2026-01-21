package org.toxsoft.skf.mnemo.mned.lite.rtc;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages IRtControls.
 * <p>
 * Notes:
 * <ul>
 * <li>VED screen framework internally uses {@link #prepareFromTemplate(IRtControlCfg)} when creating new VED item. It
 * is highly recommended to use the same approach when adding new items by external means. Thus ensuring the unified
 * item ID generation. However, user may specify it's own ID at the item creation;</li>
 * </ul>
 *
 * @author vs
 */
public interface IRtControlsManager
    extends ITsClearable {

  /**
   * Returns the managed items.
   *
   * @return {@link IStridablesList}&lt;IRtControl> - the ordered list of items
   */
  IStridablesList<IRtControl> list();

  /**
   * Retuns {@link IRtControl} by corresponding visel ID.
   *
   * @param aViselId String - visel ID
   * @return {@link IRtControl} - found RtControl or null
   */
  IRtControl getRtControlByViselId( String aViselId );

  /**
   * Returns the managed items order change means.
   *
   * @return {@link IStridablesListReorderer}&ltT&gt; - the {@link #list()} re-orderer
   */
  IStridablesListReorderer<IRtControl> reorderer();

  /**
   * Prepares the item config from some kind of the template config provided.
   * <p>
   * It is assumed that template configuration is provided by external means such as the VED items palette or copy/paste
   * operation. Preparation includes the unique (for current VED screen) ID and name generation.
   * <p>
   * Note: the new ID/name pair will be generated even if template ID/name already is unique.
   * <p>
   * It is guaranteed that {@link #create(int, IRtControlCfg)} method will not throw an "duplicate ID"
   * {@link TsItemAlreadyExistsRtException} exception.
   *
   * @param aTemplateCfg {@link IVedItemCfg} - the item configuration template
   * @return {@link IRtControlCfg} - an editable instance based on template
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no item factory found
   */
  IRtControlCfg prepareFromTemplate( IRtControlCfg aTemplateCfg );

  /**
   * Creates the item and adds it at the end of the list {@link #list()}.
   *
   * @param aCfg {@link IRtControlCfg} - the configuration the item to create
   * @return IRtControl - created item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link IVedItemsManagerValidator#canCreate(int, IVedItemCfg)}
   */
  default IRtControl create( IRtControlCfg aCfg ) {
    return create( list().size(), aCfg );
  }

  /**
   * Creates the item and inserts at the specified position in the list {@link #list()}.
   *
   * @param aIndex int - index of inserted item
   * @param aCfg {@link IRtControlCfg} - the configuration the item to create
   * @return IRtControl - created item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link IVedItemsManagerValidator#canCreate(int, IVedItemCfg)}
   */
  IRtControl create( int aIndex, IRtControlCfg aCfg );

  /**
   * Removes the item by ID.
   * <p>
   * IF items with the specified ID does not exist then method does nothing.
   *
   * @param aId String - the ID of the item to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void remove( String aId );

  /**
   * Returns the manager validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link IRtControlsManagerListener}&gt; - the manager validator
   */
  ITsValidationSupport<IRtControlsManagerValidator> svs();

  /**
   * Returns the manager eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IRtControlsManagerListener}&gt; - the manager eventer
   */
  ITsEventer<IRtControlsManagerListener> eventer();

  /**
   * Informs model about items change.
   * <p>
   * Only two operations are allowed as the argument <code>aOp</code>: {@link ECrudOp#EDIT} and {@link ECrudOp#LIST}.
   *
   * @param aOp {@link ECrudOp} - the change operation
   * @param aItemId String - the item ID or <code>null</code> for {@link ECrudOp#LIST}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid <code>aOp</code> value
   */
  void informOnModelChange( ECrudOp aOp, String aItemId );
}
