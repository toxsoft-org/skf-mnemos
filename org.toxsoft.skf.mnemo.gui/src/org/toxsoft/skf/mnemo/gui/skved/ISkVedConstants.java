package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.reports.gui.panels.valed.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.gui.valed.ugwi.*;

/**
 * The package constants.
 *
 * @author hazard157, vs
 */
@SuppressWarnings( "javadoc" )
public interface ISkVedConstants {

  String SKVED_ID = SK_ID + ".ved"; //$NON-NLS-1$

  String PROPID_GWID          = SKVED_ID + ".prop.Gwid";         //$NON-NLS-1$
  String PROPID_SKID          = SKVED_ID + ".prop.Skid";         //$NON-NLS-1$
  String PROPID_ATTR_GWID     = SKVED_ID + ".prop.AttrGwid";     //$NON-NLS-1$
  String PROPID_RTD_GWID      = SKVED_ID + ".prop.RtDataGwid";   //$NON-NLS-1$
  String PROPID_CMD_GWID      = SKVED_ID + ".prop.CmdGwid";      //$NON-NLS-1$
  String PROPID_FORMAT_STRING = SKVED_ID + ".prop.FormatString"; //$NON-NLS-1$
  String PROPID_RRI_ID        = SKVED_ID + ".prop.RriId";        //$NON-NLS-1$

  IDataDef PROP_GWID = DataDef.create( PROPID_GWID, VALOBJ, //
      TSID_NAME, STR_PROP_GWID, //
      TSID_DESCRIPTION, STR_PROP_GWID_D, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjAnyGwidEditor.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( Gwid.of( "classId[*]" ) ) //
  );

  IDataDef PROP_SKID = DataDef.create( PROPID_SKID, VALOBJ, //
      TSID_NAME, STR_PROP_SKID, //
      TSID_DESCRIPTION, STR_PROP_SKID_D, //
      TSID_KEEPER_ID, Skid.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSkidEditor.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( Skid.CANONICAL_STRING_NONE ) //
  );

  IDataDef PROP_ATTR_GWID = DataDef.create( PROPID_ATTR_GWID, VALOBJ, //
      TSID_NAME, STR_PROP_ATTR_GWID, //
      TSID_DESCRIPTION, STR_PROP_ATTR_GWID_D, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjGwidEditor.FACTORY_NAME, //
      ValedGwidEditor.OPDEF_GWID_KIND, avValobj( EGwidKind.GW_ATTR ), //
      TSID_DEFAULT_VALUE, avValobj( Gwid.createAttr( IStridable.NONE_ID, IStridable.NONE_ID ) ) //
  );

  IDataDef PROP_RTD_GWID = DataDef.create( PROPID_RTD_GWID, VALOBJ, //
      TSID_NAME, STR_PROP_RTD_GWID, //
      TSID_DESCRIPTION, STR_PROP_RTD_GWID_D, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjGwidEditor.FACTORY_NAME, //
      ValedGwidEditor.OPDEF_GWID_KIND, avValobj( EGwidKind.GW_RTDATA ), //
      TSID_DEFAULT_VALUE, avValobj( Gwid.createRtdata( IStridable.NONE_ID, IStridable.NONE_ID, IStridable.NONE_ID ) ) //
  );

  IDataDef PROP_CMD_GWID = DataDef.create( PROPID_CMD_GWID, VALOBJ, //
      TSID_NAME, STR_PROP_CMD_GWID, //
      TSID_DESCRIPTION, STR_PROP_CMD_GWID_D, //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjGwidEditor.FACTORY_NAME, //
      ValedGwidEditor.OPDEF_GWID_KIND, avValobj( EGwidKind.GW_CMD ), //
      TSID_DEFAULT_VALUE, avValobj( Gwid.createCmd( IStridable.NONE_ID, IStridable.NONE_ID ) ) //
  );

  IDataDef PROP_FORMAT_STRING = DataDef.create( PROPID_FORMAT_STRING, STRING, //
      TSID_NAME, STR_PROP_FORMAT_STRING, //
      TSID_DESCRIPTION, STR_PROP_FORMAT_STRING_D, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  IDataDef PROP_RRI_ID = DataDef.create( PROPID_RRI_ID, VALOBJ, //
      TSID_NAME, STR_PROP_RRI_ID, //
      TSID_DESCRIPTION, STR_PROP_RRI_ID_D, //
      TSID_KEEPER_ID, RriId.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjRriIdEditor.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( RriId.NONE ) //
  );

  ITinTypeInfo TTI_GWID          = new TinAtomicTypeInfo.TtiValobj<>( PROP_GWID, Gwid.class );
  ITinTypeInfo TTI_SKID          = new TinAtomicTypeInfo.TtiValobj<>( PROP_SKID, Skid.class );
  ITinTypeInfo TTI_ATTR_GWID     = new TinAtomicTypeInfo.TtiValobj<>( PROP_ATTR_GWID, Gwid.class );
  ITinTypeInfo TTI_RTD_GWID      = new TinAtomicTypeInfo.TtiValobj<>( PROP_RTD_GWID, Gwid.class );
  ITinTypeInfo TTI_CMD_GWID      = new TinAtomicTypeInfo.TtiValobj<>( PROP_CMD_GWID, Gwid.class );
  ITinTypeInfo TTI_FORMAT_STRING = new TinAtomicTypeInfo.TtiString( PROP_FORMAT_STRING );
  ITinTypeInfo TTI_RRI_ID        = new TinAtomicTypeInfo.TtiValobj<>( PROP_RRI_ID, RriId.class );

  ITinFieldInfo TFI_GWID          = new TinFieldInfo( PROP_GWID, TTI_GWID );
  ITinFieldInfo TFI_SKID          = new TinFieldInfo( PROP_SKID, TTI_SKID );
  ITinFieldInfo TFI_ATTR_GWID     = new TinFieldInfo( PROP_ATTR_GWID, TTI_ATTR_GWID );
  ITinFieldInfo TFI_RTD_GWID      = new TinFieldInfo( PROP_RTD_GWID, TTI_RTD_GWID );
  ITinFieldInfo TFI_CMD_GWID      = new TinFieldInfo( PROP_CMD_GWID, TTI_CMD_GWID );
  ITinFieldInfo TFI_FORMAT_STRING = new TinFieldInfo( PROP_FORMAT_STRING, TTI_FORMAT_STRING );
  ITinFieldInfo TFI_RRI_ID        = new TinFieldInfo( PROP_RRI_ID, TTI_RRI_ID );

  // ------------------------------------------------------------------------------------
  // UGWI support
  //

  String PROPID_ATTR_UGWI = SKVED_ID + ".prop.AttrUgwi"; //$NON-NLS-1$
  String PROPID_CMD_UGWI  = SKVED_ID + ".prop.CmdUgwi";  //$NON-NLS-1$

  IDataDef PROP_ATTR_UGWI = DataDef.create( PROPID_ATTR_UGWI, VALOBJ, //
      TSID_NAME, STR_PROP_ATTR_UGWI, //
      TSID_DESCRIPTION, STR_PROP_ATTR_UGWI_D, //
      TSID_KEEPER_ID, Ugwi.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvUgwiSelector.FACTORY_NAME, //
      ValedUgwiSelector.OPDEF_SINGLE_UGWI_KIND_ID, avStr( UgwiKindSkAttr.KIND_ID ), //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  IDataDef PROP_CMD_UGWI = DataDef.create( PROPID_CMD_UGWI, VALOBJ, //
      TSID_NAME, STR_PROP_CMD_UGWI, //
      TSID_DESCRIPTION, STR_PROP_CMD_UGWI_D, //
      TSID_KEEPER_ID, Ugwi.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvUgwiSelector.FACTORY_NAME, //
      ValedUgwiSelector.OPDEF_SINGLE_UGWI_KIND_ID, avStr( UgwiKindSkCmd.KIND_ID ), //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  ITinTypeInfo TTI_ATTR_UGWI = new TinAtomicTypeInfo.TtiValobj<>( PROP_ATTR_UGWI, Ugwi.class );
  ITinTypeInfo TTI_CMD_UGWI  = new TinAtomicTypeInfo.TtiValobj<>( PROP_CMD_UGWI, Ugwi.class );

  ITinFieldInfo TFI_ATTR_UGWI = new TinFieldInfo( PROP_ATTR_UGWI, TTI_ATTR_UGWI );
  ITinFieldInfo TFI_CMD_UGWI  = new TinFieldInfo( PROP_CMD_UGWI, TTI_CMD_UGWI );

}
