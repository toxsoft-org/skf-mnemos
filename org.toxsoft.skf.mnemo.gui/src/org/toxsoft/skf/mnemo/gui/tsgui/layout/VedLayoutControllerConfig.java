package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;

/**
 * Реализация интерфейса {@link IVedLayoutControllerConfig}.
 *
 * @author vs
 */
public final class VedLayoutControllerConfig
    implements IVedLayoutControllerConfig, IParameterizedEdit {

  private static final String KW_PARAMS = "params"; //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ved.layout.controller.config"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<VedLayoutControllerConfig> KEEPER =
      new AbstractEntityKeeper<>( VedLayoutControllerConfig.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, VedLayoutControllerConfig aEntity ) {
          aSw.incNewLine();
          // item ID and factory ID
          aSw.writeAsIs( aEntity.kindId() );
          aSw.writeSeparatorChar();
          // properties values
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.propValues() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // parameters values
          StrioUtils.writeKeywordHeader( aSw, KW_PARAMS, true );
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
        }

        @Override
        protected VedLayoutControllerConfig doRead( IStrioReader aSr ) {
          // item ID and factory ID
          String kindId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // properties values
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          // parameters values
          StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          VedLayoutControllerConfig ctrlCfg = new VedLayoutControllerConfig( kindId, propValues, params );
          ctrlCfg.propValues().setAll( propValues );
          return ctrlCfg;
        }
      };

  private final String kindId;

  private final IOptionSetEdit propValues;

  private final IOptionSetEdit params;

  /**
   * Конструктор.
   *
   * @param aKindId String - ИД типа конфигурации
   * @param aPropValues {@link IOptionSet} - свойства конфигурации
   */
  public VedLayoutControllerConfig( String aKindId, IOptionSet aPropValues ) {
    kindId = aKindId;
    propValues = new OptionSet( aPropValues );
    params = new OptionSet();
  }

  /**
   * Конструктор.
   *
   * @param aKindId String - ИД типа конфигурации
   * @param aPropValues {@link IOptionSet} - свойства конфигурации
   * @param aParams {@link IOptionSet} - параметры конфигурации
   */
  public VedLayoutControllerConfig( String aKindId, IOptionSet aPropValues, IOptionSet aParams ) {
    kindId = aKindId;
    propValues = new OptionSet( aPropValues );
    params = new OptionSet( aParams );
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IVedLayoutControllerConfig
  //

  @Override
  public String kindId() {
    return kindId;
  }

  @Override
  public IOptionSetEdit propValues() {
    return propValues;
  }

}
