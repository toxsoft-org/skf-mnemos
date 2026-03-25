package org.toxsoft.skf.mnemo.mned.lite.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.impl.*;

/**
 * LM for {@link RtControlM5Model}.
 * <p>
 *
 * @author vs
 */
public class RtControlM5LifecycleManager
    extends M5LifecycleManager<IRtControl, RtControlsManager> {

  public RtControlM5LifecycleManager( IM5Model<IRtControl> aModel, RtControlsManager aMaster ) {
    super( aModel, false, true, true, true, aMaster );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected IList<IRtControl> doListEntities() {
    return master().list();
  }

  @Override
  protected IRtControl doEdit( IM5Bunch<IRtControl> aValues ) {
    String id = aValues.getAsAv( FID_ID ).asString();
    IRtControl item = master().list().getByKey( id );
    IOptionSetEdit props = new OptionSet();
    props.setValue( FID_NAME, aValues.getAsAv( FID_NAME ) );
    props.setValue( FID_DESCRIPTION, aValues.getAsAv( FID_DESCRIPTION ) );
    // HERE add more properties (if any defined in model) here
    item.props().setProps( props );
    return item;
  }

  @Override
  public void remove( IRtControl aEntity ) {
    master().remove( aEntity.id() );
  }

}
