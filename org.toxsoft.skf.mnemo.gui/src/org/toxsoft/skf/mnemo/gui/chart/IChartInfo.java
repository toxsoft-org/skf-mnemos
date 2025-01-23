package org.toxsoft.skf.mnemo.gui.chart;

import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Information about chart containing several graphics.
 *
 * @author hazard157
 */
public interface IChartInfo {

  /**
   * Returns the preferred size of the chart.
   *
   * @return {@link TsDims} - chart size in pixels
   */
  TsDims dimensions();

  // time information

  // X scale information, etc

  IStringMap<IGraphInfo> graphInfos();

}
