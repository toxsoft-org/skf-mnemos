package org.toxsoft.skf.mnemo.gui.tsgui;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.cond.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.generic.*;
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
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Менеджер копирования/вставки визуальных элементов.
 *
 * @author vs
 */
public class VedViselsEditManager
    implements IVedContextMenuCreator, ITsGuiContextable {

  private IVedScreen vedScreen;

  private IStringList viselIds;

  static class ActorSelectionPanel
      extends AbstractGenericEntityEditPanel<IVedActorFactory> {

    ComboViewer combo;

    IStringList factoryIds;

    public ActorSelectionPanel( IStringList aActorIds, ITsGuiContext aContext ) {
      super( aContext, true );
      factoryIds = aActorIds;
    }

    @Override
    protected IVedActorFactory doGetEntity() {
      IStructuredSelection sel = (IStructuredSelection)combo.getSelection();
      if( !sel.isEmpty() ) {
        return (IVedActorFactory)sel.getFirstElement();
      }
      return null;
    }

    @Override
    protected void doProcessSetEntity() {
      // TODO Auto-generated method stub
    }

    @Override
    protected Control doCreateControl( Composite aParent ) {
      Composite bkPanel = new Composite( aParent, SWT.NONE );
      bkPanel.setLayout( new GridLayout( 2, false ) );
      CLabel l = new CLabel( bkPanel, SWT.CENTER );
      l.setText( "Actor: " );
      combo = new ComboViewer( bkPanel, SWT.DROP_DOWN );
      combo.setContentProvider( new ArrayContentProvider() );
      combo.setLabelProvider( new LabelProvider() {

        @Override
        public String getText( Object aElement ) {
          IVedActorFactory factory = (IVedActorFactory)aElement;
          return factory.nmName();
        }
      } );

      IVedActorFactoriesRegistry fr = tsContext().get( IVedActorFactoriesRegistry.class );
      IStridablesListEdit<IVedActorFactory> factList = new StridablesList<>();
      for( String fId : factoryIds ) {
        factList.add( fr.get( fId ) );
      }
      combo.setInput( factList.toArray() );
      return bkPanel;
    }

    // ------------------------------------------------------------------------------------
    // API
    //

    /**
     * Invokes dialog with {@link IPanelSingleCondInfo} for {@link ITsSingleCondInfo} editing.
     *
     * @param aDialogInfo {@link ITsDialogInfo} - the dialog window parameters
     * @param aFactIds {@link IStringList} - ИДы фабрик акторов
     * @return {@link ITsSingleCondInfo} - edited value or <code>null</code>
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     */
    public static IVedActorFactory editDialog( ITsDialogInfo aDialogInfo, IStringList aFactIds ) {
      TsNullArgumentRtException.checkNulls( aDialogInfo, aFactIds );
      IGenericEntityEditPanelCreator<IVedActorFactory> panelCreator =
          ( aContext, aViewer ) -> new ActorSelectionPanel( aFactIds, aContext );

      IDialogPanelCreator<IVedActorFactory, IStringList> creator =
          ( aParent, aOwnerDialog ) -> new TsDialogGenericEntityEditPanel<>( aParent, aOwnerDialog, panelCreator );
      TsDialog<IVedActorFactory, IStringList> d = new TsDialog<>( aDialogInfo, null, aFactIds, creator );
      return d.execData();
    }

  }

  class EditActionProvider
      extends MethodPerActionTsActionSetProvider {

    /**
     * ID of action {@link #ACDEF_VISELS_EDIT}.
     */
    public static final String ACTID_VISELS_EDIT = "ved.visels.edit"; //$NON-NLS-1$

    /**
     * Action: edit selected visels.
     */
    public static final ITsActionDef ACDEF_VISELS_EDIT = ofPush2( ACTID_VISELS_EDIT, //
        "Визуальные элементы", "Вызывает диалог изменения свойств выделенных визуальных элементов", ICONID_VED_VISEL );

    /**
     * ID of action {@link #ACDEF_VISELS_EDIT}.
     */
    public static final String ACTID_ACTORS_EDIT = "ved.actors.edit"; //$NON-NLS-1$

    /**
     * Action: edit selected actors.
     */
    public static final ITsActionDef ACDEF_ACTORS_EDIT = ofPush2( ACTID_ACTORS_EDIT, //
        "Акторы", "Вызывает диалог изменения свойств акторов выделенных визуальных элементов", ICONID_VED_ACTOR );

    EditActionProvider() {
      defineAction( ACDEF_VISELS_EDIT, VedViselsEditManager.this::edit );
      defineAction( ACDEF_ACTORS_EDIT, VedViselsEditManager.this::editActors );
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
          if( visel != null ) {
            visel.props().setProps( opSet ); // установим новые значения свойств
          }
          else {
            VedAbstractItem actor = vedScreen.model().actors().list().findByKey( viselId );
            actor.props().setProps( opSet ); // установим новые значения свойств
          }
        }
      }
    }
  };

  class EditorWindow {

    private final Shell wnd;

    private final IStringMapEdit<IAtomicValue> valuesMap = new StridMap<>();

    public EditorWindow( Shell aParent, IStridablesList<? extends IVedItem> aItems ) {
      viselIds = aItems.ids();
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

      // IStridablesListEdit<IVedItem> visels = new StridablesList<>();
      for( IVedItem item : aItems ) {
        viselsProps.put( item.id(), new OptionSet( item.props() ) );
      }
      initialTypeInfo = TinUtils.createGroupTinTypeInfo( vedScreen, aItems );
      tinWidget.setEntityInfo( initialTypeInfo );
      initialValue = TinUtils.createGroupTinValue( vedScreen, aItems );
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

  // class EditorWindow {
  //
  // private final Shell wnd;
  //
  // private final IStringMapEdit<IAtomicValue> valuesMap = new StridMap<>();
  //
  // public EditorWindow( Shell aParent, IStringList aSelectedIds ) {
  // viselIds = aSelectedIds;
  // wnd = new Shell( aParent, SWT.BORDER | SWT.SHELL_TRIM );
  // wnd.setText( "Изменение свойств" );
  // BorderLayout bl = new BorderLayout();
  // bl.setMargins( 4, 4, 8, 8 );
  // wnd.setLayout( bl );
  // ITsGuiContext ctx = new TsGuiContext( tsContext() );
  // ctx.put( IValedControlValueChangeListener.class, valedListener );
  // tinWidget = new TinWidget( ctx );
  //
  // Control tinControl = tinWidget.createControl( wnd );
  // tinControl.setLayoutData( BorderLayout.CENTER );
  // // tinWidget.addPropertyChangeListener( ( aSource, aChangedPropId ) -> {
  // // ITinValue tv = tinWidget.getValue();
  // // IAtomicValue av = tv.childValues().getByKey( aChangedPropId ).atomicValue();
  // // valuesMap.put( aChangedPropId, av );
  // // } );
  //
  // IStridablesListEdit<IVedItem> visels = new StridablesList<>();
  // for( String id : viselIds ) {
  // IVedVisel visel = VedScreenUtils.findVisel( id, vedScreen );
  // visels.add( visel );
  // viselsProps.put( id, new OptionSet( visel.props() ) );
  // }
  // initialTypeInfo = TinUtils.createGroupTinTypeInfo( vedScreen, visels );
  // tinWidget.setEntityInfo( initialTypeInfo );
  // initialValue = TinUtils.createGroupTinValue( vedScreen, visels );
  // tinWidget.setValue( initialValue );
  //
  // tinWidget.addTsSelectionListener( ( aSource, aSelectedItem ) -> {
  // selectedRow = (TinRow)tinWidget.selectedItem();
  // selectedFieldInfo = selectedRow.fieldInfo();
  // TinRow tinRow = selectedRow;
  // while( !(tinRow.parent() instanceof TinTopRow) ) {
  // tinRow = (TinRow)tinRow.parent();
  // }
  // propValueFieldInfo = tinRow.fieldInfo();
  // System.out.println( "Selected field: " + selectedFieldInfo.id() );
  // System.out.println( "PropValue field: " + propValueFieldInfo.id() );
  // } );
  //
  // Composite buttonsComp = new Composite( wnd, SWT.NONE );
  // buttonsComp.setLayoutData( BorderLayout.SOUTH );
  // buttonsComp.setLayout( new GridLayout( 2, false ) );
  //
  // Button btnRestore = new Button( buttonsComp, SWT.PUSH );
  // btnRestore.setText( "Restore" );
  // GridData gd = new GridData( SWT.RIGHT, SWT.TOP, true, false );
  // gd.widthHint = 100;
  // gd.heightHint = 32;
  // btnRestore.setLayoutData( gd );
  // btnRestore.addSelectionListener( new SelectionAdapter() {
  //
  // @Override
  // public void widgetSelected( SelectionEvent aEvent ) {
  // ITinValue currValue = tinWidget.getValue();
  // IStringListEdit changedPropIds = new StringArrayList();
  // for( ITinFieldInfo fi : initialTypeInfo.fieldInfos() ) {
  // ITinValue tv = initialValue.childValues().getByKey( fi.id() );
  // if( !tv.equals( currValue.childValues().getByKey( fi.id() ) ) ) {
  // changedPropIds.add( fi.id() );
  // }
  // }
  //
  // for( String viselId : viselIds ) { // для всех visel'ей
  // IOptionSetEdit opSet = new OptionSet();
  // IOptionSet oldSet = viselsProps.getByKey( viselId );
  // for( String propId : changedPropIds ) { // для всех измененных свойств
  // opSet.setValue( propId, oldSet.getByKey( propId ) );
  // }
  // VedAbstractVisel visel = VedScreenUtils.findVisel( viselId, vedScreen );
  // visel.props().setProps( opSet ); // установим новые значения свойств
  // }
  // wnd.close();
  // }
  // } );
  //
  // Button btnClose = new Button( buttonsComp, SWT.PUSH );
  // btnClose.setText( "Close" );
  // gd = new GridData( SWT.LEFT, SWT.TOP, false, false );
  // gd.widthHint = 100;
  // gd.heightHint = 32;
  // btnClose.setLayoutData( gd );
  // btnClose.addSelectionListener( new SelectionAdapter() {
  //
  // @Override
  // public void widgetSelected( SelectionEvent aEvent ) {
  // wnd.close();
  // }
  // } );
  // }
  //
  // void open() {
  // wnd.setSize( 600, 800 );
  // Rectangle r = getDisplay().getClientArea();
  // int x = r.x + r.width - 800;
  // int y = r.y + 200;
  // wnd.setLocation( x, y );
  // wnd.open();
  // }
  //
  // }

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

    final MenuItem editItem = new MenuItem( aMenu, SWT.CASCADE );
    editItem.setText( "Изменить" );
    editItem.setImage( iconManager().loadStdIcon( ICONID_DOCUMENT_EDIT, EIconSize.IS_16X16 ) );
    final Menu submenu = new Menu( vedScreen.getShell(), SWT.DROP_DOWN );

    editItem.setMenu( submenu );
    return menuCreator.fillMenu( submenu );

    // return menuCreator.fillMenu( aMenu );
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
    IStringList selViselIds = selectionManager.selectedViselIds();
    if( selViselIds.isEmpty() ) {
      selViselIds = new StringArrayList( clickedVisel.id() );
    }
    IStridablesList<? extends IVedItem> visels = VedScreenUtils.listVisels( selViselIds, vedScreen );
    EditorWindow w = new EditorWindow( getShell(), visels );
    w.open();
  }

  void editActors() {
    IStringList selViselIds = selectionManager.selectedViselIds();
    if( selViselIds.isEmpty() ) {
      selViselIds = new StringArrayList( clickedVisel.id() );
    }
    IStringList factIds = VedScreenUtils.listSimilarActorsFactoryIds( selViselIds, vedScreen );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.put( IVedScreen.class, vedScreen );
    ITsDialogInfo dlgInfo = new TsDialogInfo( ctx, "Выбор актора", "Выберите соответствующий актор и нажмите  ОК." );
    IVedActorFactory aFact = ActorSelectionPanel.editDialog( dlgInfo, factIds );
    if( aFact != null ) {
      IStringListEdit actorIds = new StringArrayList();
      for( String viselId : selViselIds ) {
        for( IVedActor a : VedScreenUtils.viselActors( viselId, vedScreen ) ) {
          if( a.factoryId().equals( aFact.id() ) ) {
            actorIds.add( a.id() );
          }
        }
      }
      EditorWindow w = new EditorWindow( getShell(), VedScreenUtils.listActors( actorIds, vedScreen ) );
      w.open();
    }
  }

}
