package Aplicacoes;

import java.util.ArrayList;
import java.util.List;
import Objetos.Partida;
import Objetos.Estadio;
import Objetos.Selecao;

public class OprPartida {

    private List<Partida> listaPartidas;

    public OprPartida() {
        listaPartidas = new ArrayList<>();
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

                return true;
            }
        }

        return false;
    }

    public boolean excluirPartida(String data) {

        for (Partida p : listaPartidas) {

            if (p.getData().equalsIgnoreCase(data)) {

                listaPartidas.remove(p);
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
}