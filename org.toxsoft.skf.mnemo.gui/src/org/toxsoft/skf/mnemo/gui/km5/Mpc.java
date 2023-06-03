package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.e4.services.*;
import org.toxsoft.skf.mnemo.lib.*;

class Mpc
    extends MultiPaneComponentModown<ISkMnemoCfg> {

  public static final String ACTID_EDIT_MNEMO = "org.toxsoft.mnemos.edit_mnemo";

  public static final ITsActionDef ACDEF_EDIT_MNEMO = TsActionDef.ofPush1( ACTID_EDIT_MNEMO, //
      TSID_NAME, "Правка менемо", //
      TSID_DEFAULT_VALUE, "", //
      TSID_ICON_ID, ICONID_MNEMO_EDIT //
  );

  public Mpc( ITsGuiContext aContext, IM5Model<ISkMnemoCfg> aModel, IM5ItemsProvider<ISkMnemoCfg> aItemsProvider,
      IM5LifecycleManager<ISkMnemoCfg> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    IMultiPaneComponentConstants.OPDEF_DBLCLICK_ACTION_ID.setValue( tsContext().params(), avStr( ACTID_EDIT_MNEMO ) );
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    aActs.add( ACDEF_EDIT_MNEMO );
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    ISkMnemoCfg sel = selectedItem();
    switch( aActionId ) {
      case ACTID_EDIT_MNEMO: {
        if( sel != null ) {
          ISkMnemoEditService vss = tsContext().get( ISkMnemoEditService.class );
          vss.switchTpPerspectiveAndEditMnemo( sel );
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aActionId );
    }
  }

  @Override
  protected void doUpdateActionsState( boolean aIsAlive, boolean aIsSel, ISkMnemoCfg aSel ) {
    toolbar().setActionEnabled( ACTID_EDIT_MNEMO, aIsSel );
  }

}
