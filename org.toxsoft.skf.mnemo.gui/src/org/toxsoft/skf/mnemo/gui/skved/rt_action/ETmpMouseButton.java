package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import org.eclipse.swt.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The enumeration of mouse buttons. <br>
 * FIXME use ETsMouseButton when it will be in proper state (has keeper)
 *
 * @author dima
 */
public enum ETmpMouseButton
    implements IStridable {

  /**
   * Left button {@link SWT#BUTTON1}.
   */
  LEFT( "LEFT", "Left button", "SWT#BUTTON1" ), //$NON-NLS-1$

  /**
   * Middle button {@link SWT#BUTTON2}.
   */
  MIDDLE( "MIDDLE", "Middle button", "SWT#BUTTON2" ), //$NON-NLS-1$

  /**
   * Right button {@link SWT#BUTTON3}.
   */
  RIGHT( "RIGHT", "Right button", "SWT#BUTTON3" ); //$NON-NLS-1$

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ETmpMouseButton"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ETmpMouseButton> KEEPER = new StridableEnumKeeper<>( ETmpMouseButton.class );

  private static IStridablesListEdit<ETmpMouseButton> list = null;

  private final String id;
  private final String name;
  private final String description;

  ETmpMouseButton( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link ETmpMouseButton} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ETmpMouseButton> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ETmpMouseButton} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ETmpMouseButton getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ETmpMouseButton} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ETmpMouseButton findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ETmpMouseButton item : values() ) {
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
   * @return {@link ETmpMouseButton} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ETmpMouseButton getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
