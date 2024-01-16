package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;

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

  boolean editing = true;

  int caretPos = 0;

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

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    super.doUpdateCachesAfterPropsChange( aChangedValues );
  }

  long currTime = 0;

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    VedAbstractVisel visel = getVisel();
    if( visel != null ) {
      if( editing && aRtTime - currTime > 500 ) {
        currTime = aRtTime;
        if( visel.props().hasKey( PROPID_CARET_POS ) ) {
          if( visel.props().getInt( PROPID_CARET_POS ) == -1 ) {
            visel.props().setInt( PROPID_CARET_POS, caretPos );
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
    if( aButton == ETsMouseButton.LEFT && aState == 0 ) {
      VedAbstractVisel visel = findMyVisel( aCoors );
      if( visel != null ) {
        editing = true;
        return true;
      }
    }
    editing = false;
    return false;
  }

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    VedAbstractVisel visel = getVisel();
    if( visel != null ) {
      if( editing ) {
        String text = visel.props().getStr( PROPID_TEXT );
        if( aState == 0 ) {
          switch( aCode ) {
            case SWT.ARROW_LEFT:
              caretPos--;
              if( caretPos < 0 ) {
                caretPos = 0;
              }
              visel.props().setInt( PROPID_CARET_POS, caretPos );
              return true;
            case SWT.ARROW_RIGHT:
              caretPos++;
              if( caretPos > text.length() ) {
                caretPos = text.length();
              }
              visel.props().setInt( PROPID_CARET_POS, caretPos );
              return true;
            case SWT.DEL:
              if( caretPos < text.length() ) {
                String str1 = text.substring( 0, caretPos );
                String str2 = text.substring( caretPos + 1 );
                visel.props().setStr( PROPID_TEXT, str1 + str2 );
              }
              return true;
            default:
              break;
          }
        }

        StringBuilder sb = new StringBuilder( text );
        if( aChar == SWT.BS ) {
          if( caretPos > 0 && text.length() > 0 ) {
            String str1 = text.substring( 0, caretPos - 1 );
            String str2 = TsLibUtils.EMPTY_STRING;
            if( caretPos < text.length() - 1 ) {
              str2 = text.substring( caretPos );
            }
            caretPos--;
            visel.props().setInt( PROPID_CARET_POS, caretPos );
            visel.props().setStr( PROPID_TEXT, str1 + str2 );
          }
          return true;
        }

        sb.insert( caretPos, aChar );
        caretPos++;
        visel.props().setInt( PROPID_CARET_POS, caretPos );
        visel.props().setStr( PROPID_TEXT, sb.toString() );
        return true;
      }
    }
    return false;
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
