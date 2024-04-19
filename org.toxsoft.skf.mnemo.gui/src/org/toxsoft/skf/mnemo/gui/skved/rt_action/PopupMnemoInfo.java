package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.valobj.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * Parameters for popup mnemo action.
 * <p>
 *
 * @author dima
 */
public class PopupMnemoInfo {

  /**
   * Empty info.
   */
  public static final PopupMnemoInfo EMPTY = new PopupMnemoInfo( Skid.NONE, Skid.NONE, ERtActionMouseButton.LEFT );

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "PopupMnemoInfo"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<PopupMnemoInfo> KEEPER =
      new AbstractEntityKeeper<>( PopupMnemoInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, PopupMnemoInfo aEntity ) {
          // Skid of mnemo
          Skid.KEEPER.write( aSw, aEntity.mnemoSkid() );
          aSw.writeSeparatorChar();
          // Skid of master object
          Skid.KEEPER.write( aSw, aEntity.masterObj() );
          aSw.writeSeparatorChar();
          // selected mouse button
          ERtActionMouseButton.KEEPER.write( aSw, aEntity.mouseButton() );
        }

        @Override
        protected PopupMnemoInfo doRead( IStrioReader aSr ) {
          Skid mnemoSkid = Skid.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          Skid masterObj = Skid.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          ERtActionMouseButton mouseButton = ERtActionMouseButton.KEEPER.read( aSr );
          return new PopupMnemoInfo( mnemoSkid, masterObj, mouseButton );
        }

      };

  /**
   * The class ID.
   */
  public static String CLASS_ID = ISkMnemosServiceHardConstants.CLSID_POPUP_MNEMO_INFO;

  /**
   * Skid of mnemo
   */
  private final Skid mnemoSkid;

  /**
   * master object {@link Skid} for that mnemo. Can be {Skid#NONE}
   */
  private final Skid masterObj;

  /**
   * sensitive mouse button {@link ERtActionMouseButton} to run action.
   */
  private final ERtActionMouseButton mouseButton;

  /**
   * Конструктор.<br>
   *
   * @param aMnemoSkid {@link Skid} - mnemo Skid
   * @param aMasterObj {@link Skid} - master object
   * @param aMouseButton {@link ERtActionMouseButton} - mouse hot bttn
   */
  public PopupMnemoInfo( Skid aMnemoSkid, Skid aMasterObj, ERtActionMouseButton aMouseButton ) {
    mnemoSkid = aMnemoSkid;
    masterObj = aMasterObj;
    mouseButton = aMouseButton;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * @return master object {@link Skid} of mnemo.
   */
  public Skid masterObj() {
    return masterObj;
  }

  /**
   * @return Skid of mnemo
   */
  public Skid mnemoSkid() {
    return mnemoSkid;
  }

  /**
   * @return sensitive mouse button
   */
  public ERtActionMouseButton mouseButton() {
    return mouseButton;
  }

}
