package org.toxsoft.skf.mnemo.mws.simple.e4.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.toxsoft.skf.mnemo.mws.simple.IMnemoMwsSimpleConstants;
import org.toxsoft.skf.mnemo.mws.simple.e4.main.IMnemoschemesPerspectiveController;

import jakarta.inject.Named;

/**
 * Command: open mnemoscheme by specified ID.
 * <p>
 * Command ID: {@link IMnemoMwsSimpleConstants#CMDID_OPEN_MNEMO_BY_ID}<br>
 * Arguments: <br>
 * <ul>
 * <li>{@link IMnemoMwsSimpleConstants#CMDID_OPEN_MNEMO_BY_ID} - the mnemoscheme ID ;</li>
 * </ul>
 * Simple calls {@link IMnemoschemesPerspectiveController#openMnemoscheme(String)} with the specified argument.
 *
 * @author hazard157
 */
public class CmdOpenMnemoUipart {

  @Execute
  void exec( @Named( IMnemoMwsSimpleConstants.CMDARGID_MNEMO_ID ) @Optional String aMnemoId,
      IMnemoschemesPerspectiveController aController ) {
    aController.openMnemoscheme( aMnemoId );
  }

}
