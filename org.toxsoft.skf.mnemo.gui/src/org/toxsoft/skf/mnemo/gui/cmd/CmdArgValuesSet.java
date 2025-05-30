package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Персистентный набор значений аргументов какой-либо команды определенного класса, который может передаваться при
 * вызове команды соответствующего типа.
 * <p>
 *
 * @author vs
 */
public class CmdArgValuesSet
    extends StridableParameterized {

  private static final String KW_PARAMS = "params"; //$NON-NLS-1$

  /**
   * The keeper id
   */
  public static final String KEEPER_ID = "CmdArgValuesSet"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<CmdArgValuesSet> KEEPER =
      new AbstractEntityKeeper<>( CmdArgValuesSet.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, CmdArgValuesSet aEntity ) {
          aSw.incNewLine();
          // item ID
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          // class ID
          aSw.writeAsIs( aEntity.classId );
          aSw.writeSeparatorChar();
          // command ID
          aSw.writeAsIs( aEntity.cmdId );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // properties values
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.propValues );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // parameters values
          StrioUtils.writeKeywordHeader( aSw, KW_PARAMS, true );
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          aSw.decNewLine();
        }

        @Override
        protected CmdArgValuesSet doRead( IStrioReader aSr ) {
          // item ID
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // class ID
          String classId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // command ID
          String cmdId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // properties values
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // parameters values
          StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // create an item to read extra data into it
          CmdArgValuesSet itemCfg = new CmdArgValuesSet( id, classId, cmdId, propValues, params );
          return itemCfg;
        }
      };

  // private final String id;
  private final String classId;
  private final String cmdId;

  private final IOptionSetEdit propValues = new OptionSet();

  /**
   * Constructor.
   *
   * @param aId String - ИД набора
   */
  public CmdArgValuesSet( String aId ) {
    super( aId );
    classId = IStridable.NONE_ID;
    cmdId = IStridable.NONE_ID;
  }

  /**
   * Constructor.
   *
   * @param aId String - ИД набора
   * @param aClassId String - ИД класса
   * @param aCmdId String - ИД команды
   * @param aProps {@link IOptionSet} - значения свойств
   * @param aParams {@link IOptionSet} - значения параметров
   */
  public CmdArgValuesSet( String aId, String aClassId, String aCmdId, IOptionSet aProps, IOptionSet aParams ) {
    super( aId );
    classId = aClassId;
    cmdId = aCmdId;
    propValues.setAll( aProps );
    params().setAll( aParams );
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String nmName() {
    return propValues.getStr( PROP_NAME );
  }

  @Override
  public String description() {
    return propValues.getStr( PROP_DESCRIPTION );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает ИД класса.
   *
   * @return String - ИД класса
   */
  public String classId() {
    return classId;
  }

  /**
   * Возвращает ИД команды.
   *
   * @return String - ИД команды
   */
  public String cmdId() {
    return cmdId;
  }

  /**
   * Возвращает набор значений аргументов команды определенного типа.
   *
   * @param aCoreApi {@link ISkCoreApi} - API сервера
   * @return {@link IOptionSet} - набор значений аргументов команды
   */
  public IOptionSet argValues( ISkCoreApi aCoreApi ) {
    IStridablesList<IDataDef> argDefs = argDefs( aCoreApi );
    if( argDefs == null || argDefs.isEmpty() ) {
      return IOptionSet.NULL;
    }
    IOptionSetEdit result = new OptionSet();
    for( String argId : argDefs.ids() ) {
      if( propValues.hasKey( argId ) ) {
        result.setValue( argId, propValues.getValue( argId ) );
      }
      else {
        LoggerUtils.errorLogger().warning( "Command argument: " + argId + " not defined" ); //$NON-NLS-1$//$NON-NLS-2$
      }
    }
    return result;
  }

  /**
   * Возвращает набор описаний типов аргументов команды определенного типа.
   *
   * @param aCoreApi {@link ISkCoreApi} - API сервера
   * @return IStridablesList&lt;IDataDef> - набор описаний типов аргументов команды
   */
  public IStridablesList<IDataDef> argDefs( ISkCoreApi aCoreApi ) {
    if( classId.isBlank() || cmdId.isBlank() ) {
      return IStridablesList.EMPTY;
    }
    ISkClassInfo clsInfo = aCoreApi.sysdescr().findClassInfo( classId );
    if( clsInfo == null ) {
      return IStridablesList.EMPTY;
    }
    IDtoCmdInfo cmdInfo = clsInfo.cmds().list().findByKey( cmdId );
    if( cmdInfo == null ) {
      return IStridablesList.EMPTY;
    }
    return cmdInfo.argDefs();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat instanceof CmdArgValuesSet ) {
      return KEEPER.ent2str( this ).equals( KEEPER.ent2str( (CmdArgValuesSet)aThat ) );
    }
    return false;
  }
}
