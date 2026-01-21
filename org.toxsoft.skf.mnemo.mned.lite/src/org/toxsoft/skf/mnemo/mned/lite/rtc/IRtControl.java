package org.toxsoft.skf.mnemo.mned.lite.rtc;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * Editable item of the light mnemo editor.
 *
 * @author vs
 */
public interface IRtControl
    extends IStridableParameterized, IPropertable<IRtControl> {

  /**
   * Determines if item operation is enabled.
   * <p>
   * Disabled visual elements became invisible, disabled actors stop responding to any events or time flow.
   * <p>
   * This method simply returns the value of the {@link IVedScreenConstants#PROP_IS_ACTIVE} property.
   *
   * @return boolean - the sign the item is enabled, is active
   */
  boolean isActive();

  /**
   * Sets the value of the {@link IVedScreenConstants#PROP_IS_ACTIVE} property.
   *
   * @param aIsActive boolean - VED item activity state
   */
  default void setActive( boolean aIsActive ) {
    props().setBool( PROP_IS_ACTIVE, aIsActive );
  }

  /**
   * Returns the ID of the creator factory.
   *
   * @return String - the item factory ID
   */
  String factoryId();

  /**
   * Returns the data designed for extension of the VED functionality.
   *
   * @return {@link IKeepablesStorageRo} - arbitrary editable data
   */
  IKeepablesStorage extraData();
}
