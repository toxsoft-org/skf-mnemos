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
public enum ESkObjRecognizerKind
    implements IStridable {

  /**
   * No border will be drawn.
   */
  ATTR( "attr", "Значение атрибута", "По значению атрибута" ), //$NON-NLS-1$

  /**
   * Single line border.
   */
  LINK( "link", "По связи", "По наличию связанного объекта" ), //$NON-NLS-1$

  /**
   * No border will be drawn.
   */
  RRI_ATTR( "rriAttr", "Значение НСИ атрибута", "По значению НСИ атрибута" ), //$NON-NLS-1$

  /**
   * Single line border.
   */
  RRI_LINK( "rriLink", "По НСИ связи", "По наличию связанного объекта НСИ" ), //$NON-NLS-1$

  ;

  /**
   * The keeper ID.
   */
  public static final String KEEPER_ID = "ESkObjRecognizerKind"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<ESkObjRecognizerKind> KEEPER =
      new StridableEnumKeeper<>( ESkObjRecognizerKind.class );

  private static IStridablesListEdit<ESkObjRecognizerKind> list = null;

  private final String id;
  private final String name;
  private final String description;

  ESkObjRecognizerKind( String aId, String aName, String aDescription ) {
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
   * @return {@link IStridablesList}&lt; {@link ESkObjRecognizerKind} &gt; - list of constants in order of declaraion
   */
  public static IStridablesList<ESkObjRecognizerKind> asList() {
    if( list == null ) {
      list = new StridablesList<>( values() );
    }
    return list;
  }

  /**
   * Returns the constant by the ID.
   *
   * @param aId String - the ID
   * @return {@link ESkObjRecognizerKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified ID
   */
  public static ESkObjRecognizerKind getById( String aId ) {
    return asList().getByKey( aId );
  }

  /**
   * Finds the constant by the name.
   *
   * @param aName String - the name
   * @return {@link ESkObjRecognizerKind} - found constant or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ESkObjRecognizerKind findByName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    for( ESkObjRecognizerKind item : values() ) {
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
   * @return {@link ESkObjRecognizerKind} - found constant
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no constant found by specified name
   */
  public static ESkObjRecognizerKind getByName( String aName ) {
    return TsItemNotFoundRtException.checkNull( findByName( aName ) );
  }

}
