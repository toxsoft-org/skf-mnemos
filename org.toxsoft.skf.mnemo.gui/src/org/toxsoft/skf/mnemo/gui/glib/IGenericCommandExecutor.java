package org.toxsoft.skf.mnemo.gui.glib;

import org.toxsoft.core.tslib.bricks.validator.*;

public interface IGenericCommandExecutor {

  ValidationResult execGenericCommand( IGenericCommand aCommand );

}
