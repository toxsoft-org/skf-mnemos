package org.toxsoft.skf.mnemo.gui.glib;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Generic command is and ID and argument options set.
 *
 * @author hazard157
 */
public final class GenericCommand {

  private final String     cmdId;
  private final IOptionSet args;

  private GenericCommand( String aCmdId, IOptionSet aArgs ) {
    cmdId = aCmdId;
    args = aArgs;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the command ID.
   *
   * @return String - the command ID, always an IDpath
   */
  public String cmdId() {
    return cmdId;
  }

  public IOptionSet args() {
    return args;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return cmdId + '-' + args.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof GenericCommand that ) {
      return cmdId.equals( that.cmdId ) && args.equals( that.args );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + cmdId.hashCode();
    result = TsLibUtils.PRIME * result + args.hashCode();
    return result;
  }

}
