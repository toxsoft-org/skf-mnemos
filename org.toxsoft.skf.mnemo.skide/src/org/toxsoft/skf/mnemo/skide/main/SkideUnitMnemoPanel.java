package org.toxsoft.skf.mnemo.skide.main;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * The SkIDE right panel implementation of the mnemoscheme unit.
 *
 * @author vs
 */
class SkideUnitMnemoPanel
    extends AbstractSkideUnitPanel {

  public SkideUnitMnemoPanel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    ISkCoreApi coreApi = tsContext().get( ISkConnectionSupplier.class ).defConn().coreApi();
    ISkConnection skConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
    IM5Domain m5d = skConn.scope().get( IM5Domain.class );
    IM5Model<ISkMnemoCfg> model = m5d.getModel( ISkMnemoCfg.CLASS_ID, ISkMnemoCfg.class );
    ISkMnemosService mnemoService = coreApi.getService( ISkMnemosService.SERVICE_ID );
    IM5LifecycleManager<ISkMnemoCfg> lm = model.getLifecycleManager( mnemoService );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_IS_FILTER_PANE.setValue( ctx.params(), AV_TRUE );
    IM5CollectionPanel<ISkMnemoCfg> panel = model.panelCreator().createCollEditPanel( ctx, lm.itemsProvider(), lm );
    return panel.createControl( aParent );
  }

}
