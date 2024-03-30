package org.toxsoft.skf.mnemo.gui.tsgui.layout.table;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.layout.*;

/**
 * Контроллер размещения, который располагает подконтрольные элементы в одну строку.
 * <p>
 *
 * @author vs
 */
public class VedTableLayoutController
    implements IVedViselsLayoutController {

  /**
   * ИД типа контроллера размещения
   */
  public static final String LAYOUT_KIND = "ved.layout.table"; //$NON-NLS-1$

  private final VedTableLayoutControllerConfig config;

  private final IVedScreen vedScreen;

  D2GridMargins margins = new D2GridMargins( 4 );

  int cellsQtty = 1;

  IStringMapEdit<CellLayoutData> cellConfigs = new StringMap<>();

  /**
   * Конструктор.
   *
   * @param aCfg {@link IVedLayoutControllerConfig} - конфигурация контроллера
   * @param aVedScreen {@link IVedScreen} - экран редактора
   */
  public VedTableLayoutController( IVedLayoutControllerConfig aCfg, IVedScreen aVedScreen ) {
    if( aCfg != null ) {
      TsIllegalArgumentRtException.checkFalse( aCfg.kindId().equals( LAYOUT_KIND ) );
    }
    config = (VedTableLayoutControllerConfig)aCfg;
    vedScreen = aVedScreen;
  }

  // ------------------------------------------------------------------------------------
  // IVedViselsLayoutController
  //

  @Override
  public String kindId() {
    return LAYOUT_KIND;
  }

  @Override
  public void doLayout( String aMasterId, IStringList aSlaveIds ) {
    IStringListEdit idsToRemove = new StringArrayList();
    for( String slaveId : aSlaveIds ) {
      if( !cellConfigs.keys().hasElem( slaveId ) ) {
        idsToRemove.add( slaveId );
      }
    }

    for( String id : idsToRemove ) {
      cellConfigs.removeByKey( id );
      cellConfigs.put( id, new CellLayoutData() );
    }

    for( String id : aSlaveIds ) {
      if( !cellConfigs.keys().hasElem( id ) ) {
        cellConfigs.put( id, new CellLayoutData() );
      }
    }

    VedAbstractVisel masterVisel = VedScreenUtils.findVisel( aMasterId, vedScreen );
    double currX = masterVisel.props().getDouble( PROPID_X ) + margins.left();
    double currY = masterVisel.props().getDouble( PROPID_Y ) + margins.top();
    for( String id : cellConfigs.keys() ) {
      VedAbstractVisel visel = VedScreenUtils.findVisel( id, vedScreen );
      visel.props().setPropPairs( PROPID_X, currX, PROPID_Y, currY );
      double w = calcCellWidth( id, cellConfigs.getByKey( id ) );
      currX += w;
    }

  }

  @Override
  public IVedLayoutControllerConfig getConfiguration() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  double calcCellWidth( String aViselId, CellLayoutData aCellData ) {
    VedAbstractVisel visel = VedScreenUtils.findVisel( aViselId, vedScreen );
    double mw = aCellData.margins().left() + aCellData.margins().right();
    // if( !aCellData.grabExtraWidth ) {
    return mw + visel.bounds().width();
    // }
    // return mw;
  }

}
