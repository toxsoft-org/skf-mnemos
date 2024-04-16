package org.toxsoft.skf.mnemo.gui.skved.rt_action.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;

/**
 * Editor info of run-time user action.
 * <p>
 *
 * @author dima
 */
public class ValedRtUserActionInfo
    extends AbstractValedLabelAndButton<RunTimeUserActionInfo> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".RunTimeUserActionInfo"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
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
    protected IValedControl<RunTimeUserActionInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedRtUserActionInfo( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( RunTimeUserActionInfo.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  RunTimeUserActionInfo value = RunTimeUserActionInfo.NONE;

  /**
   * Конструктор.
   *
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  protected ValedRtUserActionInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // {@link AbstractValedLabelAndButton}
  //

  @Override
  protected boolean doProcessButtonPress() {
    RunTimeUserActionInfo fi = RunTimeUserActionInfo.NONE;
    if( value != RunTimeUserActionInfo.NONE ) {
      fi = value;
    }
    // FIXME
    // fi = PanelTsFillInfoSelector.editPattern( fi, tsContext() );
    if( fi == null ) {
      return false;
    }
    value = fi;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    // TODO what to write/display in label ?
  }

  @Override
  protected RunTimeUserActionInfo doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doDoSetUnvalidatedValue( RunTimeUserActionInfo aValue ) {
    value = aValue;
  }

}
