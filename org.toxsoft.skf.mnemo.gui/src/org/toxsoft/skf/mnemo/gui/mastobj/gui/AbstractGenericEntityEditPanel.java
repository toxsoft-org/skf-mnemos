package org.toxsoft.skf.mnemo.gui.mastobj.gui;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericEntityEditPanel} abstract implementation.
 * <p>
 * When overriding this class do not forget to fire an event with {@link #fireChangeEvent()} every time when panel
 * widget values are changed.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public abstract class AbstractGenericEntityEditPanel<T>
    extends AbstractGenericContentPanel
    implements IGenericEntityEditPanel<T> {

  private T specifiedEntity = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - viewer flag, sets {@link #isViewer()} value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractGenericEntityEditPanel( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
  }

  // ------------------------------------------------------------------------------------
  // IGenericEntityPanel
  //

  @Override
  public void setEntity( T aEntity ) {
    if( aEntity == specifiedEntity ) {
      return;
    }
    specifiedEntity = aEntity;
    doProcessSetEntity();
  }

  // ------------------------------------------------------------------------------------
  // IGenericEntityEditPanel
  //

  @Override
  final public T getEntity() {
    if( isControlValid() ) {
      return specifiedEntity;
    }
    TsValidationFailedRtException.checkError( canGetEntity() );
    return doGetEntity();
  }

  @Override
  final public ValidationResult canGetEntity() {
    if( isControlValid() ) {
      return ValidationResult.SUCCESS;
    }
    return doCanGetEntity();
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the entity specified via {@link #setEntity(Object)}.
   *
   * @return &lt;T&gt; - the entity, may be <code>null</code>
   */
  protected T specifiedEntity() {
    return specifiedEntity;
  }

  /**
   * Fires a ganeric change event by calling <code>genericChangeEventer().fireChangeEvent()</code>.
   */
  protected void fireChangeEvent() {
    genericChangeEventer().fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // To implement/override
  //

  /**
   * Implementation must return the entity contained in this panel.
   * <p>
   * This method must create new entity from the values, contained in the panel widgets. Method is called from
   * {@link #getEntity()} after successful check {@link #canGetEntity()}.
   *
   * @return &lt;T&gt; - the entity, may be null
   */
  protected abstract T doGetEntity();

  /**
   * Subclass must process when client specifies new entity via {@link #setEntity(Object)},
   * <p>
   * New entity is returned by the method {@link #specifiedEntity()}.
   */
  protected abstract void doProcessSetEntity();

  /**
   * Subclass may perform check if entity may be created from the values in panel widgets.
   * <p>
   * Method is called only when SWT panel is created, that is when {@link #isControlValid()} = <code>true</code>.
   * <p>
   * Returns {@link ValidationResult#SUCCESS} in the base class, there is no need to call superclass method when
   * overriding.
   *
   * @return {@link ValidationResult} - the check result
   */
  protected ValidationResult doCanGetEntity() {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected abstract Control doCreateControl( Composite aParent );

}
