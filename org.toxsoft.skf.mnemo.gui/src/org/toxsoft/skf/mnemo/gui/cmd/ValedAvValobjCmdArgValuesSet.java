package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

public class ValedAvValobjCmdArgValuesSet
    extends AbstractAvWrapperValedControl<CmdArgValuesSet> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjCmdArgValuesSet"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjCmdArgValuesSet( aContext );
    }

  };

  ValedAvValobjCmdArgValuesSet( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.VALOBJ, ValedCmdArgValuesSet.FACTORY );
  }

  @Override
  protected IAtomicValue tv2av( CmdArgValuesSet aTypedValue ) {
    return avValobj( aTypedValue );
  }

  @Override
  protected CmdArgValuesSet av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asValobj();
  }

}
