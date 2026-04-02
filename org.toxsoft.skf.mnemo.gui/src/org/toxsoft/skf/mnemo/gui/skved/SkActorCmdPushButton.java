package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
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
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.api.users.*;

/**
 * Actor: process push button so that on click send command.
 * <p>
 * В свойствах кнопки содержится конфигурация команды, включающая в себя значения аргументов.
 *
 * @author vs
 */
public class SkActorCmdPushButton
    extends AbstractSkVedButtonActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.CmdPushButton"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_CMD_PUSH_BUTTON, //
      TSID_DESCRIPTION, STR_ACTOR_CMD_PUSH_BUTTON_D, //
      TSID_ICON_ID, ICONID_ACTOR_CMD //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_CMD_CFG );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorCmdPushButton.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorCmdPushButton( aCfg, propDefs(), aVedScreen );
    }

    // @Override
    // protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
    // // IOptionSetEdit options = new OptionSet();
    // // CmdArgValuesSet argValues = new CmdArgValuesSet( TFI_CMD_UGWI.id() );
    // VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
    // // OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
    // // cfg.propValues().setValobj( PROPID_CMD_ON_ARGS_ID, argValues );
    // IVedItemsPaletteEntry pent = new VedItemPaletteEntry( id(), params(), cfg );
    // return new StridablesList<>( pent );
    // }
  };

  private ISkCommand currCommand = null;

  private Gwid      gwid     = null;
  private IUgwiList ugwiList = IUgwiList.EMPTY;

  IAtomicValue currValue = IAtomicValue.NULL;
  boolean      selected  = false;

  protected SkActorCmdPushButton( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );
    IButtonClickHandler buttonHandler = aVisel -> {
      VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
      visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
      ISkVedEnvironment vedEnv = aVedScreen.tsContext().get( ISkVedEnvironment.class );
      ISkCoreApi coreApi = vedEnv.skConn().coreApi();

      ISkLoggedUserInfo userInfo = coreApi.getCurrentUserInfo();
      ISkUser user = vedEnv.skConn().coreApi().userService().findUser( userInfo.userSkid().strid() );

      if( !coreApi.userService().abilityManager().isAbilityAllowed( ABILITYID_MNEMO_SEND_COMMANDS ) ) {
        TsDialogUtils.warn( getShell(), ERR_STR_OPERATION_NOT_ALLOWED );
        return;
      }

      String confStr = TsLibUtils.EMPTY_STRING;
      Ugwi cmdUgwi = MnemoUtils.findUgwi( TFI_CMD_UGWI.id(), props() );
      if( confStr != null && !confStr.isBlank() ) {
        if( TsDialogUtils.askYesNoCancel( getShell(), confStr ) != ETsDialogCode.YES ) {
          return;
        }
      }
      if( cmdUgwi != null && cmdUgwi != Ugwi.NONE ) {
        Gwid cmdGwid = UgwiKindSkCmd.INSTANCE.getGwid( cmdUgwi );
        try {
          // currCommand = vedEnv.sendCommand( cmdGwid, user.skid(), args );
        }
        catch( Throwable e ) {
          // TsDialogUtils.error( getShell(), e.getMessage() );
          e.printStackTrace();
        }
        if( currCommand == null ) {
          TsDialogUtils.error( getShell(), "Unexpected NULL command returned. See stack trace..." ); //$NON-NLS-1$
        }
      }
      else {
        currCommand = null;
        LoggerUtils.errorLogger().error( "Attempt to send command with null or none UGWI" ); //$NON-NLS-1$
      }
      updateButtonState();
    };
    setButtonClickHandler( buttonHandler );
  }

  @Override
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // removeWrongUgwi( TFI_CMD_UGWI.id(), UgwiKindSkCmd.KIND_ID, aValuesToSet, coreApi() );
    // removeWrongUgwi( PROPID_OFF_CMD, UgwiKindSkCmd.KIND_ID, aValuesToSet, coreApi() );
    // removeWrongUgwi( TFI_VALUE_UGWI.id(), UgwiKindSkRtdata.KIND_ID, aValuesToSet, coreApi() );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    super.doUpdateCachesAfterPropsChange( aChangedValues );
    // if( aChangedValues.hasKey( PROPID_TOGGLE ) ) {
    // IAtomicValue val = aChangedValues.getByKey( PROPID_TOGGLE );
    // if( val.isAssigned() ) {
    // toggle = val.asBool();
    // }
    // }
    // if( aChangedValues.hasKey( TFI_VALUE_UGWI.id() ) ) {
    // gwid = null;
    // Ugwi ugwi = Ugwi.NONE;
    // ugwiList = IUgwiList.EMPTY;
    // IAtomicValue av = aChangedValues.getValue( TFI_VALUE_UGWI.id() );
    // if( av.isAssigned() ) {
    // ugwi = av.asValobj();
    // if( ugwi != null && ugwi != Ugwi.NONE ) {
    // gwid = UgwiKindSkRtdata.INSTANCE.getGwid( ugwi );
    // ugwiList = UgwiList.createDirect( new ElemArrayList<>( ugwi ) );
    // }
    // }
    // }
    // if( aChangedValues.hasKey( TFI_FEEDBACK_VALUE.id() ) ) {
    // String str = aChangedValues.getStr( TFI_FEEDBACK_VALUE.id() );
    // AvTextParser textParser = new AvTextParser();
    // feedbackValue = textParser.parse( str );
    // }
    // if( aChangedValues.hasKey( TSID_DESCRIPTION ) ) {
    // IAtomicValue v = aChangedValues.getValue( TSID_DESCRIPTION );
    // if( v != null && v.isAssigned() ) {
    // setTooltipText( v.asString() );
    // }
    // else {
    // setTooltipText( null );
    // }
    // }
    // if( aChangedValues.hasKey( PROPID_CMD_ON_ARGS_ID ) ) {
    // IAtomicValue v = aChangedValues.getValue( PROPID_CMD_ON_ARGS_ID );
    // if( v != null && v.isAssigned() ) {
    // CmdArgValuesSet args = v.asValobj();
    // argsOn = args.argValues( coreApi() );
    // }
    // else {
    // props().propsEventer().pauseFiring();
    // props().setValobj( PROP_CMD_ON_ARGS, new CmdArgValuesSet( PROPID_CMD_ON_ARGS_ID ) );
    // props().propsEventer().resumeFiring( false );
    // }
    // }
    // if( aChangedValues.hasKey( PROPID_CMD_OFF_ARGS_ID ) ) {
    // IAtomicValue v = aChangedValues.getValue( PROPID_CMD_OFF_ARGS_ID );
    // if( v != null && v.isAssigned() ) {
    // CmdArgValuesSet args = v.asValobj();
    // argsOff = args.argValues( coreApi() );
    // }
    // else {
    // props().propsEventer().pauseFiring();
    // props().setValobj( PROP_CMD_OFF_ARGS, new CmdArgValuesSet( PROPID_CMD_OFF_ARGS_ID ) );
    // props().propsEventer().resumeFiring( false );
    // }
    // }
  }

  @Override
  public final void whenRealTimePassed( long aRtTime ) {
    if( gwid != null ) {
      IAtomicValue newValue = skVedEnv().getRtDataValue( gwid );
      if( newValue.isAssigned() && !newValue.equals( currValue ) ) {
        currValue = newValue;
        doOnValueChanged( newValue );
      }
    }
    if( currCommand != null ) {
      SkCommandState cmdState = currCommand.state();
      VedAbstractVisel visel = findVisel( props().getStr( PROPID_VISEL_ID ) );
      if( visel == null ) {
        currCommand = null;
        return;
      }
      switch( cmdState.state() ) {
        case SENDING:
          return;
        case EXECUTING:
          return;
        case SUCCESS:
          visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
          currCommand = null;
          break;
        case FAILED:
        case TIMEOUTED:
        case UNHANDLED:
          visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
          currCommand = null;
          visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
          TsDialogUtils.error( getShell(), cmdState.toString() );
          break;
        default:
          visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
          currCommand = null;
          visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
          throw new TsNotAllEnumsUsedRtException();
      }
      updateButtonState();
    }
  }

  @Override
  protected IGwidList doListUsedGwids() {
    GwidList gl = new GwidList();
    for( Ugwi u : ugwiList.items() ) {
      gl.add( UgwiKindSkRtdata.INSTANCE.getGwid( u ) );
    }
    return gl;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  @SuppressWarnings( "unused" )
  protected void doOnValueChanged( IAtomicValue aNewValue ) {
    updateButtonState();
  }

  void updateButtonState() {
    VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
    // if( toggle && currCommand == null ) {
    // if( currValue.isAssigned() && currValue.equals( feedbackValue ) ) {
    // selected = true;
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.SELECTED );
    // }
    // else {
    // selected = false;
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // }
    // }
    // if( !toggle && currCommand == null ) {
    // visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
    // }
  }

}
