package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.km5.ISkResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;

/**
 * M5-model of {@link SwitchPerspInfo}.
 *
 * @author dima
 */
public class SwitchPerspInfoM5Model
    extends M5Model<SwitchPerspInfo> {

  /**
   * model id
   */
  public static final String MODEL_ID = "SwitchPerspInfoM5Model"; //$NON-NLS-1$

  /**
   * id of perspective
   */
  public static final String FID_PERSP_ID = "perspId"; //$NON-NLS-1$

  /**
   * id of view in perspective
   */
  public static final String FID_VIEW_ID = "viewId"; //$NON-NLS-1$

  /**
   * Sensitive mouse button
   */
  public static final String FID_MOUSE_BTTN = "mouseButton"; //$NON-NLS-1$

  /**
   * Attribute {@link SwitchPerspInfo#perspId()}.
   */
  public final M5AttributeFieldDef<SwitchPerspInfo> PERSP_ID = new M5AttributeFieldDef<>( FID_PERSP_ID, VALOBJ, //
      TSID_KEEPER_ID, MPerspId.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjMPerspIdEditor.FACTORY_NAME ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_PERSP_ID, STR_D_PERSP_ID );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( SwitchPerspInfo aEntity ) {
      return avValobj( aEntity.perspId() );
    }

  };

  /**
   * Attribute {@link SwitchPerspInfo#viewId()}.
   */
  public final M5AttributeFieldDef<SwitchPerspInfo> VIEW_ID = new M5AttributeFieldDef<>( FID_VIEW_ID, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( STR_N_VIEW_ID, STR_D_VIEW_ID );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( SwitchPerspInfo aEntity ) {
      return avStr( aEntity.viewId() );
    }

  };

  /**
   * Attribute {@link SwitchPerspInfo#mouseButton() } hot mouse button
   */
  public M5AttributeFieldDef<SwitchPerspInfo> MOUSE_BTTN = new M5AttributeFieldDef<>( FID_MOUSE_BTTN, VALOBJ, //
      TSID_NAME, STR_N_MOUSE_BTTN, //
      TSID_DESCRIPTION, STR_D_MOUSE_BTTN, //
      TSID_KEEPER_ID, ERtActionMouseButton.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ERtActionMouseButton.LEFT ) ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( SwitchPerspInfo aEntity ) {
      return avValobj( aEntity.mouseButton() );
    }
  };

  /**
   * Constructor.
   */
  public SwitchPerspInfoM5Model() {
    super( MODEL_ID, SwitchPerspInfo.class );

    addFieldDefs( PERSP_ID, VIEW_ID, MOUSE_BTTN );
  }

  @Override
  protected IM5LifecycleManager<SwitchPerspInfo> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<SwitchPerspInfo> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

  /**
   * LM for this model.
   *
   * @author dima
   */
  static class LifecycleManager
      extends M5LifecycleManager<SwitchPerspInfo, Object> {

    public LifecycleManager( IM5Model<SwitchPerspInfo> aModel ) {
      super( aModel, false, true, false, false, null );
    }

    private static SwitchPerspInfo makeSwitchPerspInfo( IM5Bunch<SwitchPerspInfo> aValues ) {
      MPerspId perspId = aValues.getAsAv( FID_PERSP_ID ).asValobj();
      String viewId = aValues.getAsAv( FID_VIEW_ID ).asString();
      ERtActionMouseButton mouseBttn = aValues.getAsAv( FID_MOUSE_BTTN ).asValobj();

      return new SwitchPerspInfo( perspId, viewId, mouseBttn );
    }

    @Override
    protected SwitchPerspInfo doCreate( IM5Bunch<SwitchPerspInfo> aValues ) {
      return makeSwitchPerspInfo( aValues );
    }

    @Override
    protected SwitchPerspInfo doEdit( IM5Bunch<SwitchPerspInfo> aValues ) {
      return makeSwitchPerspInfo( aValues );
    }

    @Override
    protected void doRemove( SwitchPerspInfo aEntity ) {
      // nop
    }

  }

}
