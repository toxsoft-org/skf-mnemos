package org.toxsoft.skf.mnemo.gui.tsgui;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Менеджер копирования/вставки визуальных элементов.
 *
 * @author vs
 */
public class VedViselsEditManager
    implements IVedContextMenuCreator, ITsGuiContextable {

  private IVedScreen vedScreen;

  private IStringList viselIds;

  class EditActionProvider
      extends MethodPerActionTsActionSetProvider {

    /**
     * ID of action {@link #ACDEF_EDIT}.
     */
    public static final String ACTID_EDIT = "ved.edit"; //$NON-NLS-1$

    /**
     * Action: copy selected visels and associated actors to the internal buffer.
     */
    public static final ITsActionDef ACDEF_EDIT = ofPush2( ACTID_EDIT, //
        "Изменить", "Вызывает диалог изменения свойств выделенных визуальных элементов", ICONID_DOCUMENT_EDIT );

    EditActionProvider() {
      defineAction( ACDEF_EDIT, VedViselsEditManager.this::edit );
    }

    @Override
    protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
      if( clickedVisel == null && selectionManager.selectedViselIds().size() <= 0 ) {
        return false;
      }
      return true;
    }

  }

  TinWidget     tinWidget;
  ITinFieldInfo selectedFieldInfo  = null;
  ITinFieldInfo propValueFieldInfo = null;
  TinRow        selectedRow        = null;
  ITinTypeInfo  initialTypeInfo    = null;
  ITinValue     initialValue       = ITinValue.NULL;

  IStringMapEdit<IOptionSet> viselsProps = new StringMap<>();

  IValedControlValueChangeListener valedListener = ( aValed, aEditFinished ) -> {
    // if( !aEditFinished ) { // редактирование в процессе
    if( aValed.canGetValue() == ValidationResult.SUCCESS ) {
      Object obj = aValed.getValue();
      if( !(obj instanceof IAtomicValue) ) {
        obj = AvUtils.avValobj( obj );
      }
      if( obj instanceof IAtomicValue av ) {
        selectedRow.setAtomicValueFromValed( av );
        ITinValue tv = tinWidget.getValue().childValues().getByKey( propValueFieldInfo.id() );
        IAtomicValue newAv;
        if( tv.kind() == ETinTypeKind.ATOMIC ) {
          newAv = tv.atomicValue();
        }
        else {
          newAv = propValueFieldInfo.typeInfo().compose( tv.childValues() );
        }

        for( String viselId : viselIds ) { // для всех visel'ей
          IOptionSetEdit opSet = new OptionSet();
          opSet.setValue( propValueFieldInfo.id(), newAv );
          VedAbstractVisel visel = VedScreenUtils.findVisel( viselId, vedScreen );
          visel.props().setProps( opSet ); // установим новые значения свойств
        }
      }
    }
  };

  class EditorWindow {

    private final Shell wnd;

    private final IStringMapEdit<IAtomicValue> valuesMap = new StridMap<>();

    public EditorWindow( Shell aParent, IStringList aSelectedIds ) {
      viselIds = aSelectedIds;
      wnd = new Shell( aParent, SWT.BORDER | SWT.SHELL_TRIM );
      wnd.setText( "Изменение свойств" );
      BorderLayout bl = new BorderLayout();
      bl.setMargins( 4, 4, 8, 8 );
      wnd.setLayout( bl );
      ITsGuiContext ctx = new TsGuiContext( tsContext() );
      ctx.put( IValedControlValueChangeListener.class, valedListener );
      tinWidget = new TinWidget( ctx );

      Control tinControl = tinWidget.createControl( wnd );
      tinControl.setLayoutData( BorderLayout.CENTER );
      // tinWidget.addPropertyChangeListener( ( aSource, aChangedPropId ) -> {
      // ITinValue tv = tinWidget.getValue();
      // IAtomicValue av = tv.childValues().getByKey( aChangedPropId ).atomicValue();
      // valuesMap.put( aChangedPropId, av );
      // } );

      IStridablesListEdit<IVedItem> visels = new StridablesList<>();
      for( String id : viselIds ) {
        IVedVisel visel = VedScreenUtils.findVisel( id, vedScreen );
        visels.add( visel );
        viselsProps.put( id, new OptionSet( visel.props() ) );
      }
      initialTypeInfo = TinUtils.createGroupTinTypeInfo( vedScreen, visels );
      tinWidget.setEntityInfo( initialTypeInfo );
      initialValue = TinUtils.createGroupTinValue( vedScreen, visels );
      tinWidget.setValue( initialValue );

      tinWidget.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
        selectedRow = (TinRow)tinWidget.selectedItem();
        selectedFieldInfo = selectedRow.fieldInfo();
        TinRow tinRow = selectedRow;
        while( !(tinRow.parent() instanceof TinTopRow) ) {
          tinRow = (TinRow)tinRow.parent();
        }
        propValueFieldInfo = tinRow.fieldInfo();
        System.out.println( "Selected field: " + selectedFieldInfo.id() );
        System.out.println( "PropValue field: " + propValueFieldInfo.id() );
      } );

      Composite buttonsComp = new Composite( wnd, SWT.NONE );
      buttonsComp.setLayoutData( BorderLayout.SOUTH );
      buttonsComp.setLayout( new GridLayout( 2, false ) );

      Button btnRestore = new Button( buttonsComp, SWT.PUSH );
      btnRestore.setText( "Restore" );
      GridData gd = new GridData( SWT.RIGHT, SWT.TOP, true, false );
      gd.widthHint = 100;
      gd.heightHint = 32;
      btnRestore.setLayoutData( gd );
      btnRestore.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          ITinValue currValue = tinWidget.getValue();
          IStringListEdit changedPropIds = new StringArrayList();
          for( ITinFieldInfo fi : initialTypeInfo.fieldInfos() ) {
            ITinValue tv = initialValue.childValues().getByKey( fi.id() );
            if( !tv.equals( currValue.childValues().getByKey( fi.id() ) ) ) {
              changedPropIds.add( fi.id() );
            }
          }

          for( String viselId : viselIds ) { // для всех visel'ей
            IOptionSetEdit opSet = new OptionSet();
            IOptionSet oldSet = viselsProps.getByKey( viselId );
            for( String propId : changedPropIds ) { // для всех измененных свойств
              opSet.setValue( propId, oldSet.getByKey( propId ) );
            }
            VedAbstractVisel visel = VedScreenUtils.findVisel( viselId, vedScreen );
            visel.props().setProps( opSet ); // установим новые значения свойств
          }
          wnd.close();
        }
      } );

      Button btnClose = new Button( buttonsComp, SWT.PUSH );
      btnClose.setText( "Close" );
      gd = new GridData( SWT.LEFT, SWT.TOP, false, false );
      gd.widthHint = 100;
      gd.heightHint = 32;
      btnClose.setLayoutData( gd );
      btnClose.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aEvent ) {
          wnd.close();
        }
      } );
    }

    void open() {
      wnd.setSize( 600, 800 );
      Rectangle r = getDisplay().getClientArea();
      int x = r.x + r.width - 800;
      int y = r.y + 200;
      wnd.setLocation( x, y );
      wnd.open();
    }

  }

  private final IStridablesListEdit<ICopyPasteProcessor> processors = new StridablesList<>();

  private final EditActionProvider actionsProvider;

  private final MenuCreatorFromAsp menuCreator;

  private VedAbstractVisel clickedVisel = null;

  private final IVedViselSelectionManager selectionManager;

  /**
   * Конструктор.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактирования
   */
  public VedViselsEditManager( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager ) {
    vedScreen = aVedScreen;
    selectionManager = aSelectionManager;
    actionsProvider = new EditActionProvider();
    menuCreator = new MenuCreatorFromAsp( actionsProvider, vedScreen.tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IVedContextMenuCreator
  //

  @Override
  public boolean fillMenu( Menu aMenu, VedAbstractVisel aClickedVisel, ITsPoint aSwtCoors ) {
    clickedVisel = aClickedVisel;
    return menuCreator.fillMenu( aMenu );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает поставщик действий.
   *
   * @return ITsActionSetProvider - поставщик действий
   */
  public ITsActionSetProvider actionsProvides() {
    return actionsProvider;
  }

  /**
   * Добавляет процессор.<br>
   * Если такой процессор уже есть, то ничего не делает.
   *
   * @param aProcessor {@link ICopyPasteProcessor} - процессор обрабатывающий информацию при копировании/вставке
   */
  public void addProcessor( ICopyPasteProcessor aProcessor ) {
    if( !processors.hasKey( aProcessor.id() ) ) {
      processors.add( aProcessor );
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void edit() {
    IStringList viselIds = selectionManager.selectedViselIds();
    if( viselIds.isEmpty() ) {
      viselIds = new StringArrayList( clickedVisel.id() );
    }
    EditorWindow w = new EditorWindow( getShell(), viselIds );
    w.open();
  }

}
