package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
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
public class SkActorRtdataValue
    extends AbstractSkVedActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RtdataValue"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, "RtData -> Value", //
      TSID_DESCRIPTION, "Sends RTDATA value to the specified property of the VISEL", //
      TSID_ICON_ID, ICONID_VED_ACTOR //
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
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRtdataValue.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRtdataValue( aCfg, propDefs(), aVedScreen );
    }

  };

  private Gwid         gwid      = null;
  private IGwidList    gwidList  = null;
  private IAtomicValue lastValue = IAtomicValue.NULL;

  SkActorRtdataValue( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
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
      setStdViselPropValue( newValue );
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
