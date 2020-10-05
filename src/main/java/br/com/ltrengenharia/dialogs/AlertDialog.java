package br.com.ltrengenharia.dialogs;

import javafx.scene.control.Alert;

public class AlertDialog {
  public AlertDialog(String cabecalho, Alert.AlertType at) {
    Alert alert = new Alert(at);
    alert.setTitle("Estatisticas Instagram");
    alert.setHeaderText(cabecalho);
    alert.show();
  }
}
