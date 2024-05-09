package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Тип "распознователя" объекта.
 *
 * @author vs
 */
public enum ESkoRecognizerKind
    implements IStridable {

  /**
   * По значению атрибута.
   */
  ATTR( "attr", "По значению атрибута", "По значению атрибута" ), //$NON-NLS-1$

  /**
   * По связи.
   */
  LINK( "link", "По связи", "По наличию связанного объекта" ), //$NON-NLS-1$

  /**
   * По значению НСИ атрибута.
   */
  RRI_ATTR( "rriAttr", "По значению НСИ атрибута", "По значению НСИ атрибута" ), //$NON-NLS-1$

  /**
   * По НСИ связи.
   */
  RRI_LINK( "rriLink", "По НСИ связи", "По наличию связанного объекта НСИ" ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ESkoRecognizerKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ESkoRecognizerKind> KEEPER =
      new StridableEnumKeeper<>( ESkoRecognizerKind.class );

  private static IStridablesListEdit<ESkoRecognizerKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  ESkoRecognizerKind( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link ESkoRecognizerKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ESkoRecognizerKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ESkoRecognizerKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ESkoRecognizerKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ESkoRecognizerKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ESkoRecognizerKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ESkoRecognizerKind item : values() ) {
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
   * @return {@link ESkoRecognizerKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ESkoRecognizerKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
