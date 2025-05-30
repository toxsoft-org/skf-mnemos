package org.toxsoft.skf.mnemo.gui;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.mnemo.gui.cmd.*;
import org.toxsoft.skf.mnemo.gui.km5.*;
import org.toxsoft.skf.mnemo.gui.m51.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.valed.*;
import org.toxsoft.skf.mnemo.gui.tools.imageset.*;
import org.toxsoft.skf.mnemo.gui.tools.rgbaset.*;
import org.toxsoft.skf.mnemo.gui.tsgui.comps.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;
import org.toxsoft.skf.mnemo.lib.impl.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.gui.km5.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * The library quant.
 *
 * @author hazard157, vs, dima
 */
public class QuantSkMnemoGui
    extends AbstractQuant
    implements ISkCoreExternalHandler {

  /**
   * Constructor.
   */
  public QuantSkMnemoGui() {
    super( QuantSkMnemoGui.class.getSimpleName() );
    SkCoreUtils.registerSkServiceCreator( SkMnemosService.CREATOR );
    KM5Utils.registerContributorCreator( KM5MnemosContributor.CREATOR );
    SkCoreUtils.registerCoreApiHandler( this );
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
    TsValobjUtils.registerKeeperIfNone( PopupMnemoResolverConfig.KEEPER_ID, PopupMnemoResolverConfig.KEEPER );
    TsValobjUtils.registerKeeperIfNone( SwitchPerspInfo.KEEPER_ID, SwitchPerspInfo.KEEPER );
    TsValobjUtils.registerKeeperIfNone( MPerspId.KEEPER_ID, MPerspId.KEEPER );
    TsValobjUtils.registerKeeperIfNone( SkoRecognizerCfg.KEEPER_ID, SkoRecognizerCfg.KEEPER );
    TsValobjUtils.registerKeeperIfNone( VedUserActionCfg.KEEPER_ID, VedUserActionCfg.KEEPER );
    TsValobjUtils.registerKeeperIfNone( CmdArgValuesSet.KEEPER_ID, CmdArgValuesSet.KEEPER );

    IVedViselFactoriesRegistry visFact = aAppContext.get( IVedViselFactoriesRegistry.class );
    visFact.register( ViselImagesetButton.FACTORY );

    IVedActorFactoriesRegistry actFact = aAppContext.get( IVedActorFactoriesRegistry.class );
    actFact.register( SkActorAttrText.FACTORY );
    actFact.register( SkActorRtdataText.FACTORY );
    actFact.register( SkActorRtdataValue.FACTORY );
    actFact.register( SkActorRtBooleanValue.FACTORY );
    actFact.register( SkActorCmdButton.FACTORY );
    actFact.register( SkActorCmdCheckbox.FACTORY );
    actFact.register( SkActorColorDecorator.FACTORY );
    actFact.register( SkActorRtdataImage.FACTORY );
    actFact.register( SkActorInputField.FACTORY );
    actFact.register( SkActorCmdField.FACTORY );
    actFact.register( SkActorRriInputField.FACTORY );
    actFact.register( SkActorRriCheckbox.FACTORY );
    // actFact.register( SkActorRunTimeAction.FACTORY );
    actFact.register( SkActorPopupMnemoInvoker.FACTORY );
    actFact.register( SkActorRtdataRefbook.FACTORY );
    actFact.register( SkActorNameAndTooltip.FACTORY );
    actFact.register( SkActorUserAction.FACTORY );
    actFact.register( ActorViselTooltipAction.FACTORY );
    actFact.register( SkActorUpDownCmdButton.FACTORY );

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
    vcfRegistry.registerFactory( ValedAvPopupMnemoResolverConfig.FACTORY );
    vcfRegistry.registerFactory( ValedSwitchPerspInfo.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjSwitchPerspInfo.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjMPerspIdEditor.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRtdataRefbookAttrInfo.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjVedUserActionCfg.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjCmdArgValuesSet.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRefbookValuesInfo.FACTORY );
    vcfRegistry.registerFactory( ValedAvValobjRefbookItemSelector.FACTORY );

    // ------------------------------------------------------------------------------------
    // Регистрация резолверов
    //

    ISimpleResolverFactoriesRegistry resolversRegistry = aWinContext.get( ISimpleResolverFactoriesRegistry.class );
    if( resolversRegistry == null ) {
      resolversRegistry = new SimpleResolverFactoriesRegistry();
      aWinContext.set( ISimpleResolverFactoriesRegistry.class, resolversRegistry );
    }
    resolversRegistry.register( DirectSkidResolver.FACTORY );
    resolversRegistry.register( DirectAttrResolver.FACTORY );
    resolversRegistry.register( DirectRriAttrResolver.FACTORY );
    resolversRegistry.register( DirectRtDataResolver.FACTORY );
    resolversRegistry.register( DirectCmdResolver.FACTORY );
    resolversRegistry.register( LinkInfoResolver.FACTORY );
    resolversRegistry.register( RivetInfoResolver.FACTORY );

    // ------------------------------------------------------------------------------------
    // M5
    //
    IM5Domain m5 = aWinContext.get( IM5Domain.class );
    m5.addModel( new M5BaseFieldDefModel<>() );

    MnemoUserActionsRegistry mcr = aWinContext.get( MnemoUserActionsRegistry.class );
    if( mcr == null ) {
      mcr = new MnemoUserActionsRegistry();
      aWinContext.set( MnemoUserActionsRegistry.class, mcr );
    }
    mcr.registerAction( new MnemoTestUserAction() );
    mcr.registerAction( new ActPopupChart() );
  }

  @Override
  public void processSkCoreInitialization( IDevCoreApi aCoreApi ) {
    // register abilities
    aCoreApi.userService().abilityManager().defineAbility( ISkMnemoGuiConstants.ABILITY_ACCESS_MNEMOS );
  }

}
