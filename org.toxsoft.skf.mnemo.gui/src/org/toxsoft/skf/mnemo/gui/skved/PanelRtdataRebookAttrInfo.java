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
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.refbooks.lib.*;
import org.toxsoft.skf.refbooks.lib.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.connection.*;

public class PanelRtdataRebookAttrInfo
    extends AbstractGenericEntityEditPanel<IdChain> {

  /**
   * Singleton of the panel creator {@link IGenericEntityEditPanelCreator} implementation.
   */
  public static final IGenericEntityEditPanelCreator<IdChain> PANEL_CREATOR = PanelRtdataRebookAttrInfo::new;

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

  StridableCombo refbookCombo;
  StridableCombo keyCombo;
  StridableCombo valueCombo;

  final ISkCoreApi        coreApi;
  final ISkRefbookService refbookService;

  PanelRtdataRebookAttrInfo( ITsGuiContext aTsContext, boolean aIsViewer ) {
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
        ISkRefbook r = (ISkRefbook)sel.getFirstElement();
        IDtoRefbookInfo rbInfo = DtoRefbookInfo.of( r );

        keyCombo.viewer.setInput( rbInfo.attrInfos().toArray() );
        valueCombo.viewer.setInput( rbInfo.attrInfos().toArray() );
      }
      fireChangeEvent();
    } );

    l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "Поле ключа: " );
    keyCombo = new StridableCombo( bkPanel, SWT.BORDER | SWT.DROP_DOWN );
    keyCombo.viewer.getCombo().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    keyCombo.viewer.addSelectionChangedListener( aEvent -> fireChangeEvent() );

    l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "Поле значения: " );
    valueCombo = new StridableCombo( bkPanel, SWT.BORDER | SWT.DROP_DOWN );
    valueCombo.viewer.getCombo().setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    valueCombo.viewer.addSelectionChangedListener( aEvent -> fireChangeEvent() );

    return bkPanel;
  }

  @Override
  protected IdChain doGetEntity() {
    IStringListEdit list = new StringArrayList();
    IStructuredSelection sel = (IStructuredSelection)refbookCombo.viewer.getSelection();
    list.add( ((IStridable)sel.getFirstElement()).id() );
    sel = (IStructuredSelection)keyCombo.viewer.getSelection();
    list.add( ((IStridable)sel.getFirstElement()).id() );
    sel = (IStructuredSelection)valueCombo.viewer.getSelection();
    list.add( ((IStridable)sel.getFirstElement()).id() );

    return new IdChain( list );
  }

  @Override
  protected void doProcessSetEntity() {
    IdChain chain = specifiedEntity();
    if( chain != null ) {
      String rbId = chain.get( 0 );
      ISkRefbook refbook = refbookService.listRefbooks().getByKey( rbId );
      IDtoRefbookInfo rbInfo = DtoRefbookInfo.of( refbook );
      refbookCombo.viewer.setSelection( new StructuredSelection( refbook ) );
      String fieldId = chain.get( 1 );
      keyCombo.viewer.setSelection( new StructuredSelection( rbInfo.attrInfos().getByKey( fieldId ) ) );
      fieldId = chain.get( 2 );
      valueCombo.viewer.setSelection( new StructuredSelection( rbInfo.attrInfos().getByKey( fieldId ) ) );
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
    sel = (IStructuredSelection)valueCombo.viewer.getSelection();
    if( sel.isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать поле значеня" );
    }
    return ValidationResult.SUCCESS;
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
