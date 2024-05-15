package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * {@link ISimpleResolverFactory} implementation.
 *
 * @author hazard157
 */
public abstract class AbstractSimpleResolverFactory
    extends StridableParameterized
    implements ISimpleResolverFactory {

  private final IStridablesListEdit<IDataDef> opDefs = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - the ID (IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} initial values
   * @param aOpDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - options definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public AbstractSimpleResolverFactory( String aId, OptionSet aParams, IStridablesList<IDataDef> aOpDefs ) {
    super( aId, aParams );
    opDefs.setAll( aOpDefs );
  }

  // ------------------------------------------------------------------------------------
  // IUgwiResolverFactory
  //

  @Override
  final public IUgwiResolver createResolver( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    TsValidationFailedRtException.checkError( validateConfigOptions( aResolverConfig, aSkConn ) );
    IUgwiResolver resolver = doCreateResolver( aResolverConfig, aSkConn );
    TsInternalErrorRtException.checkNull( resolver );
    return resolver;
  }

  @Override
  final public ValidationResult validateConfigOptions( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    ValidationResult vr = OptionSetUtils.validateOptionSet( aResolverConfig, opDefs );
    if( !vr.isError() ) {
      vr = ValidationResult.firstNonOk( vr, doValidateConfigOptions( aResolverConfig, aSkConn ) );
    }
    return vr;
  }

  @Override
  final public IStridablesListEdit<IDataDef> opDefs() {
    return opDefs;
  }

  @Override
  final public IGenericEntityEditPanel<IOptionSet> createConfigEditPanel( ITsGuiContext aContext,
      ISkConnection aSkConn ) {
    TsNullArgumentRtException.checkNulls( aContext, aSkConn );
    return doCreateConfigEditPanel( aContext, aSkConn );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  /**
   * Subclass must create the resolver instance.
   * <p>
   * Arguments are already validated with {@link #validateConfigOptions(IOptionSet, ISkConnection)}.
   *
   * @param aResolverConfig {@link IOptionSet} - resolver configuration, never is <code>null</code>
   * @param aSkConn {@link ISkConnection} - SK-connection, never is <code>null</code>
   * @return {@link AbstractSimpleResolver} - created instance
   */
  protected abstract AbstractSimpleResolver doCreateResolver( IOptionSet aResolverConfig, ISkConnection aSkConn );

  /**
   * Subclass may perform additional check of the configuration options.
   * <p>
   * Arguments are already validated with {@link OptionSetUtils#validateOptionSet(IOptionSet, IStridablesList)}.
   *
   * @param aResolverConfig {@link IOptionSet} - resolver configuration, never is <code>null</code>
   * @param aSkConn {@link ISkConnection} - SK-connection, never is <code>null</code>
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doValidateConfigOptions( IOptionSet aResolverConfig, ISkConnection aSkConn ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Subclass may create own implementation of the resolver configuration panel.
   * <p>
   * In the base class returns nw instance of {@link OptionSetPanel}, no need to call superclass methd when overriding.
   *
   * @param aContext {@link ITsGuiContext} - the context, never is <code>null</code>
   * @param aSkConn {@link ISkConnection} - SK-connection, never is <code>null</code>
   * @return {@link IGenericCollPanel}&lt;{@link IOptionSet}&gt; - create instance of the editor panel
   */
  protected IGenericEntityEditPanel<IOptionSet> doCreateConfigEditPanel( ITsGuiContext aContext,
      ISkConnection aSkConn ) {
    return new OptionSetPanel( aContext, false, true );
  }

}
