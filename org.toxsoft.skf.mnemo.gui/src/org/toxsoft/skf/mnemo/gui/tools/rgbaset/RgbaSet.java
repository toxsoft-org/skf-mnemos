package org.toxsoft.skf.mnemo.gui.tools.rgbaset;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Набор цветов с доступом по индексу.
 *
 * @author vs
 */
public class RgbaSet
    implements IRgbaSet {

  private static final long serialVersionUID = 157157L;

  private String id = TsLibUtils.EMPTY_STRING;

  private String name = TsLibUtils.EMPTY_STRING;

  private String description = TsLibUtils.EMPTY_STRING;

  private IListEdit<RGBA> colorsList;

  /**
   * Registered keeper ID.
   */
  public static final String KEEPER_ID = "RgbaSet"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<RgbaSet> KEEPER =
      new AbstractEntityKeeper<>( RgbaSet.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, RgbaSet aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.nmName() );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.description() );
          aSw.writeSeparatorChar();
          int size = aEntity.size();
          aSw.writeInt( size );
          aSw.writeChar( '{' );
          for( int i = 0; i < size; i++ ) {
            RGBAKeeper.KEEPER.write( aSw, aEntity.getRgba( i ) );
            if( i < size - 1 ) {
              aSw.writeSeparatorChar();
            }
          }
          aSw.writeChar( '}' );
        }

        @Override
        protected RgbaSet doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String name = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          String description = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          int size = aSr.readInt();
          aSr.ensureChar( '{' );
          IListEdit<RGBA> colors = new ElemArrayList<>();
          for( int i = 0; i < size; i++ ) {
            colors.add( RGBAKeeper.KEEPER.read( aSr ) );
            if( i < size - 1 ) {
              aSr.ensureSeparatorChar();
            }
          }
          aSr.ensureChar( '}' );
          return new RgbaSet( id, name, description, colors );
        }
      };

  /**
   * Конструктор.
   */
  public RgbaSet() {
    colorsList = new ElemArrayList<>();
    // TODO получить noneId вместо fooId
    id = "fooId"; //$NON-NLS-1$
  }

  /**
   * Конструктор.
   *
   * @param aId String - ИД набора цветов
   * @param aName String - название набора цветов
   * @param aDescription String - описание набора цветов
   * @param aColors IList&lt;RGBA> - список цветов в виде {@link RGBA}
   */
  public RgbaSet( String aId, String aName, String aDescription, IList<RGBA> aColors ) {
    colorsList = new ElemArrayList<>();
    colorsList.addAll( aColors );
    id = aId;
    name = aName;
    description = aDescription;
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
  // IRgbaSet
  //

  @Override
  public int size() {
    return colorsList.size();
  }

  @Override
  public RGBA getRgba( int aIndex ) {
    return colorsList.get( aIndex );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  IListEdit<RGBA> rgbaList() {
    return colorsList;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append( '{' );
    for( int i = 0; i < colorsList.size(); i++ ) {
      RGBA rgba = colorsList.get( i );
      sb.append( rgba.toString() );
      if( i < colorsList.size() - 1 ) {
        sb.append( ", " ); //$NON-NLS-1$
      }
    }
    sb.append( '}' );
    return sb.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof RgbaSet that ) {
      return this.colorsList.equals( that.colorsList );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + colorsList.hashCode();
    return result;
  }

}
