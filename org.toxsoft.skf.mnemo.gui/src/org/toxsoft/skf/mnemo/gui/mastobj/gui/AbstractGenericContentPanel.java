package org.toxsoft.skf.mnemo.gui.mastobj.gui;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericContentPanel} abstract implementation.
 *
 * @author hazard157
 */
public abstract class AbstractGenericContentPanel
    extends AbstractLazyPanel<Control>
    implements IGenericContentPanel {

  private final GenericChangeEventer genericChangeEventer;
  private final boolean              isViewer;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - viewer flag, sets {@link #isViewer()} value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractGenericContentPanel( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext );
    genericChangeEventer = new GenericChangeEventer( this );
    isViewer = aIsViewer;
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  final public GenericChangeEventer genericChangeEventer() {
    return genericChangeEventer;
  }

  // ------------------------------------------------------------------------------------
  // IGenericContentPanel
  //

  @Override
  final public boolean isViewer() {
    return isViewer;
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected abstract Control doCreateControl( Composite aParent );

}
