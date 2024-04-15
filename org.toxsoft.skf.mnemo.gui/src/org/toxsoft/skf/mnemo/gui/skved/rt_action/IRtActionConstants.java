package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.valed.*;

/**
 * Helper constants for package entities.
 *
 * @author dima
 */
@SuppressWarnings( "javadoc" )
public interface IRtActionConstants {

  /**
   * Data type: {@link PopupMnemoInfo} as {@link EAtomicType#VALOBJ}.
   */
  IDataType DT_POPUP_MNEEMO_INFO = DataType.create( VALOBJ, //
      TSID_NAME, "popup mnemo", //
      TSID_DESCRIPTION, "popup mnemo info", //
      TSID_KEEPER_ID, PopupMnemoInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjPopupMnemoInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( PopupMnemoInfo.EMPTY ) //
  );

  /**
   * Data type: {@link SwitchPerspInfo} as {@link EAtomicType#VALOBJ}.
   */
  IDataType DT_SWITCH_PERSP_INFO = DataType.create( VALOBJ, //
      TSID_NAME, "switch persp", //
      TSID_DESCRIPTION, "switch persp info", //
      TSID_KEEPER_ID, SwitchPerspInfo.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSwitchPerspInfo.FACTORY.factoryName(), //
      TSID_DEFAULT_VALUE, avValobj( SwitchPerspInfo.EMPTY ) //
  );

}
