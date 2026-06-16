package Aplicacoes;

import Objetos.Arbitro;
import Objetos.Estadio;
import Objetos.Usuario;
//import Objetos.Selecao;

import java.io.*;
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

    public boolean cadastrarArbitro(String nome, String cpf, String email, String senha,
                                    String nacionalidade, String experiencia, String categoria) {
        if (nome == null || nome.trim().isEmpty() || cpf == null || cpf.trim().isEmpty() || email == null || email.trim().isEmpty() || senha == null || senha.trim().isEmpty() || nacionalidade == null || nacionalidade.trim().isEmpty() || experiencia == null || experiencia.trim().isEmpty() || categoria == null || categoria.trim().isEmpty()) {
            return false;

        }

        for (Arbitro a : getListaArbitros()) {
            if (a.getNome().equalsIgnoreCase(nome.trim())) {
                System.out.println("Erro: Árbitro já cadastrado.");
                return false;

            }

        }

        Arbitro novoArbitro = new Arbitro(nome.trim(), cpf.trim(), email.trim(), senha.trim(),
                nacionalidade.trim(), experiencia.trim(), categoria.trim());
        try {
            return OprUser.getInstancia().registrarUsuario(novoArbitro);

        } catch (ErrosException e) {
            System.out.println("Erro ao cadastrar árbitro: " + e.getMessage());
            return false;

        }

    }

    // Delega a exclusão ao OprUser
    public boolean excluirArbitro(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) return false;
        boolean existe = getListaArbitros().stream()
                .anyMatch(a -> a.getCpf().equals(cpf.trim()));

        if (!existe) {
            System.out.println("Árbitro com CPF " + cpf + " não encontrado.");
            return false;

        }

        OprUser.getInstancia().excluirUser(cpf.trim(), oprSessao.getUsuario());
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