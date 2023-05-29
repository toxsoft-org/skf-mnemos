package org.toxsoft.skf.mnemo.lib;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.lib.ISkMnemosServiceSharedResources.*;

import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * The service constants.
 *
 * @author hazard157
 */
public interface ISkMnemosServiceHardConstants {

  /**
   * Mnemos configuration class ID.
   */
  String CLSID_MNEMO_CFG = ISkHardConstants.SK_ID + ".MnemoCfg"; //$NON-NLS-1$

  /**
   * ID of CLOB {@link #CLBINF_MNEMO_CFG_DATA}.
   */
  String CLBID_MNEMO_CFG_DATA = "cfgData"; //$NON-NLS-1$

  /**
   * CLOB {@link ISkMnemoCfg#cfgData()}.
   */
  IDtoClobInfo CLBINF_MNEMO_CFG_DATA = DtoClobInfo.create2( CLBID_MNEMO_CFG_DATA, //
      TSID_NAME, STR_CLB_MNEMO_CFG_DATA, //
      TSID_DESCRIPTION, STR_CLB_MNEMO_CFG_DATA_D //
  );

}
