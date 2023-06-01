package org.toxsoft.skf.mnemo.gui.km5;

import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.mnemo.lib.*;

/**
 * LM for {@link SkMnemoM5Model}.
 *
 * @author vs
 */
class MnemoM5LifecycleManager
    extends M5LifecycleManager<ISkMnemoCfg, ISkMnemosService> {

  public MnemoM5LifecycleManager( IM5Model<ISkMnemoCfg> aModel, ISkMnemosService aMaster ) {
    super( aModel, true, true, true, true, aMaster );
  }

  private static Pair<String, IOptionSet> make( IM5Bunch<ISkMnemoCfg> aValues ) {
    String mnemoId = aValues.getAsAv( AID_STRID ).asString();
    IOptionSetEdit p = new OptionSet();
    p.setValue( AID_NAME, aValues.getAsAv( AID_NAME ) );
    p.setValue( AID_DESCRIPTION, aValues.getAsAv( AID_DESCRIPTION ) );
    return new Pair<>( mnemoId, p );
  }

  // ------------------------------------------------------------------------------------
  // M5LifecycleManager
  //

  @Override
  protected ValidationResult doBeforeCreate( IM5Bunch<ISkMnemoCfg> aValues ) {
    Pair<String, IOptionSet> p = make( aValues );
    return master().svs().validator().canCreateMnemoCfg( p.left(), p.right() );
  }

  @Override
  protected ISkMnemoCfg doCreate( IM5Bunch<ISkMnemoCfg> aValues ) {
    Pair<String, IOptionSet> p = make( aValues );
    return master().createMnemo( p.left(), p.right() );
  }

  @Override
  protected ValidationResult doBeforeEdit( IM5Bunch<ISkMnemoCfg> aValues ) {
    Pair<String, IOptionSet> p = make( aValues );
    return master().svs().validator().canEditMnemoCfg( p.left(), p.right() );
  }

  @Override
  protected ISkMnemoCfg doEdit( IM5Bunch<ISkMnemoCfg> aValues ) {
    Pair<String, IOptionSet> p = make( aValues );
    return master().editMnemo( p.left(), p.right() );
  }

  @Override
  protected ValidationResult doBeforeRemove( ISkMnemoCfg aEntity ) {
    return master().svs().validator().canRemoveMnemoCfg( aEntity.strid() );
  }

  @Override
  protected void doRemove( ISkMnemoCfg aEntity ) {
    master().removeMnemo( aEntity.strid() );
  }

  @Override
  protected IList<ISkMnemoCfg> doListEntities() {
    IListEdit<ISkMnemoCfg> ll = new ElemArrayList<>();
    for( String mnid : master().listMnemosIds() ) {
      ll.add( master().getMnemo( mnid ) );
    }
    return ll;
  }

}
