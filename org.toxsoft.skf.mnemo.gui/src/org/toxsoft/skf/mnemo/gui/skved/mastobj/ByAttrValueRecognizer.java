package org.toxsoft.skf.mnemo.gui.skved.mastobj;

/**
 * "Распознаватель" объекта по значению атрибута.
 * <p>
 * Например, из множества однотипных объектов выбери тот, у которого значение атрибута "number" == 12.
 *
 * @author vs
 */
public class ByAttrValueRecognizer {
  // implements ISkObjectRecognizer {
  //
  // private static final String PROPID_ATTRID = "attrId"; //$NON-NLS-1$
  //
  // private static final String PROPID_ATTR_VALUE = "attrValue"; //$NON-NLS-1$
  //
  // /**
  // * ИД фабрики.
  // */
  // public static final String FACTORY_ID = "byAttrValueRecognizer"; //$NON-NLS-1$
  //
  // /**
  // * Фабрика создания "распознавателя".
  // */
  // public static final ISkoRecognizerFactory FACTORY = new ISkoRecognizerFactory() {
  //
  // static class CfgPanel
  // implements ISkoRecognizerCfgPanel {
  //
  // Composite bkPanel;
  //
  // StridableTableViewer attrsViewer;
  //
  // IList<ISkObject> objects;
  //
  // ListViewer valuesViewer;
  //
  // CfgPanel( Composite aParent, Skid aObjSkid, ISkCoreApi aCoreApi ) {
  // bkPanel = new Composite( aParent, SWT.NONE );
  // bkPanel.setLayout( new GridLayout( 2, false ) );
  //
  // CLabel l;
  // l = new CLabel( bkPanel, SWT.NONE );
  // l.setText( "Атрибуты:" );
  //
  // l = new CLabel( bkPanel, SWT.NONE );
  // l.setText( "Значения:" );
  //
  // objects = aCoreApi.objService().listObjs( aObjSkid.classId(), true );
  //
  // int style = SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL;
  // attrsViewer = new StridableTableViewer( bkPanel, style, 80, 200, -1 );
  // ISkClassInfo clsInfo = aCoreApi.sysdescr().findClassInfo( aObjSkid.classId() );
  // attrsViewer.viewer().setInput( clsInfo.attrs().list().toArray() );
  // attrsViewer.viewer().addSelectionChangedListener( aEvent -> {
  // IStructuredSelection selection = (IStructuredSelection)attrsViewer.viewer().getSelection();
  // if( !selection.isEmpty() ) {
  // IDtoAttrInfo attrInfo = (IDtoAttrInfo)selection.getFirstElement();
  // IListEdit<IAtomicValue> values = new ElemArrayList<>();
  // for( ISkObject skObj : objects ) {
  // IAtomicValue av = skObj.attrs().getValue( attrInfo.id() );
  // values.add( av );
  // }
  // valuesViewer.setInput( values.toArray() );
  // }
  // else {
  // valuesViewer.setInput( null );
  // }
  // bkPanel.layout();
  // } );
  //
  // valuesViewer = new ListViewer( bkPanel, SWT.SINGLE | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL );
  // valuesViewer.setLabelProvider( new LabelProvider() {
  //
  // @Override
  // public String getText( Object aElement ) {
  // IAtomicValue av = (IAtomicValue)aElement;
  // return AvUtils.printAv( "%s", av ); //$NON-NLS-1$
  // }
  // } );
  // valuesViewer.setContentProvider( new ArrayContentProvider() );
  //
  // attrsViewer.viewer().getTable().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
  // valuesViewer.getControl().setLayoutData( new GridData( SWT.FILL, SWT.FILL, true, true ) );
  // }
  //
  // @Override
  // public ISkoRecognizerCfg config() {
  // IDtoAttrInfo attrInfo = selectedAttrInfo();
  // IAtomicValue av = selectedAttrValue();
  // if( attrInfo != null && av != null ) {
  // IOptionSetEdit opSet = new OptionSet();
  // opSet.setStr( PROPID_ATTRID, attrInfo.id() );
  // opSet.setValue( PROPID_ATTR_VALUE, av );
  // return new SkoRecognizerCfg( PROPID_ATTR_VALUE, ESkoRecognizerKind.ATTR, FACTORY_ID, opSet );
  //
  // }
  // return null;
  // }
  //
  // @Override
  // public void setConfig( ISkoRecognizerCfg aCfg ) {
  // // TODO Auto-generated method stub
  //
  // }
  //
  // @Override
  // public Control getControl() {
  // return bkPanel;
  // }
  //
  // IDtoAttrInfo selectedAttrInfo() {
  // IStructuredSelection selection = (IStructuredSelection)attrsViewer.viewer().getSelection();
  // if( !selection.isEmpty() ) {
  // return (IDtoAttrInfo)selection.getFirstElement();
  // }
  // return null;
  // }
  //
  // IAtomicValue selectedAttrValue() {
  // IStructuredSelection selection = (IStructuredSelection)valuesViewer.getSelection();
  // if( !selection.isEmpty() ) {
  // return (IAtomicValue)selection.getFirstElement();
  // }
  // return null;
  // }
  // }
  //
  // @Override
  // public ISkoRecognizerCfgPanel createCfgEditPanel( Composite aParent, Skid aObjSkid, ISkCoreApi aCoreApi ) {
  // return new CfgPanel( aParent, aObjSkid, aCoreApi );
  // }
  //
  // @Override
  // public ISkObjectRecognizer create( ISkoRecognizerCfg aCfg ) {
  // String attrId = aCfg.propValues().getStr( PROPID_ATTRID );
  // IAtomicValue attrVal = aCfg.propValues().getValue( PROPID_ATTR_VALUE );
  // return new ByAttrValueRecognizer( attrId, attrVal );
  // }
  // };
  //
  // private final String attrId;
  //
  // private final IAtomicValue attrValue;
  //
  // /**
  // * Конструктор.
  // *
  // * @param aAttrId String - ИД атрибута
  // * @param aAttrValue {@link IAtomicValue} - требуемое значение атрибута
  // */
  // public ByAttrValueRecognizer( String aAttrId, IAtomicValue aAttrValue ) {
  // attrId = aAttrId;
  // attrValue = aAttrValue;
  // }
  //
  // // ------------------------------------------------------------------------------------
  // // ISkObjectRecognizer
  // //
  //
  // @Override
  // public boolean recognize( Skid aObjSkid, ISkCoreApi aCoreApi ) {
  // ISkObject obj = aCoreApi.objService().find( aObjSkid );
  // IAtomicValue value = obj.attrs().getValue( attrId );
  // return value.equals( attrValue );
  // }

}
