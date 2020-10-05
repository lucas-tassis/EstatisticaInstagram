package br.com.ltrengenharia.dialogs;

import javafx.application.Platform;

public class Dialogs {
  private static TextDialog tDialog;
  
  public static void showErro(final String s) {
    Platform.runLater(new Runnable() {
          public void run() {}
        });
  }
  
  public static void showInformacao(final String s) {
    Platform.runLater(new Runnable() {
          public void run() {}
        });
  }
  
  public static String showTextDialog(String s) {
    Platform.runLater(new Runnable() {
          public void run() {
            Dialogs.tDialog = new TextDialog("Entre com o crecebido por SMS");
          }
        });
    return tDialog.getMensagem();
  }
}
