package org.toxsoft.skf.mnemo.skide.e4.services;

import static org.toxsoft.skf.mnemo.skide.e4.services.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.skide.e4.uiparts.*;
import org.toxsoft.skf.mnemo.skide.glib.*;

/**
 * Correct {@link ISkMnemoCfg} instance to avoid exceptions during development.
 * <p>
 * Mnemoscheme configuration, when loaded by {@link IMnemoEditorPanel#setCurrentConfig(IVedScreenCfg)} is strictly
 * checked for correctness. However,, during development process, both VED items (actors and VISELs) may be intensively
 * changed. Without correction, mismatch between saved mnemo configuration and changed VED items properties makes it
 * impossible to load menemoscheme. This corrector is used by {@link UipartSkMnemoEditor} to remove mismatches before
 * editing saved mnemocheme.
 *
 * @author hazard157
 */
public class MnemoCfgCorrector
    implements ITsGuiContextable {

  private final ITsGuiContext tsContext;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public MnemoCfgCorrector( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Corrects the VED screen input configuration so that the output does not throw an exception when editing begins.
   *
   * @param aCfg {@link IVedScreenCfg} - input configuration
   * @param aVrl {@link ValResList} - list of corrections as a warning/error messages to be accepted by the user
   * @return {@link IVedScreenCfg} - output (corrected) configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IVedScreenCfg correctMnemoConfig( IVedScreenCfg aCfg, ValResList aVrl ) {
    TsNullArgumentRtException.checkNulls( aCfg, aVrl );
    IVedActorFactoriesRegistry afReg = tsContext.get( IVedActorFactoriesRegistry.class );
    IVedViselFactoriesRegistry vfReg = tsContext.get( IVedViselFactoriesRegistry.class );
    // canvas configuration and extra data are not cirrected, they are copied "as is"
    VedScreenCfg outCfg = new VedScreenCfg();
    outCfg.canvasCfg().copyFrom( aCfg.canvasCfg() );
    outCfg.extraData().copyFrom( aCfg.extraData() );
    // VISELs
    for( IVedItemCfg cIn : aCfg.viselCfgs() ) {
      // check factory is known
      IVedViselFactory vFac = vfReg.find( cIn.factoryId() );
      if( vFac == null ) {
        aVrl.add( ValidationResult.error( FMT_ERR_NO_VISEL_FACTORY, cIn.id(), cIn.factoryId() ) );
        continue; // remove VISEL from output
      }
      VedItemCfg cOut = new VedItemCfg( cIn.id(), cIn.kind(), cIn.factoryId(), cIn.params() );
      cOut.extraData().copyFrom( cIn.extraData() );
      // iterate over properties in configuration
      for( String propId : cIn.propValues().keys() ) {
        // check if property is in config but not in item properties
        IDataDef propDef = vFac.propDefs().findByKey( propId );
        if( propDef == null ) {
          aVrl.add( ValidationResult.error( FMT_ERR_VISEL_PROP_IN_CFG_BUT_NOT_FACTORY, cIn.id(), propId ) );
          continue; // remove property value from output
        }
        // check if property atomic type is same in config and factory declaration
        IAtomicValue propValue = cIn.propValues().getByKey( propId );
        if( !AvTypeCastRtException.canAssign( propDef.atomicType(), propValue.atomicType() ) ) { // mismatch!
          aVrl.add( ValidationResult.error( FMT_ERR_VISEL_PROP_INV_ATOMIC_TYPE, cIn.id(), propId ) );
          cOut.propValues().setValueIfNull( propId, propDef.defaultValue() ); // use correct type default value
        }
        else {
          cOut.propValues().setValueIfNull( propId, propValue ); // use value from config
        }
      }
      // iterate over properties in factory declaration
      for( String propId : vFac.propDefs().keys() ) {
        // add value of property in factory but not in config
        if( !cOut.propValues().hasKey( propId ) ) {
          IDataDef propDef = vFac.propDefs().findByKey( propId );
          cOut.propValues().setValueIfNull( propId, propDef.defaultValue() );
        }
      }
      outCfg.viselCfgs().add( cOut );
    }
    // ACTORs
    for( IVedItemCfg cIn : aCfg.actorCfgs() ) {
      // check factory is known
      IVedActorFactory vFac = afReg.find( cIn.factoryId() );
      if( vFac == null ) {
        aVrl.add( ValidationResult.error( FMT_ERR_NO_ACTOR_FACTORY, cIn.id(), cIn.factoryId() ) );
        continue; // remove ACTOR from output
      }
      VedItemCfg cOut = new VedItemCfg( cIn.id(), cIn.kind(), cIn.factoryId(), cIn.params() );
      cOut.extraData().copyFrom( cIn.extraData() );
      // iterate over properties in configuration
      for( String propId : cIn.propValues().keys() ) {
        // check if property is in config but not in item properties
        IDataDef propDef = vFac.propDefs().findByKey( propId );
        if( propDef == null ) {
          aVrl.add( ValidationResult.error( FMT_ERR_ACTOR_PROP_IN_CFG_BUT_NOT_FACTORY, cIn.id(), propId ) );
          continue; // remove property value from output
        }
        // check if property atomic type is same in config and factory declaration
        IAtomicValue propValue = cIn.propValues().getByKey( propId );
        if( !AvTypeCastRtException.canAssign( propDef.atomicType(), propValue.atomicType() ) ) { // mismatch!
          aVrl.add( ValidationResult.error( FMT_ERR_ACTOR_PROP_INV_ATOMIC_TYPE, cIn.id(), propId ) );
          cOut.propValues().setValueIfNull( propId, propDef.defaultValue() ); // use correct type default value
        }
        else {
          cOut.propValues().setValueIfNull( propId, propValue ); // use value from config
        }
      }
      // iterate over properties in factory declaration
      for( String propId : vFac.propDefs().keys() ) {
        // add value of property in factory but not in config
        if( !cOut.propValues().hasKey( propId ) ) {
          IDataDef propDef = vFac.propDefs().findByKey( propId );
          cOut.propValues().setValueIfNull( propId, propDef.defaultValue() );
        }
      }
      outCfg.actorCfgs().add( cOut );
    }
    return outCfg;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
