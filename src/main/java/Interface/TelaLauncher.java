package Interface;

import Aplicacoes.oprSessao;
import Objetos.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
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

    @FXML private AnchorPane painelInicial;
    @FXML private AnchorPane partidaPanelAdm;
    @FXML private AnchorPane desempenhoPanelAdm;
    @FXML private AnchorPane buttonsAdmin;

    @FXML private Button btnPerfilInicio;
    @FXML private Button btnPartidasAdm;
    @FXML private Button buttonGestaoUsr;
    @FXML private Button btnDesempenhoAdm;

    private static Stage janelaGestaoAtiva;

    @FXML
    public void initialize() {
        Usuario logado = oprSessao.getUsuario();

        if (logado != null) {
            if (labelNomeCentro != null) labelNomeCentro.setText(logado.getNome());
            if (labelCargoCentro != null) labelCargoCentro.setText(logado.getPerfilUsuario());
            if (labelCpfCentro != null) labelCpfCentro.setText(logado.getCpf());
            if (labelNomeSidebar != null) labelNomeSidebar.setText(logado.getNome());
            if (labelCargoSidebar != null) labelCargoSidebar.setText(logado.getPerfilUsuario());

            voltarInicio(null);

            String cargo = logado.getPerfilUsuario().toUpperCase();

            if (cargo.equals("ADMINISTRADOR")) {
                buttonsAdmin.setVisible(true);

            } else {
                buttonsAdmin.setVisible(false);

            }

        }

    }

    private void esconderTodosOsPaineis() {
        if (painelInicial != null) painelInicial.setVisible(false);
        if (partidaPanelAdm != null) partidaPanelAdm.setVisible(false);
        if (desempenhoPanelAdm != null) desempenhoPanelAdm.setVisible(false);

    }

    @FXML
    public void voltarInicio(ActionEvent event) {
        esconderTodosOsPaineis();
        if (painelInicial != null) {
            painelInicial.setVisible(true);
            painelInicial.toFront();

        }

    }

    @FXML
    public void mostrarPainelPartidas(ActionEvent event) {
        esconderTodosOsPaineis();
        if (partidaPanelAdm != null) {
            partidaPanelAdm.setVisible(true);
            partidaPanelAdm.toFront();

        }

    }

    @FXML
    public void mostrarPainelDesempenho(ActionEvent event) {
        esconderTodosOsPaineis();
        if (desempenhoPanelAdm != null) {
            desempenhoPanelAdm.setVisible(true);
            desempenhoPanelAdm.toFront();

        }

    }

    @FXML
    public void abrirGestaoUsuarios(ActionEvent event) {
        try {
            if (janelaGestaoAtiva != null && janelaGestaoAtiva.isShowing()) {
                janelaGestaoAtiva.toFront();
                return;

            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaGestaoUser.fxml"));
            Parent root = loader.load();

            janelaGestaoAtiva = new Stage();
            Scene scene = new Scene(root, 800, 600);

            if (getClass().getResource("/Interface/style.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            }

            janelaGestaoAtiva.setScene(scene);
            janelaGestaoAtiva.setTitle("Gestão de Usuários - Admin");
            janelaGestaoAtiva.setResizable(false);
            janelaGestaoAtiva.show();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @FXML
    public void realizarLogout(ActionEvent event) {
        if (janelaGestaoAtiva != null && janelaGestaoAtiva.isShowing()) {
            janelaGestaoAtiva.close();
            janelaGestaoAtiva = null;

        }

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
            e.printStackTrace();

        }

    }

}