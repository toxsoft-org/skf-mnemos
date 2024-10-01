package org.toxsoft.skf.mnemo.gui.skved.rt_action.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;

/**
 * Editor for PopupMnemoResolverConfig.
 * <p>
 *
 * @author vs
 */
public class ValedPopupMnemoResolverConfig
    extends AbstractValedLabelAndButton<PopupMnemoResolverConfig> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ValedPopupMnemoResolverConfig"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author dima
   */
  public static class Factory
      extends AbstractValedControlFactory {

    /**
     * Constructor.
     */
    public Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<PopupMnemoResolverConfig> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedPopupMnemoResolverConfig( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( PopupMnemoResolverConfig.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  PopupMnemoResolverConfig resolverConfig = null;

  protected ValedPopupMnemoResolverConfig( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    PopupMnemoResolverConfig cfg = PanelPopupMnemoResolverConfig.edit( resolverConfig, tsContext() );
    if( cfg == null ) {
      return false;
    }
    resolverConfig = cfg;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    if( resolverConfig == null ) {
      getLabelControl().setText( "none" ); //$NON-NLS-1$
    }
    else {
      getLabelControl().setText( resolverConfig.toString() );
    }
  }

  @Override
  protected PopupMnemoResolverConfig doGetUnvalidatedValue() {
    return resolverConfig;
  }

  @Override
  protected void doDoSetUnvalidatedValue( PopupMnemoResolverConfig aValue ) {
    resolverConfig = aValue;
  }

}
