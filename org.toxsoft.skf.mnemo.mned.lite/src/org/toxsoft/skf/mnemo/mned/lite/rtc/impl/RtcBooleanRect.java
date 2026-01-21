package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.rtc.impl.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

public class RtcBooleanRect
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.boolean.Lamp"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_ELLIPSE, //
      TSID_DESCRIPTION, STR_RTC_ELLIPSE_D, //
      TSID_ICON_ID, ICONID_RTC_ELLIPSE, //
      PARAMID_CATEGORY, CATID_BOOLEAN_LAMP//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_BK_FILL );
      fields.add( TFI_COLOR_DESCRIPTOR );
      fields.add( TFI_BORDER_INFO );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      return new PropertableEntitiesTinTypeInfo<>( fields, RtcBooleanRect.class );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      VedAbstractVisel v = null;
      if( aCfg.viselId().isBlank() ) { // создание с нуля
        IVedViselFactory f = viselFactory( RtcBooleanRect.FACTORY_ID, aVedScreen );
        v = f.create( f.paletteEntries().first().itemCfg(), aVedScreen );
      }
      if( v != null ) {
        IOptionSetEdit params = new OptionSet();
        params.setStr( PROPID_VISEL_ID, v.id() );
        RtControlCfg cfg = new RtControlCfg( v.id(), FACTORY_ID, params );
        return new RtcBooleanRect( cfg, propDefs(), aVedScreen );
      }
      return null;
    }
  };

  protected RtcBooleanRect( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // TODO Auto-generated constructor stub
    VedAbstractVisel visel = aVedScreen.model().visels().list().getByKey( aConfig.viselId() );
    visel.props().propsEventer().addListener( null );
  }

}
