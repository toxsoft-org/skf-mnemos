package org.toxsoft.skf.mnemo.gui.cmd;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

public class MnemoTestUserAction
    extends Stridable
    implements IMnemoUserAction {

  /**
   * Constructor.
   */
  public MnemoTestUserAction() {
    super( "mnemo.test.commander" );
    setName( "Тестовое действие" );
    setDescription( "Действие созданное для отладочных целей" );
  }

  // ------------------------------------------------------------------------------------
  // AbstractMnemoCommander
  //

  @Override
  public IStridablesList<IDataDef> inputDefs() {
    return IStridablesList.EMPTY;
  }

  @Override
  public IStridablesList<ITinFieldInfo> tinFields() {
    return IStridablesList.EMPTY;
  }

  @Override
  public void run( IOptionSet aInputValues, ITsPoint aCoors, ITsGuiContext aTsContext ) {
    TsDialogUtils.info( aTsContext.get( Shell.class ), "Вызвана пользовательская команда" );
  }

}
