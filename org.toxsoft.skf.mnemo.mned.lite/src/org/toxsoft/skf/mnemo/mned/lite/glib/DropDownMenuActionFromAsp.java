package org.toxsoft.skf.mnemo.mned.lite.glib;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;

/**
 * {@link TsAction} типа "Выпадающее меню", которое формируется из переданного {@link ITsActionSetProvider}'a. При этом,
 * выбранное из меню действие, становится действием "по-умолчанию", которое вызывается при нажатии на кнопку.
 * <p>
 *
 * @author vs
 */
public class DropDownMenuActionFromAsp
    extends TsAction
    implements ITsGuiContextable {

  // private final ITsActionSetProvider asp;

  class InternalMenuCreator
      implements IMenuCreator {

    private final MenuCreatorFromAsp aspMenuCreator;

    InternalMenuCreator( MenuCreatorFromAsp aMenuCreator ) {
      aspMenuCreator = aMenuCreator;
    }

    @Override
    public void dispose() {
      aspMenuCreator.dispose();
    }

    @Override
    public Menu getMenu( Control aParent ) {
      Menu menu = new Menu( aParent );
      internalFillMenu( menu );
      return menu;
    }

    @Override
    public Menu getMenu( Menu aParent ) {
      Menu menu = new Menu( aParent );
      internalFillMenu( menu );
      return menu;
    }

    // ------------------------------------------------------------------------------------
    // Implementation
    //

    SelectionListener menuItemListener = new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        MenuItem mi = (MenuItem)aEvent.widget;
        setDefaultActionDef( (TsActionDef)mi.getData() );
      }

    };

    void internalFillMenu( Menu aMenu ) {
      aspMenuCreator.fillMenu( aMenu );
      for( MenuItem mi : aMenu.getItems() ) {
        mi.addSelectionListener( menuItemListener );
      }
    }

  }

  private final InternalMenuCreator menuCreator;

  private final ITsGuiContext guiContext;

  private final TsToolbar toolbar;

  private final ITsActionSetProvider asp;

  ITsActionDef defaultActionDef;

  /**
   * Constructor.
   *
   * @param aId String - ИД действия
   * @param aAsp {@link ITsActionSetProvider} - поставщик действий
   * @param aToolbar {@link TsToolbar} - кнопочная панель
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   */
  public DropDownMenuActionFromAsp( String aId, ITsActionSetProvider aAsp, TsToolbar aToolbar,
      ITsGuiContext aContext ) {
    super( new TsActionDef( aId, IAction.AS_DROP_DOWN_MENU ), aToolbar.iconSize(), aContext );
    asp = aAsp;
    toolbar = aToolbar;
    guiContext = aContext;
    menuCreator = new InternalMenuCreator( new MenuCreatorFromAsp( aAsp, aContext ) );
    setMenuCreator( menuCreator );
    setDefaultActionDef( aAsp.listHandledActionDefs().first() );
  }

  @Override
  public void run() {
    if( asp.isActionEnabled( defaultActionDef.id() ) ) {
      asp.handleAction( defaultActionDef.id() );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return guiContext;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void setDefaultActionDef( ITsActionDef aActionDef ) {
    defaultActionDef = aActionDef;
    ImageDescriptor imgDescr = iconManager().loadStdDescriptor( defaultActionDef.iconId(), toolbar.iconSize() );
    setImageDescriptor( imgDescr );
    // setEnabled( asp.isActionEnabled( defaultActionDef.id() ) );
    // setEnabled( asp.isActionChecked( defaultActionDef.id() ) );
  }

}
