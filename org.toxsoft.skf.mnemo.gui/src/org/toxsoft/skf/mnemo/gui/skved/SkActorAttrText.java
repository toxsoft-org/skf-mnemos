package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Actor: reads specified ATTRIBUTE value and supplies it to the VISEL as a text.
 *
 * @author vs
 */
public class SkActorAttrText
    extends AbstractSkVedActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.AttrText"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_ATTR_TEXT, //
      TSID_DESCRIPTION, STR_ACTOR_ATTR_TEXT_D, //
      TSID_ICON_ID, ICONID_VED_ATR_EDIT_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_FORMAT_STRING );
      fields.add( TFI_ATTR_UGWI );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorAttrText.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorAttrText( aCfg, propDefs(), aVedScreen );
    }

  };

  private Ugwi         ugwi      = null;
  private IUgwiList    ugwiList  = IUgwiList.EMPTY;
  private String       fmtStr    = null;
  private IAtomicValue lastValue = IAtomicValue.NULL;

  SkActorAttrText( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractActor
  //

  @Override
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // check and don't allow to set invalid UGWI
    removeWrongUgwi( PROPID_ATTR_UGWI, UgwiKindSkAttr.KIND_ID, aValuesToSet, coreApi() );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    try {
      if( aChangedValues.hasKey( PROPID_ATTR_UGWI ) ) {
        IAtomicValue v = props().getValue( PROPID_ATTR_UGWI );
        if( v.isAssigned() ) {
          ugwi = v.asValobj();
          ugwiList = UgwiList.createDirect( new ElemArrayList<>( ugwi ) );
        }
      }
      if( aChangedValues.hasKey( PROPID_ATTR_UGWI ) ) {
        IAtomicValue av = aChangedValues.getValue( PROPID_ATTR_UGWI );
        if( av.isAssigned() ) {
          ugwi = av.asValobj();
          if( ugwi != null && ugwi != Ugwi.NONE ) {
            ugwiList = UgwiList.createDirect( new ElemArrayList<>( ugwi ) );
          }
          else {
            ugwiList = IUgwiList.EMPTY;
          }
        }
      }
      if( aChangedValues.hasKey( PROPID_FORMAT_STRING ) ) {
        fmtStr = props().getStr( PROP_FORMAT_STRING );
        if( fmtStr.isBlank() && ugwi != null && ugwi != Ugwi.NONE ) {
          fmtStr = null;
          ISkClassInfo classInfo = skSysdescr().findClassInfo( UgwiKindSkAttr.getClassId( ugwi ) );
          if( classInfo != null ) {
            IDtoAttrInfo attrInfo = classInfo.attrs().list().findByKey( UgwiKindSkAttr.getAttrId( ugwi ) );
            if( attrInfo != null ) {
              IAtomicValue avFmtStr = SkHelperUtils.getConstraint( attrInfo, TSID_FORMAT_STRING );
              if( avFmtStr != null && avFmtStr.isAssigned() ) {
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
    catch( Throwable e ) {
      e.printStackTrace();
    }
  }

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    if( ugwi != null && UgwiKindSkAttr.getSkid( ugwi ) != null ) {
      ISkObject skObj = skVedEnv().skConn().coreApi().objService().find( UgwiKindSkAttr.getSkid( ugwi ) );
      IAtomicValue newValue = IAtomicValue.NULL;
      if( skObj != null ) {
        newValue = skObj.attrs().getValue( UgwiKindSkAttr.getAttrId( ugwi ) );
      }
      if( !newValue.equals( lastValue ) ) {
        try {
          String text = AvUtils.printAv( fmtStr, newValue );
          setStdViselPropValue( avStr( text ) );
        }
        catch( Throwable e ) {
          TsDialogUtils.error( getShell(), e );
          e.printStackTrace();
        }
        // Даже если формат неверный все равно обновим значение, чтобы избежать повторных исключений на этапе разработки
        lastValue = newValue;
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected IGwidList doListUsedGwids() {
    GwidList gl = new GwidList();
    for( Ugwi u : ugwiList.items() ) {
      gl.add( UgwiKindSkAttr.getGwid( u ) );
    }
    return gl;
  }

}
