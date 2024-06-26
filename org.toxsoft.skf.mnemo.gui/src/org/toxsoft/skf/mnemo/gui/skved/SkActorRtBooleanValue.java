package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * Actor: reads specified RTDATA value and supplies it to the specified property of the VISEL.
 *
 * @author hazard157
 */
public class SkActorRtBooleanValue
    extends AbstractSkVedActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RtBooleanValue"; //$NON-NLS-1$

  static final String PROPID_INVERSE_BOOLEAN = "inverse.boolean"; //$NON-NLS-1$

  static final IDataDef PROP_INVERSE = DataDef.create( PROPID_INVERSE_BOOLEAN, BOOLEAN, //
      TSID_NAME, STR_INVERSE_BOOLEAN, //
      TSID_DESCRIPTION, STR_INVERSE_BOOLEAN_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  static final ITinFieldInfo TFI_INVERSE_BOOLEAN = new TinFieldInfo( PROP_INVERSE, TTI_AT_BOOLEAN );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_RTBOOLEAN_VALUE, //
      TSID_DESCRIPTION, STR_ACTOR_RTBOOLEAN_VALUE_D, //
      TSID_ICON_ID, ICONID_VED_RT_BOOL_EDIT_ACTOR //
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
      fields.add( TFI_INVERSE_BOOLEAN );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRtBooleanValue.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRtBooleanValue( aCfg, propDefs(), aVedScreen );
    }

  };

  private Gwid         gwid      = null;
  private IGwidList    gwidList  = null;
  private IAtomicValue lastValue = IAtomicValue.NULL;

  SkActorRtBooleanValue( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
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
  }

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    IAtomicValue newValue = skVedEnv().getRtDataValue( gwid );
    if( !newValue.equals( lastValue ) ) {
      IAtomicValue val2set = newValue;
      if( newValue.atomicType() == EAtomicType.BOOLEAN && props().hasKey( PROPID_INVERSE_BOOLEAN ) ) {
        boolean inverse = props().getBool( PROPID_INVERSE_BOOLEAN );
        if( inverse ) {
          val2set = avBool( !newValue.asBool() );
        }
      }
      setStdViselPropValue( val2set );
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
