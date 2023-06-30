package org.toxsoft.skf.mnemo.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.mnemo.gui.km5.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * The library quant.
 *
 * @author goga, vs
 */
public class QuantSkMnemoGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkMnemoGui() {
    super( QuantSkMnemoGui.class.getSimpleName() );
    KM5Utils.registerContributorCreator( KM5MnemosContributor.CREATOR );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkMnemoGuiConstants.init( aWinContext );

    TsValobjUtils.registerKeeper( ETsFillKind.KEEPER_ID, ETsFillKind.KEEPER );
  }

}
