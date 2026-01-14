package org.toxsoft.skf.mnemo.mned.pro.glib;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * The mnemoscheme editing panel.
 *
 * @author hazard157
 */
public interface IMnemoEditorPanel {

  /**
   * Sets the handler of the action to be performed externally.
   * <p>
   * Called when user performs some actions in the mnemo editor GUI. Right now following actions may be requested:
   * <ul>
   * <li>{@link ITsStdActionDefs#ACTID_SAVE};</li>
   * <li>TODO what else?.</li>
   * </ul>
   * <p>
   *
   * @param aHandler {@link ITsActionHandler} - external actions handler or <code>null</code>
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

  /**
   * Determines if VED screen content was changed.
   * <p>
   * The flag is set to <code>true</code> when user edits mnemoscheme content.
   * <p>
   * {@link #setCurrentConfig(IVedScreenCfg)} and {@link #setChanged(boolean)} may set changed flag to
   * <code>false</code>.
   *
   * @return boolean - <code>true</code> editor content was changed, need o save
   */
  boolean isChanged();

  /**
   * Sets value of the flag {@link #isChanged()}.
   *
   * @param aState boolean - <code>false</code> to indicate that content was
   */
  void setChanged( boolean aState );

  /**
   * Returns the {@link #isChanged()} flag state change eventer.
   *
   * @return {@link IGenericChangeEventer} - the {@link #isChanged()} flag state change eventer
   */
  IGenericChangeEventer mnemoChangedEventer();

}
