package Interface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Alert;
import Aplicacoes.OprIngresso;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;
import Aplicacoes.OprPartida;
import Objetos.Partida;

public class TelaIngressos implements Initializable {

    @FXML
    private ComboBox<String> comboPartida;

    @FXML
    private ComboBox<String> comboCategoria;

    @FXML
    private TextField txtQuantidade;

    @FXML
    private Button btnVender;

    private OprPartida oprPartida = new OprPartida();
    private OprIngresso oprIngresso = new OprIngresso();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboCategoria.getItems().addAll(
                "Normal",
                "VIP",
                "Meia"
        );

        for (Partida p : oprPartida.getListaPartidas()) {
            comboPartida.getItems().add(
                    p.getTimeCasa().getPais()
                            + " x " +
                            p.getTimeVisita().getPais()
            );
        }
        System.out.println("TelaIngressos carregada!");
    }
    @FXML
    private void venderIngresso() {
        String partida = comboPartida.getValue();
        Partida partidaSelecionada = null;

        for (Partida p : oprPartida.getListaPartidas()) {
            String nomePartida = p.getTimeCasa().getPais() + " x " + p.getTimeVisita().getPais();

            if (nomePartida.equals(partida)) {
                partidaSelecionada = p;
                break;
            }
        }
        String categoria = comboCategoria.getValue();
        String quantidadeTexto = txtQuantidade.getText();

        if (partida == null || categoria == null || quantidadeTexto.isEmpty()) {
            System.out.println("Preencha todos os campos!");
            return;
        }

        int quantidade = Integer.parseInt(quantidadeTexto);
        try {
            oprIngresso.venderIngresso(
                    categoria,
                    partidaSelecionada,
                    100.0,
                    quantidade
            );
        } catch (Exception e) {
            System.out.println(e.getMessage());
            Alert erro = new Alert(Alert.AlertType.ERROR);

            erro.setTitle("Erro na venda");

            erro.setHeaderText("Não foi possível vender o ingresso");

            erro.setContentText(e.getMessage());

            erro.showAndWait();
            return;
        }

        double precoBase = 100.0;
        double precoFinal;

        if (categoria.equalsIgnoreCase("VIP")) {
            precoFinal = precoBase * 2;
        } else if (categoria.equalsIgnoreCase("Meia")) {
            precoFinal = precoBase / 2;
        } else {
            precoFinal = precoBase;
        }

        double total = precoFinal * quantidade;

        System.out.println("===== VENDA DE INGRESSO =====");
        System.out.println("Partida: " + partida);
        System.out.println("Categoria: " + categoria);
        System.out.println("Quantidade: " + quantidade);
        System.out.println("Preco unitario: R$ " + precoFinal);
        System.out.println("Total: R$ " + total);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Venda realizada");
        alert.setHeaderText("Ingresso vendido com sucesso!");
        alert.setContentText(
                "Partida: " + partida +
                        "\nCategoria: " + categoria +
                        "\nQuantidade: " + quantidade +
                        "\nPreço unitário: R$ " + precoFinal +
                        "\nTotal: R$ " + total
        );
        alert.showAndWait();
    }
    @FXML
    private void abrirControlePublico(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaPublico.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void abrirRelatorio(ActionEvent event) {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaRelatorio.fxml"));
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