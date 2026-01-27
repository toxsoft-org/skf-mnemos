package org.toxsoft.skf.mnemo.mned.lite.rtc;

import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Configuration of the mnemoscheme RtControls manager.
 *
 * @author vs
 */
public interface IRtControlsManagerCfg {

  // /**
  // * Empty configuration singleton.
  // */
  // IRtControlsManagerCfg EMPTY = new InternalEmptyRtControlsManagerCfg();

  /**
   * @return IStridablesList&lt;IRtControlCfg> - list of the RtControls configs
   */
  IStridablesList<IRtControlCfg> rtControlCfgList();

  // /**
  // * Save needed information into the VED screen configuration.
  // *
  // * @param aScreenCfg {@link IVedScreenCfg} - VED screen configuration
  // */
  // void save( IVedScreenCfg aScreenCfg );
}

// class InternalEmptyRtControlsManagerCfg
// implements IRtControlsManagerCfg {
//
// @Override
// public IStridablesList<IRtControlCfg> rtControlCfgList() {
// return IStridablesList.EMPTY;
// }
//
// @Override
// public void save( IVedScreenCfg aScreenCfg ) {
// // nop
// }
//
// }
