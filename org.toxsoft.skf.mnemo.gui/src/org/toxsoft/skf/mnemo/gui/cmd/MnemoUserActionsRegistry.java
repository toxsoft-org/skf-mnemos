package org.toxsoft.skf.mnemo.gui.cmd;

import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Общий реестр "исполнителей" пользовательских действий вызываемых из мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class MnemoUserActionsRegistry {

  /**
   * The actions map "action name" - "the action"..
   */
  private final IStringMapEdit<IMnemoUserAction> actionsMap = new StringMap<>();

  /**
   * Register usr action.
   *
   * @param aAction {@link IMnemoUserAction} - user action
   * @throws TsItemAlreadyExistsRtException - if action already rgistered
   */
  public void registerAction( IMnemoUserAction aAction ) {
    TsNullArgumentRtException.checkNull( aAction );
    TsItemAlreadyExistsRtException.checkTrue( actionsMap.hasKey( aAction.id() ) );
    actionsMap.put( aAction.id(), aAction );
  }

  /**
   * Retuns the map of registered user actions.
   *
   * @return IStringMap&lt;IMnemoUserAction> - registered user actions
   */
  public IStringMap<IMnemoUserAction> registeredActions() {
    return actionsMap;
  }

}
