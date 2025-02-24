package org.toxsoft.skf.mnemo.gui.skved;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.refbooks.lib.*;
import org.toxsoft.skf.refbooks.lib.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Панель создания и редактирования иформации о значениях, получаемых из справочника.
 * <p>
 *
 * @author vs
 */
public class PanelRebookValuesInfo
    extends AbstractGenericEntityEditPanel<IdChain> {

  /**
   * Singleton of the panel creator {@link IGenericEntityEditPanelCreator} implementation.
   */
  public static final IGenericEntityEditPanelCreator<IdChain> PANEL_CREATOR = PanelRebookValuesInfo::new;

  static class StridableCombo {

    ComboViewer viewer;

    public StridableCombo( Composite aParent, int aStyle ) {
      viewer = new ComboViewer( aParent, aStyle );
      viewer.setContentProvider( new ArrayContentProvider() );
      viewer.setLabelProvider( new LabelProvider() {

        @Override
        public String getText( Object aElement ) {
          IStridable s = (IStridable)aElement;
          return s.id() + " (" + s.nmName() + ")"; //$NON-NLS-1$//$NON-NLS-2$
        }
      } );
    }
  }

  class PropNameEditingSupport
      extends EditingSupport {

    public PropNameEditingSupport( TableViewer aViewer ) {
      super( aViewer );
      // TODO Auto-generated constructor stub
    }

    @Override
    protected CellEditor getCellEditor( Object aElement ) {
      return new TextCellEditor( ((TableViewer)getViewer()).getTable() );
    }

    @Override
    protected boolean canEdit( Object aElement ) {
      return true;
    }

    @Override
    protected Object getValue( Object aElement ) {
      return ((Pair<String, String>)aElement).right();
    }

    @Override
    protected void setValue( Object aElement, Object aValue ) {
      Pair<String, String> p = (Pair<String, String>)aElement;
      Pair<String, String> newP = new Pair<>( p.left(), (String)aValue );
      int idx = rows.remove( p );
      if( idx != -1 ) {
        rows.insert( idx, newP );
      }
      mapViewer.setInput( rows.toArray() );
      fireChangeEvent();
    }

  }

  StridableCombo refbookCombo;
  StridableCombo keyCombo;

  TableViewer mapViewer;

  final ISkCoreApi        coreApi;
  final ISkRefbookService refbookService;
  ISkRefbook              refbook = null;

  IListEdit<Pair<String, String>> rows = new ElemArrayList<>();

  PanelRebookValuesInfo( ITsGuiContext aTsContext, boolean aIsViewer ) {
    super( aTsContext, aIsViewer );
    ISkConnection skConn = tsContext().get( ISkConnection.class );
    coreApi = skConn.coreApi();
    refbookService = skConn.coreApi().getService( ISkRefbookService.SERVICE_ID );
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericEntityEditPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    Composite bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, false ) );
    CLabel l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "Справочник: " );
    refbookCombo = new StridableCombo( bkPanel, SWT.BORDER | SWT.DROP_DOWN );
    refbookCombo.viewer.getCombo().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    refbookCombo.viewer.setInput( refbookService.listRefbooks().toArray() );
    refbookCombo.viewer.addSelectionChangedListener( aEvent -> {
      IStructuredSelection sel = (IStructuredSelection)refbookCombo.viewer.getSelection();
      if( !sel.isEmpty() ) {
        refbook = (ISkRefbook)sel.getFirstElement();
        IDtoRefbookInfo rbInfo = DtoRefbookInfo.of( refbook );

        keyCombo.viewer.setInput( rbInfo.attrInfos().toArray() );
        mapViewer.setInput( null );
      }
      fireChangeEvent();
    } );

    l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "Поле ключа: " );
    keyCombo = new StridableCombo( bkPanel, SWT.BORDER | SWT.DROP_DOWN );
    keyCombo.viewer.getCombo().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    keyCombo.viewer.addSelectionChangedListener( aEvent -> {
      IStructuredSelection sel = (IStructuredSelection)aEvent.getSelection();
      if( !sel.isEmpty() ) {
        IDtoAttrInfo attrInfo = (IDtoAttrInfo)sel.getFirstElement();
        fillMapViewer( attrInfo.id() );
      }
      fireChangeEvent();
    } );

    mapViewer = new TableViewer( bkPanel, SWT.BORDER | SWT.FULL_SELECTION );
    mapViewer.getTable().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
    mapViewer.getTable().setHeaderVisible( true );
    mapViewer.getTable().setLinesVisible( true );

    TableViewerColumn rbfColumn = new TableViewerColumn( mapViewer, SWT.NONE );
    rbfColumn.getColumn().setText( "Поле справочника" );
    rbfColumn.getColumn().setWidth( 300 );
    rbfColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        Pair<String, String> p = (Pair<String, String>)aCell.getElement();
        aCell.setText( p.left() );
      }
    } );

    TableViewerColumn propColumn = new TableViewerColumn( mapViewer, SWT.NONE );
    propColumn.getColumn().setText( "Свойство VISEL" );
    propColumn.getColumn().setWidth( 300 );
    propColumn.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        Pair<String, String> p = (Pair<String, String>)aCell.getElement();
        aCell.setText( p.right() );
      }
    } );
    propColumn.setEditingSupport( new PropNameEditingSupport( mapViewer ) );

    mapViewer.setContentProvider( new ArrayContentProvider() );

    return bkPanel;
  }

  @Override
  protected IdChain doGetEntity() {
    IStringListEdit list = new StringArrayList();
    IStructuredSelection sel = (IStructuredSelection)refbookCombo.viewer.getSelection();
    list.add( ((IStridable)sel.getFirstElement()).id() );
    sel = (IStructuredSelection)keyCombo.viewer.getSelection();
    list.add( ((IStridable)sel.getFirstElement()).id() );
    for( Pair<String, String> p : rows ) {
      if( p.right() != null && !p.right().isBlank() ) {
        list.add( p.left() );
        list.add( p.right() );
      }
    }

    return new IdChain( list );
  }

  @Override
  protected void doProcessSetEntity() {
    IdChain chain = specifiedEntity();
    if( chain != null ) {
      String rbId = chain.get( 0 );
      refbook = refbookService.listRefbooks().getByKey( rbId );
      IDtoRefbookInfo rbInfo = DtoRefbookInfo.of( refbook );
      refbookCombo.viewer.setSelection( new StructuredSelection( refbook ) );
      String fieldId = chain.get( 1 );
      keyCombo.viewer.setSelection( new StructuredSelection( rbInfo.attrInfos().getByKey( fieldId ) ) );
      fillMapViewer( fieldId );

      IStringList branches = chain.branches();
      if( branches.size() > 2 && branches.size() % 2 == 0 ) {
        for( int i = 2; i < branches.size() - 1; i += 2 ) {
          Pair<String, String> p = new Pair<>( branches.get( i ), branches.get( i + 1 ) );
          int idx = rows.remove( new Pair<>( branches.get( i ), TsLibUtils.EMPTY_STRING ) );
          rows.insert( idx, p );
        }
      }

      mapViewer.setInput( rows.toArray() );
    }
  }

  @Override
  protected ValidationResult doCanGetEntity() {
    IStructuredSelection sel = (IStructuredSelection)refbookCombo.viewer.getSelection();
    if( sel.isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать справочник" );
    }
    sel = (IStructuredSelection)keyCombo.viewer.getSelection();
    if( sel.isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать поле ключа" );
    }
    if( isPropsMapEmpty() ) {
      return ValidationResult.error( "Необходимо указать хотя бы одно свойство" );
    }
    // sel = (IStructuredSelection)valueCombo.viewer.getSelection();
    // if( sel.isEmpty() ) {
    // return ValidationResult.error( "Необходимо выбрать поле значеня" );
    // }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void fillMapViewer( String aKeyFieldId ) {
    rows.clear();
    IDtoRefbookInfo rbInfo = DtoRefbookInfo.of( refbook );
    for( IDtoAttrInfo ai : rbInfo.attrInfos() ) {
      if( !ai.id().equals( aKeyFieldId ) ) {
        Pair<String, String> p = new Pair<>( ai.id(), TsLibUtils.EMPTY_STRING );
        rows.add( p );
      }
    }
    mapViewer.setInput( rows.toArray() );
  }

  boolean isPropsMapEmpty() {
    for( Pair<String, String> p : rows ) {
      if( p.right() != null && !p.right().isEmpty() ) {
        return false;
      }
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Static API
  //

  /**
   * Invokes {@link ITsCombiCondInfo} edit dialog.
   *
   * @param aInitVal {@link ITsCombiCondInfo} - initial value to display or <code>null</code>
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return {@link ITsCombiCondInfo} - edited CCP or <code>null</code> if user cancels editing
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IdChain edit( IdChain aInitVal, ITsGuiContext aTsContext ) {
    ITsDialogInfo dialogInfo = new TsDialogInfo( aTsContext, "Справочник", "Выберите справочник и его поля " );
    IDialogPanelCreator<IdChain, ITsGuiContext> creator =
        ( p, d ) -> new TsDialogGenericEntityEditPanel<>( p, d, PANEL_CREATOR );
    TsDialog<IdChain, ITsGuiContext> d = new TsDialog<>( dialogInfo, aInitVal, aTsContext, creator );
    return d.execData();
  }

}
