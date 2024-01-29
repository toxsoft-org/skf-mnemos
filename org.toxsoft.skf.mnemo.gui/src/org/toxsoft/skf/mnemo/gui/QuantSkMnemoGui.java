package org.toxsoft.skf.mnemo.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.mnemo.gui.km5.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.tools.imageset.*;
import org.toxsoft.skf.mnemo.gui.tools.rgbaset.*;
import org.toxsoft.skf.mnemo.lib.impl.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The library quant.
 *
 * @author hazard157, vs
 */
public class QuantSkMnemoGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantSkMnemoGui() {
    super( QuantSkMnemoGui.class.getSimpleName() );
    SkCoreUtils.registerSkServiceCreator( SkMnemosService.CREATOR );
    KM5Utils.registerContributorCreator( KM5MnemosContributor.CREATOR );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    TsValobjUtils.registerKeeperIfNone( RgbaSet.KEEPER_ID, RgbaSet.KEEPER );
    TsValobjUtils.registerKeeperIfNone( ImageEntryInfo.KEEPER_ID, ImageEntryInfo.KEEPER );
    TsValobjUtils.registerKeeperIfNone( MnemoImageSetInfo.KEEPER_ID, MnemoImageSetInfo.KEEPER );
    IVedActorFactoriesRegistry actFact = aAppContext.get( IVedActorFactoriesRegistry.class );
    actFact.register( SkActorAttrText.FACTORY );
    actFact.register( SkActorRtdataText.FACTORY );
    actFact.register( SkActorRtdataValue.FACTORY );
    actFact.register( SkActorCmdButton.FACTORY );
    actFact.register( SkActorColorDecorator.FACTORY );
    actFact.register( SkActorRtdataImage.FACTORY );
    actFact.register( SkActorInputField.FACTORY );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkMnemoGuiConstants.init( aWinContext );
    IValedControlFactoriesRegistry vcfRegistry = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfRegistry.registerFactory( ValedRgbaSet.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRgbaSet.FACTORY );
    vcfRegistry.registerFactory( ValedImageInfoesSet.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjImageInfoesSet.FACTORY );
  }

}
