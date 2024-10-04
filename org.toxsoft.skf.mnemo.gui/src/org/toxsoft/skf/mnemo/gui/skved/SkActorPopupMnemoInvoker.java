package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

public class SkActorPopupMnemoInvoker
    extends AbstractSkVedClickableActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.PopupMnemoInvoker"; //$NON-NLS-1$

  /**
   * ИД поля "Skid мнемосхемы"
   */
  private static final String FID_MNEMO_SKID = "mnemoSkid"; //$NON-NLS-1$

  public static final ITinFieldInfo TFI_MNEMO_SKID = new TinFieldInfo( FID_MNEMO_SKID, TTI_SKID, //
      TSID_NAME, "Мнемосхема", //
      TSID_DESCRIPTION, "ИД объекта мнемосхемы" );

  /**
   * ИД поля "Кнопка мыши"
   */
  private static final String FID_MOUSE_BUTTON = "mouseButton"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_MOUSE_BUTTON = new TinFieldInfo( FID_MOUSE_BUTTON, TtiAvEnum.INSTANCE, //
      TSID_NAME, "Кнопка мыши", //
      TSID_DESCRIPTION, "Кнопка мыши для вызова всплывающего окна мнемосхемы", //
      TSID_KEEPER_ID, ERtActionMouseButton.KEEPER_ID //
  );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, "Показ мнемосхемы", //
      TSID_DESCRIPTION, "Вызов диалогового окна с указанной мнемосхемой", //
      TSID_ICON_ID, ICONID_RT_ACTION_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_MNEMO_SKID );
      fields.add( TFI_MOUSE_BUTTON );
      fields.add( TFI_SKID_UGWI );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorPopupMnemoInvoker.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorPopupMnemoInvoker( aCfg, propDefs(), aVedScreen );
    }

  };

  SkActorPopupMnemoInvoker( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    setMouseClickHandler( new IMouseClickHandler() {

      @Override
      public void onClick( VedAbstractVisel aVisel, ETsMouseButton aButton ) {
        ERtActionMouseButton actionButton = props().getValobj( FID_MOUSE_BUTTON );
        if( isMyMouseButton( actionButton, aButton ) ) {
          openPopupMnemo();
        }
      }

      @Override
      public void onDoubleClick( VedAbstractVisel aVisel ) {
        // TODO Auto-generated method stub

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

  /**
   * Create popup window with mnemo inside
   */
  private void openPopupMnemo() {
    ISkMnemosService mnemoService = skConn().coreApi().getService( ISkMnemosService.SERVICE_ID );
    // Ugwi mnemoUgwi = props().getValobj( FID_MNEMO_SKID );
    // Skid mnemoSkid = UgwiKindSkSkid.getSkid( mnemoUgwi );
    Skid mnemoSkid = props().getValobj( FID_MNEMO_SKID );
    ISkMnemoCfg mnemoCfg = mnemoService.getMnemo( mnemoSkid.strid() );
    IAtomicValue value = props().getValue( TFI_SKID_UGWI.id() );
    if( value.isAssigned() ) {
      Ugwi moUgwi = props().getValobj( TFI_SKID_UGWI.id() );
      Skid moSkid = UgwiKindSkSkid.getSkid( moUgwi );
      ISimpleResolverFactoriesRegistry resRegistry = tsContext().get( ISimpleResolverFactoriesRegistry.class );
      MnemoMasterObjectManager mmoManager = new MnemoMasterObjectManager( skConn(), resRegistry );
      Ugwi ugwi = UgwiKindSkSkid.makeUgwi( moSkid.classId(), moSkid.strid() );
      IVedScreenCfg scrCfg = VedScreenCfg.KEEPER.str2ent( mnemoCfg.cfgData() );
      IVedScreenCfg newCfg = mmoManager.processMasterObject( ugwi, scrCfg, skConn() );
      mnemoCfg.setCfgData( VedScreenCfg.KEEPER.ent2str( newCfg ) );
    }
    PopupMnemoDialogPanel.showPopMnemo( tsContext().eclipseContext(), mnemoCfg );
  }

  /**
   * Test if user click on proper mouse button
   *
   * @param aActionButton - designed mouse button
   * @param aMouseButton - real user selected mouse button
   * @return true if click on proper mouse button
   */
  protected boolean isMyMouseButton( ERtActionMouseButton aActionButton, ETsMouseButton aMouseButton ) {
    boolean retVal = false;
    switch( aActionButton ) {
      case LEFT:
        if( aMouseButton.equals( ETsMouseButton.LEFT ) ) {
          retVal = true;
        }
        break;
      case MIDDLE:
        if( aMouseButton.equals( ETsMouseButton.MIDDLE ) ) {
          retVal = true;
        }
        break;
      case RIGHT:
        if( aMouseButton.equals( ETsMouseButton.RIGHT ) ) {
          retVal = true;
        }
        break;
      // $CASES-OMITTED$
      default:
        break;

    }
    return retVal;
  }

}
