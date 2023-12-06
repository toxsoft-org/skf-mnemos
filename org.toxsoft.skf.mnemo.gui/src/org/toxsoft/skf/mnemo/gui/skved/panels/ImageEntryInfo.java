package org.toxsoft.skf.mnemo.gui.skved.panels;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

public class ImageEntryInfo
    extends StridableParameterized
    implements IImageEntryInfo {

  private final TsImageDescriptor imageDescriptor;

  ImageEntryInfo( String aId, TsImageDescriptor aImageDescriptor ) {
    super( aId );
    imageDescriptor = aImageDescriptor;
  }

  // ------------------------------------------------------------------------------------
  // IImageEntryInfo
  //

  @Override
  public TsImageDescriptor imageDescriptor() {
    return imageDescriptor;
  }

}
