package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

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
    ICompoundResolverConfig cfg = viewer.resolverConfig();
    String classId = viewer.selectedNode().classId();

    IStructuredSelection sel = (IStructuredSelection)attrsViewer.viewer().getSelection();
    IDtoAttrInfo attrInfo = (IDtoAttrInfo)sel.getFirstElement();
    SimpleResolverCfg arCfg = DirectAttrResolver.createResolverConfig( classId, attrInfo.id() );

    IListEdit<SimpleResolverCfg> configs = new ElemArrayList<>( cfg.cfgs() );
    configs.add( arCfg );

    return new CompoundResolverConfig( configs );
  }

  @Override
  protected ValidationResult doValidate() {
    IMasterPathNode node = viewer.selectedNode();
    if( node == null || !node.isObject() || node.parent() == null ) {
      return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать узел объекта" );
    }
    if( attrsViewer.viewer().getSelection().isEmpty() ) {
      return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать свойство объекта" );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MasterPathViewer viewer;

  StridableTableViewer attrsViewer;

  void init() {
    // setLayout( new BorderLayout() );
    setLayout( new FillLayout() );
    SashForm sash = new SashForm( this, SWT.VERTICAL );

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
          moClsId = UgwiKindSkClassInfo.getClassId( ugwi );
          System.out.println( ugwi.toString() );
        }
      }
    }

    // viewer = new MasterPathViewer( this, moClsId, tsContext() );
    viewer = new MasterPathViewer( sash, moClsId, tsContext() );
    viewer.setLayoutData( BorderLayout.CENTER );
    viewer.viewer.addSelectionChangedListener( aEvent -> {
      IMasterPathNode node = viewer.selectedNode();
      if( node.isObject() ) {
        // ICompoundResolverConfig cfg = node.resolverConfig();
        ISkCoreApi coreApi = SkGuiUtils.getCoreApi( tsContext() );
        ISkClassInfo clsInfo = coreApi.sysdescr().findClassInfo( node.classId() );
        attrsViewer.viewer().setInput( clsInfo.attrs().list().toArray() );
      }
      else {
        attrsViewer.viewer().setInput( null );
      }
      fireContentChangeEvent();
    } );

    // Composite bottomComp = new Composite( this, SWT.NONE );
    Composite bottomComp = new Composite( sash, SWT.NONE );
    bottomComp.setLayoutData( BorderLayout.NORTH );
    bottomComp.setLayout( new BorderLayout() );
    // Composite selectionComp = new Composite( bottomComp, SWT.NONE );
    // selectionComp.setLayoutData( BorderLayout.NORTH );
    // selectionComp.setLayout( new GridLayout( 2, false ) );
    // CLabel l = new CLabel( selectionComp, SWT.CENTER );
    // l.setText( "Тип свойства sk-объекта" );
    //
    // IM5CollectionPanel<IDtoAttrInfo> attrsPanel = SkGuiUtils.getAttrsListPanel( tsContext() );
    // Control ctrl = attrsPanel.createControl( bottomComp );
    // ctrl.setLayoutData( BorderLayout.CENTER );

    int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL;
    attrsViewer = new StridableTableViewer( bottomComp, style, 80, 200, -1 );

    attrsViewer.viewer().getControl().setLayoutData( BorderLayout.CENTER );
    attrsViewer.viewer().addSelectionChangedListener( aEvent -> fireContentChangeEvent() );

    sash.setWeights( 50, 50 );

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
