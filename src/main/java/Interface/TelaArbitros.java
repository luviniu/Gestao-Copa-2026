package Interface;

import java.util.List;
import Aplicacoes.OprUser;
import Aplicacoes.OprArbitro;
import Objetos.Arbitro;
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
    @FXML private TextField txtNacionalidade; // Se você tiver um campo de texto para nacionalidade na tela
    @FXML private TextField txtExperiencia;

    @FXML private TableView<Arbitro> tabelaArbitro;
    @FXML private TableColumn<Arbitro, String> colunaNome;
    @FXML private TableColumn<Arbitro, String> colunaNacionalidade;
    @FXML private TableColumn<Arbitro, String> colunaExperiencia;
    @FXML private TableColumn<Arbitro, String> colunaCategoria;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configuracao do ComboBox de Categorias
        ObservableList<String> categoria = FXCollections.observableArrayList("Juiz", "Bandeirinha", "Auxiliar");
        comboCategoria.setItems(categoria);

        // Configuracao do ComBox de Usuarios
        List<Usuario> listaDoSistema = OprUser.getInstancia().getUsuarios();
        ObservableList<Usuario> usuariosDisponiveis = FXCollections.observableArrayList();

        // Loop corrigido e mais seguro:
        for (Usuario u : listaDoSistema) {
            String perfil = u.getPerfilUsuario();

            // Se o perfil for "Sem cargo" ou "Funcionario", ele DEVE aparecer para ser promovido
            if (perfil != null && (perfil.equalsIgnoreCase("Sem cargo"))) {
                usuariosDisponiveis.add(u);
            }
        }
        comboUsuario.setItems(usuariosDisponiveis);

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaNacionalidade.setCellValueFactory(new PropertyValueFactory<>("nacionalidade"));
        colunaExperiencia.setCellValueFactory(new PropertyValueFactory<>("experiencia"));

        colunaCategoria.setCellValueFactory(new PropertyValueFactory<>("categoria"));

        atualizarTabela();
        carregarComboUsuarios();
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

    @FXML
    public void handleAdicionarArbitro(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

        System.out.println("=== CLIQUE DETECTADO ==="); // PRINT 1

        Usuario usuarioSelecionado = comboUsuario.getSelectionModel().getSelectedItem();
        String categoriaSelecionada = comboCategoria.getSelectionModel().getSelectedItem();
        String nacionalidade = txtNacionalidade.getText();
        String experiencia = txtExperiencia.getText();

        System.out.println("Usuario: " + usuarioSelecionado);
        System.out.println("Categoria: " + categoriaSelecionada);
        System.out.println("Nacionalidade: " + nacionalidade);
        System.out.println("Experiencia: " + experiencia);

        if (usuarioSelecionado == null || categoriaSelecionada == null ||
                nacionalidade.trim().isEmpty() || experiencia.trim().isEmpty()) {
            System.out.println("⚠️ PAROU NA VALIDAÇÃO: Algum campo veio nulo ou vazio!");
            Toast.exibir(stageActual, "Preencha todos os campos da tela!");
            return;
        }

        System.out.println("-> Passou da validação. Chamando o OprArbitro..."); // PRINT 2

        boolean sucessoArbitro = OprArbitro.getInstancia().cadastrarArbitro(
                usuarioSelecionado.getNome(),
                usuarioSelecionado.getCpf(),
                usuarioSelecionado.getEmail(),
                usuarioSelecionado.getSenha(),
                nacionalidade.trim(),
                experiencia.trim(),
                categoriaSelecionada
        );

        System.out.println("Resultado do OprArbitro: " + sucessoArbitro); // PRINT 3

        if (sucessoArbitro) {
            OprUser.getInstancia().editarUser(
                    usuarioSelecionado.getCpf(),
                    usuarioSelecionado.getNome(),
                    usuarioSelecionado.getEmail(),
                    usuarioSelecionado.getSenha(),
                    nacionalidade.trim(),
                    experiencia.trim(),
                    categoriaSelecionada,
                    "Arbitro",
                    usuarioSelecionado
            );

            atualizarTabela();
            carregarComboUsuarios();

            comboUsuario.getSelectionModel().clearSelection();
            comboCategoria.getSelectionModel().clearSelection();
            txtNacionalidade.clear();
            txtExperiencia.clear();

            System.out.println("🎉 SUCESSO ABSOLUTO!");
            Toast.exibir(stageActual, "Árbitro cadastrado com sucesso!");
        } else {
            System.out.println("❌ O OprArbitro recusou o cadastro (provavelmente já existe um árbitro com esse nome).");
            Toast.exibir(stageActual, "Erro ao cadastrar árbitro.");
        }
    }

    @FXML
    public void handleRemoverArbitro(ActionEvent event) {
        Stage stageActual = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        Arbitro selecionado = tabelaArbitro.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            // Remove utilizando o CPF oculto do objeto selecionado da linha
            if (OprArbitro.getInstancia().excluirArbitro(selecionado.getCpf())) {
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

        // CORRIGIDO: Ajustado para o plural 'tabelaArbitros' igual ao FXML
        Arbitro selecionado = tabelaArbitro.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            String novaNacionalidade = txtNacionalidade.getText();
            String novaExperiencia = txtExperiencia.getText();
            String novaCategoria = comboCategoria.getSelectionModel().getSelectedItem();

            // Se os campos de texto ou o combo estiverem vazios, mantém os valores antigos do objeto
            if (novaNacionalidade == null || novaNacionalidade.trim().isEmpty()) {
                novaNacionalidade = selecionado.getNacionalidade();
            }

            if (novaExperiencia == null || novaExperiencia.trim().isEmpty()) {
                novaExperiencia = selecionado.getExperiencia();
            }

            if (novaCategoria == null || novaCategoria.trim().isEmpty()) {
                novaCategoria = selecionado.getCategoria();
            }

            // CHAMADA CORRIGIDA: Agora com os 9 parâmetros exatos que o seu OprUser exige!
            OprUser.getInstancia().editarUser(
                    selecionado.getCpf(),
                    selecionado.getNome(),
                    selecionado.getEmail(),
                    selecionado.getSenha(),
                    novaNacionalidade.trim(),
                    novaExperiencia.trim(),
                    novaCategoria, // 1. Passa a categoria nova (ou a antiga)
                    "Arbitro",     // 2. Passa o novoPerfil que o método pede
                    selecionado    // 3. Passa o usuário para validação (lembre de comentar a trava no OprUser se necessário)
            );

            // Atualiza e limpa a tela
            atualizarTabela();
            tabelaArbitro.refresh();

            txtNacionalidade.clear();
            txtExperiencia.clear();
            comboCategoria.getSelectionModel().clearSelection();

            Toast.exibir(stageActual, "Árbitro alterado com sucesso!");
        } else {
            Toast.exibir(stageActual, "Selecione um árbitro da tabela.");
        }
    }
}
