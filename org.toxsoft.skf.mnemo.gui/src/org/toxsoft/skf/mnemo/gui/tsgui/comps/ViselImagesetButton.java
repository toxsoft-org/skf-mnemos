package org.toxsoft.skf.mnemo.gui.tsgui.comps;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.refbooks.lib.*;
import org.toxsoft.uskat.core.connection.*;

public class ViselImagesetButton
    extends VedAbstractVisel
    implements IViselButton {

  /**
   * The VISEL factor ID.
   */
  public static final String FACTORY_ID = VED_ID + ".visel.imageset_button"; //$NON-NLS-1$

  /**
   * Property id for buttons state
   */
  public static final String PROPID_STATE = "state"; //$NON-NLS-1$ (EButtonViselState)

  /**
   * Property id for button hovered sign
   */
  public static final String PROPID_HOVERED = "hovered"; //$NON-NLS-1$ находится под курсором

  /**
   * ИД свойства содержащего информацию о справочнике
   */
  public static final String PROPID_REFBOOK_ITEM = "refbookItem"; //$NON-NLS-1$

  private static final IDataDef PROP_REFBOOK_ITEM = DataDef.create( PROPID_REFBOOK_ITEM, VALOBJ, //
      TSID_NAME, "Элемент справочника", //
      TSID_DESCRIPTION, "Информация об элементе справочника", //
      TSID_KEEPER_ID, IdChain.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjRefbookItemSelector.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( IdChain.NULL ) //
  );

  static final ITinTypeInfo TTI_REFBOOK_ITEM = new TinAtomicTypeInfo.TtiValobj<>( PROP_REFBOOK_ITEM, IdChain.class );

  /**
   * The VISEL factory singleton.
   */
  public static final IVedViselFactory FACTORY = new VedAbstractViselFactory( FACTORY_ID, //
      TSID_NAME, "Кнопка-набор картинок", //
      TSID_DESCRIPTION, "Кнопка, отображающая свои состояния изображениями из набора ", //
      TSID_ICON_ID, ICONID_IMAGESET_BUTTON //
  ) {

    private static final TinFieldInfo TFI_STATE = new TinFieldInfo( PROPID_STATE, TtiAvEnum.INSTANCE, //
        TSID_NAME, "Состояние", //
        TSID_DESCRIPTION, "Состояние кнопки", //
        TSID_KEEPER_ID, EButtonViselState.KEEPER_ID, //
        TSID_DEFAULT_VALUE, avValobj( EButtonViselState.NORMAL ) );

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_ENABLED );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      // fields.add( TFI_TEXT );
      // fields.add( TFI_FONT );
      fields.add( TFI_X );
      fields.add( TFI_Y );
      fields.add( TFI_WIDTH );
      fields.add( TFI_HEIGHT );
      fields.add( TFI_FULCRUM );
      // fields.add( TFI_FG_COLOR );
      // fields.add( TFI_BK_COLOR );
      // fields.add( new TinFieldInfo( PROPID_HOVERED_BK_COLOR, TFI_BK_COLOR.typeInfo(), //
      // TSID_NAME, STR_HIGHLIGHT_BKG ) );
      // fields.add( new TinFieldInfo( PROPID_SELECTED_BK_COLOR, TFI_BK_COLOR.typeInfo(), //
      // TSID_NAME, STR_SELECTION_BKG ) );

      fields.add( new TinFieldInfo( PROP_REFBOOK_ITEM, TTI_REFBOOK_ITEM ) );
      fields.add( TFI_STATE );
      fields.add( TFI_HOVERED );
      fields.add( TFI_ZOOM );
      fields.add( TFI_ANGLE );
      fields.add( TinFieldInfo.makeCopy( TFI_TRANSFORM, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE ) );
      fields.add( TFI_IS_ACTIVE );

      return new PropertableEntitiesTinTypeInfo<>( fields, ViselImagesetButton.class );
    }

    @Override
    protected VedAbstractVisel doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new ViselImagesetButton( aCfg, propDefs(), aVedScreen );
    }

  };

  private final ImagesetButtonRenderer btnRenderer;

  EButtonViselState prevState = EButtonViselState.NORMAL;

  /**
   * Constructor.
   *
   * @param aConfig {@link IVedItemCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public ViselImagesetButton( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    addInterceptor( new VedViselInterceptorMinWidthHeight( this ) );
    btnRenderer = new ImagesetButtonRenderer( this );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractVisel
  //

  @Override
  public void paint( ITsGraphicsContext aPaintContext ) {
    btnRenderer.drawButton( aPaintContext );
  }

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValue ) {
    super.doUpdateCachesAfterPropsChange( aChangedValue );
    if( aChangedValue.hasKey( PROPID_ENABLED ) ) {
      IAtomicValue av = aChangedValue.getByKey( PROPID_ENABLED );
      if( av != null && av.isAssigned() ) {
        props().propsEventer().pauseFiring();
        if( av.asBool() ) {
          props().setValobj( PROPID_STATE, prevState );
        }
        else {
          prevState = props().getValobj( PROPID_STATE );
          props().setValobj( PROPID_STATE, EButtonViselState.DISABLED );
        }
        props().propsEventer().resumeFiring( false );
      }
    }
    btnRenderer.update();
    if( aChangedValue.hasKey( PROPID_REFBOOK_ITEM ) ) {
      IdChain idChain = aChangedValue.getValobj( PROPID_REFBOOK_ITEM );
      if( idChain.branches().size() > 1 ) {
        ISkConnection skConn = vedScreen().tsContext().get( ISkVedEnvironment.class ).skConn();
        ISkRefbookService rbServ = skConn.coreApi().getService( ISkRefbookService.SERVICE_ID );
        ISkRefbook refbook = rbServ.findRefbook( idChain.get( 0 ) );
        if( refbook == null ) {
          LoggerUtils.errorLogger().warning( "Refbook %s not found", idChain.get( 0 ) ); //$NON-NLS-1$
        }
        else {
          ISkRefbookItem item = refbook.findItem( idChain.get( 1 ) );
          if( item == null ) {
            LoggerUtils.errorLogger().warning( "Refbook item %s not found", idChain.get( 1 ) ); //$NON-NLS-1$
          }
          else {
            IMapEdit<EButtonViselState, TsImageDescriptor> map = new ElemMap<>();
            for( EButtonViselState state : EButtonViselState.values() ) {
              IAtomicValue value = item.attrs().findValue( state.id() );
              if( value != null && value.isAssigned() ) {
                map.put( state, value.asValobj() );
              }
              else {
                LoggerUtils.errorLogger().warning( "Refbook item attribute %s not found", state.id() ); //$NON-NLS-1$
              }
            }
            btnRenderer.setImages( map, imageManager() );
          }
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // IViselButton
  //
  @Override
  public boolean isEnabled() {
    IAtomicValue value = props().getValue( PROPID_ENABLED );
    if( value == null || !value.isAssigned() ) {
      return true;
    }
    return value.asBool();
  }

  @Override
  public EButtonViselState buttonState() {
    return props().getValobj( PROPID_STATE );
  }

  @Override
  public boolean isChecked() {
    return false;
  }

}
