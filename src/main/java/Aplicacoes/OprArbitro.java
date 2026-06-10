package Aplicacoes;

import Objetos.Arbitro;
//import Objetos.Selecao;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class OprArbitro {

    /*Estádios e Arbitragem
 Cadastrar, editar, excluir e listar estádios (nome, localização, capacidade).
 Associar partidas a estádios.
 Cadastrar árbitros (nome, nacionalidade, experiência).
 Designar árbitros para partidas.
 Consultar estádios e árbitros por critérios*/

    private List<Arbitro> listaArbitros;
    private final String CAMINHO_ARQUIVO = "arbitro.txt"; // O arquivo vai ficar na raiz do projeto ( final pra ningum poder mexer)

    // Construtor da classe, j faz o upload do .txt
    public OprArbitro() {
        this.listaArbitros = new ArrayList<>();
        carregarDadosDoArquivo(); // Toda vez que o sistema inicia, ele l o TXT
    }

    public boolean cadastrarArbitro(String nome, String cpf, String email, String senha, String nacionalidade, String experiencia) {

        // Validao: Campos no podem ser vazios
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty() || email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty() || nacionalidade == null || nacionalidade.trim().isEmpty() || experiencia == null || experiencia.trim().isEmpty()) {
            return false;
        }

        // Validao: No permitir dois pases com o mesmo nome
        for (Arbitro s : listaArbitros) {
            if (s.getNome().equalsIgnoreCase(nome.trim())) {
                System.out.println("Erro de Negocio: Essa arbitro j est cadastrada.");
                return false;
            }
        }

        // Se passou nas validaes, j cria o objeto e adiciona na lista (trim  pra tirar os espaos nas entradas)
        Arbitro novoArbitro = new Arbitro(nome.trim(), cpf.trim(), email.trim(), senha.trim(), nacionalidade.trim(), experiencia.trim());
        listaArbitros.add(novoArbitro);

        // Salva a lista atualizada no arquivo TXT de forma persistente
        salvarDadosNoArquivo();
        return true;
    }
    // REGRA DE NEGÓCIO: Validar escala do árbitro para a partida
    /*public boolean validarEscalaArbitro(Arbitro arbitro, Selecao casa, Selecao visita) {
        
        // Regra: Cada partida deve ter ao menos um árbitro principal
        if (arbitro == null) {
            System.out.println("Erro de Negócio: Toda partida precisa de ao menos um árbitro principal designado.");
            return false; // Falhou na regra
        }

        String nacArbitro = arbitro.getNacionalidade().trim();
        String paisCasa = casa.getPais().trim();   // (ou o método correspondente do Aluno 2, ex: getNome())
        String paisVisita = visita.getPais().trim();

        // Regra: Árbitros não podem atuar em partidas de seleções de sua nacionalidade
        if (nacArbitro.equalsIgnoreCase(paisCasa) || nacArbitro.equalsIgnoreCase(paisVisita)) {
            System.out.println("Erro de Negócio: O árbitro " + arbitro.getNome() + " não pode apitar jogos da sua própria nacionalidade (" + nacArbitro + ").");
            return false; // Falhou na regra
        }

        return true; // Passou na regra com sucesso!
    }*/


    public void salvarDadosNoArquivo() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(CAMINHO_ARQUIVO))) {

            // Ele percorre a lista linha por linha
            for (Arbitro s : listaArbitros) {
                // Salvando os dados identificados pela tag Arbitros
                writer.write("Arbitros;" + s.getNome() + ";" + s.getCpf() + ";" + s.getEmail() + ";" + s.getSenha() + ";" + s.getNacionalidade() + ";" + s.getExperiencia());
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
            Arbitro arbitroAtual = null;

            while ((linha = reader.readLine()) != null) {
                String[] dados = linha.split(";");

                if (dados[0].equals("Arbitros") && dados.length == 7) {
                    String nome = dados[1];
                    String cpf = dados[2];
                    String email = dados[3];
                    String senha = dados[4];
                    String nacionalidade = dados[5];
                    String experiencia = dados[6];

                    arbitroAtual = new Arbitro(nome, cpf, email, senha, nacionalidade, experiencia);
                    listaArbitros.add(arbitroAtual);
                }
            }

        } catch (IOException | NumberFormatException e) {
            System.out.println("Erro ao carregar dados do arquivo TXT.");
            e.printStackTrace();
        }
    }
}
