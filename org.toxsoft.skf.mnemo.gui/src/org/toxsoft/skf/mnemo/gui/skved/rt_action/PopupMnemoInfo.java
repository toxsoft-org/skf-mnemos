package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.valobj.*;

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
  public static final PopupMnemoInfo EMPTY = new PopupMnemoInfo( Skid.NONE, Skid.NONE );

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
          Skid.KEEPER.write( aSw, aEntity.masterObj() );
        }

        @Override
        protected PopupMnemoInfo doRead( IStrioReader aSr ) {
          Skid mnemoSkid = Skid.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          Skid masterObj = Skid.KEEPER.read( aSr );
          return new PopupMnemoInfo( mnemoSkid, masterObj );
        }

      };

  public static final String CLASS_ID = "org.toxsoft.skf.mnemo.gui.skved.rt_action.PopupMnemoInfo";

  /**
   * Skid of mnemo
   */
  private final Skid mnemoSkid;

  /**
   * master object {@link Skid} for that mnemo. Can be {Skid#NONE}
   */
  private final Skid masterObj;

  /**
   * Конструктор.<br>
   *
   * @param aMnemoSkid - mnemo Skid
   * @param aMasterObj {@link Skid} - master object
   */
  public PopupMnemoInfo( Skid aMnemoSkid, Skid aMasterObj ) {
    mnemoSkid = aMnemoSkid;
    masterObj = aMasterObj;
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
   * @return configuration of mnemo
   */
  public Skid mnemoSkid() {
    return mnemoSkid;
  }

}
