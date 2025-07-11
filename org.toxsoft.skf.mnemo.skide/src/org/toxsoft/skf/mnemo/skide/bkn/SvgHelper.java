package org.toxsoft.skf.mnemo.skide.bkn;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.w3c.dom.*;

public class SvgHelper {

  private final String          filePath;
  private final DocumentBuilder docBuilder;

  IListEdit<Rectangle> rects = new ElemArrayList<>();
  IListEdit<Path>      paths = new ElemArrayList<>();

  /**
   * Constructor.
   *
   * @param aFilePath String - полный путь к SVG файлу
   */
  public SvgHelper( String aFilePath ) {
    filePath = aFilePath;
    docBuilder = getDocumentBuilder();
  }

  /**
   * "Разбирает" содержимое SVG файла.
   *
   * @param aDisplay {@link Display} - дисплей
   */
  public void parse( Display aDisplay ) {
    clear();
    IStringList pathes = getPathStrings();
    for( String str : pathes ) {
      Rectangle r = parsePath( str, aDisplay );
      if( r != null ) {
        rects.add( r );
      }
    }
  }

  public IList<Rectangle> getRects() {
    return rects;
  }

  /**
   * Освобождает ресурсы.
   */
  public void dispose() {
    clear();
  }

  void clear() {
    rects.clear();
    for( Path p : paths ) {
      p.dispose();
    }
    paths.clear();
  }

  /**
   * Возвращает список строковых представлений "путей" {@link Path} из SVG файла.
   *
   * @return {@link IStringList} - список строковых представлений "путей" {@link Path} из SVG файла
   */
  public IStringList getPathStrings() {
    try {
      IStringListEdit pathStrings = new StringArrayList();
      Document doc = docBuilder.parse( new File( filePath ) );
      Element e = doc.getDocumentElement();
      NodeList nList = e.getChildNodes();

      for( int i = 0; i < nList.getLength(); i++ ) {
        Node n = nList.item( i );
        String ln = n.getLocalName();
        if( "g".equals( ln ) ) { //$NON-NLS-1$
          addPathAttrs( n, pathStrings );
        }
      }
      return pathStrings;
    }
    catch( Throwable ex ) {
      LoggerUtils.errorLogger().error( ex );
      return IStringList.EMPTY;
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static DocumentBuilder getDocumentBuilder() {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    factory.setIgnoringComments( true );
    factory.setNamespaceAware( true );
    try {
      return factory.newDocumentBuilder();
    }
    catch( ParserConfigurationException ex ) {
      throw new RuntimeException( "Internal error", ex ); //$NON-NLS-1$
    }
  }

  void addPathAttrs( Node aNode, IStringListEdit aStrList ) {
    NodeList nList = aNode.getChildNodes();
    for( int i = 0; i < nList.getLength(); i++ ) {
      Node n = nList.item( i );
      String ln = n.getLocalName();
      if( "path".equals( ln ) ) {
        Node attrNode = n.getAttributes().getNamedItem( "d" );
        String value = attrNode.getNodeValue();
        // if( value.indexOf( 'c' ) == -1 && value.indexOf( 'C' ) == -1 ) {
        aStrList.add( value );
        // }
      }
      else {
        addPathAttrs( n, aStrList );
      }
    }
  }

  enum CmdType {

    MOVE_TO_ABS( "M" ), //$NON-NLS-1$
    MOVE_TO_REL( "m" ), //$NON-NLS-1$
    LINE_HOR_ABS( "H" ), //$NON-NLS-1$
    LINE_HOR_REL( "h" ), //$NON-NLS-1$
    LINE_VERT_ABS( "V" ), //$NON-NLS-1$
    LINE_VERT_REL( "v" ), //$NON-NLS-1$
    LINE_TO_ABS( "L" ), //$NON-NLS-1$
    LINE_TO_REL( "l" ), //$NON-NLS-1$
    CURVE_TO_ABS( "C" ), //$NON-NLS-1$
    CURVE_TO_REL( "c" ), //$NON-NLS-1$
    CLOSE_ABS( "Z" ), //$NON-NLS-1$
    CLOSE_REL( "z" ), //$NON-NLS-1$
    UNKNOWN( "" );//$NON-NLS-1$

    private final String mnemonics;

    CmdType( String aMnemonics ) {
      mnemonics = aMnemonics;
    }

    static CmdType ofString( String aStr ) {
      for( CmdType t : values() ) {
        if( t.mnemonics.equals( aStr ) ) {
          return t;
        }
      }
      return UNKNOWN;
    }
  }

  Rectangle parsePath( String aPthsStr, Display aDisplay ) {
    Path path = new Path( aDisplay );
    String delimStr = "MmHhVvLlCcZzQqTtAa"; //$NON-NLS-1$
    StringTokenizer st = new StringTokenizer( aPthsStr, delimStr, true );
    while( st.hasMoreTokens() ) {
      String token = st.nextToken();
      if( delimStr.contains( token ) ) {
        if( !processCommand( token, st, path ) ) {
          path.dispose();
          return null;
        }
      }
    }
    float[] b = new float[4];
    path.getBounds( b );
    paths.add( path );
    Rectangle r = new Rectangle( Math.round( b[0] ), Math.round( b[1] ), Math.round( b[2] ), Math.round( b[3] ) );
    return r;
  }

  boolean processCommand( String aCmdMnemonics, StringTokenizer aTokenizer, Path aPath ) {
    IList<Float> coords = IList.EMPTY;
    if( !aCmdMnemonics.equals( "z" ) && !aCmdMnemonics.equals( "Z" ) && aTokenizer.hasMoreTokens() ) { //$NON-NLS-1$ //$NON-NLS-2$
      String token = aTokenizer.nextToken();
      coords = parsCoords( token );
    }
    float[] sp = new float[2];
    aPath.getCurrentPoint( sp );

    float k = 1.0f;

    return switch( CmdType.ofString( aCmdMnemonics ) ) {
      case CLOSE_ABS, CLOSE_REL -> {
        aPath.close();
        yield true;
      }
      case LINE_HOR_ABS -> {
        for( float f : coords ) {
          aPath.lineTo( f * k, sp[1] );
        }
        yield true;
      }
      case LINE_HOR_REL -> {
        float x = sp[0];
        for( float f : coords ) {
          x += (f * k);
        }
        aPath.lineTo( x, sp[1] + 0 );
        yield true;
      }
      case LINE_TO_ABS -> {
        aPath.lineTo( coords.get( 0 ).floatValue() * k, coords.get( 1 ).floatValue() * k );
        yield true;
      }
      case LINE_TO_REL -> {
        aPath.lineTo( sp[0] + coords.get( 0 ).floatValue() * k, sp[1] + coords.get( 1 ).floatValue() * k );
        yield true;
      }
      case LINE_VERT_ABS -> {
        for( float f : coords ) {
          aPath.lineTo( sp[0], f * k );
        }
        // aPath.lineTo( sp[0], coords.first().floatValue() * k );
        yield true;
      }
      case LINE_VERT_REL -> {
        float y = sp[1];
        for( float f : coords ) {
          y += (f * k);
        }
        aPath.lineTo( sp[0] + 0, y );
        yield true;
      }
      case CURVE_TO_ABS -> {
        if( coords.size() % 6 != 0 ) {
          yield false;
        }
        float[] x = new float[3];
        float[] y = new float[3];
        for( int i = 0; i < coords.size(); i += 6 ) {
          x[0] = (coords.get( i ).floatValue() * k);
          x[1] = (coords.get( i + 2 ).floatValue() * k);
          x[2] = (coords.get( i + 4 ).floatValue() * k);
          y[0] = (coords.get( i + 1 ).floatValue() * k);
          y[1] = (coords.get( i + 3 ).floatValue() * k);
          y[2] = (coords.get( i + 5 ).floatValue() * k);
          aPath.cubicTo( x[0], y[0], x[1], y[1], x[2], y[2] );
        }
        yield true;
      }
      case CURVE_TO_REL -> {
        if( coords.size() % 6 != 0 ) {
          yield false;
        }
        float[] x = new float[3];
        float[] y = new float[3];
        for( int i = 0; i < coords.size(); i += 6 ) {
          x[0] = sp[0] * 1 + (coords.get( i ).floatValue() * k);
          x[1] = sp[0] * 1 + (coords.get( i + 2 ).floatValue() * k);
          x[2] = sp[0] * 1 + (coords.get( i + 4 ).floatValue() * k);
          y[0] = sp[1] * 1 + (coords.get( i + 1 ).floatValue() * k);
          y[1] = sp[1] * 1 + (coords.get( i + 3 ).floatValue() * k);
          y[2] = sp[1] * 1 + (coords.get( i + 5 ).floatValue() * k);
          aPath.cubicTo( x[0], y[0], x[1], y[1], x[2], y[2] );
          aPath.getCurrentPoint( sp );
        }
        yield true;
      }
      case MOVE_TO_ABS -> {
        if( coords.size() != 2 ) {
          yield false;
        }
        aPath.moveTo( coords.get( 0 ).floatValue() * k, coords.get( 1 ).floatValue() * k );
        yield true;
      }
      case MOVE_TO_REL -> {
        if( coords.size() % 2 != 0 ) {
          yield false;
        }
        float x = sp[0] * k;
        float y = sp[1] * k;
        for( int i = 0; i < coords.size(); i += 2 ) {
          if( i < 2 ) {
            x += (coords.get( i ).floatValue() * k);
            y += (coords.get( i + 1 ).floatValue() * k);
          }
          else {
            x += (coords.get( i ).floatValue() * 1);
            y += (coords.get( i + 1 ).floatValue() * 1);
          }
        }

        // aPath.moveTo( sp[0] * k + coords.get( 0 ) * k, sp[1] * k + coords.get( 1 ) * k );
        aPath.moveTo( x, y );
        yield true;
      }
      case UNKNOWN -> false;
      default -> throw new TsNotAllEnumsUsedRtException();
    };

  }

  IList<Float> parsCoords( String aStr ) {
    IListEdit<Float> coords = new ElemArrayList<>();
    StringTokenizer st = new StringTokenizer( aStr, ", " ); //$NON-NLS-1$
    while( st.hasMoreTokens() ) {
      String nt = st.nextToken();
      float value = Float.parseFloat( nt );
      coords.add( Float.valueOf( value * 3.779f ) );
    }
    return coords;
  }

}
