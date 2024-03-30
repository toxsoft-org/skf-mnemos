package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

public class MasterSlavePositionProcessor
    implements IViselsPositionProcessor {

  private final IVedViselsMasterSlaveRelationsManager msManager;

  public MasterSlavePositionProcessor( IVedViselsMasterSlaveRelationsManager aMsManager ) {
    msManager = aMsManager;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "ved.masterSlave.position.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Переместить с подчиненными";
  }

  @Override
  public String description() {
    return "Перемещает выделенные элементы вместе с подчиненными";
  }

  // ------------------------------------------------------------------------------------
  // IViselsPositionProcessor
  //

  @Override
  public void editIdsForMove( IStringListEdit aViselIds, IOptionSetEdit aParams ) {
    IStringListEdit viselIds = new StringArrayList( aViselIds );
    for( String id : viselIds ) {
      IStringList slaveIds = msManager.listAllSlaveViselIds( id );
      for( String slaveId : slaveIds ) {
        aViselIds.remove( slaveId ); // удалим детей перемещаемого родителя
        // if( !aViselIds.hasElem( slaveId ) ) {
        // aViselIds.add( slaveId );
        // }
      }
    }
  }

}
