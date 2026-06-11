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
        listaPartidas = new ArrayList<>();
        carregarDadosDoArquivo();
    }

    public boolean cadastrarPartida(
            String selecao1,
            String selecao2,
            String estadio,
            String data,
            String horario,
            String fase) {

        if (selecao1 == null || selecao2 == null) {
            return false;
        }

        Estadio estadioObj = new Estadio(estadio, "Local", 50000);

        Selecao casa = new Selecao(selecao1, "A", "Tecnico", new ArrayList<>());

        Selecao visita = new Selecao(selecao2, "A", "Tecnico", new ArrayList<>());

        Partida novaPartida = new Partida(
                data,
                horario,
                fase,
                "Agendada",
                estadioObj,
                casa,
                visita,
                null,
                0,
                0
        );

        listaPartidas.add(novaPartida);

        salvarDadosNoArquivo();

        return true;
    }

    public boolean editarPartida(
            String data,
            String novaData,
            String novoHorario,
            String novaFase) {

        for (Partida p : listaPartidas) {

            if (p.getData().equalsIgnoreCase(data)) {

                p.setData(novaData);
                p.setHora(novoHorario);
                p.setFase(novaFase);

                salvarDadosNoArquivo();

                return true;
            }
        }

        return false;
    }

    public boolean excluirPartida(String data) {

        for (Partida p : listaPartidas) {

            if (p.getData().equalsIgnoreCase(data)) {

                listaPartidas.remove(p);

                salvarDadosNoArquivo();

                return true;
            }
        }

        return false;
    }

    public List<Partida> consultarPartidas() {
        return listaPartidas;
    }

    public List<Partida> getListaPartidas() {
        return listaPartidas;
    }

    public void salvarDadosNoArquivo() {

        try (BufferedWriter writer =
                     new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {

            for (Partida p : listaPartidas) {

                writer.write(
                        "PARTIDA;"
                                + p.getTimeCasa().getPais() + ";"
                                + p.getTimeVisita().getPais() + ";"
                                + p.getEstadio().getNome() + ";"
                                + p.getData() + ";"
                                + p.getHora() + ";"
                                + p.getFase()
                );

                writer.newLine();
            }

        } catch (IOException e) {

            System.out.println("Erro ao salvar partidas.");
            e.printStackTrace();
        }
    }

    private void carregarDadosDoArquivo() {

        File arquivo = new File(CAMINHO_ARQUIVO);

        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {

            String linha;

            while ((linha = reader.readLine()) != null) {

                String[] dados = linha.split(";");

                if (dados[0].equals("PARTIDA") && dados.length == 7) {

                    String casa = dados[1];
                    String visita = dados[2];
                    String estadio = dados[3];
                    String data = dados[4];
                    String hora = dados[5];
                    String fase = dados[6];

                    Estadio estadioObj =
                            new Estadio(estadio, "Local", 50000);

                    Selecao casaObj =
                            new Selecao(casa, "A", "Tecnico", new ArrayList<>());

                    Selecao visitaObj =
                            new Selecao(visita, "A", "Tecnico", new ArrayList<>());

                    Partida partida = new Partida(
                            data,
                            hora,
                            fase,
                            "Agendada",
                            estadioObj,
                            casaObj,
                            visitaObj,
                            null,
                            0,
                            0
                    );

                    listaPartidas.add(partida);
                }
            }

        } catch (IOException e) {

            System.out.println("Erro ao carregar partidas.");
            e.printStackTrace();
        }
    }
}