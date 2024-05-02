package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * "Разрешитель" объекта по ссылке.
 * <p>
 * Ссылаться на объект можно либо по связи (link), либо как на член класса (rivet). При этом, если ссылка указывает на
 * множество объектов, то необходимо указать {@link ISkObjectRecognizer}, который позволит распознать требуемый объект.
 *
 * @author vs
 */
public class ReferencedObjectResolver
    implements ISkObjectResolver {

  private final ISkObjectRecognizer recognizer;

  private final ISkCoreApi coreApi;

  private final Gwid refGwid;

  /**
   * Конструктор.
   *
   * @param aRefGwid {@link Gwid} - ИД ссылки
   * @param aCoreApi {@link ISkCoreApi} - API сервера
   * @param aRecognizer {@link ISkObjectRecognizer} - "распознаватель" объектов м.б. <code>null</code>
   */
  public ReferencedObjectResolver( Gwid aRefGwid, ISkCoreApi aCoreApi, ISkObjectRecognizer aRecognizer ) {
    TsIllegalArgumentRtException.checkTrue( !isLink( aRefGwid ) && !isRivet( aRefGwid ) );
    refGwid = aRefGwid;
    coreApi = aCoreApi;
    recognizer = aRecognizer;
  }

  // ------------------------------------------------------------------------------------
  // ISkObjectResolver
  //

  @Override
  public Skid resolve( Skid aSkid ) {
    ISkidList skids = ISkidList.EMPTY;
    if( isRivet( refGwid ) ) { // если ссылка Rivet
      ISkObject obj = coreApi.objService().find( aSkid );
      skids = obj.rivets().map().getByKey( refGwid.propId() );
    }

    if( isLink( refGwid ) ) { // если ссылка Link
      IDtoLinkFwd linkInfo = coreApi.linkService().getLinkFwd( aSkid, refGwid.propId() );
      skids = linkInfo.rightSkids();
    }

    if( skids.size() == 1 ) {
      return skids.first();
    }

    if( recognizer != null ) {
      for( Skid skid : skids ) {
        if( recognizer.recognize( skid, coreApi ) ) {
          return skid;
        }
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  boolean isLink( Gwid aGwid ) {
    return aGwid.propSectId().equals( EGwidKind.GW_LINK.id() );
  }

  boolean isRivet( Gwid aGwid ) {
    return aGwid.propSectId().equals( EGwidKind.GW_RIVET.id() );
  }

}
