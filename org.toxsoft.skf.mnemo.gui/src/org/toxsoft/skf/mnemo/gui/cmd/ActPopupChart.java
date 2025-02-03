package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

public class ActPopupChart
    implements IMnemoUserAction {

  static final IStridablesList<IDataDef> inputDefs = new StridablesList<>( //
      PROP_WIDTH, //
      PROP_HEIGHT, //
      PROP_RTD_UGWI, //
      PROP_SKID_UGWI //
  );

  static final IStridablesList<ITinFieldInfo> tinFields = new StridablesList<>( //
      TFI_RTD_UGWI );

  public ActPopupChart() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return "popupChart";
  }

  @Override
  public String nmName() {
    return "График";
  }

  @Override
  public String description() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IMnemoUserAction
  //

  @Override
  public IStridablesList<IDataDef> inputDefs() {
    return inputDefs;
  }

  @Override
  public IStridablesList<ITinFieldInfo> tinFields() {
    return tinFields;
  }

  @Override
  public void run( IOptionSet aInputValues, ITsPoint aCoors, ITsGuiContext aTsContext ) {
    TsDialogUtils.info( aTsContext.get( Shell.class ), "PopUp chart invoked." );
  }

}
