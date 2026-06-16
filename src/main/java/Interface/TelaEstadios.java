package Interface;

import Aplicacoes.OprEst;
import Objetos.Estadio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class TelaEstadios implements Initializable {

    @FXML private TableView<Estadio> tabelaEstadios;
    @FXML private TableColumn<Estadio, String> colunaNome;
    @FXML private TableColumn<Estadio, String> colunaLocal;
    @FXML private TableColumn<Estadio, Integer> colunaCapacidade;

    // Campo de busca em tempo real injetado do FXML
    @FXML private TextField txtBusca;

    private OprEst oprEst = OprEst.getInstancia();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colunaCapacidade.setCellValueFactory(new PropertyValueFactory<>("vagas"));

        atualizarTabela("");

        // O MÉTODO FODA DA BARRA DE BUSCA EM TEMPO REAL:
        txtBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            atualizarTabela(newValue);
        });
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

    // Atualiza a tabela aplicando um filtro dinâmico na lista do OprEst
    private void atualizarTabela(String termoBusca) {
        ObservableList<Estadio> dadosCompletos = FXCollections.observableArrayList(oprEst.getListaEstadio());

        if (termoBusca == null || termoBusca.isBlank()) {
            tabelaEstadios.setItems(dadosCompletos);
            tabelaEstadios.refresh();
            return;

        }

        ObservableList<Estadio> dadosFiltrados = FXCollections.observableArrayList();
        String busca = termoBusca.toLowerCase().trim();

        for (Estadio e : dadosCompletos) {
            if (e.getNome().toLowerCase().contains(busca) || e.getLocal().toLowerCase().contains(busca)) {
                dadosFiltrados.add(e);

            }

        }
        tabelaEstadios.setItems(dadosFiltrados);
        tabelaEstadios.refresh();

    }

    @FXML
    public void handleAdicionarEstadio(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Novo Estádio");
        dialog.setHeaderText("Insira as especificações do estádio:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        TextField txtNomePop = new TextField();
        txtNomePop.setPromptText("Ex: Maracanã");
        TextField txtLocalPop = new TextField();
        txtLocalPop.setPromptText("Ex: Rio de Janeiro");
        TextField txtCapacidadePop = new TextField();
        txtCapacidadePop.setPromptText("Ex: 78000");

        grid.add(new Label("Nome do Estádio:"), 0, 0);
        grid.add(txtNomePop, 1, 0);
        grid.add(new Label("Local / Cidade:"), 0, 1);
        grid.add(txtLocalPop, 1, 1);
        grid.add(new Label("Capacidade Máxima:"), 0, 2);
        grid.add(txtCapacidadePop, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String nome = txtNomePop.getText();
            String local = txtLocalPop.getText();
            String capacidadeStr = txtCapacidadePop.getText();

            if (nome.isBlank() || local.isBlank() || capacidadeStr.isBlank()) {
                Toast.exibir(stageActual, "Todos os campos devem ser preenchidos!");
                return;
            }

            int capacidade;
            try {
                capacidade = Integer.parseInt(capacidadeStr.trim());
                if (capacidade <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                Toast.exibir(stageActual, "A capacidade deve ser um número inteiro positivo!");
                return;
            }

            if (oprEst.cadastrarEstadio(nome, local, capacidade)) {
                atualizarTabela(txtBusca.getText());
                Toast.exibir(stageActual, "Estádio cadastrado com sucesso!");
            } else {
                Toast.exibir(stageActual, "Erro ao cadastrar estádio. Verifique duplicidade.");
            }
        }
    }

    // =========================================================================
    // POP-UP UNIFICADO: EDITAR ESTÁDIO
    // =========================================================================
    @FXML
    public void handleEditarEstadio(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Estadio selecionado = tabelaEstadios.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            Toast.exibir(stageActual, "Selecione um estádio da tabela para alterar!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Alterar Informações do Estádio");
        dialog.setHeaderText("Modifique os dados abaixo para o estádio: " + selecionado.getNome());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 100, 10, 10));

        // Já inicia preenchido com os dados que estão salvos na linha selecionada
        TextField txtNomePop = new TextField(selecionado.getNome());
        TextField txtLocalPop = new TextField(selecionado.getLocal());
        TextField txtCapacidadePop = new TextField(String.valueOf(selecionado.getVagas()));

        grid.add(new Label("Novo Nome:"), 0, 0);
        grid.add(txtNomePop, 1, 0);
        grid.add(new Label("Novo Local:"), 0, 1);
        grid.add(txtLocalPop, 1, 1);
        grid.add(new Label("Nova Capacidade:"), 0, 2);
        grid.add(txtCapacidadePop, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String novoNome = txtNomePop.getText().isBlank() ? selecionado.getNome() : txtNomePop.getText();
            String novoLocal = txtLocalPop.getText().isBlank() ? selecionado.getLocal() : txtLocalPop.getText();

            int novaCapacidade = selecionado.getVagas();
            if (!txtCapacidadePop.getText().trim().isEmpty()) {
                try {
                    novaCapacidade = Integer.parseInt(txtCapacidadePop.getText().trim());
                    if (novaCapacidade <= 0) throw new NumberFormatException();
                } catch (NumberFormatException e) {
                    Toast.exibir(stageActual, "Capacidade inválida.");
                    return;
                }
            }

            if (oprEst.editarEstadio(selecionado.getNome(), novoNome, novoLocal, novaCapacidade)) {
                atualizarTabela(txtBusca.getText());
                tabelaEstadios.getSelectionModel().clearSelection();
                Toast.exibir(stageActual, "Estádio alterado com sucesso!");
            } else {
                Toast.exibir(stageActual, "Erro ao atualizar dados.");
            }
        }
    }

    // =========================================================================
    // EXCLUIR ESTÁDIO
    // =========================================================================
    @FXML
    public void handleRemoverEstadio(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Estadio selecionado = tabelaEstadios.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            if (oprEst.excluirEstadio(selecionado.getNome())) {
                atualizarTabela(txtBusca.getText());
                tabelaEstadios.getSelectionModel().clearSelection();
                Toast.exibir(stageActual, "Estádio removido com sucesso!");
            }
        } else {
            Toast.exibir(stageActual, "Selecione um estádio da lista para remover.");
        }
    }
}