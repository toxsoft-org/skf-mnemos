package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import java.awt.*;

import org.eclipse.jface.viewers.*;
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
    extends AbstractTsDialogPanel<ICompoundResolverConfig, PanelContext> {

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

    IStructuredSelection sel = (IStructuredSelection)propsViewer.viewer().getSelection();
    SimpleResolverCfg propCfg = null;
    if( environ().ugwiKingId.equals( UgwiKindSkAttr.KIND_ID ) ) {
      IDtoAttrInfo attrInfo = (IDtoAttrInfo)sel.getFirstElement();
      propCfg = DirectAttrResolver.createResolverConfig( classId, attrInfo.id() );
    }
    if( environ().ugwiKingId.equals( UgwiKindSkRtdata.KIND_ID ) ) {
      IDtoRtdataInfo rtDataInfo = (IDtoRtdataInfo)sel.getFirstElement();
      propCfg = DirectRtDataResolver.createResolverConfig( classId, rtDataInfo.id() );
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
    if( propsViewer.viewer().getSelection().isEmpty() ) {
      return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать свойство объекта" );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MasterPathViewer viewer;

  StridableTableViewer propsViewer;

  void init() {
    // setLayout( new BorderLayout() );
    this.setData( AWTLayout.KEY_PREFERRED_SIZE, new Dimension( 800, 600 ) );
    setLayout( new FillLayout() );
    SashForm sash = new SashForm( this, SWT.VERTICAL );

    // viewer = new MasterPathViewer( this, moClsId, tsContext() );
    viewer = new MasterPathViewer( sash, environ().submasterClassId, tsContext() );
    viewer.setLayoutData( BorderLayout.CENTER );
    viewer.viewer.addSelectionChangedListener( aEvent -> {
      IMasterPathNode node = viewer.selectedNode();
      if( node.isObject() ) {
        // ICompoundResolverConfig cfg = node.resolverConfig();
        ISkCoreApi coreApi = SkGuiUtils.getCoreApi( tsContext() );
        ISkClassInfo clsInfo = coreApi.sysdescr().findClassInfo( node.classId() );
        if( environ().ugwiKingId.equals( UgwiKindSkAttr.KIND_ID ) ) {
          propsViewer.viewer().setInput( clsInfo.attrs().list().toArray() );
        }
        if( environ().ugwiKingId.equals( UgwiKindSkRtdata.KIND_ID ) ) {
          propsViewer.viewer().setInput( clsInfo.rtdata().list().toArray() );
        }
      }
      else {
        propsViewer.viewer().setInput( null );
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
    propsViewer = new StridableTableViewer( bottomComp, style, 80, 200, -1 );
    propsViewer.viewer().getControl().setLayoutData( BorderLayout.CENTER );
    propsViewer.viewer().addSelectionChangedListener( aEvent -> fireContentChangeEvent() );

    sash.setWeights( 60, 40 );

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
