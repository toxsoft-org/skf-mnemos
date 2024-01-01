package org.toxsoft.skf.mnemo.gui.skved.panels;

import static org.toxsoft.skf.mnemo.gui.skved.panels.ISkResources.*;

import java.io.*;
import java.nio.file.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.rcp.graphics.images.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

public class PanelImageSetEditor
    extends AbstractTsDialogPanel<IMnemoImageSetInfo, ITsGuiContext>
    implements ITsGuiContextable {

  Text fldId;
  Text fldName;
  Text fldDescription;

  private final IStridablesListEdit<IImageEntryInfo> imgInfoList = new StridablesList<>();

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelImageSetEditor( Composite aParent, TsDialog<IMnemoImageSetInfo, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  /**
   * Конструктор для использовани вне диалога.<br>
   *
   * @param aParent {@link Composite} - родительская компонента
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   * @param aData {@link IMnemoImageSetInfo} - информация о заливке м.б. <b>null</b>
   */
  public PanelImageSetEditor( Composite aParent, ITsGuiContext aContext, IMnemoImageSetInfo aData ) {
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
  protected void doSetDataRecord( IMnemoImageSetInfo aData ) {
    if( aData != null ) {
      fldId.setEditable( false );
    }
  }

  @Override
  protected IMnemoImageSetInfo doGetDataRecord() {
    // TODO Auto-generated method stub
    return null;
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

    TableViewer imgViewer = new TableViewer( imgPanel, SWT.BORDER );
    imgViewer.getTable().setHeaderVisible( true );
    imgViewer.getTable().setLinesVisible( true );
    imgViewer.getTable().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    imgViewer.getTable().addListener( SWT.MeasureItem, event -> {
      // height cannot be per row so simply set
      if( event.height < 67 ) {
        event.height = 67;
        event.doit = false;
      }
    } );

    imgViewer.setContentProvider( new ArrayContentProvider() );

    TableViewerColumn columnImage = new TableViewerColumn( imgViewer, SWT.NONE );
    columnImage.getColumn().setWidth( 150 );
    columnImage.getColumn().setText( "Изображение" ); //$NON-NLS-1$
    columnImage.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IImageEntryInfo info = (IImageEntryInfo)aCell.getElement();
        TsImage tsImage = imageManager().getImage( info.imageDescriptor() );
        aCell.setImage( tsImage.image() );
      }
    } );

    TableViewerColumn columnNumber = new TableViewerColumn( imgViewer, SWT.LEFT );
    columnNumber.getColumn().setWidth( 50 );
    columnNumber.getColumn().setText( "№" ); //$NON-NLS-1$
    columnNumber.getColumn().setAlignment( SWT.LEFT );
    columnNumber.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IImageEntryInfo info = (IImageEntryInfo)aCell.getElement();
        int idx = imgInfoList.indexOf( info );
        aCell.setText( "" + idx );
      }
    } );

    Composite btnsPanel = new Composite( imgPanel, SWT.NONE );
    btnsPanel.setLayout( new GridLayout( 1, true ) );
    GridData gd = new GridData( SWT.RIGHT, SWT.FILL, false, true );
    gd.heightHint = 300;
    btnsPanel.setLayoutData( gd );

    Button btnAdd = new Button( btnsPanel, SWT.PUSH );
    btnAdd.setText( "Добавить..." );
    btnAdd.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        FileDialog fd = new FileDialog( getShell(), SWT.OPEN | SWT.MULTI );
        String result = fd.open();
        if( result != null ) {
          int idx = result.lastIndexOf( '\\' ) + 1;
          String filePath = result.substring( 0, idx );
          String[] fileNames = fd.getFileNames();
          for( int i = 0; i < fileNames.length; i++ ) {
            if( isImage( filePath + fileNames[i] ) ) {
              TsImageDescriptor imd = TsImageSourceKindFile.createImageDescriptor( filePath + fileNames[i] );
              ImageEntryInfo imageInfo = new ImageEntryInfo( getUniqueName( fileNames[i] ), imd );
              imgInfoList.add( imageInfo );
            }
          }
          imgViewer.setInput( imgInfoList.toArray() );
        }
      }
    } );

    Button btnDelete = new Button( btnsPanel, SWT.PUSH );
    btnDelete.setText( "Удалить" );
    btnDelete.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Button btnUp = new Button( btnsPanel, SWT.PUSH );
    btnUp.setText( "Вверх" );
    btnUp.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Button btnDown = new Button( btnsPanel, SWT.PUSH );
    btnDown.setText( "Вниз" );
    btnDown.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

  }

  String getUniqueName( String aFileName ) {
    String un = aFileName.replace( '-', '_' );
    int num = 1;
    while( imgInfoList.hasKey( un ) ) {
      un += num;
      num++;
    }
    return un;
  }

  boolean isImage( String aFilePath ) {
    File file = new File( aFilePath );
    String mimetype = null;
    try {
      mimetype = Files.probeContentType( file.toPath() );
    }
    catch( IOException ex ) {
      // TODO Auto-generated catch block
      LoggerUtils.errorLogger().error( ex );
    }
    // mimetype should be something like "image/png"
    if( mimetype != null && mimetype.split( "/" )[0].equals( "image" ) ) { //$NON-NLS-1$ //$NON-NLS-2$
      return true;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit Pattern
  //

  /**
   * Рдактирует и возвращает описание набора изображений.
   * <p>
   *
   * @param aInfo IMnemoImageSetInfo - описание набора изображений
   * @param aContext - контекст
   * @return IMnemoImageSetInfo - описание набора изображений или <b>null</b> в случает отказа от редактирования
   */
  public static final IMnemoImageSetInfo editInfo( IMnemoImageSetInfo aInfo, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<IMnemoImageSetInfo, ITsGuiContext> creator = PanelImageSetEditor::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext, DLG_T_IMG_SET_INFO, STR_MSG_IMG_SET_INFO );
    TsDialog<IMnemoImageSetInfo, ITsGuiContext> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
