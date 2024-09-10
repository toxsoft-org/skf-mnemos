package org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

public class ByAttrValueRecognizerCfgPanel
    extends AbstractGenericEntityEditPanel<ISkoRecognizerCfg>
    implements ISkoRecognizerCfgPanel {

  Composite bkPanel;

  StridableTableViewer attrsViewer;

  ListViewer valuesViewer;

  private final Skid       objSkid;
  private final ISkCoreApi coreApi;

  private final IStridablesList<ISkObject> objects;

  // public ByAttrValueRecognizerCfgPanel( Skid aObjSkid, IStridablesList<ISkObject> aObjects, ITsGuiContext aContext )
  // {
  public ByAttrValueRecognizerCfgPanel( IStridablesList<ISkObject> aObjects, ISkCoreApi aCoreApi,
      ITsGuiContext aContext ) {
    super( aContext, false );
    objects = aObjects;
    objSkid = aObjects.first().skid();
    // objSkid = aObjSkid;
    coreApi = aCoreApi;
  }

  // ------------------------------------------------------------------------------------
  // ISkoRecognizerCfgPanel
  //

  @Override
  protected ValidationResult doCanGetEntity() {
    IDtoAttrInfo attrInfo = selectedAttrInfo();
    IAtomicValue attrValue = selectedAttrValue();
    if( attrInfo != null && attrValue != null ) {
      return ValidationResult.SUCCESS;
    }
    return ValidationResult.create( EValidationResultType.ERROR, "Необходимо выбрать аттрибут и его значение" );
  }

  @Override
  protected ISkoRecognizerCfg doGetEntity() {
    IDtoAttrInfo attrInfo = selectedAttrInfo();
    IAtomicValue attrValue = selectedAttrValue();
    if( attrInfo != null && attrValue != null ) {
      IOptionSetEdit opSet = new OptionSet();
      opSet.setStr( ByAttrValueRecognizer.PROPID_ATTRID, attrInfo.id() );
      opSet.setValue( ByAttrValueRecognizer.PROPID_ATTR_VALUE, attrValue );
      String idPath = StridUtils.makeIdPath( objSkid.classId(), objSkid.strid() );
      idPath = StridUtils.makeIdPath( idPath, attrInfo.id() );
      return new SkoRecognizerCfg( idPath, ESkoRecognizerKind.ATTR, opSet );
    }
    return null;
  }

  @Override
  protected void doProcessSetEntity() {
    ISkoRecognizerCfg cfg = getEntity();
    if( cfg != null ) {
      String attrId = cfg.propValues().getStr( ByAttrValueRecognizer.PROPID_ATTRID );
      IAtomicValue attrValue = cfg.propValues().getValue( ByAttrValueRecognizer.PROPID_ATTR_VALUE );
      attrsViewer.setSelectedItemById( attrId );
      valuesViewer.setSelection( new StructuredSelection( attrValue ) );
    }
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    bkPanel = new Composite( aParent, SWT.NONE );
    bkPanel.setLayout( new GridLayout( 2, false ) );

    CLabel l;
    l = new CLabel( bkPanel, SWT.NONE );
    l.setText( "Атрибуты:" );

    l = new CLabel( bkPanel, SWT.NONE );
    l.setText( "Значения:" );

    // objects = coreApi.objService().listObjs( objSkid.classId(), true );

    int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL;
    attrsViewer = new StridableTableViewer( bkPanel, style, 80, 200, -1 );
    ISkClassInfo clsInfo = coreApi.sysdescr().findClassInfo( objSkid.classId() );
    attrsViewer.viewer().setInput( clsInfo.attrs().list().toArray() );
    attrsViewer.viewer().addSelectionChangedListener( aEvent -> {
      IStructuredSelection selection = (IStructuredSelection)attrsViewer.viewer().getSelection();
      if( !selection.isEmpty() ) {
        IDtoAttrInfo attrInfo = (IDtoAttrInfo)selection.getFirstElement();
        IListEdit<IAtomicValue> values = new ElemArrayList<>();
        for( ISkObject skObj : objects ) {
          IAtomicValue av = skObj.attrs().getValue( attrInfo.id() );
          values.add( av );
        }
        valuesViewer.setInput( values.toArray() );
      }
      else {
        valuesViewer.setInput( null );
      }
      bkPanel.layout();
    } );

    valuesViewer = new ListViewer( bkPanel, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
    valuesViewer.setLabelProvider( new LabelProvider() {

      @Override
      public String getText( Object aElement ) {
        IAtomicValue av = (IAtomicValue)aElement;
        return AvUtils.printAv( "%s", av ); //$NON-NLS-1$
      }
    } );
    valuesViewer.setContentProvider( new ArrayContentProvider() );

    attrsViewer.viewer().getTable().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
    valuesViewer.getControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );

    attrsViewer.viewer().addSelectionChangedListener( aEvent -> genericChangeEventer().fireChangeEvent() );
    valuesViewer.addSelectionChangedListener( aEvent -> genericChangeEventer().fireChangeEvent() );

    return bkPanel;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  IDtoAttrInfo selectedAttrInfo() {
    IStructuredSelection selection = (IStructuredSelection)attrsViewer.viewer().getSelection();
    if( !selection.isEmpty() ) {
      return (IDtoAttrInfo)selection.getFirstElement();
    }
    return null;
  }

  IAtomicValue selectedAttrValue() {
    IStructuredSelection selection = (IStructuredSelection)valuesViewer.getSelection();
    if( !selection.isEmpty() ) {
      return (IAtomicValue)selection.getFirstElement();
    }
    return null;
  }

}
