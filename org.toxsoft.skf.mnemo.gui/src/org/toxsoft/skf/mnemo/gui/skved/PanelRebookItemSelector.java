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
import org.toxsoft.uskat.core.connection.*;

/**
 * Панель выбора элемента (строки) справочника справочника.
 * <p>
 *
 * @author vs
 */
public class PanelRebookItemSelector
    extends AbstractGenericEntityEditPanel<IdChain> {

  /**
   * Singleton of the panel creator {@link IGenericEntityEditPanelCreator} implementation.
   */
  public static final IGenericEntityEditPanelCreator<IdChain> PANEL_CREATOR = PanelRebookItemSelector::new;

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
      // mapViewer.setInput( rows.toArray() );
      fireChangeEvent();
    }

  }

  StridableCombo refbookCombo;
  StridableCombo itemCombo;

  // TableViewer mapViewer;

  final ISkCoreApi        coreApi;
  final ISkRefbookService refbookService;
  ISkRefbook              refbook     = null;
  ISkRefbookItem          refbookItem = null;

  IListEdit<Pair<String, String>> rows = new ElemArrayList<>();

  PanelRebookItemSelector( ITsGuiContext aTsContext, boolean aIsViewer ) {
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
        fillItemsList();
      }
      fireChangeEvent();
    } );

    l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "Элемент справочника: " );
    itemCombo = new StridableCombo( bkPanel, SWT.BORDER | SWT.DROP_DOWN );
    itemCombo.viewer.getCombo().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    itemCombo.viewer.addSelectionChangedListener( aEvent -> {
      IStructuredSelection sel = (IStructuredSelection)aEvent.getSelection();
      if( !sel.isEmpty() ) {
        refbookItem = (ISkRefbookItem)sel.getFirstElement();
      }
      fireChangeEvent();
    } );

    return bkPanel;
  }

  @Override
  protected IdChain doGetEntity() {
    IStringListEdit list = new StringArrayList();
    list.add( refbookItem.refbook().id() );
    list.add( refbookItem.id() );
    return new IdChain( list );
  }

  @Override
  protected void doProcessSetEntity() {
    IdChain chain = specifiedEntity();
    if( chain != null && chain.branches().size() > 1 ) {
      String rbId = chain.get( 0 );
      refbook = refbookService.listRefbooks().getByKey( rbId );
      refbookCombo.viewer.setSelection( new StructuredSelection( refbook ) );
      fillItemsList();
      String itemId = chain.get( 1 );
      ISkRefbookItem item = refbook.findItem( itemId );
      itemCombo.viewer.setSelection( new StructuredSelection( item ) );
    }
  }

  @Override
  protected ValidationResult doCanGetEntity() {
    IStructuredSelection sel = (IStructuredSelection)refbookCombo.viewer.getSelection();
    if( sel.isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать справочник" );
    }
    sel = (IStructuredSelection)itemCombo.viewer.getSelection();
    if( sel.isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать элемента справочника" );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void fillItemsList() {
    itemCombo.viewer.setInput( refbook.listItems().toArray() );
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
