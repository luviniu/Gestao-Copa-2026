package Interface;

import Aplicacoes.OprJog;
import Aplicacoes.OprSel;
import Objetos.Jogador;
import Objetos.Selecao;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaJogadores implements Initializable {

    private Selecao selecaoAtual;

    // Conectando o motor de gerenciamento de jogadores injetando o Singleton do OprSel
    private OprJog oprJog = new OprJog(OprSel.getInstancia());

    @FXML private TableView<Jogador> tabelaJogadores;
    @FXML private TableColumn<Jogador, String> colunaJogador;
    @FXML private TableColumn<Jogador, String> colunaPosicao;
    @FXML private TableColumn<Jogador, Integer> colunaIdade;
    @FXML private TableColumn<Jogador, String> colunaStatus;
    @FXML private TableColumn<Jogador, Integer> colunaNumero;

    @FXML private ComboBox<String> comboPosicao;
    @FXML private ComboBox<String> comboStatus;

    @FXML private TextField txtJogador;
    @FXML private TextField txtIdade;
    @FXML private TextField txtNumero;
    @FXML private Text txtTituloElenco;

    @FXML private Button adicionarJogador; // Mapeado para trocar o texto dinamicamente

    // Variável de controle para saber se estamos editando um atleta existente
    private Jogador jogadorEmEdicao = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa opções de posições
        ObservableList<String> posicoes = FXCollections.observableArrayList(
                "Goleiro", "Zagueiro", "Meio-campista", "Atacante"
        );
        comboPosicao.setItems(posicoes);
        comboPosicao.getSelectionModel().selectFirst();

        // Inicializa opções de status do atleta
        ObservableList<String> statusOpcoes = FXCollections.observableArrayList(
                "Ativo", "Lesionado", "Suspenso"
        );
        comboStatus.setItems(statusOpcoes);
        comboStatus.getSelectionModel().selectFirst();

        // Vincula as colunas da tabela com as propriedades da classe Jogador
        colunaJogador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
    }

    // Método chamado pela TelaSelecoes para passar a seleção selecionada
    public void setSelecaoAtual(Selecao selecao) {
        this.selecaoAtual = selecao;

        // Altera o texto superior dinamicamente na tela
        txtTituloElenco.setText("Elenco - " + selecaoAtual.getPais());

        // Recarrega as linhas da tabela com os atletas desta seleção específica
        atualizarTabela();
    }

    private void atualizarTabela() {
        if (selecaoAtual != null) {
            // Pega a lista interna de jogadores daquela seleção específica
            ObservableList<Jogador> dados = FXCollections.observableArrayList(selecaoAtual.getTime());
            tabelaJogadores.setItems(dados);
            tabelaJogadores.refresh(); // Força o JavaFX a renderizar as alterações visuais instantaneamente
        }
    }

    // AÇÃO DO BOTÃO "Novo Jogador": Limpa o formulário inferior e redefine para cadastro limpo
    @FXML
    public void handleBotaoNovoJogador(ActionEvent event) {
        limparFormulario();
        txtJogador.requestFocus();
    }

    // AÇÃO DO BOTÃO "Editar Jogador": Puxa os dados da tabela para os inputs inferiores
    @FXML
    public void handleEditarJogador(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Jogador selecionado = tabelaJogadores.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            jogadorEmEdicao = selecionado; // Armazena a referência do objeto que está sendo editado

            // Popula os campos do formulário inferior
            txtJogador.setText(selecionado.getNome());
            txtJogador.setDisable(true); // Bloqueia o nome (chave de busca) para não quebrar a edição no backend
            txtIdade.setText(String.valueOf(selecionado.getIdade()));
            txtNumero.setText(String.valueOf(selecionado.getNumero()));
            comboPosicao.setValue(selecionado.getPosicao());
            comboStatus.setValue(selecionado.getStatus());

            adicionarJogador.setText("Salvar Alterações"); // Feedback visual no botão de ação
        } else {
            System.out.println("Selecione um jogador na tabela para alterar.");
            Toast.exibir(stageActual, "Selecione um jogador na tabela para alterar.");
        }
    }

    // O botão "Adicionar" agora processa tanto inserções quanto atualizações de forma inteligente
    @FXML
    public void handleAdicionarJogador(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        try {
            String nome = txtJogador.getText().trim();
            String posicao = comboPosicao.getValue();
            int idade = Integer.parseInt(txtIdade.getText().trim());
            int numero = Integer.parseInt(txtNumero.getText().trim());
            String status = comboStatus.getValue();

            if (nome.isEmpty()) {
                Toast.exibir(stageActual, "Erro: O nome do jogador não pode estar vazio.");
                return;
            }

            if (jogadorEmEdicao == null) {
                // MODO: NOVO CADASTRO
                if (oprJog.cadastrarJogador(nome, posicao, numero, idade, selecaoAtual.getPais(), status)) {
                    atualizarTabela();
                    limparFormulario();
                    Toast.exibir(stageActual, "Jogador cadastrado com sucesso!");
                } else {
                    Toast.exibir(stageActual, "Erro ao cadastrar jogador. Verifique a numeração da camisa.");
                }
            } else {
                // MODO: EDIÇÃO DE ATLETA EXISTENTE
                // Atualiza diretamente no objeto em memória para refletir na tabela
                jogadorEmEdicao.setPosicao(posicao);
                jogadorEmEdicao.setIdade(idade);
                jogadorEmEdicao.setNumero(numero);
                jogadorEmEdicao.setStatus(status);

                // Envia os dados para persistência/regras de negócio do backend
                if (oprJog.editarJogador(selecaoAtual.getPais(), jogadorEmEdicao.getNome(), posicao, numero, idade, status)) {
                    atualizarTabela();
                    limparFormulario();
                    Toast.exibir(stageActual, "Jogador atualizado com sucesso!");
                } else {
                    Toast.exibir(stageActual, "Erro ao salvar edições do jogador no sistema.");
                }
            }
        } catch (NumberFormatException e) {
            Toast.exibir(stageActual, "Erro: Preencha Idade e Número com valores numéricos válidos.");
        }
    }

    @FXML
    public void handleExcluirJogador(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Jogador selecionado = tabelaJogadores.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            if (oprJog.excluirJogador(selecaoAtual.getPais(), selecionado.getNome())) {
                atualizarTabela();
                if (jogadorEmEdicao == selecionado) {
                    limparFormulario();
                }
                Toast.exibir(stageActual, "Jogador removido com sucesso!");
            }
        } else {
            Toast.exibir(stageActual, "Selecione um jogador na tabela para remover.");
        }
    }

    private void limparFormulario() {
        jogadorEmEdicao = null;
        txtJogador.setDisable(false);
        txtJogador.clear();
        txtIdade.clear();
        txtNumero.clear();
        comboPosicao.getSelectionModel().selectFirst();
        comboStatus.getSelectionModel().selectFirst();
        adicionarJogador.setText("Adicionar");
    }

    @FXML
    public void irParaSelecoes(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaSelecoes.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1024, 768);
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setTitle("Gestão de Seleções");
            stage.show();
        } catch (IOException e) {
            System.out.println("Erro ao voltar para a tela de seleções.");
            Toast.exibir(stageActual, "Erro ao voltar para a tela de seleções.");
            e.printStackTrace();
        }
    }
}