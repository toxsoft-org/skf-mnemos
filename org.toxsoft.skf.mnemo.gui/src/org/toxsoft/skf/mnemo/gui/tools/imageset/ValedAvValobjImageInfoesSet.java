package org.toxsoft.skf.mnemo.gui.tools.imageset;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.skf.mnemo.gui.tools.rgbaset.*;

/**
 * Редактор {@link IRgbaSet} как {@link EAtomicType#VALOBJ}.
 *
 * @author vs
 */
public class ValedAvValobjImageInfoesSet
    extends AbstractAvValobjWrapperValedControl<IMnemoImageSetInfo> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjImageInfoesSet"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjImageInfoesSet( aContext );
    }

    @Override
    protected boolean isSuitableAvEditor( EAtomicType aAtomicType, String aKeeperId, ITsGuiContext aEditorContext ) {
      if( aAtomicType == EAtomicType.VALOBJ && aKeeperId != null ) {
        return aKeeperId.equals( RgbaSet.KEEPER_ID );
      }
      return false;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  protected ValedAvValobjImageInfoesSet( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedImageInfoesSet.FACTORY );
  }

}
