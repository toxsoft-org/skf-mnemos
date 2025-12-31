package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.ext.mastobj.gui.main.resolver.*;

/**
 * Информация для "разрешителя" передаваемого мастер-объекта в мастер-объект мнемосхемы.
 * <p>
 * Например, если из мнемосхемы компрессора вызывается диалог с мнемосхемой аналогового входа, то необходима информация
 * по которой для конкретного объекта компрессора, можно будет добраться до конкретного объекта класса "аналоговый
 * вход".
 *
 * @author vs
 */
public class PopupMnemoResolverConfig {

  /**
   * Empty info.
   */
  public static final PopupMnemoResolverConfig EMPTY =
      new PopupMnemoResolverConfig( IStridable.NONE_ID, CompoundResolverConfig.NONE );

  private static final String KW_SUBMASTER = "resolver"; //$NON-NLS-1$

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "PopupMnemoResolverConfig"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   * <p>
   */
  public static final IEntityKeeper<PopupMnemoResolverConfig> KEEPER =
      new AbstractEntityKeeper<>( PopupMnemoResolverConfig.class, EEncloseMode.ENCLOSES_BASE_CLASS, EMPTY ) {

        @Override
        protected void doWrite( IStrioWriter aSw, PopupMnemoResolverConfig aEntity ) {
          aSw.incNewLine();
          aSw.writeAsIs( aEntity.masterClassId() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.masterObjId() );
          aSw.writeSeparatorChar();
          StrioUtils.writeKeywordHeader( aSw, KW_SUBMASTER, true );
          CompoundResolverConfig.KEEPER.write( aSw, aEntity.resolverConfig() );
          aSw.decNewLine();
        }

        @Override
        protected PopupMnemoResolverConfig doRead( IStrioReader aSr ) {
          String ownerClassId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String objId = aSr.readIdPath();
          if( !objId.isBlank() ) {
            return new PopupMnemoResolverConfig( ownerClassId, objId );
          }
          aSr.ensureSeparatorChar();
          ICompoundResolverConfig resCfg = CompoundResolverConfig.KEEPER.read( aSr );
          return new PopupMnemoResolverConfig( ownerClassId, resCfg );
        }
      };

  private final String masterClassId;

  private final String masterObjId;

  private final ICompoundResolverConfig resolverCfg;

  /**
   * Constructor.
   *
   * @param aMasterClsId String - ИД класса мастер-объекта родительской мнемосхемы
   * @param aResolverCfg {@link ICompoundResolverConfig} - конфигурация разрешителя
   */
  public PopupMnemoResolverConfig( String aMasterClsId, ICompoundResolverConfig aResolverCfg ) {
    TsIllegalArgumentRtException.checkFalse( StridUtils.isValidIdPath( aMasterClsId ) );
    masterClassId = aMasterClsId;
    resolverCfg = aResolverCfg;
    masterObjId = TsLibUtils.EMPTY_STRING;
  }

  /**
   * Constructor.
   *
   * @param aMasterClsId String - ИД класса мастер-объекта родительской мнемосхемы
   * @param aMasterObjId String - ИД мастер-объекта
   */
  public PopupMnemoResolverConfig( String aMasterClsId, String aMasterObjId ) {
    TsIllegalArgumentRtException.checkFalse( StridUtils.isValidIdPath( aMasterClsId ) );
    TsIllegalArgumentRtException.checkFalse( StridUtils.isValidIdPath( aMasterObjId ) );
    masterClassId = aMasterClsId;
    masterObjId = aMasterObjId;
    resolverCfg = CompoundResolverConfig.NONE;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает ИД класса мастер-объекта.
   *
   * @return String - ИД класса мастер-объекта
   */
  public String masterClassId() {
    return masterClassId;
  }

  /**
   * Возвращает ИД мастер-объекта.
   *
   * @return String - ИД мастер-объекта м.б. пустая строка (если задан путь к мастер-объекту)
   */
  public String masterObjId() {
    return masterObjId;
  }

  /**
   * Возвращает конфигурацию разрешителя.
   *
   * @return {@link ICompoundResolverConfig} - конфигурация разрешителя м.б. пустой (если задан ИД мастер-объекта)
   */
  public ICompoundResolverConfig resolverConfig() {
    return resolverCfg;
  }

}
