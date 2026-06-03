/* package Aplicacoes;

    import Objetos.Selecao;
    import Objetos.Jogador; // Importante para o mapeamento no TXT
    import java.io.*;
    import java.util.ArrayList;
    import java.util.List;

/* NOTE!!
- As REGRAS para cadastrar seleo: campos preenchidos, nenhuma dplica
- Mtodos de PERSISTNCIA: arquivo .txt pra guardar lista de selees+grupo+tcnico
    Usar para conferir se dados esto duplicados
    Carregar o .txt numa lista ao abrir o programa. Retornar Exception se der ruim
 */ /*
public class OprSel {

    // Lista que contm as selees
    private List<Selecao> listaSelecoes;
    private final String CAMINHO_ARQUIVO = "selecoes.txt"; // O arquivo vai ficar na raiz do projeto ( final pra ningum poder mexer)

    // Construtor da classe, j faz o upload do .txt
    public OprSel() {
        this.listaSelecoes = new ArrayList<>();
        carregarDadosDoArquivo(); // Toda vez que o sistema inicia, ele l o TXT
    }

    // Cadastrar Selecao
    public boolean cadastrarSelecao(String pais, String grupo, String tecnico) {

        // Validao: Campos no podem ser vazios
        if (pais == null || pais.trim().isEmpty() || tecnico == null || tecnico.trim().isEmpty()) {
            return false;
        }

        // Validao: No permitir dois pases com o mesmo nome
        for (Selecao s : listaSelecoes) {
            if (s.getPais().equalsIgnoreCase(pais.trim())) {
                System.out.println("Erro de Negcio: Essa seleo j est cadastrada.");
                return false;
            }
        }

        // Se passou nas validaes, j cria o objeto e adiciona na lista (trim  pra tirar os espaos nas entradas)
        Selecao novaSelecao = new Selecao(pais.trim(), grupo, tecnico.trim(), new ArrayList<>());
        listaSelecoes.add(novaSelecao);

        // Salva a lista atualizada no arquivo TXT de forma persistente
        salvarDadosNoArquivo();
        return true;
    }

    // Editar Selecao
    public boolean editarSelecao(String paisParaEditar, String novoGrupo, String novoTecnico) {
        // Validacao
        if (paisParaEditar == null || novoTecnico == null || novoTecnico.trim().isEmpty()) {
            return false;
        }

        // Procurar a seleo na  lista em memria
        for (Selecao s : listaSelecoes) {
            // Se encontrarmos o pas (ignorando maisculas/minsculas)
            if (s.getPais().equalsIgnoreCase(paisParaEditar.trim())) {

                // 3. Aplica as alteraes usando os setters da sua classe Selecao
                s.setGrupo(novoGrupo);
                s.setTecnico(novoTecnico.trim());

                // 4. Atualiza o arquivo TXT com os novos dados salvos
                salvarDadosNoArquivo();
                return true; // Edio feita com sucesso!
            }
        }

        // Se percorreu a lista toda e no achou o pas
        System.out.println("Erro de Negcio: Seleo no encontrada para edio.");
        return false;
    }

    // Método Excluir Seleção
    public boolean excluirSelecao(String paisParaExcluir) {
        if (paisParaExcluir == null) {
            return false;
        }

        for (Selecao s : listaSelecoes) {
            if (s.getPais().equalsIgnoreCase(paisParaExcluir.trim())) {
                listaSelecoes.remove(s);
                salvarDadosNoArquivo(); // Atualiza o TXT limpando a seleção excluída
                return true;
            }
        }
        return false;
    }

    // Método Consultar Seleções por Critério (Grupo)
    public List<Selecao> consultarSelecoesPorGrupo(String grupo) {
        List<Selecao> filtradas = new ArrayList<>();
        for (Selecao s : listaSelecoes) {
            if (s.getGrupo().equalsIgnoreCase(grupo.trim())) {
                filtradas.add(s);
            }
        }
        return filtradas;
    }

    // Caso for preciso retornar a lista de selees, quem sabe
    public List<Selecao> getListaSelecoes() {
        return this.listaSelecoes;
    }

    // (OprJog usa esse método)
    // Além disso, agora salva jogadores logo abaixo da linha de cada seleção.
    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {

            // Ele percorre a lista linha por linha
            for (Selecao s : listaSelecoes) {
                // Salvando os dados identificados pela tag SELECAO
                writer.write("SELECAO;" + s.getPais() + ";" + s.getGrupo() + ";" + s.getTecnico());
                writer.newLine();

                // Grava também os jogadores que pertencem a essa seleção
                for (Jogador j : s.getTime()) {
                    writer.write("JOGADOR;" + j.getNome() + ";" + j.getPosicao() + ";" + j.getNumero() + ";" + j.getIdade() + ";" + j.getStatus());
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            System.out.println("Erro ao salvar dados no arquivo TXT.");
            e.printStackTrace();
        }
    }

    //
    private void carregarDadosDoArquivo() {
        File arquivo = new File(CAMINHO_ARQUIVO);
        if (!arquivo.exists()) {
            return; // Se o arquivo no existe ainda, no faz nada
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            Selecao selecaoAtual = null;

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                // Se for linha de Seleção
                if (dados[0].equals("SELECAO") && dados.length == 4) {
                    String pais = dados[1];
                    String grupo = dados[2];
                    String tecnico = dados[3];

                    selecaoAtual = new Selecao(pais, grupo, tecnico, new ArrayList<>());
                    listaSelecoes.add(selecaoAtual);

                    // Se for linha de Jogador, joga ele para dentro da última seleção criada
                } else if (dados[0].equals("JOGADOR") && dados.length == 6 && selecaoAtual != null) {
                    String nome = dados[1];
                    String posicao = dados[2];
                    int numero = Integer.parseInt(dados[3]);
                    int idade = Integer.parseInt(dados[4]);
                    String status = dados[5];

                    Jogador j = new Jogador(nome, posicao, numero, idade, status);
                    selecaoAtual.addTime(j);
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar dados do arquivo TXT.");
            e.printStackTrace();
        }
    }
}*/