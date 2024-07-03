package org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;

public class ConfigRecognizerPanel
    extends AbstractTsDialogPanel<ISkoRecognizerCfg, IStridablesList<ISkObject>> {

  // public static class IStridablesList<ISkObject> {
  //
  // ITsGuiContext tsContext;
  // Skid objSkid;
  //
  // public IStridablesList<ISkObject>( Skid aObjSkid, ITsGuiContext aTsContext ) {
  // objSkid = aObjSkid;
  // tsContext = aTsContext;
  // }
  // }

  protected ConfigRecognizerPanel( Composite aParent,
      TsDialog<ISkoRecognizerCfg, IStridablesList<ISkObject>> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected ConfigRecognizerPanel( Composite aParent, ISkoRecognizerCfg aData, IStridablesList<ISkObject> aEnviron,
      ITsGuiContext aTsContext, int aFlags ) {
    super( aParent, aTsContext, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( ISkoRecognizerCfg aData ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected ISkoRecognizerCfg doGetDataRecord() {
    if( stackLayout.topControl != null ) {
      ISkoRecognizerCfgPanel panel = findRecognizerPanel( stackLayout.topControl );
      if( panel != null ) {
        ISkoRecognizerCfg cfg = panel.getEntity();
        return cfg;
      }
    }
    return null;
  }

  @Override
  protected ValidationResult validateData() {
    if( stackLayout.topControl != null ) {
      ISkoRecognizerCfgPanel panel = findRecognizerPanel( stackLayout.topControl );
      if( panel != null ) {
        return panel.canGetEntity();
      }
    }
    return ValidationResult.create( EValidationResultType.ERROR, "Выберите требуемые параметры" );
  }

  IGenericChangeListener panelListener = aSource -> {
    fireContentChangeEvent();
  };

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private ValedEnumCombo<ESkoRecognizerKind> kindCombo;

  Composite stackPanel;

  StackLayout stackLayout;

  ISkCoreApi coreApi;

  private final IMapEdit<String, ISkoRecognizerCfgPanel> panels = new ElemMap<>();

  void init() {
    coreApi = SkGuiUtils.getCoreApi( tsContext() );
    setLayout( new BorderLayout() );

    Composite topPanel = new Composite( this, SWT.NONE );
    topPanel.setLayout( new GridLayout( 2, false ) );

    CLabel l;

    l = new CLabel( topPanel, SWT.NONE );
    l.setText( "Тип распознавателя: " );
    kindCombo = new ValedEnumCombo<>( tsContext(), ESkoRecognizerKind.class, IStridable::nmName );
    kindCombo.createControl( topPanel );
    kindCombo.getControl().addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( kindCombo.canGetValue() == ValidationResult.SUCCESS ) {
          ESkoRecognizerKind kind = kindCombo.getValue();
          ISkoRecognizerCfgPanel cfgP;
          if( !panels.hasKey( kind.id() ) ) {
            cfgP = kind.getCfgEditPanel( environ(), tsContext() );
            panels.put( kind.id(), cfgP );
            cfgP.genericChangeEventer().addListener( panelListener );
          }
          cfgP = panels.getByKey( kind.id() );
          Control ctrl = cfgP.getControl();
          if( ctrl == null ) {
            ctrl = cfgP.createControl( stackPanel );
          }
          stackLayout.topControl = cfgP.getControl();
          stackPanel.layout();
        }
      }
    } );
    topPanel.setLayoutData( BorderLayout.NORTH );

    stackPanel = new Composite( this, SWT.NONE );
    stackLayout = new StackLayout();
    stackPanel.setLayout( stackLayout );
    stackPanel.setLayoutData( BorderLayout.CENTER );

  }

  ISkoRecognizerCfgPanel findRecognizerPanel( Control aControl ) {
    for( ISkoRecognizerCfgPanel panel : panels.values() ) {
      if( panel.getControl() == aControl ) {
        return panel;
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров выравнивания содержимого ячейки.
   *
   * @param aData ISkoRecognizerCfg - параметры "распознавателя"
   * @param aObjects IStridablesList&lt;ISkObject> - соответствующий контекст панели
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return {@link ISkoRecognizerCfg} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final ISkoRecognizerCfg edit( ISkoRecognizerCfg aData, IStridablesList<ISkObject> aObjects,
      ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aObjects );
    IDialogPanelCreator<ISkoRecognizerCfg, IStridablesList<ISkObject>> creator = ConfigRecognizerPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aTsContext, "\"Распознаватель\" объектов", "STR_MSG_CANVAS_CFG" );
    TsDialog<ISkoRecognizerCfg, IStridablesList<ISkObject>> d = new TsDialog<>( dlgInfo, aData, aObjects, creator );
    return d.execData();
  }

}
