package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.PanelCompoundResolverConfig.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

/**
 * Панель создания/редактирования конфигурации составного "разрешителя" {@link Ugwi}.
 * <p>
 *
 * @author vs
 */
public class PanelCompoundResolverConfig
    extends AbstractTsDialogPanel<ICompoundResolverConfig, PanelCtx> {

  static class PanelCtx {

    final String        masterClassId;
    final ITsGuiContext tsContext;

    PanelCtx( String aMasterClassId, ITsGuiContext aTsContext ) {
      masterClassId = aMasterClassId;
      tsContext = aTsContext;
    }
  }

  protected PanelCompoundResolverConfig( Composite aParent, TsDialog<ICompoundResolverConfig, PanelCtx> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected PanelCompoundResolverConfig( Composite aParent, ICompoundResolverConfig aData, PanelCtx aEnviron,
      int aFlags ) {
    super( aParent, aEnviron.tsContext, aData, aEnviron, aFlags );
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
    return viewer.resolverConfig().left();
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

    // String moClsId = "SkObject"; //$NON-NLS-1$
    String moClsId = environ().masterClassId;
    if( dataRecordInput() != null ) {
      SimpleResolverCfg cfg = dataRecordInput().cfgs().first();
      if( DirectGwidResolver.hasGwid( cfg ) ) {
        Gwid gwid = DirectGwidResolver.gwid( cfg );
        moClsId = gwid.classId();
      }
    }

    viewer = new MasterPathViewer( this, moClsId, environ().tsContext );
    viewer.setLayoutData( BorderLayout.CENTER );
    viewer.viewer.addSelectionChangedListener( aEvent -> {
      fireContentChangeEvent();
    } );
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров составного разрешителя.
   *
   * @param aData ICompoundResolverConfig - параметры составного разрешителя
   * @param aMasterClsId String - ИД класса мастер объекта
   * @param aTsContext {@link ITsGuiContext}- соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final ICompoundResolverConfig edit( ICompoundResolverConfig aData, String aMasterClsId,
      ITsGuiContext aTsContext ) {
    PanelCtx ctx = new PanelCtx( aMasterClsId, aTsContext );
    IDialogPanelCreator<ICompoundResolverConfig, PanelCtx> creator = PanelCompoundResolverConfig::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( ctx.tsContext, "DLG_T_SELECT_MASTER_PATH", "STR_MSG_SELECT_MASTER_PATH" );
    TsDialog<ICompoundResolverConfig, PanelCtx> d = new TsDialog<>( dlgInfo, aData, ctx, creator );
    return d.execData();
  }

}
