package org.toxsoft.skf.mnemo.skide.tasks.upload;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.gentask.IGenericTaskConstants.*;
import static org.toxsoft.skf.mnemo.skide.ISkidePluginMnemoSharedResources.*;
import static org.toxsoft.skide.core.ISkideCoreConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
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

  static final String OPID_CLEAR_MNEMOS_BEFORE_UPLOAD = SKIDE_FULL_ID + ".ClearMnemosBeforeUpload"; //$NON-NLS-1$

  static final IDataDef OPDEF_CLEAR_MNEMOS_BEFORE_UPLOAD = DataDef.create( OPID_CLEAR_MNEMOS_BEFORE_UPLOAD, BOOLEAN, //
      TSID_NAME, STR_CLEAR_MNEMOS_BEFORE_UPLOAD, //
      TSID_DESCRIPTION, STR_CLEAR_MNEMOS_BEFORE_UPLOAD_D, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Constructor.
   *
   * @param aOwnerUnit {@link AbstractSkideUnit} - the owner unit
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TaskMnemosUpload( AbstractSkideUnit aOwnerUnit ) {
    super( aOwnerUnit, UploadToServerTaskProcessor.INSTANCE.taskInfo(),
        // configuration options
        new StridablesList<>( //
            OPDEF_CLEAR_MNEMOS_BEFORE_UPLOAD //
        ) );
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
    // if configured, remove all mnemos from server
    if( OPDEF_CLEAR_MNEMOS_BEFORE_UPLOAD.getValue( getCfgOptionValues() ).asBool() ) {
      IStringList mnemoIds = destMnemosService.listMnemosIds();
      for( String mid : mnemoIds ) {
        destMnemosService.removeMnemo( mid );
      }
    }
    // upload mnemos from SkIDE
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
