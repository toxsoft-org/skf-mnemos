package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Actor: reads specified RTDATA value and supplies it to the VISEL as a text.
 *
 * @author hazard157, vs
 */
public class SkActorRtdataText
    extends AbstractSkActorSingleRtDataConsumer {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RtdataText"; //$NON-NLS-1$

  static final IDataDef PROP_NULL_TEXT = DataDef.create3( "piNullText", DDEF_NAME, // //$NON-NLS-1$
      TSID_NAME, STR_NULL_TEXT, //
      TSID_DESCRIPTION, STR_NULL_TEXT_D, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  static final ITinFieldInfo TFI_NULL_TEXT = new TinFieldInfo( PROP_NULL_TEXT, TTI_AT_STRING );

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
      fields.add( TFI_NULL_TEXT );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRtdataText.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRtdataText( aCfg, propDefs(), aVedScreen );
    }

  };

  private String fmtStr       = null;
  private String nullValueStr = TsLibUtils.EMPTY_STRING;

  SkActorRtdataText( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkActorSingleRtDataConsumer
  //

  @Override
  protected void doDoUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_FORMAT_STRING ) ) {
      fmtStr = aChangedValues.getStr( PROP_FORMAT_STRING );
      if( fmtStr.isBlank() && ugwi() != null ) {
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
    if( aChangedValues.hasKey( PROP_NULL_TEXT.id() ) ) {
      nullValueStr = aChangedValues.getStr( PROP_NULL_TEXT );
    }
  }

  @Override
  protected void doOnValueChanged( IAtomicValue aNewValue ) {
    if( aNewValue == null || !aNewValue.isAssigned() ) {
      setStdViselPropValue( avStr( nullValueStr ) );
      return;
    }
    String text = AvUtils.printAv( fmtStr, aNewValue );
    setStdViselPropValue( avStr( text ) );
  }

}
