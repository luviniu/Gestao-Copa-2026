package Aplicacoes;

import java.util.ArrayList;
import java.util.List;
import java.io.*;

import Objetos.Partida;
import Objetos.Estadio;
import Objetos.Selecao;
import Objetos.Arbitro;

public class OprPartida {

    private List<Partida> listaPartidas;
    private final String CAMINHO_ARQUIVO = "partidas.txt";

    public OprPartida() {
        this.listaPartidas = new ArrayList<>();
        carregarDadosDoArquivo();
    }

    public boolean cadastrarPartida(Selecao casa, Selecao visita, String estadio, String data, String horario, String fase, String nomeArbitro) {
        if (casa == null || visita == null || estadio == null || estadio.isBlank() ||
                data == null || data.isBlank() || horario == null || horario.isBlank() ||
                fase == null || fase.isBlank() || nomeArbitro == null || nomeArbitro.isBlank()) {
            return false;
        }

        // Busca polimórfica pelo nome do árbitro no cadastro
        OprArbitro oprArbitro = OprArbitro.getInstancia();
        Arbitro arbitroEncontrado = null;

        for (Arbitro a : oprArbitro.getListaArbitros()) {
            if (a.getNome().equalsIgnoreCase(nomeArbitro.trim())) {
                arbitroEncontrado = a;
                break;
            }
        }

        if (arbitroEncontrado == null) {
            System.out.println("Erro de Negócio: Não existe um árbitro cadastrado com o nome: " + nomeArbitro);
            return false;
        }

        if (atingiuLimiteDeJogosDaFase(fase)) {
            return false;
        }

        if (casa.getPais().equalsIgnoreCase(visita.getPais())) {
            return false;
        }

        if (!casa.isElegivel() || !visita.isElegivel()) {
            return false;
        }

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

        Partida novaPartida = new Partida(data, horario, fase, "Agendada", estadioObj, casa, visita, arbitroEncontrado, 0, 0);
        listaPartidas.add(novaPartida);
        salvarDadosNoArquivo();

        return true;
    }

    private boolean atingiuLimiteDeJogosDaFase(String fase) {
        int contador = 0;

        for (Partida p : listaPartidas) {
            if (p.getFase().equalsIgnoreCase(fase.trim())) {
                contador++;
            }
        }

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
                return false;
        }
    }

    // CORRIGIDO: Agora busca, valida e atualiza o objeto Arbitro real na edição
    public boolean editarPartida(String dataOriginal, String horaOriginal, String novaData, String novoHorario, String novaFase, String nomeArbitro) {
        OprArbitro oprArbitro = OprArbitro.getInstancia();
        Arbitro novoArbitroEncontrado = null;

        for (Arbitro a : oprArbitro.getListaArbitros()) {
            if (a.getNome().equalsIgnoreCase(nomeArbitro.trim())) {
                novoArbitroEncontrado = a;
                break;
            }
        }

        if (novoArbitroEncontrado == null) {
            System.out.println("Erro de Negócio: Não existe um árbitro cadastrado com o nome: " + nomeArbitro);
            return false;
        }

        for (Partida p : listaPartidas) {
            if (p.getData().equalsIgnoreCase(dataOriginal) && p.getHora().equalsIgnoreCase(horaOriginal)) {
                p.setData(novaData);
                p.setHora(novoHorario);
                p.setFase(novaFase);
                p.setArbitro(novoArbitroEncontrado); // Atualiza a referência do objeto

                salvarDadosNoArquivo();
                return true;
            }
        }
        return false;
    }

    // CORRIGIDO: Remoção segura fora do laço para evitar ConcurrentModificationException
    public boolean excluirPartida(String data, String horario) {
        Partida partidaParaRemover = null;
        for (Partida p : listaPartidas) {
            if (p.getData().equalsIgnoreCase(data) && p.getHora().equalsIgnoreCase(horario)) {
                partidaParaRemover = p;
                break;
            }
        }

        if (partidaParaRemover != null) {
            listaPartidas.remove(partidaParaRemover);
            salvarDadosNoArquivo();
            return true;
        }
        return false;
    }

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
                    (p.getArbitro() != null && p.getArbitro().getNome().toLowerCase().contains(busca)) || // Padronizado para getArbitro()
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
                String nomeArbitroSalvar = "Sem Arbitro";
                if (p.getArbitro() instanceof Arbitro) { // Padronizado para getArbitro()
                    nomeArbitroSalvar = p.getArbitro().getNome();
                }

                writer.write("PARTIDA;" + p.getTimeCasa().getPais() + ";" + p.getTimeVisita().getPais() + ";"
                        + p.getEstadio().getNome() + ";" + p.getData() + ";" + p.getHora() + ";"
                        + p.getFase() + ";" + p.getGolCasa() + ";" + p.getGolVisita() + ";"
                        + p.getStatus() + ";" + nomeArbitroSalvar);
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
                if (dados[0].equals("PARTIDA") && dados.length == 11) {
                    Estadio est = new Estadio(dados[3], "Local", 50000);
                    Selecao casa = new Selecao(dados[1], "A", "Tecnico", new ArrayList<>());
                    Selecao visita = new Selecao(dados[2], "A", "Tecnico", new ArrayList<>());

                    String nomeArbitro = dados[10];
                    Arbitro arbitroVinculado = null;

                    for (Arbitro a : OprArbitro.getInstancia().getListaArbitros()) {
                        if (a.getNome().equalsIgnoreCase(nomeArbitro.trim())) {
                            arbitroVinculado = a;
                            break;
                        }
                    }

                    Partida part = new Partida(dados[4], dados[5], dados[6], dados[9], est, casa, visita,
                            arbitroVinculado, Integer.parseInt(dados[7]), Integer.parseInt(dados[8]));
                    listaPartidas.add(part);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}