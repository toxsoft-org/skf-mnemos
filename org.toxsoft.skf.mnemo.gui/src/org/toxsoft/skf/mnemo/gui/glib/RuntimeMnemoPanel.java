package org.toxsoft.skf.mnemo.gui.glib;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.skf.mnemo.gui.glib.ISkResources.*;

import java.io.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.rcp.utils.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.gui.conn.*;

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

  Canvas theCanvas;

  String lastPath = TsLibUtils.EMPTY_STRING;

  ISkVedEnvironment vedEnv;

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
    setLayout( new BorderLayout() );
    theCanvas = new Canvas( this, SWT.BORDER );
    theCanvas.setLayoutData( BorderLayout.CENTER );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    vedScreen = new VedScreen( ctx );
    vedScreen.attachTo( theCanvas );
    vedScreen.pause();
    vedScreen.view().getControl().setBackground( new Color( 255, 255, 255 ) );

    vedEnv = new SkVedEnvironment( aContext.get( ISkConnectionSupplier.class ).defConn() );
    vedScreen.tsContext().put( ISkVedEnvironment.class, vedEnv );

    guiTimersService().quickTimers().addListener( vedScreen );
    guiTimersService().slowTimers().addListener( vedScreen );

    theCanvas.addMouseListener( new MouseListener() {

      @Override
      public void mouseUp( MouseEvent aE ) {
        // TODO Auto-generated method stub
      }

      @Override
      public void mouseDown( MouseEvent aE ) {
        // TODO Auto-generated method stub
      }

      @Override
      public void mouseDoubleClick( MouseEvent aE ) {
        pause();
        File f = TsRcpDialogUtils.askFileOpen( getShell(), lastPath, new StringArrayList( SCREEN_CFG_FILE_AST_EXT ) );
        if( f != null ) {
          IVedScreenCfg screenCfg = VedScreenCfg.KEEPER.read( f );
          VedScreenUtils.setVedScreenConfig( vedScreen, screenCfg );
          lastPath = f.getAbsolutePath();
          vedEnv.restart();
          resume();
        }
      }
    } );
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
      VedScreenUtils.setVedScreenConfig( vedScreen, IVedScreenCfg.NONE );
      mnemoCfg = null;
    }
    mnemoCfg = aCfg;
    if( mnemoCfg != null ) {
      try {
        String cfgStr = mnemoCfg.cfgData();
        IVedScreenCfg vedCfg = VedScreenCfg.KEEPER.str2ent( cfgStr );
        VedScreenUtils.setVedScreenConfig( vedScreen, vedCfg );
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
