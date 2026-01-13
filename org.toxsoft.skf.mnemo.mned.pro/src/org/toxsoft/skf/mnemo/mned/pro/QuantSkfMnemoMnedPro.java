package org.toxsoft.skf.mnemo.mned.pro;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantSkfMnemoMnedPro
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkfMnemoMnedPro() {
    super( QuantSkfMnemoMnedPro.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkfMnemMnedProConstants.init( aWinContext );
  }

}
