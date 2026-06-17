package Interface;

import Aplicacoes.OprEst;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import Aplicacoes.OprPartida;
import Aplicacoes.OprSel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.geometry.Insets;
import Objetos.Partida;
import Objetos.Selecao;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Optional;
import javafx.collections.ObservableList;
import Aplicacoes.OprJog;

public class TelaPartidas implements Initializable {

    private OprPartida oprPartida;
    private OprSel oprSel;
    private Partida partidaSelecionada;

    @FXML private TextField txtBusca;
    @FXML private TableView<Partida> tabelaPartidas;
    @FXML private TableColumn<Partida, String> colCasa;
    @FXML private TableColumn<Partida, String> colVisitante;
    @FXML private TableColumn<Partida, String> colEstadio;
    @FXML private TableColumn<Partida, String> colData;
    @FXML private TableColumn<Partida, String> colHorario;
    @FXML private TableColumn<Partida, String> colFase;
    @FXML private TableColumn<Partida, String> colPlacar;
    @FXML private TableColumn<Partida, String> colStatus;

    private final String[] fasesCampeonato = {
            "Grupo - Jogo 1", "Grupo - Jogo 2", "Grupo - Jogo 3",
            "Dezesseis-avos", "Oitavas", "Quartas", "Semifinal", "Final"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oprPartida = new OprPartida();
        oprSel = OprSel.getInstancia();

        OprJog oprJog = new OprJog(oprSel);
        oprJog.carregarJogadoresDoArquivo();

        configurarTabela();
        atualizarTabela("");

        txtBusca.textProperty().addListener((observable, oldValue, newValue) -> {
            atualizarTabela(newValue);
        });

        tabelaPartidas.getSelectionModel().selectedItemProperty().addListener((obs, antigo, novo) -> {
            if (novo != null) {
                partidaSelecionada = novo;
            }
        });
    }

    private void configurarTabela() {
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colHorario.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colFase.setCellValueFactory(new PropertyValueFactory<>("fase"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        colEstadio.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getEstadio().getNome()));
        colCasa.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTimeCasa().getPais()));
        colVisitante.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getTimeVisita().getPais()));
        colPlacar.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().getGolCasa() + " x " + p.getValue().getGolVisita()));
    }

    private void atualizarTabela(String termoBusca) {
        ObservableList<Partida> dados = FXCollections.observableArrayList(oprPartida.buscarPartidas(termoBusca));
        tabelaPartidas.setItems(dados);
        tabelaPartidas.refresh();
    }

    private void exibirAlerta(Alert.AlertType tipo, String mensagem) {
        Alert alert = new Alert(tipo);
        alert.setContentText(mensagem);
        alert.setHeaderText(null);
        alert.showAndWait();
    }


    @FXML
    public void handleBotaoCadastrar(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Agendar Nova Partida");
        dialog.setHeaderText("Preencha todos os dados do confronto:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        ComboBox<String> cmbCasa = new ComboBox<>();
        cmbCasa.setEditable(true);
        cmbCasa.setPromptText("Ex: Brasil");
        ObservableList<String> todasSelecoes = FXCollections.observableArrayList(
                oprSel.getListaSelecoes().stream()
                        .map(s -> s.getPais())
                        .collect(java.util.stream.Collectors.toList())
        );
        cmbCasa.setItems(todasSelecoes);
        cmbCasa.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            String filtro = newVal == null ? "" : newVal.toLowerCase();
            ObservableList<String> filtradas = FXCollections.observableArrayList(
                    todasSelecoes.stream()
                            .filter(p -> p.toLowerCase().contains(filtro))
                            .collect(java.util.stream.Collectors.toList())
            );
            cmbCasa.setItems(filtradas);
            if (!filtradas.isEmpty()) cmbCasa.show();
        });

        ComboBox<String> cmbVisitante = new ComboBox<>();
        cmbVisitante.setEditable(true);
        cmbVisitante.setPromptText("Ex: Argentina");
        cmbVisitante.setItems(FXCollections.observableArrayList(todasSelecoes));
        cmbVisitante.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            String filtro = newVal == null ? "" : newVal.toLowerCase();
            ObservableList<String> filtradas = FXCollections.observableArrayList(
                    todasSelecoes.stream()
                            .filter(p -> p.toLowerCase().contains(filtro))
                            .collect(java.util.stream.Collectors.toList())
            );
            cmbVisitante.setItems(filtradas);
            if (!filtradas.isEmpty()) cmbVisitante.show();
        });

        OprEst oprEst = Aplicacoes.OprEst.getInstancia();
        ObservableList<String> todosEstadios = FXCollections.observableArrayList(
                oprEst.getListaEstadio().stream().map(e -> e.getNome()).collect(java.util.stream.Collectors.toList())
        );
        ComboBox<String> cmbEstadio = new ComboBox<>();
        cmbEstadio.setEditable(true);
        cmbEstadio.setPromptText("Ex: Maracanã");
        cmbEstadio.setItems(todosEstadios);
        cmbEstadio.getEditor().textProperty().addListener((obs, oldVal, newVal) -> {
            String filtro = newVal == null ? "" : newVal.toLowerCase();
            ObservableList<String> filtradas = FXCollections.observableArrayList(
                    todosEstadios.stream().filter(e -> e.toLowerCase().contains(filtro)).collect(java.util.stream.Collectors.toList()));
            cmbEstadio.setItems(filtradas);
            if (!filtradas.isEmpty()) cmbEstadio.show();
        });

        TextField txtData    = new TextField("16/06/2026");
        TextField txtHora    = new TextField("20:00");

        ComboBox<String> cmbFase = new ComboBox<>();
        cmbFase.getItems().addAll(fasesCampeonato);
        cmbFase.setValue(fasesCampeonato[0]);

        grid.add(new Label("Seleção Casa:"),0, 0); grid.add(cmbCasa,1,0);
        grid.add(new Label("Seleção Visitante:"), 0, 1); grid.add(cmbVisitante,1,1);
        grid.add(new Label("Estádio:"),0, 2); grid.add(cmbEstadio,1,2);
        grid.add(new Label("Data:"),0, 3); grid.add(txtData,1,3);
        grid.add(new Label("Horário:"),0, 4); grid.add(txtHora,1,4);
        grid.add(new Label("Fase:"),0, 5); grid.add(cmbFase,1,5);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String nomeCasa     = cmbCasa.getEditor().getText().trim();
            String nomeVisita   = cmbVisitante.getEditor().getText().trim();

            if (nomeCasa.isBlank() || nomeVisita.isBlank() || cmbEstadio.getEditor().getText().trim().isBlank() || txtData.getText().isBlank() || txtHora.getText().isBlank()) {
                exibirAlerta(Alert.AlertType.WARNING, "Todos os campos devem ser preenchidos!");
                return;
            }

            Selecao casaObj   = oprSel.buscarSelecaoPorNome(nomeCasa);
            Selecao visitaObj = oprSel.buscarSelecaoPorNome(nomeVisita);

            if (casaObj == null || visitaObj == null) {
                exibirAlerta(Alert.AlertType.ERROR, "Uma ou ambas as seleções digitadas não existem no cadastro!");
                return;
            }

            if (!casaObj.isElegivel() || !visitaObj.isElegivel()) {
                exibirAlerta(Alert.AlertType.ERROR, "Não foi possível agendar! Uma das seleções possui elenco irregular (Menos de 18 ativos).");
                return;
            }

            boolean sucesso = oprPartida.cadastrarPartida(casaObj, visitaObj,cmbEstadio.getEditor().getText().trim(), txtData.getText(), txtHora.getText(), cmbFase.getValue());
            if (sucesso) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Partida cadastrada com sucesso!");
                atualizarTabela("");
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao cadastrar! Verifique choques de estádio/horário ou se os times são iguais.");
            }
        }
    }


    @FXML
    public void handleBotaoEditar(ActionEvent event) {
        if (partidaSelecionada == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Selecione uma partida na tabela primeiro!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Editar Dados da Partida");
        dialog.setHeaderText("Altere as informações desejadas:");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField txtData = new TextField(partidaSelecionada.getData());
        TextField txtHora = new TextField(partidaSelecionada.getHora());
        ComboBox<String> cmbFase = new ComboBox<>();
        cmbFase.getItems().addAll(fasesCampeonato);
        cmbFase.setValue(partidaSelecionada.getFase());

        grid.add(new Label("Nova Data:"), 0, 0);
        grid.add(txtData, 1, 0);
        grid.add(new Label("Novo Horário:"), 0, 1);
        grid.add(txtHora, 1, 1);
        grid.add(new Label("Nova Fase:"), 0, 2);
        grid.add(cmbFase, 1, 2);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            if (txtData.getText().isBlank() || txtHora.getText().isBlank()) {
                exibirAlerta(Alert.AlertType.WARNING, "Os campos não podem ficar vazios!");
                return;
            }

            boolean sucesso = oprPartida.editarPartida(
                    partidaSelecionada.getData(), partidaSelecionada.getHora(),
                    txtData.getText(), txtHora.getText(), cmbFase.getValue()
            );

            if (sucesso) {
                exibirAlerta(Alert.AlertType.INFORMATION, "Partida editada com sucesso!");
                atualizarTabela("");
                tabelaPartidas.getSelectionModel().clearSelection();
                partidaSelecionada = null;
            } else {
                exibirAlerta(Alert.AlertType.ERROR, "Erro ao salvar as edições.");
            }
        }
    }


    @FXML
    public void handleBotaoRegistrarResultado(ActionEvent event) {
        if (partidaSelecionada == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Selecione uma partida na tabela para definir o placar!");
            return;
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Registrar Placar");
        dialog.setHeaderText("Confronto: " + partidaSelecionada.getTimeCasa().getPais() + " x " + partidaSelecionada.getTimeVisita().getPais());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 80, 10, 10));

        TextField txtGolsCasa = new TextField("0");
        TextField txtGolsVisita = new TextField("0");

        grid.add(new Label("Gols " + partidaSelecionada.getTimeCasa().getPais() + ":"), 0, 0);
        grid.add(txtGolsCasa, 1, 0);
        grid.add(new Label("Gols " + partidaSelecionada.getTimeVisita().getPais() + ":"), 0, 1);
        grid.add(txtGolsVisita, 1, 1);

        dialog.getDialogPane().setContent(grid);

        Optional<ButtonType> resultado = dialog.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            try {
                int gCasa = Integer.parseInt(txtGolsCasa.getText().trim());
                int gVisita = Integer.parseInt(txtGolsVisita.getText().trim());

                if (gCasa < 0 || gVisita < 0) {
                    exibirAlerta(Alert.AlertType.ERROR, "Quantidade de gols não pode ser negativa!");
                    return;
                }

                boolean sucesso = oprPartida.registrarResultado(partidaSelecionada.getData(), partidaSelecionada.getHora(), gCasa, gVisita);
                if (sucesso) {
                    exibirAlerta(Alert.AlertType.INFORMATION, "Resultado gravado e status atualizado para Finalizada!");
                    atualizarTabela("");
                    tabelaPartidas.getSelectionModel().clearSelection();
                    partidaSelecionada = null;
                }
            } catch (NumberFormatException e) {
                exibirAlerta(Alert.AlertType.ERROR, "Por favor, insira números válidos para o placar.");
            }
        }
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


    @FXML
    public void handleBotaoExcluir(ActionEvent event) {
        if (partidaSelecionada == null) {
            exibirAlerta(Alert.AlertType.WARNING, "Selecione uma partida na tabela antes de excluir!");
            return;
        }

        boolean sucesso = oprPartida.excluirPartida(partidaSelecionada.getData(), partidaSelecionada.getHora());
        if (sucesso) {
            exibirAlerta(Alert.AlertType.INFORMATION, "Partida desmarcada e removida com sucesso.");
            atualizarTabela("");
            tabelaPartidas.getSelectionModel().clearSelection();
            partidaSelecionada = null;
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Não foi possível remover a partida.");
        }
    }

    @FXML
    public void listarPartidas(ActionEvent event) {
        atualizarTabela("");
    }
}