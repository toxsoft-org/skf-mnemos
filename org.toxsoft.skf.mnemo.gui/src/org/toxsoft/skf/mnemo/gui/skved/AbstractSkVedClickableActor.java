package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.tti.*;

/**
 * Базовый класс для создания sk акторов, обрабатывающих мышиные события. <br>
 * FIXME создано методом copy-paste из {@link AbstractSkVedButtonActor }. <br>
 * Отличается:
 * <ul>
 * <li>может быть привязан как к кнопке, так и не, например к экрану ввода НСИ;</li>
 * <li>обрабатывает mouse double click.</li>
 * </ul>
 * <p>
 *
 * @author dima
 */
public abstract class AbstractSkVedClickableActor
    extends AbstractSkVedActor {

  /**
   * ИД поля "Кнопка мыши"
   */
  protected static final String FID_KEY_MASK = "keyMask"; //$NON-NLS-1$

  protected static final ITinFieldInfo TFI_KEY_MASK = new TinFieldInfo( FID_KEY_MASK, TtiKeyMask.INSTANCE, //
      TSID_NAME, STR_KEY_MASK, //
      TSID_DESCRIPTION, STR_KEY_MASK_D //
  );

  /**
   * ИД поля "Кнопка мыши"
   */
  protected static final String FID_MOUSE_BUTTON = "mouseButton"; //$NON-NLS-1$

  protected static final ITinFieldInfo TFI_MOUSE_BUTTON = new TinFieldInfo( FID_MOUSE_BUTTON, TtiAvEnum.INSTANCE, //
      TSID_NAME, STR_MOUSE_BUTTON, //
      TSID_DESCRIPTION, STR_MOUSE_BUTTON_D, //
      TSID_KEEPER_ID, ERtActionMouseButton.KEEPER_ID //
  );

  /**
   * Интерфейс обработчика нажатия mouse кнопок
   *
   * @author dima, vs
   */
  public interface IMouseClickHandler {

    /**
     * Вызывается в момент, когда произошел Click и необходимо произвести соответствующие действия.
     *
     * @param aVisel {@link VedAbstractVisel} - визуальный элемент, на котором был Click
     * @param aButton {@link ETsMouseButton} - the mouse button
     * @param aCoors {@link ITsPoint} - координаты курсора при нажатии
     * @param aState int - SWT код состояния управляющих клавиш Shift, Alt, Ctrl
     */
    void onClick( VedAbstractVisel aVisel, ETsMouseButton aButton, ITsPoint aCoors, int aState );

    /**
     * Вызывается в момент, когда произошел DoubleClick и необходимо произвести соответствующие действия
     *
     * @param aVisel {@link VedAbstractVisel} - визуальный элемент, на котором был DoubleClick
     * @param aButton {@link ETsMouseButton} - кнопка мыши
     * @param aCoors {@link ITsPoint} - координаты курсора при нажатии
     * @param aState int - SWT код состояния управляющих клавиш Shift, Alt, Ctrl
     */
    void onDoubleClick( VedAbstractVisel aVisel, ETsMouseButton aButton, ITsPoint aCoors, int aState );
  }

  private boolean activated = false;

  private IMouseClickHandler clickHandler = null;

  private Cursor handCursor;

  private Cursor prevCursor = null;

  protected AbstractSkVedClickableActor( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );
    handCursor = cursorManager().getCursor( ECursorType.HAND );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Усанавливает обработчик мыши.<br>
   *
   * @param aMouseClickHandler {@link IMouseClickHandler} - обработчик нажатия мыши, м.б. <b>null</b>
   */
  public void setMouseClickHandler( IMouseClickHandler aMouseClickHandler ) {
    clickHandler = aMouseClickHandler;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    boolean retVal = false;
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
    if( visel == null ) {
      return retVal;
    }
    // если visel - кнопка, то отдельная обрабюотка
    if( isViselBttn( visel ) && visel.props().getValobj( ViselButton.PROPID_STATE ) == EButtonViselState.DISABLED ) {
      restorCursor();
      return false;
    }
    ID2Point p = vedScreen().view().coorsConverter().swt2Visel( aCoors, visel );
    if( visel.isYours( p ) ) {
      setIfHasProp( visel, ViselButton.PROPID_STATE, true );
      setHandCursor();
      retVal = true;
    }
    else {
      setIfHasProp( visel, ViselButton.PROPID_HOVERED, false );
      restorCursor();
      retVal = false;
    }
    vedScreen().view().redraw();
    return retVal;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( !isMyMouseButton( aButton, aState ) ) {
      return false;
    }

    // dima 22.04.24 не понимаю что делает этот кусок кода
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
    if( isViselBttn( visel ) && visel.props().getValobj( ViselButton.PROPID_STATE ) == EButtonViselState.DISABLED ) {
      return false;
    }
    // для кнопки и для не кнопки отдельные ветки алгоритма
    visel = findMyVisel( aCoors );
    if( visel == null ) {
      return false;
    }
    if( isViselBttn( visel ) ) {
      if( aButton == ETsMouseButton.LEFT && aState == 0 ) {
        if( visel.props().getValobj( ViselButton.PROPID_STATE ) == EButtonViselState.DISABLED ) {
          setActivated( false );
          return false;
        }
        visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.PRESSED );
        setActivated( true );
        return true;
      }
    }
    else {
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
        if( isViselBttn( visel ) ) {
          visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
        }
        retVal = true;
        setActivated( false );
        if( clickHandler != null ) {
          clickHandler.onClick( visel, aButton, aCoors, aState );
        }
        // TsDialogUtils.info( vedScreen().view().getControl().getShell(), "Ти нажяль!" );
      }
      visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
      if( isViselBttn( visel ) ) {
        visel.props().setBool( ViselButton.PROPID_HOVERED, false );
      }
    }
    setActivated( false );
    return retVal;
  }

  /**
   * Called when there was mouse button double click.
   *
   * @param aSource Object - the event source
   * @param aButton {@link ETsMouseButton} - the clicked button
   * @param aState int - the state of the keyboard modifier keys and mouse buttons mask as in {@link KeyEvent#stateMask}
   * @param aCoors {@link ITsPoint} - mouse coordinates relative to <code>aWidget</code>
   * @param aWidget {@link Control} - the control that issued the event
   * @return boolean - event processing flag
   */
  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    if( !isMyMouseButton( aButton, aState ) ) {
      return false;
    }
    boolean retVal = false;
    if( activated ) {
      VedAbstractVisel visel = findMyVisel( aCoors );
      if( visel != null ) {
        if( isViselBttn( visel ) ) {
          visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
        }
        retVal = true;
        setActivated( false );
        if( clickHandler != null ) {
          clickHandler.onDoubleClick( visel, aButton, aCoors, aState );
        }
        // TsDialogUtils.info( vedScreen().view().getControl().getShell(), "Ти быстро дважды нажяль!" );
      }
      visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
      setIfHasProp( visel, ViselButton.PROPID_HOVERED, false );
    }
    setActivated( false );
    return retVal;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private VedAbstractVisel findMyVisel( ITsPoint aCoors ) {
    IAtomicValue v = props().getValue( PROPID_VISEL_ID );
    if( !v.isAssigned() || v.asString().isBlank() || v.asString().equals( NONE_ID ) ) {
      IStringList ids = vedScreen().view().listViselIdsAtPoint( aCoors );
      if( !ids.isEmpty() ) {
        return vedScreen().model().visels().list().findByKey( ids.first() );
      }
      return null;
    }
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
    Cursor cur = vedScreen().view().getControl().getCursor();
    if( cur == null || !cur.equals( handCursor ) ) {
      prevCursor = vedScreen().view().getControl().getCursor();
      vedScreen().view().getControl().setCursor( handCursor );
    }
  }

  private void restorCursor() {
    vedScreen().view().getControl().setCursor( prevCursor );
  }

  /**
   * First check prop existence in visel, if exist change value
   *
   * @param aVisel - visual element
   * @param aPropId - id of property
   * @param aFlag - propp`s value
   */
  private static void setIfHasProp( VedAbstractVisel aVisel, String aPropId, boolean aFlag ) {
    if( aVisel.props().hasKey( aPropId ) ) {
      aVisel.props().setBool( aPropId, aFlag );
    }
  }

  /**
   * check if visel is button
   *
   * @param aVisel - visual element
   * @return true if visel is button
   */
  private static boolean isViselBttn( VedAbstractVisel aVisel ) {
    if( aVisel != null ) {
      return aVisel.props().hasKey( ViselButton.PROPID_STATE );
    }
    return false;
  }

  /**
   * Test if user click on proper mouse button
   *
   * @param aMouseButton - real user selected mouse button
   * @param aState int - SWT код состояния управляющих клавиш Shift, Alt, Ctrl
   * @return true if click on proper mouse button
   */
  protected boolean isMyMouseButton( ETsMouseButton aMouseButton, int aState ) {
    boolean retVal = false;
    ERtActionMouseButton actionButton = props().getValobj( FID_MOUSE_BUTTON );
    int keyMask = props().getInt( FID_KEY_MASK );
    switch( actionButton ) {
      case LEFT:
        if( aMouseButton.equals( ETsMouseButton.LEFT ) && (keyMask == aState) ) {
          retVal = true;
        }
        break;
      case MIDDLE:
        if( aMouseButton.equals( ETsMouseButton.MIDDLE ) && (keyMask == aState) ) {
          retVal = true;
        }
        break;
      case RIGHT:
        if( aMouseButton.equals( ETsMouseButton.RIGHT ) && (keyMask == aState) ) {
          retVal = true;
        }
        break;
      // $CASES-OMITTED$
      default:
        break;

    }
    return retVal;
  }

}
