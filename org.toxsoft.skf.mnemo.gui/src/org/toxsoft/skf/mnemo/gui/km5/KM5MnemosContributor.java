package org.toxsoft.skf.mnemo.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * Contributes M5-model for {@link ISkMnemosService} manager classes.
 *
 * @author vs
 */
public class KM5MnemosContributor
    extends KM5AbstractContributor {

  /**
   * Creator singleton.
   */
  public static final IKM5ContributorCreator CREATOR = KM5MnemosContributor::new;

  private static final IStringList CONRTIBUTED_MODEL_IDS = new StringArrayList( //
      PopupMnemoInfo.CLASS_ID, //
      SwitchPerspInfo.CLASS_ID, //
      ISkMnemoCfg.CLASS_ID //
  );

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @param aDomain {@link IM5Domain} - connection domain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KM5MnemosContributor( ISkConnection aConn, IM5Domain aDomain ) {
    super( aConn, aDomain );
  }

  @Override
  protected IStringList papiCreateModels() {
    m5().addModel( new SkMnemoCfgM5Model( skConn() ) );
    m5().addModel( new PopupMnemoInfoM5Model() );
    m5().addModel( new SwitchPerspInfoM5Model() );
    return CONRTIBUTED_MODEL_IDS;
  }
}
