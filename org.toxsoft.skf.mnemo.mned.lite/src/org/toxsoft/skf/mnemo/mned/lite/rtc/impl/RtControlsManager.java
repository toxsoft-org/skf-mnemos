package org.toxsoft.skf.mnemo.mned.lite.rtc.impl;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * {@link IRtControlsManager} implementation.
 *
 * @author vs
 */
public class RtControlsManager
    implements IRtControlsManager, ICloseable {

  /**
   * {@link IVedItemsManager#svs()} implementation.
   *
   * @author hazard157
   */
  static class Svs
      extends AbstractTsValidationSupport<IRtControlsManagerValidator>
      implements IRtControlsManagerValidator {

    @Override
    public IRtControlsManagerValidator validator() {
      return this;
    }

    @Override
    public ValidationResult canCreate( int aIndex, IRtControlCfg aCfg ) {
      TsNullArgumentRtException.checkNull( aCfg );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IRtControlsManagerValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canCreate( aIndex, aCfg ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemove( String aId ) {
      TsNullArgumentRtException.checkNull( aId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( IRtControlsManagerValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemove( aId ) );
      }
      return vr;
    }

  }

  /**
   * {@link IVedItemsManager#eventer()} implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractSimpleCrudOpTsEventer<IRtControlsManagerListener, String, IRtControlsManager> {

    public Eventer( IRtControlsManager aSource ) {
      super( aSource );
    }

    @Override
    protected void doReallyFireEvent( IRtControlsManagerListener aListener, IRtControlsManager aSource, ECrudOp aOp,
        String aItem ) {
      aListener.onRtControlsListChange( aSource, aOp, aItem );
    }

  }

  private final IStridablesListEdit<IRtControl>      itemsList = new StridablesList<>();
  private final IStridablesListReorderer<IRtControl> reorderer;

  private final Svs       svs = new Svs();
  private final Eventer   eventer;
  private final VedScreen vedScreen;

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreen} - editor screen
   */
  public RtControlsManager( VedScreen aScreen ) {
    vedScreen = aScreen;
    eventer = new Eventer( this );
    INotifierStridablesListEdit<IRtControl> tmpList = new NotifierStridablesListEditWrapper<>( itemsList );
    tmpList.addCollectionChangeListener( ( sec, op, item ) -> eventer.fireEvent( op, (String)item ) );
    reorderer = new StridablesListReorderer<>( tmpList );
    // svs.addValidator( builtinValidator );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private boolean hasItemWithName( String aName ) {
    for( IRtControl item : itemsList ) {
      if( item.nmName().equals( aName ) ) {
        return true;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    if( itemsList.isEmpty() ) {
      return;
    }
    while( !itemsList.isEmpty() ) {
      itemsList.removeByIndex( 0 );
      // IRtControl item = itemsList.removeByIndex( 0 );
      // item.dispose();
    }
    eventer.fireEvent( ECrudOp.LIST, null );
  }

  // ------------------------------------------------------------------------------------
  // ICloseable
  //

  @Override
  public void close() {
    while( !itemsList.isEmpty() ) {
      itemsList.removeByIndex( 0 );
      // IRtControl item = itemsList.removeByIndex( 0 );
      // item.dispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedItemsManager
  //

  @Override
  public IStridablesList<IRtControl> list() {
    return itemsList;
  }

  @Override
  public IRtControl getRtControlByViselId( String aViselId ) {
    TsIllegalArgumentRtException.checkFalse( StridUtils.isValidIdPath( aViselId ) );
    for( IRtControl rtc : itemsList ) {
      if( aViselId.equals( RtControlCfg.viselId( rtc.params() ) ) ) {
        return rtc;
      }
    }
    return null;
  }

  @Override
  public IStridablesListReorderer<IRtControl> reorderer() {
    return reorderer;
  }

  @Override
  public RtControlCfg prepareFromTemplate( IRtControlCfg aTemplateCfg ) {
    TsNullArgumentRtException.checkNull( aTemplateCfg );
    IRtControlFactory factory = findFactory( aTemplateCfg );
    TsItemNotFoundRtException.checkNull( factory );
    // generate ID
    String id;
    int counter = 0;
    String prefix = StridUtils.getLast( aTemplateCfg.factoryId() );
    prefix = prefix.toLowerCase().substring( 0, 1 ) + prefix.substring( 1 ); // convert first char to lower case
    String name;
    do {
      id = prefix + Integer.toString( ++counter ); // "prefixNN"
      name = factory.nmName() + ' ' + Integer.toString( counter ); // "Factory name NN"
    } while( itemsList.hasKey( id ) || hasItemWithName( name ) );
    // create config
    RtControlCfg cfg = new RtControlCfg( id, aTemplateCfg );
    cfg.propValues().setStr( DDEF_NAME, name );
    return cfg;
  }

  @Override
  public IRtControl create( int aIndex, IRtControlCfg aCfg ) {
    TsValidationFailedRtException.checkError( svs.canCreate( aIndex, aCfg ) );
    IRtControlFactory factory = findFactory( aCfg );
    IRtControl item = factory.create( aCfg, vedScreen );
    TsInternalErrorRtException.checkNull( item );
    item.props().propsEventer().addListener( ( src, news, olds ) -> {
      if( itemsList.hasKey( item.id() ) ) {
        eventer.fireEvent( ECrudOp.EDIT, item.id() );
      }
      else {
        throw new TsInternalErrorRtException(); // just in case someone is working with removed item
      }
    } );
    itemsList.add( item );
    eventer.fireEvent( ECrudOp.CREATE, item.id() );
    return item;
  }

  @Override
  public void remove( String aId ) {
    TsValidationFailedRtException.checkError( svs.canRemove( aId ) );
    IRtControl item = itemsList.removeById( aId );
    if( item != null ) {
      eventer.fireEvent( ECrudOp.REMOVE, aId );
      // item.dispose();
    }
  }

  @Override
  public ITsValidationSupport<IRtControlsManagerValidator> svs() {
    return svs;
  }

  @Override
  public ITsEventer<IRtControlsManagerListener> eventer() {
    return eventer;
  }

  @Override
  public void informOnModelChange( ECrudOp aOp, String aItemId ) {
    TsNullArgumentRtException.checkNull( aOp );
    switch( aOp ) {
      case EDIT: {
        TsNullArgumentRtException.checkNull( aItemId );
        eventer.fireEvent( aOp, aItemId );
        break;
      }
      case LIST: {
        eventer.fireEvent( aOp, null );
        break;
      }
      case CREATE:
      case REMOVE: {
        throw new TsIllegalArgumentRtException();
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aOp.id() );
    }

    // TODO Auto-generated method stub

  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  IRtControlFactory findFactory( IRtControlCfg aCfg ) {
    IRtControlFactoriesRegistry reg = vedScreen.tsContext().get( IRtControlFactoriesRegistry.class );
    return reg.get( aCfg.factoryId() );
  }

}
