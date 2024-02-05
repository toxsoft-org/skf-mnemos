package org.toxsoft.skf.mnemo.skide.tasks.upload;

import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoSharedResources.*;

import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.skf.mnemo.skide.main.*;
import org.toxsoft.skide.core.api.*;
import org.toxsoft.skide.core.api.tasks.*;
import org.toxsoft.skide.plugin.exconn.main.*;
import org.toxsoft.uskat.core.connection.*;

/**
 * SkIDE task {@link UploadToServerTaskProcessor} runner for {@link SkideUnitMnemo}.
 *
 * @author hazard157
 */
public class TaskMnemosUpload
    extends AbstractSkideUnitTaskSync {

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskMnemosUpload( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, UploadToServerTaskProcessor.INSTANCE.taskInfo(), IStridablesList.EMPTY );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  // ------------------------------------------------------------------------------------
  // AbstractSkideUnitTaskSync
  //

  @SuppressWarnings( "boxing" )
  @Override
  protected void doRunSync( ITsContextRo aInput, ITsContext aOutput ) {
    ILongOpProgressCallback lop = REFDEF_IN_PROGRESS_MONITOR.getRef( aInput );
    ISkMnemosService srcMnemosService = coreApi().getService( ISkMnemosService.SERVICE_ID );
    ISkConnection destConn = UploadToServerTaskProcessor.REFDEF_IN_OPEN_SK_CONN.getRef( aInput );
    ISkMnemosService destMnemosService = destConn.coreApi().getService( ISkMnemosService.SERVICE_ID );
    IStridablesList<ISkMnemoCfg> srcMnemosList = srcMnemosService.listMnemosCfgs();
    for( ISkMnemoCfg srcMnemo : srcMnemosList ) {
      ISkMnemoCfg destMnemo = destMnemosService.findMnemo( srcMnemo.strid() );
      if( destMnemo != null ) {
        destMnemo = destMnemosService.editMnemo( srcMnemo.strid(), srcMnemo.attrs() );
      }
      else {
        destMnemo = destMnemosService.createMnemo( srcMnemo.strid(), srcMnemo.attrs() );
      }
      String clob = srcMnemo.cfgData();
      destMnemo.setCfgData( clob );
    }
    ValidationResult vr = ValidationResult.info( FMT_INFO_MNEMOSCHEMES_UPLOADED, srcMnemosList.size() );
    lop.finished( vr );
    REFDEF_OUT_TASK_RESULT.setRef( aOutput, vr ); // FIXME log shows success, why?
  }

}
