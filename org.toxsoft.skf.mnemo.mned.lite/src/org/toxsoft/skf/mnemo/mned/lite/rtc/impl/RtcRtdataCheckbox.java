package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.rtc.impl.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.mned.lite.actors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

/**
 * Checkbox for direct setting boolean rt-data value (no command).
 * <p>
 *
 * @author vs
 */
public class RtcRtdataCheckbox
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.RtdataCheckbox"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_RTDATA_CHECKBOX, //
      TSID_DESCRIPTION, STR_RTC_RTDATA_CHECKBOX_D, //
      TSID_ICON_ID, ICONID_RTC_RTDATA_CHECKBOX, //
      PARAMID_CATEGORY, CATID_INPUT_FIELDS//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo( IStridablesListEdit<ITinFieldInfo> aFields ) {
      aFields.add( TFI_RTD_UGWI );
      aFields.add( TFI_TEXT );
      aFields.add( TFI_FONT );
      aFields.add( TFI_X );
      aFields.add( TFI_Y );
      aFields.add( TFI_WIDTH );
      aFields.add( TFI_HEIGHT );
      // aFields.add( TFI_FG_COLOR );
      // aFields.add( TFI_BK_COLOR );
      return new PropertableEntitiesTinTypeInfo<>( aFields, RtcRtdataCheckbox.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( TFI_TEXT.id(), TFI_TEXT.id() );
      bindViselPropId( TFI_FONT.id(), TFI_FONT.id() );
      bindViselPropId( TFI_X.id(), TFI_X.id() );
      bindViselPropId( TFI_Y.id(), TFI_Y.id() );
      bindViselPropId( TFI_WIDTH.id(), TFI_WIDTH.id() );
      bindViselPropId( TFI_HEIGHT.id(), TFI_HEIGHT.id() );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      VedAbstractVisel v = null;
      VedAbstractActor actor = null;
      if( aCfg.viselId().isBlank() ) { // создание с нуля
        // IVedViselFactory f = viselFactory( ViselCheckbox.FACTORY_ID, aVedScreen );
        // VedItemCfg viselCfg = aVedScreen.model().visels().prepareFromTemplate( f.paletteEntries().first().itemCfg()
        // );

        VedItemCfg viselCfg = createViselCfg( ViselCheckbox.FACTORY_ID, aVedScreen, "RtdataCheckbox" ); //$NON-NLS-1$
        viselCfg.propValues().setDouble( PROPID_X, aCfg.params().getDouble( PROPID_X ) );
        viselCfg.propValues().setDouble( PROPID_Y, aCfg.params().getDouble( PROPID_Y ) );
        v = aVedScreen.model().visels().create( viselCfg );

        IVedActorFactory af = actorFactory( LiteActorRtdataCheckbox.FACTORY_ID, aVedScreen );
        VedItemCfg actorCfg = aVedScreen.model().actors().prepareFromTemplate( af.paletteEntries().first().itemCfg() );
        actorCfg.propValues().setStr( PROPID_VISEL_ID, v.id() );
        actor = aVedScreen.model().actors().create( actorCfg );
      }
      else {
        v = aVedScreen.model().visels().list().getByKey( aCfg.viselId() );
        actor = aVedScreen.model().actors().list().getByKey( aCfg.actorIds().first() );
      }

      if( v != null && actor != null ) {
        IOptionSetEdit params = new OptionSet();
        params.setDouble( PROPID_X, v.props().getDouble( PROPID_X ) );
        params.setDouble( PROPID_Y, v.props().getDouble( PROPID_Y ) );
        params.setStr( PROPID_VISEL_ID, v.id() );
        params.setStr( PROPID_VISEL_PROP_ID, ViselCheckbox.TFI_CHECKED.id() );
        StringArrayList actorIds = new StringArrayList();
        actorIds.add( actor.id() );
        params.setValobj( IRtControlCfg.PROPID_ACTORS_IDS, actorIds );
        RtControlCfg cfg = new RtControlCfg( v.id(), FACTORY_ID, params );

        return new RtcRtdataCheckbox( cfg, propDefs(), aVedScreen );
      }
      return null;
    }

  };

  VedAbstractActor actor = null;

  protected RtcRtdataCheckbox( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    if( aConfig.params().hasKey( IRtControlCfg.PROPID_ACTORS_IDS ) ) {
      StringArrayList aIds = aConfig.params().getValobj( IRtControlCfg.PROPID_ACTORS_IDS );
      actor = actors.getByKey( aIds.first() );
      if( actor.props().hasKey( TFI_GWID.id() ) ) {
        Gwid gwid = actor.props().getValobj( TFI_GWID.id() );
        if( !gwid.isMulti() ) {
          Ugwi ugwi = UgwiKindSkRtdata.makeUgwi( gwid.skid(), gwid.propId() );
          props().setValobj( TFI_RTD_UGWI.id(), ugwi );
        }
      }
    }
  }

  @Override
  protected void updateActorProps( IOptionSet aChangedValues ) {
    if( actor != null ) {
      if( aChangedValues.keys().hasElem( TFI_RTD_UGWI.id() ) ) {
        Ugwi ugwi = aChangedValues.getValobj( TFI_RTD_UGWI.id() );
        Gwid gwid = UgwiKindSkRtdata.INSTANCE.getGwid( ugwi );
        actor.props().setValobj( TFI_GWID.id(), gwid );
      }
    }
  }

}
