package org.toxsoft.skf.mnemo.mned.lite.glib;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.impl.*;

/**
 * Обработчик пользовательского ввода для создания RtControl'ей в зависимости от "запавшей" кнопки на палитре.<br>
 *
 * @author vs
 */
public class PaletteSelectionManager
    extends VedAbstractUserInputHandler {

  private final RtControlsPaletteBar palette;
  private final IRtControlsManager   rtControlsManager;

  /**
   * Конструктор.
   *
   * @param aScreen {@link IVedScreen} - экран редактора
   * @param aPalette {@link RtControlsPaletteBar} - палитра компонент
   * @param aManager {@link IRtControlsManager} - менеджер RtControl'ей
   */
  public PaletteSelectionManager( IVedScreen aScreen, RtControlsPaletteBar aPalette, IRtControlsManager aManager ) {
    super( aScreen );
    palette = aPalette;
    rtControlsManager = aManager;
    palette.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
      if( palette.selectedItem() == null ) {
        vedScreen().view().setCursor( null );
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    if( palette.selectedItem() == null ) {
      return false;
    }
    Cursor cursor = palette.cursor();
    vedScreen().view().setCursor( cursor );
    return cursor != null;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT && (aState & SWT.MODIFIER_MASK) == 0 ) {
      IRtControlsPaletteEntry pe = palette.selectedItem();
      if( pe != null ) {
        createRtControlAt( pe.itemCfg(), aCoors );
      }
    }
    palette.deselectAllButtons();
    vedScreen().view().setCursor( null );
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static boolean hasItemWithName( String aName, IStridablesList<VedAbstractVisel> aItemsList ) {
    for( IVedItem item : aItemsList ) {
      if( item.nmName().equals( aName ) ) {
        return true;
      }
    }
    return false;
  }

  private void createRtControlAt( IRtControlCfg aCfg, ITsPoint aCursorCoors ) {
    // IRtControlFactoriesRegistry fr = vedScreen().tsContext().get( IRtControlFactoriesRegistry.class );
    // IRtControlFactory factory = fr.get( aCfg.factoryId() );
    // IStridablesList<VedAbstractVisel> visels = vedScreen().model().visels().list();
    // // generate ID
    // String id;
    // int counter = 0;
    // String prefix = StridUtils.getLast( aCfg.factoryId() );
    // prefix = prefix.toLowerCase().substring( 0, 1 ) + prefix.substring( 1 ); // convert first char to lower case
    // String name;
    // do {
    // id = prefix + Integer.toString( ++counter ); // "prefixNN"
    // name = factory.nmName() + ' ' + Integer.toString( counter ); // "Factory name NN"
    // } while( visels.hasKey( id ) || hasItemWithName( name, visels ) );
    // // create config
    //
    // IOptionSetEdit params = new OptionSet();
    // params.setDouble( PROPID_X, aCursorCoors.x() );
    // params.setDouble( PROPID_Y, aCursorCoors.y() );
    // RtControlCfg newCfg = new RtControlCfg( id, factory.id(), params );
    //
    // factory.create( newCfg, (VedScreen)vedScreen() );

    IRtControlCfg newCfg = rtControlsManager.prepareFromTemplate( aCfg );
    ((RtControlCfg)newCfg).params().setDouble( PROPID_X, aCursorCoors.x() );
    ((RtControlCfg)newCfg).params().setDouble( PROPID_Y, aCursorCoors.y() );
    rtControlsManager.create( newCfg );

    // EVedItemKind kind = aCfg.kind();
    // switch( kind ) {
    // case VISEL: {
    // VedItemCfg viselCfg = vedScreen().model().visels().prepareFromTemplate( aCfg );
    // viselCfg.propValues().setDouble( PROP_X, aCursorCoors.x() );
    // viselCfg.propValues().setDouble( PROP_Y, aCursorCoors.y() );
    // vedScreen().model().visels().create( viselCfg );
    // vedScreen().view().redraw();
    // break;
    // }
    // case ACTOR: {
    // VedItemCfg actorCfg = vedScreen().model().actors().prepareFromTemplate( aCfg );
    // vedScreen().model().actors().create( actorCfg );
    // break;
    // }
    // default:
    // throw new TsNotAllEnumsUsedRtException( kind.nmName() );
    // }
  }

}
