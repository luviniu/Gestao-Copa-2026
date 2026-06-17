package Interface;

import Aplicacoes.OprSel;
import Objetos.Selecao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaSelecoes implements Initializable {

    @FXML private TableView<Selecao> tabelaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaSelecao;
    @FXML private TableColumn<Selecao, String> colunaTecnico;
    @FXML private TableColumn<Selecao, String> colunaGrupo;

    @FXML private TextField txtBuscaRapida;
    @FXML private ComboBox<String> comboFiltroGrupo;

    private OprSel oprSel = OprSel.getInstancia();
    private ObservableList<Selecao> dadosMaster = FXCollections.observableArrayList();
    private FilteredList<Selecao> dadosFiltrados;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colunaSelecao.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaTecnico.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        colunaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));

        if (comboFiltroGrupo != null) {
            comboFiltroGrupo.getItems().addAll("Todos", "Grupo A", "Grupo B", "Grupo C", "Grupo D", "Grupo E", "Grupo F", "Grupo G", "Grupo H");
            comboFiltroGrupo.setValue("Todos");
        }

        configurarFiltroDinamico();
        atualizarTabela();
    }

    private void configurarFiltroDinamico() {
        dadosFiltrados = new FilteredList<>(dadosMaster, p -> true);

        txtBuscaRapida.textProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltrosCombinados();
        });

        comboFiltroGrupo.valueProperty().addListener((observable, oldValue, newValue) -> {
            aplicarFiltrosCombinados();
        });

        tabelaSelecoes.setItems(dadosFiltrados);
    }

    private void aplicarFiltrosCombinados() {
        dadosFiltrados.setPredicate(selecao -> {
            String termoTexto = txtBuscaRapida.getText() == null ? "" : txtBuscaRapida.getText().toLowerCase().trim();
            String grupoSelecionado = comboFiltroGrupo.getValue() == null ? "Todos" : comboFiltroGrupo.getValue();

            boolean correspondeTexto = termoTexto.isEmpty() ||
                    selecao.getPais().toLowerCase().contains(termoTexto) ||
                    (selecao.getTecnico() != null && selecao.getTecnico().toLowerCase().contains(termoTexto));

            boolean correspondeGrupo = "Todos".equals(grupoSelecionado) || selecao.getGrupo().equals(grupoSelecionado);

            return correspondeTexto && correspondeGrupo;
        });
    }

    private void atualizarTabela() {
        dadosMaster.setAll(oprSel.getListaSelecoes());
        aplicarFiltrosCombinados(); // Garante que mantém o filtro ativo se a lista recarregar
        tabelaSelecoes.refresh();
    }

    @FXML
    public void handleResetarTabela(ActionEvent event) {
        txtBuscaRapida.clear();
        comboFiltroGrupo.setValue("Todos");
        atualizarTabela();
    }

    @FXML
    public void handleBuscarSelecao(ActionEvent event) {
        handleResetarTabela(event);
    }

    @FXML
    private void voltarParaLauncher(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaLauncher.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);

            if (getClass().getResource("/Interface/style.css") != null)
                scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("World Cup 2026 - Launcher");
            stage.show();
        } catch (IOException e) {
            System.out.println("Erro ao voltar para o Launcher.");
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBotaoNovaSelecao(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Seleção");
        dialog.setHeaderText("Insira as informações da nova seleção");

        TextField campoPais = new TextField();
        TextField campoTecnico = new TextField();
        ComboBox<String> comboGrupo = new ComboBox<>();
        comboGrupo.getItems().addAll("Grupo A", "Grupo B", "Grupo C", "Grupo D", "Grupo E", "Grupo F", "Grupo G", "Grupo H");
        comboGrupo.setValue("Grupo A");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("País / Seleção:"), 0, 0);
        grid.add(campoPais, 1, 0);
        grid.add(new Label("Técnico:"), 0, 1);
        grid.add(campoTecnico, 1, 1);
        grid.add(new Label("Grupo:"), 0, 2);
        grid.add(comboGrupo, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                String pais = campoPais.getText().trim();
                String tecnico = campoTecnico.getText().trim();
                String grupo = comboGrupo.getValue();

                if (pais.isEmpty() || tecnico.isEmpty()) {
                    alerta(Alert.AlertType.WARNING, "Preencha todos os campos obrigatórios!");
                    return;
                }

                if (oprSel.cadastrarSelecao(pais, grupo, tecnico)) {
                    atualizarTabela();
                    alerta(Alert.AlertType.INFORMATION, "Seleção cadastrada com sucesso!");
                } else {
                    alerta(Alert.AlertType.ERROR, "Erro ao cadastrar seleção. Verifique se ela já existe.");
                }
            }
        });
    }

    @FXML
    public void handleEditarSelecao(ActionEvent event) {
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            alerta(Alert.AlertType.WARNING, "Selecione uma seleção na tabela para editar!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Seleção");
        dialog.setHeaderText("Editando informações de: " + selecionada.getPais());

        TextField campoPais = new TextField(selecionada.getPais());
        campoPais.setDisable(true);
        TextField campoTecnico = new TextField(selecionada.getTecnico());

        ComboBox<String> comboGrupo = new ComboBox<>();
        comboGrupo.getItems().addAll("Grupo A", "Grupo B", "Grupo C", "Grupo D", "Grupo E", "Grupo F", "Grupo G", "Grupo H");
        comboGrupo.setValue(selecionada.getGrupo());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("País / Seleção:"), 0, 0);
        grid.add(campoPais, 1, 0);
        grid.add(new Label("Técnico:"), 0, 1);
        grid.add(campoTecnico, 1, 1);
        grid.add(new Label("Grupo:"), 0, 2);
        grid.add(comboGrupo, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                String novoGrupo = comboGrupo.getValue();
                String novoTecnico = campoTecnico.getText().trim();

                if (novoTecnico.isEmpty()) {
                    alerta(Alert.AlertType.WARNING, "O campo Técnico é obrigatório!");
                    return;
                }

                selecionada.setGrupo(novoGrupo);
                selecionada.setTecnico(novoTecnico);

                if (oprSel.editarSelecao(selecionada.getPais(), novoGrupo, novoTecnico)) {
                    atualizarTabela();
                    alerta(Alert.AlertType.INFORMATION, "Seleção atualizada com sucesso!");
                } else {
                    alerta(Alert.AlertType.ERROR, "Erro ao atualizar a seleção no sistema.");
                }
            }
        });
    }

    @FXML
    public void handleExcluirSelecao(ActionEvent event) {
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            alerta(Alert.AlertType.WARNING, "Selecione uma seleção para excluir!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Deseja realmente remover a seleção do(a) " + selecionada.getPais() + " e todo o seu elenco?");

        confirmacao.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                if (oprSel.excluirSelecao(selecionada.getPais())) {
                    atualizarTabela();
                    alerta(Alert.AlertType.INFORMATION, "Seleção removida com sucesso!");
                } else {
                    alerta(Alert.AlertType.ERROR, "Erro ao remover a seleção.");
                }
            }
        });
    }

    @FXML
    public void irParaJogadores(ActionEvent event) {
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            alerta(Alert.AlertType.WARNING, "Selecione uma seleção na tabela para gerenciar o elenco!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaJogadores.fxml"));
            Parent root = loader.load();

            TelaJogadores controllerProximaTela = loader.getController();
            controllerProximaTela.setSelecaoAtual(selecionada);

            Stage stage = (Stage) tabelaSelecoes.getScene().getWindow();
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Gerenciamento de Elenco - " + selecionada.getPais());
            stage.show();
        } catch (IOException e) {
            alerta(Alert.AlertType.ERROR, "Erro ao carregar a tela de jogadores.");
            e.printStackTrace();
        }
    }

    private void alerta(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}