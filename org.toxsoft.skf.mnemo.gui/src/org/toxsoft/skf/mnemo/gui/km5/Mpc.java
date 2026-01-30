package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.km5.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.jface.resource.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.singlesrc.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.swt.*;
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
    setDefaultEditor( isDefaultEditorLite() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private boolean isDefaultEditorLite() {
    return prefBundle( PBID_BUNDLE_MNEMOS ).prefs().getBool( APPREF_IS_DEFAULT_EDITOR_LITE );
  }

  private void setDefaultEditor( boolean aLite ) {
    prefBundle( PBID_BUNDLE_MNEMOS ).prefs().setBool( APPREF_IS_DEFAULT_EDITOR_LITE, aLite );
    String actId = aLite ? ACTID_EDIT_MNEMO_AS_LITE : ACTID_EDIT_MNEMO_AS_PRO;
    OPDEF_DBLCLICK_ACTION_ID.setValue( tsContext().params(), avStr( actId ) );
    if( toolbar() != null ) {
      ITsActionDef acDef = aLite ? ACDEF_EDIT_MNEMO_AS_LITE : ACDEF_EDIT_MNEMO_AS_PRO;
      ImageDescriptor imgDescr = iconManager().loadStdDescriptor( acDef.iconId(), toolbar().iconSize() );
      toolbar().getAction( ACTID_EDIT_MNEMO_MENU_BTN ).setText( acDef.nmName() );
      toolbar().getAction( ACTID_EDIT_MNEMO_MENU_BTN ).setToolTipText( acDef.description() );
      toolbar().getAction( ACTID_EDIT_MNEMO_MENU_BTN ).setImageDescriptor( imgDescr );
    }
  }

  // ------------------------------------------------------------------------------------
  // MultiPaneComponentModown
  //

  @Override
  protected ITsToolbar doCreateToolbar( ITsGuiContext aContext, String aName, EIconSize aIconSize,
      IListEdit<ITsActionDef> aActs ) {
    // add func create copy of mnemo
    int index = 1 + aActs.indexOf( ACDEF_ADD );
    aActs.insert( index, ACDEF_ADD_COPY_MNEMO );
    aActs.add( ACDEF_EDIT_MNEMO_MENU_BTN );
    return super.doCreateToolbar( aContext, aName, aIconSize, aActs );
  }

  @Override
  protected void doAfterCreateControls() {
    toolbar().setActionMenu( ACTID_EDIT_MNEMO_MENU_BTN, new AbstractMenuCreator() {

      @Override
      protected boolean fillMenu( Menu aMenu ) {
        // LITE
        MenuItem mItem = new MenuItem( aMenu, SWT.PUSH );
        mItem.setText( ACDEF_EDIT_MNEMO_AS_LITE.nmName() );
        TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, ACDEF_ZOOM_ORIGINAL.description() );
        mItem.setImage(
            iconManager().loadStdIcon( ACDEF_EDIT_MNEMO_AS_LITE.iconId(), hdpiService().getMenuIconsSize() ) );
        mItem.addSelectionListener( new SelectionAdapter() {

          @Override
          public void widgetSelected( SelectionEvent e ) {
            processAction( ACTID_EDIT_MNEMO_AS_LITE );
          }
        } );
        // PRO
        mItem = new MenuItem( aMenu, SWT.PUSH );
        mItem.setText( ACDEF_EDIT_MNEMO_AS_PRO.nmName() );
        TsSinglesourcingUtils.MenuItem_setToolTipText( mItem, ACDEF_ZOOM_ORIGINAL.description() );
        mItem.setImage(
            iconManager().loadStdIcon( ACDEF_EDIT_MNEMO_AS_PRO.iconId(), hdpiService().getMenuIconsSize() ) );
        mItem.addSelectionListener( new SelectionAdapter() {

          @Override
          public void widgetSelected( SelectionEvent e ) {
            processAction( ACTID_EDIT_MNEMO_AS_PRO );
          }
        } );
        return true;
      }

    } );
    ITsActionDef acDef = isDefaultEditorLite() ? ACDEF_EDIT_MNEMO_AS_LITE : ACDEF_EDIT_MNEMO_AS_PRO;
    ImageDescriptor imgDescr = iconManager().loadStdDescriptor( acDef.iconId(), toolbar().iconSize() );
    toolbar().getAction( ACTID_EDIT_MNEMO_MENU_BTN ).setText( acDef.nmName() );
    toolbar().getAction( ACTID_EDIT_MNEMO_MENU_BTN ).setToolTipText( acDef.description() );
    toolbar().getAction( ACTID_EDIT_MNEMO_MENU_BTN ).setImageDescriptor( imgDescr );
  }

  @Override
  protected void doProcessAction( String aActionId ) {
    ISkMnemoCfg sel = selectedItem();
    switch( aActionId ) {
      case ACTID_EDIT_MNEMO_MENU_BTN: {
        if( sel != null ) {
          ISkMnemoEditService vss = tsContext().get( ISkMnemoEditService.class );
          if( isDefaultEditorLite() ) {
            vss.openMnemoForEditingLite( sel );
          }
          else {
            vss.openMnemoForEditingPro( sel );
          }
        }
        break;
      }
      case ACTID_EDIT_MNEMO_AS_LITE: {
        if( sel != null ) {
          ISkMnemoEditService vss = tsContext().get( ISkMnemoEditService.class );
          vss.openMnemoForEditingLite( sel );
          setDefaultEditor( true );
        }
        break;
      }
      case ACTID_EDIT_MNEMO_AS_PRO: {
        if( sel != null ) {
          ISkMnemoEditService vss = tsContext().get( ISkMnemoEditService.class );
          vss.openMnemoForEditingPro( sel );
          setDefaultEditor( false );
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
    toolbar().setActionEnabled( ACTID_EDIT_MNEMO_MENU_BTN, aIsSel );
  }

}
