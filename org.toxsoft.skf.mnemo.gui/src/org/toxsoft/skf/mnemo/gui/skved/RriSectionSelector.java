package org.toxsoft.skf.mnemo.gui.skved;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.gui.conn.*;

public class RriSectionSelector
    extends TsPanel {

  ISkRegRefInfoService rriServ    = null;
  ISkCoreApi           coreApi    = null;
  ISkRriSection        rriSection = null;

  private Text   fldSectionId;
  private Button btnBrowseSection;

  public RriSectionSelector( Composite aParent, String aRriSectionId, ITsGuiContext aContext ) {
    super( aParent, aContext );
    coreApi = aContext.get( ISkConnectionSupplier.class ).defConn().coreApi();
    rriServ = coreApi.getService( ISkRegRefInfoService.SERVICE_ID );
    init( this );
    if( aRriSectionId != null && !aRriSectionId.isBlank() ) {
      fldSectionId.setText( aRriSectionId );
      rriSection = rriServ.findSection( aRriSectionId );
    }
  }

  /**
   * Возвращает ИД НСИ секции или <code>null</code> если секция не выбрана.
   *
   * @return String - ИД НСИ секции или <code>null</code> если секция не выбрана
   */
  public String sectionId() {
    if( rriSection != null ) {
      return rriSection.id();
    }
    return null;
  }

  void init( Composite aParent ) {
    GridLayout gl = new GridLayout( 3, false );
    aParent.setLayout( gl );

    CLabel l = new CLabel( aParent, SWT.CENTER );
    l.setText( "Секция НСИ: " );

    fldSectionId = new Text( aParent, SWT.BORDER );
    fldSectionId.setEditable( false );
    fldSectionId.setLayoutData( new GridData( SWT.FILL, SWT.CENTER, true, false ) );

    btnBrowseSection = new Button( aParent, SWT.PUSH );
    btnBrowseSection.setText( "..." );
    btnBrowseSection.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        String sectionId = PanelRriSectionIdSelector.selectRriSectionId( fldSectionId.getText(), tsContext() );
        if( sectionId != null ) {
          fldSectionId.setText( sectionId );
          rriSection = rriServ.findSection( sectionId );
        }
      }
    } );
  }

}
