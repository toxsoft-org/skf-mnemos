package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.rtc.impl.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Простая в начертании стрелка.
 *
 * @author vs
 */
public class RtcSimpleArrow
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.SimpleArrow"; //$NON-NLS-1$

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
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_FULCRUM );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      // fields.add( new TinFieldInfo( PROPID_IS_RIGHT, TTI_AT_BOOLEAN, PROP_IS_RIGHT.params() ) );
      // fields.add( new TinFieldInfo( PROPID_NOSE_LENGTH, TTI_AT_FLOATING, PROP_NOSE_LENGHT.params() ) );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_LINE_INFO );
      return new PropertableEntitiesTinTypeInfo<>( fields, RtcSimpleArrow.class );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      // TODO Auto-generated method stub
      return null;
    }

  };

  protected RtcSimpleArrow( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // nop
  }
}
