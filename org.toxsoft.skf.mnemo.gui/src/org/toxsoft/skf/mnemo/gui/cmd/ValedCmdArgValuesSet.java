package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;

public class ValedCmdArgValuesSet
    extends AbstractValedTextAndButton<CmdArgValuesSet> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".CmdArgValuesSet"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  @SuppressWarnings( "unchecked" )
  public static final AbstractValedControlFactory FACTORY = new AbstractValedControlFactory( FACTORY_NAME ) {

    @Override
    protected AbstractValedControl<CmdArgValuesSet, Composite> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedCmdArgValuesSet( aContext );
    }

  };

  ValedCmdArgValuesSet( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedTextAndButton
  //

  CmdArgValuesSet value = null;

  @Override
  protected boolean doProcessButtonPress() {
    // ITsDialogInfo tdi = new TsDialogInfo( tsContext(), "DLG_SELECT_VISEL_ID", "DLG_SELECT_VISEL_ID_D" );
    // MnemoCommandersRegistry mcr = tsContext().get( MnemoCommandersRegistry.class );
    // IMnemoCommander mc = DialogItemsList.select( tdi, mcr.registeredCommanders().values(), null );
    // if( mc != null ) {
    // value = new CmdArgValuesSet( mc.id(), mc.id(), IOptionSet.NULL );
    // }
    CmdArgValuesSet cfg = PanelCmdArgValuesSet.edit( value, tsContext() );
    if( cfg != null ) {
      value = cfg;
      return true;
    }
    return false;
  }

  @Override
  protected void doDoSetUnvalidatedValue( CmdArgValuesSet aValue ) {
    value = aValue;
  }

  @Override
  public ValidationResult canGetValue() {
    String id = getTextControl().getText();
    // if( !viselManager().list().hasKey( id ) ) {
    // return ValidationResult.warn( FMT_WARN_NO_VISEL_ID, id );
    // }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected CmdArgValuesSet doGetUnvalidatedValue() {
    return value;
  }

}
