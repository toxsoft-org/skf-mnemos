package org.toxsoft.skf.mnemo.skide.e4.uiparts;

import java.io.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.skf.mnemo.gui.e4.services.*;
import org.toxsoft.skf.mnemo.gui.glib.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.skide.glib.*;

/**
 * Mnemoscheme editor UIpart.
 * <p>
 * Contain {@link IMnemoEditorPanel} as a mnemoscheme editor.
 * <p>
 * Multiple instances of this UIpart is managed by {@link ISkMnemoEditService}. This UIpart remembers the source of the
 * mnemoscheme configuration to save it back to the source. The source may be the Sk-connection or the file, depends on
 * which method was called {@link #setMnemoCfg(ISkMnemoCfg)} or {@link #setMnemoCfg(File, IVedScreenCfg)} respectively.
 * One of mentioned method must be called before user starts working with this UIpart.
 *
 * @author hazard157
 */
public class UipartSkMnemoRuntime
    extends MwsAbstractPart {

  /**
   * TODO prevent UIpart close when editor is dirty<br>
   */

  IRuntimeMnemoPanel panel;

  ISkMnemoCfg skMnemocfg = null; // non-null value means mnemoscheme is loaded from Skconnection
  File        mnemoFile  = null; // non-null value means mnemoscheme was loaded from the file

  @Override
  protected void doInit( Composite aParent ) {
    panel = new RuntimeMnemoPanel( aParent, tsContext() );
  }

}
