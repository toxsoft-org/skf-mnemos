package org.toxsoft.skf.mnemo.gui.tsgui.tools;

import org.eclipse.swt.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;

/**
 * Менеджер "инструментов" визуального редактора.
 *
 * @author vs
 */
public class VedToolsManager {

  // IList<E>

  IGenericChangeListener toolListener = aSource -> {
    IVedTool tool = (IVedTool)aSource;
    if( tool.isActive() ) {
      activateTool( tool );
    }
    else {
      deactivateTool( tool );
    }
  };

  void activateTool( IVedTool aTool ) {
    activeTool = aTool;
    if( aTool.captureInput() ) {
      for( VedAbstractUserInputHandler handler : vedScreen.model().screenHandlersAfter().list() ) {
        handler.setActive( false );
      }
      for( VedAbstractUserInputHandler handler : vedScreen.model().screenHandlersBefore().list() ) {
        handler.setActive( false );
      }
    }
    vedScreen.model().screenHandlersBefore().add( activeTool.inputHandler() );
    activeTool.inputHandler().setActive( true );
    vedScreen.view().setCursor( activeTool.cursor() );
  }

  void deactivateTool( IVedTool aTool ) {
    for( VedAbstractUserInputHandler handler : vedScreen.model().screenHandlersAfter().list() ) {
      handler.setActive( true );
    }
    for( VedAbstractUserInputHandler handler : vedScreen.model().screenHandlersBefore().list() ) {
      handler.setActive( true );
    }
    vedScreen.model().screenHandlersBefore().remove( activeTool.inputHandler() );
    activeTool.inputHandler().setActive( false );
    vedScreen.view().setCursor( null );
  }

  private final VedHotKeysManager hotKeysManager;

  private final IVedScreen vedScreen;

  private final IStridablesListEdit<IVedTool> tools = new StridablesList<>();

  HotKeyInfo zOrderKey;

  private IVedTool activeTool = null;

  public VedToolsManager( VedHotKeysManager aHotKeysManager, IVedScreen aVedScreen ) {
    hotKeysManager = aHotKeysManager;
    vedScreen = aVedScreen;

    // hotKeysManager.addHotKey( new HotKeyInfo( 0, 'o', SWT.CTRL ) );
    zOrderKey = new HotKeyInfo( 114, 'r', SWT.CTRL );

    hotKeysManager.addHotKey( zOrderKey );

    hotKeysManager.addSelectionChangedListener( ( aSource, aSelectedItem ) -> {
      if( aSelectedItem.equals( zOrderKey ) ) {
        IVedTool tool = tools.getByKey( ZOrdererTool.TOOLID );
        // if( tool.canActivate() ) {
        tool.setActive( true );
        // }
      }
    } );
  }

  public void addTool( IVedTool aTool ) {
    tools.add( aTool );
    aTool.eventer().addListener( toolListener );
  }

}
