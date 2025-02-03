package org.toxsoft.skf.mnemo.gui.cmd;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.skf.mnemo.gui.skved.*;

/**
 * Определяемое пользователем действие, которое будет выполнено соотвествующим актором {@link SkActorUserAction}.
 * <p>
 * Пользователю необходимо написать класс, реализующий данный интерфейс и зарегестрировать его при старте программы,
 * например в соотвествующем кванте.
 *
 * @author vs
 */
public interface IMnemoUserAction
    extends IStridable {

  /**
   * Информация о типах данных, значения которых должны быть введены пользователем на этапе разработки как свойства
   * актора {@link SkActorUserAction}.
   *
   * @return IStridablesList&lt;IDataDef> - список описаний типов входных данных
   */
  IStridablesList<IDataDef> inputDefs();

  /**
   * Информация о полях инспектора, значения которых должны быть введены пользователем на этапе разработки как свойства
   * актора {@link SkActorUserAction}.
   *
   * @return IStridablesList&lt;ITinFieldInfo> - список описаний типов входных данных
   */
  IStridablesList<ITinFieldInfo> tinFields();

  /**
   * Выполняет действие.<br>
   *
   * @param aInputValues {@link IOptionSet} - набор значений входных данных, заданных на этапе разработки
   * @param aCoors {@link ITsPoint} - координаты курсора мыши в момент вызова
   * @param aTsContext {@link ITsGuiContext} - контекст содержащий необходимую информацию
   */
  void run( IOptionSet aInputValues, ITsPoint aCoors, ITsGuiContext aTsContext );

  // /**
  // * Возвращает набор значений аргументов.<br>
  // *
  // * @param aInputValues {@link IOptionSet} - набор значений входных данных, заданных на этапе разработки
  // * @param aTsContext {@link ITsGuiContext} - контекст содержащий информацию для формирования значений аргументов
  // * @return {@link IOptionSet} - набор значений аргументов
  // */
  // IOptionSet argValues( IOptionSet aInputValues, ITsGuiContext aTsContext );

  // /**
  // * Выполняет команду.
  // *
  // * @param aArgs {@link IOptionSet}
  // * @param aTsContext {@link ITsGuiContext} - контекст содержащий информацию для выполнения команды
  // * @return {@link IOptionSet} - execution result, may be {@link IOptionSet#NULL} but not <code>null</code>
  // */
  // IOptionSet executeCommand( IOptionSet aArgs, ITsGuiContext aTsContext );

}
