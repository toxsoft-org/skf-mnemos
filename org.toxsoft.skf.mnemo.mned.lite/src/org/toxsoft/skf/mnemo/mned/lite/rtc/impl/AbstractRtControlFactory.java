package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * Базовый класс для фабрик RtControl'ей.
 *
 * @author vs
 */
public abstract class AbstractRtControlFactory
    extends StridableParameterized
    implements IRtControlFactory {

  private ITinTypeInfo                             tinTypeInfo    = null;
  private IStridablesList<IDataDef>                propDefs       = null;
  private IStridablesList<IRtControlsPaletteEntry> paletteEntries = null;

  /**
   * Список пар соответствия свойства RtContorl'я и визуального элемента.
   */
  private final IListEdit<Pair<String, String>> viselPropsBinding = new ElemArrayList<>();

  /**
   * Constructor.
   *
   * @param aId String - the
   * @param aIdsAndValues Object[] - identifier / value pairs of the {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public AbstractRtControlFactory( String aId, Object... aIdsAndValues ) {
    super( aId, OptionSetUtils.createOpSet( aIdsAndValues ) );
    bindViselProps();
  }

  // ------------------------------------------------------------------------------------
  // IRtControlFactory
  //

  @Override
  final public IStridablesList<IDataDef> propDefs() {
    if( propDefs == null ) {
      IStridablesListEdit<IDataDef> pdefs = new StridablesList<>();
      // convert child fields of type info to the properties definitions
      for( ITinFieldInfo finf : typeInfo().fieldInfos() ) {
        TsInternalErrorRtException.checkFalse( finf.typeInfo().kind().hasAtomic() );
        DataDef dd = DataDef.create4( finf.id(), finf.typeInfo().dataType(), finf.params() );
        pdefs.add( dd );
      }
      propDefs = pdefs;
    }
    return propDefs;
  }

  @Override
  public IStridablesList<IRtControlsPaletteEntry> paletteEntries() {
    if( paletteEntries == null ) {
      IStridablesList<IRtControlsPaletteEntry> ll = doCreatePaletteEntries();
      // check the returned list
      TsInternalErrorRtException.checkNull( ll );
      TsInternalErrorRtException.checkTrue( ll.isEmpty() );
      for( IRtControlsPaletteEntry entry : ll ) {
        if( !entry.itemCfg().factoryId().equals( id() ) ) {
          throw new TsInternalErrorRtException();
        }
      }
      paletteEntries = ll;
    }
    return paletteEntries;
  }

  @Override
  public ITinTypeInfo typeInfo() {
    if( tinTypeInfo == null ) {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      ITinFieldInfo tfiName = TinFieldInfo.makeCopy( TFI_NAME, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE );
      ITinFieldInfo tfiDescr = TinFieldInfo.makeCopy( TFI_DESCRIPTION, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE );
      fields.add( tfiName );
      fields.add( tfiDescr );
      tinTypeInfo = doCreateTypeInfo( fields );
      TsInternalErrorRtException.checkNull( tinTypeInfo );
      TsInternalErrorRtException.checkFalse( tinTypeInfo.kind().hasChildren() );
      for( ITinFieldInfo finf : tinTypeInfo.fieldInfos() ) {
        TsInternalErrorRtException.checkFalse( finf.typeInfo().kind().hasAtomic() );
      }
    }
    return tinTypeInfo;
  }

  @Override
  public IRtControl create( IRtControlCfg aCfg, VedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNulls( aCfg, aVedScreen );
    TsIllegalArgumentRtException.checkFalse( aCfg.factoryId().equals( id() ) );
    OptionSetUtils.checkOptionSet( aCfg.propValues(), propDefs() );
    IRtControl item = doCreate( aCfg, aVedScreen );
    // TsInternalErrorRtException.checkNull( item );
    // item.params().setAll( aCfg.params() );
    // item.props().setProps( aCfg.propValues() );
    // item.doUpdateCachesAfterPropsChange( new OptionSet( item.props() ) );
    return item;
  }

  @Override
  public IList<Pair<String, String>> viselPropIdBinding() {
    return viselPropsBinding;
  }

  // @Override
  // public IStringMap<IList<Pair<String, String>>> actorPropIdBinding() {
  // return actorPropsBinding;
  // }

  // ------------------------------------------------------------------------------------
  // To use
  //

  protected IVedViselFactory viselFactory( String aFactoryId, IVedScreen aVedScreen ) {
    IVedViselFactoriesRegistry reg = aVedScreen.tsContext().get( IVedViselFactoriesRegistry.class );
    return reg.get( aFactoryId );
  }

  protected IVedActorFactory actorFactory( String aFactoryId, IVedScreen aVedScreen ) {
    IVedActorFactoriesRegistry reg = aVedScreen.tsContext().get( IVedActorFactoriesRegistry.class );
    return reg.get( aFactoryId );
  }

  protected void bindViselPropId( String aRtcPropid, String aViselPropId ) {
    viselPropsBinding.add( new Pair<>( aRtcPropid, aViselPropId ) );
  }

  protected VedItemCfg createViselCfg( String aFactoryId, VedScreen aVedScreen, String aName ) {
    IVedViselFactory f = viselFactory( aFactoryId, aVedScreen );
    VedItemCfg viselCfg = aVedScreen.model().visels().prepareFromTemplate( f.paletteEntries().first().itemCfg() );

    String num = extractNumberFromId( viselCfg.id() );
    viselCfg = new VedItemCfg( aName + num, viselCfg );

    viselCfg.propValues().setStr( TSID_NAME, nmName() + " " + num ); //$NON-NLS-1$
    viselCfg.propValues().setStr( TSID_DESCRIPTION, description() );
    return viselCfg;
  }

  // void bindActorPropId( String aActorId, String aRtcPropid, String aViselPropId ) {
  // IListEdit<Pair<String, String>> pairs;
  // if( !actorPropsBinding.hasKey( aActorId ) ) {
  // pairs = new ElemArrayList<>();
  // actorPropsBinding.put( aActorId, pairs );
  // }
  // else {
  // pairs = (IListEdit<Pair<String, String>>)actorPropsBinding.getByKey( aActorId );
  // }
  // pairs.add( new Pair<>( aRtcPropid, aViselPropId ) );
  // }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may create own set of the palette entries.
   * <p>
   * Method is called once, on first call to {@link #paletteEntries()}.
   * <p>
   * In the base class creates the list with one entry with the default {@link #propDefs()} item configuration. Subclass
   * may call base class method and add new entries or create own list without calling parent method.
   *
   * @return {@link StridablesList}&lt;{@link IVedItemsPaletteEntry}&gt; - an editable list
   */
  protected StridablesList<IRtControlsPaletteEntry> doCreatePaletteEntries() {
    RtControlCfg cfg = new RtControlCfg( id(), id(), IOptionSet.NULL );
    OptionSetUtils.initOptionSet( cfg.propValues(), propDefs() );
    IRtControlsPaletteEntry entry = new RtControlPaletteEntry( id(), params(), cfg );
    return new StridablesList<>( entry );
  }

  /**
   * Subclass must create (once in a lifetime) the type info used as {@link #typeInfo()}.
   * <p>
   * Warning: there are following restrictions on created type info:
   * <ul>
   * <li>type must have at least one child for the created item to have at least one property;</li>
   * <li>it is not allowed for any child to be group {@link ETinTypeKind#GROUP} because such field can not be directly
   * converted to the property of atomic type.</li>
   * </ul>
   *
   * @param aFields IStridablesListEdit&lt;ITinFieldInfo> - field infoes, subclass should add needed field infoes
   * @return {@link ITinTypeInfo} - the type information for inspector
   */
  protected abstract ITinTypeInfo doCreateTypeInfo( IStridablesListEdit<ITinFieldInfo> aFields );

  /**
   * Subclass must create the IRtControl item.
   *
   * @param aCfg {@link IRtControlCfg} - the configuration data
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @return &lt;T&gt; - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException config entity kind does not matches provided entity kind
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  protected abstract IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen );

  protected void bindViselProps() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Static methods
  //

  static String extractNumberFromId( String aId ) {
    int idx = aId.length() - 1;
    for( int i = 0; i < aId.length(); i++ ) {
      char ch = aId.charAt( i );
      if( Character.isDigit( ch ) ) {
        idx = i;
        break;
      }
    }
    return aId.substring( idx );
  }

}
