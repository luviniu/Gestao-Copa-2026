package Aplicacoes;

import Objetos.Selecao;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Controla principalmente as régras de negócio
public class OprSel {

    // Apenas uma instância na memória (private, static)
    private static OprSel instancia;

    // A lista de seleções em memória (uma única por instância de OprSel)
    private List<Selecao> listaSelecoes;

    // O arquivo txt de armazenamento
    private final String CAMINHO_ARQUIVO = "selecoes.txt";

    // Apenas Opr pode criar e carregar dados na memória
    private OprSel() {
        this.listaSelecoes = new ArrayList<>();
        carregarDadosDoArquivo();
    }

    // OprSel cria a instância se já não estiver criada
    public static synchronized OprSel getInstancia() {
        if (instancia == null) {
            instancia = new OprSel();
        }
        return instancia;
    }

    /* ---------------------------------------------------- */
    /* ---------------------- MÉTODOS --------------------- */
    /* ---------------------------------------------------- */

    // Buscar na lista de seleções
    public Selecao buscarSelecaoPorNome(String nomePais) {
        if (nomePais == null) return null;
        for (Selecao s : listaSelecoes) {
            if (s.getPais().equalsIgnoreCase(nomePais.trim())) {
                return s;
            }
        }
        return null;
    }

    // Cadastrar uma seleção nova
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

        Selecao novaSelecao = new Selecao(pais.trim(), grupo, tecnico.trim(), new ArrayList<>());
        listaSelecoes.add(novaSelecao);

        salvarDadosNoArquivo();
        return true;
    }

    // Editar um seleção
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

    // Excluir uma seleção
    public boolean excluirSelecao(String paisParaExcluir) {
        Selecao s = buscarSelecaoPorNome(paisParaExcluir);
        if (s != null) {
            listaSelecoes.remove(s);
            salvarDadosNoArquivo();
            return true;
        }
        return false;
    }

    // Getter da lista de Seleções
    public List<Selecao> getListaSelecoes() {
        return this.listaSelecoes;
    }

    // Mecanismo de persistência que salva dados no .txt
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

    // Mecanismo de persistência que carrega dados no .txt
    private void carregarDadosDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            // Varre o arquivo linha por linha
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