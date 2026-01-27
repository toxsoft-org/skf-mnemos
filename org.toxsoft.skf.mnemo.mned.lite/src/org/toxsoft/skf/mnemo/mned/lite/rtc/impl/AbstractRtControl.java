package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.txtproj.lib.storage.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * {@link IRtControl} - base implementation.
 *
 * @author vs
 */
public class AbstractRtControl
    implements IRtControl, IParameterizedEdit, IDisposable, ITsGuiContextable {

  private final IListEdit<IRtControlPropertyChangeInterceptor> interseptors = new ElemArrayList<>();

  private final IRtControlCfg              initialConfig;
  private final IOptionSetEdit             params;
  private final IPropertiesSet<IRtControl> propSet;
  private final VedScreen                  vedScreen;

  private boolean disposed = false;

  private final KeepablesStorageAsKeepable extraData = new KeepablesStorageAsKeepable();

  private final VedAbstractVisel visel;

  private final IStridablesListEdit<VedAbstractActor> actors;

  private final IRtControlFactory factory;

  private final IList<Pair<String, String>> viselPropsBinding;

  private final IStringMap<IList<Pair<String, String>>> actorPropsBinding;

  /**
   * Constructor for subclasses.
   *
   * @param aConfig {@link IRtControlCfg} - the item config
   * @param aPropDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   * @param aVedScreen {@link VedScreen} - the owner screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  protected AbstractRtControl( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNulls( aConfig, aPropDefs, aVedScreen );
    vedScreen = aVedScreen;
    initialConfig = aConfig;
    params = new OptionSet( aConfig.params() );
    IRtControlFactoriesRegistry reg = aVedScreen.tsContext().get( IRtControlFactoriesRegistry.class );
    factory = reg.get( aConfig.factoryId() );
    viselPropsBinding = factory.viselPropIdBinding();
    actorPropsBinding = factory.actorPropIdBinding();

    String viselId = RtControlCfg.viselId( params );
    visel = aVedScreen.model().visels().list().getByKey( viselId );
    IStringList actorIds = RtControlCfg.actorIds( params );
    if( actorIds.size() <= 0 ) {
      actors = IStridablesList.EMPTY;
    }
    else {
      actors = new StridablesList<>();
      for( String id : actorIds ) {
        actors.add( aVedScreen.model().actors().list().getByKey( id ) );
      }
    }

    propSet = new PropertiesSet<>( this, aPropDefs );
    propSet.propsEventer().addListener( ( aSource, aNewValues, aOldValues ) -> {
      visel.props().propsEventer().pauseFiring();
      for( Pair<String, String> p : viselPropsBinding ) {
        if( aNewValues.keys().hasElem( p.left() ) ) {
          // IAtomicValue v = visel.props().getValue( p.right() );
          visel.props().setValue( p.right(), aNewValues.getValue( p.left() ) );
          // v = visel.props().getValue( p.right() );
        }
      }
      visel.props().propsEventer().resumeFiring( true );

      for( String actorId : actorPropsBinding.keys() ) {
        VedAbstractActor actor = actors.getByKey( actorId );
        actor.props().propsEventer().pauseFiring();
        IList<Pair<String, String>> pairs = actorPropsBinding.getByKey( actorId );
        for( Pair<String, String> p : pairs ) {
          if( aNewValues.keys().hasElem( p.left() ) ) {
            actor.props().setValue( p.right(), aNewValues.getValue( p.left() ) );
          }
        }
        actor.props().propsEventer().resumeFiring( true );
      }

      doUpdateCachesAfterPropsChange( aNewValues );
    } );

    visel.props().propsEventer().addListener( ( aSource, aNewValues, aOldValues ) -> {
      propSet.propsEventer().pauseFiring();
      for( Pair<String, String> p : viselPropsBinding ) {
        if( aNewValues.keys().hasElem( p.right() ) ) {
          propSet.setValue( p.left(), aNewValues.getValue( p.right() ) );
        }
      }
      propSet.propsEventer().resumeFiring( false );
    } );

    // extraData.copyFrom( aConfig.extraData() );
    props().setInterceptor( ( s, aNewValues, aValuesToSet ) -> interceptPropsChange( aNewValues, aValuesToSet ) );
    propSet.propsEventer().pauseFiring();
    updatePropsByVisel( visel.props() );
    for( VedAbstractActor actor : actors ) {
      updatePropsByActor( actor );
    }
    propSet.propsEventer().resumeFiring( false );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return initialConfig.id();
  }

  @Override
  final public String nmName() {
    return PROP_NAME.getValue( propSet ).asString();
  }

  @Override
  final public String description() {
    return PROP_DESCRIPTION.getValue( propSet ).asString();
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    IVedItemFactoryBase<?> f = tsContext().get( IVedViselFactoriesRegistry.class ).find( initialConfig.factoryId() );
    return f != null ? f.iconId() : null;
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  final public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  final public IPropertiesSet<IRtControl> props() {
    return propSet;
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  final public boolean isDisposed() {
    return disposed;
  }

  @Override
  final public void dispose() {
    if( !disposed ) {
      doDispose();
      disposed = true;
    }
    else {
      LoggerUtils.errorLogger().warning( STR_WARN_DUPLICATE_DIPOSAL, toString() );
    }
  }

  // ------------------------------------------------------------------------------------
  // IRtControl
  //

  @Override
  public boolean isActive() {
    return props().getBool( PROP_IS_ACTIVE );
  }

  @Override
  public String factoryId() {
    return initialConfig.factoryId();
  }

  @Override
  public IKeepablesStorage extraData() {
    return extraData;
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may process property values change request.
   * <p>
   * Editable argument <code>aValuesToSet</code> is the values, that will be set to properties. It initially contains
   * the same vales as <code>aNewValues</code>. Interceptor may remove values from <code>aValuesToSet</code> edit
   * existing, add any other properties values or event clear to cancel changes. Current values of the properties may be
   * accessed via {@link #props()}.
   * <p>
   * Does nothing in the base class, but in the inheritance tree, subclasses must call the superclass method.
   *
   * @param aNewValues {@link IOptionSetEdit} - changed properties values after change
   * @param aValuesToSet {@link IOptionSet} - the values to be set after interception
   */
  protected void doInterceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    // nop
  }

  /**
   * Subclass may update internal caches and perform other actions after the property(ies) change.
   * <p>
   * Does nothing in the base class, but in the inheritance tree, subclasses <b>must call</b> the superclass method to
   * successfully update the cache at all levels.
   * <p>
   * Note: this method is also called immediately after item was created and properties are set to the initial values.
   * In such case the argument <code>aChangedValues</code> contains all properties.
   * <p>
   * Warning: setting the properties values from this method is strictly prohibited!
   *
   * @param aChangedValues {@link IOpsSetter} - set of really changed properties new values, never is empty
   */
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    // nop
  }

  /**
   * Subclass may process property change event.
   * <p>
   * Does nothing in the base class, but in the inheritance tree, subclasses must call the superclass method.
   * <p>
   * Warning: setting the properties values from this method is strictly prohibited!
   *
   * @param aNewValues {@link IOptionSet} - changed properties values after change
   * @param aOldValues {@link IOptionSet} - all properties values before change
   */
  protected void onPropsChanged( IOptionSet aNewValues, IOptionSet aOldValues ) {
    // nop
  }

  /**
   * Subclass may perform the real disposal of resources if necessary.
   * <p>
   * Method is called once, even if {@link #dispose()} is called multiple times. Implementation must call superclass
   * method.
   */
  protected void doDispose() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  final private void interceptPropsChange( IOptionSet aNewValues, IOptionSetEdit aValuesToSet ) {
    for( IRtControlPropertyChangeInterceptor l : new ElemArrayList<>( interseptors ) ) {
      l.interceptPropsChange( this, aNewValues, aValuesToSet );
    }
    // call subclass interceptor
    doInterceptPropsChange( aNewValues, aValuesToSet );
  }

  void updatePropsByVisel( IOptionSet aProps ) {
    for( Pair<String, String> p : viselPropsBinding ) {
      if( aProps.keys().hasElem( p.right() ) ) {
        propSet.setValue( p.left(), aProps.getValue( p.right() ) );
      }
    }
  }

  void updatePropsByActor( VedAbstractActor aActor ) {
    IList<Pair<String, String>> pairs = actorPropsBinding.getByKey( aActor.id() );
    for( Pair<String, String> p : pairs ) {
      if( aActor.props().keys().hasElem( p.right() ) ) {
        propSet.setValue( p.left(), aActor.props().getValue( p.right() ) );
      }
    }
  }
}
