package org.toxsoft.skf.mnemo.mned.lite.actors;

import static org.toxsoft.core.tsgui.graphics.ITsGraphicsConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.actors.ITsResources.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.*;

/**
 * Актор,реализующий функциональность отображения булеого значения в ввиде лампочки (без мигания).
 * <p>
 *
 * @author vs
 */
public class LiteActorLamp
    extends AbstractSkActorSingleRtDataConsumer {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + "lite.actor.Lamp"; //$NON-NLS-1$

  static final String PROPID_TRUE_COLOR  = "trueColor";  //$NON-NLS-1$
  static final String PROPID_FALSE_COLOR = "falseColor"; //$NON-NLS-1$

  static final IDataDef PROP_TRUE_COLOR = DataDef.create3( PROPID_TRUE_COLOR, DT_COLOR_RGBA, //
      TSID_NAME, STR_TRUE_COLOR, //
      TSID_DESCRIPTION, STR_TRUE_COLOR_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.WHITE.rgba() ) //
  );

  static final IDataDef PROP_FALSE_COLOR = DataDef.create3( PROPID_FALSE_COLOR, DT_COLOR_RGBA, //
      TSID_NAME, STR_FALSE_COLOR, //
      TSID_DESCRIPTION, STR_FALSE_COLOR_D, //
      TSID_DEFAULT_VALUE, avValobj( ETsColor.BLACK.rgba() ) //
  );

  /**
   * Описание поля, содержащего цвет включенной лампы
   */
  public static final ITinFieldInfo TFI_TRUE_COLOR = new TinFieldInfo( PROP_TRUE_COLOR, TtiRGBA.INSTANCE );

  /**
   * Описание поля, содержащего цвет выключенной лампы
   */
  public static final ITinFieldInfo TFI_FALSE_COLOR = new TinFieldInfo( PROP_FALSE_COLOR, TtiRGBA.INSTANCE );

  /**
   * The ACTOR factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_FALSE_COLOR, //
      TSID_DESCRIPTION, STR_FALSE_COLOR_D, //
      TSID_ICON_ID, ICONID_ACTOR_LAMP //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );

      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_RTD_UGWI );

      fields.add( TFI_TRUE_COLOR );
      fields.add( TFI_FALSE_COLOR );

      return new PropertableEntitiesTinTypeInfo<>( fields, LiteActorLamp.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new LiteActorLamp( aCfg, propDefs(), aVedScreen );
    }

  };

  private VedAbstractVisel lampVisel = null;

  private static final TsFillInfo unknownFillInfo = new TsFillInfo( new RGBA( 128, 128, 128, 255 ) );

  private TsFillInfo trueFillInfo = new TsFillInfo( new RGBA( 255, 255, 255, 255 ) );

  private TsFillInfo falseFillInfo = new TsFillInfo( new RGBA( 0, 0, 0, 255 ) );

  protected LiteActorLamp( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  @Override
  protected void doDoUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_TRUE_COLOR ) ) {
      RGBA rgba = aChangedValues.getValobj( PROPID_TRUE_COLOR );
      trueFillInfo = new TsFillInfo( rgba );
    }
    if( aChangedValues.hasKey( PROPID_FALSE_COLOR ) ) {
      RGBA rgba = aChangedValues.getValobj( PROPID_FALSE_COLOR );
      falseFillInfo = new TsFillInfo( rgba );
    }
    if( aChangedValues.hasKey( PROPID_VISEL_ID ) ) {
      String vId = aChangedValues.getStr( PROPID_VISEL_ID );
      lampVisel = VedScreenUtils.findVisel( vId, vedScreen() );
    }
  }

  @Override
  protected void doOnValueChanged( IAtomicValue aNewValue ) {
    if( lampVisel != null ) {
      TsFillInfo fillInfo = unknownFillInfo;
      if( aNewValue != null && aNewValue.isAssigned() ) {
        fillInfo = falseFillInfo;
        if( aNewValue.asBool() ) {
          fillInfo = trueFillInfo;
        }
      }
      if( lampVisel.props().hasKey( PROPID_BK_FILL ) ) {
        lampVisel.props().setValobj( PROPID_BK_FILL, fillInfo );
      }
      if( lampVisel.props().hasKey( PROPID_BK_COLOR ) ) {
        lampVisel.props().setValobj( PROPID_BK_COLOR, fillInfo.fillColor() );
      }
    }
  }

}
