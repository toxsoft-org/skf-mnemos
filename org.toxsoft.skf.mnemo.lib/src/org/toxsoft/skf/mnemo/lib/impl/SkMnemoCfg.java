package org.toxsoft.skf.mnemo.lib.impl;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * {@link ISkMnemoCfg} implementation.
 *
 * @author hazard157
 */
class SkMnemoCfg
    extends SkObject
    implements ISkMnemoCfg {

  static final ISkObjectCreator<SkMnemoCfg> CREATOR = SkMnemoCfg::new;

  SkMnemoCfg( Skid aSkid ) {
    super( aSkid );
  }

  // ------------------------------------------------------------------------------------
  // ISkMnemoCfg
  //

  @Override
  public String cfgData() {
    ISkMnemosService ms = coreApi().getService( ISkMnemosService.SERVICE_ID );
    return ms.getMnemoData( id() );
  }

  @Override
  public void setCfgData( String aCfgData ) {
    ISkMnemosService ms = coreApi().getService( ISkMnemosService.SERVICE_ID );
    ms.setMnemoData( id(), aCfgData );
  }

}
