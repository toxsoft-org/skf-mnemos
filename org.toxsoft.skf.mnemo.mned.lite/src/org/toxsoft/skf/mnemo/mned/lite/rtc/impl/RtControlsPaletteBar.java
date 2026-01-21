package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Палитра RtControl'ей, поддерживающая категории.
 * <p>
 * Каждой категории соответствует кнопка типа DWROP_DOWN_MENU, где пунктам меню соотвестствуют элементы палитры,
 * входящие в данную категорию. Для каждого элемента палитры, не входящего ни в одну категорию, создается своя кнопка.
 *
 * @author vs
 */
public class RtControlsPaletteBar
    implements IRtControlsPalette, ITsGuiContextable, ITsSelectionChangeEventProducer<IRtControlsPaletteEntry> {

  private final IStridablesListEdit<IRtControlsPaletteEntry> entries = new StridablesList<>();

  private final IStridablesListEdit<IRtControlsPaletteCategory> categories = new StridablesList<>();

  private final IVedScreen vedScreen;

  private final EIconSize iconSize;

  ToolBar toolBar;

  private final IListEdit<ToolItem> toolItems = new ElemArrayList<>();

  private ToolItem selectedToolItem = null;

  private final TsSelectionChangeEventHelper<IRtControlsPaletteEntry> eventer;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aSwtStyle int - стиль
   * @param aVedScreen {@link VedScreen} - экран
   * @param aVertical boolean - признак расположения патитры (вертикально/горизонтально)
   * @param aIconSize {@link EIconSize} - размер значка
   */
  public RtControlsPaletteBar( Composite aParent, int aSwtStyle, IVedScreen aVedScreen, boolean aVertical,
      EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    vedScreen = aVedScreen;
    IRtControlFactoriesRegistry reg = aVedScreen.tsContext().get( IRtControlFactoriesRegistry.class );
    for( IRtControlFactory f : reg.items() ) {
      entries.addAll( f.paletteEntries() );
      RtControlsPaletteCategory pCat = getCategory( f );
      if( pCat != null ) {
        pCat.listEntries().addAll( f.paletteEntries() );
        categories.add( pCat );
      }
    }
    eventer = new TsSelectionChangeEventHelper<>( this );

    if( aIconSize == null ) {
      iconSize = hdpiService().getIconsSize( VED_EDITOR_PALETTE_ICON_SIZE_CATEGORY );
    }
    else {
      iconSize = aIconSize;
    }
    // paletteComp = new TsPanel( aParent, aVedScreen.tsContext(), aSwtStyle );
    // if( !aVertical ) {
    // paletteComp.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    // }
    // else {
    // paletteComp.setLayout( new RowLayout( SWT.VERTICAL ) );
    // }

    // ITsGuiContext ctx = new TsGuiContext( aVedScreen.tsContext() );
    // toolBar = new TsToolbar( ctx );
    // Control ctrl = toolBar.createControl( aParent );
    // toolBar.setVertical( true );
    // toolBar.setControl( paletteComp );

    toolBar = new ToolBar( aParent, SWT.VERTICAL | SWT.FLAT | SWT.SHADOW_OUT );

    initButtons();
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionChangeEventProducer
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<IRtControlsPaletteEntry> aListener ) {
    eventer.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<IRtControlsPaletteEntry> aListener ) {
    eventer.removeTsSelectionListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IRtControlsPalette
  //

  @Override
  public IStridablesList<IRtControlsPaletteEntry> listEntries() {
    return entries;
  }

  @Override
  public IStridablesList<IRtControlsPaletteCategory> listCategories() {
    return categories;
  }

  @Override
  public IRtControlsPaletteCategory defineCategory( IStridableParameterized aCategory ) {
    RtControlsPaletteCategory pCat = new RtControlsPaletteCategory( aCategory.id() );
    categories.add( pCat );
    return pCat;
  }

  @Override
  public void addEntry( IRtControlsPaletteEntry aEntry, String aCategoryId ) {
    RtControlsPaletteCategory pCat = (RtControlsPaletteCategory)categories.getByKey( aCategoryId );
    pCat.listEntries().add( aEntry );
  }

  @Override
  public Control getControl() {
    // return toolBar.getControl();
    return toolBar;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает выделенный (текущий) элемент палитры или <b>null</b>.<br>
   *
   * @return {@link IVedItemsPaletteEntry} - выделенный (текущий) элемент палитры или <b>null</b>
   */
  public IRtControlsPaletteEntry selectedItem() {
    if( selectedToolItem != null ) {
      return (IRtControlsPaletteEntry)selectedToolItem.getData();
    }
    return null;
  }

  /**
   * Возвращает курсор в зависимости от выделенного элемента.
   *
   * @return {@link Cursor} - курсор в зависимости от выделенного элемента
   */
  public Cursor cursor() {
    if( selectedToolItem != null ) {
      IRtControlsPaletteEntry pe = (IRtControlsPaletteEntry)selectedToolItem.getData();
      pe.iconId();
      if( cursorManager().findCursor( pe.id() ) == null ) {
        Image img = selectedToolItem.getImage();
        cursorManager().putCursor( pe.id(), new Cursor( getDisplay(), img.getImageData(), 0, 0 ) );
      }
      return cursorManager().findCursor( pe.id() );
    }
    return null;
  }

  /**
   * Делает все кнопки "не запавшими".
   */
  public void deselectAllButtons() {
    for( ToolItem ti : toolItems ) {
      ti.setBackground( null );
    }
    selectedToolItem = null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  RtControlsPaletteCategory getCategory( IRtControlFactory aFactory ) {
    String categoryId = TsLibUtils.EMPTY_STRING;
    if( aFactory.params().hasKey( PARAMID_CATEGORY ) ) {
      categoryId = aFactory.params().getStr( PARAMID_CATEGORY );
      if( !categories.hasKey( categoryId ) ) {
        RtControlsPaletteCategory pCat = new RtControlsPaletteCategory( categoryId );
        categories.add( pCat );
        return pCat;
      }
      return (RtControlsPaletteCategory)categories.getByKey( categoryId );
    }
    return null;
  }

  void initButtons() {
    for( IRtControlsPaletteCategory pCat : categories ) {
      createCategoryButton( pCat );
    }
  }

  void createEntityButton( IRtControlsPaletteEntry aEntry ) {
    // ITsActionDef ad = TsActionDef.ofMenu2( CATID_GEOMETRY, "Фигуры", "Геометрические фигуры", ICONID_RTC_ELLIPSE );
    // TsAction a = new TsAction( ad, iconSize, vedScreen.tsContext() );
    // toolBar.addAction( null );

    // Button btn = new Button( paletteComp, SWT.FLAT | SWT.TOGGLE );
    // btn.setData( aEntry );
    // // buttons.add( btn );
    //
    // Image image = iconManager().loadStdIcon( aEntry.iconId(), iconSize );
    //
    // btn.setImage( image );
    // btn.setToolTipText( aEntry.nmName() + '\n' + aEntry.description() );
  }

  void createCategoryButton( IRtControlsPaletteCategory aCategory ) {
    ToolItem ti = new ToolItem( toolBar, SWT.DROP_DOWN );
    toolItems.add( ti );
    IRtControlsPaletteEntry pe = aCategory.listEntries().first();
    Image image = iconManager().loadStdIcon( pe.iconId(), iconSize );

    ti.setImage( image );
    ti.setData( pe );

    Menu menu = new Menu( toolBar.getShell(), SWT.POP_UP );

    for( IRtControlsPaletteEntry pEntry : aCategory.listEntries() ) {
      MenuItem mi = new MenuItem( menu, SWT.RADIO );
      mi.setText( pEntry.nmName() );
      Image img = iconManager().loadStdIcon( pEntry.iconId(), iconSize );
      mi.setImage( img );
      mi.setData( pEntry );
      mi.addSelectionListener( new SelectionAdapter() {

        @Override
        public void widgetSelected( SelectionEvent aE ) {
          IRtControlsPaletteEntry pent = (IRtControlsPaletteEntry)mi.getData();
          ti.setData( pent );
          ti.setImage( iconManager().loadStdIcon( pent.iconId(), iconSize ) );
        }
      } );
    }

    ti.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        selectedToolItem = ti;
        if( aEvent.detail == SWT.ARROW ) {
          Rectangle bounds = ti.getBounds();
          Point point = toolBar.toDisplay( bounds.x, bounds.y + bounds.height );
          menu.setLocation( point );
          menu.setVisible( true );
        }
        else {
          if( selectedToolItem != null ) {
            selectedToolItem.setBackground( null );
          }
          selectedToolItem = ti;
          ti.setBackground( getDisplay().getSystemColor( SWT.COLOR_LIST_SELECTION ) );
          eventer.fireTsSelectionEvent( (IRtControlsPaletteEntry)selectedToolItem.getData() );
        }
      }

    } );

  }

}
