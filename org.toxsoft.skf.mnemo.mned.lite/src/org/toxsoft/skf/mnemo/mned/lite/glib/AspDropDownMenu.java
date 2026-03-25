package org.toxsoft.skf.mnemo.mned.lite.glib;

import org.eclipse.jface.action.*;
import org.eclipse.jface.resource.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;

public class AspDropDownMenu
    extends AbstractSingleActionSetProvider
    implements IMenuCreator, ITsGuiContextable {

  private final ITsActionSetProvider actionSet;
  private final ITsGuiContext        guiContext;

  private MenuCreatorFromAsp menuCreator = null;

  ITsToolbar toolbar;

  String defaultActionId;

  TsAction thisAction = null;

  public AspDropDownMenu( String aId, ITsActionSetProvider aActionSet, ITsGuiContext aGuiContext ) {
    super( TsActionDef.ofMenu1( aId ) );
    actionSet = aActionSet;
    guiContext = aGuiContext;
    defaultActionId = aActionSet.listHandledActionIds().first();
  }

  @Override
  public void run() {
    actionSet.handleAction( defaultActionId );
  }

  // ------------------------------------------------------------------------------------
  // IMenuCreator
  //

  @Override
  public void dispose() {
    menuCreator.dispose();
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
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return guiContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public void setToolbar( ITsToolbar aToolbar ) {
    toolbar = aToolbar;
    thisAction = aToolbar.getAction( listHandledActionDefs().first().id() );
    if( thisAction != null ) {
      ITsActionDef defAct = actionSet.getActionDef( defaultActionId );

      ImageDescriptor imgDescr = iconManager().loadStdDescriptor( defAct.iconId(), toolbar.iconSize() );
      thisAction.setImageDescriptor( imgDescr );
    }
  }

  // ------------------------------------------------------------------------------------
  // Static methods
  //

  public static ITsActionDef actionDef( String aId ) {
    return TsActionDef.ofMenu1( aId );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  SelectionListener menuItemListener = new SelectionAdapter() {

    @Override
    public void widgetSelected( SelectionEvent aEvent ) {
      if( thisAction != null ) {
        MenuItem mi = (MenuItem)aEvent.widget;
        TsActionDef actionDef = (TsActionDef)mi.getData();
        defaultActionId = actionDef.id();
        ImageDescriptor imgDescr = iconManager().loadStdDescriptor( actionDef.iconId(), toolbar.iconSize() );
        thisAction.setImageDescriptor( imgDescr );
      }
    }

  };

  void internalFillMenu( Menu aMenu ) {
    menuCreator = new MenuCreatorFromAsp( actionSet, guiContext );
    menuCreator.fillMenu( aMenu );
    for( MenuItem mi : aMenu.getItems() ) {
      mi.addSelectionListener( menuItemListener );
    }
  }

}
