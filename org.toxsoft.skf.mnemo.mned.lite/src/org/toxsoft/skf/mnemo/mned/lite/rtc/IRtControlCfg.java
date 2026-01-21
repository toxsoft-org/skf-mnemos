package org.toxsoft.skf.mnemo.mned.lite.rtc;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Configuration data of the individual actor.
 * <p>
 * The {@link #id()} is the rt-control instance identifier unique in owner mnemoscheme.
 * <p>
 * {@link #params()} contains common options like {@link IAvMetaConstants#TSID_NAME} for {@link #nmName()} and any other
 * options not defined yet but expected to be defined in the future.
 *
 * @author vs
 */
public interface IRtControlCfg
    extends IStridableParameterized {

  /**
   * ID for property - StringList of actors ids
   */
  String PROPID_ACTORS_IDS = "actorsIds"; //$NON-NLS-1$

  /**
   * Returns the only one visel ID.
   *
   * @return String - the only one visel ID
   */
  String viselId();

  /**
   * Returns list of corresponding actor IDs.
   *
   * @return {@link IStringList} - corresponding actor IDs
   */
  IStringList actorIds();

  /**
   * Returns the ID of the factory used to create the actor from the configuration data {@link IVedItemCfg}.
   * <p>
   * It is assumed that somewhere exists the registry containing factories.
   *
   * @return {@link String} - the VISEL factory ID
   */
  String factoryId();

  /**
   * Returns the values of the properties used to build the VISEL instance.
   *
   * @return {@link IOptionSet} - property values for RtControl instance creation
   */
  IOptionSet propValues();

}
