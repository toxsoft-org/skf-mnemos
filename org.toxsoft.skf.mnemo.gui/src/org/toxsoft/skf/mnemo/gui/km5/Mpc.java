package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.km5.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.e4.services.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * {@link MultiPaneComponentModown} implementation for {@link SkMnemoCfgM5Model}.
 *
 * @author hazard157
 */
class Mpc
    extends MultiPaneComponentModown<ISkMnemoCfg> {

  public static final String ACTID_EDIT_MNEMO = "org.toxsoft.mnemos.edit_mnemo"; //$NON-NLS-1$

  public static final ITsActionDef ACDEF_EDIT_MNEMO = TsActionDef.ofPush1( ACTID_EDIT_MNEMO, //
      TSID_NAME, STR_ACT_EDIT_MNEMO, //
      TSID_DEFAULT_VALUE, STR_ACT_EDIT_MNEMO_D, //
      TSID_ICON_ID, ICONID_MNEMO_EDIT //
  );

  /**
   * ID of action {@link #ACDEF_ADD_COPY_MNEMO}.
   */
  static final String ACTID_ADD_COPY_MNEMO = TSGUI_ACT_ID + ".add_copy_mnemo"; //$NON-NLS-1$

  /**
   * Action: add an mnemo created from current mnemo.
   */
  ITsActionDef ACDEF_ADD_COPY_MNEMO = ofPush2( ACTID_ADD_COPY_MNEMO, //
      STR_T_ADD_COPY_MNEMO, STR_P_ADD_COPY_MNEMO, ICONID_LIST_ADD_COPY );

  public Mpc( ITsGuiContext aContext, IM5Model<ISkMnemoCfg> aModel, IM5ItemsProvider<ISkMnemoCfg> aItemsProvider,
      IM5LifecycleManager<ISkMnemoCfg> aLifecycleManager ) {
    super( aContext, aModel, aItemsProvider, aLifecycleManager );
    IMultiPaneComponentConstants.OPDEF_DBLCLICK_ACTION_ID.setValue( tsContext().params(), avStr( ACTID_EDIT_MNEMO ) );
  }

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    // add func create copy of mnemo
    int index = 1 + aActs.indexOf( ACDEF_ADD );
    aActs.insert( index, ACDEF_ADD_COPY_MNEMO );
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
          vss.openMnemoForEditing( sel );
        }
        break;
      }
      case ACTID_ADD_COPY_MNEMO: {
        ISkMnemoCfg selected = tree().selectedItem();
        ITsDialogInfo cdi = doCreateDialogInfoToAddItem();
        IM5BunchEdit<ISkMnemoCfg> initVals = new M5BunchEdit<>( model() );
        initVals.fillFrom( selected, false );
        String itemId = initVals.getAsAv( AID_STRID ).asString();
        itemId = itemId + "_copy"; //$NON-NLS-1$
        initVals.set( AID_STRID, avStr( itemId ) );

        ISkMnemoCfg item = M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, lifecycleManager() );
        if( item != null ) {
          item.setCfgData( selected.cfgData() );
          fillViewer( item );
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
