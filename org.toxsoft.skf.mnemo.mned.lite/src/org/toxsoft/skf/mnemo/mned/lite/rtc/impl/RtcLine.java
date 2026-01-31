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
 * Статический элемент - линия.
 *
 * @author vs
 */
public class RtcLine
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.Line"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_LINE, //
      TSID_DESCRIPTION, STR_RTC_LINE_D, //
      TSID_ICON_ID, ICONID_RTC_LINE, //
      PARAMID_CATEGORY, CATID_GEOMETRY//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_LINE_INFO );
      fields.add( TFI_COLOR_DESCRIPTOR );
      fields.add( TinFieldInfo.makeCopy( TFI_X, TSID_NAME, "x1" ) ); //$NON-NLS-1$
      fields.add( TinFieldInfo.makeCopy( TFI_Y, TSID_NAME, "y1" ) ); //$NON-NLS-1$
      fields.add( TinFieldInfo.makeCopy( ViselLine.TFI_X2, TSID_NAME, "x2" ) ); //$NON-NLS-1$
      fields.add( TinFieldInfo.makeCopy( ViselLine.TFI_Y2, TSID_NAME, "y2" ) ); //$NON-NLS-1$
      return new PropertableEntitiesTinTypeInfo<>( fields, RtcLine.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( TFI_LINE_INFO.id(), TFI_LINE_INFO.id() );
      bindViselPropId( TFI_COLOR_DESCRIPTOR.id(), TFI_COLOR_DESCRIPTOR.id() );
      bindViselPropId( TFI_X.id(), TFI_X.id() );
      bindViselPropId( TFI_Y.id(), TFI_Y.id() );
      bindViselPropId( ViselLine.TFI_X2.id(), ViselLine.TFI_X2.id() );
      bindViselPropId( ViselLine.TFI_Y2.id(), ViselLine.TFI_X2.id() );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      VedAbstractVisel v = null;
      if( aCfg.viselId().isBlank() ) { // создание с нуля
        IVedViselFactory f = viselFactory( ViselLine.FACTORY_ID, aVedScreen );
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
        return new RtcLine( cfg, propDefs(), aVedScreen );
      }
      return null;
    }
  };

  protected RtcLine( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // nop
  }
}
