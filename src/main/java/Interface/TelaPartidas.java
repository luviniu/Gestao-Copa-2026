package Interface;

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
}