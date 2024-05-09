package org.toxsoft.skf.mnemo.gui.mastobj.resolver;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.mnemo.gui.mastobj.*;

/**
 * The compound UGWI resolver is a chain of the {@link IUgwiResolver}.
 *
 * @author hazard157
 */
public interface ICompoundResolver
    extends IUgwiResolver {

  /**
   * Returns all resolvers of the chain.
   *
   * @return {@link IList}&lt;{@link IUgwiResolver}&gt; - resolvers in the call oreder
   */
  IList<IUgwiResolver> resolversList();

  /**
   * Adds resolver to the end of the resolvers list.
   *
   * @param aResolver {@link IUgwiResolver} - the resolver
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addResolver( IUgwiResolver aResolver );

}
