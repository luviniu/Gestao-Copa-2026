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
    @FXML private AnchorPane partidaPanelOrg;
    @FXML private AnchorPane arbitroPanelOrg;
    @FXML private AnchorPane selecaoPanelOrg;
    @FXML private AnchorPane estadioPanelOrg;
    @FXML private AnchorPane ingressoPanelOp;
    @FXML private AnchorPane partidaPanelArb;

    @FXML private AnchorPane buttonsAdmin;
    @FXML private AnchorPane buttonsOrganizador;
    @FXML private AnchorPane buttonsOperador;
    @FXML private AnchorPane buttonsArbitro;

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

            if (buttonsAdmin != null) buttonsAdmin.setVisible(false);
            if (buttonsOrganizador != null) buttonsOrganizador.setVisible(false);
            if (buttonsOperador != null) buttonsOperador.setVisible(false);
            if (buttonsArbitro != null) buttonsArbitro.setVisible(false);

            switch (cargo) {
                case "ADMINISTRADOR":
                    if (buttonsAdmin != null) buttonsAdmin.setVisible(true);
                    break;
                case "ORGANIZADOR":
                    if (buttonsOrganizador != null) buttonsOrganizador.setVisible(true);
                    break;
                case "OPERADOR":
                    if (buttonsOperador != null) buttonsOperador.setVisible(true);
                    break;
                case "ÁRBITRO":
                case "ARBITRO":
                    if (buttonsArbitro != null) buttonsArbitro.setVisible(true);
                    break;
                default:
                    System.out.println("Cargo não identificado: " + cargo);
                    break;

            }

        }

    }

    private void esconderTodosOsPaineis() {
        if (painelInicial != null) painelInicial.setVisible(false);
        if (partidaPanelAdm != null) partidaPanelAdm.setVisible(false);
        if (desempenhoPanelAdm != null) desempenhoPanelAdm.setVisible(false);
        if (partidaPanelOrg != null) partidaPanelOrg.setVisible(false);
        if (arbitroPanelOrg != null) arbitroPanelOrg.setVisible(false);
        if (selecaoPanelOrg != null) selecaoPanelOrg.setVisible(false);
        if (estadioPanelOrg != null) estadioPanelOrg.setVisible(false);
        if (ingressoPanelOp != null) ingressoPanelOp.setVisible(false);
        if (partidaPanelArb != null) partidaPanelArb.setVisible(false);

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
    public void mostrarPainelPartidasOrg(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaPartidas.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);

            if (getClass().getResource("/Interface/style.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            }

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Gerenciamento de Partidas");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de partidas!");
            e.printStackTrace();

        }

    }

    @FXML
    public void mostrarPainelArbitrosOrg(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaArbitros.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);

            if (getClass().getResource("/Interface/style.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            }

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Gerenciamento de Árbitros");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de árbitros!");
            e.printStackTrace();

        }

    }

    @FXML
    public void mostrarPainelSelecaoOrg(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaSelecoes.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);

            if (getClass().getResource("/Interface/style.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            }

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Gerenciamento de Seleções");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de seleções!");
            e.printStackTrace();

        }

    }

    @FXML
    public void mostrarPainelEstadiosOrg(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaEstadios.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);

            if (getClass().getResource("/Interface/style.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            }

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Gerenciamento de Estádios");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de estádios!");
            e.printStackTrace();

        }

    }

    @FXML
    public void mostrarPainelIngressosOp(ActionEvent event) {
        esconderTodosOsPaineis();
        if (ingressoPanelOp != null) {
            ingressoPanelOp.setVisible(true);
            ingressoPanelOp.toFront();

        }

    }

    @FXML
    public void irParaTelaIngressos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaIngressos.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);

            if (getClass().getResource("/Interface/style.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            }

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Gerenciamento de Ingressos");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de ingressos!");
            e.printStackTrace();

        }

    }

    @FXML
    public void mostrarPainelPartidasArb(ActionEvent event) {
        esconderTodosOsPaineis();
        if (partidaPanelArb != null) {
            partidaPanelArb.setVisible(true);
            partidaPanelArb.toFront();

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