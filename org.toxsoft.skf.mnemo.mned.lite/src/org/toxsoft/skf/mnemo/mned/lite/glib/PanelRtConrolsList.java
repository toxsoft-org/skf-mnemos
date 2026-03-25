package org.toxsoft.skf.mnemo.mned.lite.glib;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.impl.*;

/**
 * GUI The pane contains list of RtControls.
 * <p>
 *
 * @author vs
 */
public class PanelRtConrolsList
    extends TsStdEventsProducerPanel<IRtControl> {

  private final IVedScreen vedScreen;

  private final IM5TreeViewer<IRtControl> treeViewer;

  // private final IListReorderer<IRtControl> listReorderer;

  private final IRtControlsManager rtcManager;

  IMultiPaneComponent<IRtControl> mpc;

  /**
   * Constructor.
   * <p>
   *
   * @param aParent {@link Composite} - parent component
   * @param aVedScreen {@link IVedScreen} - the VED screen to display it's model's content
   * @param aRtcManager {@link RtControlsManager} - manager of RtControl's
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public PanelRtConrolsList( Composite aParent, IVedScreen aVedScreen, IRtControlsManager aRtcManager ) {
    super( aParent, aVedScreen.tsContext() );
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    rtcManager = TsNullArgumentRtException.checkNull( aRtcManager );
    this.setLayout( new BorderLayout() );

    IM5Model<IRtControl> model = m5().getModel( MID_RT_CONTROL, IRtControl.class );
    IM5LifecycleManager<IRtControl> lm = model.getLifecycleManager( aRtcManager );
    // listReorderer = lm.itemsProvider().reorderer();

    OPDEF_IS_TOOLBAR_NAME.setValue( tsContext().params(), AV_TRUE );
    OPDEF_IS_ACTIONS_CRUD.setValue( tsContext().params(), AV_TRUE );
    OPDEF_IS_ACTIONS_HIDE_PANES.setValue( tsContext().params(), AV_TRUE );
    // OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), avBool( !aTreeModes.isEmpty() ) );
    OPDEF_IS_ACTIONS_TREE_MODES.setValue( tsContext().params(), AV_FALSE );
    OPDEF_IS_DETAILS_PANE.setValue( tsContext().params(), AV_TRUE );
    OPDEF_IS_DETAILS_PANE_HIDDEN.setValue( tsContext().params(), AV_TRUE );
    mpc = new MultiPaneComponentModown<>( tsContext(), model, lm.itemsProvider(), lm );
    // if( !aTreeModes.isEmpty() ) {
    // for( TreeModeInfo<IVedVisel> tm : aTreeModes ) {
    // mpc.treeModeManager().addTreeMode( tm );
    // }
    // mpc.treeModeManager().setCurrentMode( aTreeModes.first().id() );
    // }
    mpc.createControl( this );
    mpc.getControl().setLayoutData( BorderLayout.CENTER );
    mpc.addTsSelectionListener( selectionChangeEventHelper );
    mpc.addTsDoubleClickListener( doubleClickEventHelper );
    // vedScreen.model().visels().eventer().addListener( this::onVedViselsListChange );

    rtcManager.eventer().addListener( ( aSource, aOp, aId ) -> {
      switch( aOp ) {
        case CREATE: {
          mpc.refresh();
          break;
        }
        case EDIT: {
          IRtControl rtc = rtcManager.list().findByKey( aId );
          if( rtc != null ) {
            // FIXME just update item in tree
          }
          break;
        }
        case LIST: {
          mpc.refresh();
          break;
        }
        case REMOVE: {
          IRtControl rtc = rtcManager.list().findByKey( aId );
          if( rtc != null ) {

          }
          mpc.refresh();
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    } );
    treeViewer = mpc.tree();
  }

  // ------------------------------------------------------------------------------------
  // TsStdEventsProducerPanel
  //

  @Override
  public IRtControl selectedItem() {
    return mpc.selectedItem();
  }

  @Override
  public void setSelectedItem( IRtControl aItem ) {
    mpc.setSelectedItem( aItem );
  }

}
