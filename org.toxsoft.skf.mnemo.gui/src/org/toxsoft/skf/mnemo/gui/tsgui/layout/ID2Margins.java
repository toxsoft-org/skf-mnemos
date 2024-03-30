package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tsgui.utils.margins.*;

/**
 * Analog of the {@link ITsMargins}, but double instead of integer.
 *
 * @author vs
 */
public sealed interface ID2Margins permits ID2GridMargins,D2Margins {

  /**
   * Returns the distance between the internals and the left edge of the panel.
   *
   * @return double - distance between the internals and the left edge of the panel
   */
  double left();

  /**
   * Returns the distance between the internals and the right edge of the panel.
   *
   * @return double - distance between the internals and the right edge of the panel
   */
  double right();

  /**
   * Returns the distance between the internals and the top edge of the panel.
   *
   * @return double - distance between the internals and the top edge of the panel
   */
  double top();

  /**
   * Returns the distance between the internals and the bottom edge of the panel.
   *
   * @return double - distance between the internals and the bottom edge of the panel
   */
  double bottom();

}
