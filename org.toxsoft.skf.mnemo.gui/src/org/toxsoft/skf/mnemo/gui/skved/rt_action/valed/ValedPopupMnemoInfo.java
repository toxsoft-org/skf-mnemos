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
 * Editor for popup mnemo.
 * <p>
 *
 * @author dima
 */
public class ValedPopupMnemoInfo
    extends AbstractValedLabelAndButton<PopupMnemoInfo> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ValedPopupMnemoInfo"; //$NON-NLS-1$

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
    protected IValedControl<PopupMnemoInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedPopupMnemoInfo( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( PopupMnemoInfo.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  PopupMnemoInfo popupMnemoInfo = PopupMnemoInfo.EMPTY;

  protected ValedPopupMnemoInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    // use m5 technology to edit PopupMnemoInfo entity
    ISkConnectionSupplier connSup = tsContext().get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();

    IM5Model<PopupMnemoInfo> model =
        conn.scope().get( IM5Domain.class ).getModel( PopupMnemoInfoM5Model.MODEL_ID, PopupMnemoInfo.class );
    TsDialogInfo cdi = new TsDialogInfo( tsContext(), null, STR_N_POPUP_MNEMO_INFO, STR_D_POPUP_MNEMO_INFO, 0 );

    // редактируем
    PopupMnemoInfo tmpPopupMnemoInfo =
        M5GuiUtils.askEdit( tsContext(), model, popupMnemoInfo, cdi, model.getLifecycleManager( conn ) );
    if( tmpPopupMnemoInfo == null ) {
      return false;
    }
    popupMnemoInfo = tmpPopupMnemoInfo;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    // TODO what to write/display in label ?
  }

  @Override
  protected PopupMnemoInfo doGetUnvalidatedValue() {
    return popupMnemoInfo;
  }

  @Override
  protected void doDoSetUnvalidatedValue( PopupMnemoInfo aValue ) {
    popupMnemoInfo = aValue;
  }

}
