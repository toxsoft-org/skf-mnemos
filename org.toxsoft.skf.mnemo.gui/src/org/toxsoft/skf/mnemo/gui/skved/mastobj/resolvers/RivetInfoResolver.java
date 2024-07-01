package org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Разрешает UGWI типа {@link UgwiKindSkRivetInfo} в UGWI типа {@link UgwiKindSkSkid}.
 * <p>
 * То есть из абстрактного UGWI делает конкретный, получая на вход UGWI типа {@link UgwiKindSkSkid}, а затем из
 * множества объектов, на которые ссылается rivet, выбирает один.
 *
 * @author vs
 */
public class RivetInfoResolver
    extends AbstractSimpleResolver {

  /**
   * ИД фабрики
   */
  public static final String FACTORY_ID = "rivetInfoResolverFactory"; //$NON-NLS-1$

  /**
   * ИД свойства содержащего конфигурацию распознавателя
   */
  public static final String PROPID_RECOGNIZER_CFG = SKVED_ID + ".prop.Recognizer"; //$NON-NLS-1$

  private static final IDataDef PROP_RECOGNIZER_CFG = DataDef.create( PROPID_RECOGNIZER_CFG, VALOBJ, //
      TSID_NAME, "Распознаватель", //
      TSID_DESCRIPTION, "Конфигурация распознавателя одного sk-объекта из многих", //
      TSID_KEEPER_ID, SkoRecognizerCfg.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( null ) //
  );

  private static final IStridablesList<IDataDef> dataDefs = new StridablesList<>( PROP_UGWI, PROP_RECOGNIZER_CFG );

  /**
   * Фабрика создания "разрешителя"
   */
  public static final ISimpleResolverFactory FACTORY = new AbstractSimpleResolverFactory( //
      FACTORY_ID, new OptionSet(), dataDefs ) {

    @Override
    protected AbstractSimpleResolver doCreateResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
      return new RivetInfoResolver( aResolverConfig, aSkConn );
    }
  };

  private final ISkUgwiService ugwiService;

  /**
   * Конструктор.
   *
   * @param aResolverConfig {@link IOptionSet} - конфигурация "разрешителя"
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   */
  public RivetInfoResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    super( aResolverConfig, aSkConn );
    ugwiService = (ISkUgwiService)coreApi().services().getByKey( ISkUgwiService.SERVICE_ID );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSimpleResolver
  //

  @Override
  protected Ugwi doResolve( Ugwi aMaster ) {
    Ugwi ugwi = cfg().getValobj( PROPID_UGWI );
    if( aMaster.kindId().equals( UgwiKindSkSkid.KIND_ID ) ) {
      Skid masterSkid = ugwiService.findContentAs( aMaster, Skid.class );
      Ugwi rivetUgwi = UgwiKindSkRivetInfo.makeUgwi( masterSkid, UgwiKindSkRivetInfo.getRivetId( ugwi ) );
      ISkidList skids = ugwiService.findContentAs( rivetUgwi, ISkidList.class );

      ISkObjectRecognizer recognizer = recognizer();
      if( recognizer != null ) {
        for( Skid skid : skids ) {
          if( recognizer.recognize( skid, coreApi() ) ) {
            return UgwiKindSkSkid.makeUgwi( skid );
          }
        }
      }
      else {
        if( skids.size() > 0 ) {
          return UgwiKindSkSkid.makeUgwi( skids.first() );
        }
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  ISkObjectRecognizer recognizer() {
    if( cfg().hasKey( PROPID_RECOGNIZER_CFG ) ) {
      IAtomicValue value = cfg().getValue( PROP_RECOGNIZER_CFG );
      if( value.isAssigned() ) {
        return value.asValobj();
      }
    }
    return null;
  }

}
