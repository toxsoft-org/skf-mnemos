package org.toxsoft.skf.mnemo.lib;

import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Configuration of mnemoscheme is stored as an Sk-object.
 *
 * @author hazard157
 */
public interface ISkMnemoCfg
    extends ISkObject {

  /**
   * The class ID.
   */
  String CLASS_ID = ISkMnemosServiceHardConstants.CLSID_MNEMO_CFG;

  /**
   * Mnemoscheme configuration data stored as string.
   * <p>
   * Mnemo service does not makes any assumption about config data format. It may be any XML, JSON, KTOR or whatever.
   * <p>
   * Configuration data is stored as CLOB. So every call to this method leads to resource-heavy call the the
   * server/backend.
   * <p>
   * Note that size of {@link String} is limited to 2GB.
   *
   * @return String - huge string with configuration data
   */
  String cfgData();

  /**
   * Sets the mnemoscheme configuration data.
   *
   * @param aCfgData String - the mnemoscheme configuration data
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setCfgData( String aCfgData );

}
