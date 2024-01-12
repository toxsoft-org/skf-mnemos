package org.toxsoft.skf.mnemo.gui.tools.imageset;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.skf.mnemo.gui.tools.rgbaset.*;

/**
 * Редактор атрибутов для рисования линии.
 * <p>
 *
 * @author vs
 */
public class ValedImageInfoesSet
    extends AbstractValedLabelAndButton<IMnemoImageSetInfo> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ImageInfoesSet"; //$NON-NLS-1$

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
    protected IValedControl<IMnemoImageSetInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedImageInfoesSet( aContext );
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

  IMnemoImageSetInfo imageSet = IMnemoImageSetInfo.EMPTY;

  protected ValedImageInfoesSet( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedLabelAndButton
  //

  @Override
  protected boolean doProcessButtonPress() {
    IMnemoImageSetInfo result = PanelImageSetEditor.editInfo( imageSet, tsContext() );
    if( result == null ) {
      return false;
    }
    imageSet = result;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    getLabelControl().setText( imageSet.toString() );
  }

  @Override
  protected IMnemoImageSetInfo doGetUnvalidatedValue() {
    return imageSet;
  }

  @Override
  protected void doDoSetUnvalidatedValue( IMnemoImageSetInfo aValue ) {
    imageSet = aValue;
  }

}
