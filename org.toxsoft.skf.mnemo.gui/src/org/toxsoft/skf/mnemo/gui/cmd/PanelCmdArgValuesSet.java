package org.toxsoft.skf.mnemo.gui.cmd;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель редактирования набора значений аргументов команды определенного типа
 *
 * @author vs
 */
public class PanelCmdArgValuesSet
    extends AbstractGenericEntityEditPanel<CmdArgValuesSet> {

  ISkConnection skConn;

  public PanelCmdArgValuesSet( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
    skConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericEntityEditPanel
  //

  @Override
  protected CmdArgValuesSet doGetEntity() {
    IOptionSet props = optionsPanel.getEntity();
    return new CmdArgValuesSet( fldId.getText(), fldClassId.getText(), fldCmdId.getText(), props, IOptionSet.NULL );
  }

  @Override
  protected void doProcessSetEntity() {
    CmdArgValuesSet args = specifiedEntity();
    if( args != null ) {
      fldId.setText( args.id() );
      fldClassId.setText( args.classId() );
      fldCmdId.setText( args.cmdId() );
      fillOptionsPanel();
      optionsPanel.setEntity( args.argValues( skConn.coreApi() ) );
    }
  }

  IMnemoUserAction commander;

  Text fldId;
  Text fldClassId;
  Text fldCmdId;

  TinWidget tinWidget;

  PanelUserActionCfgUgwies ugwiesPanel;

  Composite bkPanel;

  OptionSetPanel optionsPanel;

  @Override
  protected Control doCreateControl( Composite aParent ) {
    bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 3, false ) );
    bkPanel.setLayoutData( BorderLayout.CENTER );
    bkPanel.setData( AWTLayout.KEY_PREFERRED_SIZE, new java.awt.Dimension( 500, 600 ) );

    CLabel l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "ИД набора: " );
    fldId = new Text( bkPanel, SWT.BORDER );
    fldId.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 2, 1 ) );

    l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "ИД класса: " );
    fldClassId = new Text( bkPanel, SWT.BORDER );
    fldClassId.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Button btnBrowseClass = new Button( bkPanel, SWT.PUSH );
    btnBrowseClass.setText( "..." );
    btnBrowseClass.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        ISkClassInfo clsInfo = SkGuiUtils.selectClass( null, skConn, tsContext() );
        if( clsInfo != null ) {
          fldClassId.setText( clsInfo.id() );
          fldCmdId.setText( IStridable.NONE_ID );
          fillOptionsPanel();
        }
      }
    } );

    l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "ИД кoманды: " );

    fldCmdId = new Text( bkPanel, SWT.BORDER );
    fldCmdId.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Button btnBrowseCmd = new Button( bkPanel, SWT.PUSH );
    btnBrowseCmd.setText( "..." );
    btnBrowseCmd.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        IDtoCmdInfo cmdInfo = SkGuiUtils.selectCmd( fldClassId.getText(), skConn, tsContext() );
        if( cmdInfo != null ) {
          fldCmdId.setText( cmdInfo.id() );
          fillOptionsPanel();
        }
      }
    } );

    Group group = new Group( bkPanel, SWT.NONE );
    group.setLayout( new FillLayout() );
    group.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 3, 1 ) );
    group.setText( "Аргументы" );
    optionsPanel = new OptionSetPanel( tsContext(), false );
    Control ctrl = optionsPanel.createControl( group );

    return bkPanel;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void fillOptionsPanel() {
    String clsId = fldClassId.getText();
    String cmdId = fldCmdId.getText();
    ISkClassInfo clsInfo = skConn.coreApi().sysdescr().findClassInfo( clsId );
    IDtoCmdInfo cmdInfo = null;
    if( clsInfo != null ) {
      cmdInfo = clsInfo.cmds().list().findByKey( cmdId );
    }
    if( cmdInfo != null ) {
      optionsPanel.setOptionDefs( cmdInfo.argDefs() );
    }
    else {
      optionsPanel.setOptionDefs( IStridablesList.EMPTY );
    }
    optionsPanel.setEntity( IOptionSet.NULL );
  }

  // ------------------------------------------------------------------------------------
  // Статические методы
  //

  public static CmdArgValuesSet edit( CmdArgValuesSet aCfg, ITsGuiContext aTsContext ) {
    IGenericEntityEditPanelCreator<CmdArgValuesSet> panelCreator = PanelCmdArgValuesSet::new;

    IDialogPanelCreator<CmdArgValuesSet, ITsGuiContext> creator = null;
    creator = ( aParent, aOwnerDialog ) -> new TsDialogGenericEntityEditPanel<>( aParent, aOwnerDialog, panelCreator );

    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "Caption", "Title" );
    TsDialog<CmdArgValuesSet, ITsGuiContext> d = new TsDialog<>( dlgInfo, aCfg, aTsContext, creator );
    return d.execData();
  }

}
