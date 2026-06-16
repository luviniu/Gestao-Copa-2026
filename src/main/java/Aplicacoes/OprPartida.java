package Aplicacoes;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import Objetos.Partida;
import Objetos.Estadio;
import Objetos.Selecao;

public class OprPartida {

    private List<Partida> listaPartidas;
    private final String CAMINHO_ARQUIVO = "partidas.txt";

    public OprPartida() {
        this.listaPartidas = new ArrayList<>();
        carregarDadosDoArquivo();
    }

    public boolean cadastrarPartida(Selecao casa, Selecao visita, String estadio, String data, String horario, String fase) {
        if (casa == null || visita == null || estadio == null || estadio.isBlank() ||
                data == null || data.isBlank() || horario == null || horario.isBlank() || fase == null || fase.isBlank()) {
            return false;
        }

        // Validação da Regra de Negócio: Impede estouro de limite de jogos da árvore de mata-mata
        if (atingiuLimiteDeJogosDaFase(fase)) {
            return false;
        }

        // Impede que um time jogue contra si mesmo
        if (casa.getPais().equalsIgnoreCase(visita.getPais())) {
            return false;
        }

        // Validação da Regra de Negócio: Ambas as seleções precisam estar elegíveis
        if (!casa.isElegivel() || !visita.isElegivel()) {
            return false;
        }

        // Validação de duplicidade e choque de horário no estádio
        for (Partida p : listaPartidas) {
            if (p.getTimeCasa().getPais().equalsIgnoreCase(casa.getPais()) &&
                    p.getTimeVisita().getPais().equalsIgnoreCase(visita.getPais()) &&
                    p.getData().equalsIgnoreCase(data)) {
                return false;
            }
            if (p.getData().equalsIgnoreCase(data) &&
                    p.getHora().equalsIgnoreCase(horario) &&
                    p.getEstadio().getNome().equalsIgnoreCase(estadio)) {
                return false;
            }
        }

        Estadio estadioObj = new Estadio(estadio, "Local", 50000);
        Partida novaPartida = new Partida(data, horario, fase, "Agendada", estadioObj, casa, visita, null, 0, 0);
        listaPartidas.add(novaPartida);
        salvarDadosNoArquivo();

        return true;
    }

    private boolean atingiuLimiteDeJogosDaFase(String fase) {
        int contador = 0;

        // Conta quantas partidas já existem na fase selecionada
        for (Partida p : listaPartidas) {
            if (p.getFase().equalsIgnoreCase(fase.trim())) {
                contador++;
            }
        }

        // Aplica a regra de limite com base na fase
        switch (fase.trim()) {
            case "Final":
                return contador >= 1;
            case "Semifinal":
                return contador >= 2;
            case "Quartas":
                return contador >= 4;
            case "Oitavas":
                return contador >= 8;
            case "Dezesseis-avos":
                return contador >= 16;
            default:
                // Fases de grupos não possuem restrição rígida global neste nível
                return false;
        }
    }

    public boolean editarPartida(String dataOriginal, String horaOriginal, String novaData, String novoHorario, String novaFase) {
        for (Partida p : listaPartidas) {
            if (p.getData().equalsIgnoreCase(dataOriginal) && p.getHora().equalsIgnoreCase(horaOriginal)) {
                p.setData(novaData);
                p.setHora(novoHorario);
                p.setFase(novaFase);
                salvarDadosNoArquivo();
                return true;
            }
        }
        return false;
    }

    public boolean excluirPartida(String data, String horario) {
        for (Partida p : listaPartidas) {
            if (p.getData().equalsIgnoreCase(data) && p.getHora().equalsIgnoreCase(horario)) {
                listaPartidas.remove(p);
                salvarDadosNoArquivo();
                return true;
            }
        }
        return false;
    }

    // Filtro dinâmico para a barra de pesquisa
    public List<Partida> buscarPartidas(String termo) {
        if (termo == null || termo.isBlank()) {
            return listaPartidas;
        }
        List<Partida> filtradas = new ArrayList<>();
        String busca = termo.toLowerCase().trim();

        for (Partida p : listaPartidas) {
            if (p.getTimeCasa().getPais().toLowerCase().contains(busca) ||
                    p.getTimeVisita().getPais().toLowerCase().contains(busca) ||
                    p.getEstadio().getNome().toLowerCase().contains(busca) ||
                    p.getFase().toLowerCase().contains(busca) ||
                    p.getStatus().toLowerCase().contains(busca)) {
                filtradas.add(p);
            }
        }
        return filtradas;
    }

    public boolean registrarResultado(String data, String horario, int golsCasa, int golsVisita) {
        for (Partida p : listaPartidas) {
            if (p.getData().equalsIgnoreCase(data) && p.getHora().equalsIgnoreCase(horario)) {
                p.setGolCasa(golsCasa);
                p.setGolVisita(golsVisita);
                p.setStatus("Finalizada");
                salvarDadosNoArquivo();
                return true;
            }
        }
        return false;
    }

    public List<Partida> getListaPartidas() {
        return listaPartidas;
    }

    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Partida p : listaPartidas) {
                writer.write("PARTIDA;" + p.getTimeCasa().getPais() + ";" + p.getTimeVisita().getPais() + ";"
                        + p.getEstadio().getNome() + ";" + p.getData() + ";" + p.getHora() + ";"
                        + p.getFase() + ";" + p.getGolCasa() + ";" + p.getGolVisita() + ";" + p.getStatus());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarDadosDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) return;
        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados[0].equals("PARTIDA") && dados.length == 10) {
                    Estadio est = new Estadio(dados[3], "Local", 50000);
                    Selecao casa = new Selecao(dados[1], "A", "Tecnico", new ArrayList<>());
                    Selecao visita = new Selecao(dados[2], "A", "Tecnico", new ArrayList<>());
                    Partida part = new Partida(dados[4], dados[5], dados[6], dados[9], est, casa, visita,
                            null, Integer.parseInt(dados[7]), Integer.parseInt(dados[8]));
                    listaPartidas.add(part);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}