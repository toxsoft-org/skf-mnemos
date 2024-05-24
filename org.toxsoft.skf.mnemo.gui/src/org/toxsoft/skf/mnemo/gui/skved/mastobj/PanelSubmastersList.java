package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.*;

/**
 * Панель с редактируемым списком sub-мастеров.
 * <p>
 *
 * @author vs
 */
public class PanelSubmastersList
    extends TsPanel
    implements ITsActionHandler {

  TsToolbar toolBar;

  StridableTableViewer viewer;

  // final IStridablesListEdit<ISkClassInfo> submasters = new StridablesList<>();
  final IListEdit<ICompoundResolverConfig> resolverConfigs = new ElemArrayList<>();

  private String masterClassId = TsLibUtils.EMPTY_STRING;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   */
  public PanelSubmastersList( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );
    setLayout( new BorderLayout() );
    toolBar = TsToolbar.create( this, aContext, EIconSize.IS_24X24, ACDEF_ADD, ACDEF_REMOVE );
    toolBar.setNameLabelText( "Sub-мастера: " );
    toolBar.getControl().setLayoutData( BorderLayout.NORTH );

    int style = SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.FULL_SELECTION;
    viewer = new StridableTableViewer( this, style, 120, 200, 0 );
    viewer.viewer().getControl().setLayoutData( BorderLayout.CENTER );
    viewer.viewer().addSelectionChangedListener( aEvent -> {
      IStructuredSelection selection = (IStructuredSelection)aEvent.getSelection();
      toolBar.getAction( ACTID_REMOVE ).setEnabled( !selection.isEmpty() );
    } );
    toolBar.getAction( ACTID_REMOVE ).setEnabled( false );
    toolBar.addListener( this );
  }

  // ------------------------------------------------------------------------------------
  // ITsActionHandler
  //

  @Override
  public void handleAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_ADD: {
        Gwid gwid = Gwid.createClass( masterClassId );
        ICompoundResolverConfig resCfg = DirectGwidResolver.createResolverConfig( gwid );
        PanelCompoundResolverConfig.edit( resCfg, tsContext() );

        // ISkClassInfo clsInfo = SkGuiUtils.selectClass( null, tsContext() );
        // if( clsInfo != null ) {
        // submasters.add( clsInfo );
        // viewer.viewer().setInput( submasters.toArray() );
        // }
      }
        break;
      case ACTID_REMOVE: {
        // IStructuredSelection selection = (IStructuredSelection)viewer.viewer().getSelection();
        // ISkClassInfo clsInfo = (ISkClassInfo)selection.getFirstElement();
        // submasters.remove( clsInfo );
        // viewer.viewer().setInput( submasters.toArray() );
      }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException( "%s", aActionId ); //$NON-NLS-1$
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * @param aMasterClassId String - ИД класса мастер-объекта мнемосхемы
   */
  public void setMasterClassId( String aMasterClassId ) {
    masterClassId = aMasterClassId;
  }

}
