package org.toxsoft.skf.mnemo.gui.tsgui.utils;

import static org.toxsoft.uskat.core.gui.km5.sgw.ISgwM5Constants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.gw.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.skf.mnemo.gui.m51.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.sgw.*;

/**
 * Sk GUI utility methods.
 *
 * @author vs
 */
public class SkGuiUtils {

  // /**
  // * Возвращает соединение из переданного контекста.
  // *
  // * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
  // * @return {@link ISkConnection} - соединение с сервером
  // */
  // public static ISkConnection getConnection( ITsGuiContext aTsContext ) {
  // ISkConnectionSupplier connSup = aTsContext.get( ISkConnectionSupplier.class );
  // // проверяем в контекте наличие информации о соединении
  // if( aTsContext.params().hasKey( PanelGwidSelector.OPDEF_CONN_ID_CHAIN.id() ) ) {
  // IdChain idChain = PanelGwidSelector.OPDEF_CONN_ID_CHAIN.getValue( aTsContext.params() ).asValobj();
  // return connSup.getConn( idChain );
  // }
  // return connSup.defConn();
  // }
  //
  // /**
  // * Возвращает API сервера из переданного контекста.
  // *
  // * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
  // * @return {@link ISkCoreApi} - API сервера
  // */
  // public static ISkCoreApi getCoreApi( ITsGuiContext aTsContext ) {
  // return getConnection( aTsContext ).coreApi();
  // }

  /**
   * Вызывает диалог выбора класса.
   *
   * @param aClass {@link ISkClassInfo} - описание класса или <code>null</code>
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return {@link ISkClassInfo} - описание класса
   */
  public static ISkClassInfo selectClass( ISkClassInfo aClass, ISkConnection aSkConn, ITsGuiContext aTsContext ) {
    // тут получаем KM5 модель ISkClassInfo
    IM5Domain m5 = aSkConn.scope().get( IM5Domain.class );
    IM5Model<ISkClassInfo> model = m5.getModel( ISgwM5Constants.MID_SGW_CLASS_INFO, ISkClassInfo.class );
    IM5LifecycleManager<ISkClassInfo> lm = model.getLifecycleManager( aSkConn );

    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( aTsContext, "Выбор класса", "Выберите соответствующий класс и нажмите \"ОК\"" );
    return M5GuiUtils.askSelectItem( dlgInfo, model, aClass, lm.itemsProvider(), null );
  }

  /**
   * Вызывает диалог выбора объекта.
   *
   * @param aClassId String - ИД класса
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return {@link ISkClassInfo} - описание класса
   */
  public static ISkObject selectObject( String aClassId, ISkConnection aSkConn, ITsGuiContext aTsContext ) {
    // TsNullArgumentRtException.checkNull( aClassId );
    // // тут получаем KM5 модель ISkClassInfo
    // IM5Domain m5 = aSkConn.scope().get( IM5Domain.class );
    // IM5Model<ISkObject> model = m5.getModel( ISgwM5Constants.MID_SGW_SK_OBJECT, ISkObject.class );
    //
    // IM5ItemsProvider<ISkObject> ip = () -> aSkConn.coreApi().objService().listObjs( aClassId, true );
    //
    // ITsDialogInfo dlgInfo;
    // dlgInfo = new TsDialogInfo( aTsContext, "Выбор объекта", "Выберите соответствующий объект и нажмите \"ОК\"" );
    // return M5GuiUtils.askSelectItem( dlgInfo, model, null, ip, null );

    // тут получаем KM5 модель ISkClassInfo
    IM5Domain m5 = aSkConn.scope().get( IM5Domain.class );
    M5BaseFieldDefModel model = (M5BaseFieldDefModel)m5.getModel( M5BaseFieldDefModel.MODEL_ID, IM5FieldDef.class );
    IM5Model<Gwid> model1 = m5.getModel( IGwM5Constants.MID_GWID, Gwid.class );

    // IM5ItemsProvider<ISkObject> ip = () -> aSkConn.coreApi().objService().listObjs( "SkObject", true );

    IListEdit<IM5FieldDef> fl = new ElemArrayList<>();
    for( IM5FieldDef fd : model1.fieldDefs() ) {
      fl.add( fd );
    }
    IM5ItemsProvider<IM5FieldDef> ip = () -> fl;

    // IM5ItemsProvider<IM5FieldDef> ip = () -> model.fieldDefs();

    ITsDialogInfo dlgInfo;
    dlgInfo = new TsDialogInfo( aTsContext, "Выбор объекта", "Выберите соответствующий объект и нажмите \"ОК\"" );
    M5GuiUtils.askSelectItem( dlgInfo, model, null, ip, null );
    return null;
  }

  /**
   * Возвращает панель для выбора свойства класса.<br>
   *
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return IM5CollectionPanel&lt;IDtoClassPropInfoBase> - панель выбора свойства к класса
   */
  public static IM5CollectionPanel<ISkObject> getSkObjectSelectionPanel( ISkConnection aSkConn,
      ITsGuiContext aTsContext ) {
    // тут получаем KM5 модель ISkClassInfo
    IM5Domain m5 = aSkConn.scope().get( IM5Domain.class );
    String propModelId = sgwGetClassPropModelId( null );
    IM5Model<IDtoClassPropInfoBase> modelProps = m5.getModel( propModelId, IDtoClassPropInfoBase.class );
    return modelProps.panelCreator().createCollViewerPanel( aTsContext, IM5ItemsProvider.EMPTY );
  }

  /**
   * Возвращает панель для выбора свойства класса.<br>
   *
   * @param aPropKind {@link ESkClassPropKind} - тип свойства класса
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return IM5CollectionPanel&lt;IDtoClassPropInfoBase> - панель выбора свойства к класса
   */
  public static IM5CollectionPanel<IDtoClassPropInfoBase> getClassPorpertySelectionPanel( ESkClassPropKind aPropKind,
      ISkConnection aSkConn, ITsGuiContext aTsContext ) {
    // тут получаем KM5 модель ISkClassInfo
    IM5Domain m5 = aSkConn.scope().get( IM5Domain.class );
    String propModelId = sgwGetClassPropModelId( aPropKind );
    IM5Model<IDtoClassPropInfoBase> modelProps = m5.getModel( propModelId, IDtoClassPropInfoBase.class );
    return modelProps.panelCreator().createCollViewerPanel( aTsContext, IM5ItemsProvider.EMPTY );
  }

  /**
   * Возвращает панель для выбора НСИ атрибута.<br>
   *
   * @param aSkConn {@link ISkConnection} - соединение с сервером
   * @param aTsContext {@link ITsGuiContext} - соответствующий контекст
   * @return IM5CollectionPanel&lt;IDtoClassPropInfoBase> - панель выбора НСИ атибута
   */
  public static IM5CollectionPanel<IDtoClassPropInfoBase> getRriAttrSelectionPanel( ISkConnection aSkConn,
      ITsGuiContext aTsContext ) {
    // тут получаем KM5 модель ISkClassInfo
    IM5Domain m5 = aSkConn.scope().get( IM5Domain.class );
    String propModelId = sgwGetClassPropModelId( ESkClassPropKind.ATTR );
    IM5Model<IDtoClassPropInfoBase> modelProps = m5.getModel( propModelId, IDtoClassPropInfoBase.class );
    return modelProps.panelCreator().createCollViewerPanel( aTsContext, IM5ItemsProvider.EMPTY );
  }

  // public static IM5CollectionPanel<IDtoAttrInfo> getAttrsListPanel( ITsGuiContext aTsContext ) {
  // ISkConnection conn = getConnection( aTsContext );
  // // тут получаем KM5 модель IDtoAttrInfo
  // IM5Domain m5 = conn.scope().get( IM5Domain.class );
  // IM5Model<IDtoAttrInfo> model = m5.getModel( ISgwM5Constants.MID_SGW_ATTR_INFO, IDtoAttrInfo.class );
  // IM5LifecycleManager<IDtoAttrInfo> lm = model.getLifecycleManager( conn );
  //
  // return model.panelCreator().createCollViewerPanel( aTsContext, lm.itemsProvider() );
  // }

  /**
   * No subclasses.
   */
  private SkGuiUtils() {
    // nop
  }

}
