package org.toxsoft.skf.mnemo.gui.m51;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.gui.*;

public class M5BaseFieldDefModel<T extends M5FieldDef>
    extends M5Model<T> {

  public static String MODEL_ID = "M5BaseField";

  M5StdFieldDefId<IStridable>   fldId   = new M5StdFieldDefId<>();
  M5StdFieldDefName<IStridable> fldName = new M5StdFieldDefName<>();

  M5StdFieldDefDescription<IStridable> fldDescription = new M5StdFieldDefDescription<>();

  M5StdFieldDefIconId<IIconIdable> fldIconId = new M5StdFieldDefIconId<>();

  M5FieldDef<T, String> fldFieldClass = new M5FieldDef<T, String>( "fieldClass" ) {

    @Override
    protected void doInit() {
      setNameAndDescription( "Класс поля", "Класс поля модели" );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected String doGetFieldValueName( T aEntity ) {
      return aEntity.ownerModel().entityClass().getSimpleName();
      // return "fieldClass";
    }

    @Override
    public String getFieldValue( T aEntity ) {
      return aEntity.ownerModel().entityClass().getSimpleName();
    }

    @Override
    public String defaultValue() {
      return params().getValue( TSID_DEFAULT_VALUE, avStr( "Object" ) ).asString();
    }
  };

  M5FieldDef<T, String> fldValueClass = new M5FieldDef<T, String>( "valueClass" ) {

    @Override
    protected void doInit() {
      setNameAndDescription( "Класс значения", "Класс значения поля модели" );
      setFlags( M5FF_COLUMN );
    }

    @Override
    protected String doGetFieldValueName( T aEntity ) {
      return aEntity.valueClass().getSimpleName();
      // return "fieldClass";
    }

    @Override
    public String getFieldValue( T aEntity ) {
      return aEntity.valueClass().getName();
    }

    @Override
    public String defaultValue() {
      return params().getValue( TSID_DEFAULT_VALUE, avStr( "Object" ) ).asString();
    }
  };

  public M5BaseFieldDefModel() {
    super( MODEL_ID, (Class<T>)M5FieldDef.class );
    addFieldDefs( fldId, fldName, fldDescription, fldIconId, fldFieldClass, fldValueClass );
  }

  IM5ItemsProvider<T> itemsProvider() {
    return null;
  }
}
