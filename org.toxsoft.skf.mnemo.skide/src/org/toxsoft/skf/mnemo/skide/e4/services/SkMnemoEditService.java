package org.toxsoft.skf.mnemo.skide.e4.services;

import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoConstants.*;

import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.impl.*;
import org.toxsoft.core.tsgui.mws.e4.helpers.partman.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.*;
import org.toxsoft.skf.mnemo.gui.e4.services.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.skide.uiparts.*;

public class SkMnemoEditService
    implements ISkMnemoEditService, ITsGuiContextable {

  private static final String MNEMO_PART_ID_PREFIX = "TheMnemoPart"; //$NON-NLS-1$

  private final ITsGuiContext tsContext;

  private ITsPartStackManager partStackManager = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkMnemoEditService( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  ITsPartStackManager psManager() {
    if( partStackManager == null ) {
      partStackManager = new TsPartStackManager( tsContext.eclipseContext(), PARTSTACKID_MNEMOS_STACK );
    }
    return partStackManager;
  }

  String makePartId( String aMnemoId ) {
    return StridUtils.makeIdPath( MNEMO_PART_ID_PREFIX, aMnemoId );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // ISkMnemoEditService
  //

  @Override
  public void switchTpPerspectiveAndEditMnemo( ISkMnemoCfg aMnemoCfg ) {
    e4Helper().switchToPerspective( PERSPID_MENMOS_EDITOR, null );
    // activate part for menmo if already exists
    String partId = makePartId( aMnemoCfg.id() );
    MPart found = psManager().findPart( partId );
    if( found != null ) {
      EPartService partService = tsContext.get( EPartService.class );
      partService.activate( found );
      return;
    }
    // open new part for mnrmo
    UIpartInfo partInfo = new UIpartInfo( partId );
    partInfo.setCloseable( true );
    partInfo.setContributionUri( org.toxsoft.skf.mnemo.skide.Activator.PLUGIN_ID, UipartSkMnemoEditor.class );
    partInfo.setIconUri( TsIconManagerUtils.imageUriFromPlugin( org.toxsoft.skf.mnemo.gui.Activator.PLUGIN_ID,
        ISkMnemoGuiConstants.ICONID_MNEMO ) );
    partInfo.setLabel( aMnemoCfg.nmName() );
    partInfo.setTooltip( aMnemoCfg.description() );
    MPart newPart = psManager().createPart( partInfo );
    // TODO set mnemo to edit
    UipartSkMnemoEditor editor = (UipartSkMnemoEditor)newPart.getObject();
    editor.setMnemoCfg( aMnemoCfg );

  }

}
