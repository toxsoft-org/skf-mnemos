package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.SkActorCmdCheckbox.*;
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
 * Checkbox that sending command when checked/unchecked.
 *
 * @author vs
 */
public class RtcCmdCheckbox
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.CmdCheckbox"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_LINEAR_GAUGE, //
      TSID_DESCRIPTION, STR_RTC_LINEAR_GAUGE_D, //
      TSID_ICON_ID, ICONID_RTC_CHECKBOX //
  // PARAMID_CATEGORY, CATID_GAUGE//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      // fields.add( ViselCheckbox.TFI_CHECKED );
      fields.add( TFI_TEXT );
      fields.add( SkActorCmdCheckbox.TFI_CHECK_CMD_UGWI );
      fields.add( SkActorCmdCheckbox.TFI_UNCHECK_CMD_UGWI );
      fields.add( SkActorCmdCheckbox.TFI_VALUE );
      fields.add( SkActorCmdCheckbox.TFI_INVERSE_VALUE );
      fields.add( TFI_FONT );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_FG_COLOR );
      fields.add( TFI_BK_COLOR );
      // fields.add( TFI_STATE );
      // fields.add( TFI_HOVERED );
      return new PropertableEntitiesTinTypeInfo<>( fields, RtcCmdCheckbox.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( TFI_TEXT.id(), TFI_TEXT.id() );
      bindViselPropId( TFI_FONT.id(), TFI_FONT.id() );
      bindViselPropId( TFI_X.id(), TFI_X.id() );
      bindViselPropId( TFI_Y.id(), TFI_Y.id() );
      bindViselPropId( TFI_WIDTH.id(), TFI_WIDTH.id() );
      bindViselPropId( TFI_HEIGHT.id(), TFI_HEIGHT.id() );
      bindViselPropId( TFI_FG_COLOR.id(), TFI_FG_COLOR.id() );
      bindViselPropId( TFI_BK_COLOR.id(), TFI_BK_COLOR.id() );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      VedAbstractVisel v = null;
      VedAbstractActor actor = null;
      if( aCfg.viselId().isBlank() ) { // создание с нуля
        IVedViselFactory f = viselFactory( ViselCheckbox.FACTORY_ID, aVedScreen );
        VedItemCfg viselCfg = aVedScreen.model().visels().prepareFromTemplate( f.paletteEntries().first().itemCfg() );
        viselCfg.propValues().setDouble( PROPID_X, aCfg.params().getDouble( PROPID_X ) );
        viselCfg.propValues().setDouble( PROPID_Y, aCfg.params().getDouble( PROPID_Y ) );
        v = aVedScreen.model().visels().create( viselCfg );

        IVedActorFactory af = actorFactory( SkActorCmdCheckbox.FACTORY_ID, aVedScreen );
        VedItemCfg actorCfg = aVedScreen.model().actors().prepareFromTemplate( af.paletteEntries().first().itemCfg() );
        actorCfg.propValues().setStr( PROPID_VISEL_ID, v.id() );
        // actorCfg.propValues().setStr( PROPID_VISEL_PROP_ID, ViselLinearGauge.TFI_VALUE.id() );
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

        return new RtcCmdCheckbox( cfg, propDefs(), aVedScreen );
      }
      return null;
    }
  };

  protected RtcCmdCheckbox( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  @Override
  protected void bindActorProps() {
    VedAbstractActor actor = actors().first();
    bindActorPropId( actor.id(), TFI_CHECK_CMD_UGWI.id(), TFI_CHECK_CMD_UGWI.id() );
    bindActorPropId( actor.id(), TFI_UNCHECK_CMD_UGWI.id(), TFI_UNCHECK_CMD_UGWI.id() );
    bindActorPropId( actor.id(), TFI_VALUE.id(), TFI_VALUE.id() );
    bindActorPropId( actor.id(), TFI_INVERSE_VALUE.id(), TFI_INVERSE_VALUE.id() );
  }

}
