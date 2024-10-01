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
 * @author dima, vs
 */
public class PopupMnemoInfo {

  /**
   * Empty info.
   */
  public static final PopupMnemoInfo EMPTY =
      new PopupMnemoInfo( Skid.NONE, PopupMnemoResolverConfig.EMPTY, ERtActionMouseButton.LEFT );

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
          if( aEntity.resolverConfig() != null ) {
            PopupMnemoResolverConfig.KEEPER.write( aSw, aEntity.resolverConfig() );
          }
          aSw.writeSeparatorChar();
          // selected mouse button
          ERtActionMouseButton.KEEPER.write( aSw, aEntity.mouseButton() );
        }

        @Override
        protected PopupMnemoInfo doRead( IStrioReader aSr ) {
          Skid mnemoSkid = Skid.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          PopupMnemoResolverConfig resCfg = PopupMnemoResolverConfig.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          ERtActionMouseButton mouseButton = ERtActionMouseButton.KEEPER.read( aSr );
          return new PopupMnemoInfo( mnemoSkid, resCfg, mouseButton );
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
   * Конфигурация разрешителя
   */
  private final PopupMnemoResolverConfig resolverCfg;

  /**
   * sensitive mouse button {@link ERtActionMouseButton} to run action.
   */
  private final ERtActionMouseButton mouseButton;

  /**
   * Конструктор.<br>
   *
   * @param aMnemoSkid {@link Skid} - mnemo Skid
   * @param aResCfg {@link PopupMnemoResolverConfig} - конфигурация разрешителя
   * @param aMouseButton {@link ERtActionMouseButton} - mouse hot bttn
   */
  public PopupMnemoInfo( Skid aMnemoSkid, PopupMnemoResolverConfig aResCfg, ERtActionMouseButton aMouseButton ) {
    mnemoSkid = aMnemoSkid;
    resolverCfg = aResCfg;
    mouseButton = aMouseButton;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает конфигурацию разрешителя.
   *
   * @return {@link PopupMnemoResolverConfig} - конфигурация разрешителя
   */
  public PopupMnemoResolverConfig resolverConfig() {
    return resolverCfg;
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
