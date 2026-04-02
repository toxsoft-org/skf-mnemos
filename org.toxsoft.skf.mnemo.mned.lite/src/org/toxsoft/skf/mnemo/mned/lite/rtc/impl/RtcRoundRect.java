package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.comps.ViselRoundRect.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
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
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Статическая прямоугольная область со скругленными углами с заливкой и рамкой.
 *
 * @author vs
 */
public class RtcRoundRect
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.RoundRect"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_ROUNDRECT, //
      TSID_DESCRIPTION, STR_RTC_ROUNDRECT_D, //
      TSID_ICON_ID, ICONID_VISEL_ROUND_RECT, //
      PARAMID_CATEGORY, CATID_DECORATORS//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo( IStridablesListEdit<ITinFieldInfo> aFields ) {
      aFields.add( TFI_X );
      aFields.add( TFI_Y );
      aFields.add( TFI_WIDTH );
      aFields.add( TFI_HEIGHT );
      aFields.add( TFI_FULCRUM );
      aFields.add( new TinFieldInfo( PROPID_ARC_WIDTH, TTI_AT_FLOATING, PROP_ARC_WIDTH.params() ) );
      aFields.add( new TinFieldInfo( PROPID_ARC_HEIGHT, TTI_AT_FLOATING, PROP_ARC_HEIGHT.params() ) );
      aFields.add( TFI_BK_FILL );
      aFields.add( TFI_FG_COLOR );
      aFields.add( TFI_LINE_INFO );
      return new PropertableEntitiesTinTypeInfo<>( aFields, RtcRoundRect.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( TFI_X.id(), TFI_X.id() );
      bindViselPropId( TFI_Y.id(), TFI_Y.id() );
      bindViselPropId( TFI_WIDTH.id(), TFI_WIDTH.id() );
      bindViselPropId( TFI_HEIGHT.id(), TFI_HEIGHT.id() );
      bindViselPropId( PROPID_ARC_WIDTH, PROPID_ARC_WIDTH );
      bindViselPropId( PROPID_ARC_HEIGHT, PROPID_ARC_HEIGHT );
      bindViselPropId( TFI_BK_FILL.id(), TFI_BK_FILL.id() );
      bindViselPropId( TFI_FG_COLOR.id(), TFI_FG_COLOR.id() );
      bindViselPropId( TFI_LINE_INFO.id(), TFI_LINE_INFO.id() );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      VedAbstractVisel v = null;

      if( aCfg.viselId().isBlank() ) { // создание с нуля
        IVedViselFactory f = viselFactory( ViselRoundRect.FACTORY_ID, aVedScreen );
        VedItemCfg viselCfg = aVedScreen.model().visels().prepareFromTemplate( f.paletteEntries().first().itemCfg() );
        viselCfg.propValues().setDouble( PROPID_X, aCfg.params().getDouble( PROPID_X ) );
        viselCfg.propValues().setDouble( PROPID_Y, aCfg.params().getDouble( PROPID_Y ) );
        v = aVedScreen.model().visels().create( viselCfg );
      }
      else {
        v = aVedScreen.model().visels().list().getByKey( aCfg.viselId() );
      }

      if( v != null ) {
        IOptionSetEdit params = new OptionSet();
        params.setDouble( PROPID_X, v.props().getDouble( PROPID_X ) );
        params.setDouble( PROPID_Y, v.props().getDouble( PROPID_Y ) );
        params.setStr( PROPID_VISEL_ID, v.id() );
        RtControlCfg cfg = new RtControlCfg( v.id(), FACTORY_ID, params );
        return new RtcRoundRect( cfg, propDefs(), aVedScreen );
      }
      return null;
    }

  };

  protected RtcRoundRect( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

}
