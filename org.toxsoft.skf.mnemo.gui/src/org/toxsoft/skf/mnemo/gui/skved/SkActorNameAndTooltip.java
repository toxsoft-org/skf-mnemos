package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.uskat.core.api.ugwis.*;

/**
 * Actor, который задает соотвествующему свойству "имя" {@link IStridable} sk-сущности, а в качестве Tooltip ее
 * описание.
 * <p>
 *
 * @author vs
 */
public class SkActorNameAndTooltip
    extends AbstractSkVedActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.NameAndTooltip"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_UGWI = new TinFieldInfo( "ugwi", TTI_UGWI, //
      TSID_NAME, "Ugwi", //
      TSID_DESCRIPTION, "Ugwi", //
      TSID_KEEPER_ID, Ugwi.KEEPER_ID //
  );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, "Имя и tooltip", //
      TSID_DESCRIPTION, "Name and tooltip provider", //
      TSID_ICON_ID, ICONID_RT_ACTION_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_UGWI );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorNameAndTooltip.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorNameAndTooltip( aCfg, propDefs(), aVedScreen );
    }

  };

  SkActorNameAndTooltip( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  private Ugwi ugwi = null;

  String tooltipText = null;

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( TFI_UGWI.id() ) ) {
      ugwi = aChangedValues.getValobj( TFI_UGWI.id() );
    }
    if( ugwi != null && ugwi != Ugwi.NONE ) {
      ISkUgwiService ugwiServ = coreApi().getService( ISkUgwiService.SERVICE_ID );
      Object obj = ugwiServ.findContent( ugwi );
      if( obj instanceof IStridable ) {
        tooltipText = ((IStridable)obj).description();
        setStdViselPropValue( avStr( ((IStridable)obj).nmName() ) );
      }
      else {
        tooltipText = null;
        setStdViselPropValue( avStr( TsLibUtils.EMPTY_STRING ) );
      }
    }
  }

  @Override
  protected IGwidList doListUsedGwids() {
    return IGwidList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    String viselId = props().getStr( PROPID_VISEL_ID );
    if( viselId.isBlank() ) {
      vedScreen().view().getControl().setToolTipText( TsLibUtils.EMPTY_STRING );
      return false;
    }
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( viselId );
    if( visel == null ) {
      vedScreen().view().getControl().setToolTipText( TsLibUtils.EMPTY_STRING );
      return false;
    }
    ID2Point p = vedScreen().view().coorsConverter().swt2Visel( aCoors, visel );
    if( visel.isYours( p ) ) {
      vedScreen().view().getControl().setToolTipText( tooltipText );
      return true;
    }

    vedScreen().view().getControl().setToolTipText( null );
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

}
