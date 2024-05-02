package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
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
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.cmdserv.*;
import org.toxsoft.uskat.core.api.users.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Actor: process push button so that on click send command.
 *
 * @author vs
 */
public class SkActorCmdButton
    extends AbstractSkVedButtonActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.CmdButton"; //$NON-NLS-1$

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
      fields.add( TFI_CMD_GWID );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorCmdButton.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorCmdButton( aCfg, propDefs(), aVedScreen );
    }

  };

  private ISkCommand currCommand = null;

  protected SkActorCmdButton( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );
    IButtonClickHandler buttonHandler = aVisel -> {

      VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
      visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
      ISkVedEnvironment vedEnv = aVedScreen.tsContext().get( ISkVedEnvironment.class );

      ISkConnectionSupplier conn = aVedScreen.tsContext().get( ISkConnectionSupplier.class );
      ISkUser user = conn.defConn().coreApi().userService().findUser( "root" );

      Gwid cmdGwid = props().getValobj( PROP_CMD_GWID );
      currCommand = vedEnv.sendCommand( cmdGwid, user.skid(), IOptionSet.NULL );
      if( currCommand == null ) {

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
  protected IGwidList doListUsedGwids() {
    Gwid cmdGwid = props().getValobj( PROP_CMD_GWID );
    return new GwidList( cmdGwid );
  }

}
