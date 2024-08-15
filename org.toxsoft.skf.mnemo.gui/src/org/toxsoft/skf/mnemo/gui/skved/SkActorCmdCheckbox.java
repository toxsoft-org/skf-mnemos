package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

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
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Actor: process push button so that on click send command.
 *
 * @author vs
 */
public class SkActorCmdCheckbox
    extends AbstractSkVedButtonActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.CmdCheckbox"; //$NON-NLS-1$

  public static final ITinFieldInfo TFI_CHECK_CMD_UGWI = new TinFieldInfo( "cmdCheck", TTI_CMD_UGWI, //
      TSID_NAME, "Команда (on)" );

  public static final ITinFieldInfo TFI_UNCHECK_CMD_UGWI = new TinFieldInfo( "cmdUncheck", TTI_CMD_UGWI, //
      TSID_NAME, "Команда (off)" );

  public static final ITinFieldInfo TFI_INVERSE_VALUE = new TinFieldInfo( "inverseValue", TTI_AT_BOOLEAN, //
      TSID_NAME, "Инвертировать значение" );

  public static final ITinFieldInfo TFI_VALUE = TinFieldInfo.makeCopy( TFI_RTD_UGWI, //
      TSID_NAME, "Значение" );

  static final String PROPID_INVERSE_BOOLEAN = "inverse.boolean"; //$NON-NLS-1$

  static final IDataDef PROP_INVERSE = DataDef.create( PROPID_INVERSE_BOOLEAN, BOOLEAN, //
      TSID_NAME, STR_INVERSE_BOOLEAN, //
      TSID_DESCRIPTION, STR_INVERSE_BOOLEAN_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  static final ITinFieldInfo TFI_INVERSE_BOOLEAN = new TinFieldInfo( PROP_INVERSE, TTI_AT_BOOLEAN );

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
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TinFieldInfo.makeCopy( TFI_VISEL_PROP_ID, TSID_DEFAULT_VALUE, PROPID_ON_OFF_STATE ) );
      fields.add( TFI_CHECK_CMD_UGWI );
      fields.add( TFI_UNCHECK_CMD_UGWI );
      fields.add( TFI_VALUE );
      fields.add( TFI_INVERSE_VALUE );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorCmdCheckbox.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorCmdCheckbox( aCfg, propDefs(), aVedScreen );
    }

  };

  private ISkCommand currCommand = null;

  private Ugwi         ugwi      = null;
  private Gwid         gwid      = null;
  private IUgwiList    ugwiList  = IUgwiList.EMPTY;
  private IAtomicValue lastValue = IAtomicValue.NULL;

  private Ugwi cmdOnUgwi  = null;
  private Ugwi cmdOffUgwi = null;

  protected SkActorCmdCheckbox( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );
    IButtonClickHandler buttonHandler = aVisel -> {

      VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
      visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
      ISkVedEnvironment vedEnv = aVedScreen.tsContext().get( ISkVedEnvironment.class );

      ISkConnectionSupplier conn = aVedScreen.tsContext().get( ISkConnectionSupplier.class );
      ISkUser user = conn.defConn().coreApi().userService().findUser( "root" );

      // Ugwi ugwi = SkUgwiUtils.findUgwi( TFI_CMD_UGWI.id(), props() );
      Ugwi cmdUgwi = null;
      if( visel.props().hasKey( PROPID_ON_OFF_STATE ) ) {
        if( visel.props().getBool( PROPID_ON_OFF_STATE ) ) {
          cmdUgwi = cmdOffUgwi;
        }
        else {
          cmdUgwi = cmdOnUgwi;
        }
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
      //
      // /**
      // * TODO send Sk-command on click
      // */
      // TsDialogUtils.underDevelopment( getShell() );
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
      }
    } );
  }

  @Override
  protected final void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // check and don't allow to set invalid UGWI
    removeWrongUgwi( TFI_VALUE.id(), UgwiKindSkRtdata.KIND_ID, aValuesToSet, coreApi() );
  }

  @Override
  protected final void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( TFI_VALUE.id() ) ) {
      gwid = null;
      ugwi = Ugwi.NONE;
      ugwiList = IUgwiList.EMPTY;
      IAtomicValue av = aChangedValues.getValue( TFI_VALUE.id() );
      if( av.isAssigned() ) {
        ugwi = av.asValobj();
        if( ugwi != null && ugwi != Ugwi.NONE ) {
          gwid = UgwiKindSkRtdata.getGwid( ugwi );
          ugwiList = UgwiList.createDirect( new ElemArrayList<>( ugwi ) );
        }
      }
    }
    cmdOnUgwi = SkUgwiUtils.findUgwi( TFI_CHECK_CMD_UGWI.id(), aChangedValues );
    cmdOffUgwi = SkUgwiUtils.findUgwi( TFI_UNCHECK_CMD_UGWI.id(), aChangedValues );
  }

  @Override
  public final void whenRealTimePassed( long aRtTime ) {
    if( gwid != null ) {
      IAtomicValue newValue = skVedEnv().getRtDataValue( gwid );
      if( !newValue.equals( lastValue ) ) {
        lastValue = newValue;
        doOnValueChanged( newValue );
      }
    }
  }

  @Override
  protected final IGwidList doListUsedGwids() {
    GwidList gl = new GwidList();
    for( Ugwi u : ugwiList.items() ) {
      gl.add( UgwiKindSkRtdata.getGwid( u ) );
    }
    return gl;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  protected void doOnValueChanged( IAtomicValue aNewValue ) {
    IAtomicValue val2set = aNewValue;
    if( aNewValue.atomicType() == EAtomicType.BOOLEAN && props().hasKey( PROPID_INVERSE_BOOLEAN ) ) {
      boolean inverse = props().getBool( PROPID_INVERSE_BOOLEAN );
      if( inverse ) {
        val2set = avBool( !aNewValue.asBool() );
      }
    }
    setStdViselPropValue( val2set );
  }

}
