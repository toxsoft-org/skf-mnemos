package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

public class ValedAvValobjMnemoCommandCfg
    extends AbstractAvWrapperValedControl<MnemoCommandCfg> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjMnemoCommandCfg"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjMnemoCommandCfg( aContext );
    }

  };

  ValedAvValobjMnemoCommandCfg( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.VALOBJ, ValedMnemoCommandCfg.FACTORY );
  }

  @Override
  protected IAtomicValue tv2av( MnemoCommandCfg aTypedValue ) {
    if( aTypedValue != null ) {
      return avValobj( aTypedValue );
    }
    return IAtomicValue.NULL;
  }

  @Override
  protected MnemoCommandCfg av2tv( IAtomicValue aAtomicValue ) {
    if( aAtomicValue != null && aAtomicValue.isAssigned() ) {
      return aAtomicValue.asValobj();
    }
    return null;
  }

}
