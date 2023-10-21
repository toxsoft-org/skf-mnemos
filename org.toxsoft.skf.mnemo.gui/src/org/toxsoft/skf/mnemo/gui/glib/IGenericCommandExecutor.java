package org.toxsoft.skf.mnemo.gui.glib;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.validator.*;

public interface IGenericCommandExecutor {

  ValidationResult execGenericCommand( GenericCommand aCommand );

  IOptionSetEdit callCommand( GenericCommand aCommand );

}
