package Interface;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboCategoria.getItems().addAll(
                "Normal",
                "VIP",
                "Meia"
        );
        OprPartida oprPartida = new OprPartida();

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
        String categoria = comboCategoria.getValue();
        String quantidadeTexto = txtQuantidade.getText();

        if (partida == null || categoria == null || quantidadeTexto.isEmpty()) {
            System.out.println("Preencha todos os campos!");
            return;
        }

        int quantidade = Integer.parseInt(quantidadeTexto);

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
    }
}