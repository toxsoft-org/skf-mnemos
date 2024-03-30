package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tsgui.utils.margins.*;

/**
 * Analog of the {@link ITsGridMargins}, but double instead of integer.
 *
 * @author vs
 */
public sealed interface ID2GridMargins
    extends ID2Margins permits D2GridMargins {

  /**
   * Returns the horizontal distance between grid cells.
   *
   * @return double - horizontal distance between grid cells
   */
  double horGap();

  /**
   * Returns the vertical distance between grid cells.
   *
   * @return double - vertical distance between grid cells
   */
  double verGap();

}
