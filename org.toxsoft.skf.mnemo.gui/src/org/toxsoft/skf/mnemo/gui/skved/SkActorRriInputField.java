package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
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
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Обработчик поля ввода для чения/записи значения параметра НСИ.
 * <p>
 *
 * @author vs
 */
public class SkActorRriInputField
    extends AbstractSkActorInputField {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RriInputField"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_RRI_INPUT_FIELD, //
      TSID_DESCRIPTION, STR_ACTOR_RRI_INPUT_FIELD_D, //
      TSID_ICON_ID, ICONID_VED_RRI_EDIT_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_RRI_ID );
      fields.add( TFI_FORMAT_STRING );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRriInputField.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRriInputField( aCfg, propDefs(), aVedScreen );
    }

  };

  private String oldStr = TsLibUtils.EMPTY_STRING;
  private RriId  rriId  = null;
  private String fmtStr = null;

  private IGwidList sourceGwidList = IGwidList.EMPTY;

  private IAtomicValue lastValue = IAtomicValue.NULL;

  private ISkRriSection section = null;

  protected SkActorRriInputField( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractActor
  //

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( PROPID_RRI_ID ) ) {
      rriId = props().getValobj( PROP_RRI_ID );
      ISkRegRefInfoService rriServ = coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      if( rriId != null ) {
        section = rriServ.findSection( rriId.sectionId() );
      }
    }
    if( aChangedValues.hasKey( PROPID_FORMAT_STRING ) ) {
      fmtStr = props().getStr( PROP_FORMAT_STRING );
      if( fmtStr.isBlank() ) {
        fmtStr = null;
        ISkClassInfo classInfo = skSysdescr().findClassInfo( rriId.gwid().classId() );
        if( classInfo != null ) {
          IDtoAttrInfo attrInfo = classInfo.attrs().list().findByKey( rriId.gwid().propId() );
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
    IAtomicValue newValue = getRriAttrValue();
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
    if( section != null ) {
      setRriAttrValue( getVisel().props().getStr( PROPID_TEXT ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  IAtomicValue getRriAttrValue() {
    return section.getAttrParamValue( rriId.gwid().skid(), rriId.gwid().propId() );
  }

  void setRriAttrValue( String aText ) {
    IAtomicValue value = section.getAttrParamValue( rriId.gwid().skid(), rriId.gwid().propId() );
    switch( value.atomicType() ) {
      case BOOLEAN:
        Boolean bv = Boolean.valueOf( Boolean.parseBoolean( aText ) );
        value = AvUtils.avFromObj( bv );
        break;
      case FLOATING:
        Double dv = Double.valueOf( Double.parseDouble( aText ) );
        value = AvUtils.avFromObj( dv );
        break;
      case INTEGER:
        Integer iv = Integer.valueOf( Integer.parseInt( aText ) );
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
    section.setAttrParamValue( rriId.gwid().skid(), rriId.gwid().propId(), value, TsLibUtils.EMPTY_STRING );
  }

}
