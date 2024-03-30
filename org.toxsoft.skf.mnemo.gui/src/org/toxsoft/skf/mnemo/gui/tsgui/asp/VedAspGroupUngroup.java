package org.toxsoft.skf.mnemo.gui.tsgui.asp;

import static org.toxsoft.core.tsgui.bricks.actions.TsActionDef.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.editor.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.tsgui.*;

public class VedAspGroupUngroup
    extends MethodPerActionTsActionSetProvider
    implements IVedContextMenuCreator, IVedGroupChangeListener, ITsGuiContextable {

  /**
   * ID of action {@link #ACDEF_GROUP}.
   */
  public static final String ACTID_GROUP = "ved.group"; //$NON-NLS-1$

  /**
   * ID of action {@link #ACDEF_UNGROUP}.
   */
  public static final String ACTID_UNGROUP = "ved.ungroup"; //$NON-NLS-1$

  /**
   * Action: copy selected visels and associated actors to the internal buffer and delete it from model.
   */
  public static final ITsActionDef ACDEF_GROUP = ofPush2( ACTID_GROUP, //
      "Группировать", "Сгруппировать выделенные элементы", null );

  /**
   * Action: copy selected visels and associated actors to the internal buffer.
   */
  public static final ITsActionDef ACDEF_UNGROUP = ofPush2( ACTID_UNGROUP, //
      "Разгруппировать", "Разруппировать выделенные элементы", null );

  private final IVedScreen vedScreen;

  private final IVedViselSelectionManager selectionManager;

  private final IVedViselGroupsManager groupManager;

  private final MenuCreatorFromAsp menuCreator;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the screen to handle
   * @param aSelectionManager IVedViselSelectionManager - manager of the selected visels
   * @param aGroupManager IVedViselGroupsManager - manager of visel groups
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedAspGroupUngroup( IVedScreen aVedScreen, IVedViselSelectionManager aSelectionManager,
      IVedViselGroupsManager aGroupManager ) {
    vedScreen = TsNullArgumentRtException.checkNull( aVedScreen );
    selectionManager = aSelectionManager;
    groupManager = aGroupManager;
    // groupManager.addGroupChangeListener( this );
    selectionManager.genericChangeEventer().addListener( this::onSelectionChanged );
    defineAction( ACDEF_GROUP, this::doGroup );
    defineAction( ACDEF_UNGROUP, this::doUngroup );
    menuCreator = new MenuCreatorFromAsp( this, vedScreen.tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // MethodPerActionTsActionSetProvider
  //

  @Override
  protected boolean doIsActionEnabled( ITsActionDef aActionDef ) {
    switch( aActionDef.id() ) {
      case ACTID_GROUP: {
        IStringList selIds = selectionManager.selectedViselIds();
        if( selIds.size() < 2 ) {
          return false;
        }
        IStridablesList<VedAbstractVisel> visels = vedScreen.model().visels().list();
        IStringList groupIds = groupManager.viselGroupIds( selIds.first() );
        if( groupIds.isEmpty() ) { // элемент не в группе
          return true;
        }
        for( String viselId : selIds ) {
          VedAbstractVisel visel = visels.getByKey( viselId );
          if( !groupIds.equals( groupManager.viselGroupIds( visel.id() ) ) ) {
            return true;
          }
        }
        return false;
      }
      case ACTID_UNGROUP: {
        // if( activeVisel == null ) {
        // return selectionManager.selectionKind() != ESelectionKind.NONE;
        // }
        return true;
      }
      default:
        return true;
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedContextMenuCreator
  //

  @Override
  public boolean fillMenu( Menu aMenu, VedAbstractVisel aClickedVisel, ITsPoint aSwtCoors ) {
    return menuCreator.fillMenu( aMenu );
  }

  // ------------------------------------------------------------------------------------
  // IVedGroupChangeListener
  //

  @Override
  public void onGroup( String aGroupId, IStringList aViselIds ) {
    // TODO Auto-generated method stub
  }

  @Override
  public void onUngroup( String aGroupId, IStringList aViselIds ) {
    // TODO Auto-generated method stub
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  void doGroup() {
    groupManager.groupVisels( selectionManager.selectedViselIds() );
  }

  void doUngroup() {
    String viselId = selectionManager.selectedViselIds().first();
    String groupId = groupManager.viselGroupIds( viselId ).last();
    groupManager.ungroupVisels( groupId );
  }

  void onSelectionChanged( @SuppressWarnings( "unused" ) Object aSource ) {
    actionsStateEventer().fireChangeEvent();
  }

}
