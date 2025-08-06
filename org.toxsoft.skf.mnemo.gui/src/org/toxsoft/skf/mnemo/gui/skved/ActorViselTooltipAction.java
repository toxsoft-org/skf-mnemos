package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.skf.mnemo.gui.cmd.*;

/**
 * Актор глобального действия, т.е. не прикрепленный к конкретному визелю, который вызывает пользовательское действие
 * {@link IMnemoUserAction} в момент когда курсор мыши оказывается в области какого-либо визуального элемента
 * {@link IVedVisel}.
 * <p>
 *
 * @author vs
 */
public class ActorViselTooltipAction
    extends VedAbstractActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.ViselTooltip"; //$NON-NLS-1$

  /**
   * ИД поля "Коммандер"
   */
  public static final String FID_USER_ACTION_CFG = "userActionCfg"; //$NON-NLS-1$

  private static final IDataDef PROP_USER_ACTION_CFG = DataDef.create3( FID_USER_ACTION_CFG, DDEF_VALOBJ, //
      TSID_NAME, STR_USER_ACTION_CFG, //
      TSID_DESCRIPTION, STR_USER_ACTION_CFG_D, //
      TSID_KEEPER_ID, VedUserActionCfg.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjVedUserActionCfg.FACTORY_NAME //
  );

  private static final ITinTypeInfo TTI_COMMANDER =
      new TinAtomicTypeInfo.TtiValobj<>( PROP_USER_ACTION_CFG, VedUserActionCfg.class );

  private static final ITinFieldInfo TFI_COMMANDER = new TinFieldInfo( PROP_USER_ACTION_CFG, TTI_COMMANDER );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_TOOLTIP_ACTION, //
      TSID_DESCRIPTION, STR_TOOLTIP_ACTION_D, //
      TSID_ICON_ID, ICONID_USER_ACTION //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_COMMANDER );
      return new PropertableEntitiesTinTypeInfo<>( fields, ActorViselTooltipAction.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ActorViselTooltipAction( aCfg, propDefs(), aVedScreen );
    }

  };

  private IVedVisel currVisel = null;

  ActorViselTooltipAction( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    IVedVisel v = viselUnderCursor( aCoors );
    if( v == null ) {
      vedScreen().view().getControl().setToolTipText( null );
      currVisel = null;
    }
    else {
      // System.out.println( "VISEL: " + v );
      if( !v.equals( currVisel ) ) {
        currVisel = v;
        doUserAction( aCoors );
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // IVedActor
  //

  @Override
  public boolean isBoudable() {
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void doUserAction( ITsPoint aSwtCoors ) {
    IAtomicValue v = props().getValue( PROP_USER_ACTION_CFG );
    if( v.isAssigned() ) {
      VedUserActionCfg cfg = v.asValobj();
      MnemoUserActionsRegistry mcr = tsContext().get( MnemoUserActionsRegistry.class );
      IMnemoUserAction mc = mcr.registeredActions().getByKey( cfg.commanderId() );
      // resolve( cfg );
      TsGuiContext ctx = new TsGuiContext( vedScreen().tsContext() );
      ctx.put( IVedScreen.class, vedScreen() );
      mc.run( cfg.propValues(), aSwtCoors, ctx );
    }
  }

  // private void resolve( VedUserActionCfg aCfg ) {
  // // String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
  // // if( aCfg.params().hasKey( sectionId ) ) {
  // // // MnemoResolverConfig mrc = MasterObjectUtils.readMnemoResolverConfig( vedScreen() );
  // // VedScreenCfg vsCfg = VedScreenUtils.getVedScreenConfig( vedScreen() );
  // // Ugwi masterUgwi = vsCfg.extraData().readItem( VED_SCREEN_MAIN_MNEMO_MASTER_UGWI, Ugwi.KEEPER, null );
  // // if( masterUgwi != null ) {
  // // ISimpleResolverFactoriesRegistry registry = tsContext().get( ISimpleResolverFactoriesRegistry.class );
  // // IStringMap<ICompoundResolverConfig> resolvers;
  // // resolvers = CompoundResolverConfig.KEEPER.str2strmap( aCfg.params().getStr( sectionId ) );
  // // for( String id : resolvers.keys() ) {
  // // ICompoundResolverConfig rCfg = resolvers.getByKey( id );
  // // IUgwiResolver resolver = CompoundResolver.create( rCfg, skConn(), registry );
  // // Ugwi destUgwi = resolver.resolve( masterUgwi );
  // // aCfg.propValues().setValobj( id, destUgwi );
  // // }
  // // }
  // // }
  // }

  private VedAbstractVisel viselUnderCursor( ITsPoint aCoors ) {
    IStringList ids = vedScreen().view().listViselIdsAtPoint( aCoors );
    if( !ids.isEmpty() ) {
      return vedScreen().model().visels().list().findByKey( ids.first() );
    }
    return null;
  }

  // private void setActivated( boolean aActivated ) {
  // activated = aActivated;
  // if( activated ) {
  // setHandCursor();
  // }
  // else {
  // restorCursor();
  // }
  // }

  // private void setHandCursor() {
  // Cursor cur = vedScreen().view().getControl().getCursor();
  // if( cur == null || !cur.equals( handCursor ) ) {
  // prevCursor = vedScreen().view().getControl().getCursor();
  // vedScreen().view().getControl().setCursor( handCursor );
  // }
  // }
  //
  // private void restorCursor() {
  // vedScreen().view().getControl().setCursor( prevCursor );
  // }

}
