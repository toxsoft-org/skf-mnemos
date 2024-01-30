package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;

public class SkActorInputField
    extends AbstractSkVedActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.InputField"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, "Input field", //
      TSID_DESCRIPTION, "Input field", //
      TSID_ICON_ID, ICONID_VED_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      // fields.add( TFI_ATTR_GWID );
      // fields.add( TFI_FORMAT_STRING );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorInputField.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorInputField( aCfg, propDefs(), aVedScreen );
    }

  };

  // boolean editing = true;

  InputFieldHandler inputHandler;

  protected SkActorInputField( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected IGwidList doListUsedGwids() {
    return IGwidList.EMPTY;
  }

  // @Override
  // protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
  // super.doUpdateCachesAfterPropsChange( aChangedValues );
  // if( !props().getBool( PROPID_IS_ACTIVE ) ) {
  // VedAbstractVisel visel = getVisel();
  // if( visel != null ) {
  // visel.props().setInt( PROPID_CARET_POS, -1 );
  // }
  // }
  // }

  long currTime = 0;

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    VedAbstractVisel visel = getVisel();
    if( visel != null ) {
      if( inputHandler == null ) {
        inputHandler = new InputFieldHandler( vedScreen(), visel );
      }
      if( !inputHandler.isEditing() || !props().getBool( PROPID_IS_ACTIVE ) ) {
        visel.props().setInt( PROPID_CARET_POS, -1 );
        return;
      }
      if( inputHandler != null && inputHandler.isEditing() && aRtTime - currTime > 500 ) {
        currTime = aRtTime;
        if( visel.props().hasKey( PROPID_CARET_POS ) ) {
          if( visel.props().getInt( PROPID_CARET_POS ) == -1 ) {
            visel.props().setInt( PROPID_CARET_POS, inputHandler.caretPos() );
          }
          else {
            visel.props().setInt( PROPID_CARET_POS, -1 );
          }
        }
      }
    }

  }

  // ------------------------------------------------------------------------------------
  // ITsUserInputListener
  //

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( inputHandler != null ) {
      return inputHandler.onMouseDown( aSource, aButton, aState, aCoors, aWidget );
    }
    return false;
  }

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    if( inputHandler != null ) {
      return inputHandler.onMouseDoubleClick( aSource, aButton, aState, aCoors, aWidget );
    }
    return false;
  }

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    if( inputHandler != null ) {
      return inputHandler.onKeyDown( aSource, aCode, aChar, aState );
    }
    return false;
  }

  @Override
  public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
    return inputHandler.onMouseDragStart( aSource, aDragInfo );
  }

  @Override
  public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    return inputHandler.onMouseDragMove( aSource, aDragInfo, aState, aCoors );
  }

  @Override
  public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    return inputHandler.onMouseDragFinish( aSource, aDragInfo, aState, aCoors );
  }

  @Override
  public boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
    return inputHandler.onMouseDragCancel( aSource, aDragInfo );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private VedAbstractVisel findMyVisel( ITsPoint aCoors ) {
    VedAbstractVisel visel = vedScreen().model().visels().list().findByKey( props().getStr( PROPID_VISEL_ID ) );
    if( visel != null ) {
      ID2Point p = vedScreen().view().coorsConverter().swt2Visel( aCoors, visel );
      if( visel.isYours( p ) ) {
        return visel;
      }
    }
    return null;
  }

  private int findcaretPos( ITsPoint aCoors ) {

    return -1;
  }

}
