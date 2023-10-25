package org.toxsoft.skf.mnemo.skide.e4.uiparts;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import java.io.*;

import org.eclipse.e4.ui.di.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.e4.services.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.skide.glib.*;

/**
 * Mnemoscheme editor UIpart.
 * <p>
 * Contain {@link IMnemoEditorPanel} as a mnemoscheme editor.
 * <p>
 * Multiple instances of this UIpart is managed by {@link ISkMnemoEditService}. This UIpart remembers the source of the
 * mnemoscheme configuration to save it back to the source. The source may be the Sk-connection or the file, depends on
 * which method was called {@link #setMnemoCfg(ISkMnemoCfg)} or {@link #setMnemoCfg(File, IVedScreenCfg)} respectively.
 * One of mentioned method must be called before user starts working with this UIpart.
 *
 * @author hazard157
 */
public class UipartSkMnemoEditor
    extends MwsAbstractPart {

  /**
   * TODO prevent UIpart close when editor is dirty<br>
   */

  IMnemoEditorPanel panel;

  ISkMnemoCfg skMnemocfg = null; // non-null value means mnemoscheme is loaded from Skconnection
  File        mnemoFile  = null; // non-null value means mnemoscheme was loaded from the file

  @Override
  protected void doInit( Composite aParent ) {
    panel = new MnemoEditorPanel( aParent, tsContext() );
    panel.setExternelHandler( this::processEditorPanelExternalAction );
    panel.mnemoChangedEventer().addListener( s -> whenEditorDirtyStateChanges() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void processEditorPanelExternalAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_SAVE: {
        save();
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aActionId );
    }
  }

  /**
   * Saves the mnemoscheme to is's source.
   * <p>
   * Method is also called when closing the dirty view (if user selects to svae the changes).
   */
  @Persist
  void save() {
    IVedScreenCfg scrCfg = panel.getCurrentConfig();
    if( skMnemocfg != null ) {
      String cfgStr = VedScreenCfg.KEEPER.ent2str( scrCfg );
      skMnemocfg.setCfgData( cfgStr );
      panel.setChanged( false );
      return;
    }
    if( mnemoFile != null ) {
      VedScreenCfg.KEEPER.write( mnemoFile, scrCfg );
      panel.setChanged( false );
      return;
    }
    throw new TsInternalErrorRtException();
  }

  private void whenEditorDirtyStateChanges() {
    getSelfPart().setDirty( panel.isChanged() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets mnemoscheme to be edited.
   * <p>
   * The source of the mnemoscheme is an Sk-connection.
   *
   * @param aSkCfg {@link ISkMnemoCfg} - mnemoscheme configuration loaded from the Sk-connection
   */
  public void setMnemoCfg( ISkMnemoCfg aSkCfg ) {
    TsNullArgumentRtException.checkNull( aSkCfg );
    TsInternalErrorRtException.checkNoNull( skMnemocfg );
    TsInternalErrorRtException.checkNoNull( mnemoFile );
    // check config validity
    String strMnemoCfg = aSkCfg.cfgData();
    IVedScreenCfg scrCfg = IVedScreenCfg.NONE;
    if( !strMnemoCfg.isEmpty() ) {
      scrCfg = VedScreenCfg.KEEPER.str2ent( strMnemoCfg );
    }
    panel.setCurrentConfig( scrCfg );
    skMnemocfg = aSkCfg;
  }

  public void setMnemoCfg( File aFile, IVedScreenCfg aCfg ) {
    TsNullArgumentRtException.checkNull( aCfg );
    TsInternalErrorRtException.checkNoNull( skMnemocfg );
    TsInternalErrorRtException.checkNoNull( mnemoFile );

    // TODO UipartSkMnemoEditor.setMnemoCfg()

    TsDialogUtils.error( getShell(), "setMnemoCfg( File aFile, IVedScreenCfg aCfg );" );
  }

}
