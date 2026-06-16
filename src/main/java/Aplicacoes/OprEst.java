package Aplicacoes;

//import Objetos.Partida;
import Objetos.Estadio;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*

Estádios e Arbitragem
 Cadastrar, editar, excluir e listar estádios (nome, localização, capacidade).
 Associar partidas a estádios.
 Cadastrar árbitros (nome, nacionalidade, experiência).
 Designar árbitros para partidas.
 Consultar estádios e árbitros por critérios

* */
public class OprEst {
    private List<Estadio> listaEstadio;
    private final String CAMINHO_ARQUIVO = "estadio.txt"; // O arquivo vai ficar na raiz do projeto ( final pra ningum poder mexer)

    private static OprEst instancia;

    public static OprEst getInstancia() {
        if (instancia == null) {
            instancia = new OprEst();
        }
        return instancia;
    }

    public OprEst() {
        this.listaEstadio = new ArrayList<>();
        carregarDadosDoArquivo(); // Toda vez que o sistema inicia, ele l o TXT
    }

    // Cadastrar Estadio
    public boolean cadastrarEstadio(String nome, String local, int vagas) {

        // Validao: Campos nao podem ser vazios e vagas devem ser maior que zero
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

        // Se passou nas validaes, cria o objeto e adiciona na lista (trim pra tirar os espaos nas entradas)
        Estadio novoEstadio = new Estadio(nome.trim(), local.trim(), vagas);
        listaEstadio.add(novoEstadio);

        // Salva a lista atualizada no arquivo TXT de forma persistente
        salvarDadosNoArquivo();
        return true;
    }

    public boolean editarEstadio(String nomeAtual, String novoNome, String localParaEditar, int vagasParaEditar) {
        // Validacao
        if (nomeAtual == null || novoNome == null || localParaEditar == null || localParaEditar.trim().isEmpty() || vagasParaEditar <= 0){
            return false;
        }

        // Procurar o estadio na lista em memoria
        for (Estadio s : listaEstadio) {
            // Se encontrarmos o pas (ignorando maisculas/minsculas)
            if (s.getNome().equalsIgnoreCase(nomeAtual.trim())) {

                // 3. Aplica as alteracoes usando os setters da sua classe Estadio
                s.setNome(novoNome);
                s.setLocal(localParaEditar);
                s.setVagas(vagasParaEditar);

                // 4. Atualiza o arquivo TXT com os novos dados salvos
                salvarDadosNoArquivo();
                return true; // Edicao feita com sucesso!
            }
        }

        // Se percorreu a lista toda e no achou o pas
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
                salvarDadosNoArquivo(); // Atualiza o TXT limpando a seleção excluída
                return true;
            }
        }
        return false;
    }
    // Método buscar um estádio pelo nome
    public Estadio buscarEstadioPorNome(String nome) {
        for (Estadio e : listaEstadio) {
            if (e.getNome().equalsIgnoreCase(nome.trim())) {
                return e; // Retorna o objeto Estadio encontrado
            }
        }
        return null; // Retorna null se não encontrar
    }

    // REGRA DE NEGÓCIO: Um estádio não pode sediar duas partidas no mesmo horário
    /*public boolean verificarDisponibilidadeEstadio(String nomeEstadio, String data, String horario, List<Partida> listaPartidasGlobais) {
        
        for (Partida p : listaPartidasGlobais) {
            // Se o estádio for o mesmo, no mesmo dia e no mesmo horário
            if (p.getEstadio().getNome().equalsIgnoreCase(nomeEstadio.trim()) && p.getData().equalsIgnoreCase(data.trim()) && p.getHora().equalsIgnoreCase(horario.trim())) {
                System.out.println("Erro de Negócio: O estádio " + nomeEstadio + " já possui uma partida agendada para " + data + " às " + horario + ".");
                return false; // Falhou na regra de negócio (estádio ocupado)
            }
        }
        
        return true; // Passou na regra, o estádio está livre!
    }*/

    // Getter para se precisar listar todos os estádios cadastrados
    public List<Estadio> getListaEstadio() {
        return this.listaEstadio;
    }
    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {

            // Ele percorre a lista linha por linha
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
            return; // Se o arquivo no existe ainda, no faz nada
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CAMINHO_ARQUIVO))) {
            String linha;
            Estadio estadioAtual = null;

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                // Se for linha de Seleção
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
