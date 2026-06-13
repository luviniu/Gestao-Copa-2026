package Interface;

import javafx.event.ActionEvent;
import Aplicacoes.OprPartida;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import javafx.scene.control.TextArea;

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
    private TextField txtFase;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        oprPartida = new OprPartida();
    }

    @FXML
    public void cadastrarPartida(ActionEvent event) {

        boolean sucesso = oprPartida.cadastrarPartida(
                txtSelecaoCasa.getText(),
                txtSelecaoVisitante.getText(),
                txtEstadio.getText(),
                txtData.getText(),
                txtHorario.getText(),
                txtFase.getText()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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
            txtFase.clear();
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
                            "--------------------------\n"
            );
        }
    }

    @FXML
    public void excluirPartida(ActionEvent event) {

        boolean sucesso = oprPartida.excluirPartida(
                txtData.getText()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

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
            txtFase.clear();
        }
    }
    @FXML
    public void editarPartida(ActionEvent event) {

        boolean sucesso = oprPartida.editarPartida(
                txtData.getText(),
                txtData.getText(),
                txtHorario.getText(),
                txtFase.getText()
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (sucesso) {

            Toast.exibir(stage, "Partida editada com sucesso!");

            txtSelecaoCasa.clear();
            txtSelecaoVisitante.clear();
            txtEstadio.clear();
            txtData.clear();
            txtHorario.clear();
            txtFase.clear();

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
            txtFase.clear();
        }
    }
}