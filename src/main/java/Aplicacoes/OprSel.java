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

- Por enquanto é isso, me fala se for mexer!!!!
 */

public class OprSel {

    // Lista que contém as seleções
    private List<Selecao> listaSelecoes;
    private final String CAMINHO_ARQUIVO = "selecoes.txt"; // O arquivo vai ficar na raiz do projeto (é final pra ninguém poder mexer)

    // Construtor da classe, já faz o upload do .txt
    public OprSel() {
        this.listaSelecoes = new ArrayList<>();
        carregarDadosDoArquivo(); // Toda vez que o sistema inicia, ele lê o TXT
    }

    // Cadastrar Seleção
    public boolean cadastrarSelecao(String pais, String grupo, String tecnico) {

        // Validação: Campos não podem ser vazios
        if (pais == null || pais.trim().isEmpty() || tecnico == null || tecnico.trim().isEmpty()) {
            return false;
        }

        // Validação: Não permitir dois países com o mesmo nome
        for (Selecao s : listaSelecoes) {
            if (s.getPais().equalsIgnoreCase(pais.trim())) {
                System.out.println("Erro de Negócio: Essa seleção já está cadastrada.");
                return false;
            }
        }

        // Se passou nas validações, já cria o objeto e adiciona na lista (trim é pra tirar os espaços nas entradas)
        Selecao novaSelecao = new Selecao(pais.trim(), grupo, tecnico.trim(), new ArrayList<>());
        listaSelecoes.add(novaSelecao);

        // Salva a lista atualizada no arquivo TXT de forma persistente
        salvarDadosNoArquivo();
        return true;
    }

    // Editar Seleção
    public boolean editarSelecao(String paisParaEditar, String novoGrupo, String novoTecnico) {
        // Validação
        if (paisParaEditar == null || novoTecnico == null || novoTecnico.trim().isEmpty()) {
            return false;
        }

        // Procurar a seleção na  lista em memória
        for (Selecao s : listaSelecoes) {
            // Se encontrarmos o país (ignorando maiúsculas/minúsculas)
            if (s.getPais().equalsIgnoreCase(paisParaEditar.trim())) {

                // 3. Aplica as alterações usando os setters da sua classe Selecao
                s.setGrupo(novoGrupo);
                s.setTecnico(novoTecnico.trim());

                // 4. Atualiza o arquivo TXT com os novos dados salvos
                salvarDadosNoArquivo();
                return true; // Edição feita com sucesso!
            }
        }

        // Se percorreu a lista toda e não achou o país
        System.out.println("Erro de Negócio: Seleção não encontrada para edição.");
        return false;
    }

    // Caso for preciso retornar a lista de seleções, quem sabe
    public List<Selecao> getListaSelecoes() {
        return this.listaSelecoes;
    }

    // Gravação no arquivo TXT (Persistência de Dados)
    private void salvarDadosNoArquivo() {

        // BufferedWriter é o tipo que faz isso
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {

            // Ele percorre a lista linha por linha
            for (Selecao s : listaSelecoes) {
                // Salvando os dados separados por um caractere especial (ex: ';')
                writer.write(s.getPais() + ";" + s.getGrupo() + ";" + s.getTecnico());
                writer.newLine();
            }

        // Caso der merda na hora de salvarno arquivo
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados no arquivo TXT.");
            e.printStackTrace();
        }
    }

    // Leitura do arquivo TXT ao iniciar o sistema
    private void carregarDadosDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return; // Se o arquivo não existe ainda, não faz nada
        }

        // Cria a lista a partir dos dados do arquivo
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

        // Caso der merda na hora de carregar o arquivo
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados do arquivo TXT.");
            e.printStackTrace();
        }
    }
}