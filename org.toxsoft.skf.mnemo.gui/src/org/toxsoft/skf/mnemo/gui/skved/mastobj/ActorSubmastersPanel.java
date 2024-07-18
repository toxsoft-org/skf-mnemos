package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.ugwis.*;

/**
 * Панель содержащая информацию о значениях свойств актора в зависимости от "мастер-объекта".
 * <p>
 *
 * @author vs
 */
public class ActorSubmastersPanel
    extends TsPanel
    implements ITsActionHandler {

  static class ViewerRow {

    private final String    propertyId;
    ICompoundResolverConfig cfg;

    ViewerRow( String aPropertyId ) {
      propertyId = aPropertyId;
    }

    ViewerRow( String aPropertyId, ICompoundResolverConfig aCfg ) {
      propertyId = aPropertyId;
      cfg = aCfg;
    }
  }

  TsToolbar toolBar;

  TableViewer viewer;

  private IVedActor actor;

  private final ISkUgwiService ugwiService;

  private final IVedScreen vedScreen;

  SubMastersCombo smCombo;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - рродительская панель
   * @param aVedScreen {@link IVedScreen} - экран редактора
   * @param aStyle int - SWT стиль панели
   */
  public ActorSubmastersPanel( Composite aParent, IVedScreen aVedScreen, int aStyle ) {
    super( aParent, aVedScreen.tsContext(), aStyle );
    vedScreen = aVedScreen;
    setLayout( new BorderLayout() );

    Composite comboComp = new Composite( this, SWT.NONE );
    comboComp.setLayoutData( BorderLayout.NORTH );
    comboComp.setLayout( new GridLayout( 2, false ) );

    CLabel l = new CLabel( comboComp, SWT.CENTER );
    l.setText( "Sub-мастер: " );
    smCombo = new SubMastersCombo( comboComp, vedScreen );
    smCombo.getControl().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    smCombo.getControl().addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        SubmasterConfig smCfg = smCombo.selectedConfig();
        if( smCfg != null ) {

        }
      }
    } );

    Composite listComp = new Composite( this, SWT.NONE );
    listComp.setLayoutData( BorderLayout.CENTER );
    listComp.setLayout( new BorderLayout() );

    toolBar = TsToolbar.create( listComp, tsContext(), EIconSize.IS_24X24, ACDEF_EDIT, ACDEF_REMOVE );
    toolBar.setNameLabelText( "Данные: " );
    toolBar.getControl().setLayoutData( BorderLayout.NORTH );
    toolBar.getAction( ACTID_EDIT ).setEnabled( false );
    toolBar.getAction( ACTID_REMOVE ).setEnabled( false );
    toolBar.addListener( this );

    int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION;
    viewer = new TableViewer( listComp, style );
    viewer.getTable().setHeaderVisible( true );
    viewer.getTable().setLinesVisible( true );

    TableViewerColumn nameColumn = new TableViewerColumn( viewer, SWT.NONE );
    nameColumn.getColumn().setText( "Название свойства" );
    nameColumn.getColumn().setWidth( 200 );
    nameColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ViewerRow row = (ViewerRow)aCell.getElement();
        IDataDef dd = actor.props().propDefs().getByKey( row.propertyId );
        aCell.setText( dd.nmName() );
      }
    } );

    TableViewerColumn pathColumn = new TableViewerColumn( viewer, SWT.NONE );
    pathColumn.getColumn().setText( "Путь к мастеру" );
    pathColumn.getColumn().setWidth( 400 );
    pathColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ViewerRow row = (ViewerRow)aCell.getElement();
        if( row.cfg == null ) {
          aCell.setText( "none" );
        }
        else {
          aCell.setText( row.cfg.toString() );
        }
        // IDataDef dd = actor.props().propDefs().getByKey( row.propertyId );
        // aCell.setText( dd.nmName() );
      }
    } );

    viewer.getControl().setLayoutData( BorderLayout.CENTER );
    viewer.setContentProvider( new ArrayContentProvider() );

    viewer.addSelectionChangedListener( aEvent -> {
      IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
      if( selection.isEmpty() ) {
        toolBar.getAction( ACTID_EDIT ).setEnabled( false );
        toolBar.getAction( ACTID_REMOVE ).setEnabled( false );
      }
      else {
        ViewerRow row = (ViewerRow)selection.getFirstElement();
        toolBar.getAction( ACTID_EDIT ).setEnabled( true );
        toolBar.getAction( ACTID_REMOVE ).setEnabled( row.cfg != null );
      }
    } );

    ISkCoreApi coreApi = SkGuiUtils.getCoreApi( tsContext() );
    ugwiService = coreApi.ugwiService();
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_EDIT: {
        IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
        ViewerRow row = (ViewerRow)selection.getFirstElement();
        ICompoundResolverConfig cfg = PanelActorPropertyResolverConfig.edit( row.cfg, vedScreen );
        if( cfg != null ) {
          row.cfg = cfg;
          viewer.update( row, null );
          MnemoResolverConfig resCfg = getResolverConfig();
          if( resCfg != null ) {
            resCfg.defineActorSubmaster( actor.id(), VED_SCREEN_MAIN_MNEMO_RESOLVER_ID );
            IStringMap<ICompoundResolverConfig> actorResolvers;
            String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
            if( actor.extraData().hasSection( sectionId ) ) {
              actorResolvers = actor.extraData().readStridMap( sectionId, CompoundResolverConfig.KEEPER );
            }
            else {
              actorResolvers = new StringMap<>();
            }
            IStringMapEdit<ICompoundResolverConfig> result = new StringMap<>( actorResolvers );
            result.put( row.propertyId, cfg );
            actor.extraData().writeStridMap( sectionId, result, CompoundResolverConfig.KEEPER );
          }
          System.out.println( cfg.toString() );
        }
      }
        break;
      case ACTID_REMOVE: {
        IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
        ViewerRow row = (ViewerRow)selection.getFirstElement();
        row.cfg = null;
        viewer.update( row, null );
        // submasters.remove( clsInfo );
        // viewer.viewer().setInput( submasters.toArray() );
      }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException( "%s", aActionId ); //$NON-NLS-1$
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void setActor( IVedActor aActor ) {
    actor = aActor;
    if( actor == null ) {
      viewer.setInput( null );
    }
    else {
      IStringMap<ICompoundResolverConfig> actorResolvers = null;
      String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
      if( actor.extraData().hasSection( sectionId ) ) {
        actorResolvers = actor.extraData().readStridMap( sectionId, CompoundResolverConfig.KEEPER );
      }

      IStridablesList<IDataDef> propDefs = actor.props().propDefs();
      IListEdit<ViewerRow> rows = new ElemArrayList<>();
      for( IDataDef dd : propDefs ) {
        if( dd.keeperId() != null && dd.keeperId().equals( Ugwi.KEEPER_ID ) ) { // свойство является Ugwi
          if( actorResolvers != null && actorResolvers.hasKey( dd.id() ) ) {
            ICompoundResolverConfig resCfg = actorResolvers.getByKey( dd.id() );
            rows.add( new ViewerRow( dd.id(), resCfg ) );
          }
          else {
            rows.add( new ViewerRow( dd.id() ) );
          }
        }
      }
      viewer.setInput( rows.toArray() );
      smCombo.refresh();
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MnemoResolverConfig getResolverConfig() {
    String sectionId = VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF;
    if( vedScreen.model().extraData().hasSection( sectionId ) ) {
      IEntityKeeper<IMnemoResolverConfig> keeper = MnemoResolverConfig.KEEPER;
      return (MnemoResolverConfig)vedScreen.model().extraData().readItem( sectionId, keeper, null );
    }
    return null;
  }

}
