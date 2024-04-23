package org.toxsoft.skf.mnemo.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.mnemo.gui.km5.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.valed.*;
import org.toxsoft.skf.mnemo.gui.tools.imageset.*;
import org.toxsoft.skf.mnemo.gui.tools.rgbaset.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.lib.impl.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The library quant.
 *
 * @author hazard157, vs, dima
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
    TsValobjUtils.registerKeeperIfNone( ETsFillKind.KEEPER_ID, ETsFillKind.KEEPER );
    TsValobjUtils.registerKeeperIfNone( RgbaSet.KEEPER_ID, RgbaSet.KEEPER );
    TsValobjUtils.registerKeeperIfNone( ImageEntryInfo.KEEPER_ID, ImageEntryInfo.KEEPER );
    TsValobjUtils.registerKeeperIfNone( MnemoImageSetInfo.KEEPER_ID, MnemoImageSetInfo.KEEPER );
    TsValobjUtils.registerKeeperIfNone( RriId.KEEPER_ID, RriId.KEEPER );
    TsValobjUtils.registerKeeperIfNone( D2Margins.KEEPER_ID, D2Margins.KEEPER );
    TsValobjUtils.registerKeeperIfNone( VedLayoutControllerConfig.KEEPER_ID, VedLayoutControllerConfig.KEEPER );
    TsValobjUtils.registerKeeperIfNone( ERtActionKind.KEEPER_ID, ERtActionKind.KEEPER );
    TsValobjUtils.registerKeeperIfNone( RunTimeUserActionInfo.KEEPER_ID, RunTimeUserActionInfo.KEEPER );
    TsValobjUtils.registerKeeperIfNone( ERtActionMouseButton.KEEPER_ID, ERtActionMouseButton.KEEPER );
    TsValobjUtils.registerKeeperIfNone( PopupMnemoInfo.KEEPER_ID, PopupMnemoInfo.KEEPER );
    TsValobjUtils.registerKeeperIfNone( SwitchPerspInfo.KEEPER_ID, SwitchPerspInfo.KEEPER );

    // IVedViselFactoriesRegistry visFact = aAppContext.get( IVedViselFactoriesRegistry.class );
    // visFact.register( ViselPanel.FACTORY );

    IVedActorFactoriesRegistry actFact = aAppContext.get( IVedActorFactoriesRegistry.class );
    actFact.register( SkActorAttrText.FACTORY );
    actFact.register( SkActorRtdataText.FACTORY );
    actFact.register( SkActorRtdataValue.FACTORY );
    actFact.register( SkActorRtBooleanValue.FACTORY );
    actFact.register( SkActorCmdButton.FACTORY );
    actFact.register( SkActorColorDecorator.FACTORY );
    actFact.register( SkActorRtdataImage.FACTORY );
    actFact.register( SkActorInputField.FACTORY );
    actFact.register( SkActorRriInputField.FACTORY );
    actFact.register( SkActorRunTimeAction.FACTORY );

  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    ISkMnemoGuiConstants.init( aWinContext );

    IValedControlFactoriesRegistry vcfRegistry = aWinContext.get( IValedControlFactoriesRegistry.class );
    vcfRegistry.registerFactory( ValedRgbaSet.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRgbaSet.FACTORY );
    vcfRegistry.registerFactory( ValedImageInfoesSet.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjImageInfoesSet.FACTORY );
    vcfRegistry.registerFactory( ValedAnyGwidEditor.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjAnyGwidEditor.FACTORY );
    vcfRegistry.registerFactory( ValedRriIdEditor.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRriIdEditor.FACTORY );
    vcfRegistry.registerFactory( ValedRtUserActionInfo.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRtUserActionInfo.FACTORY );
    vcfRegistry.registerFactory( ValedPopupMnemoInfo.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjPopupMnemoInfo.FACTORY );
    vcfRegistry.registerFactory( ValedSwitchPerspInfo.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjSwitchPerspInfo.FACTORY );
  }

}
