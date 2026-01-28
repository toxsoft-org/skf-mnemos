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
 * Статическая эллиптическая область с заливкой и рамкой.
 *
 * @author vs
 */
public class RtcEllipse
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.Ellipse"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_ELLIPSE, //
      TSID_DESCRIPTION, STR_RTC_ELLIPSE_D, //
      TSID_ICON_ID, ICONID_RTC_ELLIPSE, //
      PARAMID_CATEGORY, CATID_GEOMETRY//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_BK_FILL );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      return new PropertableEntitiesTinTypeInfo<>( fields, RtcEllipse.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( PROPID_BK_FILL, PROPID_BK_FILL );
      bindViselPropId( PROPID_FG_COLOR, PROPID_FG_COLOR );
      bindViselPropId( PROPID_LINE_INFO, PROPID_LINE_INFO );
      bindViselPropId( PROPID_X, PROPID_X );
      bindViselPropId( PROPID_Y, PROPID_Y );
      bindViselPropId( PROPID_WIDTH, PROPID_WIDTH );
      bindViselPropId( PROPID_HEIGHT, PROPID_HEIGHT );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      VedAbstractVisel v = null;
      if( aCfg.viselId().isBlank() ) { // создание с нуля
        IVedViselFactory f = viselFactory( ViselEllipse.FACTORY_ID, aVedScreen );
        VedItemCfg viselCfg = aVedScreen.model().visels().prepareFromTemplate( f.paletteEntries().first().itemCfg() );
        viselCfg.propValues().setDouble( PROPID_X, aCfg.params().getDouble( PROPID_X ) );
        viselCfg.propValues().setDouble( PROPID_Y, aCfg.params().getDouble( PROPID_Y ) );
        v = aVedScreen.model().visels().create( viselCfg );
      }
      if( v != null ) {
        IOptionSetEdit params = new OptionSet();
        params.setStr( PROPID_VISEL_ID, v.id() );
        RtControlCfg cfg = new RtControlCfg( v.id(), FACTORY_ID, params );
        return new RtcEllipse( cfg, propDefs(), aVedScreen );
      }
      return null;
    }
  };

  protected RtcEllipse( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // nop
    // VedAbstractVisel visel = aVedScreen.model().visels().list().getByKey( aConfig.viselId() );
    // visel.props().propsEventer().addListener( null );
  }

}
