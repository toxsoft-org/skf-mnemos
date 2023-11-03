package org.toxsoft.skf.mnemo.gui.glib;

import static org.toxsoft.skf.mnemo.gui.glib.ISkResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * {@link IRuntimeMnemoPanel} implementation.
 *
 * @author hazard157
 */
public class RuntimeMnemoPanel
    extends TsPanel
    implements IRuntimeMnemoPanel {

  private final IVedScreen vedScreen;

  private ISkMnemoCfg mnemoCfg = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public RuntimeMnemoPanel( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    vedScreen = new VedScreen( ctx );
    vedScreen.pause();
  }

  // ------------------------------------------------------------------------------------
  // IPausableAnimation
  //

  @Override
  public boolean isPaused() {
    return vedScreen.isPaused();
  }

  @Override
  public void pause() {
    vedScreen.pause();
  }

  @Override
  public void resume() {
    vedScreen.resume();
  }

  // ------------------------------------------------------------------------------------
  // IGwTimeFleetable
  //

  @Override
  public void whenGwTimePassed( long aGwTime ) {
    vedScreen.whenGwTimePassed( aGwTime );
  }

  // ------------------------------------------------------------------------------------
  // IRealTimeSensitive
  //

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    vedScreen.whenRealTimePassed( aRtTime );
  }

  // ------------------------------------------------------------------------------------
  // IRuntimeMnemoPanel
  //

  @Override
  public ISkMnemoCfg getMnemoConfig() {
    return mnemoCfg;
  }

  @Override
  public void setMnemoConfig( ISkMnemoCfg aCfg ) {
    pause();
    if( mnemoCfg != null ) {
      VedEditorUtils.setVedScreenConfig( vedScreen, IVedScreenCfg.NONE );
      mnemoCfg = null;
    }
    mnemoCfg = aCfg;
    if( mnemoCfg != null ) {
      try {
        String cfgStr = mnemoCfg.cfgData();
        IVedScreenCfg vedCfg = VedScreenCfg.KEEPER.str2ent( cfgStr );
        VedEditorUtils.setVedScreenConfig( vedScreen, vedCfg );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        TsDialogUtils.error( getShell(), FMT_ERR_CANT_LOAD_MNEMO_CFG, mnemoCfg.skid().toString(), ex.getMessage() );
        return;
      }
    }
    resume();
  }

  @Override
  public Control getControl() {
    return this;
  }

}
