package org.toxsoft.skf.mnemo.lib;

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
   * Configuration data is stored as CLOB. Note that size of {@link String} is limited to 2GB. So every call to this
   * method leads to resource-heavy call the the server/backend.
   *
   * @return String - huge string with configuration data
   */
  String cfgData();

  // TODO some additional info?

}
