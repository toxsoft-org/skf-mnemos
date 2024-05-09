package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.ConfigRecognizerPanel.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.gui.conn.*;

public class ConfigRecognizerPanel
    extends AbstractTsDialogPanel<ISkoRecognizerCfg, PanelCtx> {

  static class PanelCtx {

    ITsGuiContext tsContext;
    Skid          objSkid;

    PanelCtx( Skid aObjSkid, ITsGuiContext aTsContext ) {
      objSkid = aObjSkid;
      tsContext = aTsContext;
    }
  }

  protected ConfigRecognizerPanel( Composite aParent, TsDialog<ISkoRecognizerCfg, PanelCtx> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected ConfigRecognizerPanel( Composite aParent, ISkoRecognizerCfg aData, PanelCtx aEnviron, int aFlags ) {
    super( aParent, aEnviron.tsContext, aData, aEnviron, aFlags );
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
        ISkoRecognizerCfg cfg = panel.config();
        return cfg;
      }
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private ValedEnumCombo<ESkoRecognizerKind> kindCombo;

  Composite stackPanel;

  StackLayout stackLayout;

  ISkCoreApi coreApi;

  private final IMapEdit<String, ISkoRecognizerCfgPanel> panels = new ElemMap<>();

  void init() {
    coreApi = environ().tsContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
    setLayout( new BorderLayout() );

    Composite topPanel = new Composite( this, SWT.NONE );
    topPanel.setLayout( new GridLayout( 2, false ) );

    CLabel l;

    l = new CLabel( topPanel, SWT.NONE );
    l.setText( "Тип распознавателя: " );
    kindCombo = new ValedEnumCombo<>( environ().tsContext, ESkoRecognizerKind.class, IStridable::nmName );
    kindCombo.createControl( topPanel );
    kindCombo.getControl().addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        if( kindCombo.canGetValue() == ValidationResult.SUCCESS ) {
          ESkoRecognizerKind kind = kindCombo.getValue();
          switch( kind ) {
            case ATTR:
              ISkoRecognizerCfgPanel cfgP;
              if( !panels.hasKey( kind.id() ) ) {
                cfgP = ByAttrValueRecognizer.FACTORY.createCfgEditPanel( stackPanel, environ().objSkid, coreApi );
                panels.put( kind.id(), cfgP );
              }
              cfgP = panels.getByKey( kind.id() );
              stackLayout.topControl = cfgP.getControl();
              stackPanel.layout();
              break;
            case LINK:
              break;
            case RRI_ATTR:
              break;
            case RRI_LINK:
              break;
            default:
              throw new IllegalArgumentException( "Unexpected value: " + kind );
          }
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
   * @param aContext PanelCtx - соответствующий контекст панели
   * @return {@link ISkoRecognizerCfg} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final ISkoRecognizerCfg edit( ISkoRecognizerCfg aData, PanelCtx aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    IDialogPanelCreator<ISkoRecognizerCfg, PanelCtx> creator = ConfigRecognizerPanel::new;
    ITsDialogInfo dlgInfo = new TsDialogInfo( aContext.tsContext, "\"Распознаватель\" объектов", "STR_MSG_CANVAS_CFG" );
    TsDialog<ISkoRecognizerCfg, PanelCtx> d = new TsDialog<>( dlgInfo, aData, aContext, creator );
    return d.execData();
  }

}
