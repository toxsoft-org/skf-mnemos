package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * Панель создания/редактирования конфигурации составного "разрешителя" для объекта являющимся подмастером для
 * мнемосхемы.
 * <p>
 * Например, если у компррессора есть два масляных контура, то "Масляный контур 1" и "Масляный контур 2", могут являться
 * подмастерами мастер-объекта компрессор и "разрешители" свойств соотвествующих акторов, могут в качестве входного
 * мастер-объекта использовать их вместо компрессора, что делает путь для "разрешителя" короче.
 *
 * @author vs
 */
public class PanelMnemoSubmasterResolverConfig
    extends AbstractTsDialogPanel<SubmasterConfig, IVedScreen> {

  private static final IStridGenerator idGen = new SimpleStridGenerator( "submaster", System.currentTimeMillis(), 0 ); //$NON-NLS-1$

  protected PanelMnemoSubmasterResolverConfig( Composite aParent, TsDialog<SubmasterConfig, IVedScreen> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected PanelMnemoSubmasterResolverConfig( Composite aParent, SubmasterConfig aData, IVedScreen aVedScreen,
      int aFlags ) {
    super( aParent, aVedScreen.tsContext(), aData, aVedScreen, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( SubmasterConfig aData ) {
    if( aData != null ) {
      fldName.setText( aData.nmName() );
    }
    else {
    }
  }

  @Override
  protected SubmasterConfig doGetDataRecord() {
    ICompoundResolverConfig cfg = viewer.resolverConfig();
    IOptionSetEdit opSet = new OptionSet();
    opSet.setStr( TSID_NAME, fldName.getText() );
    return SubmasterConfig.create( idGen.nextId(), opSet, cfg );
  }

  @Override
  protected ValidationResult doValidate() {
    IMasterPathNode node = viewer.selectedNode();
    if( fldName.getText().isBlank() ) {
      return ValidationResult.create( EValidationResultType.ERROR, "Задайте имя \"подмастера\"" );
    }
    if( node == null || !node.isObject() || node.parent() == null ) {
      return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать узел объекта" );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MasterPathViewer viewer;

  Text fldName;

  void init() {
    setLayout( new BorderLayout() );

    Composite topComp = new Composite( this, SWT.NONE );
    topComp.setLayout( new GridLayout( 2, false ) );
    topComp.setLayoutData( BorderLayout.NORTH );
    CLabel l = new CLabel( topComp, SWT.CENTER );
    l.setText( "Название: " );
    fldName = new Text( topComp, SWT.BORDER );
    fldName.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    // SubMastersCombo smCombo = new SubMastersCombo( topComp, environ() );
    // smCombo.getControl().addSelectionListener( new SelectionAdapter() {
    //
    // @Override
    // public void widgetSelected( SelectionEvent aEvent ) {
    // SubmasterConfig smCfg = smCombo.selectedConfig();
    // if( smCfg != null ) {
    //
    // }
    // }
    // } );

    // String moClsId = "SkObject"; //$NON-NLS-1$
    // if( dataRecordInput() != null ) {
    // SubmasterConfig smCfg = dataRecordInput();
    // SimpleResolverCfg cfg = smCfg.resolverCfg().cfgs().first();
    // if( DirectGwidResolver.hasGwid( cfg ) ) {
    // Gwid gwid = DirectGwidResolver.gwid( cfg );
    // moClsId = gwid.classId();
    // }
    // }
    // else {
    // String sectionId = VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF;
    // if( environ().model().extraData().hasSection( sectionId ) ) {
    // IMnemoResolverConfig resCfg;
    // resCfg = environ().model().extraData().readItem( sectionId, MnemoResolverConfig.KEEPER, null );
    // if( resCfg.subMasters().hasKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID ) ) {
    // SubmasterConfig smCfg = resCfg.subMasters().getByKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID );
    // Ugwi ugwi = smCfg.resolverCfg().cfgs().first().params().getValobj( PROPID_UGWI );
    // moClsId = UgwiKindSkClassInfo.getClassId( ugwi );
    // System.out.println( ugwi.toString() );
    // }
    // }
    // }

    MnemoResolverConfig mnemoResolverCfg = MasterObjectUtils.readMnemoResolverConfig( environ() );
    ISkCoreApi coreApi = SkGuiUtils.getCoreApi( environ().tsContext() );
    ISkClassInfo clsInfo = MasterObjectUtils.findMainMasterClassId( mnemoResolverCfg, coreApi );

    viewer = new MasterPathViewer( this, clsInfo.id(), tsContext() );
    viewer.setLayoutData( BorderLayout.CENTER );
    viewer.viewer.addSelectionChangedListener( aEvent -> {
      // IMasterPathNode node = viewer.selectedNode();
      // if( node.isObject() ) {
      // // SubmasterConfig cfg = node.resolverConfig();
      // ISkCoreApi coreApi = SkGuiUtils.getCoreApi( tsContext() );
      // ISkClassInfo clsInfo = coreApi.sysdescr().findClassInfo( node.classId() );
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
   * @param aData SubmasterConfig - параметры выравнивания содержимого ячейки
   * @param aVedScreen IVedScreen - соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final SubmasterConfig edit( SubmasterConfig aData, IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    IDialogPanelCreator<SubmasterConfig, IVedScreen> creator = PanelMnemoSubmasterResolverConfig::new;
    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( aVedScreen.tsContext(), "DLG_T_SELECT_MASTER_PATH", "STR_MSG_SELECT_MASTER_PATH" );
    TsDialog<SubmasterConfig, IVedScreen> d = new TsDialog<>( dlgInfo, aData, aVedScreen, creator );
    return d.execData();
  }

}
