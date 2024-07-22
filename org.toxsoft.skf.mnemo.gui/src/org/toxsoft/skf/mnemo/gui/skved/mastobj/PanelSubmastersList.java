package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * Панель с редактируемым списком sub-мастеров всей мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class PanelSubmastersList
    extends TsPanel
    implements ITsActionHandler {

  TsToolbar toolBar;

  StridableTableViewer viewer;

  private final IVedScreen vedScreen;

  final IStridablesListEdit<SubmasterConfig> submasterConfigs = new StridablesList<>();

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   */
  public PanelSubmastersList( Composite aParent, IVedScreen aVedScreen ) {
    super( aParent, aVedScreen.tsContext() );
    vedScreen = aVedScreen;
    setLayout( new BorderLayout() );
    toolBar = TsToolbar.create( this, aVedScreen.tsContext(), EIconSize.IS_24X24, ACDEF_ADD, ACDEF_REMOVE );
    toolBar.setNameLabelText( "Sub-мастера: " );
    toolBar.getControl().setLayoutData( BorderLayout.NORTH );

    int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION;
    viewer = new StridableTableViewer( this, style, 0, 200, 0 );
    viewer.viewer().getControl().setLayoutData( BorderLayout.CENTER );
    viewer.viewer().addSelectionChangedListener( aEvent -> {
      IStructuredSelection selection = (IStructuredSelection)aEvent.getSelection();
      toolBar.getAction( ACTID_REMOVE ).setEnabled( !selection.isEmpty() );
    } );
    // toolBar.getAction( ACTID_ADD ).setEnabled( false );
    toolBar.getAction( ACTID_REMOVE ).setEnabled( false );
    toolBar.addListener( this );
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_ADD: {
        IVedScreenCfg screenCfg = VedScreenUtils.getVedScreenConfig( vedScreen );
        MnemoResolverConfig mnemoResolerConfig = MasterObjectUtils.readMnemoResolverConfig( screenCfg );
        if( !mnemoResolerConfig.subMasters().hasKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID ) ) {
          TsDialogUtils.warn( getShell(), "Задайте \"мастер-объект\" мнемосхемы" );
          return;
        }
        ISkCoreApi coreApi = SkGuiUtils.getCoreApi( vedScreen.tsContext() );
        ISkClassInfo clsInfo = MasterObjectUtils.findMainMasterClassId( mnemoResolerConfig, coreApi );
        if( clsInfo == null ) {
          viewer.viewer().setInput( null );
          MasterObjectUtils.updateSubmastersList( IStridablesList.EMPTY, vedScreen );
          return;
        }
        // masterClassId = clsInfo.id();
        Pair<SubmasterConfig, String> cfg = PanelMnemoSubmasterResolverConfig.edit( null, vedScreen );
        if( cfg != null && !submasterConfigs.hasKey( cfg.left().id() ) ) {
          submasterConfigs.add( cfg.left() );
          MasterObjectUtils.updateSubmastersList( submasterConfigs, vedScreen );
          updateSubmustersList();
          // viewer.viewer().setInput( submasterConfigs.toArray() );
        }
      }
        break;
      case ACTID_REMOVE: {
        IStructuredSelection selection = (IStructuredSelection)viewer.viewer().getSelection();
        if( !selection.isEmpty() ) {
          SubmasterConfig smCfg = (SubmasterConfig)selection.getFirstElement();
          submasterConfigs.remove( smCfg );
          MasterObjectUtils.updateSubmastersList( submasterConfigs, vedScreen );
          updateSubmustersList();
          // viewer.viewer().setInput( submasterConfigs.toArray() );
        }
      }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException( "%s", aActionId ); //$NON-NLS-1$
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает конфигурацию "разрешителя" мастер-объекта мнемосхемы.
   *
   * @param aCfg {@link MnemoResolverConfig} - конфигурация "разрешителя" мастер-объекта мнемосхемы
   */
  public void setMnemoResolverConfig( MnemoResolverConfig aCfg ) {
    submasterConfigs.clear();
    submasterConfigs.addAll( aCfg.subMasters() );
    updateSubmustersList();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void updateSubmustersList() {
    IStridablesListEdit<SubmasterConfig> smList = new StridablesList<SubmasterConfig>( submasterConfigs );
    smList.removeByKey( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID ); // Удалим конфигурацию главного мастера
    viewer.viewer().setInput( smList.toArray() );
  }
}
