package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;
import org.toxsoft.uskat.core.*;

public interface ISkoRecognizerFactory {

  ISkObjectRecognizer create( ISkoRecognizerCfg aCfg );

  ISkoRecognizerCfgPanel createCfgEditPanel( Composite aParent, Skid aObjSkid, ISkCoreApi aCoreApi );
}
