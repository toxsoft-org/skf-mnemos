package org.toxsoft.skf.mnemo.lib.cfg;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * Configuration data of the individual actor.
 * <p>
 * The {@link #id()} is the actor instance identifier unique in owner mnemoscheme.
 * <p>
 * {@link #params()} contains common options like {@link IAvMetaConstants#TSID_NAME} for {@link #nmName()} and any other
 * options not defined yet but expected to be defined in the future.
 *
 * @author hazard157
 */
public sealed interface IActorCfg
    extends IStridableParameterized permits ActorCfg {

  /**
   * Returns the ID of the factory used to create the actor from the configuration data {@link IActorCfg}.
   * <p>
   * It is assumed that somewhere exists the registry containing factories.
   *
   * @return {@link String} - the VISEL factory ID
   */
  String factoryId();

  /**
   * Returns the values of the properties used to build the VISEL instance.
   *
   * @return {@link IOptionSet} - property values for VISEL instance creation
   */
  IOptionSet propValues();

  /**
   * Returns the data designed for further extension of the mnemos functionality..
   *
   * @return {@link IKeepablesStorage} - arbitrary data
   */
  IKeepablesStorage extraData();

}
