package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Actor: reads specified RTDATA value and supplies it to the specified property of the VISEL.
 *
 * @author hazard157, vs
 */
public class SkActorRtdataValue
    extends AbstractSkActorSingleRtDataConsumer {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RtdataValue"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_RTDATA_VALUE, //
      TSID_DESCRIPTION, STR_ACTOR_RTDATA_VALUE_D, //
      TSID_ICON_ID, ICONID_VED_RT_EDIT_ACTOR //
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
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRtdataValue.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRtdataValue( aCfg, propDefs(), aVedScreen );
    }

  };

  SkActorRtdataValue( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkActorSingleRtDataConsumer
  //

  @Override
  protected void doOnValueChanged( IAtomicValue aNewValue ) {
    setStdViselPropValue( aNewValue );
  }

}
