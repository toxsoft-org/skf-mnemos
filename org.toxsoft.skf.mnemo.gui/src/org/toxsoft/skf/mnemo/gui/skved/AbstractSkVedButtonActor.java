package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Базовый класс для создания sk акторов, обрабатывающих нажатие кнопки.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractSkVedButtonActor
    extends AbstractSkVedActor {

  /**
   * Интерфейс оработчика нажатия кнопки
   *
   * @author vs
   */
  interface IButtonClickHandler {

    /**
     * Вызывается в момент, когда произошло нажатие кнопки (not Down, but Click) и необходимо произвести соответствующие
     * действия.
     *
     * @param aVisel {@link VedAbstractVisel} - визуальный элемент, который был нажат (Clicked)
     */
    void onButtonClick( VedAbstractVisel aVisel );
  }

  private boolean activated = false;

  private IButtonClickHandler buttonHandler = null;

  private Cursor handCursor;

  private Cursor prevCursor = null;

  protected AbstractSkVedButtonActor( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );
    handCursor = cursorManager().getCursor( ECursorType.HAND );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Усанавливает обработчик нажатия кнопки.<br>
   *
   * @param aButtonHandler {@link IButtonClickHandler} - обработчик нажатия кнопки м.б. <b>null</b>
   */
  public void setButtonClickHandler( IButtonClickHandler aButtonHandler ) {
    buttonHandler = aButtonHandler;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    boolean retVal = false;
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
    if( visel.props().getValobj( ViselButton.PROPID_STATE ) == EButtonViselState.DISABLED ) {
      restorCursor();
      return false;
    }
    ID2Point p = vedScreen().view().coorsConverter().swt2Visel( aCoors, visel );
    if( visel.isYours( p ) ) {
      visel.props().setBool( ViselButton.PROPID_HOVERED, true );
      setHandCursor();
      retVal = true;
    }
    else {
      visel.props().setBool( ViselButton.PROPID_HOVERED, false );
      restorCursor();
      retVal = false;
    }
    vedScreen().view().redraw();
    return retVal;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
    if( visel.props().getValobj( ViselButton.PROPID_STATE ) == EButtonViselState.DISABLED ) {
      return false;
    }
    if( aButton == ETsMouseButton.LEFT && aState == 0 ) {
      visel = findMyVisel( aCoors );
      if( visel == null || visel.props().getValobj( ViselButton.PROPID_STATE ) == EButtonViselState.DISABLED ) {
        setActivated( false );
        return false;
      }
      visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.PRESSED );
      setActivated( true );
      return true;
    }
    return false;
  }

  @Override
  public boolean onMouseUp( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    boolean retVal = false;
    if( activated ) {
      VedAbstractVisel visel = findMyVisel( aCoors );
      if( visel != null ) {
        visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
        retVal = true;
        setActivated( false );
        if( buttonHandler != null ) {
          buttonHandler.onButtonClick( visel );
        }
        // TsDialogUtils.info( vedScreen().view().getControl().getShell(), "Ти нажяль!" );
      }
      visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
      visel.props().setBool( ViselButton.PROPID_HOVERED, false );
    }
    setActivated( false );
    return retVal;
  }

  @Override
  public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    if( activated ) {
      VedAbstractVisel visel = findMyVisel( aCoors );
      if( visel != null ) {
        visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.PRESSED );
      }
      else {
        visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
        visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
        // visel.props().setBool( ButtonVisel.PROPID_HOVERED, false );
      }
    }
    return activated;
  }

  @Override
  public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    boolean retVal = false;
    if( activated ) {
      VedAbstractVisel visel = findMyVisel( aCoors );
      if( visel != null ) {
        visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
        visel.props().setBool( ViselButton.PROPID_HOVERED, false );
        retVal = true;
        if( buttonHandler != null ) {
          buttonHandler.onButtonClick( visel );
        }
        // TsDialogUtils.info( vedScreen().view().getControl().getShell(), "Ти нажяль!" );
      }
      else {
        visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
        visel.props().setBool( ViselButton.PROPID_HOVERED, false );
      }
    }
    setActivated( false );
    return retVal;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private VedAbstractVisel findMyVisel( ITsPoint aCoors ) {
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
    if( visel != null ) {
      ID2Point p = vedScreen().view().coorsConverter().swt2Visel( aCoors, visel );
      if( visel.isYours( p ) ) {
        return visel;
      }
    }
    return null;
  }

  private void setActivated( boolean aActivated ) {
    activated = aActivated;
    if( activated ) {
      setHandCursor();
    }
    else {
      restorCursor();
    }
  }

  private void setHandCursor() {
    // prevCursor = vedScreen().view().getControl().getCursor();
    vedScreen().view().getControl().setCursor( handCursor );
  }

  private void restorCursor() {
    vedScreen().view().getControl().setCursor( prevCursor );
  }

}
