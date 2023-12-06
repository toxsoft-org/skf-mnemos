package org.toxsoft.skf.mnemo.gui.tools.rgbaset;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

/**
 * Редактор {@link IRgbaSet} как {@link EAtomicType#VALOBJ}.
 *
 * @author vs
 */
public class ValedAvValobjRgbaSet
    extends AbstractAvValobjWrapperValedControl<IRgbaSet> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjRgbaSet"; //$NON-NLS-1$

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
      return new ValedAvValobjRgbaSet( aContext );
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

  protected ValedAvValobjRgbaSet( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedRgbaSet.FACTORY );
  }

}
