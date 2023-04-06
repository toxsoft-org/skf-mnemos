package org.toxsoft.skf.mnemo.lib.impl;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * {@link ISkMnemosService} implementation.
 *
 * @author hazard157
 */
public class SkMnemosService
    extends AbstractSkService
    implements ISkMnemosService {

  /**
   * Service creator singleton.
   */
  public static final ISkServiceCreator<AbstractSkService> CREATOR = SkMnemosService::new;

  /**
   * Constructor.
   *
   * @param aCoreApi {@link IDevCoreApi} - owner core API implementation
   */
  public SkMnemosService( IDevCoreApi aCoreApi ) {
    super( SERVICE_ID, aCoreApi );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkService
  //

  @Override
  public ISkMnemoSection findSection( String aSectionId ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStridablesList<ISkMnemoSection> listSections() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ISkMnemoSection defineSection( String aSectionId, IOptionSet aParams ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void removeSection( String aSectionId ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void doInit( ITsContextRo aArgs ) {
    // TODO Auto-generated method stub

  }

  @Override
  protected void doClose() {
    // TODO Auto-generated method stub

  }

}
