package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import org.toxsoft.core.tsgui.ved.screen.helpers.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Handles properties values change attempt.
 * <p>
 * This interceptor is to be added in VED item implementation constructor with the method
 * {@link VedAbstractItem#addInterceptor(IVedItemPropertyChangeInterceptor)}. Mainly interceptor implementation is
 * needed for code reuse if some interception logic has to be used in more then one item. Interception logic for a
 * single item may be implemented the subclass, in method like <code>VedAbstractVisel.doDoInterceptPropsChange()</code>.
 * <p>
 * There are some predefined interceptor implementations <code>VedViselInterceptorXxx</code> and
 * <code>VedActorInterceptorXxx</code>.
 *
 * @author vs
 */
public interface IRtControlPropertyChangeInterceptor {

  /**
   * Called when properties values change is requested.
   * <p>
   * Editable argument <code>aValuesToSet</code> is the values, that will be set to properties. It initially contains
   * the same vales as <code>aNewValues</code>. Interceptor may remove values from <code>aValuesToSet</code> edit
   * existing, add any other properties values or event clear to cancel changes.
   * <p>
   * Current values of the properties may be accessed via <code>aSource</code> argument.
   *
   * @param aSource &lt;S&gt; - the event source VED item
   * @param aNewValues {@link IOptionSetEdit} - changed properties values after change
   * @param aValuesToSet {@link IOptionSet} - the values to be set after interception
   */
  void interceptPropsChange( IRtControl aSource, IOptionSet aNewValues, IOptionSetEdit aValuesToSet );
}
