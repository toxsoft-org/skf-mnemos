package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * M5-model of {@link ISkMnemoCfg}.
 *
 * @author hazard157
 */
public class SkMnemoM5Model
    extends KM5ModelBasic<ISkMnemoCfg> {

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public SkMnemoM5Model( ISkConnection aConn ) {
    super( ISkMnemoCfg.CLASS_ID, ISkMnemoCfg.class, aConn );
    DESCRIPTION.setFlags( M5FF_COLUMN );
    addFieldDefs( STRID, NAME, DESCRIPTION );
  }

  @Override
  protected IM5LifecycleManager<ISkMnemoCfg> doCreateLifecycleManager( Object aMaster ) {
    IM5Model<Object> model = skConn().scope().find( IM5Domain.class ).findModel( ISkMnemoCfg.CLASS_ID );
    return new MnemoM5LifecycleManager( SkMnemoM5Model.class.cast( model ), ISkMnemosService.class.cast( aMaster ) );
  }

}
