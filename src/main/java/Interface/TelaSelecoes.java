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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaSelecoes implements Initializable {

    // Injeção dos componentes do FXML (ligando os fx:id ao controller)
    @FXML private ComboBox<String> comboGrupo;
    @FXML private TableView<Selecao> tabelaSelecoes;
    @FXML private TableColumn<Selecao, String> colunaSelecao;
    @FXML private TableColumn<Selecao, String> colunaTecnico;
    @FXML private TableColumn<Selecao, String> colunaGrupo;

    @FXML private TextField txtSelecao;
    @FXML private TextField txtTecnico;

    // Conexão com o motor de regras de negócio
    private OprSel oprSel = OprSel.getInstancia();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializa o combobox de grupos
        ObservableList<String> grupos = FXCollections.observableArrayList(
                "Grupo A", "Grupo B", "Grupo C", "Grupo D",
                "Grupo E", "Grupo F", "Grupo G", "Grupo H"
        );
        comboGrupo.setItems(grupos);
        comboGrupo.getSelectionModel().selectFirst();

        // Vincula as colunas da tabela com os atributos da classe Selecao
        colunaSelecao.setCellValueFactory(new PropertyValueFactory<>("pais"));
        colunaTecnico.setCellValueFactory(new PropertyValueFactory<>("tecnico"));
        colunaGrupo.setCellValueFactory(new PropertyValueFactory<>("grupo"));

        // Renderiza as seleções salvas na tabela ao abrir a tela
        atualizarTabela();
    }

    // Atualiza as linhas da tabela com os dados da memória/TXT
    private void atualizarTabela() {
        ObservableList<Selecao> dados = FXCollections.observableArrayList(oprSel.getListaSelecoes());
        tabelaSelecoes.setItems(dados);
    }

    // Funções disparadas pelos botões; jogam os dados nos métodos OprSel
    @FXML
    public void handleAdicionarSelecao(ActionEvent event) {
        String pais = txtSelecao.getText();
        String tecnico = txtTecnico.getText();
        String grupo = comboGrupo.getValue();

        if (oprSel.cadastrarSelecao(pais, grupo, tecnico)) {
            atualizarTabela();
            // Limpa os campos após salvar
            txtSelecao.clear();
            txtTecnico.clear();
        } else {
            System.out.println("Erro ao cadastrar seleção. Verifique os dados.");
        }
    }

    @FXML
    public void handleExcluirSelecao(ActionEvent event) {
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada != null) {
            if (oprSel.excluirSelecao(selecionada.getPais())) {
                atualizarTabela();
            }
        } else {
            System.out.println("Selecione uma seleção na tabela para remover.");
        }
    }

    //
    @FXML
    public void handleEditarSelecao(ActionEvent event) {
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada != null) {
            // Pega os dados novos que você digitou nos inputs inferiores para atualizar a selecionada
            String novoTecnico = txtTecnico.getText();
            String novoGrupo = comboGrupo.getValue();

            if (novoTecnico != null && !novoTecnico.trim().isEmpty()) {
                if (oprSel.editarSelecao(selecionada.getPais(), novoGrupo, novoTecnico)) {
                    atualizarTabela();
                }
            }
        } else {
            System.out.println("Selecione uma linha para alterar e use os campos inferiores.");
        }
    }

    @FXML
    public void irParaJogadores(ActionEvent event) {
        // Pega a seleção que o usuário clicou na tabela
        Selecao selecionada = tabelaSelecoes.getSelectionModel().getSelectedItem();

        if (selecionada == null) {
            System.out.println("Erro: Selecione uma seleção na tabela primeiro!");
            return; // Barra a navegação se não houver seleção
        }

        try {
            // Prepara o loader para ler o FXML da tela de jogadores
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaJogadores.fxml"));
            Parent root = loader.load(); // Transforma o FXML em objetos visuais

            // Pega a instância do controller da TelaJogadores que o loader acabou de criar
            TelaJogadores controllerProximaTela = loader.getController();

            //  Injeta a seleção selecionada direto na variável do próximo controller
            controllerProximaTela.setSelecaoAtual(selecionada);

            // Segue o fluxo normal de navegação para trocar a cena
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root, 1024, 768);
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