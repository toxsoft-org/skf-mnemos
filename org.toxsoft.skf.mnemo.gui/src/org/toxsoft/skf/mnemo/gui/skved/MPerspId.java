package org.toxsoft.skf.mnemo.gui.skved;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Идентификатор E4 перспективы.
 * <p>
 *
 * @author dima
 */
public class MPerspId
    implements Serializable {

  private static final long serialVersionUID = 4151059724175075256L;

  /**
   * The registered keeper ID.
   */
  public static final String KEEPER_ID = "MPerspId"; //$NON-NLS-1$

  public static final MPerspId NONE = new MPerspId( IStridable.NONE_ID, TsLibUtils.EMPTY_STRING );

  /**
   * The keeper singleton.
   * <p>
   */
  public static final IEntityKeeper<MPerspId> KEEPER =
      new AbstractEntityKeeper<>( MPerspId.class, EEncloseMode.ENCLOSES_BASE_CLASS, MPerspId.NONE ) {

        @Override
        protected void doWrite( IStrioWriter aSw, MPerspId aEntity ) {
          aSw.writeQuotedString( aEntity.perspId );
          aSw.writeSeparatorChar();
          aSw.writeQuotedString( aEntity.perspLabel );
        }

        @Override
        protected MPerspId doRead( IStrioReader aSr ) {
          String perspId = aSr.readQuotedString();
          aSr.ensureSeparatorChar();
          String perspLabel = aSr.readQuotedString();
          return new MPerspId( perspId, perspLabel );
        }
      };

  private final String perspId;

  private final String perspLabel;

  /**
   * Контруктор.
   *
   * @param aPerspId String - ИД E4 перспективы
   * @param aPerspLabel String - название перспективы
   */
  public MPerspId( String aPerspId, String aPerspLabel ) {
    perspId = aPerspId;
    perspLabel = aPerspLabel;
  }

  /**
   * Возвращает идентификатор перспективы
   *
   * @return String - идентификатор перспективы
   */
  public String perspId() {
    return perspId;
  }

  /**
   * Возвращает название перспективы.
   *
   * @return String - название перспективы
   */
  public String perspLabel() {
    return perspLabel;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return perspId + " (" + perspLabel + ')'; //$NON-NLS-1$
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( object instanceof MPerspId id ) {
      return (id.perspId.equals( this.perspId ) && id.perspLabel.equals( this.perspLabel ));
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + (perspId.hashCode() ^ (perspId.hashCode() >>> 32));
    result = PRIME * result + (perspLabel.hashCode() ^ (perspLabel.hashCode() >>> 32));
    return result;
  }

}
