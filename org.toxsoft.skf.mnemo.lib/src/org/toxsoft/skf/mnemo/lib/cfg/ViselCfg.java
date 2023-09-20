package org.toxsoft.skf.mnemo.lib.cfg;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * {@link IViselCfg} implementation.
 *
 * @author hazard157
 */
public final class ViselCfg
    extends StridableParameterized
    implements IViselCfg {

  /**
   * The keeper singleton.
   */
  @SuppressWarnings( "hiding" )
  public static final IEntityKeeper<IViselCfg> KEEPER =
      new AbstractEntityKeeper<>( IViselCfg.class, EEncloseMode.ENCLOSES_KEEPER_IMPLEMENTATION, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IViselCfg aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.factoryId() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.params() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.propValues() );
          aSw.writeSeparatorChar();
          aSw.writeEol();

          // FIXME save IKeepablesStorage --- ViContainerData.KEEPER.write( aSw, aEntity.containerData() );

          aSw.writeEol();
        }

        @Override
        protected IViselCfg doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String factoryId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IOptionSet propValues = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IKeepablesStorage extraData = null;

          // FIXME save IKeepablesStorage --- extraData = Extra???Data.KEEPER.read( aSr );

          ViselCfg vc = new ViselCfg( id, factoryId, extraData, params );
          vc.propValues().setAll( propValues );
          return vc;
        }
      };

  private final String         factoryId;
  private final IOptionSetEdit propValues = new OptionSet();

  private KeepablesStorageAsKeepable extraData;

  /**
   * Constructor with empty extra data.
   *
   * @param aId String - VISEL identifier
   * @param aFactoryId {@link String} - the VISEL factory ID
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ViselCfg( String aId, String aFactoryId, IOptionSet aParams ) {
    this( aId, aFactoryId, null, aParams );
  }

  /**
   * Constructor.
   *
   * @param aId String - VISEL identifier
   * @param aFactoryId {@link String} - the VISEL factory ID
   * @param aExtraData {@link IKeepablesStorage} - extra data, may be <code>null</code>
   * @param aParams {@link IOptionSet} - initial values of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  // public
  ViselCfg( String aId, String aFactoryId, IKeepablesStorage aExtraData, IOptionSet aParams ) {
    super( aId, aParams );
    TsNullArgumentRtException.checkNulls( aFactoryId, aExtraData );
    factoryId = aFactoryId;
    extraData = new KeepablesStorageAsKeepable();
    /**
     * FIXME some IKeepablesStorage management is needed!!!<br>
     * copyContent() equals(), etc.
     */
    // FIXME extraData.copyContentFrom( aEstraData );
    propValues.addAll( aParams );
  }

  // ------------------------------------------------------------------------------------
  // IViselCfg
  //

  @Override
  public String factoryId() {
    return factoryId;
  }

  @Override
  public IOptionSetEdit propValues() {
    return propValues;
  }

  @Override
  public IKeepablesStorage extraData() {
    return extraData;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "%s", factoryId.toString() ); //$NON-NLS-1$
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + factoryId.hashCode();
    result = TsLibUtils.PRIME * result + propValues.hashCode();
    result = TsLibUtils.PRIME * result + extraData.hashCode();
    return result;
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof ViselCfg that ) {
      return this.factoryId.equals( that.factoryId ) && this.propValues.equals( that.propValues )
          && this.extraData.equals( that.extraData );
    }
    return false;
  }

}
