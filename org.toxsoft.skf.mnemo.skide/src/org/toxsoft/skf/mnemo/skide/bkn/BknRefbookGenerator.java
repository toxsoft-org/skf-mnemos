package org.toxsoft.skf.mnemo.skide.bkn;

import static org.toxsoft.core.tsgui.ved.comps.EButtonViselState.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.refbooks.lib.*;
import org.toxsoft.skf.refbooks.lib.impl.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.impl.dto.*;

public class BknRefbookGenerator {

  static class RefookItemBuider {

    IOptionSetEdit attrValues = new OptionSet();

    private final Skid   skid;
    private final String name;

    RefookItemBuider( String aId, String aName, ISkRefbook aRefbook ) {
      String classId = aRefbook.itemClassId();
      skid = new Skid( classId, aId );
      name = aName;

      DtoRefbookInfo rbInfo = DtoRefbookInfo.of( aRefbook );
      for( IDtoAttrInfo ai : rbInfo.attrInfos() ) {
        attrValues.put( ai.id(), IAtomicValue.NULL );
      }
    }

    void setValue( String aFieldId, IAtomicValue aValue ) {
      attrValues.put( aFieldId, aValue );
    }

    IDtoFullObject buildItem() {
      IDtoObject dtoObj = new DtoObject( skid, attrValues, IStringMap.EMPTY );
      DtoFullObject retVal = new DtoFullObject( dtoObj, IStringMap.EMPTY, IStringMap.EMPTY );
      retVal.attrs().setStr( TSID_NAME, name );
      return retVal;
    }
  }

  private static final String FID_KEY        = "key";       //$NON-NLS-1$
  private static final String FID_DESCR      = "descr";     //$NON-NLS-1$
  private static final String FID_BK_COLOR   = "bkColor";   //$NON-NLS-1$
  private static final String FID_TEXT_COLOR = "textColor"; //$NON-NLS-1$

  public static void createAiDecorationRefbook( ISkConnection aSkConn ) {
    String rbId = "aiDecorator"; //$NON-NLS-1$
    ISkRefbookService rbs = aSkConn.coreApi().getService( ISkRefbookService.SERVICE_ID );
    ISkRefbook refbook = rbs.findRefbook( rbId );
    if( refbook != null ) {
      rbs.removeRefbook( rbId );
    }

    // Создадим структуру справочника
    DtoRefbookInfo rbInfo = createRefbookInfo( rbId, "Цвета аналогового входа", //
        "Цвета текста и фона для различных состояний аналогового входа" );
    addAtomicAttr( FID_KEY, "ключ", "Ключевое поле справочника", EAtomicType.INTEGER, rbInfo );
    // addAtomicAttr( FID_DESCR, "Описание", "Описание состояния", EAtomicType.STRING, rbInfo );
    addValobjAttr( FID_BK_COLOR, "Фон", "Цвет фона", TsFillInfo.KEEPER_ID, rbInfo );
    addValobjAttr( FID_TEXT_COLOR, "Текст", "Цвет текста", RGBAKeeper.KEEPER_ID, rbInfo );

    rbs.defineRefbook( rbInfo );

    refbook = rbs.findRefbook( rbId );
    if( refbook != null ) { // Заполним справочник значениями
      IStridablesList<ISkRefbookItem> items = refbook.listItems();
      System.out.println( "items count = " + items.size() );

      RefookItemBuider b = new RefookItemBuider( "key0", "Норма", refbook );
      b.setValue( FID_KEY, avInt( 0 ) );
      // b.setValue( FID_DESCR, avStr( "Норма" ) ); //$NON-NLS-1$
      b.setValue( FID_BK_COLOR, avValobj( new TsFillInfo( new RGBA( 189, 189, 188, 255 ) ) ) );
      b.setValue( FID_TEXT_COLOR, avValobj( new RGBA( 0, 0, 0, 255 ) ) );
      IDtoFullObject rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key1", "Переполнение", refbook );
      b.setValue( FID_KEY, avInt( 1 ) );
      b.setValue( FID_BK_COLOR, avValobj( new TsFillInfo( new RGBA( 0, 0, 0, 255 ) ) ) );
      b.setValue( FID_TEXT_COLOR, avValobj( new RGBA( 255, 0, 0, 255 ) ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key2", "Перегруз", refbook );
      b.setValue( FID_KEY, avInt( 2 ) );
      b.setValue( FID_BK_COLOR, avValobj( new TsFillInfo( new RGBA( 0, 0, 0, 255 ) ) ) );
      b.setValue( FID_TEXT_COLOR, avValobj( new RGBA( 255, 255, 0, 255 ) ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key3", "Датчик отключен", refbook );
      b.setValue( FID_KEY, avInt( 3 ) );
      b.setValue( FID_BK_COLOR, avValobj( new TsFillInfo( new RGBA( 0, 0, 255, 255 ) ) ) );
      b.setValue( FID_TEXT_COLOR, avValobj( new RGBA( 255, 255, 255, 255 ) ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key4", "Предупрежедение", refbook );
      b.setValue( FID_KEY, avInt( 4 ) );
      b.setValue( FID_BK_COLOR, avValobj( new TsFillInfo( new RGBA( 255, 255, 0, 255 ) ) ) );
      b.setValue( FID_TEXT_COLOR, avValobj( new RGBA( 0, 0, 0, 255 ) ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key5", "Авария", refbook );
      b.setValue( FID_KEY, avInt( 5 ) );
      b.setValue( FID_BK_COLOR, avValobj( new TsFillInfo( new RGBA( 255, 0, 0, 255 ) ) ) );
      b.setValue( FID_TEXT_COLOR, avValobj( new RGBA( 255, 255, 255, 255 ) ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key6", "Нет питания", refbook );
      b.setValue( FID_KEY, avInt( 6 ) );
      b.setValue( FID_BK_COLOR, avValobj( new TsFillInfo( new RGBA( 0, 0, 0, 255 ) ) ) );
      b.setValue( FID_TEXT_COLOR, avValobj( new RGBA( 255, 255, 255, 255 ) ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key7", "Неопределено", refbook );
      b.setValue( FID_KEY, avInt( -1 ) );
      b.setValue( FID_BK_COLOR, avValobj( new TsFillInfo( new RGBA( 255, 255, 255, 255 ) ) ) );
      b.setValue( FID_TEXT_COLOR, avValobj( new RGBA( 255, 255, 255, 255 ) ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );
    }
  }

  private static final String FID_VERT_IMAGE    = "vertImage";   //$NON-NLS-1$
  private static final String FID_HOR_IMAGE     = "horImage";    //$NON-NLS-1$
  private static final String FID_ZV_VERT_IMAGE = "zvVertImage"; //$NON-NLS-1$
  private static final String FID_ZV_HOR_IMAGE  = "zvHorImage";  //$NON-NLS-1$

  public static void createRevrsibleEngineStateRefbook( ISkConnection aSkConn ) {
    String rbId = "reversibleEngineState"; //$NON-NLS-1$
    ISkRefbookService rbs = aSkConn.coreApi().getService( ISkRefbookService.SERVICE_ID );
    ISkRefbook refbook = rbs.findRefbook( rbId );
    if( refbook != null ) {
      rbs.removeRefbook( rbId );
    }

    // Создадим структуру справочника
    DtoRefbookInfo rbInfo = createRefbookInfo( rbId, "Состояния реверсивного двигателя", //
        "Изображения для различных состояний реверсивного двигателя" );
    addAtomicAttr( FID_KEY, "ключ", "Ключевое поле справочника", EAtomicType.INTEGER, rbInfo );
    addValobjAttr( FID_VERT_IMAGE, "Вертикально", "Вертикальная ориентация", TsImageDescriptor.KEEPER_ID, rbInfo );
    addValobjAttr( FID_HOR_IMAGE, "Горизонтально", "Горизонтальная ориентация", TsImageDescriptor.KEEPER_ID, rbInfo );
    addValobjAttr( FID_ZV_VERT_IMAGE, "ЗВ Вертикально", "ЗВ Вертикальная ориентация", TsImageDescriptor.KEEPER_ID,
        rbInfo );
    addValobjAttr( FID_ZV_HOR_IMAGE, "ЗВ Горизонтально", "ЗВ Горизонтальная ориентация", TsImageDescriptor.KEEPER_ID,
        rbInfo );

    rbs.defineRefbook( rbInfo );

    refbook = rbs.findRefbook( rbId );
    if( refbook != null ) { // Заполним справочник значениями
      RefookItemBuider b;
      IDtoFullObject rbItem;
      TsImageDescriptor vImd;
      TsImageDescriptor hImd;
      TsImageDescriptor zvVImd;
      TsImageDescriptor zvHImd;
      String pluginId = "ru.toxsoft.bkn.gui";

      b = new RefookItemBuider( "key0", "Авария", refbook );
      b.setValue( FID_KEY, avInt( 0 ) );
      vImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_red.png" );
      hImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_red_hor.png" );
      b.setValue( FID_VERT_IMAGE, avValobj( vImd ) );
      b.setValue( FID_HOR_IMAGE, avValobj( hImd ) );
      zvVImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_red.png" );
      zvHImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_red_hor.png" );
      b.setValue( FID_ZV_VERT_IMAGE, avValobj( zvVImd ) );
      b.setValue( FID_ZV_HOR_IMAGE, avValobj( zvHImd ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key1", "Открыт", refbook );
      b.setValue( FID_KEY, avInt( 1 ) );
      vImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_green.png" );
      hImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_green_hor.png" );
      b.setValue( FID_VERT_IMAGE, avValobj( vImd ) );
      b.setValue( FID_HOR_IMAGE, avValobj( hImd ) );
      zvVImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_green.png" );
      zvHImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_green_hor.png" );
      b.setValue( FID_ZV_VERT_IMAGE, avValobj( zvVImd ) );
      b.setValue( FID_ZV_HOR_IMAGE, avValobj( zvHImd ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key2", "Закрыт", refbook );
      b.setValue( FID_KEY, avInt( 2 ) );
      vImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_grey.png" );
      hImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_grey_hor.png" );
      b.setValue( FID_VERT_IMAGE, avValobj( vImd ) );
      b.setValue( FID_HOR_IMAGE, avValobj( hImd ) );
      zvVImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_grey.png" );
      zvHImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_grey_hor.png" );
      b.setValue( FID_ZV_VERT_IMAGE, avValobj( zvVImd ) );
      b.setValue( FID_ZV_HOR_IMAGE, avValobj( zvHImd ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key3", "Промежуточное", refbook );
      b.setValue( FID_KEY, avInt( 3 ) );
      vImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_yellow.png" );
      hImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_yellow_hor.png" );
      b.setValue( FID_VERT_IMAGE, avValobj( vImd ) );
      b.setValue( FID_HOR_IMAGE, avValobj( hImd ) );
      zvVImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_yellow.png" );
      zvHImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_yellow_hor.png" );
      b.setValue( FID_ZV_VERT_IMAGE, avValobj( zvVImd ) );
      b.setValue( FID_ZV_HOR_IMAGE, avValobj( zvHImd ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key4", "Открывается", refbook );
      b.setValue( FID_KEY, avInt( 4 ) );
      vImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_opening.gif" );
      hImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_opening_hor.gif" );
      b.setValue( FID_VERT_IMAGE, avValobj( vImd ) );
      b.setValue( FID_HOR_IMAGE, avValobj( hImd ) );
      zvVImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_opening.gif" );
      zvHImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_opening_hor.gif" );
      b.setValue( FID_ZV_VERT_IMAGE, avValobj( zvVImd ) );
      b.setValue( FID_ZV_HOR_IMAGE, avValobj( zvHImd ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key5", "Закрывается", refbook );
      b.setValue( FID_KEY, avInt( 5 ) );
      vImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_closing.gif" );
      hImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_closing_hor.gif" );
      b.setValue( FID_VERT_IMAGE, avValobj( vImd ) );
      b.setValue( FID_HOR_IMAGE, avValobj( hImd ) );
      zvVImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_closing.gif" );
      zvHImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_closing_hor.gif" );
      b.setValue( FID_ZV_VERT_IMAGE, avValobj( zvVImd ) );
      b.setValue( FID_ZV_HOR_IMAGE, avValobj( zvHImd ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key6", "Неопределено", refbook );
      b.setValue( FID_KEY, avInt( -1 ) );
      vImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_white.png" );
      hImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_white_hor.png" );
      b.setValue( FID_VERT_IMAGE, avValobj( vImd ) );
      b.setValue( FID_HOR_IMAGE, avValobj( hImd ) );
      zvVImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_white.png" );
      zvHImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/zv_white_hor.png" );
      b.setValue( FID_ZV_VERT_IMAGE, avValobj( zvVImd ) );
      b.setValue( FID_ZV_HOR_IMAGE, avValobj( zvHImd ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

    }
  }

  private static final String FID_BTN_STOP          = "btnStop";        //$NON-NLS-1$
  private static final String FID_BTN_START         = "btnStart";       //$NON-NLS-1$
  private static final String FID_BTN_REWIND        = "btnRewind";      //$NON-NLS-1$
  private static final String FID_BTN_FAST_FORWARD  = "btnFastForward"; //$NON-NLS-1$
  private static final String FID_BTN_SKIP_TO_START = "btnSkipToStart"; //$NON-NLS-1$
  private static final String FID_BTN_SKIP_TO_END   = "btnSkipToEnd";   //$NON-NLS-1$

  public static void createButtonImagesRefbook( ISkConnection aSkConn ) {
    String rbId = "buttonImages"; //$NON-NLS-1$
    ISkRefbookService rbs = aSkConn.coreApi().getService( ISkRefbookService.SERVICE_ID );
    ISkRefbook refbook = rbs.findRefbook( rbId );
    if( refbook != null ) {
      rbs.removeRefbook( rbId );
    }

    // Создадим структуру справочника
    DtoRefbookInfo rbInfo = createRefbookInfo( rbId, "Картинки состояния кнопок", //
        "Изображения для различных состояний кнопок управления двигателями" );
    addAtomicAttr( FID_KEY, "Состояние (ключ)", "Состояние кнопки", EAtomicType.STRING, rbInfo );

    String imdKeeperId = TsImageDescriptor.KEEPER_ID;
    addValobjAttr( NORMAL.id(), NORMAL.nmName(), NORMAL.description(), imdKeeperId, rbInfo );
    addValobjAttr( PRESSED.id(), PRESSED.nmName(), PRESSED.description(), imdKeeperId, rbInfo );
    addValobjAttr( WORKING.id(), WORKING.nmName(), WORKING.description(), imdKeeperId, rbInfo );
    addValobjAttr( DISABLED.id(), DISABLED.nmName(), DISABLED.description(), imdKeeperId, rbInfo );
    addValobjAttr( SELECTED.id(), SELECTED.nmName(), SELECTED.description(), imdKeeperId, rbInfo );

    // addValobjAttr( FID_BTN_STOP, "Стоп", "Кнопка стоп", imdKeeperId, rbInfo );
    // addValobjAttr( FID_BTN_START, "Сарт", "Кнопка старт", imdKeeperId, rbInfo );
    // addValobjAttr( FID_BTN_REWIND, "Закрывать", "Постепенное закрывание задвижки", imdKeeperId, rbInfo );
    // addValobjAttr( FID_BTN_FAST_FORWARD, "Открывать", "Постепенное открывание задвижки", imdKeeperId, rbInfo );
    // addValobjAttr( FID_BTN_SKIP_TO_START, "Закрыть", "Полностью закрыть задвижку", imdKeeperId, rbInfo );
    // addValobjAttr( FID_BTN_SKIP_TO_END, "Открыть", "Полностью открыть задвижку", imdKeeperId, rbInfo );

    rbs.defineRefbook( rbInfo );

    refbook = rbs.findRefbook( rbId );
    if( refbook != null ) { // Заполним справочник значениями
      RefookItemBuider b;
      IDtoFullObject rbItem;
      TsImageDescriptor normalImd;
      TsImageDescriptor pressedImd;
      TsImageDescriptor workingImd;
      TsImageDescriptor disabledImd;
      TsImageDescriptor selectedImd;
      String pluginId = "ru.toxsoft.bkn.gui";

      b = new RefookItemBuider( FID_BTN_STOP, "Остановить работу (напр. открытие/закрытие )", refbook );
      b.setValue( FID_KEY, avStr( FID_BTN_STOP ) );
      normalImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/stop-normal.png" );
      pressedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/stop-pressed.png" );
      workingImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/stop-pressed.png" );
      disabledImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/stop-normal.png" );
      selectedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/stop-pressed.png" );
      b.setValue( NORMAL.id(), avValobj( normalImd ) );
      b.setValue( PRESSED.id(), avValobj( pressedImd ) );
      b.setValue( WORKING.id(), avValobj( workingImd ) );
      b.setValue( DISABLED.id(), avValobj( disabledImd ) );
      b.setValue( SELECTED.id(), avValobj( selectedImd ) );
      refbook.defineItem( b.buildItem() );

      b = new RefookItemBuider( FID_BTN_START, "Начать работу (напр. открытие/закрытие )", refbook );
      b.setValue( FID_KEY, avStr( FID_BTN_START ) );
      normalImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/start-normal.png" );
      pressedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/start-pressed.png" );
      workingImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/start-pressed.png" );
      disabledImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/start-normal.png" );
      selectedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/start-pressed.png" );
      b.setValue( NORMAL.id(), avValobj( normalImd ) );
      b.setValue( PRESSED.id(), avValobj( pressedImd ) );
      b.setValue( WORKING.id(), avValobj( workingImd ) );
      b.setValue( DISABLED.id(), avValobj( disabledImd ) );
      b.setValue( SELECTED.id(), avValobj( selectedImd ) );
      refbook.defineItem( b.buildItem() );

      b = new RefookItemBuider( FID_BTN_REWIND, "Открывать пока нажата", refbook );
      b.setValue( FID_KEY, avStr( FID_BTN_REWIND ) );
      normalImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/rewind-normal.png" );
      pressedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/rewind-pressed.png" );
      workingImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/rewind-pressed.png" );
      disabledImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/rewind-normal.png" );
      selectedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/rewind-pressed.png" );
      b.setValue( NORMAL.id(), avValobj( normalImd ) );
      b.setValue( PRESSED.id(), avValobj( pressedImd ) );
      b.setValue( WORKING.id(), avValobj( workingImd ) );
      b.setValue( DISABLED.id(), avValobj( disabledImd ) );
      b.setValue( SELECTED.id(), avValobj( selectedImd ) );
      refbook.defineItem( b.buildItem() );

      b = new RefookItemBuider( FID_BTN_FAST_FORWARD, "Закрывать пока нажата", refbook );
      b.setValue( FID_KEY, avStr( FID_BTN_FAST_FORWARD ) );
      normalImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/fast-forward-normal.png" );
      pressedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/fast-forward-pressed.png" );
      workingImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/fast-forward-pressed.png" );
      disabledImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/fast-forward-normal.png" );
      selectedImd =
          TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/fast-forward-pressed.png" );
      b.setValue( NORMAL.id(), avValobj( normalImd ) );
      b.setValue( PRESSED.id(), avValobj( pressedImd ) );
      b.setValue( WORKING.id(), avValobj( workingImd ) );
      b.setValue( DISABLED.id(), avValobj( disabledImd ) );
      b.setValue( SELECTED.id(), avValobj( selectedImd ) );
      refbook.defineItem( b.buildItem() );

      b = new RefookItemBuider( FID_BTN_SKIP_TO_END, "Закрыть полностью", refbook );
      b.setValue( FID_KEY, avStr( FID_BTN_SKIP_TO_END ) );
      normalImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-end-normal.png" );
      pressedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-end-pressed.png" );
      workingImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-end-pressed.png" );
      disabledImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-end-normal.png" );
      selectedImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-end-pressed.png" );
      b.setValue( NORMAL.id(), avValobj( normalImd ) );
      b.setValue( PRESSED.id(), avValobj( pressedImd ) );
      b.setValue( WORKING.id(), avValobj( workingImd ) );
      b.setValue( DISABLED.id(), avValobj( disabledImd ) );
      b.setValue( SELECTED.id(), avValobj( selectedImd ) );
      refbook.defineItem( b.buildItem() );

      b = new RefookItemBuider( FID_BTN_SKIP_TO_START, "Открыть полностью", refbook );
      b.setValue( FID_KEY, avStr( FID_BTN_SKIP_TO_START ) );
      normalImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-start-normal.png" );
      pressedImd =
          TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-start-pressed.png" );
      workingImd =
          TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-start-pressed.png" );
      disabledImd =
          TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-start-normal.png" );
      selectedImd =
          TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/buttons/skip-to-start-pressed.png" );
      b.setValue( NORMAL.id(), avValobj( normalImd ) );
      b.setValue( PRESSED.id(), avValobj( pressedImd ) );
      b.setValue( WORKING.id(), avValobj( workingImd ) );
      b.setValue( DISABLED.id(), avValobj( disabledImd ) );
      b.setValue( SELECTED.id(), avValobj( selectedImd ) );
      refbook.defineItem( b.buildItem() );
    }
  }

  static DtoRefbookInfo createRefbookInfo( String aId, String aName, String aDescription ) {
    IOptionSetEdit params = new OptionSet();
    params.setStr( TSID_NAME, aName );
    params.setStr( TSID_DESCRIPTION, aDescription );
    return new DtoRefbookInfo( aId, params );
  }

  static void addAtomicAttr( String aId, String aName, String aDescr, EAtomicType aType, DtoRefbookInfo aRbInfo ) {
    IOptionSetEdit params = new OptionSet();
    params.setStr( TSID_NAME, aName );
    params.setStr( TSID_DESCRIPTION, aDescr );
    IDataDef dd = fromAtomicType( aType );
    DtoAttrInfo ai = DtoAttrInfo.create1( aId, dd, params );
    aRbInfo.attrInfos().add( ai );
  }

  private static final String FID_HEATER          = "heaterImage";        //$NON-NLS-1$
  private static final String FID_OIL_PUMP        = "oilPumpImage";       //$NON-NLS-1$
  private static final String FID_MAIN_SWITCH     = "mainSwitchImage";    //$NON-NLS-1$
  private static final String FID_MAIN_BIG_SWITCH = "mainBigSwitchImage"; //$NON-NLS-1$

  public static void createIrrevrsibleEngineStateRefbook( ISkConnection aSkConn ) {
    String rbId = "irreversibleEngineState"; //$NON-NLS-1$
    ISkRefbookService rbs = aSkConn.coreApi().getService( ISkRefbookService.SERVICE_ID );
    ISkRefbook refbook = rbs.findRefbook( rbId );
    if( refbook != null ) {
      rbs.removeRefbook( rbId );
    }

    // Создадим структуру справочника
    DtoRefbookInfo rbInfo = createRefbookInfo( rbId, "Состояния нереверсивного двигателя", //
        "Изображения для различных состояний нереверсивного двигателя" );
    addAtomicAttr( FID_KEY, "ключ", "Ключевое поле справочника", EAtomicType.INTEGER, rbInfo );
    addValobjAttr( FID_HEATER, "Нагреватель", "Нагреватель масла", TsImageDescriptor.KEEPER_ID, rbInfo );
    addValobjAttr( FID_OIL_PUMP, "Маслонасос", "Маслонасос", TsImageDescriptor.KEEPER_ID, rbInfo );
    addValobjAttr( FID_MAIN_SWITCH, "Главный выключатель", "Главный выключатель", TsImageDescriptor.KEEPER_ID, rbInfo );
    addValobjAttr( FID_MAIN_BIG_SWITCH, "Главный выключатель", "Главный выключатель", TsImageDescriptor.KEEPER_ID,
        rbInfo );

    rbs.defineRefbook( rbInfo );

    refbook = rbs.findRefbook( rbId );
    if( refbook != null ) { // Заполним справочник значениями
      RefookItemBuider b;
      IDtoFullObject rbItem;
      TsImageDescriptor heaterImd;
      TsImageDescriptor pumpImd;
      TsImageDescriptor mSwitchImd;
      TsImageDescriptor mSwitchBig;
      String pluginId = "ru.toxsoft.bkn.gui";

      b = new RefookItemBuider( "key0", "Авария", refbook );
      b.setValue( FID_KEY, avInt( 0 ) );
      heaterImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/heater-error.png" );
      b.setValue( FID_HEATER, avValobj( heaterImd ) );
      heaterImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/oil-pump-error.png" );
      b.setValue( FID_OIL_PUMP, avValobj( heaterImd ) );
      mSwitchImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/main-switch-error.png" );
      b.setValue( FID_MAIN_SWITCH, avValobj( mSwitchImd ) );
      mSwitchBig = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/main-switch-error-big.png" );
      b.setValue( FID_MAIN_BIG_SWITCH, avValobj( mSwitchBig ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key1", "Включен", refbook );
      b.setValue( FID_KEY, avInt( 1 ) );
      heaterImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/heater-on.png" );
      b.setValue( FID_HEATER, avValobj( heaterImd ) );
      heaterImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/oil-pump-on.png" );
      b.setValue( FID_OIL_PUMP, avValobj( heaterImd ) );
      mSwitchImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/main-switch-on.png" );
      b.setValue( FID_MAIN_SWITCH, avValobj( mSwitchImd ) );
      mSwitchBig = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/main-switch-on-big.png" );
      b.setValue( FID_MAIN_BIG_SWITCH, avValobj( mSwitchBig ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key2", "Выключен", refbook );
      b.setValue( FID_KEY, avInt( 2 ) );
      heaterImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/heater-off.png" );
      b.setValue( FID_HEATER, avValobj( heaterImd ) );
      heaterImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/oil-pump-off.png" );
      b.setValue( FID_OIL_PUMP, avValobj( heaterImd ) );
      mSwitchImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/main-switch-off.png" );
      b.setValue( FID_MAIN_SWITCH, avValobj( mSwitchImd ) );
      mSwitchBig = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/main-switch-off-big.png" );
      b.setValue( FID_MAIN_BIG_SWITCH, avValobj( mSwitchBig ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );

      b = new RefookItemBuider( "key3", "Неопределен", refbook );
      b.setValue( FID_KEY, avInt( -1 ) );
      heaterImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/heater-unknown.png" );
      b.setValue( FID_HEATER, avValobj( heaterImd ) );
      heaterImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/oil-pump-unknown.png" );
      b.setValue( FID_OIL_PUMP, avValobj( heaterImd ) );
      mSwitchImd = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/main-switch-unknown.png" );
      b.setValue( FID_MAIN_SWITCH, avValobj( mSwitchImd ) );
      mSwitchBig = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/main-switch-unknown-big.png" );
      b.setValue( FID_MAIN_BIG_SWITCH, avValobj( mSwitchBig ) );
      rbItem = b.buildItem();
      refbook.defineItem( rbItem );
    }
  }

  static void addValobjAttr( String aId, String aName, String aDescr, String aKeeperId, DtoRefbookInfo aRbInfo ) {
    IOptionSetEdit params = new OptionSet();
    params.setStr( TSID_NAME, aName );
    params.setStr( TSID_DESCRIPTION, aDescr );
    params.setStr( "ts.KeeperId", aKeeperId ); //$NON-NLS-1$
    DtoAttrInfo ai = DtoAttrInfo.create1( aId, DDEF_VALOBJ, params );
    aRbInfo.attrInfos().add( ai );
  }

  static IDataDef fromAtomicType( EAtomicType aType ) {
    return switch( aType ) {
      case BOOLEAN -> DDEF_BOOLEAN;
      case FLOATING -> DDEF_FLOATING;
      case INTEGER -> DDEF_INTEGER;
      case NONE -> DDEF_NONE;
      case STRING -> DDEF_STRING;
      case TIMESTAMP -> DDEF_TIMESTAMP;
      case VALOBJ -> DDEF_VALOBJ;
      default -> throw new TsNotAllEnumsUsedRtException( aType.toString() );
    };
  }

}
