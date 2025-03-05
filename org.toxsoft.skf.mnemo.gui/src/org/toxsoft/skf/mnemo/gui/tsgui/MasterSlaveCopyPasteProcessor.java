package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
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
      // IStringList actIds = VedScreenUtils.viselActorIds( vid, vedScreen );
      // aActorIds.addAll( actIds );
    }
    for( String vid : aViselIds ) {
      IStringList actIds = VedScreenUtils.viselActorIds( vid, vedScreen );
      aActorIds.addAll( actIds );
    }
  }

  @Override
  public void editConfigsForPaste( IStridablesListEdit<VedItemCfg> aVisConfs, IListEdit<VedItemCfg> aActConfs,
      IStringMap<String> aViselsMap, IStringMap<String> aActorsMap, IOptionSet aParams ) {
    for( VedItemCfg cfg : aVisConfs ) {
      msManager.setSlaveIds( cfg, IStringList.EMPTY ); // очистим список подчиненных элементов
      String mId = msManager.viselMasterId( cfg );
      if( mId != null ) {
        if( !aViselsMap.hasKey( mId ) ) { // если мастер визел не был скопирован
          msManager.setMasterId( cfg, mId ); // если у визеля был мастер, то у копии установим такой же
          msManager.addSlaveId( mId, cfg.id() ); // добавим этот визель к мастеру
        }
        else {
          String masterCopyId = aViselsMap.getByKey( mId );
          msManager.setMasterId( cfg, masterCopyId ); // если у визеля был мастер, то у копии установим такой же
          for( VedItemCfg masterCfg : aVisConfs ) {
            if( masterCfg.id().equals( masterCopyId ) ) {
              msManager.addSlaveId( masterCfg, cfg.id() ); // добавим этот визель к мастеру
              break;
            }
          }
        }
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
  }
}
