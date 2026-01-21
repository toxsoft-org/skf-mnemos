package org.toxsoft.skf.mnemo.mned.lite.glib;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.toxsoft.core.tsgui.bricks.actions.asp.*;

/**
 * Action - save mnemocheme
 *
 * @author vs
 */
public class AspSaveMnemo
    extends AbstractSingleActionSetProvider {

  private final IMnemoEditorPanel editorPanel;

  /**
   * Constructor.
   *
   * @param aEditorPanel {@link IMnemoEditorPanel} - editor panel
   */
  public AspSaveMnemo( IMnemoEditorPanel aEditorPanel ) {
    super( ACDEF_SAVE );
    editorPanel = aEditorPanel;
  }

  @Override
  public void run() {
    if( editorPanel.externalHandler() != null ) {
      editorPanel.externalHandler().handleAction( ACTID_SAVE );
    }
  }

  @Override
  protected boolean doIsActionEnabled() {
    return editorPanel.isChanged();
  }

}
