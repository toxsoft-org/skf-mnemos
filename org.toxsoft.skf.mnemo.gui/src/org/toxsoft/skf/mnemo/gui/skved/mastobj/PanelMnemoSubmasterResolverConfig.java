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
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.utils.*;

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
    extends AbstractTsDialogPanel<Pair<SubmasterConfig, String>, IVedScreen>
    implements ISkGuiContextable {

  private static final IStridGenerator idGen = new SimpleStridGenerator( "submaster", System.currentTimeMillis(), 0 ); //$NON-NLS-1$

  protected PanelMnemoSubmasterResolverConfig( Composite aParent,
      TsDialog<Pair<SubmasterConfig, String>, IVedScreen> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected PanelMnemoSubmasterResolverConfig( Composite aParent, Pair<SubmasterConfig, String> aData,
      IVedScreen aVedScreen, int aFlags ) {
    super( aParent, aVedScreen.tsContext(), aData, aVedScreen, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( Pair<SubmasterConfig, String> aData ) {
    if( aData != null ) {
      throw new TsIllegalStateRtException( "This method should not be invoked for non null data" ); //$NON-NLS-1$
    }
  }

  @Override
  protected Pair<SubmasterConfig, String> doGetDataRecord() {
    Pair<ICompoundResolverConfig, String> cfg = viewer.resolverConfig();
    IOptionSetEdit opSet = new OptionSet();
    opSet.setStr( TSID_NAME, fldName.getText() );
    opSet.setStr( MasterObjectUtils.PROPID_RESOLVER_OUTPUT_CLASS_ID, cfg.right() );
    return new Pair<>( SubmasterConfig.create( idGen.nextId(), opSet, cfg.left() ), cfg.right() );
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

    MnemoResolverConfig mnemoResolverCfg = MasterObjectUtils.readMnemoResolverConfig( environ() );
    ISkClassInfo clsInfo = MasterObjectUtils.findMainMasterClassInfo( mnemoResolverCfg, coreApi() );

    viewer = new MasterPathViewer( this, clsInfo.id(), tsContext() );
    viewer.setLayoutData( BorderLayout.CENTER );
    viewer.viewer.addSelectionChangedListener( aEvent -> {
      fireContentChangeEvent();
    } );
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров выравнивания содержимого ячейки.
   *
   * @param aData Pair<SubmasterConfig,String> - параметры выравнивания содержимого ячейки
   * @param aVedScreen IVedScreen - соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final Pair<SubmasterConfig, String> edit( Pair<SubmasterConfig, String> aData, IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    IDialogPanelCreator<Pair<SubmasterConfig, String>, IVedScreen> creator = PanelMnemoSubmasterResolverConfig::new;
    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( aVedScreen.tsContext(), "DLG_T_SELECT_MASTER_PATH", "STR_MSG_SELECT_MASTER_PATH" );
    TsDialog<Pair<SubmasterConfig, String>, IVedScreen> d = new TsDialog<>( dlgInfo, aData, aVedScreen, creator );
    return d.execData();
  }

  // ------------------------------------------------------------------------------------
  // ISkGuiContextable
  //

  @Override
  public ISkConnection skConn() {
    ISkVedEnvironment vedEnv = environ().tsContext().get( ISkVedEnvironment.class );
    return vedEnv.skConn();
  }

}
