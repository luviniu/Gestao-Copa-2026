package Aplicacoes;

import Objetos.Selecao;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OprSel {

    private static OprSel instancia;

    private List<Selecao> listaSelecoes;
    private final String CAMINHO_ARQUIVO = "selecoes.txt";

    private OprSel() {
        this.listaSelecoes = new ArrayList<>();
        carregarDadosDoArquivo();
    }

    public static synchronized OprSel getInstancia() {
        if (instancia == null) {
            instancia = new OprSel();
        }
        return instancia;
    }

    public Selecao buscarSelecaoPorNome(String nomePais) {
        if (nomePais == null) return null;
        for (Selecao s : listaSelecoes) {
            if (s.getPais().equalsIgnoreCase(nomePais.trim())) {
                return s;
            }
        }
        return null;
    }

    public boolean cadastrarSelecao(String pais, String grupo, String tecnico) {
        if (pais == null || pais.trim().isEmpty() || tecnico == null || tecnico.trim().isEmpty()) {
            return false;
        }

        if (!pais.matches("^[a-zA-Zá-úÁ-Ú\\s]+$") || !tecnico.matches("^[a-zA-Zá-úÁ-Ú\\s]+$")) {
            System.out.println("Erro: Os campos País e Técnico não devem conter números ou caracteres especiais!");
            return false;
        }

        if (buscarSelecaoPorNome(pais) != null) {
            System.out.println("Erro de Negócio: Essa seleção já está cadastrada.");
            return false;
        }

        Selecao novaSelecao = new Selecao(pais.trim(), group, tecnico.trim(), new ArrayList<>());
        listaSelecoes.add(novaSelecao);

        salvarDadosNoArquivo();
        return true;
    }

    public boolean editarSelecao(String paisParaEditar, String novoGrupo, String novoTecnico) {
        if (paisParaEditar == null || novoTecnico == null || novoTecnico.trim().isEmpty()) {
            return false;
        }

        Selecao s = buscarSelecaoPorNome(paisParaEditar);
        if (s != null) {
            s.setGrupo(novoGrupo);
            s.setTecnico(novoTecnico.trim());

            salvarDadosNoArquivo();
            return true;
        }

        System.out.println("Erro de Negócio: Seleção não encontrada para edição.");
        return false;
    }

    public boolean excluirSelecao(String paisParaExcluir) {
        Selecao s = buscarSelecaoPorNome(paisParaExcluir);
        if (s != null) {
            listaSelecoes.remove(s);
            salvarDadosNoArquivo();
            return true;
        }
        return false;
    }

    public List<Selecao> consultarSelecoesPorGrupo(String grupo) {
        List<Selecao> filtradas = new ArrayList<>();
        if (grupo == null) return filtradas;

        for (Selecao s : listaSelecoes) {
            if (s.getGrupo().equalsIgnoreCase(grupo.trim())) {
                filtradas.add(s);
            }
        }
        return filtradas;
    }

    public List<Selecao> getListaSelecoes() {
        return this.listaSelecoes;
    }

    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            for (Selecao s : listaSelecoes) {
                writer.write(s.getPais() + ";" + s.getGrupo() + ";" + s.getTecnico());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados no arquivo de seleções.");
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
            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados.length == 3) {
                    String pais = dados[0];
                    String grupo = dados[1];
                    String tecnico = dados[2];

                    Selecao s = new Selecao(pais, grupo, tecnico, new ArrayList<>());
                    listaSelecoes.add(s);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados do arquivo de seleções.");
            e.printStackTrace();
        }
    }
}