package org.toxsoft.skf.mnemo.gui.glib;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
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

  private IVedScreenCfg mnemoCfg = null;

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
    theCanvas = new Canvas( this, SWT.DOUBLE_BUFFERED );
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

    // DEBUG --- load mnemo from file by mouse double click on an empty area
    // theCanvas.addMouseListener( new MouseListener() {
    //
    // @Override
    // public void mouseUp( MouseEvent aE ) {
    // // nop
    // }
    //
    // @Override
    // public void mouseDown( MouseEvent aE ) {
    // // nop
    // }
    //
    // @Override
    // public void mouseDoubleClick( MouseEvent aE ) {
    // pause();
    // File f = TsRcpDialogUtils.askFileOpen( getShell(), lastPath, new StringArrayList( SCREEN_CFG_FILE_AST_EXT ) );
    // if( f != null ) {
    // IVedScreenCfg screenCfg = VedScreenCfg.KEEPER.read( f );
    // VedScreenUtils.setVedScreenConfig( vedScreen, screenCfg );
    // lastPath = f.getAbsolutePath();
    // vedEnv.restart();
    // resume();
    // }
    // }
    // } );
    // ---

  }

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aParent {@link Composite} - parent component
   * @param aContext {@link ITsGuiContext} - the context
   * @param aStyle int - SWT widget style
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public RuntimeMnemoPanel( Composite aParent, ITsGuiContext aContext, int aStyle ) {
    super( aParent, aContext );
    setLayout( new BorderLayout() );
    theCanvas = new Canvas( this, aStyle | SWT.DOUBLE_BUFFERED );
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
  }

  // ------------------------------------------------------------------------------------
  // Control
  //

  @Override
  public void setBackground( Color aColor ) {
    super.setBackground( aColor );
    theCanvas.setBackground( aColor );
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
    vedScreen.setActorsEnabled( false );
  }

  @Override
  public void resume() {
    vedScreen.resume();
    vedScreen.setActorsEnabled( true );
    vedEnv.restart();
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
  public IVedScreenCfg getMnemoConfig() {
    return mnemoCfg;
  }

  @Override
  public void setMnemoConfig( IVedScreenCfg aCfg ) {
    pause();
    if( mnemoCfg != null ) {
      VedScreenUtils.setVedScreenConfig( vedScreen, IVedScreenCfg.NONE );
      mnemoCfg = null;
    }
    mnemoCfg = aCfg;
    if( mnemoCfg != null ) {
      try {
        VedScreenUtils.setVedScreenConfig( vedScreen, aCfg );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
        VedScreenUtils.setVedScreenConfig( vedScreen, IVedScreenCfg.NONE );
      }
    }
    resume();
  }

  @Override
  public Control getControl() {
    return this;
  }

}
