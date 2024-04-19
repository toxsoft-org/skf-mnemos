package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.rt_action.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.mnemo.gui.glib.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.tti.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * Actor: process user action on run time. Samples of actions <br>
 * <ul>
 * <li>open pop up mnemo</li>
 * <li>switch to other perspective</li>
 * <li>something I don't know now</li>
 * </ul>
 *
 * @author dima
 */
public class SkActorRunTimeAction
    extends AbstractSkVedClickableActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RunTimeAction"; //$NON-NLS-1$

  private static final String FID_RT_USER_ACTION = "rtUserAction"; //$NON-NLS-1$

  private static final TinFieldInfo TFI_RT_USER_ACTION = new TinFieldInfo( FID_RT_USER_ACTION, TtiRtActionInfo.INSTANCE, //
      TSID_NAME, STR_N_RUNTIME_USER_ACTION, //
      TSID_DESCRIPTION, STR_D_RUNTIME_USER_ACTION, //
      TSID_KEEPER_ID, RunTimeUserActionInfo.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( RunTimeUserActionInfo.NONE ) );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_RUNTIME_ACTION, //
      TSID_DESCRIPTION, STR_ACTOR_RUNTIME_ACTION_D, //
      TSID_ICON_ID, ICONID_VED_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_RT_USER_ACTION ); // type
                                        // of
                                        // action:
                                        // popup
                                        // mnemo,
                                        // switch
                                        // persp,
                                        // etc
      // fields.add( TtiRtActionInfo.TFI_RT_ACTION_TYPE ); // type of action: popup mnemo, switch persp, etc
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRunTimeAction.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRunTimeAction( aCfg, propDefs(), aVedScreen );
    }

  };

  protected SkActorRunTimeAction( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );
    // IButtonClickHandler buttonHandler = aVisel -> {
    // // for debug печатаем нашу metainfo
    // RunTimeUserActionInfo rtUserAction = props().getValobj( TFI_RT_USER_ACTION.id() );
    //
    // TsDialogUtils.info( getShell(), "Mouse button: %s,\nMnemo Skid: %s,\nmaster object: %s", //$NON-NLS-1$
    // rtUserAction.popupMnemoInfo().mouseButton(), rtUserAction.popupMnemoInfo().mnemoSkid(),
    // rtUserAction.popupMnemoInfo().masterObj() );
    //
    // ISkMnemosService mnemoService = skConn().coreApi().getService( ISkMnemosService.SERVICE_ID );
    // ISkMnemoCfg mnemoCfg = mnemoService.getMnemo( rtUserAction.popupMnemoInfo().mnemoSkid().strid() );
    // // пробуем открыть мнемосхему
    // // PopupMnemoDialog dialog = new PopupMnemoDialog( getShell(), tsContext(), mnemoCfg );
    // // dialog.open();
    // Shell wnd = new Shell( getShell(), SWT.BORDER | SWT.CLOSE );
    // FillLayout layout = new FillLayout();
    // wnd.setLayout( layout );
    // Composite bkPanel = new Composite( wnd, SWT.NONE );
    // bkPanel.setLayout( layout );
    // IRuntimeMnemoPanel panel = new RuntimeMnemoPanel( bkPanel, new TsGuiContext( tsContext() ) );
    // panel.setMnemoConfig( mnemoCfg );
    // panel.resume();
    // TsPoint p = computeSize( mnemoCfg );
    // wnd.setSize( p.x(), p.y() );
    // // setLocation( 100, 100 );
    // wnd.open();
    // };

    setMouseClickHandler( new IMouseClickHandler() {

      @Override
      public void onDoubleClick( VedAbstractVisel aVisel ) {
        RunTimeUserActionInfo rtUserAction = props().getValobj( TFI_RT_USER_ACTION.id() );
        ERtActionKind actionKind = rtUserAction.kind();
        switch( actionKind ) {
          case NONE: {
            break;
          }
          case POPUP_MNEMO: {
            PopupMnemoInfo popupMnemoInfo = rtUserAction.popupMnemoInfo();
            ERtActionMouseButton button = popupMnemoInfo.mouseButton();
            if( button.equals( ERtActionMouseButton.DOUBLE_CLICK ) ) {
              // open popum mnemo
              openPopupMnemo( popupMnemoInfo );
            }
            break;
          }
          case SWITCH_PERSP: {
            SwitchPerspInfo switchPerspInfo = rtUserAction.switchPerspInfo();
            ERtActionMouseButton button = switchPerspInfo.mouseButton();
            if( button.equals( ERtActionMouseButton.DOUBLE_CLICK ) ) {
              // switch Eclipse perspective and, optionally, activate view in it
              switchPersp( switchPerspInfo );
            }
            break;
          }
          default:
            break;
        }
      }

      @Override
      public void onClick( VedAbstractVisel aVisel, ETsMouseButton aMouseButton ) {
        RunTimeUserActionInfo rtUserAction = props().getValobj( TFI_RT_USER_ACTION.id() );
        ERtActionKind actionKind = rtUserAction.kind();
        switch( actionKind ) {
          case NONE: {
            break;
          }
          case POPUP_MNEMO: {
            PopupMnemoInfo popupMnemoInfo = rtUserAction.popupMnemoInfo();
            ERtActionMouseButton actionButton = popupMnemoInfo.mouseButton();
            if( isMyMouseButton( actionButton, aMouseButton ) ) {
              // open popum mnemo
              openPopupMnemo( popupMnemoInfo );
            }
            break;
          }
          case SWITCH_PERSP: {
            SwitchPerspInfo switchPerspInfo = rtUserAction.switchPerspInfo();
            ERtActionMouseButton actionButton = switchPerspInfo.mouseButton();
            if( isMyMouseButton( actionButton, aMouseButton ) ) {
              // switch Eclipse perspective and, optionally, activate view in it
              switchPersp( switchPerspInfo );
            }
            break;
          }
          default:
            break;
        }
      }
    } );
  }

  private static TsPoint computeSize( ISkMnemoCfg aMnemoCfg ) {
    IVedScreenCfg vedCfg = VedScreenCfg.KEEPER.str2ent( aMnemoCfg.cfgData() );
    IVedCanvasCfg canvasCfg = vedCfg.canvasCfg();
    return new TsPoint( (int)(canvasCfg.size().x()) + 10, (int)(canvasCfg.size().y()) + 30 );
  }

  @Override
  protected IGwidList doListUsedGwids() {
    return IGwidList.EMPTY;
  }

  /**
   * Create popup window with mnemo inside
   *
   * @param aPopupMnemoInfo {@link PopupMnemoInfo} popum mnemo info
   */
  private void openPopupMnemo( PopupMnemoInfo aPopupMnemoInfo ) {
    ISkMnemosService mnemoService = skConn().coreApi().getService( ISkMnemosService.SERVICE_ID );
    ISkMnemoCfg mnemoCfg = mnemoService.getMnemo( aPopupMnemoInfo.mnemoSkid().strid() );

    Shell wnd = new Shell( getShell(), SWT.BORDER | SWT.CLOSE );
    FillLayout layout = new FillLayout();
    wnd.setLayout( layout );
    Composite bkPanel = new Composite( wnd, SWT.NONE );
    bkPanel.setLayout( layout );
    IRuntimeMnemoPanel panel = new RuntimeMnemoPanel( bkPanel, new TsGuiContext( tsContext() ) );
    panel.setMnemoConfig( mnemoCfg );
    panel.resume();
    TsPoint p = computeSize( mnemoCfg );
    wnd.setSize( p.x(), p.y() );
    // setLocation( 100, 100 );
    wnd.open();
  }

  /**
   * Switch current Eclipse pespecitve
   *
   * @param aSwitchPerspInfo {@link SwitchPerspInfo} action info
   */
  protected void switchPersp( SwitchPerspInfo aSwitchPerspInfo ) {
    TsE4Helper e4Helper = new TsE4Helper( tsContext().eclipseContext() );
    String viewId = aSwitchPerspInfo.viewId();
    viewId = viewId.trim().length() > 0 ? viewId.trim() : null;
    e4Helper.switchToPerspective( aSwitchPerspInfo.perspId(), viewId );
  }

  /**
   * Test if user click on proper mouse button
   *
   * @param aActionButton - designed mouse button
   * @param aMouseButton - real user selected mouse button
   * @return true if click on proper mouse button
   */
  protected boolean isMyMouseButton( ERtActionMouseButton aActionButton, ETsMouseButton aMouseButton ) {
    boolean retVal = false;
    switch( aActionButton ) {
      case LEFT:
        if( aMouseButton.equals( ETsMouseButton.LEFT ) ) {
          retVal = true;
        }
        break;
      case MIDDLE:
        if( aMouseButton.equals( ETsMouseButton.MIDDLE ) ) {
          retVal = true;
        }
        break;
      case RIGHT:
        if( aMouseButton.equals( ETsMouseButton.RIGHT ) ) {
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
