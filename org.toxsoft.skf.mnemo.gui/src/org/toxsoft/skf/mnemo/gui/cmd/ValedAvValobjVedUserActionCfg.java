package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;

public class ValedAvValobjVedUserActionCfg
    extends AbstractAvWrapperValedControl<VedUserActionCfg> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjVedUserActionCfg"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjVedUserActionCfg( aContext );
    }

  };

  ValedAvValobjVedUserActionCfg( ITsGuiContext aTsContext ) {
    super( aTsContext, EAtomicType.VALOBJ, ValedVedUserActionCfg.FACTORY );
  }

  @Override
  protected IAtomicValue tv2av( VedUserActionCfg aTypedValue ) {
    return avValobj( aTypedValue );
  }

  @Override
  protected VedUserActionCfg av2tv( IAtomicValue aAtomicValue ) {
    return aAtomicValue.asValobj();
  }

}
