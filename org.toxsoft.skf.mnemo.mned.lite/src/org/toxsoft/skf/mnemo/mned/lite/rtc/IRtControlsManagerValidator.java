package org.toxsoft.skf.mnemo.mned.lite.rtc;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Validate {@link IRtControlsManager} CRUD operations.
 *
 * @author vs
 */
public interface IRtControlsManagerValidator {

  /**
   * Checks if the item can be created.
   * <p>
   * Check for:
   * <ul>
   * <li>index validity;</li>
   * <li>same ID already exists;</li>
   * <li>factory ID is not registered.</li>
   * </ul>
   *
   * @param aIndex int - index of inserted item
   * @param aCfg {@link IRtControlCfg} - the configuration the item to create
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canCreate( int aIndex, IRtControlCfg aCfg );

  /**
   * Checks if item can be removed.
   *
   * @param aId String - the ID of the item to remove
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemove( String aId );
}
