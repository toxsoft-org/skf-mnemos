package org.toxsoft.skf.mnemo.mned.lite.actors;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.actors.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Checkbox for changing attribute boolean value.
 *
 * @author vs
 */
public class LiteActorAttrCheckbox
    extends AbstractLiteActorCheckbox {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + "lite.actor.AttrCheckbox"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_CHECKBOX, //
      TSID_DESCRIPTION, STR_ACTOR_CHECKBOX_D, //
      TSID_ICON_ID, ICONID_VED_COMMAND_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );

      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_GWID );

      return new PropertableEntitiesTinTypeInfo<>( fields, LiteActorLamp.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new LiteActorAttrCheckbox( aCfg, propDefs(), aVedScreen );
    }

  };

  ISkObject skObject = null;

  protected LiteActorAttrCheckbox( IVedItemCfg aCfg, IStridablesList<IDataDef> aDataDefs, VedScreen aVedScreen ) {
    super( aCfg, aDataDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // AbstractLiteActorCheckbox
  //

  @Override
  protected void onGwidChanged( Gwid aGwid ) {
    if( aGwid != null && !aGwid.isMulti() ) {
      skObject = coreApi.objService().get( aGwid.skid() );
    }
  }

  @Override
  protected IAtomicValue getValue( Gwid aGwid ) {
    if( aGwid != null && !aGwid.isMulti() ) {
      skObject = coreApi.objService().get( aGwid.skid() );
      if( skObject != null ) {
        return skObject.attrs().getByKey( aGwid.propId() );
      }
    }
    return IAtomicValue.NULL;
  }

  @Override
  protected void setValue( Gwid aGwid, IAtomicValue aBoolValue ) {
    DtoFullObject dto = DtoFullObject.createDtoFullObject( aGwid.skid(), coreApi );
    dto.attrs().setValue( aGwid.propId(), aBoolValue );
    skObject = coreApi.objService().defineObject( dto );
  }

}
