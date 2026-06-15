package Interface;

import Aplicacoes.OprIngresso;
import Aplicacoes.OprPartida;
import Objetos.Partida;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TelaPublico {

    @FXML
    private Label lblPublicoTotal;

    @FXML
    private Label lblIngressosVendidos;

    @FXML
    private Label lblVagasDisponiveis;

    private OprIngresso oprIngresso = new OprIngresso();
    private OprPartida oprPartida = new OprPartida();

    @FXML
    public void initialize() {
        int capacidadeTotal = 0;

        for (Partida partida : oprPartida.getListaPartidas()) {
            if (partida.getEstadio() != null) {
                capacidadeTotal += partida.getEstadio().getVagas();
            }
        }

        int ingressosVendidos = oprIngresso.contarIngressosVendidos();
        int vagasDisponiveis = capacidadeTotal - ingressosVendidos;

        lblPublicoTotal.setText("Público total: " + oprIngresso.calcularPublicoTotal());
        lblIngressosVendidos.setText("Ingressos vendidos: " + ingressosVendidos);
        lblVagasDisponiveis.setText("Vagas disponíveis: " + vagasDisponiveis);
    }

    @FXML
    private void voltarIngressos(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaIngressos.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}