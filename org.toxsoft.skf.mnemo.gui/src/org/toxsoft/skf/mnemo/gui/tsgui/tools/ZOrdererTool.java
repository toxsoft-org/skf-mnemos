package org.toxsoft.skf.mnemo.gui.tsgui.tools;

import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;

/**
 * Инструмент задания Z-порядка среди выделенных элементов.
 *
 * @author vs
 */
public class ZOrdererTool
    extends VedAbstractTool {

  /**
   * Идентификатор "инструмента"
   */
  public static final String TOOLID = "toolZOrderer"; //$NON-NLS-1$

  private final static String CURSOR_NAME = "zOrderCursor"; //$NON-NLS-1$

  static class ZNumberDecorator
      extends VedAbstractDecorator {

    private final VedAbstractVisel visel;

    Color colorWhite = new Color( 255, 255, 255 );
    Color colorBlack = new Color( 0, 0, 0 );

    final int zNumber;

    ZNumberDecorator( VedAbstractVisel aVisel, int aZNumber, IVedScreen aVedScreen ) {
      super( aVedScreen );
      visel = TsNullArgumentRtException.checkNull( aVisel );
      zNumber = aZNumber;
    }

    @Override
    public void paint( ITsGraphicsContext aPaintContext ) {
      aPaintContext.gc().setForeground( colorBlack );
      aPaintContext.gc().setBackground( colorWhite );
      aPaintContext.gc().drawText( TsLibUtils.EMPTY_STRING + (zNumber + 1), 0, 0, false );
    }

    @Override
    public boolean isYours( double aX, double aY ) {
      return false;
    }

    @Override
    public ID2Rectangle bounds() {
      return null;
    }

    VedAbstractVisel visel() {
      return visel;
    }
  }

  class InputHandler
      extends VedAbstractUserInputHandler {

    public InputHandler( IVedScreen aScreen ) {
      super( aScreen );
    }

    @Override
    public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
      if( isActive() ) {
        if( aCode == SWT.ESC ) { // Esc pressed
          removeDecorators();
          ZOrdererTool.this.setActive( false );
          vedScreen().view().redraw();
          return true;
        }
        if( aCode == SWT.CR ) { // Enter pressed
          reorderVisels();
          removeDecorators();
          ZOrdererTool.this.setActive( false );
          vedScreen().view().redraw();
          return true;
        }
      }
      return false;
    }

    @Override
    public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
      VedAbstractVisel item = VedScreenUtils.itemByPoint( aCoors.x(), aCoors.y(), vedScreen(), false );
      if( item != null && selectionManager.isSelected( item.id() ) && !decorators.keys().hasElem( item.id() ) ) {
        vedScreen().view().setCursor( cursor );
      }
      else {
        vedScreen().view().setCursor( stopCursor() );
      }
      return false;
    }

    @Override
    public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
      VedAbstractVisel item = VedScreenUtils.itemByPoint( aCoors.x(), aCoors.y(), vedScreen(), false );
      if( item != null && selectionManager.isSelected( item.id() ) && !decorators.keys().hasElem( item.id() ) ) {
        ZNumberDecorator zd = new ZNumberDecorator( item, decorators.size(), vedScreen() );
        vedScreen().model().viselDecoratorsAfter( item.id() ).add( zd );
        decorators.put( item.id(), zd );
        vedScreen().view().redraw();
      }
      return false;
    }
  }

  private final InputHandler inputHandler;

  private final IVedViselSelectionManager selectionManager;

  private final IVedViselsMasterSlaveRelationsManager msManager;

  private Cursor cursor = null;

  private Cursor currCursor = null;

  IStringMapEdit<ZNumberDecorator> decorators = new StringMap<>();

  /**
   * Конструктор.
   *
   * @param aSelectionManager {@link IVedViselSelectionManager} - менеджер выделения
   * @param aMsManager {@link IVedViselsMasterSlaveRelationsManager} - менеджер отношений master-slave
   * @param aVedScreen {@link IVedScreen} - экран редактора
   */
  public ZOrdererTool( IVedViselSelectionManager aSelectionManager, IVedViselsMasterSlaveRelationsManager aMsManager,
      IVedScreen aVedScreen ) {
    super( TOOLID, "Z-Orderer", "", aVedScreen );
    selectionManager = aSelectionManager;
    msManager = aMsManager;
    inputHandler = new InputHandler( aVedScreen );
    ITsCursorManager cursorManager = cursorManager();
    if( !cursorManager.hasCursor( CURSOR_NAME ) ) {
      Image cursorImage;
      String pluginId = Activator.PLUGIN_ID;
      cursorImage = TsIconManagerUtils.imageDescriptorFromPlugin( pluginId, CURSOR_IMG_ZORDER ).createImage();
      // Cursor cursor = TsSingleFiltersourcingUtils.Cursor_Cursor( display, cursorImage.getImageData(), 0, 0 );
      cursor = new Cursor( getDisplay(), cursorImage.getImageData(), 0, 0 );
      cursorManager.putCursor( CURSOR_NAME, cursor );
      cursorImage.dispose();
    }
    else {
      cursor = cursorManager.findCursor( CURSOR_NAME );
    }
  }

  // ------------------------------------------------------------------------------------
  // IIconIdabel
  //

  @Override
  public String iconId() {
    return null; // TODO what icon?
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractTool
  //

  @Override
  protected boolean doCanActivate() {
    if( selectionManager.selectedViselIds().size() > 1 ) {
      if( !msManager.areTheySiblings( selectionManager.selectedViselIds() ) ) {
        TsDialogUtils.warn( getShell(), "Изменение z-порядка недопустимо. Элементы принадлежат разным родителям!" );
        return false;
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean captureInput() {
    return true;
  }

  @Override
  public Cursor cursor() {
    return currCursor;
  }

  @Override
  public VedAbstractUserInputHandler inputHandler() {
    return inputHandler;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void removeDecorators() {
    for( String id : decorators.keys() ) {
      vedScreen().model().viselDecoratorsAfter( id ).remove( decorators.getByKey( id ) );
    }
    decorators.clear();
  }

  void reorderVisels() {
    try {
      vedScreen().model().visels().eventer().pauseFiring();
      if( decorators.size() > 1 ) {
        IListReorderer<VedAbstractVisel> reorderer = vedScreen().model().visels().reorderer();
        int minIdx = vedScreen().model().visels().list().keys().indexOf( decorators.keys().first() );
        for( String id : decorators.keys() ) {
          int currIdx = vedScreen().model().visels().list().keys().indexOf( id );
          if( currIdx < minIdx ) {
            minIdx = currIdx;
          }
        }
        int idx = minIdx;
        for( String id : decorators.keys() ) {
          int oldIdx = vedScreen().model().visels().list().keys().indexOf( id );
          int zNumber = decorators.getByKey( id ).zNumber;
          reorderer.move( oldIdx, idx + zNumber );
        }
      }
    }
    finally {
      vedScreen().model().visels().eventer().resumeFiring( true );
    }
  }

  // void reorderVisels() {
  // if( decorators.size() > 1 ) {
  // IListReorderer<VedAbstractVisel> reorderer = vedScreen().model().visels().reorderer();
  // int maxIdx = vedScreen().model().visels().list().keys().indexOf( decorators.keys().first() );
  // for( String id : decorators.keys() ) {
  // int currIdx = vedScreen().model().visels().list().keys().indexOf( id );
  // if( currIdx > maxIdx ) {
  // maxIdx = currIdx;
  // }
  // }
  // int idx = maxIdx;
  // for( String id : decorators.keys() ) {
  // int oldIdx = vedScreen().model().visels().list().keys().indexOf( id );
  // int zNumber = decorators.getByKey( id ).zNumber;
  // reorderer.move( oldIdx, idx + zNumber );
  // idx--;
  // }
  // }
  // }

}
