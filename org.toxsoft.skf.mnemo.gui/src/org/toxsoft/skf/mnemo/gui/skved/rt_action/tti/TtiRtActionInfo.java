package org.toxsoft.skf.mnemo.gui.skved.rt_action.tti;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.rt_action.tti.ITsResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.rt_action.tti.ITtiConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.valed.*;

/**
 * The {@link ITinTypeInfo} implementation for {@link RunTimeUserActionInfo}.
 *
 * @author dima
 */
public class TtiRtActionInfo
    extends AbstractTinTypeInfo<RunTimeUserActionInfo> {

  private static final String FID_RT_ACTION_TYPE = "type";        //$NON-NLS-1$
  private static final String FID_POPUP_MNEMO    = "popupMnemo";  //$NON-NLS-1$
  private static final String FID_SWITCH_PERSP   = "switchPersp"; //$NON-NLS-1$

  /**
   * Select user action type
   */
  public static final ITinFieldInfo TFI_RT_ACTION_TYPE = new TinFieldInfo( FID_RT_ACTION_TYPE, TtiAvEnum.INSTANCE, //
      TSID_NAME, STR_RT_ACTION_TYPE, //
      TSID_DESCRIPTION, STR_RT_ACTION_TYPE_D, //
      TSID_KEEPER_ID, ERtActionKind.KEEPER_ID //
  );

  private static final ITinFieldInfo TFI_POPUP_MNEMO = new TinFieldInfo( FID_POPUP_MNEMO, TTI_POPUP_MNEMO_INFO, //
      TSID_NAME, STR_POPUP_MNEMO, //
      TSID_DESCRIPTION, STR_POPUP_MNEMO_D, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjPopupMnemoInfo.FACTORY_NAME, //
      TSID_KEEPER_ID, PopupMnemoInfo.KEEPER_ID //
  );

  private static final ITinFieldInfo TFI_SWITCH_PERSP = new TinFieldInfo( FID_SWITCH_PERSP, TTI_SWITCH_PERSP_INFO, //
      TSID_NAME, STR_SWITCH_PERSP, //
      TSID_DESCRIPTION, STR_SWITCH_PERSP_D, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSwitchPerspInfo.FACTORY_NAME, //
      TSID_KEEPER_ID, SwitchPerspInfo.KEEPER_ID //
  );

  /**
   * The type information singleton.
   */
  public static final TtiRtActionInfo INSTANCE = new TtiRtActionInfo();

  private TtiRtActionInfo() {
    super( ETinTypeKind.FULL, IRtActionConstants.DT_RT_USER_ACTION_INFO, RunTimeUserActionInfo.class );
    fieldInfos().add( TFI_RT_ACTION_TYPE );
    fieldInfos().add( TFI_POPUP_MNEMO );
    fieldInfos().add( TFI_SWITCH_PERSP );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( RunTimeUserActionInfo.NONE );
  }

  @Override
  protected ITinValue doGetTinValue( RunTimeUserActionInfo aEntity ) {
    IAtomicValue av = avValobj( aEntity );
    return TinValue.ofFull( av, decompose( av ) );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    ERtActionKind kind = extractChildValobj( TFI_RT_ACTION_TYPE, aChildValues );
    switch( kind ) {
      case NONE:
        return avValobj( RunTimeUserActionInfo.NONE );
      case POPUP_MNEMO: {
        PopupMnemoInfo popupMnemoInfo = extractChildValobj( TFI_POPUP_MNEMO, aChildValues );
        return avValobj( new RunTimeUserActionInfo( popupMnemoInfo ) );
      }
      case SWITCH_PERSP: {
        SwitchPerspInfo switchPerspInfo = extractChildValobj( TFI_SWITCH_PERSP, aChildValues );
        return avValobj( new RunTimeUserActionInfo( switchPerspInfo ) );
      }
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    RunTimeUserActionInfo rtUserAction = RunTimeUserActionInfo.NONE;
    if( aValue != null && aValue.isAssigned() ) {
      rtUserAction = aValue.asValobj();
    }
    aChildValues.put( FID_RT_ACTION_TYPE, TinValue.ofAtomic( avValobj( rtUserAction.kind() ) ) );
    switch( rtUserAction.kind() ) {
      case NONE:
        break;
      case POPUP_MNEMO:
        aChildValues.put( FID_POPUP_MNEMO, TTI_POPUP_MNEMO_INFO.makeValue( rtUserAction.popupMnemoInfo() ) );
        break;
      case SWITCH_PERSP:
        aChildValues.put( FID_SWITCH_PERSP, TTI_SWITCH_PERSP_INFO.makeValue( rtUserAction.switchPerspInfo() ) );
        break;
      default:
        throw new IllegalArgumentException( "Unexpected value: " + rtUserAction.kind() ); //$NON-NLS-1$
    }
  }

  @Override
  protected IStringList doGetVisibleFieldIds( ITinValue aValue ) {
    RunTimeUserActionInfo rtUserAction = RunTimeUserActionInfo.NONE;
    if( aValue != null ) {
      IAtomicValue av = aValue.atomicValue();
      if( av != null && av.isAssigned() ) {
        rtUserAction = av.asValobj();
      }
    }
    IStringListEdit result = new StringArrayList();
    switch( rtUserAction.kind() ) {
      case NONE:
        result.add( FID_RT_ACTION_TYPE );
        return result;
      case POPUP_MNEMO:
        result.add( FID_RT_ACTION_TYPE );
        result.add( FID_POPUP_MNEMO );
        return result;
      case SWITCH_PERSP:
        result.add( FID_RT_ACTION_TYPE );
        result.add( FID_SWITCH_PERSP );
        return result;
      default:
        throw new IllegalArgumentException( "Unexpected value: " + rtUserAction.kind() ); //$NON-NLS-1$
    }
  }

}
