package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkResources.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.mnemo.gui.glib.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

/**
 * Actor, который по щелчку мыши вызывает всплывающее окно, содержимое которого является мнемосхемой.
 * <p>
 *
 * @author vs
 */
public class SkActorPopupMnemoInvoker
    extends AbstractSkVedClickableActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.PopupMnemoInvoker"; //$NON-NLS-1$

  /**
   * ИД поля "Заголовок окна"
   */
  private static final String FID_CAPTION = "mnemoCaption"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_CAPTION = new TinFieldInfo( FID_CAPTION, TTI_AT_STRING, //
      TSID_NAME, STR_CAPTION, //
      TSID_DESCRIPTION, STR_CAPTION_D );

  /**
   * ИД поля "Skid мнемосхемы"
   */
  private static final String FID_MNEMO_SKID = "mnemoSkid"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_MNEMO_SKID = new TinFieldInfo( FID_MNEMO_SKID, TTI_SKID, //
      TSID_NAME, STR_MNEMOSCHEMA, //
      TSID_DESCRIPTION, STR_MNEMOSCHEMA_D );

  // /**
  // * ИД поля "Кнопка мыши"
  // */
  // private static final String FID_MOUSE_BUTTON = "mouseButton"; //$NON-NLS-1$
  //
  // private static final ITinFieldInfo TFI_MOUSE_BUTTON = new TinFieldInfo( FID_MOUSE_BUTTON, TtiAvEnum.INSTANCE, //
  // TSID_NAME, STR_MOUSE_BUTTON, //
  // TSID_DESCRIPTION, STR_MOUSE_BUTTON_D, //
  // TSID_KEEPER_ID, ERtActionMouseButton.KEEPER_ID //
  // );

  /**
   * ИД поля "Двойной щелчок"
   */
  private static final String FID_DOUBLE_CLICK = "doubleClick"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_DOUBLE_CLIK = new TinFieldInfo( FID_DOUBLE_CLICK, TTI_AT_BOOLEAN, //
      TSID_NAME, STR_DOUBLE_CLICK, //
      TSID_DESCRIPTION, STR_DOUBLE_CLICK_D, //
      TSID_KEEPER_ID, ERtActionMouseButton.KEEPER_ID //
  );

  // /**
  // * ИД поля "Кнопка мыши"
  // */
  // private static final String FID_KEY_MASK = "keyMask"; //$NON-NLS-1$
  //
  // private static final ITinFieldInfo TFI_KEY_MASK = new TinFieldInfo( FID_KEY_MASK, TtiKeyMask.INSTANCE, //
  // TSID_NAME, STR_KEY_MASK, //
  // TSID_DESCRIPTION, STR_KEY_MASK_D //
  // );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_SHOW_MNEMO, //
      TSID_DESCRIPTION, STR_SHOW_MNEMO_D, //
      TSID_ICON_ID, ICONID_RT_ACTION_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_CAPTION );
      // fields.add( TinFieldInfo.makeCopy( TFI_ATTR_UGWI, TSID_NAME, "Название объекта" ) );
      fields.add( TFI_MNEMO_SKID );
      fields.add( TFI_MOUSE_BUTTON );
      fields.add( TFI_DOUBLE_CLIK );
      fields.add( TFI_KEY_MASK );
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
      public void onClick( VedAbstractVisel aVisel, ETsMouseButton aButton, ITsPoint aCoors, int aState ) {
        if( !props().getBool( FID_DOUBLE_CLICK ) ) {
          // ERtActionMouseButton actionButton = props().getValobj( FID_MOUSE_BUTTON );
          // if( isMyMouseButton( actionButton, aButton, aState ) ) {
          // if( isMyMouseButton( actionButton, aButton, aState ) ) {
          openPopupMnemoShell( aCoors );
          // }
        }
      }

      @Override
      public void onDoubleClick( VedAbstractVisel aVisel, ETsMouseButton aButton, ITsPoint aCoors, int aState ) {
        if( props().getBool( FID_DOUBLE_CLICK ) ) {
          // ERtActionMouseButton actionButton = props().getValobj( FID_MOUSE_BUTTON );
          // if( isMyMouseButton( actionButton, aButton, aState ) ) {
          openPopupMnemoShell( aCoors );
          // }
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

  private ISkMnemoCfg getMnemoConfig() {
    ISkMnemosService mnemoService = skConn().coreApi().getService( ISkMnemosService.SERVICE_ID );
    Skid mnemoSkid = props().getValobj( FID_MNEMO_SKID );
    ISkMnemoCfg mnemoCfg = mnemoService.getMnemo( mnemoSkid.strid() );
    IAtomicValue value = props().getValue( TFI_SKID_UGWI.id() );
    if( value.isAssigned() ) {
      Ugwi moUgwi = props().getValobj( TFI_SKID_UGWI.id() );
      if( moUgwi != Ugwi.NONE ) {
        Skid moSkid = UgwiKindSkSkid.getSkid( moUgwi );
        // prepare (resolve) popup mnemo config for #moSkid master object
        ISimpleResolverFactoriesRegistry resRegistry = tsContext().get( ISimpleResolverFactoriesRegistry.class );
        MnemoMasterObjectManager mmoManager = new MnemoMasterObjectManager( skConn(), resRegistry );
        Ugwi ugwi = UgwiKindSkSkid.makeUgwi( moSkid.classId(), moSkid.strid() );
        IVedScreenCfg scrCfg = VedScreenCfg.KEEPER.str2ent( mnemoCfg.cfgData() );
        IVedScreenCfg newCfg = mmoManager.processMasterObject( ugwi, scrCfg, skConn() );
        mnemoCfg.setCfgData( VedScreenCfg.KEEPER.ent2str( newCfg ) );
      }
    }
    return mnemoCfg;
  }

  /**
   * Create popup window with mnemo inside
   */
  @SuppressWarnings( "unused" )
  private void openPopupMnemo() {
    PopupMnemoDialogPanel.showPopMnemo( tsContext().eclipseContext(),
        VedScreenCfg.cfgFromString( getMnemoConfig().cfgData() ) );
  }

  private static TsPoint computeMnemoSize( ISkMnemoCfg aMnemoCfg ) {
    IVedScreenCfg vedCfg = VedScreenCfg.KEEPER.str2ent( aMnemoCfg.cfgData() );
    IVedCanvasCfg canvasCfg = vedCfg.canvasCfg();
    return new TsPoint( (int)(canvasCfg.size().x()), (int)(canvasCfg.size().y()) );
  }

  /**
   * Open mnemo in new Shell
   */
  private void openPopupMnemoShell( ITsPoint aCoors ) {
    ISkMnemoCfg mnemoCfg = getMnemoConfig();
    Control scrCtrl = vedScreen().view().getControl();
    Shell wnd = new Shell( scrCtrl.getShell(), SWT.BORDER | SWT.CLOSE );
    // String caption = props().getStr( FID_CAPTION );
    String caption = getCaption();
    wnd.setText( caption );
    FillLayout layout = new FillLayout();
    wnd.setLayout( layout );
    Composite bkPanel = new Composite( wnd, SWT.NONE );
    bkPanel.setLayout( layout );
    IRuntimeMnemoPanel panel = new RuntimeMnemoPanel( bkPanel, new TsGuiContext( tsContext() ) );

    panel.setMnemoConfig( VedScreenCfg.cfgFromString( mnemoCfg.cfgData() ) );
    panel.resume();
    TsPoint p = computeMnemoSize( mnemoCfg );
    wnd.setSize( p.x(), p.y() );
    Rectangle clientRect = wnd.getClientArea();

    int dw = p.x() - clientRect.width;
    int dh = p.y() - clientRect.height;
    wnd.setSize( p.x() + dw, p.y() + dh );

    // Point ctrlP = scrCtrl.toControl( aCoors.x(), aCoors.y() );
    Point ctrlP = scrCtrl.toDisplay( aCoors.x(), aCoors.y() );
    // ctrlP = getShell().toDisplay( ctrlP );

    wnd.setLocation( ctrlP.x, ctrlP.y );
    wnd.open();
  }

  // /**
  // * Test if user click on proper mouse button
  // *
  // * @param aActionButton - designed mouse button
  // * @param aMouseButton - real user selected mouse button
  // * @param aState int - SWT код состояния управляющих клавиш Shift, Alt, Ctrl
  // * @return true if click on proper mouse button
  // */
  // @Override
  // protected boolean isMyMouseButton( ERtActionMouseButton aActionButton, ETsMouseButton aMouseButton, int aState ) {
  // boolean retVal = false;
  // int keyMask = props().getInt( FID_KEY_MASK );
  // switch( aActionButton ) {
  // case LEFT:
  // if( aMouseButton.equals( ETsMouseButton.LEFT ) && (keyMask & aState) == keyMask ) {
  // retVal = true;
  // }
  // break;
  // case MIDDLE:
  // if( aMouseButton.equals( ETsMouseButton.MIDDLE ) && (keyMask & aState) == keyMask ) {
  // retVal = true;
  // }
  // break;
  // case RIGHT:
  // if( aMouseButton.equals( ETsMouseButton.RIGHT ) && (keyMask & aState) == keyMask ) {
  // retVal = true;
  // }
  // break;
  // // $CASES-OMITTED$
  // default:
  // break;
  //
  // }
  // return retVal;
  // }

  private String getCaption() {
    String str = props().getStr( TFI_CAPTION.id() );
    if( !str.isBlank() ) {
      return str;
    }

    Ugwi moUgwi = null;
    if( props().hasKey( TFI_SKID_UGWI.id() ) ) {
      IAtomicValue av = props().getValue( TFI_SKID_UGWI.id() );
      if( av.isAssigned() ) {
        moUgwi = av.asValobj();
      }
    }
    if( moUgwi != null && moUgwi != Ugwi.NONE ) {
      Skid moSkid = UgwiKindSkSkid.getSkid( moUgwi );
      ISkObject skObj = skVedEnv().skConn().coreApi().objService().find( moSkid );
      if( skObj != null ) {
        return skObj.attrs().getValue( TSID_NAME ).asString();
      }
    }
    return TsLibUtils.EMPTY_STRING;
  }

}
