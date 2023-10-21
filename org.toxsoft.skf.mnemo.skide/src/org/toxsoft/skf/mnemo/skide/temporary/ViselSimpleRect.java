package org.toxsoft.skf.mnemo.skide.temporary;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Simple rectangle VISEL.
 * <p>
 * Rectangle with solid fill of color {@link IVedScreenConstants#PROP_BK_COLOR}.
 *
 * @author hazard157
 */
public class ViselSimpleRect
    extends VedAbstractVisel {

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = "hz.SimpleRect"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, "SimpleRect", //
      TSID_DESCRIPTION, "Simple rectangle filled by the background color", //
      TSID_ICON_ID, ICONID_SIMPLE_RECT //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_TRANSFORM );
      fields.add( TFI_BK_COLOR );
      return new PropertableEntitiesTinTypeInfo<>( fields, ViselSimpleRect.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselSimpleRect( aCfg, propDefs(), aVedScreen );
    }

  };

  private Color fillColor = null;

  private ViselSimpleRect( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    fillColor = colorManager().getColor( (RGBA)PROP_BK_COLOR.defaultValue().asValobj() );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    if( aChangedValue.hasKey( PROPID_BK_COLOR ) ) {
      fillColor = new Color( (RGBA)props().getValobj( PROP_BK_COLOR ) );
    }
  }

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    aPaintContext.gc().setBackground( fillColor );

    // FIXME

    aPaintContext.gc().fillRectangle( (int)bounds().x1(), (int)bounds().y1(), (int)bounds().width(),
        (int)bounds().height() );
  }

}
