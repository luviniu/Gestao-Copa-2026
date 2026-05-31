package Interface;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaSelecoes implements Initializable {

    @FXML private ComboBox<String> comboGrupo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Criando as opções de grupos da Copa (A até H)
        ObservableList<String> grupos = FXCollections.observableArrayList(
                "Grupo A", "Grupo B", "Grupo C", "Grupo D",
                "Grupo E", "Grupo F", "Grupo G", "Grupo H"
        );

        // Coloca a lista dentro do ComboBox
        comboGrupo.setItems(grupos);

        // Deixa o Grupo A selecionado por padrão
        comboGrupo.getSelectionModel().selectFirst();
    }

    // Só pra ligar o TelaSeleoces à TelaJogadores
    @FXML
    public void irParaJogadores(ActionEvent event) {
        try {
            // 1. Carrega o FXML da nova tela de seleções
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaJogadores.fxml"));
            Parent root = loader.load();

            // 2. Pega a janela atual (Stage) que já está aberta
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // 3. Cria a nova cena com o tamanho que você escolheu (ex: 1024x768)
            Scene scene = new Scene(root, 1024, 768);

            // Aplica o seu CSS para o design não quebrar
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            // 4. Coloca a nova cena na janela e mostra
            stage.setScene(scene);
            stage.setTitle("Gerenciamento de Seleções");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de seleções! Verifique o caminho do FXML.");
            e.printStackTrace();
        }
    }
}