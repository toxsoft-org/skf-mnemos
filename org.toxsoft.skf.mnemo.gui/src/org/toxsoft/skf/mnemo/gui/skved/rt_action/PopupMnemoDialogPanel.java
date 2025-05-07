package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.rt_action.ISkResources.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.glib.*;

/**
 * Panel to show popup mnemo
 *
 * @author dima
 */
public class PopupMnemoDialogPanel
    extends AbstractTsDialogPanel<IVedScreenCfg, Skid> {

  private IRuntimeMnemoPanel mnemoPanel;
  private IVedScreenCfg      mnemoCfg;

  PopupMnemoDialogPanel( Composite aParent, TsDialog<IVedScreenCfg, Skid> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    // создаем мнемосхему
    mnemoPanel = new RuntimeMnemoPanel( this, new TsGuiContext( tsContext() ) );
  }

  @Override
  protected void doSetDataRecord( IVedScreenCfg aData ) {
    if( aData != null ) {
      mnemoCfg = aData;
      mnemoPanel.setMnemoConfig( mnemoCfg );
      mnemoPanel.resume();
      this.pack();
    }
  }

  @Override
  protected IVedScreenCfg doGetDataRecord() {
    return null;
  }

  /**
   * @param aMnemoCfg mnemo config
   * @return initial size of the dialog
   */
  protected static TsPoint getInitialSize( IVedScreenCfg aMnemoCfg ) {
    IVedCanvasCfg canvasCfg = aMnemoCfg.canvasCfg();
    // TODO как учесть высоту зоны кнопок
    return new TsPoint( (int)(canvasCfg.size().x()) + 10, (int)(canvasCfg.size().y()) + 50 );
  }

  /**
   * Create and popup dialog containing mnemo
   *
   * @param aAppContext - app context
   * @param aMnemoCfg - mnemo configuarion
   */
  public static final void showPopMnemo( IEclipseContext aAppContext, IVedScreenCfg aMnemoCfg ) {
    IDialogPanelCreator<IVedScreenCfg, Skid> creator = PopupMnemoDialogPanel::new;
    IVedScreenCfg appInfo = aMnemoCfg;
    ITsGuiContext ctx = new TsGuiContext( aAppContext );
    TsDialogInfo cdi =
        // TODO pass master Skid, show mnemo name and description
        new TsDialogInfo( ctx, ctx.get( Shell.class ), DLG_C_POPUP_MNEMO, DLG_T_POPUP_MNEMO, DF_NO_APPROVE );
    cdi.setMinSize( getInitialSize( aMnemoCfg ) );
    TsDialog<IVedScreenCfg, Skid> d = new TsDialog<>( cdi, appInfo, null, creator );
    d.execData();
  }

}
