package Interface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaIngressos implements Initializable {

    @FXML
    private ComboBox<String> comboPartida;

    @FXML
    private ComboBox<String> comboCategoria;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private Button btnVender;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboCategoria.getItems().addAll(
                "Normal",
                "VIP",
                "Meia"
        );
        System.out.println("TelaIngressos carregada!");
    }
    @FXML
    private void venderIngresso() {
        String categoria = comboCategoria.getValue();
        String quantidade = txtQuantidade.getText();

        System.out.println("Categoria: " + categoria);
        System.out.println("Quantidade: " + quantidade);
    }
}