/**
 *
 */
package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.skf.mnemo.gui.glib.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * @author dima
 */
public class PopupMnemoDialog
    extends Dialog {

  private final ITsGuiContext context;
  private final ISkMnemoCfg   mnemoCfg;
  private IRuntimeMnemoPanel  mnemoPanel;

  /**
   * @param aParentShell parent Shell
   * @param aContext GUI context
   * @param aMnemoCfg mnemo configuraion
   */
  public PopupMnemoDialog( final Shell aParentShell, ITsGuiContext aContext, ISkMnemoCfg aMnemoCfg ) {
    super( aParentShell );
    context = aContext;
    mnemoCfg = aMnemoCfg;
  }

  @Override
  protected Control createDialogArea( final Composite parent ) {
    final Composite container = (Composite)super.createDialogArea( parent );
    container.setLayout( new BorderLayout() );
    // открываем мнемосхему
    mnemoPanel = new RuntimeMnemoPanel( container, new TsGuiContext( context ) );
    mnemoPanel.setMnemoConfig( mnemoCfg );
    mnemoPanel.resume();

    return container;
  }

  /**
   * Create contents of the button bar.
   *
   * @param aParent parent
   */
  @Override
  protected void createButtonsForButtonBar( final Composite aParent ) {
    // createButton( aParent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true );
    // createButton( aParent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false );
  }

  /**
   * Return the initial size of the dialog.
   */
  @Override
  protected Point getInitialSize() {

    IVedScreenCfg vedCfg = VedScreenCfg.KEEPER.str2ent( mnemoCfg.cfgData() );
    IVedCanvasCfg canvasCfg = vedCfg.canvasCfg();
    return new Point( (int)(canvasCfg.size().x()) + 10, (int)(canvasCfg.size().y()) + 10 );

    // return new Point( 745, 550 );
  }

}
