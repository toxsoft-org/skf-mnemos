package org.toxsoft.skf.mnemo.mned.lite.glib;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.mnemo.mned.lite.glib.ITsResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.BorderLayout;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;

/**
 * The SWT panel to inspect {@link IRtControl} properties.
 *
 * @author vs
 */
public class RtControlInspector
    extends TsPanel {

  private final IVedScreen vedScreen;
  private final CLabel     label;
  private final Text       idText;
  private final Button     btnCopy;  // copy item ID to clipboard
  private final Button     btnEdit;  // edit item ID
  private final ITinWidget tinWidget;
  private final EIconSize  iconSize; // label and buttons icons size

  private IRtControl rtControl = null;

  private boolean selfEditing = false;

  /**
   * Constructor.
   *
   * @param aParent {@link Composite} - SWT parent
   * @param aVedScreen {@link IVedScreen} - the screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public RtControlInspector( Composite aParent, IVedScreen aVedScreen ) {
    super( aParent, TsNullArgumentRtException.checkNull( aVedScreen ).tsContext() );
    vedScreen = aVedScreen;
    this.setLayout( new BorderLayout() );
    iconSize = hdpiService().getToolbarIconsSize().prevSize();
    // create ID field
    Composite northBoard = new Composite( this, SWT.BORDER );
    northBoard.setLayoutData( BorderLayout.NORTH );
    GridLayout gl1 = new GridLayout( 4, false );
    gl1.marginHeight = 2;
    northBoard.setLayout( gl1 );
    // label
    label = new CLabel( northBoard, SWT.CENTER );
    label.setText( STR_LABEL_ID + ": " ); //$NON-NLS-1$
    label.setToolTipText( STR_LABEL_ID );
    label.setLayoutData( new GridData( SWT.FILL, SWT.FILL, false, false ) );
    // ID read-only text
    idText = new Text( northBoard, SWT.READ_ONLY | SWT.BORDER );
    idText.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, false ) );
    // btnCopy
    btnCopy = new Button( northBoard, SWT.PUSH );
    // btnCopy.setText( STR_BTN_COPY );
    btnCopy.setToolTipText( STR_BTN_COPY_D );
    btnCopy.setImage( iconManager().loadStdIcon( ICONID_EDIT_COPY, iconSize ) );
    // btnEdit
    btnEdit = new Button( northBoard, SWT.PUSH );
    // btnEdit.setText( STR_BTN_EDIT );
    btnEdit.setToolTipText( STR_BTN_EDIT_D );
    btnEdit.setImage( iconManager().loadStdIcon( ICONID_DOCUMENT_EDIT, iconSize ) );
    // inspector tree
    ITsGuiContext ctx = new TsGuiContext( vedScreen.tsContext() );
    ctx.put( IVedScreen.class, vedScreen );
    tinWidget = new TinWidget( ctx );
    tinWidget.createControl( this );
    tinWidget.getControl().setLayoutData( BorderLayout.CENTER );
    // setup
    btnCopy.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        whenBtnCopyPressed();
      }
    } );
    btnEdit.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        whenBtnEditPressed();
      }
    } );
    tinWidget.addPropertyChangeListener( this::whenInspectorChanges );
    vedScreen.model().visels().eventer().addListener( this::whenRtControlChanged );
    updateVedKindIcon();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenRtControlChanged( @SuppressWarnings( "unused" ) IVedItemsManager<VedAbstractVisel> aSource,
      ECrudOp aOp, String aId ) {
    if( selfEditing || rtControl == null || aOp == null ) {
      return;
    }
    String currItemId = rtControl.id();
    if( aId == null || aId.equals( currItemId ) ) {
      switch( aOp ) {
        case CREATE: {
          // this must not happen, as I think now...
          // throw new TsInternalErrorRtException();
          break;
        }
        case REMOVE: {
          clearInspector();
          break;
        }
        case EDIT:
        case LIST: {
          refreshInspectorValues();
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
  }

  private void whenInspectorChanges( @SuppressWarnings( "unused" ) ITinWidget aSource, String aChangedPropId ) {
    if( selfEditing || rtControl == null ) {
      throw new TsInternalErrorRtException(); // this must not happen
    }
    try {
      selfEditing = true;
      ITinValue tv = tinWidget.getValue();
      IAtomicValue av = tv.childValues().getByKey( aChangedPropId ).atomicValue();
      /**
       * NOTE: due to IPropertiesSet implementation conceptual problem (see fix-me note in the
       * PropertiesSet.doBeforeSet() method body), here we MUST call batch update method setProps(), not a
       * setValue().<br>
       * Even when conceptual problem will be solved, this code will remain. Only this note need to be removed :)
       */
      IOptionSetEdit newValueAsSet = new OptionSet();
      newValueAsSet.setValue( aChangedPropId, av );
      rtControl.props().setProps( newValueAsSet );
    }
    finally {
      selfEditing = false;
    }
  }

  private void whenBtnCopyPressed() {
    if( rtControl != null ) {
      String s = rtControl.id();
      Clipboard clipboard = new Clipboard( getDisplay() );
      clipboard.setContents( new String[] { s }, new Transfer[] { TextTransfer.getInstance() } );
      clipboard.dispose();
    }
  }

  private void whenBtnEditPressed() {

    // TODO VedScreenItemInspector.whenBtnEditPressed()
    TsDialogUtils.underDevelopment( getShell() );
  }

  private void updateVedKindIcon() {
    String iconId = ICONID_TRANSPARENT;
    if( rtControl != null ) {
      IRtControlFactory factory = getFactory( rtControl );
      iconId = factory.iconId();
    }
    label.setImage( iconManager().loadStdIcon( iconId, iconSize ) );
  }

  private IRtControlFactory getFactory( IRtControl aRtControl ) {
    IRtControlFactoriesRegistry reg = tsContext().get( IRtControlFactoriesRegistry.class );
    return reg.get( aRtControl.factoryId() );
  }

  private void refreshInspectorValues() {
    idText.setText( rtControl.id() );
    IRtControlFactory factory = getFactory( rtControl );
    ITinValue tv = factory.typeInfo().makeValue( rtControl );
    tinWidget.setValue( tv );
  }

  private void clearInspector() {
    rtControl = null;
    idText.setText( TsLibUtils.EMPTY_STRING );
    tinWidget.setEntityInfo( null );
    tinWidget.setValue( null );
    updateVedKindIcon();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the inspected VED item,
   *
   * @return {@link IRtControl} - the inspected VED item or <code>null</code>
   */
  public IRtControl getVedItem() {
    return rtControl;
  }

  /**
   * Sets VED item to inspect.
   *
   * @param aItem {@link IRtControl} - the VED item to inspect or <code>null</code>
   * @throws TsItemNotFoundRtException the item is not in VED screen model
   */
  public void setRtControl( IRtControl aItem ) {
    if( aItem == null ) {
      clearInspector();
      return;
    }
    // update inspector
    rtControl = aItem;
    IRtControlFactory factory = getFactory( rtControl );
    tinWidget.setEntityInfo( factory.typeInfo() );
    refreshInspectorValues();
    updateVedKindIcon();
  }

}
