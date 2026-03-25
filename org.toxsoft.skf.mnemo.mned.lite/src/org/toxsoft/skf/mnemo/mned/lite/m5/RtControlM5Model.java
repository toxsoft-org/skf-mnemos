package org.toxsoft.skf.mnemo.mned.lite.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.m5.IVedM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.impl.*;

/**
 * M5-model of {@link IRtControl}.
 * <p>
 *
 * @author vs
 */
public class RtControlM5Model
    extends M5Model<IRtControl> {

  /**
   * Field {@link IRtControl#id()}
   */
  public final IM5AttributeFieldDef<IRtControl> ID = new M5StdFieldDefId<>( //
      TSID_NAME, STR_ITEM_ID, //
      TSID_DESCRIPTION, STR_ITEM_ID_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN | M5FF_INVARIANT ) //
  ) {

    protected Image doGetFieldValueIcon( IRtControl aEntity, EIconSize aIconSize ) {
      String iconId = aEntity.iconId();
      if( iconId != null ) {
        return iconManager().loadStdIcon( iconId, aIconSize );
      }
      return null;
    }

  };

  /**
   * Field {@link IRtControl#factoryId()}
   */
  public final IM5AttributeFieldDef<IRtControl> FACTORY_ID = new M5AttributeFieldDef<>( FID_FACTORY_ID, DDEF_IDPATH, //
      TSID_NAME, STR_ITEM_FACTORY_ID, //
      TSID_DESCRIPTION, STR_ITEM_FACTORY_ID_D, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL | M5FF_INVARIANT ) //
  ) {

    protected IAtomicValue doGetFieldValue( IRtControl aEntity ) {
      return avStr( aEntity.factoryId() );
    }

  };

  /**
   * Field displays the name of the factory by {@link IRtControl#factoryId()}.
   */
  public final IM5AttributeFieldDef<IRtControl> FACTORY_NAME = new M5AttributeFieldDef<>( FID_FACTORY_NAME, DDEF_STRING, //
      TSID_NAME, STR_ITEM_FACTORY_NAME, //
      TSID_DESCRIPTION, STR_ITEM_FACTORY_NAME_D, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL | M5FF_INVARIANT ) //
  ) {

    protected IAtomicValue doGetFieldValue( IRtControl aEntity ) {
      IRtControlFactory factory = tsContext().get( IRtControlFactoriesRegistry.class ).find( aEntity.factoryId() );
      if( factory == null ) {
        return AV_STR_EMPTY;
      }
      return avStr( factory.nmName() );
    }

  };

  /**
   * Field {@link IVedItem#nmName()}
   */
  public final IM5AttributeFieldDef<IRtControl> NAME = new M5StdFieldDefName<>( //
      TSID_NAME, STR_ITEM_NAME, //
      TSID_DESCRIPTION, STR_ITEM_NAME_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN ) //
  );

  /**
   * Field {@link IRtControl#description()}
   */
  public final IM5AttributeFieldDef<IRtControl> DESCRIPTION = new M5StdFieldDefDescription<>( //
      TSID_NAME, STR_ITEM_DESCRIPTION, //
      TSID_DESCRIPTION, STR_ITEM_DESCRIPTION_D, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL ) //
  );

  /**
   * Constructor.
   *
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException identifier is not an IDpath
   */
  public RtControlM5Model() {
    super( MID_RT_CONTROL, IRtControl.class );
    addFieldDefs( ID, FACTORY_ID, FACTORY_NAME, NAME, DESCRIPTION );

    setName( "Элементы мнемосхемы" );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      protected IM5CollectionPanel<IRtControl> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IRtControl> aItemsProvider, IM5LifecycleManager<IRtControl> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_REORDER.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_TREE_MODES.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_ACTIONS_HIDE_PANES.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_DETAILS_PANE_HIDDEN.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUMMARY_PANE_HIDDEN.setValue( aContext.params(), AV_FALSE );
        MultiPaneComponentModown<IRtControl> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );

  }

  @Override
  protected IM5LifecycleManager<IRtControl> doCreateLifecycleManager( Object aMaster ) {
    return new RtControlM5LifecycleManager( this, RtControlsManager.class.cast( aMaster ) );
  }

}
