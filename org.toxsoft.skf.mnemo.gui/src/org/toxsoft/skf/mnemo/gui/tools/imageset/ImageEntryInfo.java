package org.toxsoft.skf.mnemo.gui.tools.imageset;

import java.io.*;

import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вхождение в список описаний изображений.
 * <p>
 *
 * @author vs
 */
public class ImageEntryInfo
    implements IImageEntryInfo, Serializable {

  private static final long serialVersionUID = 8007296849090328037L;

  private final TsImageDescriptor imageDescriptor;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "ImageEntryInfo"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IImageEntryInfo> KEEPER =
      new AbstractEntityKeeper<>( IImageEntryInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IImageEntryInfo aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          TsImageDescriptor.KEEPER.write( aSw, aEntity.imageDescriptor() );
        }

        @Override
        protected IImageEntryInfo doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          TsImageDescriptor imd = TsImageDescriptor.KEEPER.read( aSr );
          return new ImageEntryInfo( id, imd );
        }
      };

  private final String id;

  ImageEntryInfo( String aId, TsImageDescriptor aImageDescriptor ) {
    TsIllegalArgumentRtException.checkFalse( StridUtils.isValidIdPath( aId ) );
    id = aId;
    imageDescriptor = aImageDescriptor;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  public String description() {
    return TsLibUtils.EMPTY_STRING;
  }

  // ------------------------------------------------------------------------------------
  // IImageEntryInfo
  //

  @Override
  public TsImageDescriptor imageDescriptor() {
    return imageDescriptor;
  }

}
