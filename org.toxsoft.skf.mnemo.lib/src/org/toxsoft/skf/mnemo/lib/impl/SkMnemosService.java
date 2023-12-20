package org.toxsoft.skf.mnemo.lib.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.skf.mnemo.lib.ISkMnemosServiceHardConstants.*;
import static org.toxsoft.skf.mnemo.lib.ISkMnemosServiceSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;

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
   * {@link ISkMnemosService#svs()} implementation.
   *
   * @author hazard157
   */
  static class ValidationSupport
      extends AbstractTsValidationSupport<ISkMnemosServiceValidator>
      implements ISkMnemosServiceValidator {

    @Override
    public ISkMnemosServiceValidator validator() {
      return this;
    }

    @Override
    public ValidationResult canCreateMnemoCfg( String aMnemoId, IOptionSet aAttrs ) {
      TsNullArgumentRtException.checkNulls( aMnemoId, aAttrs );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkMnemosServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canCreateMnemoCfg( aMnemoId, aAttrs ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canEditMnemoCfg( String aMnemoId, IOptionSet aAttrs ) {
      TsNullArgumentRtException.checkNulls( aMnemoId, aAttrs );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkMnemosServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canEditMnemoCfg( aMnemoId, aAttrs ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canSetMnemoData( String aMnemoId, String aData, ISkMnemoCfg aMnemoCfg ) {
      TsNullArgumentRtException.checkNulls( aMnemoId, aData, aMnemoCfg );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkMnemosServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canSetMnemoData( aMnemoId, aData, aMnemoCfg ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveMnemoCfg( String aMnemoId ) {
      TsNullArgumentRtException.checkNulls( aMnemoId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkMnemosServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveMnemoCfg( aMnemoId ) );
      }
      return vr;
    }

  }

  /**
   * {@link ISkMnemosService#eventer()} implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<ISkMnemosServiceListener> {

    private ECrudOp op      = null;
    private String  mnemoId = null;

    @Override
    protected void doClearPendingEvents() {
      op = null;
      mnemoId = null;
    }

    @Override
    protected void doFirePendingEvents() {
      if( op != null ) {
        reallyFireConfigEvent( op, mnemoId );
      }
    }

    @Override
    protected boolean doIsPendingEvents() {
      return op != null;
    }

    private void reallyFireConfigEvent( ECrudOp aOp, String aConfigId ) {
      for( ISkMnemosServiceListener l : listeners() ) {
        try {
          l.onMnemoCfgChanged( coreApi(), aOp, aConfigId );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

    void fireConfigChanged( ECrudOp aOp, String aConfigId ) {
      if( isFiringPaused() ) {
        if( op == null ) { // first call
          op = aOp;
          mnemoId = aConfigId;
        }
        else { // second and subsequent calls
          op = ECrudOp.LIST;
          mnemoId = null;
        }
        return;
      }
      reallyFireConfigEvent( aOp, aConfigId );
    }

  }

  private final ISkMnemosServiceValidator builtinValidator = new ISkMnemosServiceValidator() {

    @Override
    public ValidationResult canCreateMnemoCfg( String aMnemoId, IOptionSet aAttrs ) {
      // error if mnemo with same ID already exists
      SkMnemoCfg mnemo = findMnemo( aMnemoId );
      if( mnemo != null ) {
        return ValidationResult.error( FMT_ERR_MNEMO_EXISTS, aMnemoId );
      }
      // warn for empty/default name
      String name = aAttrs.getStr( AID_NAME, null );
      if( name == null || name.isBlank() || name.equals( DEFAULT_NAME ) ) {
        return ValidationResult.warn( MSG_WARN_NAME_NOT_SET );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canEditMnemoCfg( String aMnemoId, IOptionSet aAttrs ) {
      // error if mnemo with asked ID not exists
      SkMnemoCfg mnemo = findMnemo( aMnemoId );
      if( mnemo == null ) {
        return ValidationResult.error( FMT_ERR_MNEMO_NOT_EXISTS, aMnemoId );
      }
      // warn for empty/default name
      String name = aAttrs.getStr( AID_NAME, null );
      if( name == null ) {
        name = mnemo.nmName();
      }
      if( name.isBlank() || name.equals( DEFAULT_NAME ) ) {
        return ValidationResult.warn( MSG_WARN_NAME_NOT_SET );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canSetMnemoData( String aMnemoId, String aData, ISkMnemoCfg aMnemoCfg ) {
      // error if mnemo with asked ID not exists
      SkMnemoCfg mnemo = findMnemo( aMnemoId );
      if( mnemo == null ) {
        return ValidationResult.error( FMT_ERR_MNEMO_NOT_EXISTS, aMnemoId );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveMnemoCfg( String aMnemoId ) {
      // warn if mnemo with asked ID not exists
      SkMnemoCfg mnemo = findMnemo( aMnemoId );
      if( mnemo == null ) {
        return ValidationResult.error( FMT_WARN_MNEMO_NOT_EXISTS, aMnemoId );
      }
      return ValidationResult.SUCCESS;
    }

  };

  private final ValidationSupport          svs               = new ValidationSupport();
  private final Eventer                    eventer           = new Eventer();
  private final ClassClaimingCoreValidator claimingValidator = new ClassClaimingCoreValidator();

  /**
   * Constructor.
   *
   * @param aCoreApi {@link IDevCoreApi} - owner core API implementation
   */
  public SkMnemosService( IDevCoreApi aCoreApi ) {
    super( SERVICE_ID, aCoreApi );
    svs.addValidator( builtinValidator );
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkService
  //

  @Override
  protected void doInit( ITsContextRo aArgs ) {
    // create class for ISkMnemoCfg
    IDtoClassInfo mnemoCinf = internalCreateMnemoCfgClassDto();
    sysdescr().defineClass( mnemoCinf );
    objServ().registerObjectCreator( ISkMnemosServiceHardConstants.CLSID_MNEMO_CFG, SkMnemoCfg.CREATOR );
    // claim on self classes
    sysdescr().svs().addValidator( claimingValidator );
    objServ().svs().addValidator( claimingValidator );
    linkService().svs().addValidator( claimingValidator );
    clobService().svs().addValidator( claimingValidator );
    // listen to the mnemo content changes
    clobService().eventer().addListener( this::whenClobChanged );
  }

  @Override
  protected void doClose() {
    // nop
  }

  @Override
  protected boolean doIsClassClaimedByService( String aClassId ) {
    return switch( aClassId ) {
      case ISkMnemosServiceHardConstants.CLSID_MNEMO_CFG -> true;
      default -> false;
    };
  }

  /**
   * FIXME talk to MVK<br>
   * TODO listen to the server/backend messages to generate eventer messages
   */

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Creates DTO of {@link ISkMnemosServiceHardConstants#CLSID_MNEMO_CFG} class.
   *
   * @return {@link IDtoClassInfo} - class info
   */
  public static IDtoClassInfo internalCreateMnemoCfgClassDto() {
    DtoClassInfo cinf = new DtoClassInfo( CLSID_MNEMO_CFG, GW_ROOT_CLASS_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_MNEMOSCHEME_CLASS, //
        TSID_DESCRIPTION, STR_MNEMOSCHEME_CLASS_D //
    ) );
    OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS.setValue( cinf.params(), AV_TRUE );
    cinf.clobInfos().add( CLBINF_MNEMO_CFG_DATA );
    return cinf;
  }

  // FIXME with MVK -> move this methods to the base class
  private void pauseCoreValidation() {
    sysdescr().svs().pauseValidator( claimingValidator );
    objServ().svs().pauseValidator( claimingValidator );
    linkService().svs().pauseValidator( claimingValidator );
    clobService().svs().pauseValidator( claimingValidator );
  }

  private void resumeCoreValidation() {
    sysdescr().svs().resumeValidator( claimingValidator );
    objServ().svs().resumeValidator( claimingValidator );
    linkService().svs().resumeValidator( claimingValidator );
    clobService().svs().resumeValidator( claimingValidator );
  }

  private static Skid makeMnemoSkid( String aMnemoId ) {
    return new Skid( CLSID_MNEMO_CFG, aMnemoId );
  }

  private static Gwid makeMnemoGwid( String aMnemoId ) {
    return Gwid.createClob( CLSID_MNEMO_CFG, aMnemoId, CLBID_MNEMO_CFG_DATA );
  }

  private void whenClobChanged( ISkCoreApi aCoreApi, Gwid aClobGwid ) {
    if( aClobGwid.classId().equals( CLSID_MNEMO_CFG ) ) {
      eventer.fireConfigChanged( ECrudOp.EDIT, aClobGwid.strid() );
    }
  }

  // ------------------------------------------------------------------------------------
  // ISkMnemosService
  //

  @Override
  public SkMnemoCfg findMnemo( String aMnemoId ) {
    return coreApi().objService().find( makeMnemoSkid( aMnemoId ) );
  }

  @Override
  public SkMnemoCfg getMnemo( String aMnemoId ) {
    return coreApi().objService().get( makeMnemoSkid( aMnemoId ) );
  }

  @Override
  public IStringList listMnemosIds() {
    ISkidList skids = objServ().listSkids( CLSID_MNEMO_CFG, false );
    IStringListEdit ll = new StringArrayList( skids.size() );
    for( Skid s : skids ) {
      ll.add( s.strid() );
    }
    return ll;
  }

  @Override
  public IStridablesList<ISkMnemoCfg> listMnemosCfgs() {
    IList<ISkMnemoCfg> ll = objServ().listObjs( CLSID_MNEMO_CFG, true );
    return new StridablesList<>( ll );
  }

  @Override
  public ISkMnemoCfg createMnemo( String aMnemoId, IOptionSet aAttrs ) {
    TsValidationFailedRtException.checkError( svs.canCreateMnemoCfg( aMnemoId, aAttrs ) );
    DtoObject dto = new DtoObject( makeMnemoSkid( aMnemoId ), aAttrs, IStringMap.EMPTY );
    pauseCoreValidation();
    try {
      return objServ().defineObject( dto );
      // TODO generate event
    }
    finally {
      resumeCoreValidation();
    }
  }

  @Override
  public ISkMnemoCfg editMnemo( String aMnemoId, IOptionSet aAttrs ) {
    TsValidationFailedRtException.checkError( svs.canEditMnemoCfg( aMnemoId, aAttrs ) );
    DtoObject dto = DtoObject.createDtoObject( makeMnemoSkid( aMnemoId ), coreApi() );
    dto.attrs().addAll( aAttrs );
    pauseCoreValidation();
    try {
      return objServ().defineObject( dto );
      // TODO generate event
    }
    finally {
      resumeCoreValidation();
    }
  }

  @Override
  public void setMnemoData( String aMnemoId, String aData ) {
    TsNullArgumentRtException.checkNulls( aMnemoId, aData );
    SkMnemoCfg mnemo = getMnemo( aMnemoId );
    TsValidationFailedRtException.checkError( svs.canSetMnemoData( aMnemoId, aData, mnemo ) );
    pauseCoreValidation();
    try {
      clobService().writeClob( makeMnemoGwid( aMnemoId ), aData );
      // TODO generate event
    }
    finally {
      resumeCoreValidation();
    }
  }

  @Override
  public String getMnemoData( String aMnemoId ) {
    getMnemo( aMnemoId ); // checks for existence
    return clobService().readClob( makeMnemoGwid( aMnemoId ) );
  }

  @Override
  public void removeMnemo( String aMnemoId ) {
    TsValidationFailedRtException.checkError( svs.canRemoveMnemoCfg( aMnemoId ) );
    pauseCoreValidation();
    try {
      objServ().removeObject( makeMnemoSkid( aMnemoId ) );
      // TODO generate event
    }
    finally {
      resumeCoreValidation();
    }
  }

  @Override
  public ITsValidationSupport<ISkMnemosServiceValidator> svs() {
    return svs;
  }

  @Override
  public ITsEventer<ISkMnemosServiceListener> eventer() {
    return eventer;
  }

}
