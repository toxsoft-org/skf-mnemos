package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.mnemo.gui.tools.rgbaset.*;

/**
 * Актор устанавливающий цвет для указанного свойства в зависимости от значения указанного данного.
 *
 * @author vs
 */
public class SkActorColorDecorator
    extends AbstractSkVedActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.ColorDecorator"; //$NON-NLS-1$

  static final String PROPID_RGBA_SET = SKVED_ID + ".prop.RgbaSet"; //$NON-NLS-1$

  static final IDataDef PROP_RGBA_SET = DataDef.create( PROPID_RGBA_SET, VALOBJ, //
      TSID_NAME, STR_RGBA_SET, //
      TSID_DESCRIPTION, STR_RGBA_SET_D, //
      TSID_KEEPER_ID, RgbaSet.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjRgbaSet.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( new RgbaSet() ) //
  );

  static final ITinTypeInfo TTI_RGBA_SET = new TinAtomicTypeInfo.TtiValobj<>( PROP_RGBA_SET, RgbaSet.class );

  static final ITinFieldInfo TFI_RGBA_SET = new TinFieldInfo( PROP_RGBA_SET, TTI_RGBA_SET );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_COLOR_DECORATOR, //
      TSID_DESCRIPTION, STR_ACTOR_COLOR_DECORATOR_D, //
      TSID_ICON_ID, ICONID_VED_ACTOR_COLOR_DECORATOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_RTD_GWID );
      fields.add( TFI_RGBA_SET );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorColorDecorator.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorColorDecorator( aCfg, propDefs(), aVedScreen );
    }

  };

  private Gwid      gwid     = null;
  private IGwidList gwidList = null;
  // private IAtomicValue lastValue = IAtomicValue.NULL;
  private IAtomicValue lastValue = null;
  private IRgbaSet     rgbaSet   = new RgbaSet();

  SkActorColorDecorator( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    IList<Class<?>> propClasses = new ElemArrayList<>( TsFillInfo.class, RGB.class, RGBA.class );
    addInterceptor( new VedActorInterceptorViselPropertyValidator( propClasses, aVedScreen.tsContext() ) );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractActor
  //

  @Override
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // check and don't allow to set invalid GWID
    if( aValuesToSet.hasKey( PROPID_RTD_GWID ) ) {
      Gwid g = aValuesToSet.getValobj( PROP_RTD_GWID );
      if( g.isAbstract() || g.kind() != EGwidKind.GW_RTDATA || g.isMulti() ) {
        aValuesToSet.remove( PROPID_RTD_GWID );
      }
    }
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_RTD_GWID ) ) {
      gwid = props().getValobj( PROP_RTD_GWID );
      gwidList = new GwidList( gwid );
    }
    if( aChangedValues.hasKey( PROPID_RGBA_SET ) ) {
      rgbaSet = props().getValobj( PROP_RGBA_SET );
    }
  }

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    IAtomicValue newValue = skVedEnv().getRtDataValue( gwid );
    if( !newValue.equals( lastValue ) && rgbaSet != null ) {
      RGBA rgba = rgbaSet.getRgba( 0 );
      if( newValue.isAssigned() ) {
        rgba = rgbaSet.getRgba( newValue.asInt() );
      }
      String viselPropId = props().getStr( PROPID_VISEL_PROP_ID );
      if( VedEditorUtils.isPropertyClass( TsFillInfo.class, viselPropId, getVisel(), tsContext() ) ) {
        setStdViselPropValue( avValobj( new TsFillInfo( rgba ) ) );
      }
      else {
        setStdViselPropValue( avValobj( rgba ) );
      }
      lastValue = newValue;
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected IGwidList doListUsedGwids() {
    return gwidList;
  }

}
