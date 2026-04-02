package org.toxsoft.skf.mnemo.gui.cmd;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Конфигурация команды для мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class MnemoCommandCfg
    extends StridableParameterized {

  /**
   * Property ID for command ugwi
   */
  public static final String PROPID_CMD_UGWI = "command.cfg.ugwi"; //$NON-NLS-1$

  /**
   * Propety ID for command arguments
   */
  public static final String PROPID_CMD_ARGS = "command.cfg.args"; //$NON-NLS-1$

  /**
   * Property ID for confirmationtext
   */
  public static final String PROPID_CMD_CONFIRM = "command.cfg.confirm"; //$NON-NLS-1$

  private static final String KW_PARAMS = "params"; //$NON-NLS-1$

  /**
   * The keeper id
   */
  public static final String KEEPER_ID = "MnemoCommandCfg"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<MnemoCommandCfg> KEEPER =
      new AbstractEntityKeeper<>( MnemoCommandCfg.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, MnemoCommandCfg aEntity ) {
          aSw.incNewLine();
          // item ID and factory ID
          aSw.writeAsIs( aEntity.id() );
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
        protected MnemoCommandCfg doRead( IStrioReader aSr ) {
          // item ID and commander ID
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // properties values
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // parameters values
          StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // create an item to read extra data into it
          MnemoCommandCfg itemCfg = new MnemoCommandCfg( id, propValues, params );
          return itemCfg;
        }
      };

  private final IOptionSetEdit propValues = new OptionSet();

  /**
   * Constructor.
   *
   * @param aId - identifier
   * @param aProps {@link IOptionSet} - properties
   * @param aParams {@link IOptionSet} - additional non-mandatory parameters
   */
  public MnemoCommandCfg( String aId, IOptionSet aProps, IOptionSet aParams ) {
    super( aId, aParams );
    propValues.addAll( aProps );
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
   * Возвращает значения свойств.
   *
   * @return {@link IOptionSetEdit} - редактируемый набор значений свойств
   */
  public IOptionSetEdit propValues() {
    return propValues;
  }

  /**
   * Returns command ugwi.
   *
   * @return Ugwi - command ugwi
   */
  public Ugwi cmdUgwi() {
    if( propValues.hasKey( PROPID_CMD_UGWI ) ) {
      return propValues.getValobj( PROPID_CMD_UGWI );
    }
    return Ugwi.NONE;
  }

  /**
   * Returns argument values.
   *
   * @return {@link IOptionSet} - command arguments
   */
  IOptionSet args() {
    if( propValues.hasKey( PROPID_CMD_ARGS ) ) {
      return propValues.getValobj( PROPID_CMD_ARGS );
    }
    return IOptionSet.NULL;
  }

  String cmdConfirmationText() {
    if( propValues.hasKey( PROPID_CMD_CONFIRM ) ) {
      return propValues.getStr( PROPID_CMD_CONFIRM );
    }
    return TsLibUtils.EMPTY_STRING;
  }

}
