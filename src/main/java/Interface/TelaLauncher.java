package Interface;
import Aplicacoes.oprSessao;
import Objetos.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class TelaLauncher {
    @FXML private Label labelNomeCentro;
    @FXML private Label labelCargoCentro;
    @FXML private Label labelCpfCentro;
    @FXML private Label labelNomeSidebar;
    @FXML private Label labelCargoSidebar;

    @FXML
    public void initialize() {
        Usuario logado = oprSessao.getUsuario();
        if (logado != null) {
            if (labelNomeCentro != null) labelNomeCentro.setText(logado.getNome());
            if (labelCargoCentro != null) labelCargoCentro.setText(logado.getPerfilUsuario());
            if (labelCpfCentro != null) labelCpfCentro.setText(logado.getCpf());
            if (labelNomeSidebar != null) labelNomeSidebar.setText(logado.getNome());
            if (labelCargoSidebar != null) labelCargoSidebar.setText(logado.getPerfilUsuario());

        }

    }
    @FXML
    public void realizarLogout(ActionEvent event) {
        oprSessao.encerrar();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaLogin.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("World Cup 2026 - Login");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao tentar retornar para a tela de login.");
            e.printStackTrace();

        }

    }

}