package Aplicacoes;

import Objetos.Selecao;
import Objetos.Jogador;
import java.util.ArrayList;
import java.util.List;

public class OprJog {

    private OprSel oprSel; // Dependência do gerenciador de seleções

    // O construtor exige receber o OprSel que já tem as seleções carregadas na memória
    public OprJog(OprSel oprSel) {
        this.oprSel = oprSel;
    }

    // Cadastrar e Associar jogador a uma seleção
    public boolean cadastrarJogador(String nome, String posicao, int numero, int idade, String nomeSelecao, String status) {
        if (nome == null || nome.trim().isEmpty() || posicao == null || posicao.trim().isEmpty()) {
            return false;
        }

        // Busca a seleção onde o jogador vai jogar
        for (Selecao s : oprSel.getListaSelecoes()) {
            if (s.getPais().equalsIgnoreCase(nomeSelecao.trim())) {

                // Validação: Evitar número duplicado na mesma seleção
                for (Jogador j : s.getTime()) {
                    if (j.getNumero() == numero) {
                        System.out.println("Erro: Esse número já está sendo usado nesta seleção.");
                        return false;
                    }
                }

                Jogador novoJogador = new Jogador(nome.trim(), numero, posicao, idade, s, status);
                s.addTime(novoJogador);

                oprSel.salvarDadosNoArquivo(); // Grava a alteração no TXT através do OprSel
                return true;
            }
        }
        return false; // Casoa Seleção não for encontrada
    }

    // Editar dados do Jogador e Controlar Status
    public boolean editarJogador(String nomeSelecao, String nomeJogador, String novaPosicao, int novoNumero, int novaIdade, String novoStatus) {
        for (Selecao s : oprSel.getListaSelecoes()) {
            if (s.getPais().equalsIgnoreCase(nomeSelecao.trim())) {
                for (Jogador j : s.getTime()) {
                    if (j.getNome().equalsIgnoreCase(nomeJogador.trim())) {

                        j.setPosicao(novaPosicao);
                        j.setNumero(novoNumero);
                        j.setIdade(novaIdade);
                        j.setStatus(novoStatus);

                        oprSel.salvarDadosNoArquivo();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Excluir Jogador
    public boolean excluirJogador(String nomeSelecao, String nomeJogador) {
        for (Selecao s : oprSel.getListaSelecoes()) {
            if (s.getPais().equalsIgnoreCase(nomeSelecao.trim())) {
                for (Jogador j : s.getTime()) {
                    if (j.getNome().equalsIgnoreCase(nomeJogador.trim())) {
                        s.delTime(j);
                        oprSel.salvarDadosNoArquivo();
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Consultar jogadores por critérios (Posição e/ou Status)
    public List<Jogador> consultarJogadoresPorCriterio(String posicao, String status) {
        List<Jogador> filtrados = new ArrayList<>();
        for (Selecao s : oprSel.getListaSelecoes()) {
            for (Jogador j : s.getTime()) {
                boolean batePosicao = (posicao == null || posicao.isEmpty() || j.getPosicao().equalsIgnoreCase(posicao));
                boolean bateStatus = (status == null || status.isEmpty() || j.getStatus().equalsIgnoreCase(status));

                if (batePosicao && bateStatus) {
                    filtrados.add(j);
                }
            }
        }
        return filtrados;
    }
}