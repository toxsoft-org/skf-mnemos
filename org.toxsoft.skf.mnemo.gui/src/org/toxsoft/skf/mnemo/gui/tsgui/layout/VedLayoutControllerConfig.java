package org.toxsoft.skf.mnemo.gui.tsgui.layout;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;

/**
 * Реализация интерфейса {@link IVedLayoutControllerConfig}.
 *
 * @author vs
 */
public final class VedLayoutControllerConfig
    implements IVedLayoutControllerConfig, IParameterizedEdit {

  private final String id;

  private final String kindId;

  private final IOptionSetEdit propValues = new OptionSet();

  private final IOptionSetEdit params = new OptionSet();

  /**
   * Конструктор.
   *
   * @param aId String - ИД конфигурации
   * @param aKindId String - ИД типа конфигурации
   */
  public VedLayoutControllerConfig( String aId, String aKindId ) {
    id = aId;
    kindId = aKindId;
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
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID, null );
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
