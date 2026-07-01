package Interface;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import Aplicacoes.OprSel;
import Aplicacoes.OprUser;
import Aplicacoes.OprArbitro;
import Aplicacoes.oprSessao;
import Objetos.Arbitro;
import Objetos.Selecao;
import Objetos.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TelaArbitros implements Initializable {

    @FXML private TableView<Arbitro> tabelaArbitro;
    @FXML private TableColumn<Arbitro, String> colunaNome;
    @FXML private TableColumn<Arbitro, String> colunaNacionalidade;
    @FXML private TableColumn<Arbitro, String> colunaExperiencia;
    @FXML private TableColumn<Arbitro, String> colunaCategoria;

    @FXML private TextField txtBusca;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaNacionalidade.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));
        colunaExperiencia.setCellValueFactory(new PropertyValueFactory<>("experiencia"));
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        atualizarTabela("");

        txtBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            atualizarTabela(newValue);
        });
    }


    private void atualizarTabela(String termoBusca) {
        ObservableList<Arbitro> dadosCompletos = FXCollections.observableArrayList(OprArbitro.getInstancia().getListaArbitros());

        if (termoBusca == null || termoBusca.isBlank()) {
            tabelaArbitro.setItems(dadosCompletos);
            return;
        }

        ObservableList<Arbitro> dadosFiltrados = FXCollections.observableArrayList();
        String busca = termoBusca.toLowerCase().trim();

        for (Arbitro a : dadosCompletos) {
            if (a.getNome().toLowerCase().contains(busca) ||
                    a.getCategoria().toLowerCase().contains(busca) ||
                    a.getNacionalidade().toLowerCase().contains(busca)) {
                dadosFiltrados.add(a);
            }
        }
        tabelaArbitro.setItems(dadosFiltrados);
        tabelaArbitro.refresh();
    }

    private ObservableList<Usuario> obterUsuariosDisponiveis() {
        List<Usuario> listaDoSistema = OprUser.getInstancia().getUsuarios();
        ObservableList<Usuario> usuariosDisponiveis = FXCollections.observableArrayList();

        for (Usuario u : listaDoSistema) {
            String perfil = u.getPerfilUsuario();
            if (perfil != null && (perfil.equalsIgnoreCase("Sem cargo") || perfil.equalsIgnoreCase("Funcionario"))) {
                usuariosDisponiveis.add(u);
            }
        }
        return usuariosDisponiveis;
    }

    private ObservableList<String> obterNacionalidadesDisponiveis() {
        List<Selecao> listaDoSistema = OprSel.getInstancia().getListaSelecoes();
        ObservableList<String> nacionalidades = FXCollections.observableArrayList();

        for (Selecao s : listaDoSistema) {
            String pais = s.getPais();
            if (pais != null) {
                nacionalidades.add(pais);
            }
        }
        return nacionalidades;
    }

    @FXML
    public void handleAdicionarArbitro(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Novo Árbitro");
        dialog.setHeaderText("Insira as informações do novo árbitro:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<Usuario> comboUsuario = new ComboBox<>(obterUsuariosDisponiveis());
        comboUsuario.setPromptText("Selecione o Usuário");

        ComboBox<String> comboNacionalidade = new ComboBox<>(obterNacionalidadesDisponiveis());
        comboNacionalidade.setPromptText("Selecione o País");

        TextField txtExperiencia = new TextField();
        txtExperiencia.setPromptText("Ex: 5 anos");

        ComboBox<String> comboCategoria = new ComboBox<>(FXCollections.observableArrayList("Juiz", "Bandeirinha", "Auxiliar"));
        comboCategoria.setPromptText("Selecione a Categoria");

        grid.add(new Label("Usuário Base:"), 0, 0);
        grid.add(comboUsuario, 1, 0);
        grid.add(new Label("Nacionalidade:"), 0, 1);
        grid.add(comboNacionalidade, 1, 1);
        grid.add(new Label("Experiência:"), 0, 2);
        grid.add(txtExperiencia, 1, 2);
        grid.add(new Label("Categoria:"), 0, 3);
        grid.add(comboCategoria, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            Usuario usuarioSelecionado = comboUsuario.getSelectionModel().getSelectedItem();
            String nacionalidadeSelecionada = comboNacionalidade.getSelectionModel().getSelectedItem();
            String experiencia = txtExperiencia.getText();
            String categoriaSelecionada = comboCategoria.getSelectionModel().getSelectedItem();

            if (usuarioSelecionado == null || categoriaSelecionada == null ||
                    nacionalidadeSelecionada == null || experiencia.isBlank()) {
                Toast.exibir(stageActual, "Todos os campos devem ser preenchidos!");
                return;
            }

            boolean sucessoArbitro = OprArbitro.getInstancia().cadastrarArbitro(
                    usuarioSelecionado.getCpf(),
                    nacionalidadeSelecionada.trim(),
                    experiencia.trim(),
                    categoriaSelecionada,
                    oprSessao.getUsuario()
            );

            if (sucessoArbitro) {
                atualizarTabela(txtBusca.getText());
                Toast.exibir(stageActual, "Árbitro cadastrado com sucesso!");
            } else {
                Toast.exibir(stageActual, "Erro ao cadastrar árbitro.");
            }
        }
    }

    @FXML
    public void handleEditarArbitro(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Arbitro selecionado = tabelaArbitro.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            Toast.exibir(stageActual, "Selecione um árbitro da tabela para alterar!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Alterar Informações do Árbitro");
        dialog.setHeaderText("Modifique os dados abaixo para o árbitro: " + selecionado.getNome());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> comboNacionalidade = new ComboBox<>(obterNacionalidadesDisponiveis());
        comboNacionalidade.setValue(selecionado.getNacionalidade());

        TextField txtExperiencia = new TextField(selecionado.getExperiencia());

        ComboBox<String> comboCategoria = new ComboBox<>(FXCollections.observableArrayList("Juiz", "Bandeirinha", "Auxiliar"));
        comboCategoria.setValue(selecionado.getCategoria());

        grid.add(new Label("Nova Nacionalidade:"), 0, 0);
        grid.add(comboNacionalidade, 1, 0);
        grid.add(new Label("Nova Experiência:"), 0, 1);
        grid.add(txtExperiencia, 1, 1);
        grid.add(new Label("Nova Categoria:"), 0, 2);
        grid.add(comboCategoria, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String novaNacionalidade = comboNacionalidade.getValue();
            String novaExperiencia = txtExperiencia.getText();
            String novaCategoria = comboCategoria.getValue();

            if (novaNacionalidade == null || novaNacionalidade.trim().isEmpty()) novaNacionalidade = selecionado.getNacionalidade();
            if (novaExperiencia == null || novaExperiencia.trim().isEmpty()) novaExperiencia = selecionado.getExperiencia();
            if (novaCategoria == null || novaCategoria.trim().isEmpty()) novaCategoria = selecionado.getCategoria();

            OprUser.getInstancia().editarUser(
                    selecionado.getCpf(),
                    selecionado.getNome(),
                    selecionado.getEmail(),
                    selecionado.getSenha(),
                    novaNacionalidade.trim(),
                    novaExperiencia.trim(),
                    novaCategoria,
                    "Arbitro",
                    oprSessao.getUsuario()

            );
            OprUser.getInstancia().editarArbitro(
                    selecionado.getCpf(),
                    novaNacionalidade.trim(),
                    novaExperiencia,
                    novaCategoria,
                    oprSessao.getUsuario()

            );

            atualizarTabela(txtBusca.getText());
            tabelaArbitro.getSelectionModel().clearSelection();
            Toast.exibir(stageActual, "Árbitro alterado com sucesso!");
        }
    }

    @FXML
    private void voltarParaLauncher(ActionEvent event) {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/Interface/TelaLauncher.fxml")
            );
            javafx.scene.Parent root = loader.load();
            javafx.stage.Stage stage = (javafx.stage.Stage)
                    ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            javafx.scene.Scene scene = new javafx.scene.Scene(root, 1920, 1080);

            if (getClass().getResource("/Interface/style.css") != null)
                scene.getStylesheets().add(
                        getClass().getResource("/Interface/style.css").toExternalForm()
                );

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("World Cup 2026 - Launcher");
            stage.show();
        } catch (java.io.IOException e) {
            System.out.println("Erro ao voltar para o Launcher.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRemoverArbitro(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Arbitro selecionado = tabelaArbitro.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            Toast.exibir(stageActual, "Selecione um árbitro da lista para remover.");
            return;
        }

        OprUser.getInstancia().rebaixarParaFuncionario(selecionado.getCpf(), oprSessao.getUsuario());
        atualizarTabela(txtBusca.getText());
        tabelaArbitro.getSelectionModel().clearSelection();
        Toast.exibir(stageActual, "Árbitro removido com sucesso!");
    }
}