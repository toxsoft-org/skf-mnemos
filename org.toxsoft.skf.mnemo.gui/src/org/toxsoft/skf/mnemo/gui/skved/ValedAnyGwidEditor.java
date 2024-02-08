package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.window.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Временный редактор GWID'а любого типа.
 * <p>
 * Allows to select {@link Gwid} by accessing {@link ISkObjectService}.
 *
 * @author vs
 */
public class ValedAnyGwidEditor
    extends AbstractValedTextAndButton<Gwid> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AnyGwidEditor"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<Gwid> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<Gwid, ?> e = new ValedAnyGwidEditor( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedAnyGwidEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  @Override
  protected boolean doProcessButtonPress() {
    // create and dispaly Gwid selector
    // Gwid gwid = PanelGwidSelector.selectGwid( canGetValue().isOk() ? getValue() : null, tsContext(), propKind, null
    // );
    //
    Gwid gwid = canGetValue().isOk() ? getValue() : null;
    String strValue = TsLibUtils.EMPTY_STRING;
    if( gwid != null ) {
      strValue = gwid.toString();
    }
    InputDialog dlg = new InputDialog( getShell(), "ИД Sk сущности", "ИД (GWID):", strValue, null );
    if( dlg.open() == Window.OK ) {
      String str = dlg.getValue();
      try {
        gwid = Gwid.of( str );
        doSetUnvalidatedValue( gwid );
        return true;
      }
      catch( @SuppressWarnings( "unused" ) Exception ex ) {
        TsDialogUtils.error( getShell(), "Неверный формат GWID" );
        return false;
      }
    }
    return false;
  }

  @Override
  public ValidationResult canGetValue() {
    try {
      Gwid.of( getTextControl().getText() );
      return ValidationResult.SUCCESS;
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      return ValidationResult.error( "Неверный формат GWID" );
    }
  }

  @Override
  protected void doUpdateTextControl() {
    // nop
  }

  @Override
  protected Gwid doGetUnvalidatedValue() {
    return Gwid.of( getTextControl().getText() );
  }

  @Override
  protected void doDoSetUnvalidatedValue( Gwid aValue ) {
    String txt = TsLibUtils.EMPTY_STRING;
    if( aValue != null ) {
      txt = aValue.toString();
    }
    getTextControl().setText( txt );
  }

}
