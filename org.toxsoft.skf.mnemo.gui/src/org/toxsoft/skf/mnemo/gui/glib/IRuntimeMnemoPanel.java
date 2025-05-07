package org.toxsoft.skf.mnemo.gui.glib;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.gw.time.*;

/**
 * Panel to display specified mnemoscheme at runtime.
 *
 * @author hazard157, vs
 */
public interface IRuntimeMnemoPanel
    extends IPausableAnimation, IGwTimeFleetable, IRealTimeSensitive {

  /**
   * Returns the displayed mnemoscheme config.
   *
   * @return {@link IVedScreenCfg} - the mnemoscheme config
   */
  IVedScreenCfg getMnemoConfig();

  /**
   * Sets the configuration of the mnemoscheme to display.
   *
   * @param aCfg {@link IVedScreenCfg} - the mnemoscheme config or <code>null</code> for no mnemo
   */
  void setMnemoConfig( IVedScreenCfg aCfg );

  /**
   * Returns the panel implementing SWT control.
   *
   * @return {@link Control} - the SWT control
   */
  Control getControl();

}
