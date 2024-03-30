package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

public class MasterSlaveDeleteProcessor
    implements IDeleteProcessor {

  private final IVedScreen vedScreen;

  private final IVedViselsMasterSlaveRelationsManager msManager;

  public MasterSlaveDeleteProcessor( IVedScreen aVedScreen, IVedViselsMasterSlaveRelationsManager aMsManager ) {
    vedScreen = aVedScreen;
    msManager = aMsManager;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "ved.delete.masterSlave.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Удалить дочерние элементы";
  }

  @Override
  public String description() {
    return "Удаляет элемент вместе с подчиненными элементами";
  }

  // ------------------------------------------------------------------------------------
  // IDeleteProcessor
  //

  @Override
  public void editIdsForDelete( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams ) {
    IStringListEdit viselIds = new StringArrayList( aViselIds ); // копируем, чтобы избежать ConcurrentModification
    for( String vid : viselIds ) {
      addSlaveIds( vid, aViselIds, aActorIds );
      IStringList actIds = VedScreenUtils.viselActorIds( vid, vedScreen );
      aActorIds.addAll( actIds );
    }

    for( String vid : viselIds ) {
      msManager.freeVisel( vid );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void addSlaveIds( String aViselId, IStringListEdit aViselIds, IStringListEdit aActorIds ) {
    for( String slaveId : msManager.listSlaveViselIds( aViselId ) ) {
      aViselIds.add( slaveId );
      aActorIds.addAll( VedScreenUtils.viselActorIds( slaveId, vedScreen ) );
      addSlaveIds( slaveId, aViselIds, aActorIds );
    }
  }

}
