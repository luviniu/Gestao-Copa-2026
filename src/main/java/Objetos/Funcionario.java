package Objetos;

public class Funcionario extends Usuario implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public Funcionario(String nome, String cpf, String email, String senha) {
        super(nome, cpf, email, senha);

    }

    @Override
    public String getPerfilUsuario() {
        return "Sem cargo";

    }
}
