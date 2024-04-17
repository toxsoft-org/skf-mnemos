package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The run time user action parameters.
 *
 * @author dima
 */
public class RunTimeUserActionInfo {

  /**
   * Singleton of the no fill.
   */
  public static final RunTimeUserActionInfo NONE = new RunTimeUserActionInfo();

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "RunTimeUserActionInfo"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<RunTimeUserActionInfo> KEEPER =
      new AbstractEntityKeeper<>( RunTimeUserActionInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, RunTimeUserActionInfo aEntity ) {
          ERtActionKind kind = aEntity.kind();
          ERtActionKind.KEEPER.write( aSw, kind );
          aSw.writeSeparatorChar();
          switch( kind ) {
            case NONE:
              break;
            case POPUP_MNEMO:
              PopupMnemoInfo.KEEPER.write( aSw, aEntity.popupMnemoInfo() );
              break;
            case SWITCH_PERSP:
              SwitchPerspInfo.KEEPER.write( aSw, aEntity.switchPerspInfo() );
              break;
            default:
              break;
          }
        }

        @Override
        protected RunTimeUserActionInfo doRead( IStrioReader aSr ) {
          ERtActionKind kind = ERtActionKind.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          switch( kind ) {
            case NONE:
              return NONE;
            case POPUP_MNEMO:
              PopupMnemoInfo popupMnemoInfo = PopupMnemoInfo.KEEPER.read( aSr );
              return new RunTimeUserActionInfo( popupMnemoInfo );
            case SWITCH_PERSP:
              SwitchPerspInfo switchPerspInfo = SwitchPerspInfo.KEEPER.read( aSr );
              return new RunTimeUserActionInfo( switchPerspInfo );
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
        }
      };

  private final ERtActionKind kind;

  private PopupMnemoInfo  popupMnemoInfo  = null;
  private SwitchPerspInfo switchPerspInfo = null;

  private RunTimeUserActionInfo() {
    kind = ERtActionKind.NONE;
  }

  /**
   * Creates instance of kind {@link ERtActionKind#POPUP_MNEMO}.
   *
   * @param aPopupMnemoInfo {@link PopupMnemoInfo} - the parameters for popup mnemo
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public RunTimeUserActionInfo( PopupMnemoInfo aPopupMnemoInfo ) {
    popupMnemoInfo = TsNullArgumentRtException.checkNull( aPopupMnemoInfo );
    kind = ERtActionKind.POPUP_MNEMO;
  }

  /**
   * Creates instance of kind {@link ERtActionKind#SWITCH_PERSP}.
   *
   * @param aPerspInfo {@link SwitchPerspInfo} - the Eclipse perspective parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public RunTimeUserActionInfo( SwitchPerspInfo aPerspInfo ) {
    switchPerspInfo = TsNullArgumentRtException.checkNull( aPerspInfo );
    kind = ERtActionKind.SWITCH_PERSP;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the filling kind.
   *
   * @return {@link ERtActionKind} - the filling kind
   */
  public ERtActionKind kind() {
    return kind;
  }

  /**
   * Returns the popup mnemo information (kind {@link ERtActionKind#POPUP_MNEMO}).
   *
   * @return {@link PopupMnemoInfo} - the image filling parameters
   */
  public PopupMnemoInfo popupMnemoInfo() {
    return popupMnemoInfo;
  }

  /**
   * Returns the information for switch Eclipse perspective (kind {@link ERtActionKind#SWITCH_PERSP}).
   *
   * @return {@link SwitchPerspInfo} - the switch Eclipse perspective parameters
   */
  public SwitchPerspInfo switchPerspInfo() {
    return switchPerspInfo;
  }

}
