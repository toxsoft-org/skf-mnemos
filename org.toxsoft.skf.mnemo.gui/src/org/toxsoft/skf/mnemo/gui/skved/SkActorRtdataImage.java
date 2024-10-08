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
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.tools.imageset.*;

/**
 * Actor: reads specified RTDATA value, select the image descriptor from set of descriptors using value as index and set
 * it to Visel's property.
 * <p>
 *
 * @author hazard157
 */
public class SkActorRtdataImage
    extends AbstractSkActorSingleRtDataConsumer {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RtdataImage"; //$NON-NLS-1$

  static final String PROPID_IMAGE_SET = SKVED_ID + ".prop.ImageSet"; //$NON-NLS-1$

  static final IDataDef PROP_IMAGE_SET = DataDef.create( PROPID_IMAGE_SET, VALOBJ, //
      TSID_NAME, STR_IMAGE_SET, //
      TSID_DESCRIPTION, STR_IMAGE_SET_D, //
      TSID_KEEPER_ID, MnemoImageSetInfo.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjImageInfoesSet.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( IMnemoImageSetInfo.EMPTY ) //
  );

  static final ITinTypeInfo TTI_IMAGE_SET =
      new TinAtomicTypeInfo.TtiValobj<>( PROP_IMAGE_SET, MnemoImageSetInfo.class );

  static final ITinFieldInfo TFI_IMAGE_SET = new TinFieldInfo( PROP_IMAGE_SET, TTI_IMAGE_SET );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_RTDATA_IMAGE, //
      TSID_DESCRIPTION, STR_ACTOR_RTDATA_IMAGE_D, //
      TSID_ICON_ID, ICONID_VED_RTDATA_IMG_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_RTD_UGWI );
      fields.add( TFI_IMAGE_SET );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRtdataImage.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRtdataImage( aCfg, propDefs(), aVedScreen );
    }

  };

  IMnemoImageSetInfo imageSet = IMnemoImageSetInfo.EMPTY;

  SkActorRtdataImage( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkActorSingleRtDataConsumer
  //

  @Override
  protected void doDoUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_IMAGE_SET ) ) {
      imageSet = props().getValobj( PROP_IMAGE_SET );
    }
    if( !props().getStr( PROPID_VISEL_ID ).isBlank() ) {
      if( imageSet != null && imageSet.imageInfoes().size() > 0 ) {
        TsImageDescriptor imd = imageSet.imageInfoes().get( 0 ).imageDescriptor();
        setStdViselPropValue( avValobj( imd ) );
      }
    }
  }

  @Override
  protected void doOnValueChanged( IAtomicValue aNewValue ) {
    if( aNewValue.isAssigned() ) {
      TsImageDescriptor imd = imageSet.imageInfoes().get( aNewValue.asInt() ).imageDescriptor();
      setStdViselPropValue( avValobj( imd ) );
    }
    else {
      setStdViselPropValue( avValobj( TsImageDescriptor.NONE ) );
    }
  }

}
