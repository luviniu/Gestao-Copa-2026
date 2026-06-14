package Interface;

import Aplicacoes.OprUser;
import Aplicacoes.ErrosException;
import Aplicacoes.oprSessao;
import Objetos.Funcionario;
import Objetos.Usuario;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.IOException;

public class TelaLogin {
    @FXML private TextField identificaLogin;
    @FXML private PasswordField identificaSenha;

    @FXML private TextField cadNome;
    @FXML private TextField cadEmail;
    @FXML private TextField cadCpf;
    @FXML private PasswordField cadSenha;
    @FXML private PasswordField cadConfirmarSenha;

    @FXML private ImageView imageView;
    @FXML private AnchorPane telaLogin;
    @FXML private AnchorPane telaCadastro;

    @FXML
    public void initialize() {
        telaLogin.setVisible(true);
        telaCadastro.setVisible(false);

        javafx.application.Platform.runLater(() -> {
            try {
                Scene scene = telaLogin.getScene();
                Stage stage = (Stage) telaLogin.getScene().getWindow();

                javafx.scene.transform.Scale scaleTransform = new javafx.scene.transform.Scale(1, 1, 0, 0);
                scene.getRoot().getTransforms().add(scaleTransform);

                stage.widthProperty().addListener((observable, oldValue, newValue) -> {
                    scaleTransform.setX(stage.getWidth() / 1920.0);

                });

                stage.heightProperty().addListener((observable, oldValue, newValue) -> {
                    scaleTransform.setY(stage.getHeight() / 1080.0);

                });

                javafx.application.Platform.runLater(() -> {
                    stage.setMaximized(true);

                });

            } catch(Exception e) {
                System.out.println("Erro ao tentar abrir o telaLauncher");
                e.printStackTrace();

            }

        });

    }

    @FXML
    public void executarCadastro(ActionEvent event) throws IOException {
        String nome = cadNome.getText().trim();
        String email = cadEmail.getText().trim();
        String cpf = cadCpf.getText().trim();
        String senha = cadSenha.getText().trim();
        String confirmarSenha = cadConfirmarSenha.getText().trim();

        if(nome.isEmpty()||email.isEmpty()||cpf.isEmpty()||senha.isEmpty()||confirmarSenha.isEmpty()){
            alerta(Alert.AlertType.WARNING, "Preencha os campos!");
            return;

        }

        if(!senha.equals(confirmarSenha)){
            alerta(Alert.AlertType.ERROR,"As senhas não podem ser diferentes!");
            return;

        }

        try {
            Usuario novoUsuario = new Funcionario(nome, cpf, email, senha);
            OprUser user= OprUser.getInstancia();
            boolean cadastrou=user.registrarUsuario(novoUsuario);

            if(cadastrou){
                alerta(Alert.AlertType.INFORMATION, "Cadastro realizado com sucesso!");
                cadNome.clear();
                cadEmail.clear();
                cadCpf.clear();
                cadSenha.clear();
                cadConfirmarSenha.clear();
                abrirLogin(null);

            }

        } catch (ErrosException e) {
            alerta(Alert.AlertType.ERROR, e.getMessage());

        }

    }

    @FXML
    public void irLauncher(ActionEvent event) throws IOException {
        String login=identificaLogin.getText().trim();
        String senha=identificaSenha.getText().trim();

        if(login.isEmpty() || senha.isEmpty()) {
            alerta(Alert.AlertType.WARNING,"Preencha os campos!");
            return;

        }

        OprUser user= OprUser.getInstancia();
        Usuario usuario=user.login(login, senha);

        if(usuario==null) {
            alerta(Alert.AlertType.ERROR, "Login ou senha incorretos!");
            identificaSenha.clear();
            return;

        }

        oprSessao.setUsuario(usuario);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaLauncher.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setMaximized(false);
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            javafx.scene.transform.Scale scaleTransform = new javafx.scene.transform.Scale(1, 1, 0, 0);
            scene.getRoot().getTransforms().add(scaleTransform);

            stage.widthProperty().addListener((observable, oldValue, newValue) -> {
                scaleTransform.setX(stage.getWidth() / 1920.0);

            });

            stage.heightProperty().addListener((observable, oldValue, newValue) -> {
                scaleTransform.setY(stage.getHeight() / 1080.0);

            });

            stage.setScene(scene);
            stage.setResizable(true);
            stage.setTitle("Gestão Copa do Mundo");
            stage.show();

            javafx.application.Platform.runLater(() -> {
                stage.setMaximized(true);

            });

        } catch(Exception e) {
            System.out.println("Erro ao tentar abrir o telaLauncher");
            e.printStackTrace();

        }

    }

    private void alerta(Alert.AlertType tipo, String s) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(s);
        alert.showAndWait();

    }

    @FXML
    public void abrirCadastro(ActionEvent event){
        telaCadastro.setVisible(true);
        telaLogin.setVisible(false);

    }

    @FXML
    public void abrirLogin(ActionEvent event){
        telaLogin.setVisible(true);
        telaCadastro.setVisible(false);

    }

    // Só pra ligar o login à TelaSelecoes
    @FXML
    public void irParaSelecoes(ActionEvent event) {
        try {
            // 1. Carrega o FXML da nova tela de seleções
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaSelecoes.fxml"));
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

    // Só pra ligar o login à TelaSelecoes
    @FXML
    public void irParaPartidas(ActionEvent event) {
        try {
            // 1. Carrega o FXML da nova tela de seleções
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaPartidas.fxml"));
            Parent root = loader.load();

            // 2. Pega a janela atual (Stage) que já está aberta
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // 3. Cria a nova cena com o tamanho que você escolheu (ex: 1024x768)
            Scene scene = new Scene(root, 1024, 768);

            // Aplica o seu CSS para o design não quebrar
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            // 4. Coloca a nova cena na janela e mostra
            stage.setScene(scene);
            stage.setTitle("Gerenciamento de Partidas");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de partidas! Verifique o caminho do FXML.");
            e.printStackTrace();
        }
    }

    // Só pra ligar o login à TelaSelecoes
    @FXML
    public void irParaArbitros(ActionEvent event) {
        try {
            // 1. Carrega o FXML da nova tela de seleções
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaArbitros.fxml"));
            Parent root = loader.load();

            // 2. Pega a janela atual (Stage) que já está aberta
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // 3. Cria a nova cena com o tamanho que você escolheu (ex: 1024x768)
            Scene scene = new Scene(root, 1024, 768);

            // Aplica o seu CSS para o design não quebrar
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            // 4. Coloca a nova cena na janela e mostra
            stage.setScene(scene);
            stage.setTitle("Gerenciamento de Árbitros");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de árbitros! Verifique o caminho do FXML.");
            e.printStackTrace();
        }
    }

    // Só pra ligar o login à TelaSelecoes
    @FXML
    public void irParaIngressos(ActionEvent event) {
        try {
            // 1. Carrega o FXML da nova tela de seleções
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaIngressos.fxml"));
            Parent root = loader.load();

            // 2. Pega a janela atual (Stage) que já está aberta
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            // 3. Cria a nova cena com o tamanho que você escolheu (ex: 1024x768)
            Scene scene = new Scene(root, 1920, 1080);

            // Aplica o seu CSS para o design não quebrar
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            // 4. Coloca a nova cena na janela e mostra
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("Gerenciamento de Ingressos");
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de ingressos! Verifique o caminho do FXML.");
            e.printStackTrace();
        }
    }
}
