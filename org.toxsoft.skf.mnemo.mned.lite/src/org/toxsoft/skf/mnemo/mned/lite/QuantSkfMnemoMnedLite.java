package org.toxsoft.skf.mnemo.mned.lite;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantSkfMnemoMnedLite
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkfMnemoMnedLite() {
    super( QuantSkfMnemoMnedLite.class.getSimpleName() );
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
    ISkfMnemMnedLiteConstants.init( aWinContext );
  }

}
