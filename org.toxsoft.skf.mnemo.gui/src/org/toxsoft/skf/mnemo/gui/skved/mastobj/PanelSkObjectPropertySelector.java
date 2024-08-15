package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import java.awt.*;

import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;

/**
 * Панель выбора свойства sk-объекта.
 *
 * @author vs
 */
public class PanelSkObjectPropertySelector
    extends TsPanel {

  StridableTableViewer attrsViewer;

  private final String ugwiKindId;

  M5DefaultItemsProvider<IDtoClassPropInfoBase> propItemsProvider = new M5DefaultItemsProvider<>();

  IM5CollectionPanel<IDtoClassPropInfoBase> m5Panel = null;

  private ISkClassInfo classInfo = null;

  public PanelSkObjectPropertySelector( String aUgwiKind, Composite aParent, ITsGuiContext aContext, int aStyle ) {
    super( aParent, aContext, aStyle );
    setLayout( new FillLayout() );
    ugwiKindId = aUgwiKind;
    setData( AWTLayout.KEY_PREFERRED_SIZE, new Dimension( 400, 600 ) );
    m5Panel = switch( ugwiKindId ) {
      case UgwiKindSkAttr.KIND_ID -> SkGuiUtils.getClassPorpertySelectionPanel( ESkClassPropKind.ATTR, aContext );
      case UgwiKindSkRtdata.KIND_ID -> SkGuiUtils.getClassPorpertySelectionPanel( ESkClassPropKind.RTDATA, aContext );
      case UgwiKindSkCmd.KIND_ID -> SkGuiUtils.getClassPorpertySelectionPanel( ESkClassPropKind.CMD, aContext );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    if( m5Panel != null ) {
      propItemsProvider.items().setAll( IStridablesList.EMPTY );
      m5Panel.setItemsProvider( propItemsProvider );
      m5Panel.createControl( this );
    }
  }

  public void setClassInfo( ISkClassInfo aClassInfo ) {
    propItemsProvider.items().clear();
    classInfo = aClassInfo;
    switch( ugwiKindId ) {
      case UgwiKindSkAttr.KIND_ID: {
        propItemsProvider.items().addAll( aClassInfo.props( ESkClassPropKind.ATTR ).list() );
        break;
      }
      case UgwiKindSkRivet.KIND_ID: {

        break;
      }
      case UgwiKindSkRtdata.KIND_ID: {
        propItemsProvider.items().addAll( aClassInfo.props( ESkClassPropKind.RTDATA ).list() );
        break;
      }
      case UgwiKindSkCmd.KIND_ID: {
        propItemsProvider.items().addAll( aClassInfo.props( ESkClassPropKind.CMD ).list() );
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( ugwiKindId );
    }
    if( m5Panel != null ) {
      m5Panel.refresh();
    }
  }

  public void clear() {
    if( m5Panel != null ) {
      propItemsProvider.items().clear();
      m5Panel.refresh();
    }
  }

  public Ugwi selectedUgwi() {
    if( m5Panel != null ) {
      IDtoClassPropInfoBase propInfo = m5Panel.selectedItem();
      if( propInfo != null ) {
        return switch( propInfo.kind() ) {
          case ATTR -> UgwiKindSkAttrInfo.makeUgwi( classInfo.id(), propInfo.id() );
          case RTDATA -> UgwiKindSkRtDataInfo.makeUgwi( classInfo.id(), propInfo.id() );
          case CMD -> UgwiKindSkCmdInfo.makeUgwi( classInfo.id(), propInfo.id() );
          case CLOB, EVENT, LINK, RIVET -> throw new TsNotAllEnumsUsedRtException();
          default -> throw new TsNotAllEnumsUsedRtException();
        };
      }
    }
    return null;
  }

  public void addTsSelectionChangeListener( ITsSelectionChangeListener<IDtoClassPropInfoBase> aListener ) {
    if( m5Panel != null ) {
      m5Panel.addTsSelectionListener( aListener );
    }
  }

  public void removeTsSelectionChangeListener( ITsSelectionChangeListener<IDtoClassPropInfoBase> aListener ) {
    if( m5Panel != null ) {
      m5Panel.removeTsSelectionListener( aListener );
    }
  }

  // StridableTableViewer createAttrsViewer(Composite aParent) {
  // int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL;
  // attrsViewer = new StridableTableViewer( aParent, style, 80, 200, -1 );
  // }

}
