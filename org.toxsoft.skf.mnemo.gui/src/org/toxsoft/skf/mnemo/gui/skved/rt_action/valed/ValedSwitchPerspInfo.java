package org.toxsoft.skf.mnemo.gui.skved.rt_action.valed;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.mnemo.gui.skved.rt_action.valed.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.skf.mnemo.gui.km5.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Editor for action of switch persp.
 * <p>
 *
 * @author dima
 */
public class ValedSwitchPerspInfo
    extends AbstractValedLabelAndButton<SwitchPerspInfo> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ValedSwitchPerspInfo"; //$NON-NLS-1$

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
    protected IValedControl<SwitchPerspInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSwitchPerspInfo( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( SwitchPerspInfo.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  SwitchPerspInfo switchPerspInfo = SwitchPerspInfo.EMPTY;

  protected ValedSwitchPerspInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    // use m5 technology to edit SwitchPerspInfo entity
    ISkConnectionSupplier connSup = tsContext().get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();

    IM5Model<SwitchPerspInfo> model =
        conn.scope().get( IM5Domain.class ).getModel( SwitchPerspInfoM5Model.MODEL_ID, SwitchPerspInfo.class );
    TsDialogInfo cdi = new TsDialogInfo( tsContext(), null, SWITCH_N_MNEMO_INFO, SWITCH_D_MNEMO_INFO, 0 );

    // редактируем
    SwitchPerspInfo tmpSwitchPerspInfo =
        M5GuiUtils.askEdit( tsContext(), model, switchPerspInfo, cdi, model.getLifecycleManager( conn ) );
    if( tmpSwitchPerspInfo == null ) {
      return false;
    }
    switchPerspInfo = tmpSwitchPerspInfo;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    // TODO what to write/display in label ?
  }

  @Override
  protected SwitchPerspInfo doGetUnvalidatedValue() {
    return switchPerspInfo;
  }

  @Override
  protected void doDoSetUnvalidatedValue( SwitchPerspInfo aValue ) {
    switchPerspInfo = aValue;
  }

}
