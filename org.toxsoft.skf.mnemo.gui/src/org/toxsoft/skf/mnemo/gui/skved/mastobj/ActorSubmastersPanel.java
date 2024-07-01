package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
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
  }

  TsToolbar toolBar;

  TableViewer viewer;

  private IVedActor actor;

  private final ISkUgwiService ugwiService;

  private final IVedScreen vedScreen;

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
    toolBar = TsToolbar.create( this, tsContext(), EIconSize.IS_24X24, ACDEF_EDIT, ACDEF_REMOVE );
    toolBar.setNameLabelText( "Данные: " );
    toolBar.getControl().setLayoutData( BorderLayout.NORTH );
    toolBar.getAction( ACTID_EDIT ).setEnabled( false );
    toolBar.getAction( ACTID_REMOVE ).setEnabled( false );
    toolBar.addListener( this );

    int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION;
    viewer = new TableViewer( this, style );
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
      IStridablesList<IDataDef> propDefs = actor.props().propDefs();
      IListEdit<ViewerRow> rows = new ElemArrayList<>();
      for( IDataDef dd : propDefs ) {
        if( dd.keeperId() != null && dd.keeperId().equals( Ugwi.KEEPER_ID ) ) { // свойство является Ugwi
          rows.add( new ViewerRow( dd.id() ) );
        }
      }
      viewer.setInput( rows.toArray() );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

}
