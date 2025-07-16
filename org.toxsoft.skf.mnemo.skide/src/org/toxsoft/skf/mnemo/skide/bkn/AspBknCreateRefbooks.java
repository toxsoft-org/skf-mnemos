package org.toxsoft.skf.mnemo.skide.bkn;

import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.uskat.core.connection.*;

public class AspBknCreateRefbooks
    extends AbstractSingleActionSetProvider {

  private final ISkConnection skConn;

  public AspBknCreateRefbooks( ISkConnection aSkConn ) {
    super( TsActionDef.ofPush2( "actBknRefbooks", "Справочники", "Создание справочников для Байконура",
        ICONID_USER_ACTION ) );
    skConn = aSkConn;
  }

  @Override
  public void run() {
    // BknRefbookGenerator.createAiDecorationRefbook( skConn );
    // BknRefbookGenerator.createRevrsibleEngineStateRefbook( skConn );
    BknRefbookGenerator.createIrrevrsibleEngineStateRefbook( skConn );
    BknRefbookGenerator.createButtonImagesRefbook( skConn );
  }

}
