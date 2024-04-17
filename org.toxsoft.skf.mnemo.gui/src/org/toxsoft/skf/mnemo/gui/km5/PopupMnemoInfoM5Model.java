package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.reports.gui.panels.valed.*;

/**
 * M5-model of {@link PopupMnemoInfo}.
 *
 * @author dima
 */
public class PopupMnemoInfoM5Model
    extends M5Model<PopupMnemoInfo> {

  /**
   * model id
   */
  public static final String MODEL_ID = "PopupMnemoInfoM5Model"; //$NON-NLS-1$

  /**
   * Skid field of mnemo
   */
  public static final String FID_MNEMO_SKID = "mnemoSkid"; //$NON-NLS-1$

  /**
   * Skid field of master object
   */
  public static final String FID_MASTER_SKID = "masterSkid"; //$NON-NLS-1$

  /**
   * Sensitive mouse button
   */
  public static final String FID_MOUSE_BTTN = "mouseButton"; //$NON-NLS-1$

  /**
   * Attribute {@link PopupMnemoInfo#masterObj() } master
   */
  public M5AttributeFieldDef<PopupMnemoInfo> MNEMO_SKID = new M5AttributeFieldDef<>( FID_MNEMO_SKID, VALOBJ, //
      TSID_NAME, "Skid мнемосхемы", //
      TSID_DESCRIPTION, "Skid мнемосхемы", //
      TSID_KEEPER_ID, Skid.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSkidEditor.FACTORY_NAME //
  ) {

    protected IAtomicValue doGetFieldValue( PopupMnemoInfo aEntity ) {
      return AvUtils.avValobj( aEntity.mnemoSkid() );
    }

  };

  /**
   * Attribute {@link PopupMnemoInfo#masterObj() } master
   */
  public M5AttributeFieldDef<PopupMnemoInfo> MASTER_SKID = new M5AttributeFieldDef<>( FID_MASTER_SKID, VALOBJ, //
      TSID_NAME, "Skid мастер объекта", //
      TSID_DESCRIPTION, "Skid мастер объекта", //
      TSID_KEEPER_ID, Skid.KEEPER_ID, //
      OPID_EDITOR_FACTORY_NAME, ValedAvValobjSkidEditor.FACTORY_NAME //
  ) {

    protected IAtomicValue doGetFieldValue( PopupMnemoInfo aEntity ) {
      return AvUtils.avValobj( aEntity.masterObj() );
    }

  };

  /**
   * Attribute {@link PopupMnemoInfo#mouseButton() } sensitive mouse button
   */
  public M5AttributeFieldDef<PopupMnemoInfo> MOUSE_BTTN = new M5AttributeFieldDef<>( FID_MOUSE_BTTN, VALOBJ, //
      TSID_NAME, "mouse button", //
      TSID_DESCRIPTION, "sensitive mouse button", //
      TSID_KEEPER_ID, ETmpMouseButton.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ETmpMouseButton.LEFT ) ) {

    @Override
    protected void doInit() {
      setFlags( M5FF_COLUMN );
    }

    protected IAtomicValue doGetFieldValue( PopupMnemoInfo aEntity ) {
      return avValobj( aEntity.mouseButton() );
    }
  };

  /**
   * Constructor.
   */
  public PopupMnemoInfoM5Model() {
    super( MODEL_ID, PopupMnemoInfo.class );

    addFieldDefs( MOUSE_BTTN, MNEMO_SKID, MASTER_SKID );
  }

  @Override
  protected IM5LifecycleManager<PopupMnemoInfo> doCreateDefaultLifecycleManager() {
    return new LifecycleManager( this );
  }

  @Override
  protected IM5LifecycleManager<PopupMnemoInfo> doCreateLifecycleManager( Object aMaster ) {
    return getLifecycleManager( null );
  }

  /**
   * LM for this model.
   *
   * @author dima
   */
  static class LifecycleManager
      extends M5LifecycleManager<PopupMnemoInfo, Object> {

    public LifecycleManager( IM5Model<PopupMnemoInfo> aModel ) {
      super( aModel, false, true, false, false, null );
    }

    private static PopupMnemoInfo makePopupMnemoInfo( IM5Bunch<PopupMnemoInfo> aValues ) {
      Skid mnemoSkid = aValues.getAsAv( FID_MNEMO_SKID ).asValobj();
      Skid masterSkid = aValues.getAsAv( FID_MASTER_SKID ).asValobj();
      ETmpMouseButton mouseBttn = aValues.getAsAv( FID_MOUSE_BTTN ).asValobj();

      return new PopupMnemoInfo( mnemoSkid, masterSkid, mouseBttn );
    }

    @Override
    protected PopupMnemoInfo doCreate( IM5Bunch<PopupMnemoInfo> aValues ) {
      return makePopupMnemoInfo( aValues );
    }

    @Override
    protected PopupMnemoInfo doEdit( IM5Bunch<PopupMnemoInfo> aValues ) {
      return makePopupMnemoInfo( aValues );
    }

    @Override
    protected void doRemove( PopupMnemoInfo aEntity ) {
      // nop
    }

  }

}
