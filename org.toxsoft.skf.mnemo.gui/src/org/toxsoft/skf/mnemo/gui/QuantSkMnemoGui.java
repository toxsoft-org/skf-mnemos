package org.toxsoft.skf.mnemo.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.mnemo.gui.km5.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.tools.rgbaset.*;
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
    TsValobjUtils.registerKeeperIfNone( RgbaSet.KEEPER_ID, RgbaSet.KEEPER );
    IVedActorFactoriesRegistry actFact = aAppContext.get( IVedActorFactoriesRegistry.class );
    actFact.register( SkActorRtdataText.FACTORY );
    actFact.register( SkActorRtdataValue.FACTORY );
    actFact.register( SkActorCmdButton.FACTORY );
    actFact.register( SkActorColorDecorator.FACTORY );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkMnemoGuiConstants.init( aWinContext );
    IValedControlFactoriesRegistry vcfRegistry = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfRegistry.registerFactory( ValedRgbaSet.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRgbaSet.FACTORY );
  }

}
