package org.toxsoft.skf.mnemo.gui.tsgui.tools;

import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.skf.mnemo.gui.*;

/**
 * Базовый класс "инструментов" визуального редактора
 *
 * @author vs
 */
public abstract class VedAbstractTool
    implements IVedTool, ITsGuiContextable {

  private static final String STOP_CURSOR_NAME = "stopCursor"; //$NON-NLS-1$

  boolean active = false;

  private final IVedScreen vedScreen;

  private final GenericChangeEventer eventer;

  private final String id;

  private final String name;

  private final String description;

  private Cursor stopCursor = null;

  VedAbstractTool( String aId, String aName, String aDescription, IVedScreen aVedScreen ) {
    id = aId;
    name = aName;
    description = aDescription;
    vedScreen = aVedScreen;
    eventer = new GenericChangeEventer( this );
    if( !cursorManager().hasCursor( STOP_CURSOR_NAME ) ) {
      Image cursorImage;
      String pluginId = Activator.PLUGIN_ID;
      cursorImage = TsIconManagerUtils.imageDescriptorFromPlugin( pluginId, CURSOR_IMG_STOP ).createImage();
      // Cursor cursor = TsSingleFiltersourcingUtils.Cursor_Cursor( display, cursorImage.getImageData(), 0, 0 );
      stopCursor = new Cursor( getDisplay(), cursorImage.getImageData(), 0, 0 );
      cursorManager().putCursor( STOP_CURSOR_NAME, stopCursor );
      cursorImage.dispose();
    }
    else {
      stopCursor = cursorManager().findCursor( STOP_CURSOR_NAME );
    }
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IVedTool
  //

  @Override
  public final boolean isActive() {
    return active;
  }

  @Override
  public final void setActive( boolean aIsActive ) {
    if( aIsActive ) {
      if( canActivate() ) {
        active = true;
        onActivated();
      }
    }
    else {
      active = false;
      onDeactivated();
    }
  }

  @Override
  public final IGenericChangeEventer eventer() {
    return eventer;
  }

  @Override
  public final boolean canActivate() {
    if( !isActive() ) {
      return doCanActivate();
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // To use
  //

  protected IVedScreen vedScreen() {
    return vedScreen;
  }

  protected Cursor stopCursor() {
    return stopCursor;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract boolean doCanActivate();

  @Override
  public abstract boolean captureInput();

  @Override
  public abstract VedAbstractUserInputHandler inputHandler();

  @Override
  public abstract Cursor cursor();

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected void onActivated() {
    eventer.fireChangeEvent();
  }

  protected void onDeactivated() {
    eventer.fireChangeEvent();
  }

}
