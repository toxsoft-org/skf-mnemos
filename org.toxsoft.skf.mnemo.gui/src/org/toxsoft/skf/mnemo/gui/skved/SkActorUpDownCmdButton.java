package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.lib.ISkMnemosServiceHardConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.api.users.*;

/**
 * Actor: process push button so that on Down and Up send command.
 * <p>
 *
 * @author vs
 */
public class SkActorUpDownCmdButton
    extends AbstractSkVedButtonActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.UpDownCmdButton"; //$NON-NLS-1$

  static final String PROPID_DOWN_CMD = "command.Down"; //$NON-NLS-1$
  static final String PROPID_UP_CMD   = "command.Up";   //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_CMD_BUTTON, //
      TSID_DESCRIPTION, "Актор посылающий команды при нажатии и отпускании кнопки", //
      TSID_ICON_ID, ICONID_VED_COMMAND_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      // fields.add( TFI_CMD_UGWI );
      fields.add( new TinFieldInfo( PROPID_DOWN_CMD, TFI_CMD_UGWI.typeInfo(), //
          TSID_NAME, "Команда при нажатии" ) );
      fields.add( new TinFieldInfo( PROPID_UP_CMD, TFI_CMD_UGWI.typeInfo(), //
          TSID_NAME, "Команда при отпускании" ) );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorUpDownCmdButton.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorUpDownCmdButton( aCfg, propDefs(), aVedScreen );
    }

  };

  private ISkCommand downCommand = null;
  private ISkCommand upCommand   = null;

  private final ISkVedEnvironment vedEnv;
  private final ISkCoreApi        coreApi;

  protected SkActorUpDownCmdButton( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );

    vedEnv = aVedScreen.tsContext().get( ISkVedEnvironment.class );
    coreApi = vedEnv.skConn().coreApi();

    // IButtonClickHandler buttonHandler = aVisel -> {
    // VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
    //
    // ISkLoggedUserInfo userInfo = coreApi.getCurrentUserInfo();
    // ISkUser user = vedEnv.skConn().coreApi().userService().findUser( userInfo.userSkid().strid() );
    //
    // if( !coreApi.userService().abilityManager().isAbilityAllowed( ABILITYID_MNEMO_SEND_COMMANDS ) ) {
    // TsDialogUtils.warn( getShell(), ERR_STR_OPERATION_NOT_ALLOWED );
    // return;
    // }
    //
    // Ugwi cmdUgwi = MnemoUtils.findUgwi( PROPID_DOWN_CMD, props() );
    // if( cmdUgwi != null && cmdUgwi != Ugwi.NONE ) {
    // Gwid cmdGwid = UgwiKindSkCmd.getGwid( cmdUgwi );
    // currCommand = vedEnv.sendCommand( cmdGwid, user.skid(), IOptionSet.NULL );
    // if( currCommand == null ) {
    // TsDialogUtils.error( getShell(), "Unexpected NULL command returned" ); //$NON-NLS-1$
    // }
    // }
    // else {
    // currCommand = null;
    // LoggerUtils.errorLogger().error( "Attempt to send command with null or none UGWI" ); //$NON-NLS-1$
    // }
    // updateButtonState();
    // };
    // setButtonClickHandler( buttonHandler );

    IButtonUpDownHandler buttonHandler = new IButtonUpDownHandler() {

      @Override
      public void onButtonUp( VedAbstractVisel aVisel ) {
        if( upCommand != null ) {
          return;
        }
        if( !canSendCommands() ) {
          TsDialogUtils.warn( getShell(), ERR_STR_OPERATION_NOT_ALLOWED );
          return;
        }
        ISkLoggedUserInfo userInfo = coreApi.getCurrentUserInfo();
        ISkUser user = vedEnv.skConn().coreApi().userService().findUser( userInfo.userSkid().strid() );
        Ugwi cmdUgwi = MnemoUtils.findUgwi( PROPID_UP_CMD, props() );
        if( cmdUgwi != null && cmdUgwi != Ugwi.NONE ) {
          Gwid cmdGwid = UgwiKindSkCmd.getGwid( cmdUgwi );
          upCommand = vedEnv.sendCommand( cmdGwid, user.skid(), IOptionSet.NULL );
          if( upCommand == null ) {
            TsDialogUtils.error( getShell(), "Unexpected NULL command returned" ); //$NON-NLS-1$
          }
        }
        else {
          upCommand = null;
          LoggerUtils.errorLogger().error( "Attempt to send command with null or none UGWI" ); //$NON-NLS-1$
        }
      }

      @Override
      public void onButtonDown( VedAbstractVisel aVisel ) {
        if( downCommand != null ) {
          return;
        }
        if( !canSendCommands() ) {
          TsDialogUtils.warn( getShell(), ERR_STR_OPERATION_NOT_ALLOWED );
          return;
        }
        ISkLoggedUserInfo userInfo = coreApi.getCurrentUserInfo();
        ISkUser user = vedEnv.skConn().coreApi().userService().findUser( userInfo.userSkid().strid() );
        Ugwi cmdUgwi = MnemoUtils.findUgwi( PROPID_DOWN_CMD, props() );
        if( cmdUgwi != null && cmdUgwi != Ugwi.NONE ) {
          Gwid cmdGwid = UgwiKindSkCmd.getGwid( cmdUgwi );
          downCommand = vedEnv.sendCommand( cmdGwid, user.skid(), IOptionSet.NULL );
          if( downCommand == null ) {
            TsDialogUtils.error( getShell(), "Unexpected NULL command returned" ); //$NON-NLS-1$
          }
        }
        else {
          downCommand = null;
          LoggerUtils.errorLogger().error( "Attempt to send command with null or none UGWI" ); //$NON-NLS-1$
        }
      }
    };
    setButtonUpDownHandler( buttonHandler );

    guiTimersService().quickTimers().addListener( aRtTime -> {
      if( upCommand != null ) {
        SkCommandState cmdState = upCommand.state();
        VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
        switch( cmdState.state() ) {
          case SENDING:
            return;
          case EXECUTING:
            return;
          case SUCCESS:
            // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
            upCommand = null;
            break;
          case FAILED:
          case TIMEOUTED:
          case UNHANDLED:
            visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
            upCommand = null;
            // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
            TsDialogUtils.error( getShell(), cmdState.toString() );
            break;
          default:
            visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
            upCommand = null;
            // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
            throw new TsNotAllEnumsUsedRtException();
        }
      }

      if( downCommand != null ) {
        SkCommandState cmdState = downCommand.state();
        VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
        switch( cmdState.state() ) {
          case SENDING:
            return;
          case EXECUTING:
            return;
          case SUCCESS:
            // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
            downCommand = null;
            break;
          case FAILED:
          case TIMEOUTED:
          case UNHANDLED:
            visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
            downCommand = null;
            // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
            TsDialogUtils.error( getShell(), cmdState.toString() );
            break;
          default:
            visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
            downCommand = null;
            // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
            throw new TsNotAllEnumsUsedRtException();
        }
      }

    } );
  }

  @Override
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    removeWrongUgwi( PROPID_DOWN_CMD, UgwiKindSkCmd.KIND_ID, aValuesToSet, coreApi() );
    removeWrongUgwi( PROPID_UP_CMD, UgwiKindSkCmd.KIND_ID, aValuesToSet, coreApi() );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    super.doUpdateCachesAfterPropsChange( aChangedValues );
    if( aChangedValues.hasKey( TSID_DESCRIPTION ) ) {
      IAtomicValue v = aChangedValues.getValue( TSID_DESCRIPTION );
      if( v != null && v.isAssigned() ) {
        setTooltipText( v.asString() );
      }
      else {
        setTooltipText( null );
      }
    }
  }

  @Override
  protected IGwidList doListUsedGwids() {
    return IGwidList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  boolean canSendCommands() {
    return coreApi.userService().abilityManager().isAbilityAllowed( ABILITYID_MNEMO_SEND_COMMANDS );
  }

  void updateButtonState() {
    // if( toggle && currCommand == null ) {
    // VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
    // if( currValue.isAssigned() && currValue.equals( feedbackValue ) ) {
    // selected = true;
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.SELECTED );
    // }
    // else {
    // selected = false;
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // }
    // }
  }

}
