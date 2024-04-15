package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.io.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
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
   * Skid field of mnemo
   */
  public static final String FID_PERSP_ID = "perspId"; //$NON-NLS-1$

  /**
   * Attribute {@link File#getName()}.
   */
  public final M5AttributeFieldDef<SwitchPerspInfo> PERSP_ID = new M5AttributeFieldDef<>( FID_PERSP_ID, STRING ) {

    @Override
    protected void doInit() {
      setNameAndDescription( "persp id", "Eclipse perspective id" );
      setDefaultValue( AV_STR_EMPTY );
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( SwitchPerspInfo aEntity ) {
      return avStr( aEntity.perspId() );
    }

  };

  /**
   * Constructor.
   */
  public SwitchPerspInfoM5Model() {
    super( MODEL_ID, SwitchPerspInfo.class );

    addFieldDefs( PERSP_ID );
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

    private static SwitchPerspInfo makePopupMnemoInfo( IM5Bunch<SwitchPerspInfo> aValues ) {
      String perspId = aValues.getAsAv( FID_PERSP_ID ).asString();

      return new SwitchPerspInfo( perspId );
    }

    @Override
    protected SwitchPerspInfo doCreate( IM5Bunch<SwitchPerspInfo> aValues ) {
      return makePopupMnemoInfo( aValues );
    }

    @Override
    protected SwitchPerspInfo doEdit( IM5Bunch<SwitchPerspInfo> aValues ) {
      return makePopupMnemoInfo( aValues );
    }

    @Override
    protected void doRemove( SwitchPerspInfo aEntity ) {
      // nop
    }

  }

}
