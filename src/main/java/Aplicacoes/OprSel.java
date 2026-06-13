package Aplicacoes;

import Objetos.Selecao;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/* NOTE!!
- As REGRAS para cadastrar seleção: campos preenchidos, nenhuma dúplica
- Métodos de PERSISTÊNCIA: arquivo .txt pra guardar lista de seleções+grupo+técnico
    Usar para conferir se dados estão duplicados
    Carregar o .txt numa lista ao abrir o programa. Retornar Exception se der ruim
 */
public class OprSel {

    private static OprSel instancia;

    // Lista que contém as seleções
    private List<Selecao> listaSelecoes;
    private final String CAMINHO_ARQUIVO = "selecoes.txt"; // Mantido na raiz do projeto

    // Construtor da classe, já faz o upload do .txt
    private OprSel() {
        this.listaSelecoes = new ArrayList<>();
        carregarDadosDoArquivo(); // Toda vez que o sistema inicia, ele lê o TXT
    }

    // O método estático público que o Controller da tela vai chamar
    public static synchronized OprSel getInstancia() {
        if (instancia == null) {
            instancia = new OprSel();
        }
        return instancia;
    }

    // --------------------------------------------------
    // MÉTODOS DE NEGÓCIO E BUSCA
    // --------------------------------------------------

    // Método utilitário essencial para o OprJog vincular os objetos em memória
    public Selecao buscarSelecaoPorNome(String nomePais) {
        if (nomePais == null) return null;
        for (Selecao s : listaSelecoes) {
            if (s.getPais().equalsIgnoreCase(nomePais.trim())) {
                return s;
            }
        }
        return null;
    }

    // Cadastrar Seleção
    public boolean cadastrarSelecao(String pais, String grupo, String tecnico) {
        // Validação: Campos não podem ser vazios
        if (pais == null || pais.trim().isEmpty() || tecnico == null || tecnico.trim().isEmpty()) {
            return false;
        }

        // Validação 2: Não aceitar números nas entradas de País e Técnico
        if (!pais.matches("^[a-zA-ZÀ-ÿ\\s]+$") || !tecnico.matches("^[a-zA-ZÀ-ÿ\\s]+$")) {
            System.out.println("Erro: Os campos País e Técnico não devem conter números ou caracteres especiais!");
            return false;
        }

        // Validação 3: Não permitir dois países com o mesmo nome
        if (buscarSelecaoPorNome(pais) != null) {
            System.out.println("Erro de Negócio: Essa seleção já está cadastrada.");
            return false;
        }

        // Cria o objeto puro (com uma lista de jogadores vazia na memória)
        Selecao novaSelecao = new Selecao(pais.trim(), grupo, tecnico.trim(), new ArrayList<>());
        listaSelecoes.add(novaSelecao);

        // Salva a lista de seleções de forma persistente
        salvarDadosNoArquivo();
        return true;
    }

    // Editar Seleção
    public boolean editarSelecao(String paisParaEditar, String novoGrupo, String novoTecnico) {
        if (paisParaEditar == null || novoTecnico == null || novoTecnico.trim().isEmpty()) {
            return false;
        }

        Selecao s = buscarSelecaoPorNome(paisParaEditar);
        if (s != null) {
            s.setGrupo(novoGrupo);
            s.setTecnico(novoTecnico.trim());

            salvarDadosNoArquivo();
            return true; // Edição feita com sucesso!
        }

        System.out.println("Erro de Negócio: Seleção não encontrada para edição.");
        return false;
    }

    // Método Excluir Seleção
    public boolean excluirSelecao(String paisParaExcluir) {
        Selecao s = buscarSelecaoPorNome(paisParaExcluir);
        if (s != null) {
            listaSelecoes.remove(s);
            salvarDadosNoArquivo(); // Atualiza o TXT limpando a seleção excluída
            return true;
        }
        return false;
    }

    // Método Consultar Seleções por Critério (Grupo)
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

    // --------------------------------------------------
    // MÉTODOS DE PERSISTÊNCIA (FOCADOS APENAS EM SELEÇÕES)
    // --------------------------------------------------

    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {
            // Agora salvamos apenas os dados da seleção pura. Não precisamos mais da tag "SELECAO;"
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

                // Como o arquivo só guarda seleções, cada linha válida tem tamanho 3
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