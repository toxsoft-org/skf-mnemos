package org.toxsoft.skf.mnemo.gui.tsgui.asp;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.tsgui.asp.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Поставщик действий печати мнемосхемы.
 *
 * @author vs
 */
public class AspPrint
    extends MethodPerActionTsActionSetProvider {

  /**
   * ID of action {@link #ACDEF_PRINT}.
   */
  static final String ACTID_PRINT_TO_FILE = VED_ACT_ID + ".printToFile"; //$NON-NLS-1$

  /**
   * Action: print the mnemo to file.
   */
  static final ITsActionDef ACDEF_PRINT_TO_FILE = ofPush2( ACTID_PRINT_TO_FILE, //
      STR_PRINT_TO_FILE, STR_PRINT_TO_FILE_D, ICONID_PRINT_MNEMO_TO_IMAGE );

  // private final Canvas canvas;
  private final IVedScreen vedScreen;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the mnemo screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  // public AspPrint( Canvas aCanvas ) {
  // canvas = TsNullArgumentRtException.checkNull( aCanvas );
  public AspPrint( IVedScreen aVedScreen ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    defineAction( ACDEF_PRINT_TO_FILE, this::doHandlePrintToFile, this::doIsEnabledPrintToFile );
    defineAction( ACDEF_PRINT, this::doHandlePrint, this::doIsEnabledPrint );
  }

  void doHandlePrintToFile() {
    Image img = null;
    GC gc = null;
    try {
      // img = new Image( Display.getDefault(), canvas.getClientArea().width, canvas.getClientArea().height );
      IVedCanvasCfg canvasCfg = vedScreen.view().canvasConfig();
      img = new Image( vedScreen.getDisplay(), canvasCfg.size().intX(), canvasCfg.size().intY() );
      gc = new GC( img );
      // Control ctrl = vedScreen.view().getControl();
      // ctrl.print( gc );

      ((VedScreen)vedScreen).paint( gc );

      FileDialog fd = new FileDialog( vedScreen.getShell(), SWT.SAVE );
      String filePath = fd.open();
      if( filePath != null && !filePath.isBlank() ) {
        ImageData imd = img.getImageData();
        ImageLoader loader = new ImageLoader();
        loader.data = new ImageData[] { imd };
        loader.save( filePath, SWT.IMAGE_PNG );
      }
    }
    finally {
      if( img != null ) {
        img.dispose();
      }
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  void doHandlePrint() {
    System.out.println( "Print" );
  }

  boolean doIsEnabledPrintToFile() {
    return true;
  }

  boolean doIsEnabledPrint() {
    return true;
  }

}
