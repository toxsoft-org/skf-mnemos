package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

public abstract class AbstractSkActorInputField
    extends AbstractSkVedActor {

  InputFieldHandler inputHandler;

  protected AbstractSkActorInputField( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs,
      VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

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
      if( inputHandler.isEditing() ) {
        if( aCode == SWT.CR ) {
          inputHandler.setEditing( false );
          onFinishEdit();
          return true;
        }
        if( aCode == SWT.ESC ) {
          inputHandler.setEditing( false );
          onCancelEdit();
          return true;
        }
        return inputHandler.onKeyDown( aSource, aCode, aChar, aState );
      }
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
  // To override
  //

  protected abstract void onFinishEdit();

  protected abstract void onCancelEdit();

}
