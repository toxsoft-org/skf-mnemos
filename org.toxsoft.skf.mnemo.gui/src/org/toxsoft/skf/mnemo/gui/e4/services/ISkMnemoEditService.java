package org.toxsoft.skf.mnemo.gui.e4.services;

import org.toxsoft.skf.mnemo.gui.km5.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * Mnemoschemes editing service.
 * <p>
 * Service is used by {@link SkMnemoCfgM5Model} GUI panels.
 * <p>
 * Service implementation must be created and put in context by the editor.
 *
 * @author hazard157
 */
public interface ISkMnemoEditService {

  /**
   * Opens the specified mnemoscheme for editing.
   *
   * @param aMnemoCfg {@link ISkMnemoCfg} - the mnemo to edit
   */
  void openMnemoForEditing( ISkMnemoCfg aMnemoCfg );

  void openMnemoForEditingLite( ISkMnemoCfg aMnemoCfg );

  void openMnemoForEditingPro( ISkMnemoCfg aMnemoCfg );

}
