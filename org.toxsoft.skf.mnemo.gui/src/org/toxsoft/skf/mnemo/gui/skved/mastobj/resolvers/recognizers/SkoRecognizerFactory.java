package org.toxsoft.skf.mnemo.gui.skved.mastobj.resolvers.recognizers;

public class SkoRecognizerFactory {

  public static final SkoRecognizerFactory INSTANCE = new SkoRecognizerFactory();

  public ISkObjectRecognizer createRecognizer() {
    return null;
  }

  private SkoRecognizerFactory() {
    // nop
  }
}
