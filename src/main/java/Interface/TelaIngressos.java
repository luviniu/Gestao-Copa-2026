package Interface;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;

import java.net.URL;
import java.util.ResourceBundle;

import Aplicacoes.OprIngresso;
import Aplicacoes.OprPartida;
import Objetos.Partida;

public class TelaIngressos implements Initializable {

    @FXML private TextField txtBuscaPartida;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private TextField txtQuantidade;
    @FXML private Button btnVender;

    @FXML private TableView<Partida> tabelaInfoPartida;
    @FXML private TableColumn<Partida, String>  colInfoJogo;
    @FXML private TableColumn<Partida, String>  colInfoHorario;
    @FXML private TableColumn<Partida, String>  colInfoData;
    @FXML private TableColumn<Partida, String>  colInfoLocal;
    @FXML private TableColumn<Partida, Integer> colInfoCapacidade;
    @FXML private TableColumn<Partida, Integer> colInfoVendidos;
    @FXML private TableColumn<Partida, Double>  colInfoArrecadacao;

    private OprPartida oprPartida = new OprPartida();
    private OprIngresso oprIngresso = new OprIngresso();

    private ObservableList<Partida> todasPartidas = FXCollections.observableArrayList();
    private FilteredList<Partida> partidasFiltradas;

    private Partida partidaSelecionadaAtual = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboCategoria.getItems().addAll("Normal", "VIP", "Meia");

        colInfoJogo.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getTimeCasa().getPais() + " x " + c.getValue().getTimeVisita().getPais()));

        colInfoHorario.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getHora()));

        colInfoData.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getData()));

        colInfoLocal.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEstadio().getNome()));

        colInfoCapacidade.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().getEstadio().getVagas()).asObject());

        colInfoVendidos.setCellValueFactory(c ->
                new SimpleIntegerProperty(oprIngresso.calcularPublicoPorPartida(c.getValue())).asObject());

        if (colInfoArrecadacao != null) {
            colInfoArrecadacao.setCellValueFactory(c ->
                    new SimpleDoubleProperty(oprIngresso.calcularArrecadacaoPorPartida(c.getValue())).asObject());
        }

        todasPartidas.addAll(oprPartida.getListaPartidas());
        partidasFiltradas = new FilteredList<>(todasPartidas, p -> true);
        tabelaInfoPartida.setItems(partidasFiltradas);

        txtBuscaPartida.textProperty().addListener((obs, oldVal, newVal) -> {
            if (partidaSelecionadaAtual != null) {
                return;
            }

            partidasFiltradas.setPredicate(p -> {
                if (newVal == null || newVal.isBlank()) {
                    return true;
                }
                String busca = newVal.toLowerCase().trim();
                return p.getTimeCasa().getPais().toLowerCase().contains(busca)
                        || p.getTimeVisita().getPais().toLowerCase().contains(busca);
            });
        });

        tabelaInfoPartida.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                partidaSelecionadaAtual = newVal;
                partidasFiltradas.setPredicate(p -> true);
                txtBuscaPartida.setText(newVal.getTimeCasa().getPais() + " x " + newVal.getTimeVisita().getPais());
            }
        });

        txtBuscaPartida.setOnKeyTyped(e -> partidaSelecionadaAtual = null);

        System.out.println("TelaIngressos carregada!");
        return;
    }

    @FXML
    private void venderIngresso() {
        Partida partidaSelecionada = partidaSelecionadaAtual != null
                ? partidaSelecionadaAtual
                : tabelaInfoPartida.getSelectionModel().getSelectedItem();

        String categoria = comboCategoria.getValue();
        String quantidadeTexto = txtQuantidade.getText();

        if (partidaSelecionada == null || categoria == null || quantidadeTexto == null || quantidadeTexto.isEmpty()) {
            mostrarErro("Campos incompletos", "Não foi possível vender o ingresso", "Selecione uma partida na tabela, a categoria e a quantidade.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeTexto);
        } catch (NumberFormatException ex) {
            mostrarErro("Quantidade inválida", "Não foi possível vender o ingresso", "A quantidade deve ser um número inteiro.");
            return;
        }

        try {
            oprIngresso.venderIngresso(categoria, partidaSelecionada, 100.0, quantidade);
        } catch (Exception e) {
            mostrarErro("Erro na venda", "Não foi possível vender o ingresso", e.getMessage());
            return;
        }

        tabelaInfoPartida.getColumns().forEach(col -> {
            col.setVisible(false);
            col.setVisible(true);
        });

        double precoBase = 100.0;
        double precoFinal = categoria.equalsIgnoreCase("VIP") ? precoBase * 2
                : categoria.equalsIgnoreCase("Meia") ? precoBase / 2
                  : precoBase;
        double total = precoFinal * quantidade;

        String nomePartida = partidaSelecionada.getTimeCasa().getPais() + " x " + partidaSelecionada.getTimeVisita().getPais();

        System.out.println("===== VENDA DE INGRESSO =====");
        System.out.println("Partida: " + nomePartida);
        System.out.println("Categoria: " + categoria);
        System.out.println("Quantidade: " + quantidade);
        System.out.println("Preço unitário: R$ " + precoFinal);
        System.out.println("Total: R$ " + total);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Venda realizada");
        alert.setHeaderText("Ingresso vendido com sucesso!");
        alert.setContentText("Partida: " + nomePartida + "\n"
                + "Categoria: " + categoria + "\n"
                + "Quantidade: " + quantidade + "\n"
                + "Preço unitário: R$ " + precoFinal + "\n"
                + "Total: R$ " + total);
        alert.showAndWait();
        return;
    }

    @FXML
    private void devolverIngresso() {
        Partida partidaSelecionada = partidaSelecionadaAtual != null
                ? partidaSelecionadaAtual
                : tabelaInfoPartida.getSelectionModel().getSelectedItem();

        String categoria = comboCategoria.getValue();
        String quantidadeTexto = txtQuantidade.getText();

        if (partidaSelecionada == null || categoria == null || quantidadeTexto == null || quantidadeTexto.isEmpty()) {
            mostrarErro("Campos incompletos", "Não foi possível devolver o ingresso", "Selecione uma partida na tabela, a categoria e a quantidade.");
            return;
        }

        int quantidade;
        try {
            quantidade = Integer.parseInt(quantidadeTexto);
        } catch (NumberFormatException ex) {
            mostrarErro("Quantidade inválida", "Não foi possível devolver o ingresso", "A quantidade deve ser um número inteiro.");
            return;
        }

        try {
            oprIngresso.devolverIngresso(categoria, partidaSelecionada, quantidade);
        } catch (Exception e) {
            mostrarErro("Erro na devolução", "Não foi possível devolver o ingresso", e.getMessage());
            return;
        }

        tabelaInfoPartida.getColumns().forEach(col -> {
            col.setVisible(false);
            col.setVisible(true);
        });

        String nomePartida = partidaSelecionada.getTimeCasa().getPais() + " x " + partidaSelecionada.getTimeVisita().getPais();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Devolução realizada");
        alert.setHeaderText("Ingresso devolvido com sucesso!");
        alert.setContentText("Partida: " + nomePartida + "\n"
                + "Categoria: " + categoria + "\n"
                + "Quantidade: " + quantidade);
        alert.showAndWait();
        return;
    }

    @FXML
    private void voltarTela(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaLauncher.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

    @FXML
    private void abrirRelatorio(ActionEvent event) {
        int vendidos = oprIngresso.contarIngressosVendidos();
        double arrecadacao = oprIngresso.calcularArrecadacaoTotal();

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Relatório Financeiro");
        alert.setHeaderText("Resumo financeiro");
        alert.setContentText(
                "Ingressos vendidos: " + vendidos +
                        "\nArrecadação total: R$ " + String.format("%.2f", arrecadacao)
        );

        alert.showAndWait();
    }

    private void mostrarErro(String titulo, String cabecalho, String conteudo) {
        System.out.println(conteudo);
        Alert erro = new Alert(Alert.AlertType.ERROR);
        erro.setTitle(titulo);
        erro.setHeaderText(cabecalho);
        erro.setContentText(conteudo);
        erro.showAndWait();
        return;
    }
}