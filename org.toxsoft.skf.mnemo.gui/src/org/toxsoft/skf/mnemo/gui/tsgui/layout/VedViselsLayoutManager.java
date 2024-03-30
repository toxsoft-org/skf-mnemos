package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;

/**
 * Реализация {@link IVedViselsLayoutManager}.
 *
 * @author vs
 */
public class VedViselsLayoutManager
    implements IVedViselsLayoutManager {

  private static final String PARAMID_LAYOUT_KIND = "layoutKind"; //$NON-NLS-1$

  private static final String PARAMID_LAYOUT_CONFIG = "layoutConfig"; //$NON-NLS-1$

  class LayoutActionsProvider
      extends MethodPerActionTsActionSetProvider {

    /**
     * ID of action {@link #ACDEF_XY_LAYOUT}.
     */
    public static final String ACTID_XY_LAYOUT = "ved.layout.xy"; //$NON-NLS-1$

    /**
     * ID of action {@link #ACDEF_DO_LAYOUT}.
     */
    public static final String ACTID_DO_LAYOUT = "ved.layout.do"; //$NON-NLS-1$

    /**
     * ID of action {@link #ACDEF_LAYOUT_SETTINGS}.
     */
    public static final String ACTID_LAYOUT_SETTINGS = "ved.layout.settings"; //$NON-NLS-1$

    /**
     * Action: copy selected visels and associated actors to the internal buffer.
     */
    public static final ITsActionDef ACDEF_XY_LAYOUT = ofRadio2( ACTID_XY_LAYOUT, //
        "Произвольно", "При размещении нет никаких правил", null );

    /**
     * Action: copy selected visels and associated actors to the internal buffer.
     */
    public static final ITsActionDef ACDEF_DO_LAYOUT = ofPush2( ACTID_DO_LAYOUT, //
        "Разместить", "Размещает подконтрольные элементы в соответствии с правилами", null );

    /**
     * Action: copy selected visels and associated actors to the internal buffer.
     */
    public static final ITsActionDef ACDEF_LAYOUT_SETTINGS = ofPush2( ACTID_LAYOUT_SETTINGS, //
        "Настроить...", "Вызывает диалог настройки параметров текущего контроллера размещений",
        ICONID_DOCUMENT_PROPERTIES );

    LayoutActionsProvider() {
      defineAction( ACDEF_XY_LAYOUT, VedViselsLayoutManager.this::removeLayoutController );
      defineSeparator();
      defineAction( ACDEF_LAYOUT_SETTINGS, VedViselsLayoutManager.this::tune );
      defineAction( ACDEF_DO_LAYOUT, VedViselsLayoutManager.this::layout );
    }

    @Override
    public boolean isActionChecked( String aActionId ) {
      if( aActionId.equals( ACTID_XY_LAYOUT ) ) {
        return layoutKindId.isBlank();
      }
      return false;
    }

    @Override
    protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
      if( targetVisel == null ) {
        return false;
      }
      if( aActionDef.id().equals( ACTID_DO_LAYOUT ) || aActionDef.id().equals( ACTID_LAYOUT_SETTINGS ) ) {
        return !layoutKindId.isBlank();
      }
      return true;
    }
  }

  private final IVedScreen vedScreen;

  private final IVedLayoutFactoriesProvider factProvider;

  private final IVedViselSelectionManager selectionManager;

  private final IVedViselsMasterSlaveRelationsManager msManager;

  private final LayoutActionsProvider actionsProvider;

  private final MenuCreatorFromAsp menuCreator;

  private VedAbstractVisel targetVisel = null;

  private String layoutKindId = TsLibUtils.EMPTY_STRING;

  public VedViselsLayoutManager( IVedScreen aVedScreen, IVedLayoutFactoriesProvider aFactProvider,
      IVedViselSelectionManager aSelManager, IVedViselsMasterSlaveRelationsManager aMsManager ) {
    vedScreen = aVedScreen;
    factProvider = aFactProvider;
    selectionManager = aSelManager;
    msManager = aMsManager;
    actionsProvider = new LayoutActionsProvider();
    menuCreator = new MenuCreatorFromAsp( actionsProvider, vedScreen.tsContext() );
    selectionManager.genericChangeEventer().addListener( aSource -> {
      targetVisel = selectedVisel();
      actionsProvider.actionsStateEventer().fireChangeEvent();
    } );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IVedViselsLayoutManager
  //

  @Override
  public void setLayoutController( String aViselId, IVedViselsLayoutController aLayoutController ) {
    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // IVedContextMenuCreator
  //

  @Override
  public boolean fillMenu( Menu aMenu, VedAbstractVisel aClickedVisel, ITsPoint aSwtCoors ) {
    if( aClickedVisel != null ) {
      targetVisel = aClickedVisel;
    }
    else {
      targetVisel = selectedVisel();
    }
    if( targetVisel == null ) {
      return false;
    }

    layoutKindId = getLayoutKindId( targetVisel );

    final MenuItem layoutItem = new MenuItem( aMenu, SWT.CASCADE );
    layoutItem.addDisposeListener( aE -> targetVisel = selectedVisel() );
    layoutItem.setText( "Размещение" );
    final Menu submenu = new Menu( vedScreen.getShell(), SWT.DROP_DOWN );

    for( IVedLayoutControllerFactory factory : factProvider.factories() ) {
      createMenuItem( submenu, factory );
    }

    layoutItem.setMenu( submenu );
    boolean result = menuCreator.fillMenu( submenu );

    String masterId = msManager.viselMasterId( targetVisel.id() );
    if( masterId != null ) {
      VedAbstractVisel masterVisel = VedScreenUtils.findVisel( masterId, vedScreen );
      String lki = getLayoutKindId( masterVisel );
      if( !lki.isBlank() ) {
        createEditViselLayoutDataMenuItem( submenu );
      }
    }

    return result;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  MenuItem createMenuItem( Menu aMenu, IVedLayoutControllerFactory aFactory ) {
    MenuItem mi = new MenuItem( aMenu, SWT.RADIO );
    mi.setText( aFactory.nmName() );
    mi.setData( aFactory.id() );
    mi.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        MenuItem m = (MenuItem)aE.widget;
        if( m.getSelection() ) {
          System.out.println( "Layout Selected: " + m.getData() );
          selectLayout( (String)m.getData() );
        }
      }
    } );

    mi.setSelection( aFactory.id().equals( layoutKindId ) );
    mi.setEnabled( targetVisel != null );
    return mi;
  }

  @SuppressWarnings( "unused" )
  MenuItem createEditViselLayoutDataMenuItem( Menu aMenu ) {
    new MenuItem( aMenu, SWT.SEPARATOR );
    MenuItem mi = new MenuItem( aMenu, SWT.PUSH );
    mi.setText( "Параметры размещения элемента..." );
    mi.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aE ) {
        TsDialogUtils.info( getShell(), "Настройка LayoutData" );
      }
    } );
    return mi;
  }

  void layout() {
    if( targetVisel != null && targetVisel.params().hasKey( PARAMID_LAYOUT_KIND ) ) {
      layoutKindId = targetVisel.params().getStr( PARAMID_LAYOUT_KIND );
      if( factProvider.factories().hasKey( layoutKindId ) ) {
        IVedLayoutControllerFactory f = factProvider.factories().getByKey( layoutKindId );
        IVedViselsLayoutController lc = f.create( null, vedScreen );
        lc.doLayout( targetVisel.id(), msManager.listSlaveViselIds( targetVisel.id() ) );
      }
    }
  }

  void tune() {
    String lkid = getLayoutKindId( targetVisel );
    if( !lkid.isBlank() ) {
      if( factProvider.factories().hasKey( layoutKindId ) ) {
        IVedLayoutControllerFactory f = factProvider.factories().getByKey( layoutKindId );
        f.editConfig( getLayoutConfig( targetVisel ), vedScreen );
      }
    }
  }

  void removeLayoutController() {
    layoutKindId = TsLibUtils.EMPTY_STRING;
    if( targetVisel != null ) {
      targetVisel.params().setStr( PARAMID_LAYOUT_KIND, TsLibUtils.EMPTY_STRING );
    }
  }

  private void selectLayout( String aKindId ) {
    layoutKindId = aKindId;
    if( targetVisel != null ) {
      targetVisel.params().setStr( PARAMID_LAYOUT_KIND, layoutKindId );
    }
    actionsProvider.actionsStateEventer().fireChangeEvent();
  }

  private VedAbstractVisel selectedVisel() {
    targetVisel = null;
    IStringList selIds = selectionManager.selectedViselIds();
    if( selIds.size() == 1 ) { // выделен строго один элемент
      targetVisel = VedScreenUtils.findVisel( selIds.first(), vedScreen );
    }
    return targetVisel;
  }

  private String getLayoutKindId( VedAbstractVisel aVisel ) {
    if( aVisel.params().hasKey( PARAMID_LAYOUT_KIND ) ) {
      return aVisel.params().getStr( PARAMID_LAYOUT_KIND );
    }
    return TsLibUtils.EMPTY_STRING;
  }

  private IVedLayoutControllerConfig getLayoutConfig( VedAbstractVisel aVisel ) {
    if( aVisel.params().hasKey( PARAMID_LAYOUT_CONFIG ) ) {
      return aVisel.params().getValobj( PARAMID_LAYOUT_CONFIG );
    }
    return null;
  }

}
