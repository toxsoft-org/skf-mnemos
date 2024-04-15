package org.toxsoft.skf.mnemo.gui.skved.rt_action.tti;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;

/**
 * Object inspector helper constants.
 *
 * @author dima
 */
@SuppressWarnings( "javadoc" )
public interface ITtiConstants {

  ITinTypeInfo TTI_POPUP_MNEMO_INFO =
      new TinAtomicTypeInfo.TtiValobj<>( IRtActionConstants.DT_POPUP_MNEEMO_INFO, PopupMnemoInfo.class );

  ITinTypeInfo TTI_SWITCH_PERSP_INFO =
      new TinAtomicTypeInfo.TtiValobj<>( IRtActionConstants.DT_SWITCH_PERSP_INFO, SwitchPerspInfo.class );

}
