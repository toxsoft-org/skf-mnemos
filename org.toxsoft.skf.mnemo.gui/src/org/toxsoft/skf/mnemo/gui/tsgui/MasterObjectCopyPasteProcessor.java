package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skf.ext.mastobj.gui.main.*;
import org.toxsoft.skf.ext.mastobj.gui.skved.*;

/**
 * Дополнительный обработчик информации связанной с поддержкой "мастер-объекта" при действиях copy/paste.
 *
 * @author AUTHOR_NAME
 */
public class MasterObjectCopyPasteProcessor
    implements ICopyPasteProcessor {

  private final IVedScreen vedScreen;

  public MasterObjectCopyPasteProcessor( IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "ved.copyPaste.masterObject.processor"; //$NON-NLS-1$
  }

  @Override
  public String nmName() {
    return "Корректор информации \"разрешителя\" мастер-объекта мнемосхемы";
  }

  @Override
  public String description() {
    return "Корректирует информацию \"разрешителя\" мастер-объекта мнемосхемы в соотвествии с копируемыми элементами";
  }

  // ------------------------------------------------------------------------------------
  // ICopyPasteProcessor
  //

  @Override
  public void editIdsForCopy( IStringListEdit aViselIds, IStringListEdit aActorIds, IOptionSetEdit aParams ) {
    // nop - do nothing
  }

  @Override
  public void editConfigsForPaste( IStridablesListEdit<VedItemCfg> aVisConfs, IListEdit<VedItemCfg> aActConfs,
      IStringMap<String> aViselsMap, IStringMap<String> aActorsMap, IOptionSet aParams ) {
    MnemoResolverConfig mrCfg = MasterObjectUtils.readMnemoResolverConfig( vedScreen );
    IStringMap<String> actorSubmasters = mrCfg.actorSubmasterIds();
    for( String oldActorId : aActorsMap.keys() ) {
      String newActorId = aActorsMap.getByKey( oldActorId );
      String submasterId = actorSubmasters.findByKey( oldActorId );
      if( submasterId != null ) {
        mrCfg.defineActorSubmaster( newActorId, submasterId );
      }
    }
    MasterObjectUtils.updateMnemoResolverConfig( mrCfg, vedScreen );
  }

}
