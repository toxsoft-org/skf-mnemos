package org.toxsoft.skf.mnemo.mned.lite.rtc;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * RtControl factory.
 *
 * @author vs
 */
public interface IRtControlFactory
    extends IStridableParameterized {

  /**
   * Returns the type information for item to be viewed and edited in object inspector.
   *
   * @return {@link ITinTypeInfo} - the information for inspecting the VED item instance
   */
  ITinTypeInfo typeInfo();

  /**
   * Returns the information about VED item properties.
   * <p>
   * The list of properties is unambiguously compiled from the information provided by {@link #typeInfo()}.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

  /**
   * Returns the palette entries.
   * <p>
   * The list returns at least one entry.
   *
   * @return {@link IStridablesList}&lt;{@link IRtControlsPaletteEntry}&gt; - entries list
   */
  IStridablesList<IRtControlsPaletteEntry> paletteEntries();

  /**
   * Creates the entity instance with default values of fields.
   *
   * @param aCfg {@link IRtControlCfg} - the configuration data
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @return {@link IRtControlCfg} - created instance of the item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException config entity kind does not matches provided entity kind
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  IRtControl create( IRtControlCfg aCfg, VedScreen aVedScreen );

  /**
   * Correspondence between visel propertyIds and RtControl propertyIds.<br>
   * Left - rtControl propertyId, right - viselPropertyId
   *
   * @return IList&lt;Pair&lt;String, String>> - correspondence between visel and RtControl propertyIds
   */
  IList<Pair<String, String>> viselPropIdBinding();

  /**
   * Correspondence between actor propertyIds and RtControl propertyIds.<br>
   * Key - actor ID, Left - rtControl propertyId, right - viselPropertyId
   *
   * @return IList&lt;Pair&lt;String, String>> - correspondence between visel and RtControl propertyIds
   */
  IStringMap<IList<Pair<String, String>>> actorPropIdBinding();
}
