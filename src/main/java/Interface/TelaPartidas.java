package Interface;
import javafx.scene.control.ComboBox;
import javafx.event.ActionEvent;
import Aplicacoes.OprPartida;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import javafx.scene.control.TextArea;
import Objetos.Partida;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaPartidas implements Initializable {

    private OprPartida oprPartida;

    @FXML
    private TextArea txtAreaPartidas;

    @FXML
    private TextField txtSelecaoCasa;

    @FXML
    private TextField txtSelecaoVisitante;

    @FXML
    private TextField txtEstadio;

    @FXML
    private TextField txtData;

    @FXML
    private TextField txtHorario;


    @FXML
    private ComboBox<String> cmbFase;
    @FXML
    private TextField txtGolCasa;

    @FXML
    private TextField txtGolVisitante;

    @Override

    public void initialize(URL url, ResourceBundle rb) {

        oprPartida = new OprPartida();

        cmbFase.getItems().addAll(
                "Grupo - Jogo 1",
                "Grupo - Jogo 2",
                "Grupo - Jogo 3",
                "Dezesseis-avos",
                "Oitavas",
                "Quartas",
                "Semifinal",
                "Final"
        );
    }

    @FXML

    public void cadastrarPartida(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (txtSelecaoCasa.getText().isEmpty() ||
                txtSelecaoVisitante.getText().isEmpty() ||
                txtEstadio.getText().isEmpty() ||
                txtData.getText().isEmpty() ||
                txtHorario.getText().isEmpty() ||
                cmbFase.getValue() == null) {

            Toast.exibir(stage, "Preencha todos os campos!");
            return;
        }
        if (txtSelecaoCasa.getText().equalsIgnoreCase(
                txtSelecaoVisitante.getText())) {

            Toast.exibir(stage,
                    "Uma selecao nao pode jogar contra ela mesma!");

            return;
        }
        boolean sucesso = oprPartida.cadastrarPartida(
                txtSelecaoCasa.getText(),
                txtSelecaoVisitante.getText(),
                txtEstadio.getText(),
                txtData.getText(),
                txtHorario.getText(),
                cmbFase.getValue()
        );


        if (sucesso) {
            Toast.exibir(stage, "Partida cadastrada com sucesso!");
        } else {
            Toast.exibir(stage, "Erro ao cadastrar partida!");
        }
        if (sucesso) {
            System.out.println("Partida cadastrada com sucesso!");
        } else {
            System.out.println("Erro ao cadastrar partida.");
        }

        if (sucesso) {
            txtSelecaoCasa.clear();
            txtSelecaoVisitante.clear();
            txtEstadio.clear();
            txtData.clear();
            txtHorario.clear();
            cmbFase.setValue(null);
        }
    }

    @FXML
    public void listarPartidas(ActionEvent event) {

        txtAreaPartidas.clear();

        for (var p : oprPartida.getListaPartidas()) {

            txtAreaPartidas.appendText(
                    "Casa: " + p.getTimeCasa().getPais() + "\n" +
                            "Visitante: " + p.getTimeVisita().getPais() + "\n" +
                            "Estadio: " + p.getEstadio().getNome() + "\n" +
                            "Data: " + p.getData() + "\n" +
                            "Horario: " + p.getHora() + "\n" +
                            "Fase: " + p.getFase() + "\n" +
                            "Gols Casa: " + p.getGolCasa() + "\n" +
                            "Gols Visitante: " + p.getGolVisita() + "\n" +
                            "Status: " + p.getStatus() + "\n" +
                            "--------------------------\n"
            );
        }
    }

    @FXML
    public void excluirPartida(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (txtData.getText().isEmpty()) {

            Toast.exibir(stage, "Informe a data da partida!");
            return;
        }

        boolean sucesso = oprPartida.excluirPartida(
                txtData.getText()
        );


        if (sucesso) {
            Toast.exibir(stage, "Partida excluida com sucesso!");
        } else {
            Toast.exibir(stage, "Partida nao encontrada!");
        }
        if (sucesso) {
            System.out.println("Partida excluido com sucesso!");
        } else {
            System.out.println("Erro ao excluir partida.");
        }
        if (sucesso) {
            txtSelecaoCasa.clear();
            txtSelecaoVisitante.clear();
            txtEstadio.clear();
            txtData.clear();
            txtHorario.clear();
            cmbFase.setValue(null);
        }
    }
    @FXML
    public void editarPartida(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (txtData.getText().isEmpty() ||
                txtHorario.getText().isEmpty() ||
                cmbFase.getValue() == null) {

            Toast.exibir(stage, "Preencha todos os campos!");
            return;
        }

        boolean sucesso = oprPartida.editarPartida(
                txtData.getText(),
                txtData.getText(),
                txtHorario.getText(),
                cmbFase.getValue()
        );


        if (sucesso) {

            Toast.exibir(stage, "Partida editada com sucesso!");

            txtSelecaoCasa.clear();
            txtSelecaoVisitante.clear();
            txtEstadio.clear();
            txtData.clear();
            txtHorario.clear();
            cmbFase.setValue(null);

        } else {

            Toast.exibir(stage, "Erro ao editar partida!");
        }


        if (sucesso) {
            System.out.println("Partida editado com sucesso!");
        } else {
            System.out.println("Erro ao editar partida.");
        }
        if (sucesso) {
            txtSelecaoCasa.clear();
            txtSelecaoVisitante.clear();
            txtEstadio.clear();
            txtData.clear();
            txtHorario.clear();
            cmbFase.setValue(null);
        }
    }
    @FXML
    public void registrarResultado(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (txtData.getText().isEmpty() ||
                txtGolCasa.getText().isEmpty() ||
                txtGolVisitante.getText().isEmpty()) {

            Toast.exibir(stage, "Preencha todos os campos!");
            return;
        }

        int golsCasa;
        int golsVisitante;

        try {
            golsCasa = Integer.parseInt(txtGolCasa.getText());
            golsVisitante = Integer.parseInt(txtGolVisitante.getText());
        }
        catch (NumberFormatException e) {

            Toast.exibir(stage, "Digite apenas numeros nos gols!");
            return;
        }

        boolean sucesso = oprPartida.registrarResultado(
                txtData.getText(),
                golsCasa,
                golsVisitante
        );
        if (golsCasa < 0 || golsVisitante < 0) {

            Toast.exibir(stage, "Gols nao podem ser negativos!");
            return;
        }

        if (sucesso) {

            Toast.exibir(stage, "Resultado registrado!");

            txtGolCasa.clear();
            txtGolVisitante.clear();

        } else {

            Toast.exibir(stage, "Partida nao encontrada!");
        }
        Toast.exibir(stage, "Resultado registrado!");
    }
    @FXML
    public void consultarPorSelecao(ActionEvent event) {

        txtAreaPartidas.clear();

        var partidas =
                oprPartida.consultarPorSelecao(
                        txtSelecaoCasa.getText()
                );

        if (partidas.isEmpty()) {

            txtAreaPartidas.setText(
                    "Nenhuma partida encontrada."
            );

            return;
        }

        for (Partida p : partidas) {

            txtAreaPartidas.appendText(
                    p.getTimeCasa().getPais()
                            + " x "
                            + p.getTimeVisita().getPais()
                            + " | "
                            + p.getFase()
                            + " | "
                            + p.getData()
                            + "\n"
            );
        }
    }
    @FXML
    public void consultarPorFase(ActionEvent event) {

        txtAreaPartidas.clear();

        var partidas =
                oprPartida.consultarPorFase(
                        cmbFase.getValue()
                );

        if (partidas.isEmpty()) {

            txtAreaPartidas.setText(
                    "Nenhuma partida encontrada."
            );

            return;
        }

        for (Partida p : partidas) {

            txtAreaPartidas.appendText(
                    p.getTimeCasa().getPais()
                            + " x "
                            + p.getTimeVisita().getPais()
                            + " - "
                            + p.getData()
                            + " - "
                            + p.getHora()
                            + "\n"
            );
        }
    }

    @FXML
    public void consultarPartida(ActionEvent event) {

        txtAreaPartidas.clear();

        Partida p = oprPartida.consultarPartida(
                txtData.getText()
        );

        if (p == null) {

            txtAreaPartidas.setText(
                    "Partida nao encontrada."
            );

            return;
        }

        txtAreaPartidas.setText(
                "Casa: " + p.getTimeCasa().getPais() + "\n" +
                        "Visitante: " + p.getTimeVisita().getPais() + "\n" +
                        "Estadio: " + p.getEstadio().getNome() + "\n" +
                        "Data: " + p.getData() + "\n" +
                        "Horario: " + p.getHora() + "\n" +
                        "Fase: " + p.getFase() + "\n" +
                        "Gols Casa: " + p.getGolCasa() + "\n" +
                        "Gols Visitante: " + p.getGolVisita() + "\n" +
                        "Status: " + p.getStatus()
        );
        txtSelecaoCasa.clear();
        txtSelecaoVisitante.clear();
        txtEstadio.clear();
        txtHorario.clear();
        txtGolCasa.clear();
        txtGolVisitante.clear();
        cmbFase.setValue(null);
    }


}