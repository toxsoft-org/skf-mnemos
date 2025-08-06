package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;

/**
 * Конфигурация "пользовательского" действия.
 * <p>
 *
 * @author vs
 */
public final class VedUserActionCfg
    extends StridableParameterized {

  private static final String KW_PARAMS = "params"; //$NON-NLS-1$

  /**
   * The keeper id
   */
  public static final String KEEPER_ID = "VedUserActionCfg"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<VedUserActionCfg> KEEPER =
      new AbstractEntityKeeper<>( VedUserActionCfg.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, VedUserActionCfg aEntity ) {
          aSw.incNewLine();
          // item ID and factory ID
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.commanderId() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // properties values
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.propValues() );
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
        protected VedUserActionCfg doRead( IStrioReader aSr ) {
          // item ID and commander ID
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String cmdrId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // properties values
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // parameters values
          StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // create an item to read extra data into it
          VedUserActionCfg itemCfg = new VedUserActionCfg( id, cmdrId, propValues, params );
          return itemCfg;
        }
      };

  private final String id;
  private final String commanderId;

  private final IOptionSetEdit propValues = new OptionSet();

  public VedUserActionCfg( String aId, String aCommanderId, IOptionSet aProps, IOptionSet aParams ) {
    id = aId;
    commanderId = aCommanderId;
    propValues.setAll( aProps );
    params().setAll( aParams );
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
   * Возвращает ИД коммандера.
   *
   * @return String - ИД коммандера
   */
  public String commanderId() {
    return commanderId;
  }

  /**
   * Возвращает значения свойств.
   *
   * @return {@link IOptionSetEdit} - редактируемый набор значений свойств
   */
  public IOptionSetEdit propValues() {
    return propValues;
  }

}
