package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
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
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.core.utils.*;

public class SkActorInputField
    extends AbstractSkActorInputField {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.InputField"; //$NON-NLS-1$

  /**
   * Id for property - "Source GWID"
   */
  public static final String PROPID_SOURCE_GWID = "sourceGwid"; //$NON-NLS-1$

  private final static IDataDef PROP_SOURCE_GWID = DataDef.create( PROPID_SOURCE_GWID, VALOBJ, //
      TSID_NAME, STR_PROP_SOURCE_GWID, //
      TSID_DESCRIPTION, STR_PROP_SOURCE_GWID_D, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjAnyGwidEditor.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( Gwid.of( "classId[*]" ) ) //
  );

  private final static ITinFieldInfo TFI_SOURCE_GWID = new TinFieldInfo( PROP_SOURCE_GWID, TTI_GWID );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_INPUT_FIELD, //
      TSID_DESCRIPTION, STR_ACTOR_INPUT_FIELD_D, //
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
      fields.add( TFI_SOURCE_GWID );
      fields.add( TFI_GWID );
      fields.add( TFI_FORMAT_STRING );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorInputField.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorInputField( aCfg, propDefs(), aVedScreen );
    }

  };

  private String oldStr = TsLibUtils.EMPTY_STRING;
  private Gwid   gwid   = null;
  private String fmtStr = null;

  private IGwidList sourceGwidList = IGwidList.EMPTY;

  private IAtomicValue lastValue = IAtomicValue.NULL;

  protected SkActorInputField( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractActor
  //

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_GWID ) ) {
      gwid = props().getValobj( PROP_GWID );
    }
    if( aChangedValues.hasKey( PROPID_SOURCE_GWID ) ) {
      sourceGwidList = new GwidList( gwid = props().getValobj( PROP_SOURCE_GWID ) );
    }
    if( aChangedValues.hasKey( PROPID_FORMAT_STRING ) ) {
      fmtStr = props().getStr( PROP_FORMAT_STRING );
      if( fmtStr.isBlank() ) {
        fmtStr = null;
        ISkClassInfo classInfo = skSysdescr().findClassInfo( gwid.classId() );
        if( classInfo != null ) {
          IDtoAttrInfo attrInfo = classInfo.attrs().list().findByKey( gwid.propId() );
          if( attrInfo != null ) {
            IAtomicValue avFmtStr = SkHelperUtils.getConstraint( attrInfo, TSID_FORMAT_STRING );
            if( avFmtStr != null ) {
              fmtStr = avFmtStr.asString();
            }
          }
        }
      }
      if( fmtStr != null && fmtStr.isBlank() ) {
        fmtStr = null;
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected IGwidList doListUsedGwids() {
    return sourceGwidList;
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkActorInputField
  //

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    if( inputHandler != null && inputHandler.isEditing() ) {
      super.whenRealTimePassed( aRtTime );
      return;
    }
    IAtomicValue newValue = getValue();
    if( !newValue.equals( lastValue ) ) {
      String text = AvUtils.printAv( fmtStr, newValue );
      setStdViselPropValue( avStr( text ) );
      lastValue = newValue;
    }
  }

  @Override
  protected void onStartEdit() {
    oldStr = getVisel().props().getStr( PROPID_TEXT );
  }

  @Override
  protected void onCancelEdit() {
    getVisel().props().setStr( PROPID_TEXT, oldStr );
  }

  @Override
  protected void onFinishEdit() {
    setValue( getVisel().props().getStr( PROPID_TEXT ) );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private IAtomicValue getValue() {
    if( gwid != null && !gwid.isAbstract() ) {
      EGwidKind gwKind = gwid.kind();
      switch( gwKind ) {
        case GW_ATTR:
          return getAttrValue();
        case GW_RIVET:
        case GW_RTDATA:
        case GW_CMD:
        case GW_CMD_ARG:
          break;
        case GW_CLASS:
        case GW_CLOB:
        case GW_EVENT:
        case GW_EVENT_PARAM:
        case GW_LINK:
        default:
          return IAtomicValue.NULL;
        // throw new IllegalArgumentException( "Unexpected value: " + gwKind ); //$NON-NLS-1$
      }
    }
    return IAtomicValue.NULL;
  }

  private void setValue( String aText ) {
    if( gwid != null && !gwid.isAbstract() ) {
      EGwidKind gwKind = gwid.kind();
      switch( gwKind ) {
        case GW_ATTR:
          setAttrValue( aText );
          break;
        case GW_RIVET:
        case GW_RTDATA:
        case GW_CMD:
        case GW_CMD_ARG:
          break;
        case GW_CLASS:
        case GW_CLOB:
        case GW_EVENT:
        case GW_EVENT_PARAM:
        case GW_LINK:
        default:
          throw new IllegalArgumentException( "Unexpected value: " + gwKind ); //$NON-NLS-1$
      }
    }
  }

  IAtomicValue getAttrValue() {
    ISkObject skObj = skVedEnv().skConn().coreApi().objService().find( gwid.skid() );
    IAtomicValue newValue = IAtomicValue.NULL;
    if( skObj != null ) {
      newValue = skObj.attrs().getValue( gwid.propId() );
    }
    return newValue;
  }

  void setAttrValue( String aText ) {
    ISkObject skObj = skVedEnv().skConn().coreApi().objService().find( gwid.skid() );
    IAtomicValue value = IAtomicValue.NULL;
    if( skObj != null ) {
      value = skObj.attrs().getValue( gwid.propId() );
      switch( value.atomicType() ) {
        case BOOLEAN:
          Boolean bv = Boolean.parseBoolean( aText );
          value = AvUtils.avFromObj( bv );
          break;
        case FLOATING:
          Double dv = Double.parseDouble( aText );
          value = AvUtils.avFromObj( dv );
          break;
        case INTEGER:
          Integer iv = Integer.parseInt( aText );
          value = AvUtils.avFromObj( iv );
          break;
        case STRING:
          value = AvUtils.avFromObj( aText );
          break;
        case TIMESTAMP:
        case VALOBJ:
        case NONE:
          break;
        default:
          throw new IllegalArgumentException( "Unexpected value: " + value.atomicType() ); //$NON-NLS-1$
      }
      DtoObject dto = new DtoObject( skObj.skid() );
      dto.attrs().setValue( gwid.propId(), value );
      skVedEnv().skConn().coreApi().objService().defineObject( dto );
    }

  }

}
