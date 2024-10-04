package org.toxsoft.skf.mnemo.gui.glib;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.gw.time.*;
import org.toxsoft.skf.mnemo.lib.*;

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
   * @return {@link ISkMnemoCfg} - the mnemoscheme config
   */
  ISkMnemoCfg getMnemoConfig();

  /**
   * Sets the configuration of the mnemoscheme to display.
   *
   * @param aCfg {@link ISkMnemoCfg} - the mnemoscheme config or <code>null</code> for no mnemo
   */
  void setMnemoConfig( ISkMnemoCfg aCfg );

  /**
   * Returns the panel implementing SWT control.
   *
   * @return {@link Control} - the SWT control
   */
  Control getControl();

}
