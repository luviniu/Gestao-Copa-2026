package Interface;

import Aplicacoes.OprSel;
import Objetos.Selecao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaSelecoes implements Initializable {

    @FXML private ComboBox<String> comboGrupo;
    @FXML private TableView<Selecao> tabelaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaSelecao;
    @FXML private TableColumn<Selecao, String> colunaTecnico;
    @FXML private TableColumn<Selecao, String> colunaGrupo;

    @FXML private TextField txtSelecao;
    @FXML private TextField txtTecnico;
    @FXML private Button adicionarSelecao; // Mapeado o botão do popup para mudar o texto dinamicamente

    private OprSel oprSel = OprSel.getInstancia();

    // Variável para controlar se estamos editando uma seleção ou criando uma nova
    private Selecao selecaoEmEdicao = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> grupos = FXCollections.observableArrayList(
                "Grupo A", "Grupo B", "Grupo C", "Grupo D",
                "Grupo E", "Grupo F", "Grupo G", "Grupo H"
        );
        comboGrupo.setItems(grupos);
        comboGrupo.getSelectionModel().selectFirst();

        colunaSelecao.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaTecnico.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        colunaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));

        atualizarTabela();
    }

    private void atualizarTabela() {
        ObservableList<Selecao> dados = FXCollections.observableArrayList(oprSel.getListaSelecoes());
        tabelaSelecoes.setItems(dados);
        tabelaSelecoes.refresh();
    }

    // AÇÃO DO BOTÃO "Nova Seleção": Prepara os campos para um novo cadastro limpo
    @FXML
    public void handleBotaoNovaSelecao(ActionEvent event) {
        selecaoEmEdicao = null;
        txtSelecao.setDisable(false); // Permite digitar o país em novos cadastros
        txtSelecao.clear();
        txtTecnico.clear();
        comboGrupo.getSelectionModel().selectFirst();
        adicionarSelecao.setText("Adicionar"); // Garante o texto original do botão
        txtSelecao.requestFocus(); // Joga o foco do teclado para o primeiro input
    }

    // AÇÃO DO BOTÃO "Alterar Informações do Elenco": Puxa os dados da tabela para os inputs
    @FXML
    public void handleEditarSelecao(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada != null) {
            selecaoEmEdicao = selecionada; // Guarda a referência de quem estamos editando

            // Popula os inputs inferiores com os dados atuais da seleção
            txtSelecao.setText(selecionada.getPais());
            txtSelecao.setDisable(true); // Opcional: trava o ID/País para não mudar a chave primária na edição
            txtTecnico.setText(selecionada.getTecnico());
            comboGrupo.setValue(selecionada.getGrupo());

            adicionarSelecao.setText("Salvar Alterações"); // Muda o texto para dar feedback visual
            txtTecnico.requestFocus();
        } else {
            Toast.exibir(stageActual, "Selecione uma linha na tabela para alterar.");
        }
    }

    // O botão "Adicionar" (ou "Salvar Alterações") agora decide de forma inteligente o que fazer
    @FXML
    public void handleAdicionarSelecao(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        String pais = txtSelecao.getText().trim();
        String tecnico = txtTecnico.getText().trim();
        String grupo = comboGrupo.getValue();

        if (pais.isEmpty() || tecnico.isEmpty()) {
            Toast.exibir(stageActual, "Preencha todos os campos!");
            return;
        }

        if (selecaoEmEdicao == null) {
            // MODO: NOVO CADASTRO
            if (oprSel.cadastrarSelecao(pais, grupo, tecnico)) {
                atualizarTabela();
                limparFormulario();
                Toast.exibir(stageActual, "Seleção cadastrada com sucesso!");
            } else {
                Toast.exibir(stageActual, "Erro ao cadastrar seleção. Verifique se já existe.");
            }
        } else {
            // MODO: EDIÇÃO DE SELEÇÃO EXISTENTE
            if (oprSel.editarSelecao(selecaoEmEdicao.getPais(), grupo, tecnico)) {
                atualizarTabela();
                limparFormulario();
                Toast.exibir(stageActual, "Seleção atualizada com sucesso!");
            } else {
                Toast.exibir(stageActual, "Erro ao atualizar informações da seleção.");
            }
        }
    }

    @FXML
    public void handleExcluirSelecao(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada != null) {
            if (oprSel.excluirSelecao(selecionada.getPais())) {
                atualizarTabela();
                if (selecaoEmEdicao == selecionada) {
                    limparFormulario();
                }
                Toast.exibir(stageActual, "Seleção removida com sucesso!");
            }
        } else {
            Toast.exibir(stageActual, "Selecione uma seleção na tabela para remover.");
        }
    }

    private void limparFormulario() {
        selecaoEmEdicao = null;
        txtSelecao.setDisable(false);
        txtSelecao.clear();
        txtTecnico.clear();
        comboGrupo.getSelectionModel().selectFirst();
        adicionarSelecao.setText("Adicionar");
    }

    @FXML
    public void irParaJogadores(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            Toast.exibir(stageActual, "Erro: Selecione uma seleção na tabela primeiro!");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaJogadores.fxml"));
            Parent root = loader.load();

            TelaJogadores controllerProximaTela = loader.getController();
            controllerProximaTela.setSelecaoAtual(selecionada);

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1920, 1080);
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Gerenciamento de Jogadores - " + selecionada.getPais());
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir a tela de jogadores! Verifique o caminho do FXML.");
            e.printStackTrace();
        }
    }
}