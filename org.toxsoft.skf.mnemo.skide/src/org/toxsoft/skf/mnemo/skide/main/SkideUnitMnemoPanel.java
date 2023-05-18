package org.toxsoft.skf.mnemo.skide.main;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;

/**
 * Реализация правой панели модуля работы с мнемосхемами.
 *
 * @author vs
 */
class SkideUnitMnemoPanel
    extends AbstractSkideUnitPanel {

  public SkideUnitMnemoPanel( ITsGuiContext aContext, ISkideUnit aUnit ) {
    super( aContext, aUnit );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {

    SashForm sash = new SashForm( aParent, SWT.HORIZONTAL );

    CLabel l1 = new CLabel( sash, SWT.BORDER | SWT.CENTER );
    l1.setText( "Mnemos  sections list" );

    CLabel l2 = new CLabel( sash, SWT.BORDER | SWT.CENTER );
    l2.setText( "Current section mnemos list" );

    sash.setWeights( 1, 2 );
    return sash;
  }

}
