package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * Parameters for switch persp action.
 * <p>
 *
 * @author dima
 */
public class SwitchPerspInfo {

  /**
   * The class ID.
   */
  public static String CLASS_ID = ISkMnemosServiceHardConstants.CLSID_SWITCH_PERSP_INFO;

  /**
   * Empty info.
   */
  public static final SwitchPerspInfo EMPTY =
      new SwitchPerspInfo( MPerspId.NONE, TsLibUtils.EMPTY_STRING, ERtActionMouseButton.LEFT );

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "SwitchPerspInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<SwitchPerspInfo> KEEPER =
      new AbstractEntityKeeper<>( SwitchPerspInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, SwitchPerspInfo aEntity ) {
          // id of persp
          MPerspId.KEEPER.write( aSw, aEntity.perspId() );
          aSw.writeSeparatorChar();
          // id of view
          aSw.writeQuotedString( aEntity.viewId() );
          aSw.writeSeparatorChar();
          // selected mouse button
          ERtActionMouseButton.KEEPER.write( aSw, aEntity.mouseButton() );
        }

        @Override
        protected SwitchPerspInfo doRead( IStrioReader aSr ) {
          MPerspId perspId = MPerspId.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          String viewId = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          ERtActionMouseButton mouseButton = ERtActionMouseButton.KEEPER.read( aSr );
          return new SwitchPerspInfo( perspId, viewId, mouseButton );
        }

      };

  /**
   * id of persp
   */
  private final MPerspId perspId;

  /**
   * id of view in persp
   */
  private final String viewId;

  /**
   * sensitive mouse button {@link ERtActionMouseButton} to run action.
   */
  private final ERtActionMouseButton mouseButton;

  /**
   * Конструктор.<br>
   *
   * @param aPerspId - id of persp
   * @param aViewId - id of view in perspective
   * @param aMouseButton {@link ERtActionMouseButton} - mouse hot bttn
   */
  public SwitchPerspInfo( MPerspId aPerspId, String aViewId, ERtActionMouseButton aMouseButton ) {
    perspId = aPerspId;
    viewId = aViewId;
    mouseButton = aMouseButton;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * @return id of persp
   */
  public MPerspId perspId() {
    return perspId;
  }

  /**
   * @return id of view in persp
   */
  public String viewId() {
    return viewId;
  }

  /**
   * @return sensitive mouse button
   */
  public ERtActionMouseButton mouseButton() {
    return mouseButton;
  }

}
