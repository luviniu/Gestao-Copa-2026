package Aplicacoes;

import Objetos.Ingresso;
import Objetos.Partida;

import java.util.ArrayList;

public class OprIngresso {

    private ArrayList<Ingresso> ingressosVendidos = new ArrayList<>();

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
        if (valorBase <= 0) {
            throw new Exception("O valor do ingresso deve ser maior que zero.");
        }
        if (!partida.getStatus().equalsIgnoreCase("Agendada")) {
            throw new Exception("Ingressos só podem ser vendidos para partidas agendadas.");
        }
        int capacidade = partida.getEstadio().getVagas();
        int vendidos = ingressosVendidos.size();
        if (vendidos + quantidade > capacidade) {
            throw new Exception("A quantidade de ingressos excede a capacidade do estádio.");
        }
        for (int i = 0; i < quantidade; i++) {
            Ingresso ingresso = new Ingresso(tipo, partida, valorBase);
            ingressosVendidos.add(ingresso);
        }
    }
    public int contarIngressosVendidos() {
        return ingressosVendidos.size();
    }
}