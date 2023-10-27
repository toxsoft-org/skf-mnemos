package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * M5-model of {@link ISkMnemoCfg}.
 *
 * @author hazard157
 */
public class SkMnemoCfgM5Model
    extends KM5ModelBasic<ISkMnemoCfg> {

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkMnemoCfgM5Model( ISkConnection aConn ) {
    super( ISkMnemoCfg.CLASS_ID, ISkMnemoCfg.class, aConn );
    DESCRIPTION.setFlags( M5FF_COLUMN );
    addFieldDefs( STRID, NAME, DESCRIPTION );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      @Override
      protected IM5CollectionPanel<ISkMnemoCfg> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISkMnemoCfg> aItemsProvider, IM5LifecycleManager<ISkMnemoCfg> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        // OPDEF_IS_FILTER_PANE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<ISkMnemoCfg> mpc = new Mpc( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<ISkMnemoCfg> doCreateLifecycleManager( Object aMaster ) {
    IM5Model<Object> model = skConn().scope().find( IM5Domain.class ).findModel( ISkMnemoCfg.CLASS_ID );
    return new MnemoM5LifecycleManager( SkMnemoCfgM5Model.class.cast( model ), ISkMnemosService.class.cast( aMaster ) );
  }

}
