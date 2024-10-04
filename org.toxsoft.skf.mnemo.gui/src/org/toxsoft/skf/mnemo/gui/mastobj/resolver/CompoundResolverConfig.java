package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ICompoundResolverConfig} final immutable implementation.
 *
 * @author hazard157
 */
public final class CompoundResolverConfig
    implements ICompoundResolverConfig {

  /**
   * Constant of the configuration returning an empty string from {@link ICompoundResolverConfig#cfgs()}.
   */
  public static final ICompoundResolverConfig NONE = new CompoundResolverConfig( IList.EMPTY );

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ICompoundResolverConfig> KEEPER =
      new AbstractEntityKeeper<>( ICompoundResolverConfig.class, EEncloseMode.ENCLOSES_BASE_CLASS, NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ICompoundResolverConfig aEntity ) {
          SimpleResolverCfg.KEEPER.writeColl( aSw, aEntity.cfgs(), true );
        }

        @Override
        protected ICompoundResolverConfig doRead( IStrioReader aSr ) {
          IList<SimpleResolverCfg> ll = SimpleResolverCfg.KEEPER.readColl( aSr );
          return new CompoundResolverConfig( ll );
        }
      };

  private final IList<SimpleResolverCfg> cfgsList;

  /**
   * Constructor.
   *
   * @param aSimpleConfigs {@link IList}&lt;{@link SimpleResolverCfg}&gt; - simple configurations ordered list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is an empty list
   */
  public CompoundResolverConfig( IList<SimpleResolverCfg> aSimpleConfigs ) {
    TsNullArgumentRtException.checkNull( aSimpleConfigs );
    // TsIllegalArgumentRtException.checkTrue( aSimpleConfigs.isEmpty() ); Sol-- иначе NONE дает ошибку
    cfgsList = new ElemArrayList<>( aSimpleConfigs );
  }

  // ------------------------------------------------------------------------------------
  // ICompoundResolverConfig
  //

  @Override
  public IList<SimpleResolverCfg> cfgs() {
    return cfgsList;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  private final static char CH_SEPARATOR = '/';

  @Override
  public String toString() {
    if( this == NONE ) {
      return "none"; //$NON-NLS-1$
    }
    if( cfgsList.isEmpty() ) {
      return super.toString();
    }
    StringBuilder sb = new StringBuilder();
    for( int i = 0; i < cfgsList.size(); i++ ) {
      SimpleResolverCfg cfg = cfgsList.get( i );
      sb.append( cfg.toString() );
      if( i < cfgsList.size() - 1 ) {
        sb.append( CH_SEPARATOR );
      }
    }
    return sb.toString();
  }

}
