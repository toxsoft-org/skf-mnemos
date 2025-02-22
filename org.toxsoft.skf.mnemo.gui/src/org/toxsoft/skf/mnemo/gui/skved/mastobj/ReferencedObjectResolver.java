package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.objserv.*;

// 5341/3
// 5341/
// 689515747

/**
 * "Разрешитель" объекта по ссылке.
 * <p>
 * Ссылаться на объект можно либо по связи (link), либо как на член класса (rivet). При этом, если ссылка указывает на
 * множество объектов, то необходимо указать {@link ISkObjectRecognizer}, который позволит распознать требуемый объект.
 *
 * @author vs
 */
public class ReferencedObjectResolver
    implements IUgwiResolver {

  /**
   * ИД фабрики
   */
  public static final String FACTORY_ID = "referencedObjectResolverFactory"; //$NON-NLS-1$

  private static final String PROPID_RECOGNIZER_CFG = SKVED_ID + ".prop.Recognizer"; //$NON-NLS-1$

  private static final IDataDef PROP_RECOGNIZER_CFG = DataDef.create( PROPID_RECOGNIZER_CFG, VALOBJ, //
      TSID_NAME, "Распознаватель", //
      TSID_DESCRIPTION, "Конфигурация распознавателя одного sk-объекта из многих", //
      TSID_KEEPER_ID, SkoRecognizerCfg.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( null ) //
  );

  private static final IStridablesList<IDataDef> dataDefs = new StridablesList<>( PROP_GWID, PROP_RECOGNIZER_CFG );

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

  @Override
  public Ugwi resolve( Ugwi aMaster ) {
    String essence = aMaster.essence();
    Gwid gwid = Gwid.of( essence );
    Skid skid = resolve( gwid.skid() );
    gwid = makeConcreteGwid( skid, gwid );
    // FIXME return Ugwi.of( UgwiKindGwid.KIND_ID, gwid.canonicalString() );
    return Ugwi.of( "gwid", gwid.canonicalString() );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  @SuppressWarnings( "static-method" )
  private boolean isLink( Gwid aGwid ) {
    return aGwid.propSectId().equals( EGwidKind.GW_LINK.id() );
  }

  @SuppressWarnings( "static-method" )
  private boolean isRivet( Gwid aGwid ) {
    return aGwid.propSectId().equals( EGwidKind.GW_RIVET.id() );
  }

  /**
   * Создает "конкретный" Gwid из абстрактного.<br>
   * Более точно - задает новому Gwid'у указанный Skid оъекта, сохраняя все остальные свойства.
   *
   * @param aSkid Skid - ИД объекта
   * @param aGwid Gwid - исходный Gwid
   * @return Gwid с указанным идентификатором объекта
   */
  public static Gwid makeConcreteGwid( Skid aSkid, Gwid aGwid ) {
    String propSectId = aGwid.propSectId();
    String propId = aGwid.propId();
    String subPropSectId = aGwid.subPropSectId();
    String subPropId = aGwid.subPropId();
    return Gwid.create( aSkid.classId(), aSkid.strid(), propSectId, propId, subPropSectId, subPropId );
  }

}
