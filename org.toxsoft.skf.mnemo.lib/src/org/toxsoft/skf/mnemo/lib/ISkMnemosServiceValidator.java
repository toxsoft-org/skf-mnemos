package org.toxsoft.skf.mnemo.lib;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ISkMnemosService} validator.
 *
 * @author hazard157
 */
public interface ISkMnemosServiceValidator {

  /**
   * Checks if mnemoCfg can be created.
   *
   * @param aMnemoId String - the mnemo ID
   * @param aAttrs {@link IOptionSet} - attribute values
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canCreateMnemoCfg( String aMnemoId, IOptionSet aAttrs );

  /**
   * Checks if mnemoCfg can be edited.
   *
   * @param aMnemoId String - the mnemo ID
   * @param aAttrs {@link IOptionSet} - attribute values
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canEditMnemoCfg( String aMnemoId, IOptionSet aAttrs );

  /**
   * Checks if the configuration data of the existing mnemo can be changed.
   *
   * @param aMnemoId String - the mnemo ID
   * @param aData String - configuration data is the same as {@link ISkMnemoCfg#cfgData()}
   * @param aMnemoCfg {@link ISkMnemoCfg} - current mnemoCfg data
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no mnemo with the specified ID
   */
  ValidationResult canSetMnemoData( String aMnemoId, String aData, ISkMnemoCfg aMnemoCfg );

  /**
   * Checks if mnemo section may removed.
   *
   * @param aMnemoId String - ID of section to remove
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemoveMnemoCfg( String aMnemoId );

}
