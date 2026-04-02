package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.rtc.impl.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.skf.mnemo.mned.lite.actors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Rectangular lamp to show boolean value as black color for false and specified color for true.
 * <p>
 *
 * @author vs
 */
public class RtcRectLamp
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.RectLamp"; //$NON-NLS-1$

  static final ITinFieldInfo TFI_SWT_TRUE_COLOR = TinFieldInfo.makeCopy( LiteActorLamp.TFI_TRUE_COLOR, //
      AbstractValedSimpleRgba.OPDEF_USE_SWT_COLOR_DIALOG, AV_TRUE );

  static final ITinFieldInfo TFI_SWT_FALSE_COLOR = TinFieldInfo.makeCopy( LiteActorLamp.TFI_FALSE_COLOR, //
      AbstractValedSimpleRgba.OPDEF_USE_SWT_COLOR_DIALOG, AV_TRUE );

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_RECT_LAMP, //
      TSID_DESCRIPTION, STR_RTC_RECT_LAMP_D, //
      TSID_ICON_ID, ICONID_RTC_RECT_LAMP, //
      PARAMID_CATEGORY, CATID_LAMPS//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo( IStridablesListEdit<ITinFieldInfo> aFields ) {
      aFields.add( TFI_RTD_UGWI );
      aFields.add( TFI_SWT_TRUE_COLOR );
      aFields.add( TFI_SWT_FALSE_COLOR );
      aFields.add( TFI_SWT_BORDER_INFO );
      aFields.add( TFI_X );
      aFields.add( TFI_Y );
      aFields.add( TFI_WIDTH );
      aFields.add( TFI_HEIGHT );
      return new PropertableEntitiesTinTypeInfo<>( aFields, RtcRectLamp.class );
    }

    @Override
    protected void bindViselProps() {
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
        VedItemCfg viselCfg = createViselCfg( ViselRectangle.FACTORY_ID, aVedScreen, "RectLamp" ); //$NON-NLS-1$

        viselCfg.propValues().setDouble( PROPID_X, aCfg.params().getDouble( PROPID_X ) );
        viselCfg.propValues().setDouble( PROPID_Y, aCfg.params().getDouble( PROPID_Y ) );
        v = aVedScreen.model().visels().create( viselCfg );

        IVedActorFactory af = actorFactory( LiteActorLamp.FACTORY_ID, aVedScreen );
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
        StringArrayList actorIds = new StringArrayList();
        actorIds.add( actor.id() );
        params.setValobj( IRtControlCfg.PROPID_ACTORS_IDS, actorIds );
        RtControlCfg cfg = new RtControlCfg( v.id(), FACTORY_ID, params );

        return new RtcRectLamp( cfg, propDefs(), aVedScreen );
      }
      return null;
    }
  };

  protected RtcRectLamp( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  @Override
  protected void bindActorProps() {
    VedAbstractActor actor = actors().first();
    bindActorPropId( actor.id(), TFI_RTD_UGWI.id(), TFI_RTD_UGWI.id() );
    bindActorPropId( actor.id(), LiteActorLamp.TFI_TRUE_COLOR.id(), LiteActorLamp.TFI_TRUE_COLOR.id() );
    bindActorPropId( actor.id(), LiteActorLamp.TFI_FALSE_COLOR.id(), LiteActorLamp.TFI_FALSE_COLOR.id() );
  }

}
