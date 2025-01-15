package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
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
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.cmd.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.tti.*;

public class SkActorUserAction
    extends AbstractSkVedClickableActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.UserAction"; //$NON-NLS-1$

  /**
   * ИД поля "Кнопка мыши"
   */
  private static final String FID_MOUSE_BUTTON = "mouseButton"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_MOUSE_BUTTON = new TinFieldInfo( FID_MOUSE_BUTTON, TtiAvEnum.INSTANCE, //
      TSID_NAME, STR_MOUSE_BUTTON, //
      TSID_DESCRIPTION, STR_MOUSE_BUTTON_D, //
      TSID_KEEPER_ID, ERtActionMouseButton.KEEPER_ID //
  );

  /**
   * ИД поля "Двойной щелчок"
   */
  private static final String FID_DOUBLE_CLICK = "doubleClick"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_DOUBLE_CLIK = new TinFieldInfo( FID_DOUBLE_CLICK, TTI_AT_BOOLEAN, //
      TSID_NAME, STR_DOUBLE_CLICK, //
      TSID_DESCRIPTION, STR_DOUBLE_CLICK_D, //
      TSID_KEEPER_ID, ERtActionMouseButton.KEEPER_ID //
  );

  /**
   * ИД поля "Кнопка мыши"
   */
  private static final String FID_KEY_MASK = "keyMask"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_KEY_MASK = new TinFieldInfo( FID_KEY_MASK, TtiKeyMask.INSTANCE, //
      TSID_NAME, STR_KEY_MASK, //
      TSID_DESCRIPTION, STR_KEY_MASK_D //
  );

  /**
   * ИД поля "Коммандер"
   */
  private static final String FID_USER_ACTION_CFG = "userActionCfg"; //$NON-NLS-1$

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
      TSID_NAME, STR_USER_ACTION, //
      TSID_DESCRIPTION, STR_USER_ACTION_D, //
      TSID_ICON_ID, ICONID_USER_ACTION //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      // fields.add( TFI_CAPTION );
      // fields.add( TinFieldInfo.makeCopy( TFI_ATTR_UGWI, TSID_NAME, "Название объекта" ) );
      // fields.add( TFI_MNEMO_SKID );
      fields.add( TFI_MOUSE_BUTTON );
      fields.add( TFI_DOUBLE_CLIK );
      fields.add( TFI_KEY_MASK );
      fields.add( TFI_COMMANDER );
      // fields.add( TFI_SKID_UGWI );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorUserAction.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorUserAction( aCfg, propDefs(), aVedScreen );
    }

  };

  SkActorUserAction( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    setMouseClickHandler( new IMouseClickHandler() {

      @Override
      public void onClick( VedAbstractVisel aVisel, ETsMouseButton aButton, ITsPoint aCoors, int aState ) {
        if( !props().getBool( FID_DOUBLE_CLICK ) ) {
          ERtActionMouseButton actionButton = props().getValobj( FID_MOUSE_BUTTON );
          if( isMyMouseButton( actionButton, aButton, aState ) ) {
            doUserAction();
          }
        }
      }

      @Override
      public void onDoubleClick( VedAbstractVisel aVisel, ETsMouseButton aButton, ITsPoint aCoors, int aState ) {
        if( props().getBool( FID_DOUBLE_CLICK ) ) {
          ERtActionMouseButton actionButton = props().getValobj( FID_MOUSE_BUTTON );
          if( isMyMouseButton( actionButton, aButton, aState ) ) {
            doUserAction();
          }
        }
      }

    } );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected IGwidList doListUsedGwids() {
    return IGwidList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void doUserAction() {
    IAtomicValue v = props().getValue( PROP_USER_ACTION_CFG );
    if( v.isAssigned() ) {
      VedUserActionCfg cfg = v.asValobj();
      MnemoUserActionsRegistry mcr = tsContext().get( MnemoUserActionsRegistry.class );
      IMnemoUserAction mc = mcr.registeredActions().getByKey( cfg.commanderId() );
      resolve( cfg );
      mc.run( cfg.propValues(), vedScreen().tsContext() );
    }
  }

  void resolve( VedUserActionCfg aCfg ) {
    String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
    if( aCfg.params().hasKey( sectionId ) ) {
      // MnemoResolverConfig mrc = MasterObjectUtils.readMnemoResolverConfig( vedScreen() );
      VedScreenCfg vsCfg = VedScreenUtils.getVedScreenConfig( vedScreen() );
      Ugwi masterUgwi = vsCfg.extraData().readItem( VED_SCREEN_MAIN_MNEMO_MASTER_UGWI, Ugwi.KEEPER, null );
      if( masterUgwi != null ) {
        ISimpleResolverFactoriesRegistry registry = tsContext().get( ISimpleResolverFactoriesRegistry.class );
        IStringMap<ICompoundResolverConfig> resolvers;
        resolvers = CompoundResolverConfig.KEEPER.str2smap( aCfg.params().getStr( sectionId ) );
        for( String id : resolvers.keys() ) {
          ICompoundResolverConfig rCfg = resolvers.getByKey( id );
          IUgwiResolver resolver = CompoundResolver.create( rCfg, skConn(), registry );
          Ugwi destUgwi = resolver.resolve( masterUgwi );
          aCfg.propValues().setValobj( id, destUgwi );
        }
      }
    }
  }

  /**
   * Test if user click on proper mouse button
   *
   * @param aActionButton - designed mouse button
   * @param aMouseButton - real user selected mouse button
   * @param aState int - SWT код состояния управляющих клавиш Shift, Alt, Ctrl
   * @return true if click on proper mouse button
   */
  protected boolean isMyMouseButton( ERtActionMouseButton aActionButton, ETsMouseButton aMouseButton, int aState ) {
    boolean retVal = false;
    int keyMask = props().getInt( FID_KEY_MASK );
    switch( aActionButton ) {
      case LEFT:
        if( aMouseButton.equals( ETsMouseButton.LEFT ) && (keyMask & aState) == keyMask ) {
          retVal = true;
        }
        break;
      case MIDDLE:
        if( aMouseButton.equals( ETsMouseButton.MIDDLE ) && (keyMask & aState) == keyMask ) {
          retVal = true;
        }
        break;
      case RIGHT:
        if( aMouseButton.equals( ETsMouseButton.RIGHT ) && (keyMask & aState) == keyMask ) {
          retVal = true;
        }
        break;
      // $CASES-OMITTED$
      default:
        break;

    }
    return retVal;
  }

  // void updateActorSubmaster() {
  // MnemoResolverConfig mrCfg = MasterObjectUtils.readMnemoResolverConfig( vedScreen() );
  // String masterClassId = MasterObjectUtils.findMainMasterClassId( mrCfg );
  // if( masterClassId != null ) {
  // mrCfg.defineActorSubmaster( id(), smCfg.id() );
  // MasterObjectUtils.updateMnemoResolverConfig( mrCfg, vedScreen() );
  // }
  // }

}
