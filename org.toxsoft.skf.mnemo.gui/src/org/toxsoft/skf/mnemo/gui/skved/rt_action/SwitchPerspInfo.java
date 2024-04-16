package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.valobj.*;
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
  public static final SwitchPerspInfo EMPTY = new SwitchPerspInfo( TsLibUtils.EMPTY_STRING );

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
          aSw.writeAsIs( aEntity.perspId() );
        }

        @Override
        protected SwitchPerspInfo doRead( IStrioReader aSr ) {
          String perspId = aSr.readIdName();
          return new SwitchPerspInfo( perspId );
        }

      };

  /**
   * id of persp
   */
  private final String perspId;

  /**
   * Конструктор.<br>
   *
   * @param aPerspId - id of persp
   */
  public SwitchPerspInfo( String aPerspId ) {
    perspId = aPerspId;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * @return id of persp
   */
  public String perspId() {
    return perspId;
  }

}
