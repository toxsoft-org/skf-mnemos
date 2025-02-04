package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.PanelActorPropertyResolverConfig.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

/**
 * Панель редактирования свйств пользовательского действия типа - UGWI, которые требуют разрешения на этапе исполнения.
 *
 * @author vs
 */
public class PanelUserActionCfgUgwies
    extends TsPanel
    implements ITsActionHandler {

  static class ViewerRow {

    private final String    ugwiKindId;
    private final String    propertyId;
    ICompoundResolverConfig cfg;

    ViewerRow( String aPropertyId, String aUgwiKindId ) {
      propertyId = aPropertyId;
      ugwiKindId = aUgwiKindId;
    }

    ViewerRow( String aPropertyId, String aUgwiKindId, ICompoundResolverConfig aCfg ) {
      propertyId = aPropertyId;
      ugwiKindId = aUgwiKindId;
      cfg = aCfg;
    }
  }

  TsToolbar toolBar;

  TableViewer viewer;

  private IMnemoUserAction action;

  private IOptionSetEdit resolversInfo;

  private IVedScreen vedScreen;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - рродительская панель
   * @param aVedScreen {@link IVedScreen} - экран редактора
   * @param aStyle int - SWT стиль панели
   */
  public PanelUserActionCfgUgwies( Composite aParent, IVedScreen aVedScreen, int aStyle ) {
    super( aParent, aVedScreen.tsContext(), aStyle );
    vedScreen = aVedScreen;
    setLayout( new BorderLayout() );

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
    nameColumn.getColumn().setText( "Тип свойства" );
    nameColumn.getColumn().setWidth( 150 );
    nameColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ViewerRow row = (ViewerRow)aCell.getElement();
        // IDataDef dd = actor.props().propDefs().getByKey( row.propertyId );
        IDataType dt = action.tinFields().getByKey( row.propertyId ).typeInfo().dataType();
        aCell.setText( dt.params().getStr( TSID_NAME ) );
      }
    } );

    TableViewerColumn dataColumn = new TableViewerColumn( viewer, SWT.NONE );
    dataColumn.getColumn().setText( "Значение свойства" );
    dataColumn.getColumn().setWidth( 230 );
    dataColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        ViewerRow row = (ViewerRow)aCell.getElement();
        if( row.cfg != null ) {
          String classId = MasterObjectUtils.resolverTargetClassId( row.cfg );
          String paramId = MasterObjectUtils.resolverTargetParamId( row.cfg );
          aCell.setText( classId + '[' + paramId + ']' );
        }
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
        String smClassId = MasterObjectUtils.findMainMasterClassId( getResolverConfig() );
        ICompoundResolverConfig cfg = null;
        PanelContext pCtx = new PanelContext( row.ugwiKindId, smClassId, vedScreen );
        if( row.ugwiKindId.equals( UgwiKindSkSkid.KIND_ID ) ) {
          cfg = PanelCompoundResolverConfig.edit( null, smClassId, vedScreen.tsContext() );
        }
        else {
          cfg = PanelActorPropertyResolverConfig.edit( row.cfg, pCtx );
        }
        if( cfg != null ) {
          row.cfg = cfg;
          viewer.update( row, null );
          // MnemoResolverConfig resCfg = getResolverConfig();
          // if( resCfg != null ) {
          // resCfg.defineActorSubmaster( actor.id(), VED_SCREEN_MAIN_MNEMO_RESOLVER_ID );
          IStringMap<ICompoundResolverConfig> resolvers;
          String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
          if( resolversInfo.hasKey( sectionId ) ) {
            resolvers = CompoundResolverConfig.KEEPER.str2strmap( resolversInfo.getStr( sectionId ) );
          }
          else {
            resolvers = new StringMap<>();
          }
          IStringMapEdit<ICompoundResolverConfig> result = new StringMap<>( resolvers );
          result.put( row.propertyId, cfg );
          resolversInfo.setStr( sectionId, CompoundResolverConfig.KEEPER.strmap2str( result, true ) );
          // actor.extraData().writeStridMap( sectionId, result, );
          // }
          System.out.println( cfg.toString() );
        }
      }
        break;
      case ACTID_REMOVE: {
        IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
        ViewerRow row = (ViewerRow)selection.getFirstElement();
        row.cfg = null;
        viewer.update( row, null );
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
   * Задает данные пользовательского действия.
   *
   * @param aAction {@link IMnemoUserAction} - пользовательское действие
   * @param aCfg {@link VedUserActionCfg} - конфигурация пользовательского действия
   */
  public void setUserAction( IMnemoUserAction aAction, VedUserActionCfg aCfg ) {
    action = aAction;
    if( aAction == null ) {
      viewer.setInput( null );
    }
    else {
      resolversInfo = new OptionSet();
      if( aCfg != null ) {
        resolversInfo.setAll( aCfg.params() );
      }
      IStringMap<ICompoundResolverConfig> actorResolvers = null;
      String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
      if( resolversInfo.hasKey( sectionId ) ) {
        actorResolvers = CompoundResolverConfig.KEEPER.str2strmap( resolversInfo.getStr( sectionId ) );
      }

      IListEdit<ViewerRow> rows = new ElemArrayList<>();
      for( ITinFieldInfo fi : aAction.tinFields() ) {
        IDataType dt = fi.typeInfo().dataType();
        if( dt.keeperId() != null && dt.keeperId().equals( Ugwi.KEEPER_ID ) ) {
          if( dt.params().hasKey( PROPID_UGWI_KIND ) ) {
            String ugwiKindId = dt.params().getStr( PROPID_UGWI_KIND );
            if( actorResolvers != null && actorResolvers.hasKey( fi.id() ) ) {
              ICompoundResolverConfig resCfg = actorResolvers.getByKey( fi.id() );
              rows.add( new ViewerRow( fi.id(), ugwiKindId, resCfg ) );
            }
            else {
              rows.add( new ViewerRow( fi.id(), ugwiKindId ) );
            }
          }
        }
      }

      viewer.setInput( rows.toArray() );
    }
  }

  IOptionSet getResolversInfo() {
    return resolversInfo;
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
