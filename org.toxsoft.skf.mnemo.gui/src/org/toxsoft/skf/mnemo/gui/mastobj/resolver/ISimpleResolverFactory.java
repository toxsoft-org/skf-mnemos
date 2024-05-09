package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * The description and factory of the UGWI resolver {@link IUgwiResolver}.
 *
 * @author hazard157
 */
public interface ISimpleResolverFactory
    extends IStridableParameterized {

  /**
   * Creates the configured new instance of the resolver.
   *
   * @param aResolverConfig {@link IOptionSet} - resolver configuration
   * @return {@link IUgwiResolver} - created instance
   * @param aSkConn {@link ISkConnection} - active SK-connection used for resolver configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #validateConfigOptions(IOptionSet, ISkConnection)}
   */
  IUgwiResolver createResolver( IOptionSet aResolverConfig, ISkConnection aSkConn );

  /**
   * Checks configuration options for validity.
   *
   * @param aResolverConfig {@link IOptionSet} - resolver configuration
   * @param aSkConn {@link ISkConnection} - active SK-connection used for resolver configuration
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult validateConfigOptions( IOptionSet aResolverConfig, ISkConnection aSkConn );

  /**
   * Returns the resolver configuration options definitions.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - options definitions
   */
  IStridablesList<IDataDef> opDefs();

  /**
   * Creates of the configuration options editor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aSkConn {@link ISkConnection} - active SK-connection used for resolver configuration
   * @return {@link IGenericEntityEditPanel}&lt;{@link IOptionSet}&gt; - created editor panel
   */
  IGenericEntityEditPanel<IOptionSet> createConfigEditPanel( ITsGuiContext aContext, ISkConnection aSkConn );

}
