package org.toxsoft.skf.mnemo.gui.tools.imageset;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Реализация интерфейса {@link IMnemoImageSetInfo}.
 * <p>
 *
 * @author vs
 */
public class MnemoImageSetInfo
    implements IMnemoImageSetInfo, Serializable {

  private static final long serialVersionUID = 8007296849090328037L;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "ImageSetInfo"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IMnemoImageSetInfo> KEEPER =
      new AbstractEntityKeeper<>( IMnemoImageSetInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IMnemoImageSetInfo aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.nmName() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.description() );
          aSw.writeSeparatorChar();
          int size = aEntity.imageInfoes().size();
          aSw.writeInt( size );
          aSw.writeChar( '{' );
          for( int i = 0; i < size; i++ ) {
            ImageEntryInfo.KEEPER.write( aSw, aEntity.imageInfoes().get( i ) );
            if( i < size - 1 ) {
              aSw.writeSeparatorChar();
            }
          }
          aSw.writeChar( '}' );
        }

        @Override
        protected IMnemoImageSetInfo doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String name = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          String description = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          int size = aSr.readInt();
          aSr.ensureChar( '{' );
          IStridablesListEdit<IImageEntryInfo> imgInfoes = new StridablesList<>();
          for( int i = 0; i < size; i++ ) {
            imgInfoes.add( ImageEntryInfo.KEEPER.read( aSr ) );
            if( i < size - 1 ) {
              aSr.ensureSeparatorChar();
            }
          }
          aSr.ensureChar( '}' );
          return new MnemoImageSetInfo( id, name, description, imgInfoes );
        }
      };

  private final String id;

  private String name = TsLibUtils.EMPTY_STRING;

  private String description = TsLibUtils.EMPTY_STRING;

  private IStridablesListEdit<IImageEntryInfo> imageInfoes = IStridablesList.EMPTY;

  MnemoImageSetInfo( String aId, String aName, String aDescription, IStridablesList<IImageEntryInfo> aImageInfoes ) {
    id = aId;
    name = aName;
    description = aDescription;
    imageInfoes = new StridablesList<>( aImageInfoes );
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
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ------------------------------------------------------------------------------------
  // IMnemoImageSetInfo
  //

  @Override
  public IStridablesList<IImageEntryInfo> imageInfoes() {
    return imageInfoes;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return nmName();
  }
}
