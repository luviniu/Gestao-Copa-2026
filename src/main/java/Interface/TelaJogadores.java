package Interface;

import Aplicacoes.OprJog;
import Aplicacoes.OprSel;
import Objetos.Jogador;
import Objetos.Selecao;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TelaJogadores implements Initializable {

    private Selecao selecaoAtual;
    private OprJog oprJog = new OprJog(OprSel.getInstancia());

    @FXML private TableView<Jogador> tabelaJogadores;
    @FXML private TableColumn<Jogador, String> colunaJogador;
    @FXML private TableColumn<Jogador, String> colunaPosicao;
    @FXML private TableColumn<Jogador, Integer> colunaIdade;
    @FXML private TableColumn<Jogador, String> colunaStatus;
    @FXML private TableColumn<Jogador, Integer> colunaNumero;

    @FXML private Text txtTituloElenco;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Vincula as colunas da tabela com as propriedades da classe Jogador
        colunaJogador.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPosicao.setCellValueFactory(new PropertyValueFactory<>("posicao"));
        colunaIdade.setCellValueFactory(new PropertyValueFactory<>("idade"));
        colunaStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
    }

    public void setSelecaoAtual(Selecao selecao) {
        this.selecaoAtual = selecao;
        txtTituloElenco.setText("Elenco - " + selecaoAtual.getPais());
        atualizarTabela();
    }

    private void atualizarTabela() {
        if (selecaoAtual != null) {
            // 1. Atualiza as linhas da tabela normalmente
            ObservableList<Jogador> dados = FXCollections.observableArrayList(selecaoAtual.getTime());
            tabelaJogadores.setItems(dados);
            tabelaJogadores.refresh();

            // 2. Contagem silenciosa para validação dos requisitos
            int totalJogadores = selecaoAtual.getTime().size();
            int ativos = 0;

            for (Jogador j : selecaoAtual.getTime()) {
                if ("Ativo".equals(j.getStatus())) {
                    ativos++;
                }
            }

            // 3. Apenas muda o texto do título, sem interromper o usuário
            if (totalJogadores >= 18 && totalJogadores <= 26 && ativos >= 18) {
                txtTituloElenco.setText("Elenco - " + selecaoAtual.getPais() + " (Apto)");
            } else {
                txtTituloElenco.setText("Elenco - " + selecaoAtual.getPais() + " (Incompleto)");
            }
        }
    }

    // AÇÃO: Reseta os filtros aplicados e restaura a lista original do elenco
    @FXML
    public void handleResetarTabela(ActionEvent event) {
        atualizarTabela();
    }

    // IMPLEMENTAÇÃO: Janela Flutuante de Busca/Filtro com múltiplos parâmetros
    @FXML
    public void handleBuscarJogador(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Buscar / Filtrar Jogadores");
        dialog.setHeaderText("Filtre o elenco por Nome, Posição ou Status\n(Deixe em branco/Todos para ignorar o filtro)");

        TextField campoBuscaNome = new TextField();
        campoBuscaNome.setPromptText("Ex: Neymar");

        ComboBox<String> comboBuscaPosicao = new ComboBox<>();
        comboBuscaPosicao.getItems().addAll("Todos", "Goleiro", "Zagueiro", "Meio-campista", "Atacante");
        comboBuscaPosicao.setValue("Todos");

        ComboBox<String> comboBuscaStatus = new ComboBox<>();
        comboBuscaStatus.getItems().addAll("Todos", "Ativo", "Lesionado", "Suspenso");
        comboBuscaStatus.setValue("Todos");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome do Atleta:"), 0, 0);
        grid.add(campoBuscaNome, 1, 0);
        grid.add(new Label("Posição:"), 0, 1);
        grid.add(comboBuscaPosicao, 1, 1);
        grid.add(new Label("Status:"), 0, 2);
        grid.add(comboBuscaStatus, 1, 2);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                String termoNome = campoBuscaNome.getText().toLowerCase().trim();
                String termoPosicao = comboBuscaPosicao.getValue();
                String termoStatus = comboBuscaStatus.getValue();

                ObservableList<Jogador> todos = FXCollections.observableArrayList(selecaoAtual.getTime());

                if (termoNome.isEmpty() && "Todos".equals(termoPosicao) && "Todos".equals(termoStatus)) {
                    tabelaJogadores.setItems(todos);
                    return;
                }

                ObservableList<Jogador> filtrados = FXCollections.observableArrayList();

                for (Jogador j : todos) {
                    boolean passaNome = termoNome.isEmpty() || j.getNome().toLowerCase().contains(termoNome);
                    boolean passaPosicao = "Todos".equals(termoPosicao) || j.getPosicao().equals(termoPosicao);
                    boolean passaStatus = "Todos".equals(termoStatus) || j.getStatus().equals(termoStatus);

                    if (passaNome && passaPosicao && passaStatus) {
                        filtrados.add(j);
                    }
                }

                tabelaJogadores.setItems(filtrados);
                tabelaJogadores.refresh();

                if (filtrados.isEmpty()) {
                    alerta(Alert.AlertType.INFORMATION, "Nenhum jogador corresponde aos critérios de busca.");
                }
            }
        });
    }

    // IMPLEMENTAÇÃO: Dialog de cadastro isolado e limpo
    @FXML
    public void handleBotaoNovoJogador(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Cadastrar Jogador");
        dialog.setHeaderText("Insira os dados do novo atleta");

        TextField campoNome = new TextField();
        TextField campoIdade = new TextField();
        TextField campoNumero = new TextField();

        ComboBox<String> comboPos = new ComboBox<>(FXCollections.observableArrayList("Goleiro", "Zagueiro", "Meio-campista", "Atacante"));
        comboPos.setValue("Goleiro");

        ComboBox<String> comboSt = new ComboBox<>(FXCollections.observableArrayList("Ativo", "Lesionado", "Suspenso"));
        comboSt.setValue("Ativo");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(campoNome, 1, 0);
        grid.add(new Label("Idade:"), 0, 1);
        grid.add(campoIdade, 1, 1);
        grid.add(new Label("Número da Camisa:"), 0, 2);
        grid.add(campoNumero, 1, 2);
        grid.add(new Label("Posição:"), 0, 3);
        grid.add(comboPos, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(comboSt, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                try {
                    String nome = campoNome.getText().trim();
                    String posicao = comboPos.getValue();
                    int idade = Integer.parseInt(campoIdade.getText().trim());
                    int numero = Integer.parseInt(campoNumero.getText().trim());
                    String status = comboSt.getValue();

                    if (nome.isEmpty()) {
                        alerta(Alert.AlertType.WARNING, "O nome do jogador não pode estar vazio!");
                        return;
                    }

                    if (oprJog.cadastrarJogador(nome, posicao, numero, idade, selecaoAtual.getPais(), status)) {
                        atualizarTabela();
                        alerta(Alert.AlertType.INFORMATION, "Jogador cadastrado com sucesso!");
                    } else {
                        alerta(Alert.AlertType.ERROR, "Erro ao cadastrar. Verifique se o número da camisa já está em uso.");
                    }
                } catch (NumberFormatException e) {
                    alerta(Alert.AlertType.ERROR, "Idade e Número precisam ser valores numéricos válidos.");
                }
            }
        });
    }

    // IMPLEMENTAÇÃO: Dialog de edição injetando dados do objeto selecionado
    @FXML
    public void handleEditarJogador(ActionEvent event) {
        Jogador selecionado = tabelaJogadores.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            alerta(Alert.AlertType.WARNING, "Selecione um jogador na tabela para alterar!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Informações do Atleta");
        dialog.setHeaderText("Editando: " + selecionado.getNome());

        TextField campoNome = new TextField(selecionado.getNome());
        campoNome.setDisable(true); // Nome travado pois atua como chave de busca

        TextField campoIdade = new TextField(String.valueOf(selecionado.getIdade()));
        TextField campoNumero = new TextField(String.valueOf(selecionado.getNumero()));

        ComboBox<String> comboPos = new ComboBox<>(FXCollections.observableArrayList("Goleiro", "Zagueiro", "Meio-campista", "Atacante"));
        comboPos.setValue(selecionado.getPosicao());

        ComboBox<String> comboSt = new ComboBox<>(FXCollections.observableArrayList("Ativo", "Lesionado", "Suspenso"));
        comboSt.setValue(selecionado.getStatus());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(campoNome, 1, 0);
        grid.add(new Label("Idade:"), 0, 1);
        grid.add(campoIdade, 1, 1);
        grid.add(new Label("Número da Camisa:"), 0, 2);
        grid.add(campoNumero, 1, 2);
        grid.add(new Label("Posição:"), 0, 3);
        grid.add(comboPos, 1, 3);
        grid.add(new Label("Status:"), 0, 4);
        grid.add(comboSt, 1, 4);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                try {
                    String posicao = comboPos.getValue();
                    int idade = Integer.parseInt(campoIdade.getText().trim());
                    int numero = Integer.parseInt(campoNumero.getText().trim());
                    String status = comboSt.getValue();

                    selecionado.setPosicao(posicao);
                    selecionado.setIdade(idade);
                    selecionado.setNumero(numero);
                    selecionado.setStatus(status);

                    if (oprJog.editarJogador(selecaoAtual.getPais(), selecionado.getNome(), posicao, numero, idade, status)) {
                        atualizarTabela();
                        alerta(Alert.AlertType.INFORMATION, "Informações do jogador atualizadas!");
                    } else {
                        alerta(Alert.AlertType.ERROR, "Erro ao salvar alterações no sistema.");
                    }
                } catch (NumberFormatException e) {
                    alerta(Alert.AlertType.ERROR, "Idade e Número precisam ser valores numéricos válidos.");
                }
            }
        });
    }

    @FXML
    public void handleExcluirJogador(ActionEvent event) {
        Jogador selecionado = tabelaJogadores.getSelectionModel().getSelectedItem();

        if (selecionado == null) {
            alerta(Alert.AlertType.WARNING, "Selecione um jogador na tabela para remover.");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setHeaderText(null);
        confirmacao.setContentText("Tem certeza que deseja dispensar o jogador " + selecionado.getNome() + " do elenco?");

        confirmacao.showAndWait().ifPresent(tipo -> {
            if (tipo == ButtonType.OK) {
                if (oprJog.excluirJogador(selecaoAtual.getPais(), selecionado.getNome())) {
                    atualizarTabela();
                    alerta(Alert.AlertType.INFORMATION, "Jogador removido com sucesso!");
                } else {
                    alerta(Alert.AlertType.ERROR, "Erro ao remover o jogador.");
                }
            }
        });
    }

    @FXML
    public void irParaSelecoes(ActionEvent event) {
        // 1. Verificação dos critérios do elenco antes de mudar de tela
        if (selecaoAtual != null) {
            int totalJogadores = selecaoAtual.getTime().size();
            int ativos = 0;

            for (Jogador j : selecaoAtual.getTime()) {
                if ("Ativo".equals(j.getStatus())) {
                    ativos++;
                }
            }

            // Se NÃO atender aos requisitos, exibe o lembrete amigável
            if (totalJogadores < 18 || totalJogadores > 26 || ativos < 18) {
                StringBuilder aviso = new StringBuilder("Nota: Este elenco saiu de sincronia com as regras do campeonato.\n\n");
                aviso.append("Estado atual:\n");
                aviso.append("- Total de inscritos: ").append(totalJogadores).append(" (Permitido: 18 a 26)\n");
                aviso.append("- Jogadores ativos: ").append(ativos).append(" (Mínimo: 18)\n\n");
                aviso.append("Lembre-se de regularizar o elenco para poder usar esta seleção em partidas.");

                alerta(Alert.AlertType.WARNING, aviso.toString());
            }
        }

        // 2. Transição de tela original (mantendo o seu try-catch)
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
            alerta(Alert.AlertType.ERROR, "Erro ao retornar para a tela de seleções.");
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