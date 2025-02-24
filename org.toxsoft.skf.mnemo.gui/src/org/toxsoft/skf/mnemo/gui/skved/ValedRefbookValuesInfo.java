package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;

/**
 * Редактор информации, позволяющей получить значение атрибута элемента справочника по ключу.
 * <p>
 *
 * @author vs
 */
public class ValedRefbookValuesInfo
    extends AbstractValedTextAndButton<IdChain> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".RefbookValuesInfoEditor"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IdChain> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedRefbookValuesInfo( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( IdChain.class );
    }
  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  IdChain value;

  protected ValedRefbookValuesInfo( ITsGuiContext aContext ) {
    super( aContext );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected IdChain doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected boolean doProcessButtonPress() {
    IdChain result = PanelRebookValuesInfo.edit( value, tsContext() );
    if( result != null ) {
      value = result;
      return true;
    }
    return false;
  }

  @Override
  protected void doDoSetUnvalidatedValue( IdChain aValue ) {
    value = aValue;
  }

  @Override
  protected void doUpdateTextControl() {
    if( value != null ) {
      getTextControl().setText( value.toString() );
    }
  }

}
