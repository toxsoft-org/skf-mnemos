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
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Bar value indicator.
 *
 * @author vs
 */
public class RtcLinearGauge
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.LinearGauge"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_LINEAR_GAUGE, //
      TSID_DESCRIPTION, STR_RTC_LINEAR_GAUGE_D, //
      TSID_ICON_ID, ICONID_RTC_LINEAR_GAUGE, //
      PARAMID_CATEGORY, CATID_GAUGE//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_RTD_UGWI );
      fields.add( TFI_ORIENTATION );
      fields.add( ViselLinearGauge.TFI_VALUE );
      fields.add( ViselLinearGauge.TFI_MIN_VALUE );
      fields.add( ViselLinearGauge.TFI_MAX_VALUE );
      fields.add( TFI_BK_FILL );
      fields.add( ViselLinearGauge.TFI_VALUE_FILL );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      return new PropertableEntitiesTinTypeInfo<>( fields, AbstractRtControl.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( TFI_ORIENTATION.id(), TFI_ORIENTATION.id() );
      bindViselPropId( ViselLinearGauge.TFI_VALUE.id(), ViselLinearGauge.TFI_VALUE.id() );
      bindViselPropId( ViselLinearGauge.TFI_MIN_VALUE.id(), ViselLinearGauge.TFI_MIN_VALUE.id() );
      bindViselPropId( ViselLinearGauge.TFI_MAX_VALUE.id(), ViselLinearGauge.TFI_MAX_VALUE.id() );
      bindViselPropId( TFI_BK_FILL.id(), TFI_BK_FILL.id() );
      bindViselPropId( ViselLinearGauge.TFI_VALUE_FILL.id(), ViselLinearGauge.TFI_VALUE_FILL.id() );
      bindViselPropId( TFI_BORDER_INFO.id(), TFI_BORDER_INFO.id() );
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
        IVedViselFactory f = viselFactory( ViselLinearGauge.FACTORY_ID, aVedScreen );
        VedItemCfg viselCfg = aVedScreen.model().visels().prepareFromTemplate( f.paletteEntries().first().itemCfg() );
        viselCfg.propValues().setDouble( PROPID_X, aCfg.params().getDouble( PROPID_X ) );
        viselCfg.propValues().setDouble( PROPID_Y, aCfg.params().getDouble( PROPID_Y ) );
        v = aVedScreen.model().visels().create( viselCfg );

        IVedActorFactory af = actorFactory( SkActorRtdataValue.FACTORY_ID, aVedScreen );
        VedItemCfg actorCfg = aVedScreen.model().actors().prepareFromTemplate( af.paletteEntries().first().itemCfg() );
        actorCfg.propValues().setStr( PROPID_VISEL_ID, v.id() );
        actorCfg.propValues().setStr( PROPID_VISEL_PROP_ID, ViselLinearGauge.TFI_VALUE.id() );
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
        StringArrayList actorIds = new StringArrayList();
        actorIds.add( actor.id() );
        params.setValobj( IRtControlCfg.PROPID_ACTORS_IDS, actorIds );
        RtControlCfg cfg = new RtControlCfg( v.id(), FACTORY_ID, params );

        bindActorPropId( actor.id(), TFI_RTD_UGWI.id(), TFI_RTD_UGWI.id() );

        return new RtcLinearGauge( cfg, propDefs(), aVedScreen );
      }
      return null;
    }

  };

  protected RtcLinearGauge( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

}
