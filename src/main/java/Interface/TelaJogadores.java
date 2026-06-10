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
        }
    }

    @FXML
    public void handleAdicionarJogador(ActionEvent event) {
        try {
            String nome = txtJogador.getText();
            String posicao = comboPosicao.getValue();
            int idade = Integer.parseInt(txtIdade.getText().trim());
            int numero = Integer.parseInt(txtNumero.getText().trim());
            String status = comboStatus.getValue();

            // Chama o motor passando o nome do país como String, conforme combinamos
            if (oprJog.cadastrarJogador(nome, posicao, numero, idade, selecaoAtual.getPais(), status)) {
                atualizarTabela();

                // Limpa apenas os inputs de texto
                txtJogador.clear();
                txtIdade.clear();
                txtNumero.clear();
            } else {
                System.out.println("Erro ao cadastrar jogador. Verifique a numeração da camisa.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Erro: Preencha Idade e Número com valores numéricos válidos.");
        }
    }

    @FXML
    public void handleExcluirJogador(ActionEvent event) {
        Jogador selecionado = tabelaJogadores.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            if (oprJog.excluirJogador(selecaoAtual.getPais(), selecionado.getNome())) {
                atualizarTabela();
            }
        } else {
            System.out.println("Selecione um jogador na tabela para remover.");
        }
    }

    @FXML
    public void handleEditarJogador(ActionEvent event) {
        Jogador selecionado = tabelaJogadores.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            try {
                String novaPosicao = comboPosicao.getValue();
                int novoNumero = Integer.parseInt(txtNumero.getText().trim());
                int novaIdade = Integer.parseInt(txtIdade.getText().trim());
                String novoStatus = comboStatus.getValue();

                if (oprJog.editarJogador(selecaoAtual.getPais(), selecionado.getNome(), novaPosicao, novoNumero, novaIdade, novoStatus)) {
                    atualizarTabela();
                }
            } catch (NumberFormatException e) {
                System.out.println("Para editar, preencha os novos valores de número e idade nos campos inferiores.");
            }
        } else {
            System.out.println("Selecione um jogador na tabela para alterar.");
        }
    }

    @FXML
    public void irParaSelecoes(ActionEvent event) {
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
            e.printStackTrace();
        }
    }
}