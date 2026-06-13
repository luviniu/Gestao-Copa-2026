package Aplicacoes;

import Objetos.Selecao;
import Objetos.Jogador;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OprJog {

    private OprSel oprSel;
    private final String CAMINHO_ARQUIVO = "jogadores.txt";

    public OprJog(OprSel oprSel) {
        this.oprSel = oprSel;
        // Não precisamos chamar carregar aqui se fizermos o carregamento centralizado
        // após as seleções existirem na memória.
    }

    // --- MÉTODOS DE SALVAMENTO E CARREGAMENTO DE ARQUIVO ---

    public void salvarJogadoresNoArquivo() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Selecao s : oprSel.getListaSelecoes()) {
                for (Jogador j : s.getTime()) {
                    // Salva os dados separados por ";" incluindo o nome da seleção como vínculo
                    bw.write(String.format("%s;%d;%s;%d;%s;%s",
                            j.getNome(),
                            j.getNumero(),
                            j.getPosicao(),
                            j.getIdade(),
                            s.getPais(),
                            j.getStatus()));
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo de jogadores: " + e.getMessage());
        }
    }

    public void carregarJogadoresDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) return;

        // Limpa os elencos atuais para evitar duplicados ao recarregar
        for (Selecao s : oprSel.getListaSelecoes()) {
            s.getTime().clear();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(";");
                if (dados.length == 6) {
                    String nome = dados[0];
                    int numero = Integer.parseInt(dados[1]);
                    String posicao = dados[2];
                    int idade = Integer.parseInt(dados[3]);
                    String nomeSelecao = dados[4];
                    String status = dados[5];

                    // Busca a seleção na memória para reconstruir o vínculo de objetos
                    Selecao s = oprSel.buscarSelecaoPorNome(nomeSelecao);
                    if (s != null) {
                        Jogador jogador = new Jogador(nome, numero, posicao, idade, s, status);
                        s.addTime(jogador); // Associa o jogador à sua respectiva seleção em memória
                    }
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar o arquivo de jogadores: " + e.getMessage());
        }
    }

    // --- OPERAÇÕES DE NEGÓCIO ATUALIZADAS ---

    public boolean cadastrarJogador(String nome, String posicao, int numero, int idade, String nomeSelecao, String status) {
        if (nome == null || nome.trim().isEmpty() || posicao == null || posicao.trim().isEmpty()) {
            return false;
        }

        Selecao s = oprSel.buscarSelecaoPorNome(nomeSelecao);
        if (s == null) return false;

        // Validação: Evitar número duplicado na mesma seleção
        for (Jogador j : s.getTime()) {
            if (j.getNumero() == numero) {
                return false;
            }
        }

        Jogador novoJogador = new Jogador(nome.trim(), numero, posicao, idade, s, status);
        s.addTime(novoJogador);

        salvarJogadoresNoArquivo(); // Agora grava apenas no arquivo de jogadores!
        return true;
    }

    public boolean editarJogador(String nomeSelecao, String nomeJogador, String novaPosicao, int novoNumero, int novaIdade, String novoStatus) {
        Selecao s = oprSel.buscarSelecaoPorNome(nomeSelecao);
        if (s == null) return false;

        for (Jogador j : s.getTime()) {
            if (j.getNome().equalsIgnoreCase(nomeJogador.trim())) {
                j.setPosicao(novaPosicao);
                j.setNumero(novoNumero);
                j.setIdade(novaIdade);
                j.setStatus(novoStatus);

                salvarJogadoresNoArquivo(); // Atualiza o arquivo específico
                return true;
            }
        }
        return false;
    }

    public boolean excluirJogador(String nomeSelecao, String nomeJogador) {
        Selecao s = oprSel.buscarSelecaoPorNome(nomeSelecao);
        if (s == null) return false;

        for (Jogador j : s.getTime()) {
            if (j.getNome().equalsIgnoreCase(nomeJogador.trim())) {
                s.delTime(j);
                salvarJogadoresNoArquivo(); // Atualiza o arquivo específico
                return true;
            }
        }
        return false;
    }

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