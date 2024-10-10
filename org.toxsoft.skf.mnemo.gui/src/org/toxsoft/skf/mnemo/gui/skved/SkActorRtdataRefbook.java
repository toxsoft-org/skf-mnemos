package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.refbooks.lib.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Actor: reads specified RTDATA value and supplies it to the VISEL as a text.
 *
 * @author hazard157
 */
public class SkActorRtdataRefbook
    extends AbstractSkActorSingleRtDataConsumer {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RtdataRefbook"; //$NON-NLS-1$

  private static final String REFBOOK_INFO_ID = "refbookInfo"; //$NON-NLS-1$

  private static final IDataDef PROP_REFBOOK_INFO = DataDef.create( REFBOOK_INFO_ID, VALOBJ, //
      TSID_NAME, "Справочник", //
      TSID_DESCRIPTION, "Информация о справочнике", //
      TSID_KEEPER_ID, IdChain.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjRtdataRefbookAttrInfo.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( IdChain.NULL ) //
  );

  static final ITinTypeInfo TTI_REFBOOK_INFO = new TinAtomicTypeInfo.TtiValobj<>( PROP_REFBOOK_INFO, IdChain.class );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_RTDATA_TEXT, //
      TSID_DESCRIPTION, STR_ACTOR_RTDATA_TEXT_D, //
      TSID_ICON_ID, ICONID_VED_RT_EDIT_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_FORMAT_STRING );
      fields.add( TFI_RTD_UGWI );
      fields.add( new TinFieldInfo( PROP_REFBOOK_INFO, TTI_REFBOOK_INFO ) );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRtdataRefbook.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRtdataRefbook( aCfg, propDefs(), aVedScreen );
    }

  };

  private String fmtStr = null;

  ISkRefbook refbook;

  IdChain rbChain;

  SkActorRtdataRefbook( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkActorSingleRtDataConsumer
  //

  @Override
  protected void doDoUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_FORMAT_STRING ) ) {
      fmtStr = aChangedValues.getStr( PROP_FORMAT_STRING );
      if( fmtStr.isBlank() && ugwi() != null && ugwi() != Ugwi.NONE ) {
        fmtStr = null;
        ISkClassInfo classInfo = skSysdescr().findClassInfo( UgwiKindSkRtdata.getClassId( ugwi() ) );
        if( classInfo != null ) {
          IDtoRtdataInfo rtdInfo = classInfo.rtdata().list().findByKey( UgwiKindSkRtdata.getRtdataId( ugwi() ) );
          if( rtdInfo != null ) {
            IAtomicValue avFmtStr = SkHelperUtils.getConstraint( rtdInfo, TSID_FORMAT_STRING );
            if( avFmtStr != null ) {
              fmtStr = avFmtStr.asString();
            }
          }
        }
      }
      if( fmtStr != null && fmtStr.isBlank() ) {
        fmtStr = null;
      }
    }
    if( aChangedValues.hasKey( PROP_REFBOOK_INFO.id() ) ) {
      IAtomicValue av = aChangedValues.getValue( PROP_REFBOOK_INFO.id() );
      if( av.isAssigned() ) {
        rbChain = av.asValobj();
        ISkRefbookService rbServ = coreApi().getService( ISkRefbookService.SERVICE_ID );
        refbook = rbServ.findRefbook( rbChain.get( 0 ) );
        if( refbook == null ) {
          LoggerUtils.errorLogger().warning( "Refbook %s not found", rbChain.get( 0 ) ); //$NON-NLS-1$
        }
      }
      else {
        refbook = null;
      }
    }
  }

  @Override
  protected void doOnValueChanged( IAtomicValue aNewValue ) {
    // String text = AvUtils.printAv( fmtStr, aNewValue );
    // setStdViselPropValue( avStr( text ) );
    if( refbook != null ) {
      for( ISkRefbookItem item : refbook.listItems() ) {
        IAtomicValue av = item.attrs().getValue( rbChain.get( 1 ) );
        if( av.equals( aNewValue ) ) {
          setStdViselPropValue( item.attrs().getValue( rbChain.get( 2 ) ) );
          break;
        }
      }
    }
  }

}
