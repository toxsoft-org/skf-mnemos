package org.toxsoft.skf.mnemo.gui.skved.rt_action.tti;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

public class TtiKeyMask
    extends AbstractTinTypeInfo<Integer> {

  /**
   * ИД поля "Alt"
   */
  private static final String FID_ALT = "maskAlt"; //$NON-NLS-1$

  /**
   * ИД поля "Shift"
   */
  private static final String FID_SHIFT = "maskShift"; //$NON-NLS-1$

  /**
   * ИД поля "Ctrl"
   */
  private static final String FID_CTRL = "maskCtrl"; //$NON-NLS-1$

  public static final ITinFieldInfo TFI_ALT = new TinFieldInfo( FID_ALT, TTI_AT_BOOLEAN, //
      TSID_NAME, "Alt", //
      TSID_DESCRIPTION, "Нажата клавиша Alt" );

  public static final ITinFieldInfo TFI_SHIFT = new TinFieldInfo( FID_SHIFT, TTI_AT_BOOLEAN, //
      TSID_NAME, "Shift", //
      TSID_DESCRIPTION, "Нажата клавиша Shift" );

  public static final ITinFieldInfo TFI_CTRL = new TinFieldInfo( FID_CTRL, TTI_AT_BOOLEAN, //
      TSID_NAME, "Ctrl", //
      TSID_DESCRIPTION, "Нажата клавиша Ctrl" );

  public static final TtiKeyMask INSTANCE = new TtiKeyMask();

  TtiKeyMask() {
    super( ETinTypeKind.FULL, DDEF_INTEGER, Integer.class );
    fieldInfos().add( TFI_ALT );
    fieldInfos().add( TFI_SHIFT );
    fieldInfos().add( TFI_CTRL );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  @Override
  protected ITinValue doGetNullTinValue() {
    return doGetTinValue( Integer.valueOf( 0 ) );
  }

  @Override
  protected ITinValue doGetTinValue( Integer aEntity ) {
    IStringMapEdit<ITinValue> children = new StringMap<>();
    return TinValue.ofFull( avInt( aEntity.intValue() ), children );
  }

  @Override
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    boolean alt = extractChildBool( TFI_ALT.id(), aChildValues, false );
    boolean shift = extractChildBool( TFI_SHIFT.id(), aChildValues, false );
    boolean ctrl = extractChildBool( TFI_CTRL.id(), aChildValues, false );

    int result = 0;
    if( alt ) {
      result |= SWT.ALT;
    }
    if( shift ) {
      result |= SWT.SHIFT;
    }
    if( ctrl ) {
      result |= SWT.CTRL;
    }

    return avInt( result );
  }

  @Override
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    int value = aValue.asInt();
    boolean alt = (value & SWT.ALT) == SWT.ALT;
    boolean shift = (value & SWT.SHIFT) == SWT.SHIFT;
    boolean ctrl = (value & SWT.CTRL) == SWT.CTRL;
    aChildValues.put( FID_ALT, TinValue.ofAtomic( avBool( alt ) ) );
    aChildValues.put( FID_SHIFT, TinValue.ofAtomic( avBool( shift ) ) );
    aChildValues.put( FID_CTRL, TinValue.ofAtomic( avBool( ctrl ) ) );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private boolean extractChildBool( String aFieldId, IStringMap<ITinValue> aChildValues, boolean aDefaultValue ) {
    IAtomicValue av = extractChildAtomic( aFieldId, aChildValues, null );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asBool();
  }

}
