package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;

public class ValedMnemoCommandCfg
    extends AbstractValedTextAndButton<MnemoCommandCfg> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".CommandCfg"; //$NON-NLS-1$

  MnemoCommandCfg currCfg = null;

  /**
   * The factory singleton.
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected AbstractValedControl<MnemoCommandCfg, Composite> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedMnemoCommandCfg( aContext );
    }

  };

  ValedMnemoCommandCfg( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  @Override
  protected boolean doProcessButtonPress() {
    MnemoCommandCfg cfg = PanelMnemoCommandCfg.edit( currCfg, tsContext() );
    if( cfg != null ) {
      currCfg = cfg;
      return true;
    }
    return false;
  }

  @Override
  protected void doDoSetUnvalidatedValue( MnemoCommandCfg aValue ) {
    currCfg = aValue;
  }

  @Override
  protected MnemoCommandCfg doGetUnvalidatedValue() {
    return currCfg;
  }

  @Override
  protected void doUpdateTextControl() {
    if( currCfg != null ) {
      getTextControl().setText( currCfg.propValues().getStr( TSID_NAME ) );
      return;
    }
    super.doUpdateTextControl();
  }
}
