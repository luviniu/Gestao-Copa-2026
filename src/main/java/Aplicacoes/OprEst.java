package Aplicacoes;

import Objetos.Partida;
import Objetos.Estadio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OprEst {
    private List<Estadio> listaEstadio;
    private final String CAMINHO_ARQUIVO = "estadio.txt";

    private static OprEst instancia;

    public static OprEst getInstancia() {
        if (instancia == null) {
            instancia = new OprEst();
        }
        return instancia;
    }

    public OprEst() {
        this.listaEstadio = new ArrayList<>();
        carregarDadosDoArquivo();
    }

    public boolean cadastrarEstadio(String nome, String local, int vagas) {

        if (nome == null || nome.trim().isEmpty() || local  == null || local.trim().isEmpty() || vagas <= 0){
            return false;
        }

        // Validao: No permitir dois pases com o mesmo nome
        for (Estadio s : listaEstadio) {
            if (s.getNome().equalsIgnoreCase(nome.trim())) {
                System.out.println("Erro de Negcio: Esse estadio ja esta cadastrado.");
                return false;
            }
        }

        Estadio novoEstadio = new Estadio(nome.trim(), local.trim(), vagas);
        listaEstadio.add(novoEstadio);

        salvarDadosNoArquivo();
        return true;
    }

    public boolean editarEstadio(String nomeAtual, String novoNome, String localParaEditar, int vagasParaEditar) {
        if (nomeAtual == null || novoNome == null || localParaEditar == null || localParaEditar.trim().isEmpty() || vagasParaEditar <= 0){
            return false;
        }

        for (Estadio s : listaEstadio) {
            if (s.getNome().equalsIgnoreCase(nomeAtual.trim())) {

                s.setNome(novoNome);
                s.setLocal(localParaEditar);
                s.setVagas(vagasParaEditar);

                salvarDadosNoArquivo();
                return true;
            }
        }

        System.out.println("Erro de Negocio: Estadio nao encontrada para edicao.");
        return false;
    }

    // Método Excluir Estadio
    public boolean excluirEstadio(String nomeParaExcluir) {
        if (nomeParaExcluir == null) {
            return false;
        }

        for (Estadio s : listaEstadio) {
            if (s.getNome().equalsIgnoreCase(nomeParaExcluir.trim())) {
                listaEstadio.remove(s);
                salvarDadosNoArquivo();
                return true;
            }
        }
        return false;
    }
    // Método buscar um estádio pelo nome
    public Estadio buscarEstadioPorNome(String nome) {
        for (Estadio e : listaEstadio) {
            if (e.getNome().equalsIgnoreCase(nome.trim())) {
                return e;
            }
        }
        return null;
    }

    public List<Estadio> getListaEstadio() {
        return this.listaEstadio;
    }

    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {

            for (Estadio s : listaEstadio) {
                // Salvando os dados identificados pela tag ESTADIO
                writer.write("ESTADIO;" + s.getNome() + ";" + s.getLocal() + ";" + s.getVagas());
                writer.newLine();
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar dados no arquivo TXT.");
            e.printStackTrace();
        }
    }

    private void carregarDadosDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            Estadio estadioAtual = null;

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados[0].equals("ESTADIO") && dados.length == 4) {
                    String nome = dados[1];
                    String local = dados[2];
                    int vagas = Integer.parseInt(dados[3].trim());

                    estadioAtual = new Estadio(nome, local, vagas);
                    listaEstadio.add(estadioAtual);
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar dados do arquivo TXT.");
            e.printStackTrace();
        }
    }
}
