package org.toxsoft.skf.mnemo.gui.skved;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ISkResources {

  /**
   * {@link ISkVedConstants}
   */
  String STR_PROP_ATTR_GWID       = Messages.getString( "STR_PROP_ATTR_GWID" );       //$NON-NLS-1$
  String STR_PROP_ATTR_GWID_D     = Messages.getString( "STR_PROP_ATTR_GWID_D" );     //$NON-NLS-1$
  String STR_PROP_RTD_GWID        = Messages.getString( "STR_PROP_RTD_GWID" );        //$NON-NLS-1$
  String STR_PROP_RTD_GWID_D      = Messages.getString( "STR_PROP_RTD_GWID_D" );      //$NON-NLS-1$
  String STR_PROP_CMD_GWID        = Messages.getString( "STR_PROP_CMD_GWID" );        //$NON-NLS-1$
  String STR_PROP_CMD_GWID_D      = Messages.getString( "STR_PROP_CMD_GWID_D" );      //$NON-NLS-1$
  String STR_PROP_FORMAT_STRING   = Messages.getString( "STR_PROP_FORMAT_STRING" );   //$NON-NLS-1$
  String STR_PROP_FORMAT_STRING_D = Messages.getString( "STR_PROP_FORMAT_STRING_D" ); //$NON-NLS-1$

  /**
   * {@link SkActorCmdButton}
   */
  String STR_ACTOR_CMD_BUTTON   = Messages.getString( "STR_ACTOR_CMD_BUTTON" );   //$NON-NLS-1$
  String STR_ACTOR_CMD_BUTTON_D = Messages.getString( "STR_ACTOR_CMD_BUTTON_D" ); //$NON-NLS-1$

  /**
   * {@link SkActorRtdataText}
   */
  String STR_ACTOR_RTDATA_TEXT   = Messages.getString( "STR_ACTOR_RTDATA_TEXT" );   //$NON-NLS-1$
  String STR_ACTOR_RTDATA_TEXT_D = Messages.getString( "STR_ACTOR_RTDATA_TEXT_D" ); //$NON-NLS-1$

  /**
   * {@link SkActorAttrText}
   */
  String STR_ACTOR_ATTR_TEXT   = Messages.getString( "STR_ACTOR_ATTR_TEXT" );   //$NON-NLS-1$
  String STR_ACTOR_ATTR_TEXT_D = Messages.getString( "STR_ACTOR_ATTR_TEXT_D" ); //$NON-NLS-1$

  /**
   * {@link SkActorRtdataValue}
   */
  String STR_ACTOR_RTDATA_VALUE   = Messages.getString( "STR_ACTOR_RTDATA_VALUE" );   //$NON-NLS-1$
  String STR_ACTOR_RTDATA_VALUE_D = Messages.getString( "STR_ACTOR_RTDATA_VALUE_D" ); //$NON-NLS-1$

  /**
   * {@link SkActorRtBooleanValue}
   */
  String STR_ACTOR_RTBOOLEAN_VALUE   = Messages.getString( "STR_ACTOR_RTBOOLEAN_VALUE" );   //$NON-NLS-1$
  String STR_ACTOR_RTBOOLEAN_VALUE_D = Messages.getString( "STR_ACTOR_RTBOOLEAN_VALUE_D" ); //$NON-NLS-1$
  String STR_INVERSE_BOOLEAN         = Messages.getString( "STR_INVERSE_BOOLEAN" );         //$NON-NLS-1$
  String STR_INVERSE_BOOLEAN_D       = Messages.getString( "STR_INVERSE_BOOLEAN_D" );       //$NON-NLS-1$

  /**
   * {@link SkActorColorDecorator}
   */
  String STR_RGBA_SET                = "Набор цветов";
  String STR_RGBA_SET_D              = "Набор цветов в виде RGBA";
  String STR_ACTOR_COLOR_DECORATOR   = "Декоратор цветом";
  String STR_ACTOR_COLOR_DECORATOR_D = "В зависимости от значения данного задает цвет указанного свойства";

  /**
   * {@link SkActorRtdataImage}
   */
  String STR_IMAGE_SET            = "Набор изображений";
  String STR_IMAGE_SET_D          = "Набор изображений в виде их описаний";
  String STR_ACTOR_RTDATA_IMAGE   = "Отображение значения изображением";
  String STR_ACTOR_RTDATA_IMAGE_D = "Выбирает одно изображение из набора, используя значение РВ_данного как индекс";
}
