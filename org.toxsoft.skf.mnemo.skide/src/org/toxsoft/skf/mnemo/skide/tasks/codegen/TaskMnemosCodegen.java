package org.toxsoft.skf.mnemo.skide.tasks.codegen;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoSharedResources.*;
import static org.toxsoft.skf.mnemo.skide.tasks.codegen.IPackageConstants.*;
import static org.toxsoft.skide.task.codegen.gen.ICodegenConstants.*;
import static org.toxsoft.skide.task.codegen.gen.impl.CodegenUtils.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.skide.main.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.impl.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.task.codegen.gen.*;
import org.toxsoft.skide.task.codegen.main.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * SkIDE task {@link SkideTaskCodegenInfo} runner for {@link SkideUnitMnemo}.
 *
 * @author hazard157
 */
public class TaskMnemosCodegen
    extends AbstractSkideUnitTaskSync {

  private static final String PREFIX_MNEMO = "MNEMOID"; //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskMnemosCodegen( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, SkideTaskCodegenInfo.INSTANCE, new StridablesList<>( OPDEF_GW_MNEMOS_INTERFACE_NAME ) );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static void writeConstants( ISkConnection aConn, IJavaConstantsInterfaceWriter aJw ) {
    ISkMnemosService mnemosService = aConn.coreApi().getService( ISkMnemosService.SERVICE_ID );
    for( ISkMnemoCfg mnemoCfg : mnemosService.listMnemosCfgs() ) {
      String cn = makeJavaConstName( PREFIX_MNEMO, mnemoCfg.id() );
      aJw.addConstString( cn, mnemoCfg.id(), mnemoCfg.nmName() );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkideUnitTaskSync
  //

  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    ICodegenEnvironment codegenEnv = REFDEF_CODEGEN_ENV.getRef( aInput );
    String interfaceName = OPDEF_GW_MNEMOS_INTERFACE_NAME.getValue( aInput.params() ).asString();
    IJavaConstantsInterfaceWriter jw = codegenEnv.createJavaInterfaceWriter( interfaceName );
    ISkConnectionSupplier cs = tsContext().get( ISkConnectionSupplier.class );
    writeConstants( cs.defConn(), jw );
    jw.writeFile();
    lop.finished( ValidationResult.info( FMT_INFO_JAVA_INTERFACE_WAS_GENERATED, interfaceName ) );
  }

}
