package br.com.ltrengenharia.dialogs;

import java.util.Optional;
import javafx.scene.control.TextInputDialog;

public class TextDialog {
  private String mensagem;
  
  public String getMensagem() {
    return this.mensagem;
  }
  
  public TextDialog(String cabecalho) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Estatisticas Instagram");
    dialog.setHeaderText(cabecalho);
    Optional<String> result = dialog.showAndWait();
    if (result.isPresent())
      this.mensagem = result.get(); 
  }
}
