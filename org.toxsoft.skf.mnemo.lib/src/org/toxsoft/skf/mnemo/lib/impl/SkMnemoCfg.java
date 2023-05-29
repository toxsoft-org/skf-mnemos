package org.toxsoft.skf.mnemo.lib.impl;

import static org.toxsoft.skf.mnemo.lib.ISkMnemosServiceHardConstants.*;

import org.toxsoft.core.tslib.gw.gwid.*;
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

  private transient Gwid dataGwid = null;

  SkMnemoCfg( Skid aSkid ) {
    super( aSkid );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  Gwid dataGwid() {
    if( dataGwid == null ) {
      dataGwid = Gwid.createClob( classId(), strid(), CLBID_MNEMO_CFG_DATA );
    }
    return dataGwid;
  }

  // ------------------------------------------------------------------------------------
  // ISkMnemoCfg
  //

  @Override
  public String cfgData() {
    return coreApi().clobService().readClob( dataGwid() );
  }

}
