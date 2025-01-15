package org.toxsoft.skf.mnemo.gui.cmd;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.opsedit.*;
import org.toxsoft.core.tsgui.panels.opsedit.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.*;

public class PanelVedUserActionCfg
    extends AbstractGenericEntityEditPanel<VedUserActionCfg> {

  public PanelVedUserActionCfg( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericEntityEditPanel
  //

  @Override
  protected VedUserActionCfg doGetEntity() {
    if( commander != null ) {
      IOptionSetEdit props = new OptionSet();
      IOptionSetEdit params = new OptionSet();
      if( optionPanel != null ) {
        props.setAll( optionPanel.getEntity() );
      }
      if( tinWidget != null ) {
        ITinTypeInfo tti = tinWidget.getTypeInfo();
        ITinValue tv = tinWidget.getValue();
        props.addAll( TinUtils.tinValue2OptionSet( tv, tti ) );
        // props.addAll( TinUtils.tinValue2OptionSet( tinWidget.getValue(), tinWidget.getTypeInfo() ) );
      }
      if( ugwiesPanel != null ) {
        params.setAll( ugwiesPanel.getResolversInfo() );
      }
      return new VedUserActionCfg( commander.id(), commander.id(), props, params );
    }
    return null;
  }

  @Override
  protected void doProcessSetEntity() {
    VedUserActionCfg cfg = specifiedEntity();
    if( cfg != null ) {
      MnemoUserActionsRegistry mcr = tsContext().get( MnemoUserActionsRegistry.class );
      commander = mcr.registeredActions().getByKey( cfg.commanderId() );
      fldCommander.setText( commander.nmName() );

      createContent( bkPanel, commander, cfg );

      // optionPanel.setOptionDefs( commander.inputDefs() );
      // optionPanel.setEntity( cfg.propValues() );
    }
  }

  IMnemoUserAction commander;

  Text fldCommander;

  TinWidget tinWidget;

  IOptionSetPanel optionPanel;

  PanelUserActionCfgUgwies ugwiesPanel;

  Composite bkPanel;

  @Override
  protected Control doCreateControl( Composite aParent ) {
    bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 3, false ) );
    bkPanel.setLayoutData( BorderLayout.CENTER );
    bkPanel.setData( AWTLayout.KEY_PREFERRED_SIZE, new java.awt.Dimension( 500, 600 ) );

    CLabel l = new CLabel( bkPanel, SWT.CENTER );
    l.setText( "Commander: " );

    fldCommander = new Text( bkPanel, SWT.BORDER );
    fldCommander.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    Button btnBrowse = new Button( bkPanel, SWT.PUSH );
    btnBrowse.setText( "..." );
    btnBrowse.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        ITsDialogInfo tdi = new TsDialogInfo( tsContext(), "Commander", "Select commander and press\"OK\"" );
        MnemoUserActionsRegistry mcr = tsContext().get( MnemoUserActionsRegistry.class );
        IMnemoUserAction mc = DialogItemsList.select( tdi, mcr.registeredActions().values(), null );
        if( mc != null ) {
          commander = mc;
          fldCommander.setText( commander.nmName() );
          createContent( bkPanel, mc, null );
          // optionPanel.setOptionDefs( commander.inputDefs() );
        }
      }
    } );

    // tinWidget = new TinWidget( tsContext() );
    // optionPanel = new OptionSetPanel( tsContext(), false );

    // Control ctrl = optionPanel.createControl( bkPanel );
    // ctrl.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 3, 1 ) );

    return bkPanel;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  protected ITinTypeInfo tinTypeInfo( IMnemoUserAction aAction ) {
    IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
    for( ITinFieldInfo fi : aAction.tinFields() ) {
      fields.add( fi );
    }
    return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRtdataValue.class );
  }

  void createContent( Composite aParent, IMnemoUserAction aAction, VedUserActionCfg aCfg ) {
    if( tinWidget != null && tinWidget.getControl() != null ) {
      tinWidget.getControl().dispose();
    }
    if( optionPanel != null && optionPanel.getControl() != null ) {
      optionPanel.getControl().dispose();
    }
    if( ugwiesPanel != null ) {
      ugwiesPanel.dispose();
      ugwiesPanel = null;
    }

    if( aAction != null ) {
      if( aAction.tinFields().size() > 0 ) {
        IVedScreen vedScreen = tsContext().get( IVedScreen.class );
        MnemoResolverConfig mrf = MasterObjectUtils.readMnemoResolverConfig( vedScreen );
        String smClassId = MasterObjectUtils.findMainMasterClassId( mrf );
        if( smClassId != null ) {
          ugwiesPanel = new PanelUserActionCfgUgwies( aParent, vedScreen, SWT.NONE );
          GridData gd = new GridData( SWT.FILL, SWT.FILL, true, true, 3, 1 );
          gd.minimumHeight = 80;
          ugwiesPanel.setLayoutData( gd );
          ugwiesPanel.setUserAction( aAction, aCfg );
        }

        // for( ITinFieldInfo fi : aAction.tinFields() ) {
        // fi.typeInfo().dataType();
        // }

        tinWidget = new TinWidget( tsContext() );
        Control ctrl = tinWidget.createControl( aParent );
        ctrl.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 3, 1 ) );
        ITinTypeInfo typeInfo = tinTypeInfo( aAction );
        tinWidget.setEntityInfo( typeInfo );
        if( aCfg != null ) {
          ITinValue tv = TinUtils.optionSet2TinValue( aCfg.propValues(), typeInfo );
          tinWidget.setValue( tv );
        }
      }

      // if( aAction.tinFields().size() > 0 ) {
      if( commander.inputDefs().size() > 0 ) {
        optionPanel = new OptionSetPanel( tsContext(), false );
        Control ctrl = optionPanel.createControl( aParent );
        ctrl.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false, 3, 1 ) );
        optionPanel.setOptionDefs( commander.inputDefs() );
        if( aCfg != null ) {
          optionPanel.setEntity( aCfg.propValues() );
        }
      }
    }

  }

  // ------------------------------------------------------------------------------------
  //
  //

  public static VedUserActionCfg edit( VedUserActionCfg aCfg, ITsGuiContext aTsContext ) {
    IGenericEntityEditPanelCreator<VedUserActionCfg> panelCreator = PanelVedUserActionCfg::new;

    IDialogPanelCreator<VedUserActionCfg, ITsGuiContext> creator = null;
    creator = ( aParent, aOwnerDialog ) -> new TsDialogGenericEntityEditPanel<>( aParent, aOwnerDialog, panelCreator );

    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "Caption", "Title" );
    TsDialog<VedUserActionCfg, ITsGuiContext> d = new TsDialog<>( dlgInfo, aCfg, aTsContext, creator );
    return d.execData();
  }

}
