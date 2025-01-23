package org.toxsoft.skf.mnemo.gui.chart;

import org.toxsoft.core.tsgui.graphics.lines.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Information about one graphic in char.
 *
 * @author hazard157
 */
public interface IGraphInfo
    extends IStridable {

  TsLineInfo lineInfo();

  // scale information, etc

}
