package org.toxsoft.skf.mnemo.mned.pro.bkn;

import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

public class AspBknCreateMnemos
    extends AbstractSingleActionSetProvider {

  private final IVedScreen vedScreen;

  static final StringArrayList compStationInputs = new StringArrayList( //
      "TKA1_GPK", "TKA2_GPK", "TKA3_GPK", //
      "TKA1_PDD2", "TKA2_PDD2", "TKA3_PDD2", //
      "TKA1_GDZ", "TKA2_GDZ", "TKA3_GDZ", //
      "TKA2_DM", "TKA3_DM", "TKA1_DM" );

  static final StringArrayList compStationValves = new StringArrayList( //
      "TKA1_ZN", "TKA2_ZN", "TKA3_ZN", //
      "TKA1_PK", "TKA3_PK", "TKA2_PK", //
      "TKA1_DZ", "TKA3_DZ", "TKA2_DZ" );

  static final StringArrayList tkaTempInputs = new StringArrayList( //
      "TP2", "TP6", "TP7", "TP3", "TP4", "TP5", "TP8", "TP9", "TP1", "TV5", //
      "T11", "T12", "TV3", "RDP", "RDS", "RDM1", "GDZ", "DM", "TV1", "RDM2", //
      "TV2", "TM2", "TM4", "TV6", "TV4", "PDD2", "GVW", "TV7", "TM1", "RDV", "GPK" );

  static final StringArrayList tkaVibrationInputs = new StringArrayList( //
      "VSP5", "VSP6", "VSP2", "VSP3", "VSP4", "VSP7", "VSP8", "VSP1", "TV5", //
      "T11", "T12", "TV3", "RDP", "RDS", "RDM1", "GDZ", "DM", "TV1", "RDM2", //
      "TV2", "TM2", "TM4", "TV6", "TV4", "PDD2", "GVW", "TV7", "TM1", "RDV", "GPK" );

  static final StringArrayList airInputs = new StringArrayList( //
      "TP1", "VSP1", "TP2", "TP3", "VSP2", "DM", "TV5", "TV3", //
      "TV1", "GDZ", "PDD2", "TV6", "TV2", "TV4", "GPK" );

  static final StringArrayList oilInputs = new StringArrayList( //
      "RDM1", "RDP", "RDS", "TM4", "GVW", "TM2", "TM1", "RDM2" );

  static final StringArrayList waterInputs = new StringArrayList( //
      "T11", "T12", "GVW", "TV7", "RDV" );

  static final StringArrayList tkaTableInputs = new StringArrayList( //
      "TM1", "TP1", "TM2", "TP2", "TP3", "TM4", "TP4", "RDS", "TP5", "RDM1", //
      "TP6", "RDP", "TP7", "RDM2", "GVW", "TP8", "TP9", "TV1", "VSP1", "TV2", //
      "VSP2", "TV3", "VSP3", "TV4", "VSP4", "TV5", "VSP5", "TV6", "VSP6", "T7", //
      "VSP7", "T8", "VSP8", "T9", "PDD2", "DM", "T1", "T2", "GDZ", "T3", //
      "GPK", "T4", "T5", "TV7", "T6", "T11", "T12", "RDV" );

  static final StringArrayList allTkaTableInputs = new StringArrayList( //
      "TM1", "TP1", "TP2", "TM2", "TP3", "TM4", "TP4", "RDS", "TP5", "RDM1", //
      "TP6", "RDP", "TP7", "RDM2", "TP8", "GVW", "TP9", "TV1", "VSP1", "TV2", //
      "TV3", "VSP2", "VSP3", "TV4", "VSP4", "TV5", "TV6", "VSP5", "VSP6", "T7", //
      "VSP7", "T8", "T9", "VSP8", "PDD2", "DM", "T1", "GDZ", "T2", "GPK", //
      "T3", "T4", "TV7", "T5", "T6", "T11", "T12", "RDV" );

  public AspBknCreateMnemos( IVedScreen aVedScreen ) {
    super( TsActionDef.ofPush2( "actBknMnemos", "Мнемосхемы", "Создание мнемосхем для Байконура", ICONID_MNEMO_EDIT ) );
    vedScreen = aVedScreen;
  }

  @Override
  public void run() {

    // Созданние мнемосхемы компрессорной станции
    // BknMnemoBuilder builder = new BknMnemoBuilder( vedScreen );
    // builder.setMnemoProps( "tkaStation", "Компрессорная", "Мнемосхема Компрессорной станции" );
    // builder.setBkImage( "ru.toxsoft.bkn.gui", "images/comp-station-bkg.png" );
    // builder.setSvgFilePath( "C:\\works\\bkn\\Компрессорная-зачения.svg" );
    // builder.setSvgValvesPath( "C:\\works\\bkn\\Компрессорная-задвижки.svg" );
    // builder.build( compStationInputs, compStationValves );
    // -----------------------------------------------------

    // Созданние мнемосхемы компрессора с показанниями температуры
    // BknMnemoBuilder builder = new BknMnemoBuilder( vedScreen );
    // builder.setMnemoProps( "tkaTemperature", "ТКА температура", "Мнемосхема ТКА со значениями температуры" );
    // builder.setBkImage( "ru.toxsoft.bkn.gui", "images/tka-temp-bkg.png" );
    // builder.setSvgFilePath( "C:\\works\\bkn\\ТКА-1 температура.svg" );
    // builder.setMasterObjectClass( "AirCompressor" );
    // builder.build( tkaTempInputs, null );
    // -----------------------------------------------------

    // Созданние мнемосхемы компрессора с показанниями вибрации
    // BknMnemoBuilder builder = new BknMnemoBuilder( vedScreen );
    // builder.setMnemoProps( "tkaVibration", "ТКА вибрация", "Мнемосхема ТКА со значениями вибрации" );
    // builder.setBkImage( "ru.toxsoft.bkn.gui", "images/tka-vibration-bkg.png" );
    // builder.setSvgFilePath( "C:\\works\\bkn\\ТКА вибрация.svg" );
    // builder.setMasterObjectClass( "AirCompressor" );
    // builder.build( tkaVibrationInputs, null );
    // -----------------------------------------------------

    // Созданние мнемосхемы воздушного контура
    // BknMnemoBuilder builder = new BknMnemoBuilder( vedScreen );
    // builder.setMnemoProps( "airContour", "Воздушный контур", "Мнемосхема воздушного контура" );
    // builder.setBkImage( "ru.toxsoft.bkn.gui", "images/air-contour-bkg.png" );
    // builder.setSvgFilePath( "C:\\works\\bkn\\Воздушный контур.svg" );
    // builder.setMasterObjectClass( "AirCompressor" );
    // builder.build( airInputs, null );
    // -----------------------------------------------------

    // Созданние мнемосхемы масляного контура
    // BknMnemoBuilder builder = new BknMnemoBuilder( vedScreen );
    // builder.setMnemoProps( "oilContour", "Масляный контур", "Мнемосхема масляного контура" );
    // builder.setBkImage( "ru.toxsoft.bkn.gui", "images/oil-contour-bkg.png" );
    // builder.setSvgFilePath( "C:\\works\\bkn\\Масляный контур.svg" );
    // builder.setMasterObjectClass( "AirCompressor" );
    // builder.build( oilInputs, null );
    // -----------------------------------------------------

    // Созданние мнемосхемы водяного контура
    // BknMnemoBuilder builder = new BknMnemoBuilder( vedScreen );
    // builder.setMnemoProps( "waterContour", "Водяной контур", "Мнемосхема водяного контура" );
    // builder.setBkImage( "ru.toxsoft.bkn.gui", "images/water-contour-bkg.png" );
    // builder.setSvgFilePath( "C:\\works\\bkn\\Водяной контур.svg" );
    // builder.setMasterObjectClass( "AirCompressor" );
    // builder.build( waterInputs, null );
    // -----------------------------------------------------

    // Созданние мнемосхемы таблицы значений ТКА
    // BknMnemoBuilder builder = new BknMnemoBuilder( vedScreen );
    // builder.setMnemoProps( "tkaValuesTable", "Таблица значений", "Таблица значений одного компрессора" );
    // builder.setBkImage( "ru.toxsoft.bkn.gui", "images/tka-table-bkg.png" );
    // builder.setSvgFilePath( "C:\\works\\bkn\\ТКА таблица values.svg" );
    // builder.setMasterObjectClass( "AirCompressor" );
    // builder.build( tkaTableInputs, null );
    // -----------------------------------------------------

    // Созданние мнемосхемы таблицы значений всех ТКА
    BknMnemoBuilder builder = new BknMnemoBuilder( vedScreen );
    builder.setMnemoProps( "allTkaValuesTable", "Таблица всех значений", "Таблица значений всех компрессоров" );
    builder.setBkImage( "ru.toxsoft.bkn.gui", "images/all-tka-table-bkg.png" );
    builder.setSvgFilePath( "C:\\works\\bkn\\Все ТКА таблица.svg" );
    builder.setMasterObjectClass( "AirCompressor" );
    builder.buildTableMnemo( allTkaTableInputs );
    // -----------------------------------------------------

  }

}
