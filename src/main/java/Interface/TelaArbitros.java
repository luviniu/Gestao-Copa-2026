package Interface;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaArbitros implements Initializable {
    @FXML
    private ComboBox<String> comboCategoria;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Criando as opções de posição
        ObservableList<String> posicoes = FXCollections.observableArrayList(
                "Juiz", "Bandeirinha", "Auxiliar"
        );

        // Coloca a lista dentro do ComboBox
        comboCategoria.setItems(posicoes);

    }
}
