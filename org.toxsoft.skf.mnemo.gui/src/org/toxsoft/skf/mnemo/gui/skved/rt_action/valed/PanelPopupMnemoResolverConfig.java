package org.toxsoft.skf.mnemo.gui.skved.rt_action.valed;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * Панель создания/редактирования конфигурации составного "разрешителя" для объекта являющимся мастером для дочерней
 * мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class PanelPopupMnemoResolverConfig
    extends AbstractTsDialogPanel<PopupMnemoResolverConfig, ITsGuiContext> {
  // implements ISkGuiContextable {

  protected PanelPopupMnemoResolverConfig( Composite aParent,
      TsDialog<PopupMnemoResolverConfig, ITsGuiContext> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected PanelPopupMnemoResolverConfig( Composite aParent, PopupMnemoResolverConfig aData, ITsGuiContext aTsContext,
      int aFlags ) {
    super( aParent, aTsContext, aData, aTsContext, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( PopupMnemoResolverConfig aData ) {
    if( aData != null ) {
      masterClassId = aData.masterClassId();
      masterObjId = aData.masterObjId();
      resolverCfg = aData.resolverConfig();
      if( !masterObjId.isBlank() ) {
        btnDirectObject.setSelection( true );
      }
      else {
        btnPath2Object.setSelection( true );
      }
    }
  }

  @Override
  protected PopupMnemoResolverConfig doGetDataRecord() {
    if( btnDirectObject.getSelection() ) {
      return new PopupMnemoResolverConfig( masterClassId, masterObjId );
    }
    return new PopupMnemoResolverConfig( masterClassId, resolverCfg );
  }

  @Override
  protected ValidationResult doValidate() {
    if( masterClassId.isBlank() ) {
      return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать класс мастер-объекта" );
    }
    if( btnDirectObject.getSelection() ) {
      if( masterObjId.isBlank() ) {
        return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать мастер-объект" );
      }
    }
    else {
      if( resolverCfg == CompoundResolverConfig.NONE ) {
        return ValidationResult.create( EValidationResultType.ERROR, "Необходимо задать путь к мастер-объекту" );
      }
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MasterPathViewer viewer;
  CLabel           fldMasterClass;
  CLabel           fldDirectObject;
  CLabel           fldMasterPath;
  Button           btnDirectObject;
  Button           btnPath2Object;

  String                  masterClassId = TsLibUtils.EMPTY_STRING;
  String                  masterObjId   = TsLibUtils.EMPTY_STRING;
  ICompoundResolverConfig resolverCfg   = CompoundResolverConfig.NONE;

  void init() {
    setLayout( new GridLayout( 3, false ) );

    CLabel l = new CLabel( this, SWT.CENTER );
    l.setText( "Класс мастер-объекта:" );

    fldMasterClass = new CLabel( this, SWT.BORDER );
    fldMasterClass.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );

    Button btnSelectMaster = new Button( this, SWT.PUSH );
    btnSelectMaster.setImage( iconManager().loadStdIcon( ICONID_DOCUMENT_EDIT, EIconSize.IS_16X16 ) );
    btnSelectMaster.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        ISkConnection skConn = tsContext().get( ISkConnection.class );
        ISkClassInfo clsInfo = SkGuiUtils.selectClass( null, skConn, tsContext() );
        if( clsInfo != null ) {
          masterClassId = clsInfo.id();
          if( !clsInfo.nmName().isBlank() ) {
            fldMasterClass.setText( clsInfo.nmName() );
          }
          else {
            fldMasterClass.setText( clsInfo.id() );
          }
        }
        fireContentChangeEvent();
      }
    } );

    btnDirectObject = new Button( this, SWT.RADIO );
    btnDirectObject.setText( "Конкретный объект: " );
    btnDirectObject.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        fireContentChangeEvent();
      }
    } );
    fldDirectObject = new CLabel( this, SWT.BORDER );
    fldDirectObject.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );

    Button btnSelectObject = new Button( this, SWT.PUSH );
    btnSelectObject.setImage( iconManager().loadStdIcon( ICONID_DOCUMENT_EDIT, EIconSize.IS_16X16 ) );
    btnSelectObject.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        if( masterClassId.isBlank() ) {
          TsDialogUtils.warn( getShell(), "Сначала необходимо выбрать класс мастер-объекта" );
          return;
        }
        ISkConnection skConn = tsContext().get( ISkConnection.class );
        ISkObject skObj = SkGuiUtils.selectObject( masterClassId, skConn, tsContext() );
        if( skObj != null ) {
          fldDirectObject.setText( skObj.readableName() );
          masterObjId = skObj.id();
        }
        fireContentChangeEvent();
      }
    } );

    btnPath2Object = new Button( this, SWT.RADIO );
    btnPath2Object.setText( "Путь к объекту: " );
    btnPath2Object.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        fireContentChangeEvent();
      }
    } );

    fldMasterPath = new CLabel( this, SWT.BORDER );
    fldMasterPath.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false, 1, 1 ) );
    Button btnSelectPath = new Button( this, SWT.PUSH );
    btnSelectPath.setImage( iconManager().loadStdIcon( ICONID_DOCUMENT_EDIT, EIconSize.IS_16X16 ) );
    btnSelectPath.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        if( masterClassId.isBlank() ) {
          TsDialogUtils.warn( getShell(), "Сначала необходимо выбрать класс мастер-объекта" );
          return;
        }
        ICompoundResolverConfig resCfg = PanelCompoundResolverConfig.edit( null, masterClassId, tsContext() );
        if( resCfg != null ) {
          resolverCfg = resCfg;
          fldMasterPath.setText( resolverCfg.toString() );
        }
        fireContentChangeEvent();
      }
    } );

  }

  // ------------------------------------------------------------------------------------
  // Invocation
  //

  /**
   * Статический метод вызова диалога редактирования параметров разрешителя мастер-объекта для popup мнемосхемы.
   *
   * @param aData Pair<SubmasterConfig,String> - параметры разрешителя мастер-объекта для popup мнемосхемы
   * @param aTsContext ITsGuiContext - соответствующий контекст
   * @return {@link VedTableLayoutControllerConfig} - новая отредактированнная конфигурация или <b>null</br>
   */
  public static final PopupMnemoResolverConfig edit( PopupMnemoResolverConfig aData, ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    IDialogPanelCreator<PopupMnemoResolverConfig, ITsGuiContext> creator = PanelPopupMnemoResolverConfig::new;
    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( aTsContext, "DLG_T_SELECT_MASTER_PATH", "STR_MSG_SELECT_MASTER_PATH" );
    TsDialog<PopupMnemoResolverConfig, ITsGuiContext> d = new TsDialog<>( dlgInfo, aData, aTsContext, creator );
    return d.execData();
  }

}
