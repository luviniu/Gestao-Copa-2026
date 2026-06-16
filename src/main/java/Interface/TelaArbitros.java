package Interface;

import java.util.List;

import Aplicacoes.OprSel;
import Aplicacoes.OprUser;
import Aplicacoes.OprArbitro;
import Aplicacoes.oprSessao;
import Objetos.Arbitro;
import Objetos.Selecao;
import Objetos.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaArbitros implements Initializable {
    @FXML private ComboBox<String> comboCategoria;
    @FXML private ComboBox<Usuario> comboUsuario;
    @FXML private ComboBox<String> comboNacionalidade;
    @FXML private TextField txtNacionalidade;
    @FXML private TextField txtExperiencia;

    @FXML private TableView<Arbitro> tabelaArbitro;
    @FXML private TableColumn<Arbitro, String> colunaNome;
    @FXML private TableColumn<Arbitro, String> colunaNacionalidade;
    @FXML private TableColumn<Arbitro, String> colunaExperiencia;
    @FXML private TableColumn<Arbitro, String> colunaCategoria;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> categoria = FXCollections.observableArrayList("Juiz", "Bandeirinha", "Auxiliar");
        comboCategoria.setItems(categoria);

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaNacionalidade.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));
        colunaExperiencia.setCellValueFactory(new PropertyValueFactory<>("experiencia"));
        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        atualizarTabela();
        carregarComboUsuarios();
        carregarComboNacionalidade();
    }

    private void atualizarTabela() {
        ObservableList<Arbitro> dados = FXCollections.observableArrayList(OprArbitro.getInstancia().getListaArbitros());
        tabelaArbitro.setItems(dados);

    }

    private void carregarComboUsuarios() {
        List<Usuario> listaDoSistema = OprUser.getInstancia().getUsuarios();
        ObservableList<Usuario> usuariosDisponiveis = FXCollections.observableArrayList();

        for (Usuario u : listaDoSistema) {
            String perfil = u.getPerfilUsuario();
            if (perfil != null && (perfil.equalsIgnoreCase("Sem cargo") || perfil.equalsIgnoreCase("Funcionario"))) {
                usuariosDisponiveis.add(u);
            }
        }
        comboUsuario.setItems(usuariosDisponiveis);
    }

    private void carregarComboNacionalidade() {
        List<Selecao> listaDoSistema = OprSel.getInstancia().getListaSelecoes();
        ObservableList<String> nacionalidadeDisponiveis = FXCollections.observableArrayList();

        for (Selecao s : listaDoSistema) {
            String perfil = s.getPais();
            if (perfil != null) {
                nacionalidadeDisponiveis.add(perfil);
            }
        }
        comboNacionalidade.setItems(nacionalidadeDisponiveis);
    }


    @FXML
    public void handleAdicionarArbitro(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        Usuario usuarioSelecionado = comboUsuario.getSelectionModel().getSelectedItem();
        String categoriaSelecionada = comboCategoria.getSelectionModel().getSelectedItem();
        String nacionalidade = comboNacionalidade.getSelectionModel().getSelectedItem();
        String experiencia = txtExperiencia.getText();

        if (usuarioSelecionado == null || categoriaSelecionada == null || nacionalidade.trim().isEmpty() || experiencia.trim().isEmpty()) {
            Toast.exibir(stageActual, "Preencha todos os campos da tela!");
            return;

        }

        boolean sucessoArbitro = OprArbitro.getInstancia().cadastrarArbitro(
                usuarioSelecionado.getNome(),
                usuarioSelecionado.getCpf(),
                usuarioSelecionado.getEmail(),
                usuarioSelecionado.getSenha(),
                nacionalidade.trim(),
                experiencia.trim(),
                categoriaSelecionada

        );

        if (sucessoArbitro) {
            atualizarTabela();
            carregarComboUsuarios();

            comboUsuario.getSelectionModel().clearSelection();
            comboCategoria.getSelectionModel().clearSelection();
            comboNacionalidade.getSelectionModel().clearSelection();
            txtExperiencia.clear();

            Toast.exibir(stageActual, "Árbitro cadastrado com sucesso!");

        } else {
            Toast.exibir(stageActual, "Erro ao cadastrar árbitro.");

        }

    }

    @FXML
    public void handleRemoverArbitro(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Arbitro selecionado = tabelaArbitro.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            if (OprArbitro.getInstancia().excluirArbitro(selecionado.getCpf(), oprSessao.getUsuario())) {
                atualizarTabela();
                carregarComboUsuarios();
                Toast.exibir(stageActual, "Árbitro removido com sucesso!");

            }

        } else {
            Toast.exibir(stageActual, "Selecione um árbitro da lista para remover.");

        }

    }

    @FXML
    public void handleEditarArbitro(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Arbitro selecionado = tabelaArbitro.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            String novaNacionalidade = comboNacionalidade.getSelectionModel().getSelectedItem();
            String novaExperiencia = txtExperiencia.getText();
            String novaCategoria = comboCategoria.getSelectionModel().getSelectedItem();

            if (novaNacionalidade == null || novaNacionalidade.trim().isEmpty()) {
                novaNacionalidade = selecionado.getNacionalidade();

            }
            if (novaExperiencia == null || novaExperiencia.trim().isEmpty()) {
                novaExperiencia = selecionado.getExperiencia();

            }
            if (novaCategoria == null || novaCategoria.trim().isEmpty()) {
                novaCategoria = selecionado.getCategoria();

            }

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

            atualizarTabela();
            tabelaArbitro.refresh();

            comboNacionalidade.getSelectionModel().clearSelection();
            txtExperiencia.clear();
            comboCategoria.getSelectionModel().clearSelection();

            Toast.exibir(stageActual, "Árbitro alterado com sucesso!");

        } else {
            Toast.exibir(stageActual, "Selecione um árbitro da tabela.");

        }

    }

}