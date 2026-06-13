package Objetos;

public class Funcionario extends Usuario {
    public Funcionario(String nome, String cpf, String email, String senha) {
        super(nome, cpf, email, senha);

    }

    @Override
    public String getPerfilUsuario() {
        return "Sem cargo";

    }
}
