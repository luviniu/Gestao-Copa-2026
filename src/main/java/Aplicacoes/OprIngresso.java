package Aplicacoes;

import Objetos.Ingresso;
import Objetos.Partida;
import Objetos.Estadio;
import Objetos.Selecao;

import java.io.*;
import java.util.ArrayList;

public class OprIngresso {

    private ArrayList<Ingresso> ingressosVendidos = new ArrayList<>();
    private final String CAMINHO_ARQUIVO = "ingressos.txt";

    public OprIngresso() {
        carregarDadosDoArquivo();
    }

    private double calcularValorReal(String tipo) {
        if (tipo.equalsIgnoreCase("VIP")) return 200.0;
        if (tipo.equalsIgnoreCase("MEIA")) return 50.0;
        return 100.0; // NORMAL ou PADRAO
    }

    public void venderIngresso(String tipo, Partida partida, double valorBase, int quantidade) throws Exception {

        if (partida == null) {
            throw new Exception("A partida deve ser selecionada.");
        }
        if (partida.getEstadio() == null) {
            throw new Exception("A partida deve estar vinculada a um estádio.");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new Exception("A categoria do ingresso deve ser informada.");
        }
        if (quantidade <= 0) {
            throw new Exception("A quantidade deve ser maior que zero.");
        }
        if (partida.getStatus() == null || !partida.getStatus().equalsIgnoreCase("Agendada")) {
            throw new Exception("Ingressos só podem ser vendidos para partidas agendadas.");
        }

        int capacidade = partida.getEstadio().getVagas();
        int vendidosNaPartida = contarIngressosVendidosPorPartida(partida);

        if (vendidosNaPartida + quantidade > capacidade) {
            throw new Exception("Capacidade esgotada. Vendidos: " + vendidosNaPartida +
                    " | Disponíveis: " + (capacidade - vendidosNaPartida) +
                    " | Capacidade: " + capacidade);
        }

        double valorReal = calcularValorReal(tipo);

        for (int i = 0; i < quantidade; i++) {
            Ingresso ingresso = new Ingresso(tipo, partida, valorReal);
            ingressosVendidos.add(ingresso);
        }

        salvarDadosNoArquivo();
    }

    private boolean mesmaPartida(Partida a, Partida b) {
        if (a == null || b == null) return false;
        if (a.getData() == null || b.getData() == null) return false;
        if (a.getHora() == null || b.getHora() == null) return false;
        if (a.getTimeCasa() == null || b.getTimeCasa() == null) return false;
        if (a.getTimeVisita() == null || b.getTimeVisita() == null) return false;
        if (a.getTimeCasa().getPais() == null || b.getTimeCasa().getPais() == null) return false;
        if (a.getTimeVisita().getPais() == null || b.getTimeVisita().getPais() == null) return false;

        return a.getData().equalsIgnoreCase(b.getData()) &&
                a.getHora().equalsIgnoreCase(b.getHora()) &&
                a.getTimeCasa().getPais().equalsIgnoreCase(b.getTimeCasa().getPais()) &&
                a.getTimeVisita().getPais().equalsIgnoreCase(b.getTimeVisita().getPais());
    }

    public int contarIngressosVendidos() {
        return ingressosVendidos.size();
    }

    public int contarIngressosVendidosPorPartida(Partida partida) {
        int total = 0;

        for (Ingresso ingresso : ingressosVendidos) {
            if (mesmaPartida(ingresso.getPartida(), partida)) {
                total++;
            }
        }

        return total;
    }

    public int calcularPublicoTotal() {
        return ingressosVendidos.size();
    }

    public int calcularPublicoPorPartida(Partida partida) {
        return contarIngressosVendidosPorPartida(partida);
    }

    public double calcularArrecadacaoTotal() {
        double total = 0;

        for (Ingresso ingresso : ingressosVendidos) {
            total += ingresso.getValor();
        }

        return total;
    }

    public double calcularArrecadacaoPorPartida(Partida partida) {
        double total = 0;

        for (Ingresso ingresso : ingressosVendidos) {
            if (mesmaPartida(ingresso.getPartida(), partida)) {
                total += ingresso.getValor();
            }
        }

        return total;
    }

    public int calcularIngressosDisponiveisPorPartida(Partida partida) {
        if (partida == null || partida.getEstadio() == null) return 0;

        return partida.getEstadio().getVagas() - contarIngressosVendidosPorPartida(partida);
    }

    public void devolverIngresso(String tipo, Partida partida, int quantidade) throws Exception {

        if (partida == null) {
            throw new Exception("A partida deve ser selecionada.");
        }
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new Exception("A categoria do ingresso deve ser informada.");
        }
        if (quantidade <= 0) {
            throw new Exception("A quantidade deve ser maior que zero.");
        }

        int disponiveis = contarIngressosVendidosPorPartidaETipo(partida, tipo);

        if (disponiveis < quantidade) {
            throw new Exception("Não há ingressos suficientes do tipo " + tipo +
                    " para devolver. Disponíveis: " + disponiveis);
        }

        int removidos = 0;

        for (int i = ingressosVendidos.size() - 1; i >= 0 && removidos < quantidade; i--) {
            Ingresso ing = ingressosVendidos.get(i);

            if (mesmaPartida(ing.getPartida(), partida) && ing.getTipo().equalsIgnoreCase(tipo)) {
                ingressosVendidos.remove(i);
                removidos++;
            }
        }

        salvarDadosNoArquivo();
    }

    public int contarIngressosVendidosPorPartidaETipo(Partida partida, String tipo) {
        int total = 0;

        for (Ingresso ingresso : ingressosVendidos) {
            if (mesmaPartida(ingresso.getPartida(), partida) &&
                    ingresso.getTipo().equalsIgnoreCase(tipo)) {
                total++;
            }
        }

        return total;
    }

    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {

            for (Ingresso ing : ingressosVendidos) {
                Partida p = ing.getPartida();

                writer.write("INGRESSO;" +
                        ing.getTipo() + ";" +
                        ing.getValor() + ";" +
                        p.getTimeCasa().getPais() + ";" +
                        p.getTimeVisita().getPais() + ";" +
                        p.getData() + ";" +
                        p.getHora() + ";" +
                        p.getEstadio().getNome() + ";" +
                        p.getEstadio().getLocal() + ";" +
                        p.getEstadio().getVagas() + ";" +
                        p.getStatus());

                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar ingressos.");
            e.printStackTrace();
        }
    }

    private void carregarDadosDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;

            while ((linha = reader.readLine()) != null) {
                String[] d = linha.split(";");

                if (d[0].equals("INGRESSO") && d.length == 11) {
                    String tipo = d[1];
                    double valor = Double.parseDouble(d[2]);
                    String casa = d[3];
                    String visita = d[4];
                    String data = d[5];
                    String hora = d[6];
                    String estNome = d[7];
                    String estLoc = d[8];
                    int estVag = Integer.parseInt(d[9].trim());
                    String status = d[10];

                    Estadio est = new Estadio(estNome, estLoc, estVag);
                    Selecao sc = new Selecao(casa, "A", "Tecnico", new ArrayList<>());
                    Selecao sv = new Selecao(visita, "A", "Tecnico", new ArrayList<>());

                    Partida p = new Partida(data, hora, "", status, est, sc, sv, null, 0, 0);

                    Ingresso ing = new Ingresso(tipo, p, valor);
                    ingressosVendidos.add(ing);
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar ingressos.");
            e.printStackTrace();
        }
    }
}