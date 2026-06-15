package Interface;

import Aplicacoes.OprIngresso;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TelaRelatorio {

    @FXML
    private Label lblArrecadacaoTotal;

    @FXML
    private Label lblIngressosVendidos;

    private OprIngresso oprIngresso = new OprIngresso();

    @FXML
    public void initialize() {
        lblArrecadacaoTotal.setText("Arrecadação total: R$ " + oprIngresso.calcularArrecadacaoTotal());
        lblIngressosVendidos.setText("Ingressos vendidos: " + oprIngresso.contarIngressosVendidos());
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