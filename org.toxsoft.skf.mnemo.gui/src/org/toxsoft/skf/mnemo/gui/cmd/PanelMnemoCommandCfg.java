package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.IMnemoGuiSharedResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.valed.ugwi.*;

/**
 * Панель редактирования набора значений свойств описания команды.
 * <p>
 *
 * @author vs
 */
public class PanelMnemoCommandCfg
    extends AbstractGenericEntityEditPanel<MnemoCommandCfg> {

  ISkConnection skConn;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - corresponding context
   * @param aIsViewer - <b>true</b> - panel is readonly<br>
   *          <b>false</b> - panel is editable
   */
  public PanelMnemoCommandCfg( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
    skConn = tsContext().get( ISkConnectionSupplier.class ).defConn();
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericEntityEditPanel
  //

  @Override
  protected MnemoCommandCfg doGetEntity() {
    IOptionSetEdit options = new OptionSet();
    IAtomicValue v = cmdUgwiEditor.getValue();
    if( v != null && v.isAssigned() ) {
      Ugwi cmdUgwi = v.asValobj();
      options.setValobj( MnemoCommandCfg.PROPID_CMD_UGWI, cmdUgwi );
      UgwiKindSkCmd uk = (UgwiKindSkCmd)SkUgwiUtils.listUgwiKinds().getByKey( cmdUgwi.kindId() );
      Gwid gwid = uk.getGwid( cmdUgwi );
      ISkClassInfo clsInfo = skConn.coreApi().sysdescr().getClassInfo( gwid.classId() );
      IDtoCmdInfo cmdInfo = clsInfo.cmds().list().getByKey( gwid.propId() );
      options.setStr( TSID_NAME, cmdInfo.nmName() );
      options.setStr( TSID_DESCRIPTION, cmdInfo.description() );
    }
    IOptionSet args = optionsPanel.getEntity();
    options.setValobj( MnemoCommandCfg.PROPID_CMD_ARGS, args );
    options.setValobj( MnemoCommandCfg.PROPID_CMD_CONFIRM, fldConfirm.getText() );
    return new MnemoCommandCfg( "cmdCfg" + System.currentTimeMillis(), options, IOptionSet.NULL ); //$NON-NLS-1$
  }

  @Override
  protected void doProcessSetEntity() {
    MnemoCommandCfg cfg = specifiedEntity();
    if( cfg != null ) {
      cmdUgwiEditor.setValue( avValobj( cfg.cmdUgwi() ) );
      fillArgsPanel( cfg.cmdUgwi(), cfg.args() );
      fldConfirm.setText( cfg.cmdConfirmationText() );
    }
  }

  Composite                        bkPanel;
  ValedAvUgwiSelectorTextAndButton cmdUgwiEditor;
  OptionSetPanel                   optionsPanel;
  Text                             fldConfirm;

  @Override
  protected Control doCreateControl( Composite aParent ) {
    bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, false ) );
    bkPanel.setLayoutData( BorderLayout.CENTER );
    bkPanel.setData( AWTLayout.KEY_PREFERRED_SIZE, new java.awt.Dimension( 500, 300 ) );

    CLabel label;

    label = new CLabel( bkPanel, SWT.CENTER );
    label.setText( STR_L_COMMAND );
    Control ctrl;
    ITsGuiContext cmdUgwiCtx = new TsGuiContext( tsContext() );
    cmdUgwiEditor = new ValedAvUgwiSelectorTextAndButton( cmdUgwiCtx );
    cmdUgwiEditor.params().setStr( ValedUgwiSelector.OPDEF_SINGLE_UGWI_KIND_ID, UgwiKindSkCmd.KIND_ID );
    cmdUgwiEditor.eventer().addListener( ( aSource, aEditFinished ) -> {
      if( aEditFinished ) {
        optionsPanel.setEntity( IOptionSet.NULL );
        Ugwi ugwi = cmdUgwiEditor.getValue().asValobj();
        fillArgsPanel( ugwi, IOptionSet.NULL );
        // if( ugwi != null && ugwi != Ugwi.NONE ) {
        // UgwiKindSkCmd uk = (UgwiKindSkCmd)SkUgwiUtils.listUgwiKinds().getByKey( ugwi.kindId() );
        // Gwid gwid = uk.getGwid( ugwi );
        // ISkClassInfo clsInfo = skConn.coreApi().sysdescr().findClassInfo( gwid.classId() );
        // IDtoCmdInfo cmdInfo = null;
        // if( clsInfo != null ) {
        // cmdInfo = clsInfo.cmds().list().findByKey( gwid.propId() );
        // optionsPanel.setOptionDefs( cmdInfo.argDefs() );
        // }
        // }
      }
    } );

    ctrl = cmdUgwiEditor.createControl( bkPanel );
    ctrl.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    Group group = new Group( bkPanel, SWT.NONE );
    group.setLayout( new FillLayout() );
    group.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 3, 1 ) );
    group.setText( STR_L_ARGUMENTS );
    optionsPanel = new OptionSetPanel( tsContext(), false );
    optionsPanel.createControl( group );

    // label = new CLabel( bkPanel, SWT.CENTER );
    // label.setText( STR_L_ARGUMENTS );
    // ITsGuiContext argsCtx = new TsGuiContext( tsContext() );
    // argSetEditor = new ValedCmdArgValuesSet( argsCtx );
    // ctrl = argSetEditor.createControl( bkPanel );
    // ctrl.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    label = new CLabel( bkPanel, SWT.CENTER );
    label.setText( STR_L_CONFIRMATION );
    fldConfirm = new Text( bkPanel, SWT.BORDER );
    fldConfirm.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );

    return bkPanel;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void fillArgsPanel( Ugwi aCmdUgwi, IOptionSet aArgValues ) {
    if( aCmdUgwi != null && aCmdUgwi != Ugwi.NONE ) {
      UgwiKindSkCmd uk = (UgwiKindSkCmd)SkUgwiUtils.listUgwiKinds().getByKey( aCmdUgwi.kindId() );
      Gwid gwid = uk.getGwid( aCmdUgwi );
      ISkClassInfo clsInfo = skConn.coreApi().sysdescr().findClassInfo( gwid.classId() );
      IDtoCmdInfo cmdInfo = null;
      if( clsInfo != null ) {
        cmdInfo = clsInfo.cmds().list().findByKey( gwid.propId() );
        optionsPanel.setOptionDefs( cmdInfo.argDefs() );
        optionsPanel.setEntity( aArgValues );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Статические методы
  //

  /**
   * Edit command properties.
   *
   * @param aCfg {@link MnemoCommandCfg} - command configuration
   * @param aTsContext {@link ITsGuiContext} corresponding context
   * @return MnemoCommandCfg - new command configuration or <b>null</b>
   */
  public static MnemoCommandCfg edit( MnemoCommandCfg aCfg, ITsGuiContext aTsContext ) {
    IGenericEntityEditPanelCreator<MnemoCommandCfg> panelCreator = PanelMnemoCommandCfg::new;

    IDialogPanelCreator<MnemoCommandCfg, ITsGuiContext> creator = null;
    creator = ( aParent, aOwnerDialog ) -> new TsDialogGenericEntityEditPanel<>( aParent, aOwnerDialog, panelCreator );

    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, DLG_T_CMD_PROPS, DLG_C_SELECT_CMD_AND_ARGS );
    TsDialog<MnemoCommandCfg, ITsGuiContext> d = new TsDialog<>( dlgInfo, aCfg, aTsContext, creator );
    return d.execData();
  }

}
