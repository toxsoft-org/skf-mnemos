package org.toxsoft.skf.mnemo.gui.skved;

import java.util.*;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.advanced.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель выбора идентификатора перспективы Eclipse.
 * <p>
 *
 * @author dima
 */
public class PanelE4PerspIdSelector
    extends AbstractTsDialogPanel<MPerspId, ITsGuiContext> {

  /**
   * Отображалка информации о E4 перспективе в виде таблицы
   *
   * @author dima
   */
  private class MPerspectiveTableViewer {

    private final TableViewer viewer;

    protected MPerspectiveTableViewer( Composite aParent, int aStyle, int aIdWidth, int aNameWidth ) {
      viewer = new TableViewer( aParent, aStyle );

      viewer.getTable().setHeaderVisible( true );
      viewer.getTable().setLinesVisible( true );

      TableViewerColumn columnId = new TableViewerColumn( viewer, SWT.NONE );
      columnId.getColumn().setWidth( aIdWidth );
      columnId.getColumn().setText( "E4 perspective id" );
      columnId.setLabelProvider( new ColumnLabelProvider() {

        @Override
        public void update( ViewerCell aCell ) {
          MPerspId mp = (MPerspId)aCell.getElement();
          aCell.setText( mp.perspId() );
        }

      } );

      TableViewerColumn columnName = new TableViewerColumn( viewer, SWT.NONE );
      columnName.getColumn().setWidth( aNameWidth );
      columnName.getColumn().setText( "Название" );
      columnName.setLabelProvider( new CellLabelProvider() {

        @Override
        public void update( ViewerCell aCell ) {
          MPerspId mp = (MPerspId)aCell.getElement();
          aCell.setText( mp.perspLabel() );
        }

      } );

      ColumnViewerToolTipSupport.enableFor( viewer );
      viewer.setContentProvider( new ArrayContentProvider() );
    }

    TableViewer viewer() {
      return viewer;
    }

  }

  /**
   * Конструктор панели, предназаначенной для вставки в диалог {@link TsDialog}.
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelE4PerspIdSelector( Composite aParent, TsDialog<MPerspId, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init( aParent );
  }

  private MPerspectiveTableViewer tv;

  void init( Composite aParent ) {
    int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL;
    tv = new MPerspectiveTableViewer( aParent, style, 80, 200 );
    List<MPerspective> perspectives = listMPerspectives();
    List<MPerspId> mpIdList = new ArrayList<>();
    for( MPerspective mp : perspectives ) {
      mpIdList.add( new MPerspId( mp.getElementId(), mp.getLabel() ) );
    }
    tv.viewer().setInput( mpIdList.toArray() );
    tv.viewer().addSelectionChangedListener( aEvent -> fireContentChangeEvent() );
  }

  private List<MPerspective> listMPerspectives() {
    EModelService modelService = tsContext().eclipseContext().get( EModelService.class );
    MWindow window = tsContext().eclipseContext().get( MWindow.class );
    List<MPerspective> perspectives = modelService.findElements( window, null, MPerspective.class );
    return perspectives;
  }

  @Override
  protected void doSetDataRecord( MPerspId aPerspId ) {
    if( aPerspId != null ) {
      List<MPerspective> perspectives = listMPerspectives();
      for( MPerspective perspective : perspectives ) {
        if( perspective.getElementId().compareTo( aPerspId.perspId() ) == 0 ) {
          tv.viewer().setSelection( new StructuredSelection( perspective ) );
        }
      }
    }
  }

  @Override
  protected MPerspId doGetDataRecord() {
    IStructuredSelection selection = (IStructuredSelection)tv.viewer().getSelection();
    if( !selection.isEmpty() ) {
      return ((MPerspId)selection.getFirstElement());
    }
    return null;
  }

  @Override
  protected ValidationResult doValidate() {
    // check selected perspective
    if( tv.viewer().getSelection().isEmpty() ) {
      return ValidationResult.error( "Необходимо выбрать перспективу" );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Выводит диалог выбора Eclipse perspective.
   * <p>
   *
   * @param aInitPerspId {@link MPerspId} для инициализации
   * @param aContext {@link ITsGuiContext} - контекст
   * @return {@link MPerspId} - выбранный параметр <b>null</b> в случает отказа от редактирования
   */
  public static final MPerspId selectE4PerspId( MPerspId aInitPerspId, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<MPerspId, ITsGuiContext> creator = PanelE4PerspIdSelector::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, "Выбор E4 перспективы", "Выберите перспективу и нажмите Ok" );
    TsDialog<MPerspId, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInitPerspId, aContext, creator );
    return d.execData();
  }

}
