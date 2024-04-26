package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.skved.*;

/**
 * Allows to select {@MPerspecive id} by accessing {@link EModelService}.
 *
 * @author dima
 */
public class ValedMPerspectiveIdEditor
    extends AbstractValedTextAndButton<MPerspId> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SkidEditor"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author dima
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<MPerspId> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<MPerspId, ?> e = new ValedMPerspectiveIdEditor( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();
  private MPerspId                                value   = null;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedMPerspectiveIdEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );

  }

  @Override
  protected boolean doProcessButtonPress() {
    // create and dispaly MPerspective selector
    MPerspId initVal = canGetValue().isOk() ? getValue() : MPerspId.NONE;
    MPerspId pesrpId = PanelE4PerspIdSelector.selectE4PerspId( initVal, tsContext() );
    if( pesrpId != null ) {
      value = pesrpId;
      return true;
    }
    return false;
  }

  @Override
  protected MPerspId doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doDoSetUnvalidatedValue( MPerspId aValue ) {
    String txt = MPerspId.NONE.toString();
    if( aValue != null ) {
      txt = aValue.toString();
      value = aValue;
    }
    getTextControl().setText( txt );
  }

  @Override
  protected void doUpdateTextControl() {
    if( value != null ) {
      getTextControl().setText( value.toString() );
    }
    else {
      getTextControl().setText( MPerspId.NONE.toString() );
    }
  }

}
