package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;

/**
 * Выпадающий список подмастеров мнемосхемы.
 * <p>
 *
 * @author vs
 */
public class SubMastersCombo {

  ComboViewer viewer;

  private final IVedScreen vedScreen;

  /**
   * Конструктор.
   *
   * @param aParent Composite - родительская панель
   * @param aVedScreen {@link IVedScreen} - экран мнемосхемы
   */
  SubMastersCombo( Composite aParent, IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
    viewer = new ComboViewer( aParent, SWT.DROP_DOWN );
    viewer.setContentProvider( new ArrayContentProvider() );
    viewer.setLabelProvider( new LabelProvider() {

      @Override
      public String getText( Object aElement ) {
        SubmasterConfig cfg = (SubmasterConfig)aElement;
        if( cfg.id().equals( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID ) ) {
          return TsLibUtils.EMPTY_STRING;
        }
        if( cfg.params().hasKey( TSID_NAME ) ) {
          return cfg.params().getStr( TSID_NAME );
        }
        return cfg.id();
      }
    } );
    refresh();
  }

  void refresh() {
    VedScreenCfg scrCfg = VedScreenUtils.getVedScreenConfig( vedScreen );
    MnemoResolverConfig cfg = MasterObjectUtils.readMnemoResolverConfig( scrCfg );
    viewer.setInput( cfg.subMasters().toArray() );
  }

  Combo getControl() {
    return viewer.getCombo();
  }

  SubmasterConfig selectedConfig() {
    IStructuredSelection selection = (IStructuredSelection)viewer.getSelection();
    if( !selection.isEmpty() ) {
      return (SubmasterConfig)selection.getFirstElement();
    }
    return null;
  }

  void selectSubmasterCnfig( SubmasterConfig aCfg ) {
    Object[] input = (Object[])viewer.getInput();
    for( int i = 0; i < input.length; i++ ) {
      if( ((SubmasterConfig)input[i]).id().equals( aCfg.id() ) ) {
        viewer.getCombo().select( i );
        break;
      }
    }
    // viewer.setSelection( new StructuredSelection( aCfg ) );
  }

  void addSelectionChangedListener( ISelectionChangedListener aListener ) {
    viewer.addSelectionChangedListener( aListener );
  }

}
