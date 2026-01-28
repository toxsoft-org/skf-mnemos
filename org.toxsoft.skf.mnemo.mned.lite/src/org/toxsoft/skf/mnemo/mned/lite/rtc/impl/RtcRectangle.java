package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

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
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Статическая прямоугольная область с заливкой и рамкой.
 *
 * @author vs
 */
public class RtcRectangle
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.Rectangle"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_RECTANGLE, //
      TSID_DESCRIPTION, STR_RTC_RECTANGLE_D, //
      TSID_ICON_ID, ICONID_RTC_RECTANGLE, //
      PARAMID_CATEGORY, CATID_GEOMETRY//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_BK_FILL );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      return new PropertableEntitiesTinTypeInfo<>( fields, RtcRectangle.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( PROPID_BK_FILL, PROPID_BK_FILL );
      bindViselPropId( PROPID_BORDER_INFO, PROPID_BORDER_INFO );
      bindViselPropId( PROPID_X, PROPID_X );
      bindViselPropId( PROPID_Y, PROPID_Y );
      bindViselPropId( PROPID_WIDTH, PROPID_WIDTH );
      bindViselPropId( PROPID_HEIGHT, PROPID_HEIGHT );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      VedAbstractVisel v = null;
      // String viselId = TsLibUtils.EMPTY_STRING;
      // if( aCfg.params().hasKey( PROPID_VISEL_ID ) ) {
      // viselId = aCfg.params().getStr( PROPID_VISEL_ID );
      // }
      // if( viselId.isBlank() ) { // создание с нуля

      if( aCfg.viselId().isBlank() ) { // создание с нуля
        IVedViselFactory f = viselFactory( ViselRectangle.FACTORY_ID, aVedScreen );
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
        return new RtcRectangle( cfg, propDefs(), aVedScreen );
      }
      return null;
    }
  };

  protected RtcRectangle( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // nop
    // VedAbstractVisel visel = aVedScreen.model().visels().list().getByKey( aConfig.viselId() );
    // visel.props().propsEventer().addListener( null );
  }

}
