package org.toxsoft.skf.mnemo.gui.tools.rgbaset;

import static org.toxsoft.skf.mnemo.gui.tools.ITsResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Панель редактирования набора {@link RGBA}.
 *
 * @author vs
 */
public class PanelRgbaSetEditor
    extends AbstractTsDialogPanel<IRgbaSet, ITsGuiContext> {

  Text fldId;
  Text fldName;
  Text fldDescription;

  TableViewer rgbaViewer;

  private final IListEdit<RGBA> rgbaList = new ElemArrayList<>();

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelRgbaSetEditor( Composite aParent, TsDialog<IRgbaSet, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  /**
   * Конструктор для использовани вне диалога.<br>
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   * @param aData {@link IRgbaSet} - информация о заливке м.б. <b>null</b>
   */
  public PanelRgbaSetEditor( Composite aParent, ITsGuiContext aContext, IRgbaSet aData ) {
    super( aParent, aContext, aData, aContext, 0 );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return environ();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( IRgbaSet aData ) {
    // TODO получить noneId вместо fooId
    if( aData != null && !aData.id().equals( "fooId" ) ) { //$NON-NLS-1$
      fldId.setEditable( false );
      fldId.setText( aData.id() );
      fldName.setText( aData.nmName() );
      fldDescription.setText( aData.description() );
      for( int i = 0; i < aData.size(); i++ ) {
        rgbaList.add( aData.getRgba( i ) );
      }
      rgbaViewer.setInput( rgbaList.toArray() );
    }
  }

  @Override
  protected IRgbaSet doGetDataRecord() {
    String id = fldId.getText();
    String name = fldName.getText();
    String description = fldDescription.getText();
    return new RgbaSet( id, name, description, rgbaList );
  }

  @Override
  protected ValidationResult validateData() {
    if( !StridUtils.isValidIdPath( fldId.getText() ) ) {
      return ValidationResult.error( STR_ERR_WRONG_SET_ID );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void init() {
    Composite descrPanel = new Composite( this, SWT.NONE );
    descrPanel.setLayout( new GridLayout( 2, false ) );
    descrPanel.setLayoutData( BorderLayout.NORTH );
    CLabel l;

    l = new CLabel( descrPanel, SWT.CENTER );
    l.setText( STR_L_IDENTIFIER );
    fldId = new Text( descrPanel, SWT.BORDER );
    fldId.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    fldId.addModifyListener( aEvent -> fireContentChangeEvent() );

    l = new CLabel( descrPanel, SWT.CENTER );
    l.setText( STR_L_NAME );
    fldName = new Text( descrPanel, SWT.BORDER );
    fldName.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    l = new CLabel( descrPanel, SWT.CENTER );
    l.setText( STR_L_DESCRIPTION );
    fldDescription = new Text( descrPanel, SWT.BORDER );
    fldDescription.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Composite imgPanel = new Composite( this, SWT.NONE );
    imgPanel.setLayout( new GridLayout( 2, false ) );
    imgPanel.setLayoutData( BorderLayout.CENTER );

    rgbaViewer = new TableViewer( imgPanel, SWT.BORDER | SWT.FULL_SELECTION );
    rgbaViewer.getTable().setHeaderVisible( true );
    rgbaViewer.getTable().setLinesVisible( true );
    rgbaViewer.getTable().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    rgbaViewer.getTable().addListener( SWT.EraseItem, event -> {
      TableItem tableItem = (TableItem)event.item;
      int itemIndex = rgbaViewer.getTable().indexOf( tableItem );
      RGBA rgba = rgbaList.get( itemIndex );
      Rectangle r = tableItem.getBounds( 0 );
      GC gc = event.gc;
      gc.setClipping( r );
      Color oldForeground = gc.getForeground();
      Color oldBackground = gc.getBackground();
      int oldAlpha = gc.getAlpha();

      Color c1 = colorManager().getColor( ETsColor.GRAY );
      Color c2 = colorManager().getColor( ETsColor.DARK_GRAY );
      GradientUtils.drawChessBoard( gc, r, c1, c2, 8 );

      gc.setBackground( new Color( rgba.rgb ) );
      gc.setAlpha( rgba.alpha );
      gc.fillRectangle( r );

      gc.setAlpha( oldAlpha );
      gc.setForeground( oldForeground );
      gc.setBackground( oldBackground );
      event.detail &= ~SWT.BACKGROUND;
      event.detail &= ~SWT.HOT;
    } );

    rgbaViewer.setContentProvider( new ArrayContentProvider() );

    TableViewerColumn columnImage = new TableViewerColumn( rgbaViewer, SWT.NONE );
    columnImage.getColumn().setWidth( 150 );
    columnImage.getColumn().setText( "Цвет" ); //$NON-NLS-1$
    columnImage.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        // nop
      }
    } );

    TableViewerColumn columnNumber = new TableViewerColumn( rgbaViewer, SWT.LEFT );
    columnNumber.getColumn().setWidth( 50 );
    columnNumber.getColumn().setText( "№" ); //$NON-NLS-1$
    columnNumber.getColumn().setAlignment( SWT.LEFT );
    columnNumber.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        RGBA rgba = (RGBA)aCell.getElement();
        int idx = rgbaList.indexOf( rgba );
        aCell.setText( TsLibUtils.EMPTY_STRING + idx );
      }
    } );

    TableViewerColumn columnColor = new TableViewerColumn( rgbaViewer, SWT.LEFT );
    columnColor.getColumn().setWidth( 210 );
    columnColor.getColumn().setText( "Компоненты цвета" ); //$NON-NLS-1$
    columnColor.getColumn().setAlignment( SWT.LEFT );
    columnColor.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        RGBA rgba = (RGBA)aCell.getElement();
        aCell.setText( rgba.toString() );
      }
    } );

    Composite btnsPanel = new Composite( imgPanel, SWT.NONE );
    btnsPanel.setLayout( new GridLayout( 1, true ) );
    GridData gd = new GridData( SWT.RIGHT, SWT.FILL, false, true );
    gd.heightHint = 300;
    btnsPanel.setLayoutData( gd );

    Button btnAdd = new Button( btnsPanel, SWT.PUSH );
    btnAdd.setText( STR_B_ADD );
    btnAdd.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        RGBA rgba = PanelRgbaSelector.editRgba( new RGBA( 0, 0, 0, 255 ), environ() );
        if( rgba != null ) {
          rgbaList.add( rgba );
          rgbaViewer.setInput( rgbaList.toArray() );
        }
      }
    } );

    Button btnDelete = new Button( btnsPanel, SWT.PUSH );
    btnDelete.setText( STR_B_REMOVE );
    btnDelete.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Button btnUp = new Button( btnsPanel, SWT.PUSH );
    btnUp.setText( STR_B_UP );
    btnUp.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Button btnDown = new Button( btnsPanel, SWT.PUSH );
    btnDown.setText( STR_B_DOWN );
    btnDown.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit Pattern
  //

  /**
   * Рдактирует и возвращает описание набора цветов в виде {@link RGBA}.
   * <p>
   *
   * @param aInfo IRgbaSet - описание набора цветов
   * @param aContext - контекст
   * @return IRgbaSet - описание набора цветов или <b>null</b> в случает отказа от редактирования
   */
  public static final IRgbaSet editRgbaSet( IRgbaSet aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<IRgbaSet, ITsGuiContext> creator = PanelRgbaSetEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_RGBA_SET, STR_MSG_RGBA_SET );
    TsDialog<IRgbaSet, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
