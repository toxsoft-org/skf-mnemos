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
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.glib.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * @author dima
 */
public class PopupMnemoDialog
    extends Dialog {

  private final ITsGuiContext context;
  private final ISkMnemoCfg   mnemoCfg;
  private final Skid          masterSkid;
  private IRuntimeMnemoPanel  mnemoPanel;

  /**
   * @param aParentShell parent Shell
   * @param aContext GUI context
   * @param aMnemoCfg mnemo configuraion
   * @param aMasterObj {@link Skid} - master-object can be <code>null</code>
   */
  public PopupMnemoDialog( final Shell aParentShell, ITsGuiContext aContext, ISkMnemoCfg aMnemoCfg, Skid aMasterObj ) {
    super( aParentShell );
    context = aContext;
    mnemoCfg = aMnemoCfg;
    masterSkid = aMasterObj;
  }

  @Override
  protected Control createDialogArea( final Composite parent ) {
    final Composite container = (Composite)super.createDialogArea( parent );
    container.setLayout( new BorderLayout() );
    // открываем мнемосхему
    mnemoPanel = new RuntimeMnemoPanel( container, new TsGuiContext( context ) );
    // обработка мастер-объекта
    // if( aMasterObjSkid != Skid.NONE ) {
    // // ISkConnection skConn = vedScreen.v
    // // ISimpleResolverFactoriesRegistry resRegistry = tsContext().get( ISimpleResolverFactoriesRegistry.class );
    // // MnemoMasterObjectManager mmoManager = new MnemoMasterObjectManager( skConn(), resRegistry );
    // // Ugwi ugwi = UgwiKindSkSkid.makeUgwi( masterObj.classId(), masterObj.strid() );
    // // IVedScreenCfg scrCfg = VedScreenUtils.getVedScreenConfig( vedScreen );
    // // IVedScreenCfg newCfg = mmoManager.processMasterObject( ugwi, scrCfg, skConn() );
    // }
    mnemoPanel.setMnemoConfig( VedScreenCfg.cfgFromString( mnemoCfg.cfgData() ) );
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
