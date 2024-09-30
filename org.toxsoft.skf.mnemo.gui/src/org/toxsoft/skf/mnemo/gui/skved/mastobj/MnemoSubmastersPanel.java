package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.mnemo.gui.mastobj.IMnemoMasterObjectConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.mastobj.ISkResources.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;
import org.toxsoft.skf.mnemo.gui.mastobj.resolver.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.*;
import org.toxsoft.skf.mnemo.gui.tsgui.utils.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.ugwis.kinds.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.utils.*;

/**
 * Панель для редактирования класса главного мастер-объекта
 *
 * @author vs
 */
public class MnemoSubmastersPanel
    extends TsPanel
    implements ISkGuiContextable {

  private final CLabel labelMasterClass;

  private final IVedScreen vedScreen;

  private MnemoResolverConfig resolverConfig = null;

  private final PanelSubmastersList submastersPanel;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aVedScreen {@link IVedScreen} - экран редактора
   * @param aStyle int - SWT стиль панели
   */
  public MnemoSubmastersPanel( Composite aParent, IVedScreen aVedScreen, int aStyle ) {
    super( aParent, aVedScreen.tsContext(), aStyle );

    vedScreen = aVedScreen;

    setLayout( new GridLayout( 2, false ) );
    CLabel l = new CLabel( this, SWT.NONE );
    l.setText( STR_L_MASTER_OBJECT_CLASS );
    l.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false, 2, 1 ) );
    labelMasterClass = new CLabel( this, SWT.BORDER );
    labelMasterClass.setLayoutData( new GridData( SWT.FILL, SWT.TOP, true, false ) );
    Button btnSelectMaster = new Button( this, SWT.PUSH );
    btnSelectMaster.setImage( iconManager().loadStdIcon( ICONID_DOCUMENT_EDIT, EIconSize.IS_16X16 ) );
    btnSelectMaster.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        ISkClassInfo clsInfo = SkGuiUtils.selectClass( null, skConn(), vedScreen.tsContext() );
        if( clsInfo != null ) {
          labelMasterClass.setText( clsInfo.nmName() );
          Ugwi ugwi = UgwiKindSkClassInfo.makeUgwi( clsInfo.id() );
          ICompoundResolverConfig resCfg = DirectSkidResolver.createResolverConfig( ugwi );
          SubmasterConfig smCfg = SubmasterConfig.create( VED_SCREEN_MAIN_MNEMO_RESOLVER_ID, new OptionSet(), resCfg );
          resolverConfig.subMasters().add( smCfg );
          String itemId = VED_SCREEN_EXTRA_DATA_ID_MNEMO_RESOLVER_CONGIF;
          vedScreen.model().extraData().writeItem( itemId, resolverConfig, MnemoResolverConfig.KEEPER );
        }
      }
    } );

    submastersPanel = new PanelSubmastersList( this, vedScreen );
    submastersPanel.setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true, 2, 1 ) );
  }

  // ------------------------------------------------------------------------------------
  // ISkGuiContextable
  //

  @Override
  public ISkConnection skConn() {
    ISkVedEnvironment vedEnv = vedScreen.tsContext().get( ISkVedEnvironment.class );
    return vedEnv.skConn();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает конфигурацию "разрешителя" главного мастер-объета мнемосхемы.
   *
   * @param aCfg MnemoResolverConfig - конфигурация "разрешителя" главного мастер-объета мнемосхемы
   */
  public void setMnemoResolverConfig( MnemoResolverConfig aCfg ) {
    resolverConfig = aCfg;
    labelMasterClass.setText( TsLibUtils.EMPTY_STRING );
    if( aCfg != null ) {
      ISkClassInfo clsInfo = MasterObjectUtils.findMainMasterClassInfo( aCfg, coreApi() );
      if( clsInfo != null ) {
        labelMasterClass.setText( clsInfo.nmName() );
      }
      submastersPanel.setMnemoResolverConfig( aCfg );
    }
  }

}
