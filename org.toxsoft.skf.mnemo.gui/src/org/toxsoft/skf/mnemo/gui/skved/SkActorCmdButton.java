package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
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
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.misc.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
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
 * Опционально, может быть западающей кнопкой, чье состояние (нажата/отжата) определяется РВ-данным. Для западающей
 * кнопки м.б. задана команда посылаемая при отжатии.
 *
 * @author vs
 */
public class SkActorCmdButton
    extends AbstractSkVedButtonActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.CmdButton"; //$NON-NLS-1$

  static final String PROPID_TOGGLE = "cmdButton.toggle"; //$NON-NLS-1$

  static final ITinFieldInfo TFI_VALUE_UGWI = TinFieldInfo.makeCopy( TFI_RTD_UGWI, //
      TSID_NAME, STR_VALUE_UGWI, TSID_DESCRIPTION, STR_VALUE_UGWI_D );

  static final IDataDef PROP_TOGGLE = DataDef.create( PROPID_TOGGLE, BOOLEAN, //
      TSID_NAME, STR_TOGGLE, //
      TSID_DESCRIPTION, STR_TOGGLE_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  static final ITinFieldInfo TFI_TOGGLE = new TinFieldInfo( PROP_TOGGLE, TTI_AT_BOOLEAN );

  static final String PROPID_FEEDBACK_VALUE = "cmdButton.feedbackValue"; //$NON-NLS-1$

  static final IDataDef PROP_FEEDBACK_VALUE = DataDef.create( PROPID_FEEDBACK_VALUE, STRING, //
      TSID_NAME, STR_FEEDBACK_VALUE, //
      TSID_DESCRIPTION, STR_FEEDBACK_VALUE_D, //
      TSID_DEFAULT_VALUE, avStr( TsLibUtils.EMPTY_STRING ) //
  );

  static final ITinFieldInfo TFI_FEEDBACK_VALUE = new TinFieldInfo( PROP_FEEDBACK_VALUE, TTI_AT_STRING );

  static final String PROPID_OFF_CMD = "command.Off"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_CMD_BUTTON, //
      TSID_DESCRIPTION, STR_ACTOR_CMD_BUTTON_D, //
      TSID_ICON_ID, ICONID_VED_COMMAND_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_TOGGLE );
      fields.add( TFI_VALUE_UGWI );
      fields.add( TFI_FEEDBACK_VALUE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_CMD_UGWI );
      fields.add( new TinFieldInfo( PROPID_OFF_CMD, TFI_CMD_UGWI.typeInfo(), //
          TSID_NAME, STR_N_COMMAND_ON_UNPRESS ) );
      fields.add( TFI_IS_ACTIVE );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorCmdButton.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorCmdButton( aCfg, propDefs(), aVedScreen );
    }

  };

  private ISkCommand currCommand = null;

  private Gwid      gwid     = null;
  private IUgwiList ugwiList = IUgwiList.EMPTY;

  boolean      toggle        = false;
  IAtomicValue currValue     = IAtomicValue.NULL;
  IAtomicValue feedbackValue = IAtomicValue.NULL; // значение для сравнения
  boolean      selected      = false;

  protected SkActorCmdButton( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
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

      Ugwi cmdUgwi = MnemoUtils.findUgwi( TFI_CMD_UGWI.id(), props() );
      // if( toggle && selected.isAssigned() && selected.asBool() ) {
      if( toggle && selected ) {
        cmdUgwi = MnemoUtils.findUgwi( PROPID_OFF_CMD, props() );
      }
      if( cmdUgwi != null && cmdUgwi != Ugwi.NONE ) {
        Gwid cmdGwid = UgwiKindSkCmd.getGwid( cmdUgwi );
        currCommand = vedEnv.sendCommand( cmdGwid, user.skid(), IOptionSet.NULL );
        if( currCommand == null ) {
          TsDialogUtils.error( getShell(), "Unexpected NULL command returned" ); //$NON-NLS-1$
        }
      }
      else {
        currCommand = null;
        LoggerUtils.errorLogger().error( "Attempt to send command with null or none UGWI" ); //$NON-NLS-1$
      }
      updateButtonState();
    };
    setButtonClickHandler( buttonHandler );

    guiTimersService().quickTimers().addListener( aRtTime -> {
      if( currCommand != null ) {
        SkCommandState cmdState = currCommand.state();
        VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
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
    } );
  }

  @Override
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    removeWrongUgwi( TFI_CMD_UGWI.id(), UgwiKindSkCmd.KIND_ID, aValuesToSet, coreApi() );
    removeWrongUgwi( PROPID_OFF_CMD, UgwiKindSkCmd.KIND_ID, aValuesToSet, coreApi() );
    removeWrongUgwi( TFI_VALUE_UGWI.id(), UgwiKindSkRtdata.KIND_ID, aValuesToSet, coreApi() );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    super.doUpdateCachesAfterPropsChange( aChangedValues );
    if( aChangedValues.hasKey( PROPID_TOGGLE ) ) {
      IAtomicValue val = aChangedValues.getByKey( PROPID_TOGGLE );
      if( val.isAssigned() ) {
        toggle = val.asBool();
      }
    }
    if( aChangedValues.hasKey( TFI_VALUE_UGWI.id() ) ) {
      gwid = null;
      Ugwi ugwi = Ugwi.NONE;
      ugwiList = IUgwiList.EMPTY;
      IAtomicValue av = aChangedValues.getValue( TFI_VALUE_UGWI.id() );
      if( av.isAssigned() ) {
        ugwi = av.asValobj();
        if( ugwi != null && ugwi != Ugwi.NONE ) {
          gwid = UgwiKindSkRtdata.getGwid( ugwi );
          ugwiList = UgwiList.createDirect( new ElemArrayList<>( ugwi ) );
        }
      }
    }
    if( aChangedValues.hasKey( TFI_FEEDBACK_VALUE.id() ) ) {
      String str = aChangedValues.getStr( TFI_FEEDBACK_VALUE.id() );
      AvTextParser textParser = new AvTextParser();
      feedbackValue = textParser.parse( str );
    }
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
  }

  @Override
  protected IGwidList doListUsedGwids() {
    GwidList gl = new GwidList();
    for( Ugwi u : ugwiList.items() ) {
      gl.add( UgwiKindSkRtdata.getGwid( u ) );
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
    if( toggle && currCommand == null ) {
      VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
      if( currValue.isAssigned() && currValue.equals( feedbackValue ) ) {
        selected = true;
        visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.SELECTED );
      }
      else {
        selected = false;
        visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.NORMAL );
      }
    }
  }

}
