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
      new PopupMnemoInfo( Skid.NONE, "Mnemo", PopupMnemoResolverConfig.EMPTY, ERtActionMouseButton.LEFT, false, 0 );

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
          Skid.KEEPER.write( aSw, aEntity.mnemoSkid() ); // Skid of mnemo
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.windowCaption ); // Window caption
          aSw.writeSeparatorChar();
          if( aEntity.resolverConfig() != null ) {
            PopupMnemoResolverConfig.KEEPER.write( aSw, aEntity.resolverConfig() );
          }
          aSw.writeSeparatorChar();
          ERtActionMouseButton.KEEPER.write( aSw, aEntity.mouseButton() ); // selected mouse button
          aSw.writeSeparatorChar();
          aSw.writeBoolean( aEntity.doubleClick ); // double click
          aSw.writeSeparatorChar();
          aSw.writeInt( aEntity.keyMask ); // Key mask
        }

        @Override
        protected PopupMnemoInfo doRead( IStrioReader aSr ) {
          Skid mnemoSkid = Skid.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          String caption = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          PopupMnemoResolverConfig resCfg = PopupMnemoResolverConfig.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          ERtActionMouseButton mouseButton = ERtActionMouseButton.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          boolean dblClick = aSr.readBoolean(); // double click
          aSr.ensureSeparatorChar();
          int keyMask = aSr.readInt(); // Key mask
          return new PopupMnemoInfo( mnemoSkid, caption, resCfg, mouseButton, dblClick, keyMask );
        }

      };

  /**
   * The class ID.
   */
  public static String CLASS_ID = ISkMnemosServiceHardConstants.CLSID_POPUP_MNEMO_INFO;

  private final String windowCaption;

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

  private final boolean doubleClick;

  private final int keyMask;

  /**
   * Конструктор.<br>
   *
   * @param aMnemoSkid {@link Skid} - mnemo Skid
   * @param aCaption String - заголовок окна мнемохемы
   * @param aResCfg {@link PopupMnemoResolverConfig} - конфигурация разрешителя
   * @param aMouseButton {@link ERtActionMouseButton} - mouse hot bttn
   * @param aDoubleClick boolean - признак использования двойного щелчка
   * @param aKeyMask int - SWT маска нажатия управляющих клавиш (Alt, Ctrl, Shift)
   */
  public PopupMnemoInfo( Skid aMnemoSkid, String aCaption, PopupMnemoResolverConfig aResCfg,
      ERtActionMouseButton aMouseButton, boolean aDoubleClick, int aKeyMask ) {
    mnemoSkid = aMnemoSkid;
    windowCaption = aCaption;
    resolverCfg = aResCfg;
    mouseButton = aMouseButton;
    doubleClick = aDoubleClick;
    keyMask = aKeyMask;
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
   * Возвращает заголовок окна мнемосхемы.
   *
   * @return String - заголовок окна мнемосхемы
   */
  public String windowCaption() {
    return windowCaption;
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

  /**
   * Возвращает признак использования двойного щелчка мыши для вызова мнемосхемы
   *
   * @return <b>true</b> - использовать двойной щелчок<br>
   *         <b>false</b> - использовать одиночный щелчок
   */
  public boolean doubleClick() {
    return doubleClick;
  }

  /**
   * Возвращает комбинацию SWT кодов для нажатых управляющих клавиш
   *
   * @return int - комбинация SWT кодов для нажатых управляющих клавиш (Alt, Ctrl,Shift)
   */
  public int keyMask() {
    return keyMask;
  }

  /**
   * Возвращает Skid мастер-объекта мнемосхемы.<br>
   * В случае когда мастер-объект не требуется возвращает {@link Skid#NONE}.
   *
   * @return Skid - ИД мастер-объекта
   */
  public Skid masterSkid() {
    if( resolverCfg == PopupMnemoResolverConfig.EMPTY ) {
      return Skid.NONE;
    }
    if( !resolverCfg.masterObjId().isBlank() ) { // объект указан непосредственно
      return new Skid( resolverCfg.masterClassId(), resolverCfg.masterObjId() );
    }
    // resolverCfg.resolverConfig().
    return Skid.NONE;
  }

}
