package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.rt_action.ISkResources.*;

import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.valed.*;

/**
 * Helper constants for package entities.
 *
 * @author dima
 */
public interface IRtActionConstants {

  /**
   * Data type: {@link TsFillInfo} as {@link EAtomicType#VALOBJ VALOBJ}.
   */
  IDataType DT_RT_USER_ACTION_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_RUNTIME_USER_ACTION, //
      TSID_DESCRIPTION, STR_D_RUNTIME_USER_ACTION, //
      TSID_KEEPER_ID, RunTimeUserActionInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjRtUserActionInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( RunTimeUserActionInfo.NONE ) //
  );

  /**
   * Data type: {@link PopupMnemoInfo} as {@link EAtomicType#VALOBJ}.
   */
  IDataType DT_POPUP_MNEEMO_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_POPUP_MNEMO, //
      TSID_DESCRIPTION, STR_D_POPUP_MNEMO, //
      TSID_KEEPER_ID, PopupMnemoInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjPopupMnemoInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( PopupMnemoInfo.EMPTY ) //
  );

  /**
   * Data type: {@link SwitchPerspInfo} as {@link EAtomicType#VALOBJ}.
   */
  IDataType DT_SWITCH_PERSP_INFO = DataType.create( VALOBJ, //
      TSID_NAME, STR_N_SWITCH_PERSP, //
      TSID_DESCRIPTION, STR_D_SWITCH_PERSP, //
      TSID_KEEPER_ID, SwitchPerspInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSwitchPerspInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( SwitchPerspInfo.EMPTY ) //
  );

}
