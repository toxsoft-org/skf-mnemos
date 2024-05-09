package org.toxsoft.skf.mnemo.gui.mastobj;

import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Configuration of the master objects used when resolving mnemoscheme items resolvable properties.
 *
 * @author hazard157
 */
public interface IMnemoResolverConfig {

  /**
   * Empty configuration singleton.
   */
  IMnemoResolverConfig EMPTY = new MnemoResolverConfig();

  /**
   * Determines sub-master objects for the mnemoscheme.
   * <p>
   * Each element of the returned list denotes a sub-master object. Sub-master object UGWI is to be resolved from the
   * mnemoscheme main master as specified by {@link SubmasterConfig#resolverCfg()}.
   * <p>
   * Sub-master objects are optional for the mnemoscheme, this method may returna an empty list.
   *
   * @return {@link IStridablesListEdit}&lt;{@link SubmasterConfig}&gt; - sub-master objects configureation
   */
  IStridablesList<SubmasterConfig> subMasters();

  /**
   * Determines which master object is used by which actor.
   * <p>
   * Returned map keys are IDs of the VED actors, values - IDs of sub-master objects (used as a key to
   * {@link #subMasters()}). If actor ID does not presents in this map, are value for actor is an emty String, then the
   * mnemoscheme master is to be used when resolving propertis of the actor.
   *
   * @return {@link IStringMap}&lt;String&gt; - map "VED actor ID" - "Sub-master object ID"
   */
  IStringMap<String> actorSubmasterIds();

}
