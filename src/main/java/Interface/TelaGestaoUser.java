package Interface;

import Aplicacoes.OprUser;
import Aplicacoes.ErrosException;
import Aplicacoes.oprSessao;
import Objetos.Usuario;
import Objetos.Funcionario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class TelaGestaoUser {

    @FXML private TableView<Usuario> listaUsuarios;
    @FXML private TableColumn<Usuario, String> colNome;
    @FXML private TableColumn<Usuario, String> colCargo;
    @FXML private TableColumn<Usuario, String> colCpf;

    @FXML private ToggleButton toggleNome;
    @FXML private ToggleButton toggleCpf;
    @FXML private ToggleButton toggleCargo;

    @FXML private TextField nomeSearchAdm;
    @FXML private TextField cpfSearchAdm;
    @FXML private TextField cargoSearchAdm;

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNome()));
        colCargo.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getPerfilUsuario()));
        colCpf.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getCpf()));

        nomeSearchAdm.setDisable(true);
        cpfSearchAdm.setDisable(true);
        cargoSearchAdm.setDisable(true);

        toggleNome.selectedProperty().addListener((obs, oldVal, newVal) -> {
            nomeSearchAdm.setDisable(!newVal);
            if (!newVal) nomeSearchAdm.clear();
            filtrar();

        });

        toggleCpf.selectedProperty().addListener((obs, oldVal, newVal) -> {
            cpfSearchAdm.setDisable(!newVal);
            if (!newVal) cpfSearchAdm.clear();
            filtrar();

        });

        toggleCargo.selectedProperty().addListener((obs, oldVal, newVal) -> {
            cargoSearchAdm.setDisable(!newVal);
            if (!newVal) cargoSearchAdm.clear();
            filtrar();

        });

        nomeSearchAdm.textProperty().addListener((obs, oldVal, newVal) -> filtrar());
        cpfSearchAdm.textProperty().addListener((obs, oldVal, newVal) -> filtrar());
        cargoSearchAdm.textProperty().addListener((obs, oldVal, newVal) -> filtrar());

        carregarTabela();

    }

    private void carregarTabela() {
        OprUser user = OprUser.getInstancia();
        ObservableList<Usuario> lista = FXCollections.observableArrayList(user.getUsuarios());
        listaUsuarios.setItems(lista);

    }

    private void filtrar() {
        OprUser user = OprUser.getInstancia();
        ObservableList<Usuario> todos = FXCollections.observableArrayList(user.getUsuarios());

        if (!toggleNome.isSelected() && !toggleCpf.isSelected() && !toggleCargo.isSelected()) {
            listaUsuarios.setItems(todos);
            return;

        }

        ObservableList<Usuario> filtrados = FXCollections.observableArrayList();

        String nome = toggleNome.isSelected() ? nomeSearchAdm.getText().toLowerCase().trim() : "";
        String cpf = toggleCpf.isSelected() ? cpfSearchAdm.getText().trim() : "";
        String cargo = toggleCargo.isSelected() ? cargoSearchAdm.getText().toLowerCase().trim() : "";

        for (Usuario u : todos) {
            boolean passaNome = !toggleNome.isSelected() || u.getNome().toLowerCase().contains(nome);
            boolean passaCpf = !toggleCpf.isSelected() || u.getCpf().contains(cpf);
            boolean passaCargo = !toggleCargo.isSelected() || u.getPerfilUsuario().toLowerCase().contains(cargo);

            if (passaNome && passaCpf && passaCargo) {
                filtrados.add(u);

            }

        }

        listaUsuarios.setItems(filtrados);

    }

    @FXML
    public void acaoBuscar(ActionEvent event) {
        filtrar();

    }

    @FXML
    public void acaoCadastrar(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Usuário");
        dialog.setHeaderText("Novo usuário");

        TextField campoNome = new TextField();
        TextField campoEmail = new TextField();
        TextField campoCpf = new TextField();
        PasswordField campoSenha = new PasswordField();
        PasswordField campoConfirmarSenha = new PasswordField();
        ComboBox<String> comboPerfil = new ComboBox<>();
        comboPerfil.getItems().addAll("Administrador", "Operador", "Organizador", "Arbitro");
        comboPerfil.setValue("Operador");

        TextField campoNacionalidade = new TextField();
        TextField campoExperiencia = new TextField();
        campoNacionalidade.setDisable(true);
        campoExperiencia.setDisable(true);

        comboPerfil.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isArbitro = "Arbitro".equals(newVal);
            campoNacionalidade.setDisable(!isArbitro);
            campoExperiencia.setDisable(!isArbitro);
            if (!isArbitro) {
                campoNacionalidade.clear();
                campoExperiencia.clear();

            }

        });

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(campoNome, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(campoEmail, 1, 1);
        grid.add(new Label("CPF:"), 0, 2);
        grid.add(campoCpf, 1, 2);
        grid.add(new Label("Senha:"), 0, 3);
        grid.add(campoSenha, 1, 3);
        grid.add(new Label("Confirmar Senha:"), 0, 4);
        grid.add(campoConfirmarSenha, 1, 4);
        grid.add(new Label("Perfil:"), 0, 5);
        grid.add(comboPerfil, 1, 5);
        grid.add(new Label("Nacionalidade:"), 0, 6);
        grid.add(campoNacionalidade, 1, 6);
        grid.add(new Label("Experiência:"), 0, 7);
        grid.add(campoExperiencia, 1, 7);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                String nome = campoNome.getText().trim();
                String email = campoEmail.getText().trim();
                String cpf = campoCpf.getText().trim();
                String senha = campoSenha.getText().trim();
                String confirmarSenha = campoConfirmarSenha.getText().trim();
                String perfil = comboPerfil.getValue();
                String nacionalidade = campoNacionalidade.getText().trim();
                String experiencia = campoExperiencia.getText().trim();

                if (nome.isEmpty() || email.isEmpty() || cpf.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
                    alerta(Alert.AlertType.WARNING, "Preencha todos os campos obrigatórios!");
                    return;

                }

                if (!senha.equals(confirmarSenha)) {
                    alerta(Alert.AlertType.ERROR, "As senhas não podem ser diferentes!");
                    return;

                }

                try {
                    Usuario novoUsuario;

                    if (perfil.equals("Administrador")) {
                        novoUsuario = new Objetos.Administrador(nome, cpf, email, senha);

                    } else if (perfil.equals("Operador")) {
                        novoUsuario = new Objetos.Operador(nome, cpf, email, senha);

                    } else if (perfil.equals("Organizador")) {
                        novoUsuario = new Objetos.Organizador(nome, cpf, email, senha);

                    } else {
                        novoUsuario = new Objetos.Arbitro(nome, cpf, email, senha, nacionalidade, experiencia);

                    }

                    OprUser user = OprUser.getInstancia();
                    user.registrarUsuario(novoUsuario);
                    carregarTabela();
                    alerta(Alert.AlertType.INFORMATION, "Usuário cadastrado com sucesso!");

                } catch (ErrosException e) {
                    alerta(Alert.AlertType.ERROR, e.getMessage());

                }

            }

        });

    }

    @FXML
    public void acaoEditar(ActionEvent event) {
        Usuario selecionado = listaUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            alerta(Alert.AlertType.WARNING, "Selecione um usuário para editar!");
            return;

        }

        Usuario logado = oprSessao.getUsuario();

        if (selecionado.getCpf().equals(logado.getCpf())) {
            alerta(Alert.AlertType.ERROR, "Você não pode editar seu próprio usuário!");
            return;

        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Usuário");
        dialog.setHeaderText("Editando: " + selecionado.getNome());

        TextField campoNome = new TextField(selecionado.getNome());
        TextField campoEmail = new TextField(selecionado.getEmail());
        PasswordField campoSenha = new PasswordField();
        ComboBox<String> comboPerfil = new ComboBox<>();
        comboPerfil.getItems().addAll("Administrador", "Operador", "Organizador", "Arbitro");
        comboPerfil.setValue(selecionado.getPerfilUsuario());

        TextField campoNacionalidade = new TextField();
        TextField campoExperiencia = new TextField();

        campoNacionalidade.setDisable(!selecionado.getPerfilUsuario().equals("Arbitro"));
        campoExperiencia.setDisable(!selecionado.getPerfilUsuario().equals("Arbitro"));

        comboPerfil.valueProperty().addListener((obs, oldVal, newVal) -> {
            boolean isArbitro = "Arbitro".equals(newVal);
            campoNacionalidade.setDisable(!isArbitro);
            campoExperiencia.setDisable(!isArbitro);

        });

        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(campoNome, 1, 0);
        grid.add(new Label("Email:"), 0, 1);
        grid.add(campoEmail, 1, 1);
        grid.add(new Label("Nova Senha:"), 0, 2);
        grid.add(campoSenha, 1, 2);
        grid.add(new Label("Perfil:"), 0, 3);
        grid.add(comboPerfil, 1, 3);
        grid.add(new Label("Nacionalidade:"), 0, 4);
        grid.add(campoNacionalidade, 1, 4);
        grid.add(new Label("Experiência:"), 0, 5);
        grid.add(campoExperiencia, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                String novoNome = campoNome.getText().trim();
                String novoEmail = campoEmail.getText().trim();
                String novaSenha = campoSenha.getText().trim();
                String novoPerfil = comboPerfil.getValue();
                String nacionalidade = campoNacionalidade.getText().trim();
                String experiencia = campoExperiencia.getText().trim();

                if (novoNome.isEmpty() || novoEmail.isEmpty()) {
                    alerta(Alert.AlertType.WARNING, "Nome e Email são obrigatórios!");
                    return;

                }

                if (novaSenha.isEmpty()) {
                    novaSenha = selecionado.getSenha();

                }

                OprUser user = OprUser.getInstancia();
                user.editarUser(selecionado.getCpf(), novoNome, novoEmail, novaSenha, nacionalidade, experiencia, novoPerfil, logado);
                carregarTabela();
                alerta(Alert.AlertType.INFORMATION, "Usuário editado com sucesso!");

            }

        });

    }

    @FXML
    public void acaoExcluir(ActionEvent event) {
        Usuario selecionado = listaUsuarios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            alerta(Alert.AlertType.WARNING, "Selecione um usuário para excluir!");
            return;

        }

        Usuario logado = oprSessao.getUsuario();

        if (selecionado.getCpf().equals(logado.getCpf())) {
            alerta(Alert.AlertType.ERROR, "Você não pode excluir seu próprio usuário!");
            return;

        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente excluir o usuário " + selecionado.getNome() + "?");
        confirmacao.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                OprUser user = OprUser.getInstancia();
                user.excluirUser(selecionado.getCpf(), logado);
                carregarTabela();
                alerta(Alert.AlertType.INFORMATION, "Usuário excluído com sucesso!");

            }

        });

    }

    @FXML
    public void acaoVoltar(ActionEvent event) {
        Stage stage = (Stage) listaUsuarios.getScene().getWindow();
        stage.close();

    }

    private void alerta(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();

    }

}