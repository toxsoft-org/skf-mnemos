package org.toxsoft.skf.mnemo.gui.tools.rgbaset;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * Редактор атрибутов для рисования линии.
 * <p>
 *
 * @author vs
 */
public class ValedRgbaSet
    extends AbstractValedLabelAndButton<IRgbaSet> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".RgbaSet"; //$NON-NLS-1$

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
    protected IValedControl<IRgbaSet> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedRgbaSet( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( RgbaSet.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  IRgbaSet rgbaSet = new RgbaSet();

  protected ValedRgbaSet( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    IRgbaSet result = PanelRgbaSetEditor.editRgbaSet( rgbaSet, tsContext() );
    if( result == null ) {
      return false;
    }
    rgbaSet = result;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    getLabelControl().setText( rgbaSet.toString() );
  }

  @Override
  protected IRgbaSet doGetUnvalidatedValue() {
    return rgbaSet;
  }

  @Override
  protected void doDoSetUnvalidatedValue( IRgbaSet aValue ) {
    rgbaSet = aValue;
  }

}
