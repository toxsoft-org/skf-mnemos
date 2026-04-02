package org.toxsoft.skf.mnemo.mned.lite.glib;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.glib.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Standard handling of the mnemo lite editor settings actions.
 * <p>
 * Handles the actions:
 * <ul>
 * <li>Canvas settings</li>
 * <li>TODO: add more actions</li>
 * </ul>
 *
 * @author vs
 */
public class AspLiteEditorSettings
    extends MethodPerActionTsActionSetProvider
    implements ITsGuiContextable {

  static final String ACTID_SCREEN_CONFIG = "screen.config"; //$NON-NLS-1$

  static final ITsActionDef ACDEF_SCREEN_CONFIG = ofPush2( ACTID_SCREEN_CONFIG, //
      STR_SCREEN_CONFIG, STR_SCREEN_CONFIG_D, ICONID_SETTINGS );

  private final IVedScreen vedScreen;

  private final IGenericChangeListener canvasChangeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      if( aSource instanceof PanelCanvasConfig ) {
        IVedCanvasCfg cfg = ((PanelCanvasConfig)aSource).getDataRecord();
        vedScreen.view().setCanvasConfig( cfg );
        vedScreen.view().redraw();
        vedScreen.view().update();
      }

    }
  };

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to handle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AspLiteEditorSettings( IVedScreen aVedScreen ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    defineAction( ACDEF_SCREEN_CONFIG, this::doConfigurateCanvas );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiConextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //
  void doConfigurateCanvas() {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.put( IGenericChangeListener.class, canvasChangeListener );
    IVedCanvasCfg canvasConfig = PanelCanvasConfig.editCanvasConfig( vedScreen.view().canvasConfig(), ctx );
    if( canvasConfig != null ) {
      vedScreen.view().setCanvasConfig( canvasConfig );
      vedScreen.view().redraw();
      vedScreen.view().update();
    }
  }

}
