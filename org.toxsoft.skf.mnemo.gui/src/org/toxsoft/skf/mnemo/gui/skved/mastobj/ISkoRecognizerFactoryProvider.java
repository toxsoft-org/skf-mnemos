package org.toxsoft.skf.mnemo.gui.skved.mastobj;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers.*;

/**
 * Поставщик фабрик "распознавателей" sk-объектов.
 * <p>
 *
 * @author vs
 */
public interface ISkoRecognizerFactoryProvider {

  /**
   * Поставщик фабрик "распознавателей" sk-объектов по-умолчанию
   */
  ISkoRecognizerFactoryProvider DEFAULT = new InternalProvider();

  /**
   * Возвращает фабрику по типу "распознавателя" sk-объектов или <code>null</code> если фабрика не найдена.
   *
   * @param aKind {@link ESkoRecognizerKind} - тип "распознавателя"
   * @return {@link ISkoRecognizerFactory} - фабрика "распознавателя" sk-объектов или <code>null</code>
   */
  ISkoRecognizerFactory findFactory( ESkoRecognizerKind aKind );
}

class InternalProvider
    implements ISkoRecognizerFactoryProvider {

  private final static IMapEdit<ESkoRecognizerKind, ISkoRecognizerFactory> factoriesMap = new ElemMap<>();

  InternalProvider() {
    // factoriesMap.put( ESkoRecognizerKind.ATTR, ByAttrValueRecognizer.FACTORY );
  }

  @Override
  public ISkoRecognizerFactory findFactory( ESkoRecognizerKind aKind ) {
    if( factoriesMap.hasKey( aKind ) ) {
      return factoriesMap.getByKey( aKind );
    }
    return null;
  }
}
