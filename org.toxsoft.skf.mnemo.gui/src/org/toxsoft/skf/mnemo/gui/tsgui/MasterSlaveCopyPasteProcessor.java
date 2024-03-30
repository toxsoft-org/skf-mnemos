package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

public class MasterSlaveCopyPasteProcessor
    implements ICopyPasteProcessor {

  private final IVedScreen vedScreen;

  private final IVedViselsMasterSlaveRelationsManager msManager;

  /**
   * Конструктор.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   */
  public MasterSlaveCopyPasteProcessor( IVedScreen aVedScreen, IVedViselsMasterSlaveRelationsManager aMsManager ) {
    vedScreen = aVedScreen;
    msManager = aMsManager;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "ved.copyPaste.masterSlave.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Копировать с подчиненными элементами";
  }

  @Override
  public String description() {
    return "Корректирует информацию для копирования и вставки с учетом подчиненных элементов";
  }

  // ------------------------------------------------------------------------------------
  // ICopyPasteProcessor
  //

  @Override
  public void editIdsForCopy( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams ) {
    IStringListEdit viselIds = new StringArrayList( aViselIds ); // копируем, чтобы избежать ConcurrentModification
    for( String vid : viselIds ) {
      addSlaveIds( vid, aViselIds );
      IStringList actIds = VedScreenUtils.viselActorIds( vid, vedScreen );
      aActorIds.addAll( actIds );
    }
  }

  @Override
  public void editConfigsForPaste( IListEdit<VedItemCfg> aVisConfs, IListEdit<VedItemCfg> aActConfs,
      IStringMap<String> aViselsMap, IStringMap<String> aActorsMap, IOptionSet aParams ) {
    for( VedItemCfg cfg : aVisConfs ) {
      String mId = msManager.viselMasterId( cfg );
      if( mId != null ) {
        msManager.setMasterId( cfg, aViselsMap.getByKey( mId ) );
      }
      IStringList slaveIds = msManager.listViselSlaveIds( cfg );
      IStringListEdit newSlaveIds = new StringArrayList();
      for( String id : slaveIds ) {
        newSlaveIds.add( aViselsMap.getByKey( id ) );
      }
      if( !newSlaveIds.isEmpty() ) {
        msManager.setSlaveIds( cfg, newSlaveIds );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void addSlaveIds( String aViselId, IStringListEdit aViselIds ) {
    for( String slaveId : msManager.listSlaveViselIds( aViselId ) ) {
      aViselIds.add( slaveId );
      addSlaveIds( slaveId, aViselIds );
    }
    //
    // VedAbstractVisel visel = VedScreenUtils.findVisel( aViselId, vedScreen );
    // if( visel.params().hasKey( VedViselsMasterSlaveRelationsManager.PARAMID_SLAVE_IDS ) ) {
    // String idpathStr = visel.params().getStr( VedViselsMasterSlaveRelationsManager.PARAMID_SLAVE_IDS );
    // if( !idpathStr.isBlank() ) {
    // IStringList slaveIds = StridUtils.getComponents( idpathStr );
    // for( String id : slaveIds ) {
    // aViselIds.add( id );
    // addSlaveIds( id, aViselIds );
    // }
    // }
    // }
  }

}
