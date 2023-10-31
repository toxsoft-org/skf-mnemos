package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.strid.IStridable.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.gw.gwid.*;

/**
 * The package constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkVedConstants {

  String SKVED_ID = SK_ID + ".ved"; //$NON-NLS-1$

  String PROPID_RTD_GWID      = SKVED_ID + ".prop.RtDataGwid";   //$NON-NLS-1$
  String PROPID_FORMAT_STRING = SKVED_ID + ".prop.FormatString"; //$NON-NLS-1$

  IDataDef PROP_RTD_GWID = DataDef.create( PROPID_RTD_GWID, VALOBJ, //
      TSID_NAME, "RtData", //
      TSID_DESCRIPTION, "RtData GWID", //
      TSID_KEEPER_ID, Gwid.KEEPER_ID, //
      // TODO OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjSkGwid.EDITOR_NAME, //
      // TODO ValedSkAvValobjGwid.OPDEF_GWID_KIND, avValobj( EGwidKind.GW_RTDATA ), //
      // TODO ValedSkAvValobjGwid.OPDEF_IS_ONLY_CONCRETE, AV_TRUE, //
      // TODO ValedSkAvValobjGwid.OPDEF_IS_<ULTI_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, avValobj( Gwid.createRtdata( NONE_ID, NONE_ID, NONE_ID ) ) //
  );

  IDataDef PROP_FORMAT_STRING = DataDef.create( PROPID_FORMAT_STRING, STRING, //
      TSID_NAME, "Format", //
      TSID_DESCRIPTION, "Format string for atomic value formatting (empty string uses default for RTdata)", //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY //
  );

  ITinTypeInfo  TTI_RTD_GWID      = new TinAtomicTypeInfo.TtiValobj<>( PROP_RTD_GWID, Gwid.class );
  ITinFieldInfo TFI_RTD_GWID      = new TinFieldInfo( PROP_RTD_GWID, TTI_RTD_GWID );
  ITinTypeInfo  TTI_FORMAT_STRING = new TinAtomicTypeInfo.TtiString( PROP_FORMAT_STRING );
  ITinFieldInfo TFI_FORMAT_STRING = new TinFieldInfo( PROP_FORMAT_STRING, TTI_FORMAT_STRING );

}
