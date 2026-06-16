package Aplicacoes;

import Objetos.Arbitro;
import Objetos.Usuario;
import java.util.ArrayList;
import java.util.List;

public class OprArbitro {
    private static OprArbitro instancia;
    private OprArbitro() {}

    public static OprArbitro getInstancia() {
        if (instancia == null) {
            instancia = new OprArbitro();

        }
        return instancia;

    }
    public boolean cadastrarArbitro(String cpf, String nacionalidade, String experiencia, String categoria, Usuario usuarioLogado) {
        if (cpf == null || cpf.trim().isEmpty()
                || nacionalidade == null || nacionalidade.trim().isEmpty()
                || experiencia == null || experiencia.trim().isEmpty()
                || categoria == null || categoria.trim().isEmpty()) {
            return false;
        }

        // Acessa a lista global de usuários para encontrar o cadastro base
        OprUser oprUser = OprUser.getInstancia();
        Usuario usuarioExistente = null;

        for (Usuario u : oprUser.getUsuarios()) {
            if (u.getCpf().equals(cpf.trim())) {
                usuarioExistente = u;
                break;
            }
        }

        // Se o usuário não existe no sistema, não dá para promovê-lo
        if (usuarioExistente == null) {
            System.out.println("Erro: Usuário não encontrado.");
            return false;
        }

        try {
            // Invoca o editarUser passando os dados atuais dele e injetando as variáveis do árbitro
            oprUser.editarUser(
                    usuarioExistente.getCpf(),
                    usuarioExistente.getNome(),
                    usuarioExistente.getEmail(),
                    usuarioExistente.getSenha(),
                    nacionalidade.trim(),
                    experiencia.trim(),
                    categoria.trim(),
                    "Arbitro",
                    usuarioLogado
            );
            return true;

        } catch (Exception e) {
            System.out.println("Erro ao cadastrar árbitro: " + e.getMessage());
            return false;
        }
    }
    public boolean excluirArbitro(String cpf, Usuario usuarioLogado) {
        if (cpf == null || cpf.trim().isEmpty()) return false;
        boolean existe = getListaArbitros().stream().anyMatch(a -> a.getCpf().equals(cpf.trim()));

        if (!existe) {
            System.out.println("Árbitro com CPF " + cpf + " não encontrado.");
            return false;

        }
        OprUser.getInstancia().excluirUser(cpf.trim(), usuarioLogado);
        return true;

    }
    public List<Arbitro> getListaArbitros() {
        List<Arbitro> arbitros = new ArrayList<>();
        for (Usuario u : OprUser.getInstancia().getUsuarios()) {
            if (u instanceof Arbitro) {
                arbitros.add((Arbitro) u);

            }

        }
        return arbitros;

    }

}
