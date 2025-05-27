package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.lib.ISkMnemosServiceHardConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
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
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Актор реализующий функциональность "поля ввода" (Swt control Text), который после завершения редактирования, посылает
 * команду с аргументом - значение поля ввода.
 * <p>
 *
 * @author vs
 */
public class SkActorCmdField
    extends AbstractSkActorInputField {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.CommandField"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_CMD_FIELD, //
      TSID_DESCRIPTION, STR_ACTOR_CMD_FIELD_D, //
      TSID_ICON_ID, ICONID_VED_CMD_FIELD_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_RTD_UGWI );
      fields.add( TFI_FORMAT_STRING );
      fields.add( TFI_CMD_UGWI );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorCmdField.class );
    }

    @Override
    protected StridablesList<IVedItemsPaletteEntry> doCreatePaletteEntries() {
      VedItemCfg cfg = new VedItemCfg( id(), kind(), id(), IOptionSet.NULL );
      OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
      cfg.propValues().setStr( PROPID_VISEL_PROP_ID, PROPID_TEXT );
      IVedItemsPaletteEntry pe = new VedItemPaletteEntry( id(), params(), cfg );
      return new StridablesList<>( pe );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorCmdField( aCfg, propDefs(), aVedScreen );
    }

  };

  private ISkCommand currCommand = null;

  private String oldStr = TsLibUtils.EMPTY_STRING;
  private Gwid   gwid   = null;
  private Ugwi   ugwi   = null;
  private String fmtStr = null;

  private IGwidList sourceGwidList = IGwidList.EMPTY;

  private IAtomicValue lastValue = IAtomicValue.NULL;

  protected SkActorCmdField( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );

    guiTimersService().quickTimers().addListener( aRtTime -> {
      if( currCommand != null ) {
        SkCommandState cmdState = currCommand.state();
        switch( cmdState.state() ) {
          case SENDING:
            return;
          case EXECUTING:
            return;
          case SUCCESS:
            currCommand = null;
            break;
          case FAILED:
          case TIMEOUTED:
          case UNHANDLED:
            currCommand = null;
            TsDialogUtils.error( getShell(), cmdState.toString() );
            break;
          default:
            currCommand = null;
            if( inputHandler != null ) {
              inputHandler.resume();
            }
            throw new TsNotAllEnumsUsedRtException();
        }
        if( inputHandler != null ) {
          inputHandler.resume();
        }
      }
    } );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractActor
  //

  @Override
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    removeWrongUgwi( TFI_CMD_UGWI.id(), UgwiKindSkCmd.KIND_ID, aValuesToSet, coreApi() );
    removeWrongUgwi( TFI_RTD_UGWI.id(), UgwiKindSkRtdata.KIND_ID, aValuesToSet, coreApi() );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_RTD_UGWI ) ) {
      IAtomicValue av = props().getValue( PROPID_RTD_UGWI );
      if( av != null && av.isAssigned() ) {
        ugwi = av.asValobj();
        if( ugwi != Ugwi.NONE ) {
          gwid = UgwiKindSkRtdata.getGwid( ugwi );
          sourceGwidList = new GwidList( gwid );
        }
      }
    }
    if( aChangedValues.hasKey( PROPID_FORMAT_STRING ) ) {
      fmtStr = props().getStr( PROP_FORMAT_STRING );
      if( fmtStr.isBlank() ) {
        fmtStr = null;
        if( gwid != null ) {
          ISkClassInfo classInfo = skSysdescr().findClassInfo( gwid.classId() );
          if( classInfo != null ) {
            IDtoRtdataInfo rtdInfo = classInfo.rtdata().list().findByKey( gwid.propId() );
            if( rtdInfo != null ) {
              IAtomicValue avFmtStr = SkHelperUtils.getConstraint( rtdInfo, TSID_FORMAT_STRING );
              if( avFmtStr != null ) {
                fmtStr = avFmtStr.asString();
              }
            }
          }
        }
      }
      if( fmtStr != null && fmtStr.isBlank() ) {
        fmtStr = null;
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected IGwidList doListUsedGwids() {
    return sourceGwidList;
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkActorInputField
  //

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    if( inputHandler != null && inputHandler.isEditing() ) {
      super.whenRealTimePassed( aRtTime );
      return;
    }
    if( gwid != null ) {
      IAtomicValue newValue = skVedEnv().getRtDataValue( gwid );
      if( !newValue.equals( lastValue ) ) {
        String text = AvUtils.printAv( fmtStr, newValue );
        setStdViselPropValue( avStr( text ) );
        lastValue = newValue;
      }
    }
  }

  @Override
  protected void onStartEdit() {
    oldStr = getVisel().props().getStr( PROPID_TEXT );
  }

  @Override
  protected void onCancelEdit() {
    getVisel().props().setStr( PROPID_TEXT, oldStr );
  }

  @Override
  protected void onFinishEdit() {
    sendCommand();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void sendCommand() {
    ISkVedEnvironment vedEnv = skVedEnv();
    ISkCoreApi coreApi = vedEnv.skConn().coreApi();

    ISkLoggedUserInfo userInfo = coreApi.getCurrentUserInfo();
    ISkUser user = vedEnv.skConn().coreApi().userService().findUser( userInfo.userSkid().strid() );

    if( !coreApi.userService().abilityManager().isAbilityAllowed( ABILITYID_MNEMO_SEND_COMMANDS ) ) {
      TsDialogUtils.warn( getShell(), ERR_STR_OPERATION_NOT_ALLOWED );
      return;
    }

    Ugwi cmdUgwi = MnemoUtils.findUgwi( TFI_CMD_UGWI.id(), props() );
    if( cmdUgwi != null && cmdUgwi != Ugwi.NONE ) {
      Gwid cmdGwid = UgwiKindSkCmd.getGwid( cmdUgwi );
      IOptionSetEdit opSet = new OptionSet();
      IDtoCmdInfo cmdInfo = getCommandInfo();
      IAtomicValue argValue = getFiledValue( cmdInfo );
      opSet.setValue( cmdInfo.argDefs().first().id(), argValue );
      lastValue = argValue;
      currCommand = vedEnv.sendCommand( cmdGwid, user.skid(), opSet );
      if( currCommand == null ) {
        TsDialogUtils.error( getShell(), "Unexpected NULL command returned" ); //$NON-NLS-1$
      }
      if( inputHandler != null ) {
        inputHandler.pause();
      }
    }
    else {
      currCommand = null;
      LoggerUtils.errorLogger().error( "Attempt to send command with null or none UGWI" ); //$NON-NLS-1$
    }
  }

  IAtomicValue getFiledValue( IDtoCmdInfo aCmdInfo ) {
    String valStr = getVisel().props().getStr( PROPID_TEXT );

    IDataDef argDef = aCmdInfo.argDefs().first();

    IAtomicValue value = IAtomicValue.NULL;
    switch( argDef.atomicType() ) {
      case BOOLEAN:
        Boolean bv = Boolean.valueOf( Boolean.parseBoolean( valStr ) );
        value = AvUtils.avFromObj( bv );
        break;
      case FLOATING:
        Double dv = Double.valueOf( Double.parseDouble( valStr ) );
        value = AvUtils.avFromObj( dv );
        break;
      case INTEGER:
        Integer iv = Integer.valueOf( Integer.parseInt( valStr ) );
        value = AvUtils.avFromObj( iv );
        break;
      case STRING:
        value = AvUtils.avFromObj( valStr );
        break;
      case TIMESTAMP:
      case VALOBJ:
      case NONE:
        break;
      default:
        throw new IllegalArgumentException( "Unexpected value: " + value.atomicType() ); //$NON-NLS-1$
    }
    return value;
  }

  IDtoCmdInfo getCommandInfo() {
    Ugwi cmdUgwi = MnemoUtils.findUgwi( TFI_CMD_UGWI.id(), props() );
    String aClassId = UgwiKindSkCmd.getClassId( cmdUgwi );
    ISkClassInfo clsInfo = coreApi().sysdescr().getClassInfo( aClassId );
    IDtoCmdInfo cmdInfo = clsInfo.cmds().list().getByKey( UgwiKindSkCmd.getCmdId( cmdUgwi ) );
    return cmdInfo;
  }

}
