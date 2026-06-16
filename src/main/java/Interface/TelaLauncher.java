
package Interface;

import Aplicacoes.oprSessao;
import Aplicacoes.OprPartida;
import Aplicacoes.OprIngresso;
import Objetos.Partida;
import Objetos.Usuario;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;

public class TelaLauncher {

    @FXML private Label labelNomeCentro;
    @FXML private Label labelCargoCentro;
    @FXML private Label labelCpfCentro;
    @FXML private Label labelNomeSidebar;
    @FXML private Label labelCargoSidebar;

    @FXML private AnchorPane painelInicial;
    @FXML private AnchorPane partidaPanelAdm;
    @FXML private AnchorPane desempenhoPanelAdm;
    @FXML private AnchorPane partidaPanelOrg;
    @FXML private AnchorPane arbitroPanelOrg;
    @FXML private AnchorPane selecaoPanelOrg;
    @FXML private AnchorPane estadioPanelOrg;
    @FXML private AnchorPane ingressoPanelOp;
    @FXML private AnchorPane partidaPanelArb;

    @FXML private AnchorPane buttonsAdmin;
    @FXML private AnchorPane buttonsOrganizador;
    @FXML private AnchorPane buttonsOperador;
    @FXML private AnchorPane buttonsArbitro;

    @FXML private Button btnPerfilInicio;
    @FXML private Button btnPartidasAdm;
    @FXML private Button buttonGestaoUsr;
    @FXML private Button btnDesempenhoAdm;

    @FXML private TableView<Partida>              relatorioPartida;
    @FXML private TableColumn<Partida, String>    colStatus;
    @FXML private TableColumn<Partida, String>    colTime1;
    @FXML private TableColumn<Partida, String>    colTime2;
    @FXML private TableColumn<Partida, String>    colEstadio;
    @FXML private TableColumn<Partida, Integer>   colGolsT1;
    @FXML private TableColumn<Partida, Integer>   colGolsT2;
    @FXML private TableColumn<Partida, Integer>   colPublico;
    @FXML private TableColumn<Partida, Integer>   colFaltas;
    @FXML private TableColumn<Partida, String>    colVencedor;

    public static class ResumoGeral {
        private final String totalPartidas;
        private final String publicoTotal;
        private final String ganhoTotal;

        public ResumoGeral(String totalPartidas, String publicoTotal, String ganhoTotal) {
            this.totalPartidas = totalPartidas;
            this.publicoTotal  = publicoTotal;
            this.ganhoTotal    = ganhoTotal;
        }
        public String getTotalPartidas(){
            return totalPartidas;

        }
        public String getPublicoTotal(){
            return publicoTotal;

        }
        public String getGanhoTotal(){
            return ganhoTotal;

        }

    }

    @FXML private TableView<ResumoGeral> infos;
    @FXML private TableColumn<ResumoGeral, String> colInfoPartidas;
    @FXML private TableColumn<ResumoGeral, String> colInfoPublico;
    @FXML private TableColumn<ResumoGeral, String> colInfoGanho;


    public static class DesempenhoSelecao {
        private final String selecao;
        private final int pontos, jogos, vitorias, empates, derrotas,
                gols, golsContra, saldoGols, classificacao;

        public DesempenhoSelecao(String selecao, int pontos, int jogos, int vitorias, int empates, int derrotas, int gols, int golsContra, int saldoGols, int classificacao) {
            this.selecao = selecao;
            this.pontos = pontos;
            this.jogos = jogos;
            this.vitorias= vitorias;
            this.empates = empates;
            this.derrotas= derrotas;
            this.gols = gols;
            this.golsContra = golsContra;
            this.saldoGols = saldoGols;
            this.classificacao = classificacao;
        }
        public String getSelecao() {
            return selecao;

        }

        public int getPontos() {
            return pontos;
        }

        public int getJogos() {
            return jogos;

        }

        public int getVitorias() {
            return vitorias;

        }

        public int getEmpates() {
            return empates;

        }

        public int getDerrotas() {
            return derrotas;

        }

        public int getGols() {
            return gols;

        }

        public int getGolsContra() {
            return golsContra;

        }

        public int getSaldoGols() {
            return saldoGols;

        }
        public int getClassificacao(){
            return classificacao;

        }

    }

    @FXML private TableView<DesempenhoSelecao> relatorioDesempenho;
    @FXML private TableColumn<DesempenhoSelecao, String> colDSelecao;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDPontos;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDJogos;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDVitorias;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDEmpates;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDDerrotas;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDGols;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDGolsContra;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDSaldo;
    @FXML private TableColumn<DesempenhoSelecao, Integer> colDClassificacao;

    private final OprPartida  oprPartida  = new OprPartida();
    private final OprIngresso oprIngresso = new OprIngresso();

    private static Stage janelaGestaoAtiva;

    @FXML
    public void initialize() {
        configurarTabelaPartidas();
        configurarTabelaDesempenho();

        Usuario logado = oprSessao.getUsuario();
        if (logado != null) {
            if (labelNomeCentro != null) {
                labelNomeCentro.setText(logado.getNome());

            }
            if (labelCargoCentro != null) {
                labelCargoCentro.setText(logado.getPerfilUsuario());

            }
            if (labelCpfCentro != null) {
                labelCpfCentro.setText(logado.getCpf());

            }
            if (labelNomeSidebar != null) {
                labelNomeSidebar.setText(logado.getNome());

            }
            if (labelCargoSidebar != null) {
                labelCargoSidebar.setText(logado.getPerfilUsuario());

            }

            voltarInicio(null);

            if (buttonsAdmin      != null) {
                buttonsAdmin.setVisible(false);

            }
            if (buttonsOrganizador!= null) {
                buttonsOrganizador.setVisible(false);

            }
            if (buttonsOperador   != null) {
                buttonsOperador.setVisible(false);

            }
            if (buttonsArbitro    != null) {
                buttonsArbitro.setVisible(false);

            }
            switch (logado.getPerfilUsuario().toUpperCase()) {
                case "ADMINISTRADOR":
                    if (buttonsAdmin != null) {
                        buttonsAdmin.setVisible(true);
                        break;

                    }
                case "ORGANIZADOR":
                    if (buttonsOrganizador != null) {
                        buttonsOrganizador.setVisible(true);
                        break;

                    }
                case "OPERADOR":
                    if (buttonsOperador != null){ buttonsOperador.setVisible(true);
                        break;

                    }
                case "ÁRBITRO":
                case "ARBITRO":
                    if (buttonsArbitro != null){
                        buttonsArbitro.setVisible(true);
                        break;

                    }

                default:
                    System.out.println("Cargo não identificado: " + logado.getPerfilUsuario());

            }
        }


    }

    private void configurarTabelaPartidas() {
        if (relatorioPartida == null) return;

        colStatus  .setCellValueFactory(c -> new SimpleStringProperty (c.getValue().getStatus()));
        colTime1   .setCellValueFactory(c -> new SimpleStringProperty (c.getValue().getTimeCasa().getPais()));
        colTime2   .setCellValueFactory(c -> new SimpleStringProperty (c.getValue().getTimeVisita().getPais()));
        colEstadio .setCellValueFactory(c -> new SimpleStringProperty (c.getValue().getEstadio().getNome()));
        colGolsT1  .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getGolCasa())   .asObject());
        colGolsT2  .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getGolVisita()) .asObject());

        // Público: ingressos vendidos para aquela partida
        colPublico .setCellValueFactory(c -> {
            int pub = oprIngresso.calcularPublicoPorPartida(c.getValue());
            return new SimpleIntegerProperty(pub).asObject();

        });

        colFaltas  .setCellValueFactory(c -> new SimpleIntegerProperty(0).asObject());


        colVencedor.setCellValueFactory(c -> {
            Partida p = c.getValue();
            if (!"Finalizada".equalsIgnoreCase(p.getStatus())) {
                return new SimpleStringProperty("-");

            }
            if (p.getGolCasa() > p.getGolVisita()) {
                return new SimpleStringProperty(p.getTimeCasa().getPais());

            } else if (p.getGolVisita() > p.getGolCasa()) {
                return new SimpleStringProperty(p.getTimeVisita().getPais());

            } else {
                return new SimpleStringProperty("Empate");

            }

        });

    }


    private void configurarTabelaDesempenho() {
        if (relatorioDesempenho == null) return;

        colDSelecao      .setCellValueFactory(c -> new SimpleStringProperty (c.getValue().getSelecao()));
        colDPontos       .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPontos())      .asObject());
        colDJogos        .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getJogos())       .asObject());
        colDVitorias     .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getVitorias())    .asObject());
        colDEmpates      .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getEmpates())     .asObject());
        colDDerrotas     .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getDerrotas())    .asObject());
        colDGols         .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getGols())        .asObject());
        colDGolsContra   .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getGolsContra())  .asObject());
        colDSaldo        .setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getSaldoGols())   .asObject());
        colDClassificacao.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getClassificacao()).asObject());

        if (infos != null) {
            colInfoPartidas.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTotalPartidas()));
            colInfoPublico .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getPublicoTotal()));
            colInfoGanho   .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getGanhoTotal()));

        }

    }

    private void carregarRelatorioPartidas() {
        if (relatorioPartida == null) return;
        List<Partida> lista = oprPartida.getListaPartidas();
        relatorioPartida.setItems(FXCollections.observableArrayList(lista));

    }


    private void carregarDesempenho() {
        List<Partida> partidas = oprPartida.getListaPartidas();

        Map<String, int[]> stats = new LinkedHashMap<>();

        for (Partida p : partidas) {
            if (!"Finalizada".equalsIgnoreCase(p.getStatus())) continue;

            String casa    = p.getTimeCasa().getPais();
            String visita  = p.getTimeVisita().getPais();
            int    gC      = p.getGolCasa();
            int    gV      = p.getGolVisita();

            stats.putIfAbsent(casa,   new int[7]);
            stats.putIfAbsent(visita, new int[7]);

            int[] sc = stats.get(casa);
            int[] sv = stats.get(visita);

            sc[1]++; sv[1]++;
            sc[5] += gC; sv[5] += gV;
            sc[6] += gV; sv[6] += gC;

            if (gC > gV) {
                sc[2]++; sc[0] += 3;
                sv[4]++;

            } else if (gV > gC) {
                sv[2]++; sv[0] += 3;
                sc[4]++;

            } else {
                sc[3]++; sc[0]++;
                sv[3]++; sv[0]++;

            }

        }
        List<Map.Entry<String, int[]>> ordenado = new ArrayList<>(stats.entrySet());
        ordenado.sort((a, b) -> {
            int[] sa = a.getValue(), sb = b.getValue();
            int saldoA = sa[5] - sa[6];
            int saldoB = sb[5] - sb[6];
            if (sb[0] != sa[0]) return sb[0] - sa[0];
            return saldoB - saldoA;

        });

        ObservableList<DesempenhoSelecao> desempenhos = FXCollections.observableArrayList();
        int pos = 1;
        for (Map.Entry<String, int[]> e : ordenado) {
            int[] s    = e.getValue();
            int saldo  = s[5] - s[6];
            desempenhos.add(new DesempenhoSelecao(e.getKey(), s[0], s[1], s[2], s[3], s[4], s[5], s[6], saldo, pos++));

        }

        if (relatorioDesempenho != null) {
            relatorioDesempenho.setItems(desempenhos);

        }

        // Resumo geral (tabela superior)
        if (infos != null) {
            int totalPartidas = (int) partidas.stream().filter(p -> "Finalizada".equalsIgnoreCase(p.getStatus())).count();
            int publicoTotal  = oprIngresso.calcularPublicoTotal();
            double ganhoTotal    = oprIngresso.calcularArrecadacaoTotal();

            ObservableList<ResumoGeral> resumo = FXCollections.observableArrayList(new ResumoGeral(String.valueOf(totalPartidas), String.valueOf(publicoTotal), String.format("R$ %.2f", ganhoTotal)));
            infos.setItems(resumo);

        }

    }


    private void esconderTodosOsPaineis() {
        if (painelInicial != null){
                painelInicial.setVisible(false);

        }
        if (partidaPanelAdm != null) {
            partidaPanelAdm.setVisible(false);

        }
        if (desempenhoPanelAdm != null) {
            desempenhoPanelAdm.setVisible(false);

        }
        if (partidaPanelOrg != null) {
            partidaPanelOrg.setVisible(false);

        }
        if (arbitroPanelOrg != null) {
            arbitroPanelOrg.setVisible(false);

        }
        if (selecaoPanelOrg != null) {
            selecaoPanelOrg.setVisible(false);

        }
        if (estadioPanelOrg != null) {
            estadioPanelOrg.setVisible(false);

        }
        if (ingressoPanelOp != null) {
            ingressoPanelOp.setVisible(false);

        }
        if (partidaPanelArb != null) {
            partidaPanelArb.setVisible(false);

        }

    }

    @FXML
    public void voltarInicio(ActionEvent event) {
        esconderTodosOsPaineis();
        if (painelInicial != null) {
            painelInicial.setVisible(true);
            painelInicial.toFront();

        }

    }

    @FXML
    public void mostrarPainelPartidas(ActionEvent event) {
        esconderTodosOsPaineis();
        if (partidaPanelAdm != null) {
            carregarRelatorioPartidas();
            partidaPanelAdm.setVisible(true);
            partidaPanelAdm.toFront();

        }

    }

    @FXML
    public void mostrarPainelDesempenho(ActionEvent event) {
        esconderTodosOsPaineis();
        if (desempenhoPanelAdm != null) {
            carregarDesempenho();
            desempenhoPanelAdm.setVisible(true);
            desempenhoPanelAdm.toFront();

        }

    }

    @FXML
    public void abrirGestaoUsuarios(ActionEvent event) {
        try {
            if (janelaGestaoAtiva != null && janelaGestaoAtiva.isShowing()) {
                janelaGestaoAtiva.toFront();
                return;

            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaGestaoUser.fxml"));
            Parent root = loader.load();

            janelaGestaoAtiva = new Stage();
            Scene scene = new Scene(root, 800, 600);
            if (getClass().getResource("/Interface/style.css") != null) {
                scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

                janelaGestaoAtiva.setScene(scene);
                janelaGestaoAtiva.setTitle("Gestão de Usuários - Admin");
                janelaGestaoAtiva.setResizable(false);
                janelaGestaoAtiva.show();
            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    @FXML
    public void mostrarPainelPartidasOrg(ActionEvent event) {
        navegarParaTela("/Interface/TelaPartidas.fxml", "Gerenciamento de Partidas", event);
    }

    @FXML
    public void mostrarPainelArbitrosOrg(ActionEvent event) {
        navegarParaTela("/Interface/TelaArbitros.fxml", "Gerenciamento de Árbitros", event);
    }

    @FXML
    public void mostrarPainelSelecaoOrg(ActionEvent event) {
        navegarParaTela("/Interface/TelaSelecoes.fxml", "Gerenciamento de Seleções", event);
    }

    @FXML
    public void mostrarPainelEstadiosOrg(ActionEvent event) {
        navegarParaTela("/Interface/TelaEstadios.fxml", "Gerenciamento de Estádios", event);

    }

    @FXML
    public void irParaTelaIngressos(ActionEvent event) {
        navegarParaTela("/Interface/TelaIngressos.fxml", "Gerenciamento de Ingressos", event);

    }

    private void navegarParaTela(String fxmlPath, String titulo, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root  = loader.load();
            Stage  stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene  scene = new Scene(root, 1920, 1080);

            if (getClass().getResource("/Interface/style.css") != null) scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());

            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle(titulo);
            stage.show();

        } catch (IOException e) {
            System.out.println("Erro ao abrir: " + fxmlPath);
            e.printStackTrace();

        }

    }

    @FXML
    public void mostrarPainelIngressosOp(ActionEvent event) {
        esconderTodosOsPaineis();

        if (ingressoPanelOp != null) {
            ingressoPanelOp.setVisible(true);
            ingressoPanelOp.toFront();

        }

    }

    @FXML
    public void mostrarPainelPartidasArb(ActionEvent event) {
        esconderTodosOsPaineis();

        if (partidaPanelArb != null) {
            partidaPanelArb.setVisible(true);
            partidaPanelArb.toFront();

        }

    }

    @FXML
    public void realizarLogout(ActionEvent event) {
        if (janelaGestaoAtiva != null && janelaGestaoAtiva.isShowing()) {
            janelaGestaoAtiva.close();
            janelaGestaoAtiva = null;

        }

        oprSessao.encerrar();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Interface/TelaLogin.fxml"));
            Parent root  = loader.load();
            Stage  stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            Scene  scene = new Scene(root, 1920, 1080);
            scene.getStylesheets().add(getClass().getResource("/Interface/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.setTitle("World Cup 2026 - Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}