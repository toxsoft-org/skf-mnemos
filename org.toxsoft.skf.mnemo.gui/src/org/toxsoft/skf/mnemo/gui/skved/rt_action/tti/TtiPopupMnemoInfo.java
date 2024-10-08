package org.toxsoft.skf.mnemo.gui.skved.rt_action.tti;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.skved.rt_action.*;

/**
 * The {@link ITinTypeInfo} implementation for {@link PopupMnemoInfo}.
 *
 * @author vs
 */
public class TtiPopupMnemoInfo
    extends AbstractTinTypeInfo<PopupMnemoInfo> {

  /**
   * ИД поля "Skid мнемосхемы"
   */
  private static final String FID_MNEMO_SKID = "mnemoSkid"; //$NON-NLS-1$

  public static final ITinFieldInfo TFI_MNEMO_SKID = new TinFieldInfo( FID_MNEMO_SKID, TTI_SKID, //
      TSID_NAME, "Мнемосхема", //
      TSID_DESCRIPTION, "ИД объекта мнемосхемы" );

  /**
   * ИД поля "Кнопка мыши"
   */
  private static final String FID_MOUSE_BUTTON = "mouseButton"; //$NON-NLS-1$

  private static final ITinFieldInfo TFI_MOUSE_BUTTON = new TinFieldInfo( FID_MOUSE_BUTTON, TtiAvEnum.INSTANCE, //
      TSID_NAME, "Кнопка мыши", //
      TSID_DESCRIPTION, "Кнопка мыши для вызова всплывающего окна мнемосхемы", //
      TSID_KEEPER_ID, ERtActionMouseButton.KEEPER_ID //
  );

  TtiPopupMnemoInfo() {
    super( ETinTypeKind.FULL, IRtActionConstants.DT_POPUP_MNEEMO_INFO, PopupMnemoInfo.class );
    fieldInfos().add( TFI_MNEMO_SKID );
    fieldInfos().add( TFI_MOUSE_BUTTON );
    fieldInfos().add( TFI_RTD_UGWI );
  }

  /**
   * The type information singleton.
   */
  public static final TtiPopupMnemoInfo INSTANCE = new TtiPopupMnemoInfo();

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( PopupMnemoInfo.EMPTY );
  }

  @Override
  protected ITinValue doGetTinValue( PopupMnemoInfo aEntity ) {
    IStringMapEdit<ITinValue> childValues = new StringMap<>();
    childValues.put( FID_MNEMO_SKID, TinValue.ofAtomic( avValobj( aEntity.mnemoSkid() ) ) );
    childValues.put( FID_MOUSE_BUTTON, TinValue.ofAtomic( avValobj( aEntity.mouseButton() ) ) );
    return TinValue.ofFull( avValobj( aEntity ), childValues );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    Skid mnemoSkid = extractChildValobj( TFI_MNEMO_SKID, aChildValues );
    ERtActionMouseButton mouseBtn = extractChildValobj( TFI_MOUSE_BUTTON, aChildValues );
    return avValobj( new PopupMnemoInfo( mnemoSkid, "", PopupMnemoResolverConfig.EMPTY, mouseBtn, false, 0 ) );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    PopupMnemoInfo pmi = aValue.asValobj();
    aChildValues.put( FID_MNEMO_SKID, TinValue.ofAtomic( avValobj( pmi.mnemoSkid() ) ) );
    aChildValues.put( FID_MOUSE_BUTTON, TinValue.ofAtomic( avValobj( pmi.mouseButton() ) ) );
  }
}
