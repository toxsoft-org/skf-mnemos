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
   * Интерфейс оработчика "клика" кнопки
   *
   * @author vs
   */
  public interface IButtonClickHandler {

    /**
     * Вызывается в момент, когда произошло нажатие кнопки (not Down, but Click) и необходимо произвести соответствующие
     * действия.
     *
     * @param aVisel {@link VedAbstractVisel} - визуальный элемент, который был нажат (Clicked)
     */
    void onButtonClick( VedAbstractVisel aVisel );
  }

  /**
   * Интерфейс оработчика нажатия/отпускания кнопки
   *
   * @author vs
   */
  public interface IButtonUpDownHandler {

    /**
     * Вызывается в момент, когда произошло нажатие кнопки (not Click, but Down) и необходимо произвести соответствующие
     * действия.
     *
     * @param aVisel {@link VedAbstractVisel} - визуальный элемент, который был нажат
     */
    void onButtonDown( VedAbstractVisel aVisel );

    /**
     * Вызывается в момент, когда произошло отжатие кнопки (not Click, but Up) и необходимо произвести соответствующие
     * действия.
     *
     * @param aVisel {@link VedAbstractVisel} - визуальный элемент, который был отажат
     */
    void onButtonUp( VedAbstractVisel aVisel );
  }

  private boolean activated = false;

  private IButtonClickHandler clickHandler = null;

  private IButtonUpDownHandler upDownHandler = null;

  private Cursor handCursor;

  private Cursor prevCursor = null;

  private String tooltipText = null;

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
    clickHandler = aButtonHandler;
  }

  /**
   * Усанавливает обработчик нажатия/отжатия кнопки.<br>
   *
   * @param aButtonHandler {@link IButtonUpDownHandler} - обработчик нажатия/отжатия кнопки м.б. <b>null</b>
   */
  public void setButtonUpDownHandler( IButtonUpDownHandler aButtonHandler ) {
    upDownHandler = aButtonHandler;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  VedAbstractVisel currVisel = null;

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    boolean retVal = false;
    String viselId = props().getStr( PROPID_VISEL_ID );
    if( viselId.isBlank() ) {
      return false;
    }
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( viselId );
    if( visel == null ) {
      return false;
    }

    if( visel.props().getValobj( ViselButton.PROPID_STATE ) == EButtonViselState.DISABLED ) {
      restoreCursor();
      return false;
    }
    ID2Point p = vedScreen().view().coorsConverter().swt2Visel( aCoors, visel );
    if( visel.isYours( p ) ) {
      visel.props().setBool( ViselButton.PROPID_HOVERED, true );
      setHandCursor();
      retVal = true;
      if( currVisel == null ) {
        currVisel = visel;
        onMouseIn();
      }
    }
    else {
      visel.props().setBool( ViselButton.PROPID_HOVERED, false );
      restoreCursor();
      retVal = false;
      if( currVisel != null ) {
        onMouseOut();
        currVisel = null;
      }
    }
    vedScreen().view().redraw();
    return retVal;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
    if( visel != null ) {
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
        notifyDown( visel );
        return true;
      }
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
        if( clickHandler != null ) {
          clickHandler.onButtonClick( visel );
        }
        // TsDialogUtils.info( vedScreen().view().getControl().getShell(), "Ти нажяль!" );
        notifyUp( visel );
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
        if( currVisel == null ) {
          notifyDown( visel );
          currVisel = visel;
          onMouseIn();
        }
      }
      else {
        visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
        visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
        if( currVisel != null ) {
          notifyUp( visel );
          onMouseOut();
          currVisel = null;
        }
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
        if( clickHandler != null ) {
          clickHandler.onButtonClick( visel );
        }
        onMouseUp( aSource, aDragInfo.button(), aState, aCoors, aDragInfo.starterControl() );
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
  // To use
  //

  void setTooltipText( String aText ) {
    tooltipText = aText;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  protected void onMouseIn() {
    vedScreen().view().getControl().setToolTipText( tooltipText );
  }

  protected void onMouseOut() {
    vedScreen().view().getControl().setToolTipText( null );
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
      restoreCursor();
    }
  }

  private void setHandCursor() {
    vedScreen().view().getControl().setCursor( handCursor );
  }

  private void restoreCursor() {
    vedScreen().view().getControl().setCursor( prevCursor );
  }

  private void notifyUp( VedAbstractVisel aVisel ) {
    if( upDownHandler != null ) {
      upDownHandler.onButtonUp( aVisel );
    }
  }

  private void notifyDown( VedAbstractVisel aVisel ) {
    if( upDownHandler != null ) {
      upDownHandler.onButtonDown( aVisel );
    }
  }
}
