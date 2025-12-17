package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.lib.ISkMnemosServiceHardConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.cmd.*;
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

  static final String PROPID_UP_CMD_ARGS_ID   = "command.Up.CmdArgs";   //$NON-NLS-1$
  static final String PROPID_DOWN_CMD_ARGS_ID = "command.Down.CmdArgs"; //$NON-NLS-1$

  private static final IDataDef PROP_UP_CMD_ARGS   = DataDef.create3( PROPID_UP_CMD_ARGS_ID, DT_CMD_ARG_VALUES_SET );
  private static final IDataDef PROP_DOWN_CMD_ARGS = DataDef.create3( PROPID_DOWN_CMD_ARGS_ID, DT_CMD_ARG_VALUES_SET );

  private static final ITinTypeInfo TTI_UP_CMD_ARGS =
      new TinAtomicTypeInfo.TtiValobj<>( PROP_UP_CMD_ARGS, CmdArgValuesSet.class );

  private static final ITinTypeInfo TTI_DOW_CMD_ARGS =
      new TinAtomicTypeInfo.TtiValobj<>( PROP_DOWN_CMD_ARGS, CmdArgValuesSet.class );

  private static final ITinFieldInfo TFI_UP_CMD_ARGS   = new TinFieldInfo( PROP_UP_CMD_ARGS, TTI_UP_CMD_ARGS );
  private static final ITinFieldInfo TFI_DOWN_CMD_ARGS = new TinFieldInfo( PROP_DOWN_CMD_ARGS, TTI_DOW_CMD_ARGS );

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
      fields.add( TFI_DOWN_CMD_ARGS );
      fields.add( new TinFieldInfo( PROPID_UP_CMD, TFI_CMD_UGWI.typeInfo(), //
          TSID_NAME, "Команда при отпускании" ) );
      fields.add( TFI_UP_CMD_ARGS );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorUpDownCmdButton.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorUpDownCmdButton( aCfg, propDefs(), aVedScreen );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      CmdArgValuesSet argValues = new CmdArgValuesSet( PROPID_DOWN_CMD_ARGS_ID );
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setValobj( PROPID_UP_CMD_ARGS_ID, argValues );
      IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pent );
    }

  };

  private ISkCommand downCommand = null;
  private ISkCommand upCommand   = null;

  private IOptionSet argsUp   = IOptionSet.NULL;
  private IOptionSet argsDown = IOptionSet.NULL;

  private final ISkVedEnvironment vedEnv;
  private final ISkCoreApi        coreApi;

  protected SkActorUpDownCmdButton( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );

    vedEnv = aVedScreen.tsContext().get( ISkVedEnvironment.class );
    coreApi = vedEnv.skConn().coreApi();

    IButtonUpDownHandler buttonHandler = new IButtonUpDownHandler() {

      @Override
      public void onButtonUp( VedAbstractVisel aVisel ) {
        if( upCommand != null ) {
          LoggerUtils.errorLogger().warning( "Up command is in process: " + upCommand.toString() ); //$NON-NLS-1$
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
          upCommand = vedEnv.sendCommand( cmdGwid, user.skid(), argsUp );
          if( upCommand == null ) {
            TsDialogUtils.error( getShell(), "Unexpected NULL command returned" ); //$NON-NLS-1$
          }
          else {
            LoggerUtils.errorLogger().info( "Up command sent: " + upCommand.toString() ); //$NON-NLS-1$
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
          LoggerUtils.errorLogger().warning( "Down command is in process: " + downCommand.toString() ); //$NON-NLS-1$
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
          downCommand = vedEnv.sendCommand( cmdGwid, user.skid(), argsDown );
          if( downCommand == null ) {
            TsDialogUtils.error( getShell(), "Unexpected NULL command returned" ); //$NON-NLS-1$
          }
          else {
            LoggerUtils.errorLogger().info( "Down command sent: " + downCommand.toString() ); //$NON-NLS-1$
          }
        }
        else {
          downCommand = null;
          LoggerUtils.errorLogger().error( "Attempt to send command with null or none UGWI" ); //$NON-NLS-1$
        }
      }
    };
    setButtonUpDownHandler( buttonHandler );

    // guiTimersService().quickTimers().addListener( aRtTime -> {
    // if( upCommand != null ) {
    // SkCommandState cmdState = upCommand.state();
    // VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
    // switch( cmdState.state() ) {
    // case SENDING:
    // return;
    // case EXECUTING:
    // return;
    // case SUCCESS:
    // // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // upCommand = null;
    // break;
    // case FAILED:
    // case TIMEOUTED:
    // case UNHANDLED:
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // upCommand = null;
    // // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
    // TsDialogUtils.error( getShell(), cmdState.toString() );
    // break;
    // default:
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // upCommand = null;
    // // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
    // throw new TsNotAllEnumsUsedRtException();
    // }
    // }
    //
    // if( downCommand != null ) {
    // SkCommandState cmdState = downCommand.state();
    // VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
    // switch( cmdState.state() ) {
    // case SENDING:
    // return;
    // case EXECUTING:
    // return;
    // case SUCCESS:
    // // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // downCommand = null;
    // break;
    // case FAILED:
    // case TIMEOUTED:
    // case UNHANDLED:
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // downCommand = null;
    // // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
    // TsDialogUtils.error( getShell(), cmdState.toString() );
    // break;
    // default:
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // downCommand = null;
    // // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
    // throw new TsNotAllEnumsUsedRtException();
    // }
    // }
    //
    // } );
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
    if( aChangedValues.hasKey( PROPID_UP_CMD_ARGS_ID ) ) {
      IAtomicValue v = aChangedValues.getValue( PROPID_UP_CMD_ARGS_ID );
      if( v != null && v.isAssigned() ) {
        CmdArgValuesSet args = v.asValobj();
        argsUp = args.argValues( coreApi() );
      }
      else {
        props().propsEventer().pauseFiring();
        props().setValobj( PROPID_UP_CMD_ARGS_ID, new CmdArgValuesSet( PROPID_UP_CMD_ARGS_ID ) );
        props().propsEventer().resumeFiring( false );
      }
    }
    if( aChangedValues.hasKey( PROPID_DOWN_CMD_ARGS_ID ) ) {
      IAtomicValue v = aChangedValues.getValue( PROPID_DOWN_CMD_ARGS_ID );
      if( v != null && v.isAssigned() ) {
        CmdArgValuesSet args = v.asValobj();
        argsDown = args.argValues( coreApi() );
      }
      else {
        props().propsEventer().pauseFiring();
        props().setValobj( PROPID_DOWN_CMD_ARGS_ID, new CmdArgValuesSet( PROPID_DOWN_CMD_ARGS_ID ) );
        props().propsEventer().resumeFiring( false );
      }
    }
  }

  @Override
  protected IGwidList doListUsedGwids() {
    return IGwidList.EMPTY;
  }

  @Override
  public void whenRealTimePassed( long aRtTime ) {
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
