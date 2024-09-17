package org.toxsoft.skf.mnemo.gui.tools.imageset;

import static org.toxsoft.skf.mnemo.gui.tools.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.valed.controls.graphics.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tools.imageset.PanelImageSetEntryEditor.*;

/**
 * Панель создания и редактирования элемента набора изображений.
 *
 * @author vs
 */
public class PanelImageSetEntryEditor
    extends AbstractTsDialogPanel<ImageEntryInfo, PanelCtx> {

  static class PanelCtx {

    final IStringList       entryNames;
    final ITsGuiContext     tsContext;
    final TsImageDescriptor imd;

    PanelCtx( TsImageDescriptor aDescriptor, ITsGuiContext aTsContext, IStringList aEntryNames ) {
      imd = aDescriptor;
      tsContext = aTsContext;
      entryNames = aEntryNames;
    }
  }

  PanelTsImageDescriptorEditor imdEditor;

  /**
   * Конструктор.<br>
   *
   * @param aParent Composite - родительская компонента
   * @param aOwnerDialog TsDialog - родительский диалог
   */
  public PanelImageSetEntryEditor( Composite aParent, TsDialog<ImageEntryInfo, PanelCtx> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    this.setLayout( new BorderLayout() );
    init();
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  Text fldName;

  void init() {
    setLayout( new org.toxsoft.core.tsgui.utils.layout.BorderLayout() );
    Composite topComp = new Composite( this, SWT.NONE );
    topComp.setLayout( new GridLayout( 2, false ) );
    CLabel l = new CLabel( topComp, SWT.CENTER );
    l.setText( STR_L_NAME );
    fldName = new Text( topComp, SWT.BORDER );
    fldName.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );
    topComp.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.NORTH );

    imdEditor = new PanelTsImageDescriptorEditor( this, environ().tsContext, environ().imd, 0 );
    imdEditor.setLayoutData( org.toxsoft.core.tsgui.utils.layout.BorderLayout.CENTER );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ImageEntryInfo aData ) {
    imdEditor.setDataRecord( environ().imd );
    // fldId.setEditable( false );
    // if( aData == null || aData == IMnemoImageSetInfo.EMPTY ) {
    // fldId.setText( idGen.nextId() );
    // }
    // if( aData != null ) {
    // if( aData != IMnemoImageSetInfo.EMPTY ) {
    // fldId.setText( aData.id() );
    // }
    // fldName.setText( aData.nmName() );
    // fldDescription.setText( aData.description() );
    // imgInfoList.clear();
    // imgInfoList.addAll( aData.imageInfoes() );
    // imgViewer.setInput( imgInfoList.toArray() );
    // }
  }

  @Override
  protected ImageEntryInfo doGetDataRecord() {
    TsImageDescriptor imgDescr = imdEditor.getDataRecord();
    return new ImageEntryInfo( fldName.getText(), imgDescr );
  }

  // ------------------------------------------------------------------------------------
  // static method to create or edit IMnemoImageSetInfo
  //

  /**
   * Рдактирует и возвращает описание набора изображений.
   * <p>
   *
   * @param aInfo IMnemoImageSetInfo - описание набора изображений
   * @param aContext - контекст
   * @return IMnemoImageSetInfo - описание набора изображений или <b>null</b> в случает отказа от редактирования
   */
  public static final ImageEntryInfo edit( ImageEntryInfo aInfo, PanelCtx aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<ImageEntryInfo, PanelCtx> creator = PanelImageSetEntryEditor::new;
    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( aContext.tsContext, DLG_T_IMG_SET_ENTRY_INFO, STR_MSG_IMG_SET_ENTRY_INFO );
    TsDialog<ImageEntryInfo, PanelCtx> d = new TsDialog<>( dlgInfo, aInfo, aContext, creator );
    return d.execData();
  }

}
