package org.toxsoft.skf.mnemo.lib;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Menmoschemes service.
 * <p>
 * Note: mnemoscheme ID {@link ISkMnemoCfg#strid()} must be unique across all sections, not just it's own section.
 *
 * @author hazard157
 */
public interface ISkMnemosService
    extends ISkService {

  /**
   * Service identifier.
   */
  String SERVICE_ID = ISkHardConstants.SK_SYSEXT_SERVICE_ID_PREFIX + ".Mnemoschemes"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Mnemos

  /**
   * Finds the mnemoscheme by ID.
   *
   * @param aMnemoId String - the mnemo ID
   * @return {@link ISkMnemoCfg} - found mnemoscheme or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ISkMnemoCfg findMnemo( String aMnemoId );

  /**
   * Returns the mnemoscheme by ID.
   *
   * @param aMnemoId String - the mnemo ID
   * @return {@link ISkMnemoCfg} - found mnemoscheme
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no mnemo with the specified ID
   */
  ISkMnemoCfg getMnemo( String aMnemoId );

  /**
   * Returns the IDs of all existing mnemos.
   *
   * @return {@link IStringList} - all mnemo IDs
   */
  IStringList listMnemosIds();

  /**
   * Creates new mnemoscheme with an empty config data.
   * <p>
   * Note: mnemoscheme ID {@link ISkMnemoCfg#strid()} must be unique across all sections, not just it's own section.
   *
   * @param aMnemoId String - the mnemo ID
   * @param aAttrs {@link IOptionSet} - values of {@link ISkObject#attrs()} of mnemo
   * @return {@link ISkMnemoCfg} - new mnemo
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation failed
   */
  ISkMnemoCfg createMnemo( String aMnemoId, IOptionSet aAttrs );

  /**
   * Updates attributes of an existing mnemoscheme while content (config data) remains intact.
   *
   * @param aMnemoId String - the mnemo ID
   * @param aAttrs {@link IOptionSet} - new values of {@link ISkObject#attrs()} of mnemo
   * @return {@link ISkMnemoCfg} - new or updated mnemo
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation failed
   */
  ISkMnemoCfg editMnemo( String aMnemoId, IOptionSet aAttrs );

  /**
   * Sets the configuration data of the existing mnemo.
   *
   * @param aMnemoId String - the mnemo ID
   * @param aData String - configuration data is the same as {@link ISkMnemoCfg#cfgData()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no mnemo with the specified ID
   */
  void setMnemoData( String aMnemoId, String aData );

  /**
   * Returns the mnemoscheme configuration data.
   *
   * @param aMnemoId String - the mnemo ID
   * @return String - configuration data is the same as {@link ISkMnemoCfg#cfgData()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no mnemo with the specified ID
   */
  String getMnemoData( String aMnemoId );

  /**
   * Removes mnemoscheme.
   * <p>
   * Does nothing is mnemoscheme does not exists.
   *
   * @param aMnemoId String - the mnemo ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException validation failed
   */
  void removeMnemo( String aMnemoId );

  // ------------------------------------------------------------------------------------
  // Service support

  /**
   * Returns the service validator.
   *
   * @return {@link ITsValidationSupport}&lt;{@link ISkMnemosServiceValidator}&gt; - the service validator
   */
  ITsValidationSupport<ISkMnemosServiceValidator> svs();

  /**
   * Returns the service eventer.
   *
   * @return {@link ITsEventer}&lt;{@link ISkMnemosServiceListener}&gt; - the service eventer
   */
  ITsEventer<ISkMnemosServiceListener> eventer();

}
