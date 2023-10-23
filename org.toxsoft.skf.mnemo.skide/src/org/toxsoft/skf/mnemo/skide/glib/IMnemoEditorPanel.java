package org.toxsoft.skf.mnemo.skide.glib;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;

/**
 * The mnemoscheme editing panel.
 *
 * @author hazard157
 */
public interface IMnemoEditorPanel {

  /**
   * Sets the handler of the action to be performed externally.
   * <p>
   * Called when user performs some actions in the mnemo editor GUI. Right now following actions may be reuested:
   * <ul>
   * <li>{@link ITsStdActionDefs#ACTID_SAVE};</li>
   * <li>TODO what else?.</li>
   * </ul>
   * <p>
   * FIXME change to external command executor to return value to panel, eg. SAVE_AS was proceeded or cancelled
   *
   * @param aHandler {@link ITsActionHandler} - external actions handler
   */
  void setExternelHandler( ITsActionHandler aHandler );

  /**
   * Returns the current VED screen configuration.
   *
   * @return {@link IVedScreenCfg} - the VED screen configuration
   */
  IVedScreenCfg getCurrentConfig();

  /**
   * Replaces the current config with the new one.
   *
   * @param aCfg {@link IVedScreenCfg} - the VED screen configuration
   */
  void setCurrentConfig( IVedScreenCfg aCfg );

}
