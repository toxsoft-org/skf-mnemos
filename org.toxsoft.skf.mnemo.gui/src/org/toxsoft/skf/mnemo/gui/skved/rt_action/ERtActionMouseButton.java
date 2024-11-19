package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import static org.toxsoft.skf.mnemo.gui.skved.rt_action.ISkResources.*;

import org.eclipse.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The enumeration of mouse buttons for run-time user action. <br>
 *
 * @author dima
 */
public enum ERtActionMouseButton
    implements IStridable {

  /**
   * Left button {@link SWT#BUTTON1}.
   */
  LEFT( "LEFT", STR_LEFT_BTTN, STR_LEFT_BTTN_D ), //$NON-NLS-1$

  // /**
  // * Double click left button {@link SWT#BUTTON3}.
  // */
  // DOUBLE_CLICK( "DOUBLE_CLICK", STR_N_DOUBLE_CLICK, STR_D_DOUBLE_CLICK ), //$NON-NLS-1$

  /**
   * Middle button {@link SWT#BUTTON2}.
   */
  MIDDLE( "MIDDLE", STR_MIDDLE_BTTN, STR_MIDDLE_BTTN_D ), //$NON-NLS-1$

  /**
   * Right button {@link SWT#BUTTON3}.
   */
  RIGHT( "RIGHT", STR_RIGHT_BTTN, STR_RIGHT_BTTN_D ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ERtActionMouseButton"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ERtActionMouseButton> KEEPER =
      new StridableEnumKeeper<>( ERtActionMouseButton.class );

  private static IStridablesListEdit<ERtActionMouseButton> list = null;

  private final String id;
  private final String name;
  private final String description;

  ERtActionMouseButton( String aId, String aName, String aDescription ) {
    id = aId;
    name = aName;
    description = aDescription;
  }

  // --------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return name;
  }

  @Override
  public String description() {
    return description;
  }

  // ----------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ERtActionMouseButton} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ERtActionMouseButton> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ERtActionMouseButton} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ERtActionMouseButton getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ERtActionMouseButton} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ERtActionMouseButton findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ERtActionMouseButton item : values() ) {
      if( item.name.equals( aName ) ) {
        return item;
      }
    }
    return null;
  }

  /**
   * Returns the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ERtActionMouseButton} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ERtActionMouseButton getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
