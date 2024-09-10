package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import java.awt.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.PanelActorPropertyResolverConfig.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.utils.*;

/**
 * Панель создания/редактирования конфигурации составного "разрешителя" {@link Ugwi}.
 * <p>
 *
 * @author vs
 */
public class PanelActorPropertyResolverConfig
    extends AbstractTsDialogPanel<ICompoundResolverConfig, PanelContext>
    implements ISkGuiContextable {

  public static class PanelContext {

    private final String     submasterClassId;
    private final IVedScreen vedScreen;
    private final String     ugwiKingId;

    public PanelContext( String aUgwiKingId, String aSubmasterClassId, IVedScreen aVedScreen ) {
      ugwiKingId = aUgwiKingId;
      submasterClassId = aSubmasterClassId;
      vedScreen = aVedScreen;
    }

    // public String submasterClassId() {
    // return submasterClassId;
    // }
    //
    // public IVedScreen vedScreen() {
    // return vedScreen;
    // }
    //
    // public String ugwiKindId() {
    // return ugwiKingId;
    // }
  }

  protected PanelActorPropertyResolverConfig( Composite aParent,
      TsDialog<ICompoundResolverConfig, PanelContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected PanelActorPropertyResolverConfig( Composite aParent, ICompoundResolverConfig aData, PanelContext aCtx,
      int aFlags ) {
    super( aParent, aCtx.vedScreen.tsContext(), aData, aCtx, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ICompoundResolverConfig aData ) {
    if( aData != null ) {
      viewer.setResolverConfig( aData );
    }
  }

  @Override
  protected ICompoundResolverConfig doGetDataRecord() {
    Pair<ICompoundResolverConfig, String> cfg = viewer.resolverConfig();
    String classId = viewer.selectedNode().classId();

    Ugwi ugwi = propSelector.selectedUgwi();
    SimpleResolverCfg propCfg = null;
    if( environ().ugwiKingId.equals( UgwiKindSkAttr.KIND_ID ) ) {
      String attrId = UgwiKindSkAttrInfo.getAttrId( ugwi );
      propCfg = DirectAttrResolver.createResolverConfig( classId, attrId );
    }
    if( environ().ugwiKingId.equals( UgwiKindSkRtdata.KIND_ID ) ) {
      String dataId = UgwiKindSkRtDataInfo.getRtDataId( ugwi );
      propCfg = DirectRtDataResolver.createResolverConfig( classId, dataId );
    }
    if( environ().ugwiKingId.equals( UgwiKindSkCmd.KIND_ID ) ) {
      String cmdId = UgwiKindSkCmdInfo.getCmdId( ugwi );
      propCfg = DirectCmdResolver.createResolverConfig( classId, cmdId );
    }
    if( environ().ugwiKingId.equals( UgwiKindRriAttr.KIND_ID ) ) {
      propCfg = DirectRriAttrResolver.createResolverConfig( ugwi );
    }
    IListEdit<SimpleResolverCfg> configs = new ElemArrayList<>( cfg.left().cfgs() );
    configs.add( propCfg );

    return new CompoundResolverConfig( configs );
  }

  @Override
  protected ValidationResult doValidate() {
    IMasterPathNode node = viewer.selectedNode();
    if( node == null || !node.isObject() ) {// || node.parent() == null ) {
      return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать узел объекта" );
    }
    // if( propsViewer.viewer().getSelection().isEmpty() ) {
    if( propSelector.selectedUgwi() == null ) {
      return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать свойство объекта" );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // ISkGuiContextable
  //

  @Override
  public ISkConnection skConn() {
    ISkVedEnvironment vedEnv = environ().vedScreen.tsContext().get( ISkVedEnvironment.class );
    return vedEnv.skConn();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MasterPathViewer viewer;

  StridableTableViewer propsViewer;

  PanelSkObjectPropertySelector propSelector;

  void init() {
    // setLayout( new BorderLayout() );
    this.setData( AWTLayout.KEY_PREFERRED_SIZE, new Dimension( 800, 600 ) );
    setLayout( new FillLayout() );
    SashForm sash = new SashForm( this, SWT.VERTICAL );

    viewer = new MasterPathViewer( sash, environ().submasterClassId, tsContext() );
    viewer.setLayoutData( BorderLayout.CENTER );
    viewer.viewer.addSelectionChangedListener( aEvent -> {
      IMasterPathNode node = viewer.selectedNode();
      if( node.isObject() ) {
        ISkClassInfo clsInfo = skSysdescr().findClassInfo( node.classId() );
        propSelector.setClassInfo( clsInfo );
      }
      else {
        propSelector.clear();
      }
      fireContentChangeEvent();
    } );

    Composite bottomComp = new Composite( sash, SWT.NONE );
    bottomComp.setLayoutData( BorderLayout.NORTH );
    bottomComp.setLayout( new BorderLayout() );

    propSelector = new PanelSkObjectPropertySelector( environ().ugwiKingId, bottomComp, tsContext(), SWT.NONE );
    propSelector.addTsSelectionChangeListener( ( aSource, aSelectedItem ) -> fireContentChangeEvent() );

    // Composite selectionComp = new Composite( bottomComp, SWT.NONE );
    // selectionComp.setLayoutData( BorderLayout.NORTH );
    // selectionComp.setLayout( new GridLayout( 2, false ) );
    // CLabel l = new CLabel( selectionComp, SWT.CENTER );
    // l.setText( "Тип свойства sk-объекта" );
    //
    // IM5CollectionPanel<IDtoAttrInfo> attrsPanel = SkGuiUtils.getAttrsListPanel( tsContext() );
    // Control ctrl = attrsPanel.createControl( bottomComp );
    // ctrl.setLayoutData( BorderLayout.CENTER );

    // int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL;
    // propsViewer = new StridableTableViewer( bottomComp, style, 80, 200, -1 );
    // propsViewer.viewer().getControl().setLayoutData( BorderLayout.CENTER );
    // propsViewer.viewer().addSelectionChangedListener( aEvent -> fireContentChangeEvent() );

    sash.setWeights( 40, 60 );

  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров выравнивания содержимого ячейки.
   *
   * @param aData ICompoundResolverConfig - параметры выравнивания содержимого ячейки
   * @param aCtx PanelContext - контекст панели
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final ICompoundResolverConfig edit( ICompoundResolverConfig aData, PanelContext aCtx ) {
    TsNullArgumentRtException.checkNull( aCtx );
    IDialogPanelCreator<ICompoundResolverConfig, PanelContext> creator = PanelActorPropertyResolverConfig::new;
    ITsGuiContext tsContext = aCtx.vedScreen.tsContext();
    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( tsContext, "DLG_T_SELECT_MASTER_PATH", "STR_MSG_SELECT_MASTER_PATH" );
    TsDialog<ICompoundResolverConfig, PanelContext> d = new TsDialog<>( dlgInfo, aData, aCtx, creator );
    return d.execData();
  }

}
