package org.toxsoft.skf.mnemo.skide.main;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.lib.impl.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Реализация правой панели модуля работы с мнемосхемами.
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
    if( !coreApi.services().hasKey( ISkMnemosService.SERVICE_ID ) ) {
      coreApi.addService( SkMnemosService.CREATOR );
      tsContext().put( ISkMnemosService.class, coreApi.getService( ISkMnemosService.SERVICE_ID ) );
    }

    ISkConnection skConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
    IM5Domain m5d = skConn.scope().get( IM5Domain.class );
    IM5Model<Object> model = m5d.findModel( ISkMnemoCfg.CLASS_ID );
    // SkMnemoM5Model model = SkMnemoM5Model.class.cast( m5d.findModel( ISkMnemoCfg.CLASS_ID ) );
    // SkMnemoM5Model model = SkMnemoM5Model.class.cast( m5().findModel( ISkMnemoCfg.CLASS_ID ) );

    ISkMnemosService mnemoService = tsContext().get( ISkMnemosService.class );

    IM5CollectionPanel<Object> panel;
    IM5ItemsProvider<Object> ip = () -> {
      IListEdit<Object> result = new ElemArrayList<>();
      for( String id : mnemoService.listMnemosIds() ) {
        result.add( mnemoService.findMnemo( id ) );
      }
      return result;
    };
    panel = model.panelCreator().createCollEditPanel( tsContext(), ip, model.getLifecycleManager( mnemoService ) );

    return panel.createControl( aParent );

    // SashForm sash = new SashForm( aParent, SWT.HORIZONTAL );
    //
    // CLabel l1 = new CLabel( sash, SWT.BORDER | SWT.CENTER );
    // l1.setText( "Mnemos sections list" );
    //
    // CLabel l2 = new CLabel( sash, SWT.BORDER | SWT.CENTER );
    // l2.setText( "Current section mnemos list" );
    //
    // sash.setWeights( 1, 2 );
    //
    //
    // ISkMnemosService mnemoServ = coreApi.getService( ISkMnemosService.SERVICE_ID );
    // return sash;
  }

}
