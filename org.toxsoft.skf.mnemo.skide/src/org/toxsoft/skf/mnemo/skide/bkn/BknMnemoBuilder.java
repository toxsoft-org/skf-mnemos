package org.toxsoft.skf.mnemo.skide.bkn;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.ext.mastobj.gui.main.IMnemoMasterObjectConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.editor.palette.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.ext.mastobj.gui.main.resolver.*;
import org.toxsoft.skf.ext.mastobj.gui.skved.*;
import org.toxsoft.skf.ext.mastobj.gui.skved.recognizers.*;
import org.toxsoft.skf.ext.mastobj.gui.skved.recognizers.ByAttrValueRecognizer;
import org.toxsoft.skf.ext.mastobj.gui.skved.resolvers.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.connection.*;

public class BknMnemoBuilder {

  private static final String CLS_ANALOG_INPUT      = "AnalogInput";
  private static final String CLS_AIR_COMPRESSOR    = "AirCompressor";
  private static final String CLS_REVERSIBLE_ENGINE = "ReversibleEngine";
  private static final String ATTRID_MO_RESOLVER    = "atrMasterObjResolver";

  IVedScreen vedScreen;

  IVedViselFactory labelFactory;
  IVedViselFactory imageFactory;
  IVedViselFactory checkboxFactory;

  ISkConnection skConn;

  ISkMnemosService mnemoService;

  String mnemoId;
  String mnemoName;
  String mnemoDescr;

  Image bkImage = null;

  TsImageDescriptor bkImageDescriptor;

  String svgFilePath;
  String svgValvesPath;

  String masterClass;

  TsImageDescriptor imdHorValve;
  TsImageDescriptor imdVertValve;

  public BknMnemoBuilder( IVedScreen aVedScreen ) {
    vedScreen = aVedScreen;
    ISkVedEnvironment vedEnv = vedScreen.tsContext().get( ISkVedEnvironment.class );
    skConn = vedEnv.skConn();
    mnemoService = skConn.coreApi().getService( ISkMnemosService.SERVICE_ID );

    IVedViselFactoriesRegistry registy = vedScreen.tsContext().get( IVedViselFactoriesRegistry.class );
    labelFactory = registy.get( ViselLabel.FACTORY_ID );
    imageFactory = registy.get( ViselImage.FACTORY_ID );
    checkboxFactory = registy.get( ViselCheckbox.FACTORY_ID );

    String pluginId = "ru.toxsoft.bkn.gui";
    imdVertValve = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_white.png" );
    imdHorValve = TsImageSourceKindPlugin.createImageDescriptor( pluginId, "images/valve/valve_white_hor.png" );
  }

  void setMnemoProps( String aId, String aName, String aDescription ) {
    mnemoId = aId;
    mnemoName = aName;
    mnemoDescr = aDescription;
  }

  void setBkImage( String aPluginId, String aImagePath ) {
    bkImageDescriptor = TsImageSourceKindPlugin.createImageDescriptor( aPluginId, aImagePath );
    if( bkImage != null && !bkImage.isDisposed() ) {
      bkImage.dispose();
    }
    bkImage = vedScreen.imageManager().getImage( bkImageDescriptor ).image();
  }

  void setSvgFilePath( String aFilePath ) {
    svgFilePath = aFilePath;
  }

  void setSvgValvesPath( String aFilePath ) {
    svgValvesPath = aFilePath;
  }

  void setMasterObjectClass( String aClassId ) {
    masterClass = aClassId;
  }

  private Pair<ISkMnemoCfg, VedScreenCfg> prepareCanvas() {
    VedScreenCfg screenCfg = null;
    ISkMnemoCfg mnemoCfg = null;
    mnemoCfg = mnemoService.findMnemo( mnemoId );
    if( mnemoCfg == null ) {
      IOptionSetEdit opSet = new OptionSet();
      opSet.setStr( TSID_NAME, mnemoName );
      opSet.setStr( TSID_DESCRIPTION, mnemoDescr );
      mnemoCfg = mnemoService.createMnemo( mnemoId, opSet );
      screenCfg = new VedScreenCfg();
    }
    if( mnemoCfg == null ) { // если не получилось - выйдем
      return null;
    }

    String cfgData = mnemoCfg.cfgData();
    if( screenCfg == null ) {
      screenCfg = (VedScreenCfg)VedScreenCfg.KEEPER.str2ent( cfgData );
    }
    VedCanvasCfg canvasCfg = screenCfg.canvasCfg();
    int w = bkImage.getImageData().width;
    int h = bkImage.getImageData().height;
    TsImageFillInfo ifi = new TsImageFillInfo( bkImageDescriptor, EImageFillKind.CENTER );
    TsFillInfo fi = new TsFillInfo( ifi );
    canvasCfg.params().setValobj( "Size", new D2Point( w, h ) ); //$NON-NLS-1$
    canvasCfg.params().setValobj( "FillInfo", fi ); //$NON-NLS-1$

    vedScreen.model().visels().clear();
    vedScreen.model().actors().clear();
    return new Pair<>( mnemoCfg, screenCfg );
  }

  void build( IStringList aDataNames, IStringList aValveNames ) {
    // VedScreenCfg screenCfg = null;
    // ISkMnemoCfg mnemoCfg = null;
    // mnemoCfg = mnemoService.findMnemo( mnemoId );
    // if( mnemoCfg == null ) {
    // IOptionSetEdit opSet = new OptionSet();
    // opSet.setStr( TSID_NAME, mnemoName );
    // opSet.setStr( TSID_DESCRIPTION, mnemoDescr );
    // mnemoCfg = mnemoService.createMnemo( mnemoId, opSet );
    // screenCfg = new VedScreenCfg();
    // }
    // if( mnemoCfg == null ) { // если не получилось - выйдем
    // return;
    // }
    //
    // String cfgData = mnemoCfg.cfgData();
    // if( screenCfg == null ) {
    // screenCfg = (VedScreenCfg)VedScreenCfg.KEEPER.str2ent( cfgData );
    // }
    // VedCanvasCfg canvasCfg = screenCfg.canvasCfg();
    // int w = bkImage.getImageData().width;
    // int h = bkImage.getImageData().height;
    // TsImageFillInfo ifi = new TsImageFillInfo( bkImageDescriptor, EImageFillKind.CENTER );
    // TsFillInfo fi = new TsFillInfo( ifi );
    // canvasCfg.params().setValobj( "Size", new D2Point( w, h ) );
    // canvasCfg.params().setValobj( "FillInfo", fi );
    //
    // vedScreen.model().visels().clear();
    // vedScreen.model().actors().clear();

    Pair<ISkMnemoCfg, VedScreenCfg> result = prepareCanvas();
    if( result == null ) {
      return; // что-то не срослось - выйдем
    }
    ISkMnemoCfg mnemoCfg = result.left();
    VedScreenCfg screenCfg = result.right();

    if( svgFilePath != null && !svgFilePath.isBlank() ) {
      SvgHelper svgHelper = new SvgHelper( svgFilePath );
      svgHelper.parse( vedScreen.getDisplay() );
      IList<Rectangle> rects = svgHelper.getRects();
      int num = 1;
      VedAbstractVisel label = null;
      for( Rectangle r : rects ) {
        if( aDataNames != null ) {
          String dataName = aDataNames.get( num - 1 );
          label = createLabel( r, num, dataName, null );
          if( masterClass != null && !masterClass.isBlank() ) {
            createAnalogInput( label, dataName, null );
          }
          else {
            Skid skid = new Skid( CLS_ANALOG_INPUT, dataName );
            Ugwi ugwi = UgwiKindSkRtdata.makeUgwi( skid, "rtdCV" );
            createAnalogInput( label, dataName, ugwi );
          }
          VedAbstractVisel checkbox = createCheckBox( r, num, dataName, 103, 1 );
          createCheckBoxActor( checkbox, dataName, null );
          createCmdCheckBoxActor( checkbox, dataName, null );
        }
        else {
          createLabel( r, num, "" + num, null );
        }
        num++;
      }
      svgHelper.dispose();
    }

    if( svgValvesPath != null && !svgValvesPath.isBlank() ) {
      SvgHelper svgHelper = new SvgHelper( svgValvesPath );
      svgHelper.parse( vedScreen.getDisplay() );
      IList<Rectangle> rects = svgHelper.getRects();
      int num = 1;
      VedAbstractVisel label = null;
      TsFillInfo fillInfo = new TsFillInfo( new RGBA( 0, 0, 200, 255 ) );
      for( Rectangle r : rects ) {
        if( aValveNames != null ) {
          String dataName = aValveNames.get( num - 1 );
          createReversibleEngine( r, dataName );
          // label = createLabel( r, num, dataName );
          // IVedActor a = createAnalogInput( label, dataName );
        }
        else {
          createLabel( r, num, "Valve" + num, fillInfo ); //$NON-NLS-1$
        }
        num++;
      }
      svgHelper.dispose();
      // if( label != null ) {
      // IVedActor a = createAnalogInput( label, "TP1" );
      // }
    }

    if( masterClass != null && !masterClass.isBlank() ) {
      MasterObjectUtils.setMainMasterClassId( masterClass, vedScreen );
    }

    mnemoCfg.setCfgData( VedScreenCfg.KEEPER.ent2str( screenCfg ) );
    mnemoService.editMnemo( mnemoId, mnemoCfg.attrs() );
  }

  void buildTableMnemo( IStringList aDataNames ) {

    Pair<ISkMnemoCfg, VedScreenCfg> result = prepareCanvas();
    if( result == null ) {
      return; // что-то не срослось - выйдем
    }
    ISkMnemoCfg mnemoCfg = result.left();
    VedScreenCfg screenCfg = result.right();

    if( svgFilePath != null && !svgFilePath.isBlank() ) {
      SvgHelper svgHelper = new SvgHelper( svgFilePath );
      svgHelper.parse( vedScreen.getDisplay() );
      IList<Rectangle> rects = svgHelper.getRects();
      int num = 1;
      VedAbstractVisel label = null;
      for( Rectangle r : rects ) {
        if( aDataNames != null ) {
          for( int i = 1; i < 4; i++ ) {
            String dataName = "TKA" + i + "_" + aDataNames.get( num - 1 );
            label = createLabel( r, num, dataName, null );

            Skid skid = new Skid( CLS_ANALOG_INPUT, dataName );
            Ugwi ugwi = UgwiKindSkRtdata.makeUgwi( skid, "rtdCV" );
            createAnalogInput( label, dataName, ugwi );

            //
            VedAbstractVisel checkbox = createCheckBox( r, num, dataName, -22, 1 );
            ugwi = UgwiKindSkRtdata.makeUgwi( skid, "rtdEnable" );
            createCheckBoxActor( checkbox, dataName, ugwi );
            ugwi = UgwiKindSkCmd.makeUgwi( skid, "cmdEnable" );
            createCmdCheckBoxActor( checkbox, dataName, ugwi );
            // if( num > 3 ) {
            // return;
            // }
            r.x += 80;
            if( r.x > 600 ) {
              r.x += 7;
            }
          }
        }
        else {
          createLabel( r, num, "" + num, null );
        }
        num++;
      }
      svgHelper.dispose();
    }

    mnemoCfg.setCfgData( VedScreenCfg.KEEPER.ent2str( screenCfg ) );
    mnemoService.editMnemo( mnemoId, mnemoCfg.attrs() );
  }

  VedAbstractVisel createLabel( Rectangle r, int aNum, String aDataName, TsFillInfo aFillInfo ) {
    IVedItemsPaletteEntry pe = labelFactory.paletteEntries().first();
    IVedItemCfg cfg = pe.itemCfg();
    VedItemCfg labelCfg = vedScreen.model().visels().prepareFromTemplate( cfg );
    String labelId = "label" + aDataName; //$NON-NLS-1$
    labelCfg = new VedItemCfg( labelId, labelCfg );
    labelCfg.propValues().setStr( DDEF_NAME, aDataName );
    labelCfg.propValues().setDouble( PROP_X, r.x );
    labelCfg.propValues().setDouble( PROP_Y, r.y );
    labelCfg.propValues().setDouble( PROP_WIDTH, r.width );
    labelCfg.propValues().setDouble( PROP_HEIGHT, r.height );
    if( aFillInfo != null ) {
      labelCfg.propValues().setValobj( PROP_BK_FILL, aFillInfo );
    }
    labelCfg.propValues().setStr( PROP_TEXT, "" + aNum ); //$NON-NLS-1$

    return vedScreen.model().visels().create( labelCfg );
  }

  // ------------------------------------------------------------------------------------
  // Utils
  //

  private static ISkoRecognizerCfg getRecognizerCfg( IDtoAttrInfo aAttrInfo, String aAttrValue ) {
    if( aAttrInfo != null && aAttrValue != null ) {
      IOptionSetEdit opSet = new OptionSet();
      opSet.setStr( ByAttrValueRecognizer.PROPID_ATTRID, aAttrInfo.id() );
      opSet.setStr( ByAttrValueRecognizer.PROPID_ATTR_VALUE, aAttrValue );
      String idPath = StridUtils.makeIdPath( CLS_ANALOG_INPUT, aAttrInfo.id() );
      idPath = StridUtils.makeIdPath( idPath, aAttrValue );
      return new SkoRecognizerCfg( idPath, ESkoRecognizerKind.ATTR, opSet );
    }
    return null;
  }

  private SimpleResolverCfg getLinkResolverConfig( String aAttrValue, String aRtDataId ) {
    ISkCoreApi coreApi = skConn.coreApi();
    ISkClassInfo clsInfo = coreApi.sysdescr().findClassInfo( CLS_ANALOG_INPUT );
    if( clsInfo != null ) {
      if( clsInfo.attrs().list().hasKey( ATTRID_MO_RESOLVER ) ) {
        IDtoAttrInfo resolverInfo = clsInfo.attrs().list().getByKey( ATTRID_MO_RESOLVER );
        ISkoRecognizerCfg rCfg = getRecognizerCfg( resolverInfo, aAttrValue );

        ISkClassInfo compClsInfo = coreApi.sysdescr().findClassInfo( CLS_AIR_COMPRESSOR );
        IDtoLinkInfo li = compClsInfo.links().list().findByKey( "lnkAnalogInputs" ); //$NON-NLS-1$
        Ugwi ugwi = UgwiKindSkLinkInfo.makeUgwi( CLS_AIR_COMPRESSOR, li.id() );
        IOptionSetEdit opSet = new OptionSet();
        opSet.setValobj( PROP_UGWI, ugwi );
        opSet.setValobj( LinkInfoResolver.PROPID_RECOGNIZER_CFG, rCfg );
        opSet.setStr( MasterObjectUtils.PROPID_RESOLVER_OUTPUT_CLASS_ID, CLS_ANALOG_INPUT );
        SimpleResolverCfg lnkResCfg = new SimpleResolverCfg( LinkInfoResolver.FACTORY_ID, opSet );
        return lnkResCfg;
      }
    }
    return null;
  }

  private CompoundResolverConfig getResolverConfig( String aAttrValue, String aRtDataId ) {
    // ISkCoreApi coreApi = skConn.coreApi();
    // ISkClassInfo clsInfo = coreApi.sysdescr().findClassInfo( CLS_ANALOG_INPUT );
    // if( clsInfo != null ) {
    // if( clsInfo.attrs().list().hasKey( ATTRID_MO_RESOLVER ) ) {
    // IDtoAttrInfo resolverInfo = clsInfo.attrs().list().getByKey( ATTRID_MO_RESOLVER );
    // ISkoRecognizerCfg rCfg = getRecognizerCfg( resolverInfo, aAttrValue );
    //
    // ISkClassInfo compClsInfo = coreApi.sysdescr().findClassInfo( CLS_AIR_COMPRESSOR );
    // IDtoLinkInfo li = compClsInfo.links().list().findByKey( "lnkAnalogInputs" );
    // Ugwi ugwi = UgwiKindSkLinkInfo.makeUgwi( CLS_AIR_COMPRESSOR, li.id() );
    // IOptionSetEdit opSet = new OptionSet();
    // opSet.setValobj( PROP_UGWI, ugwi );
    // opSet.setValobj( LinkInfoResolver.PROPID_RECOGNIZER_CFG, rCfg );
    // opSet.setStr( MasterObjectUtils.PROPID_RESOLVER_OUTPUT_CLASS_ID, CLS_ANALOG_INPUT );
    // SimpleResolverCfg lnkResCfg = new SimpleResolverCfg( LinkInfoResolver.FACTORY_ID, opSet );
    //
    // SimpleResolverCfg rtdCfg = DirectRtDataResolver.createResolverConfig( CLS_ANALOG_INPUT, aRtDataId );
    //
    // IListEdit<SimpleResolverCfg> configs = new ElemArrayList<>( lnkResCfg );
    // configs.add( rtdCfg );
    //
    // CompoundResolverConfig resCfg = new CompoundResolverConfig( configs );
    // return resCfg;
    // }
    // }

    SimpleResolverCfg lnkResCfg = getLinkResolverConfig( aAttrValue, aRtDataId );
    if( lnkResCfg != null ) {
      SimpleResolverCfg rtdCfg = DirectRtDataResolver.createResolverConfig( CLS_ANALOG_INPUT, aRtDataId );

      IListEdit<SimpleResolverCfg> configs = new ElemArrayList<>( lnkResCfg );
      configs.add( rtdCfg );

      CompoundResolverConfig resCfg = new CompoundResolverConfig( configs );
      return resCfg;
    }

    return null;
  }

  private CompoundResolverConfig getCmdResolverConfig( String aAttrValue, String aRtDataId ) {
    SimpleResolverCfg lnkResCfg = getLinkResolverConfig( aAttrValue, aRtDataId );
    if( lnkResCfg != null ) {
      SimpleResolverCfg rtdCfg = DirectCmdResolver.createResolverConfig( CLS_ANALOG_INPUT, aRtDataId );

      IListEdit<SimpleResolverCfg> configs = new ElemArrayList<>( lnkResCfg );
      configs.add( rtdCfg );

      CompoundResolverConfig resCfg = new CompoundResolverConfig( configs );
      return resCfg;
    }
    return null;
  }

  private CompoundResolverConfig getRtDataResolverConfig( Skid aObjSkid, String aDataId ) {
    SimpleResolverCfg simpleCfg;
    simpleCfg = DirectRtDataResolver.createResolverConfig( UgwiKindSkRtdata.makeUgwi( aObjSkid, aDataId ) );
    return new CompoundResolverConfig( new ElemArrayList<>( simpleCfg ) );
  }

  private VedAbstractActor createAnalogInput( VedAbstractVisel aVisel, String aAttrValue, Ugwi aRtdUgwi ) {
    IOptionSetEdit params = new OptionSet();

    String id = StridUtils.makeIdPath( "rtd", aAttrValue ); //$NON-NLS-1$
    VedItemCfg cfg = new VedItemCfg( id, EVedItemKind.ACTOR, SkActorRtdataText.FACTORY.id(), params );
    VedAbstractActor a = vedScreen.model().actors().create( cfg );
    a.props().setStr( TSID_NAME, "данное " + aAttrValue );
    a.props().setStr( PROPID_VISEL_ID, aVisel.id() );
    a.props().setStr( PROPID_VISEL_PROP_ID, PROPID_TEXT );
    a.props().setStr( PROPID_FORMAT_STRING, "%.2f" ); //$NON-NLS-1$

    String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
    IStringMapEdit<ICompoundResolverConfig> resolvers = new StringMap<>();
    if( aRtdUgwi == null ) {
      CompoundResolverConfig resCfg = getResolverConfig( aAttrValue, "rtdCV" );
      resolvers.put( PROPID_RTD_UGWI, resCfg );
      a.extraData().writeStridMap( sectionId, resolvers, CompoundResolverConfig.KEEPER );
      aVisel.props().setStr( PROPID_TEXT, aAttrValue );
    }
    else {
      a.props().setValobj( PROPID_RTD_UGWI, aRtdUgwi );
    }

    id = StridUtils.makeIdPath( "bkColor", aAttrValue ); //$NON-NLS-1$
    cfg = new VedItemCfg( id, EVedItemKind.ACTOR, SkActorRtdataRefbook.FACTORY.id(), params );
    a = vedScreen.model().actors().create( cfg );
    a.props().setStr( TSID_NAME, "фон " + aAttrValue );
    a.props().setStr( PROPID_VISEL_ID, aVisel.id() );
    StringArrayList branchList = new StringArrayList( "aiDecorator", "key", "bkColor", "bkFill" );
    IdChain idChain = new IdChain( branchList );
    a.props().setValobj( SkActorRtdataRefbook.REFBOOK_INFO_ID, idChain );
    if( aRtdUgwi != null ) {
      Skid skid = UgwiKindSkRtdata.INSTANCE.getGwid( aRtdUgwi ).skid();
      Ugwi ugwi = UgwiKindSkRtdata.makeUgwi( skid, "rtdVisualMode" );
      a.props().setValobj( PROPID_RTD_UGWI, ugwi );
    }
    else {
      CompoundResolverConfig resCfg = getResolverConfig( aAttrValue, "rtdVisualMode" );
      resolvers.put( PROPID_RTD_UGWI, resCfg );
      a.extraData().writeStridMap( sectionId, resolvers, CompoundResolverConfig.KEEPER );
    }

    id = StridUtils.makeIdPath( "textColor", aAttrValue ); //$NON-NLS-1$
    cfg = new VedItemCfg( id, EVedItemKind.ACTOR, SkActorRtdataRefbook.FACTORY.id(), params );
    a = vedScreen.model().actors().create( cfg );
    a.props().setStr( TSID_NAME, "цвет текста " + aAttrValue );
    a.props().setStr( PROPID_VISEL_ID, aVisel.id() );
    branchList = new StringArrayList( "aiDecorator", "key", "textColor", "fgColor" );
    idChain = new IdChain( branchList );
    a.props().setValobj( SkActorRtdataRefbook.REFBOOK_INFO_ID, idChain );
    if( aRtdUgwi != null ) {
      Skid skid = UgwiKindSkRtdata.INSTANCE.getGwid( aRtdUgwi ).skid();
      Ugwi ugwi = UgwiKindSkRtdata.makeUgwi( skid, "rtdVisualMode" );
      a.props().setValobj( PROPID_RTD_UGWI, ugwi );
    }
    else {
      CompoundResolverConfig resCfg = getResolverConfig( aAttrValue, "rtdVisualMode" );
      resolvers.put( PROPID_RTD_UGWI, resCfg );
      a.extraData().writeStridMap( sectionId, resolvers, CompoundResolverConfig.KEEPER );
    }

    return a;
  }

  private VedAbstractActor createReversibleEngine( Rectangle aRect, String aObjId ) {
    ISkObject engineObj = skConn.coreApi().objService().find( new Skid( CLS_REVERSIBLE_ENGINE, aObjId ) );
    IVedItemsPaletteEntry pe = imageFactory.paletteEntries().first();
    IVedItemCfg cfg = pe.itemCfg();
    VedItemCfg valveCfg = vedScreen.model().visels().prepareFromTemplate( cfg );
    String valveId = "valve" + aObjId; //$NON-NLS-1$
    valveCfg = new VedItemCfg( valveId, valveCfg );
    valveCfg.propValues().setStr( DDEF_NAME, engineObj.nmName() );
    valveCfg.propValues().setStr( DDEF_DESCRIPTION, engineObj.description() );
    valveCfg.propValues().setDouble( PROP_X, aRect.x - 3 );
    valveCfg.propValues().setDouble( PROP_Y, aRect.y - 1 );

    boolean isHor = aRect.width > aRect.height;
    if( isHor ) {
      valveCfg.propValues().setValobj( PROP_IMAGE_DESCRIPTOR, imdHorValve );
    }
    else {
      valveCfg.propValues().setValobj( PROP_IMAGE_DESCRIPTOR, imdVertValve );
    }

    VedAbstractVisel visel = vedScreen.model().visels().create( valveCfg );

    IOptionSetEdit params = new OptionSet();
    String id = StridUtils.makeIdPath( "valve", engineObj.id() ); //$NON-NLS-1$
    cfg = new VedItemCfg( id, EVedItemKind.ACTOR, SkActorRtdataRefbook.FACTORY.id(), params );
    VedAbstractActor a = vedScreen.model().actors().create( cfg );
    a.props().setStr( TSID_NAME, engineObj.nmName() );
    a.props().setStr( TSID_DESCRIPTION, engineObj.description() );
    a.props().setStr( PROPID_VISEL_ID, visel.id() );
    Ugwi ugwi = UgwiKindSkRtdata.makeUgwi( engineObj.skid(), "rtdVisualMode" );
    a.props().setValobj( PROPID_RTD_UGWI, ugwi );
    // a.props().setStr( PROPID_VISEL_PROP_ID, PROPID_IMAGE_DESCRIPTOR );
    StringArrayList branchList;
    if( isHor ) {
      branchList = new StringArrayList( "reversibleEngineState", "key", "horImage", PROPID_IMAGE_DESCRIPTOR );
    }
    else {
      branchList = new StringArrayList( "reversibleEngineState", "key", "vertImage", PROPID_IMAGE_DESCRIPTOR );
    }
    IdChain idChain = new IdChain( branchList );
    a.props().setValobj( SkActorRtdataRefbook.REFBOOK_INFO_ID, idChain );
    return a;
  }

  VedAbstractVisel createCheckBox( Rectangle r, int aNum, String aDataName, int aDx, int aDy ) {
    IVedItemsPaletteEntry pe = checkboxFactory.paletteEntries().first();
    IVedItemCfg cfg = pe.itemCfg();
    VedItemCfg checkboxCfg = vedScreen.model().visels().prepareFromTemplate( cfg );
    String labelId = "checkbox" + aDataName; //$NON-NLS-1$
    checkboxCfg = new VedItemCfg( labelId, checkboxCfg );
    checkboxCfg.propValues().setStr( DDEF_NAME, aDataName );
    checkboxCfg.propValues().setDouble( PROP_X, r.x + aDx );
    checkboxCfg.propValues().setDouble( PROP_Y, r.y + aDy );
    checkboxCfg.propValues().setDouble( PROP_WIDTH, r.width );
    checkboxCfg.propValues().setDouble( PROP_HEIGHT, r.height );
    checkboxCfg.propValues().setStr( PROP_TEXT, "" ); //$NON-NLS-1$

    return vedScreen.model().visels().create( checkboxCfg );
  }

  private VedAbstractActor createCheckBoxActor( VedAbstractVisel aVisel, String aAttrValue, Ugwi aRtdUgwi ) {
    IOptionSetEdit params = new OptionSet();

    String id = StridUtils.makeIdPath( "rtdEnable", aAttrValue ); //$NON-NLS-1$
    VedItemCfg cfg = new VedItemCfg( id, EVedItemKind.ACTOR, SkActorRtdataValue.FACTORY.id(), params );
    VedAbstractActor a = vedScreen.model().actors().create( cfg );
    a.props().setStr( TSID_NAME, "в работе " + aAttrValue );
    a.props().setStr( PROPID_VISEL_ID, aVisel.id() );
    a.props().setStr( PROPID_VISEL_PROP_ID, PROPID_ON_OFF_STATE );

    String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
    IStringMapEdit<ICompoundResolverConfig> resolvers = new StringMap<>();
    if( aRtdUgwi == null ) {
      CompoundResolverConfig resCfg = getResolverConfig( aAttrValue, "rtdEnable" );
      resolvers.put( PROPID_RTD_UGWI, resCfg );
      a.extraData().writeStridMap( sectionId, resolvers, CompoundResolverConfig.KEEPER );
    }
    else {
      a.props().setValobj( PROPID_RTD_UGWI, aRtdUgwi );
    }
    return null;
  }

  private VedAbstractActor createCmdCheckBoxActor( VedAbstractVisel aVisel, String aAttrValue, Ugwi aRtdUgwi ) {
    IOptionSetEdit params = new OptionSet();

    String id = StridUtils.makeIdPath( "cmdEnable", aAttrValue ); //$NON-NLS-1$
    VedItemCfg cfg = new VedItemCfg( id, EVedItemKind.ACTOR, SkActorCmdCheckbox.FACTORY.id(), params );
    VedAbstractActor a = vedScreen.model().actors().create( cfg );
    a.props().setStr( TSID_NAME, "в работе " + aAttrValue );
    a.props().setStr( PROPID_VISEL_ID, aVisel.id() );
    a.props().setStr( PROPID_VISEL_PROP_ID, PROPID_VALUE );

    String sectionId = VED_ITEM_EXTRA_DATA_ID_PROPERTIES_RESOLVERS;
    IStringMapEdit<ICompoundResolverConfig> resolvers = new StringMap<>();
    if( aRtdUgwi == null ) {
      CompoundResolverConfig resCfg = getCmdResolverConfig( aAttrValue, "cmdEnable" );
      resolvers.put( SkActorCmdCheckbox.TFI_CHECK_CMD_UGWI.id(), resCfg );
      resolvers.put( SkActorCmdCheckbox.TFI_UNCHECK_CMD_UGWI.id(), resCfg );
      a.extraData().writeStridMap( sectionId, resolvers, CompoundResolverConfig.KEEPER );
    }
    else {
      a.props().setValobj( SkActorCmdCheckbox.TFI_CHECK_CMD_UGWI.id(), aRtdUgwi );
      a.props().setValobj( SkActorCmdCheckbox.TFI_UNCHECK_CMD_UGWI.id(), aRtdUgwi );
    }
    return null;
  }

}
