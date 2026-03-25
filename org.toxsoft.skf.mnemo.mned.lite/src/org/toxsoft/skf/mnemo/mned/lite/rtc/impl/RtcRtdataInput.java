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
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

/**
 * Поле ввода значения РВ-данного.
 * <p>
 *
 * @author vs
 */
public class RtcRtdataInput
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.RtdInput"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_RTDATA_INPUT, //
      TSID_DESCRIPTION, STR_RTC_RTDATA_INPUT_D, //
      TSID_ICON_ID, ICONID_RTC_RTDATA_INPUT, //
      PARAMID_CATEGORY, CATID_INPUT_FIELDS//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();

      fields.add( TFI_RTD_UGWI );
      fields.add( TFI_FORMAT_STRING );

      fields.add( TFI_TEXT );
      fields.add( TFI_FONT );
      fields.add( TFI_SWT_FG_COLOR );
      fields.add( TFI_SWT_BK_FILL );
      fields.add( TFI_SWT_BORDER_INFO );

      fields.add( TFI_HOR_ALIGNMENT );
      fields.add( TFI_VER_ALIGNMENT );

      fields.add( TFI_LEFT_INDENT );
      fields.add( TFI_TOP_INDENT );
      fields.add( TFI_RIGHT_INDENT );
      fields.add( TFI_BOTTOM_INDENT );

      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      return new PropertableEntitiesTinTypeInfo<>( fields, RtcRtdataInput.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( TFI_TEXT.id(), TFI_TEXT.id() );
      bindViselPropId( TFI_FONT.id(), TFI_FONT.id() );
      bindViselPropId( TFI_FG_COLOR.id(), TFI_FG_COLOR.id() );
      bindViselPropId( TFI_BK_FILL.id(), TFI_BK_FILL.id() );
      bindViselPropId( TFI_BORDER_INFO.id(), TFI_BORDER_INFO.id() );
      bindViselPropId( TFI_HOR_ALIGNMENT.id(), TFI_HOR_ALIGNMENT.id() );
      bindViselPropId( TFI_VER_ALIGNMENT.id(), TFI_VER_ALIGNMENT.id() );
      bindViselPropId( TFI_LEFT_INDENT.id(), TFI_LEFT_INDENT.id() );
      bindViselPropId( TFI_TOP_INDENT.id(), TFI_TOP_INDENT.id() );
      bindViselPropId( TFI_RIGHT_INDENT.id(), TFI_RIGHT_INDENT.id() );
      bindViselPropId( TFI_BOTTOM_INDENT.id(), TFI_BOTTOM_INDENT.id() );
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
        IVedViselFactory f = viselFactory( ViselLabel.FACTORY_ID, aVedScreen );
        VedItemCfg viselCfg = aVedScreen.model().visels().prepareFromTemplate( f.paletteEntries().first().itemCfg() );
        viselCfg.propValues().setDouble( PROPID_X, aCfg.params().getDouble( PROPID_X ) );
        viselCfg.propValues().setDouble( PROPID_Y, aCfg.params().getDouble( PROPID_Y ) );
        v = aVedScreen.model().visels().create( viselCfg );

        IVedActorFactory af = actorFactory( SkActorInputField.FACTORY_ID, aVedScreen );
        VedItemCfg actorCfg = aVedScreen.model().actors().prepareFromTemplate( af.paletteEntries().first().itemCfg() );
        actorCfg.propValues().setStr( PROPID_VISEL_ID, v.id() );
        actorCfg.propValues().setStr( PROPID_VISEL_PROP_ID, PROPID_TEXT );
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

        return new RtcRtdataInput( cfg, propDefs(), aVedScreen );
      }
      return null;
    }
  };

  VedAbstractActor actor = null;

  protected RtcRtdataInput( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    if( aConfig.params().hasKey( IRtControlCfg.PROPID_ACTORS_IDS ) ) {
      StringArrayList aIds = aConfig.params().getValobj( IRtControlCfg.PROPID_ACTORS_IDS );
      actor = actors.getByKey( aIds.first() );
      if( actor.props().hasKey( TFI_GWID.id() ) ) {
        Gwid gwid = actor.props().getValobj( TFI_GWID.id() );
        if( gwid != null && !gwid.isMulti() ) {
          Ugwi ugwi = UgwiKindSkRtdata.makeUgwi( gwid.skid(), gwid.propId() );
          props().setValobj( TFI_RTD_UGWI.id(), ugwi );
        }
      }
      if( actor.props().hasKey( TFI_FORMAT_STRING.id() ) ) {
        props().setStr( TFI_FORMAT_STRING.id(), actor.props().getStr( TFI_FORMAT_STRING.id() ) );
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
        actor.props().setValobj( SkActorInputField.TFI_SOURCE_GWID.id(), gwid );
      }
      if( aChangedValues.keys().hasElem( TFI_FORMAT_STRING.id() ) ) {
        String fmtStr = aChangedValues.getStr( TFI_FORMAT_STRING.id() );
        actor.props().setStr( TFI_FORMAT_STRING.id(), fmtStr );
      }
    }
  }

}
