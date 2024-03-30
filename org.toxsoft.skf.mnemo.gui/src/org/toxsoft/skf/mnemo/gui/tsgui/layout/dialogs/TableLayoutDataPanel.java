package org.toxsoft.skf.mnemo.gui.tsgui.layout.dialogs;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.valed.controls.basic.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.table.*;

/**
 * Панель редактирования полей прямоугольной области.
 *
 * @author vs
 */
public class TableLayoutDataPanel
    extends AbstractTsDialogPanel<VedTableLayoutControllerConfig, IVedScreen> {

  protected TableLayoutDataPanel( Composite aParent,
      TsDialog<VedTableLayoutControllerConfig, IVedScreen> aOwnerDialog ) {
    super( aParent, aOwnerDialog );
    init();
  }

  protected TableLayoutDataPanel( Composite aParent, ITsGuiContext aContext, VedTableLayoutControllerConfig aData,
      IVedScreen aEnviron, int aFlags ) {
    super( aParent, aContext, aData, aEnviron, aFlags );
    init();
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsDialogPanel
  //

  @Override
  protected void doSetDataRecord( VedTableLayoutControllerConfig aData ) {
    if( aData != null ) {
    }
  }

  @Override
  protected VedTableLayoutControllerConfig doGetDataRecord() {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  D2MarginsPanel                marginsPanel;
  ValedEnumCombo<EHorAlignment> horAlignCombo;
  ValedBooleanCheck             horFillCheck;
  ValedEnumCombo<EVerAlignment> verAlignCombo;
  ValedBooleanCheck             vertFillCheck;

  void init() {

  }

}
