package org.toxsoft.skf.mnemo.gui.skved;

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
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Actor: reads specified RTDATA value and supplies it to the VISEL as a text.
 *
 * @author hazard157
 */
public class SkActorRtdataText
    extends AbstractSkVedActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RtdataText"; //$NON-NLS-1$

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
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRtdataText.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRtdataText( aCfg, propDefs(), aVedScreen );
    }

  };

  private Ugwi         ugwi      = null;
  private Gwid         gwid      = null;
  private IUgwiList    ugwiList  = IUgwiList.EMPTY;
  private String       fmtStr    = null;
  private IAtomicValue lastValue = IAtomicValue.NULL;

  SkActorRtdataText( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractActor
  //

  @Override
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // check and don't allow to set invalid GWID
    removeWrongUgwi( PROPID_RTD_UGWI, UgwiKindSkRtdata.KIND_ID, aValuesToSet );
    if( aValuesToSet.hasKey( PROPID_RTD_UGWI ) ) {
      IAtomicValue av = aValuesToSet.getValue( PROPID_RTD_UGWI );
      if( av.isAssigned() ) {
        ugwi = aValuesToSet.getValobj( PROP_RTD_UGWI );
        if( ugwi != null ) {
          gwid = UgwiKindSkRtdata.getGwid( ugwi );
          ugwiList = UgwiList.createDirect( new ElemArrayList<>( ugwi ) );
        }
        else {
          gwid = null;
          ugwiList = IUgwiList.EMPTY;
        }
      }
    }
    // if( aValuesToSet.hasKey( PROPID_RTD_UGWI ) ) {
    // IAtomicValue av = aValuesToSet.getValue( PROP_RTD_UGWI );
    // if( !av.isAssigned() ) {
    // aValuesToSet.remove( PROPID_RTD_UGWI );
    // }
    // else {
    // Ugwi ug = av.asValobj();
    // if( ug != Ugwi.NONE && !ug.kindId().equals( UgwiKindSkRtdata.KIND_ID ) ) {
    // aValuesToSet.remove( PROPID_RTD_UGWI );
    // }
    // }
    // }
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_RTD_UGWI ) ) {
      IAtomicValue av = aChangedValues.getValue( PROPID_RTD_UGWI );
      if( av.isAssigned() ) {
        ugwi = aChangedValues.getValobj( PROP_RTD_UGWI );
        if( ugwi != null ) {
          gwid = UgwiKindSkRtdata.getGwid( ugwi );
          ugwiList = UgwiList.createDirect( new ElemArrayList<>( ugwi ) );
        }
        else {
          gwid = null;
          ugwiList = IUgwiList.EMPTY;
        }
      }
    }
    if( aChangedValues.hasKey( PROPID_FORMAT_STRING ) ) {
      fmtStr = aChangedValues.getStr( PROP_FORMAT_STRING );
      if( fmtStr.isBlank() && ugwi != null ) {
        fmtStr = null;
        ISkClassInfo classInfo = skSysdescr().findClassInfo( UgwiKindSkRtdata.getClassId( ugwi ) );
        if( classInfo != null ) {
          IDtoRtdataInfo rtdInfo = classInfo.rtdata().list().findByKey( UgwiKindSkRtdata.getRtdataId( ugwi ) );
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
  }

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    IAtomicValue newValue = skVedEnv().getRtDataValue( gwid );
    if( !newValue.equals( lastValue ) ) {
      String text = AvUtils.printAv( fmtStr, newValue );
      setStdViselPropValue( avStr( text ) );
      lastValue = newValue;
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected IGwidList doListUsedGwids() {
    GwidList gl = new GwidList();
    for( Ugwi u : ugwiList.items() ) {
      gl.add( UgwiKindSkRtdata.getGwid( u ) );
    }
    return gl;
  }

}
