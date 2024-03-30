package org.toxsoft.skf.mnemo.gui.tsgui;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Объект, который служит для передачи данных (напр. в Clipboard) при операциях копирования и вставки.
 *
 * @author vs
 */
public class DtoCopyPasteInfo
    implements IParameterized {

  /**
   * The keeper singleton.
   * <p>
   * Read configuration may safely be casted to {@link DtoCopyPasteInfo}.
   */
  public static final IEntityKeeper<DtoCopyPasteInfo> KEEPER =
      new AbstractEntityKeeper<>( DtoCopyPasteInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        private static final String KW_VISEL_CONFIGS = "ViselConfigs"; //$NON-NLS-1$
        private static final String KW_ACTOR_CONFIGS = "ActorConfigs"; //$NON-NLS-1$

        @Override
        protected void doWrite( IStrioWriter aSw, DtoCopyPasteInfo aEntity ) {
          aSw.writeAsIs( aEntity.vedScreenId() );
          aSw.writeSeparatorChar();
          // visels2paste
          StrioUtils.writeCollection( aSw, KW_VISEL_CONFIGS, aEntity.viselConfigs(), VedItemCfg.KEEPER, true );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // actors2paste
          StrioUtils.writeCollection( aSw, KW_ACTOR_CONFIGS, aEntity.actorConfigs(), VedItemCfg.KEEPER, true );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.params() );
          aSw.writeEol();
        }

        @Override
        protected DtoCopyPasteInfo doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          // viselCfgs
          IList<IVedItemCfg> viselCfgs = StrioUtils.readCollection( aSr, KW_VISEL_CONFIGS, VedItemCfg.KEEPER );
          aSr.ensureSeparatorChar();
          // actorCfgs
          IList<IVedItemCfg> actorCfgs = StrioUtils.readCollection( aSr, KW_ACTOR_CONFIGS, VedItemCfg.KEEPER );
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER_INDENTED.read( aSr );
          return new DtoCopyPasteInfo( id, viselCfgs, actorCfgs, params );
        }
      };

  private final IOptionSetEdit params = new OptionSet();

  private final String vedScreenId;

  private final IStridablesListEdit<IVedItemCfg> visels2paste = new StridablesList<>();

  private final IStridablesListEdit<IVedItemCfg> actors2paste = new StridablesList<>();

  /**
   * Конструктор со всми инвариантами.
   *
   * @param aVedScreenId String - ИД экрана редактора
   * @param aViselConfs IList&lt;IVedItemCfg> - список конвигураций визуальных элементов
   * @param aActorConfs IList&lt;IVedItemCfg> - список конфигураций акторов
   * @param aParams {@link IOptionSet} - набор дополнительных параметров
   */
  public DtoCopyPasteInfo( String aVedScreenId, IList<IVedItemCfg> aViselConfs, //
      IList<IVedItemCfg> aActorConfs, IOptionSet aParams ) {
    vedScreenId = aVedScreenId;
    visels2paste.addAll( aViselConfs );
    actors2paste.addAll( aActorConfs );
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSet params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает идентификатор экрана редактирования.
   *
   * @return String - идентификатор экрана редактирования
   */
  String vedScreenId() {
    return vedScreenId;
  }

  /**
   * Возвращает список конфигураций визуальных элементов.
   *
   * @return IStridablesList&lt;IVedItemCfg> - список конфигураций визуальных элементов
   */
  public IStridablesList<IVedItemCfg> viselConfigs() {
    return visels2paste;
  }

  /**
   * Возвращает список конфигураций визуальных элементов.
   *
   * @return IStridablesList&lt;IVedItemCfg> - список конфигураций визуальных элементов
   */
  public IStridablesList<IVedItemCfg> actorConfigs() {
    return actors2paste;
  }

}
