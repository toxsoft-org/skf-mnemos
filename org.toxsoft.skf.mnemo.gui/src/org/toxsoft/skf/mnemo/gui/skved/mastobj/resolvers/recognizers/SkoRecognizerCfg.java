package org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;

/**
 * Разрешенная реализация {@link ISkoRecognizerCfg}.
 * <p>
 *
 * @author vs
 */
public final class SkoRecognizerCfg
    implements ISkoRecognizerCfg {

  /**
   * Keeper Id
   */
  public static final String KEEPER_ID = "SkoRecognizerCfgKeeper"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ISkoRecognizerCfg> KEEPER =
      new AbstractEntityKeeper<>( ISkoRecognizerCfg.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ISkoRecognizerCfg aEntity ) {
          aSw.incNewLine();
          // item ID and kind
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.kind().id() );
          aSw.writeSeparatorChar();
          // aSw.writeAsIs( aEntity.factoryId() );
          // aSw.writeSeparatorChar();
          aSw.writeEol();
          // properties values
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.propValues() );
        }

        @Override
        protected ISkoRecognizerCfg doRead( IStrioReader aSr ) {
          // item ID and kind
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          ESkoRecognizerKind kind = ESkoRecognizerKind.getById( aSr.readIdName() );
          aSr.ensureSeparatorChar();
          // String factoryId = aSr.readIdPath();
          // aSr.ensureSeparatorChar();
          // properties values
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          // SkoRecognizerCfg itemCfg = new SkoRecognizerCfg( id, kind, factoryId, propValues );
          SkoRecognizerCfg itemCfg = new SkoRecognizerCfg( id, kind, propValues );
          return itemCfg;
        }
      };

  private final String id;

  private final ESkoRecognizerKind kind;

  private final IOptionSetEdit opSet;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aId String - ИД конфигурации
   * @param aKind {@link ESkoRecognizerKind} - тип "распознавателя"
   * @param aProps {@link IOptionSet} - значения свойств "раcпознавателя"
   */
  public SkoRecognizerCfg( String aId, ESkoRecognizerKind aKind, IOptionSet aProps ) {
    id = aId;
    kind = aKind;
    opSet = new OptionSet( aProps );
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
    return kind.nmName();
  }

  @Override
  public String description() {
    return kind.description();
  }

  // ------------------------------------------------------------------------------------
  // ISkoRecognizerCfg
  //

  @Override
  public ESkoRecognizerKind kind() {
    return kind;
  }

  @Override
  public IOptionSet propValues() {
    return opSet;
  }

}
