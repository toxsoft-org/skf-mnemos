package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * {@link IRtControlCfg} editable implementation.
 *
 * @author vs
 */
public class RtControlCfg
    implements IRtControlCfg, IParameterizedEdit {

  private final String         id;
  private final IOptionSetEdit params     = new OptionSet();
  private final String         factoryId;
  private final IOptionSetEdit propValues = new OptionSet();

  private String viselId = TsLibUtils.EMPTY_STRING;

  private IStringListEdit actorIds = IStringList.EMPTY;

  /**
   * Constructor.
   *
   * @param aId String - item identifier
   * @param aFactoryId {@link String} - the item factory ID
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public RtControlCfg( String aId, String aFactoryId, IOptionSet aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    factoryId = StridUtils.checkValidIdPath( aFactoryId );
    params.setAll( aParams );
    if( aParams.hasKey( PROPID_VISEL_ID ) ) {
      viselId = aParams.getStr( PROPID_VISEL_ID );
    }
    if( aParams.hasKey( PROPID_ACTORS_IDS ) ) {
      actorIds = aParams.getValobj( PROPID_ACTORS_IDS );
    }
  }

  /**
   * Constructor.
   *
   * @param aId String - item identifier
   * @param aTemplateCfg {@link IRtControlCfg} - template configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public RtControlCfg( String aId, IRtControlCfg aTemplateCfg ) {
    id = StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNull( aTemplateCfg );
    factoryId = aTemplateCfg.factoryId();
    viselId = aTemplateCfg.viselId();
    IStringList actList = aTemplateCfg.actorIds();
    if( actList.size() > 0 ) {
      actorIds = new StringArrayList();
      actorIds.addAll( actList );
    }
    params.setAll( aTemplateCfg.params() );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return propValues().getStr( PROP_NAME );
  }

  @Override
  public String description() {
    return propValues().getStr( PROP_DESCRIPTION );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID, null );
  }

  // ------------------------------------------------------------------------------------
  // IRtControlCfg
  //

  @Override
  public String viselId() {
    return viselId;
  }

  @Override
  public IStringList actorIds() {
    return actorIds;
  }

  @Override
  public String factoryId() {
    return factoryId;
  }

  @Override
  public IOptionSetEdit propValues() {
    return propValues;
  }

  // ------------------------------------------------------------------------------------
  // Static methods
  //

  /**
   * Returns visel ID or empty string.
   *
   * @param aParams {@link IOptionSet} - parameters
   * @return String - visel ID or empty string
   */
  public static String viselId( IOptionSet aParams ) {
    if( aParams.hasKey( PROPID_VISEL_ID ) ) {
      return aParams.getStr( PROPID_VISEL_ID );
    }
    return TsLibUtils.EMPTY_STRING;
  }
}
