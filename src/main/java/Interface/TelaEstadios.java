package Interface;

import Aplicacoes.OprEst;
import Objetos.Estadio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaEstadios implements Initializable {

    @FXML private TableView<Estadio> tabelaEstadios;
    @FXML private TableColumn<Estadio, String> colunaNome;
    @FXML private TableColumn<Estadio, String> colunaLocal;
    @FXML private TableColumn<Estadio, Integer> colunaCapacidade;
    @FXML private TextField txtNome;
    @FXML private TextField txtLocal;
    @FXML private TextField txtCapacidade;

    private OprEst oprEst = new OprEst();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaLocal.setCellValueFactory(new PropertyValueFactory<>("local"));
        colunaCapacidade.setCellValueFactory(new PropertyValueFactory<>("vagas"));
      
        atualizarTabela();
    }

    // Atualiza as linhas visíveis na tabela com base na lista do OprEst
    private void atualizarTabela() {
        ObservableList<Estadio> dados = FXCollections.observableArrayList(oprEst.getListaEstadio());
        tabelaEstadios.setItems(dados);
    }

    @FXML
    public void handleAdicionarEstadio(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        String nome = txtNome.getText();
        String local = txtLocal.getText();
        String capacidadeStr = txtCapacidade.getText();

        int capacidade = 0;
        try {
            // Conversão segura do texto digitado para número inteiro
            capacidade = Integer.parseInt(capacidadeStr.trim());
        } catch (NumberFormatException e) {
            System.out.println("Erro: A capacidade digitada deve ser um número válido.");
            Toast.exibir(stageActual, "Capacidade deve ser um número válido!");
            return;
        }

        if (oprEst.cadastrarEstadio(nome, local, capacidade)) {
            atualizarTabela();

            txtNome.clear();
            txtLocal.clear();
            txtCapacidade.clear();

            System.out.println("Estádio cadastrado com sucesso!");
            Toast.exibir(stageActual, "Estádio cadastrado com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar estádio. Verifique as validações.");
            Toast.exibir(stageActual, "Erro ao cadastrar estádio. Verifique os dados.");
        }
    }

    @FXML
    public void handleRemoverEstadio(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Estadio selecionado = tabelaEstadios.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            if (oprEst.excluirEstadio(selecionado.getNome())) {
                atualizarTabela();
                System.out.println("Estádio removido com sucesso!");
                Toast.exibir(stageActual, "Estádio removido com sucesso!");
            }
        } else {
            System.out.println("Selecione um estádio da lista para remover.");
            Toast.exibir(stageActual, "Selecione um estádio da lista para remover.");
        }
    }

    @FXML
    public void handleEditarEstadio(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Estadio selecionado = tabelaEstadios.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            // 1. Pegar os dados dos campos de texto
            String novoNome = txtNome.getText();
            String novoLocal = txtLocal.getText();
            String novaCapacidadeStr = txtCapacidade.getText();

            // Se o campo do nome estiver em branco, mantém o antigo
            if (novoNome == null || novoNome.trim().isEmpty()) {
                novoNome = selecionado.getNome();
            }

            // Se o campo do local estiver em branco, mantém o antigo
            if (novoLocal == null || novoLocal.trim().isEmpty()) {
                novoLocal = selecionado.getLocal();
            }

            // Se a capacidade estiver em branco, mantém a atual
            int novaCapacidade = selecionado.getVagas();
            if (!novaCapacidadeStr.trim().isEmpty()) {
                try {
                    novaCapacidade = Integer.parseInt(novaCapacidadeStr.trim());
                } catch (NumberFormatException e) {
                    Toast.exibir(stageActual, "Nova capacidade inválida.");
                    return;
                }
            }

            // 2. Envia o NOME ANTIGO (para busca) e os NOVOS DADOS
            if (oprEst.editarEstadio(selecionado.getNome(), novoNome, novoLocal, novaCapacidade)) {
                atualizarTabela();
                tabelaEstadios.refresh(); // Garante que a linha mude na tela na hora
                System.out.println("Estádio alterado com sucesso!");
                Toast.exibir(stageActual, "Estádio alterado com sucesso!");
            }
        } else {
            Toast.exibir(stageActual, "Selecione um estádio e use os campos inferiores.");
        }
    }
}
