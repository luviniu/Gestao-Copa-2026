package Interface;

import javafx.event.ActionEvent;
import Aplicacoes.OprPartida;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class TelaPartidas implements Initializable {

    private OprPartida oprPartida;

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

        System.out.println("Partida cadastrada: " + sucesso);
    }

    @FXML
    public void listarPartidas(ActionEvent event) {

        System.out.println("=== LISTA DE PARTIDAS ===");

        for (var p : oprPartida.getListaPartidas()) {

            System.out.println(
                    p.getTimeCasa().getPais()
                            + " x "
                            + p.getTimeVisita().getPais()
                            + " | "
                            + p.getData()
                            + " | "
                            + p.getFase()
            );
        }
    }

    @FXML
    public void excluirPartida(ActionEvent event) {

        boolean sucesso = oprPartida.excluirPartida(
                txtData.getText()
        );

        System.out.println("Partida excluida: " + sucesso);
    }
    @FXML
    public void editarPartida(ActionEvent event) {

        boolean sucesso = oprPartida.editarPartida(
                txtData.getText(),
                txtData.getText(),
                txtHorario.getText(),
                txtFase.getText()
        );

        System.out.println("Partida editada: " + sucesso);
    }
}