package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.e4.ui.model.application.ui.advanced.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link EAtomicType#VALOBJ} of type {@link MPerspective id} editor.
 * <p>
 * Wraps over {@link ValedMPerspectiveIdEditor}.
 *
 * @author dima
 */
public class ValedAvValobjMPerspIdEditor
    extends AbstractAvValobjWrapperValedControl<String> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ValedAvValobjMPerspIdEditor"; //$NON-NLS-1$

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
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjMPerspIdEditor( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvValobjMPerspIdEditor( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedMPerspectiveIdEditor.FACTORY );
  }

}
