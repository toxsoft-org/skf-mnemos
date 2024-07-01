package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

/**
 * Панель создания/редактирования конфигурации составного "разрешителя" {@link Ugwi}.
 * <p>
 *
 * @author vs
 */
public class PanelActorPropertyResolverConfig
    extends AbstractTsDialogPanel<ICompoundResolverConfig, IVedScreen> {

  protected PanelActorPropertyResolverConfig( Composite aParent,
      TsDialog<ICompoundResolverConfig, IVedScreen> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected PanelActorPropertyResolverConfig( Composite aParent, ICompoundResolverConfig aData, IVedScreen aVedScreen,
      int aFlags ) {
    super( aParent, aVedScreen.tsContext(), aData, aVedScreen, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ICompoundResolverConfig aData ) {
    if( aData != null ) {
    }
    else {
    }
  }

  @Override
  protected ICompoundResolverConfig doGetDataRecord() {
    return viewer.resolverConfig();
  }

  @Override
  protected ValidationResult doValidate() {
    IMasterPathNode node = viewer.selectedNode();
    if( node != null && node.isObject() && node.parent() != null ) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать узел объекта" );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MasterPathViewer viewer;

  void init() {
    setLayout( new BorderLayout() );

    String moClsId = "SkObject"; //$NON-NLS-1$
    if( dataRecordInput() != null ) {
      SimpleResolverCfg cfg = dataRecordInput().cfgs().first();
      if( DirectGwidResolver.hasGwid( cfg ) ) {
        Gwid gwid = DirectGwidResolver.gwid( cfg );
        moClsId = gwid.classId();
      }
    }
    else {
      String sectionId = VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF;
      if( environ().model().extraData().hasSection( sectionId ) ) {
        IMnemoResolverConfig resCfg;
        resCfg = environ().model().extraData().readItem( sectionId, MnemoResolverConfig.KEEPER, null );
        if( resCfg.subMasters().hasKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID ) ) {
          SubmasterConfig smCfg = resCfg.subMasters().getByKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID );
          Ugwi ugwi = smCfg.resolverCfg().cfgs().first().params().getValobj( PROPID_UGWI );
          System.out.println( ugwi.toString() );
        }
      }
    }

    viewer = new MasterPathViewer( this, moClsId, tsContext() );
    viewer.setLayoutData( BorderLayout.CENTER );
    viewer.viewer.addSelectionChangedListener( aEvent -> {
      IMasterPathNode node = viewer.selectedNode();
      // if( node.isObject() ) {
      // // ICompoundResolverConfig cfg = node.resolverConfig();
      // }
      fireContentChangeEvent();
    } );
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров выравнивания содержимого ячейки.
   *
   * @param aData ICompoundResolverConfig - параметры выравнивания содержимого ячейки
   * @param aVedScreen IVedScreen - соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final ICompoundResolverConfig edit( ICompoundResolverConfig aData, IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    IDialogPanelCreator<ICompoundResolverConfig, IVedScreen> creator = PanelActorPropertyResolverConfig::new;
    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( aVedScreen.tsContext(), "DLG_T_SELECT_MASTER_PATH", "STR_MSG_SELECT_MASTER_PATH" );
    TsDialog<ICompoundResolverConfig, IVedScreen> d = new TsDialog<>( dlgInfo, aData, aVedScreen, creator );
    return d.execData();
  }

}
