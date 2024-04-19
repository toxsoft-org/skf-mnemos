package org.toxsoft.skf.mnemo.gui.skved.rt_action;

import static org.toxsoft.skf.mnemo.gui.skved.rt_action.ISkResources.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Types of run time user actions.
 *
 * @author dima
 */
@SuppressWarnings( "javadoc" )
public enum ERtActionKind
    implements IStridable {

  NONE( "none", STR_N_EMPTY_ACTION, STR_D_EMPTY_ACTION ), //$NON-NLS-1$

  POPUP_MNEMO( "popup_mnemo", STR_N_POPUP_MNEMO, STR_D_POPUP_MNEMO ), //$NON-NLS-1$

  SWITCH_PERSP( "switch_persp", STR_N_SWITCH_PERSP, STR_D_SWITCH_PERSP ); //$NON-NLS-1$

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "ERtActionKind"; //$NON-NLS-1$

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<ERtActionKind> KEEPER = new StridableEnumKeeper<>( ERtActionKind.class );

  private static IStridablesListEdit<ERtActionKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  ERtActionKind( String aId, String aName, String aDescription ) {
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
  // Stridable enum common API
  //

  /**
   * Returns all constants in single list.
   *
   * @return {@link IStridablesList}&lt; {@link ERtActionKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ERtActionKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ERtActionKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ERtActionKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ERtActionKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ERtActionKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ERtActionKind item : values() ) {
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
   * @return {@link ERtActionKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ERtActionKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
