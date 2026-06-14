package Objetos;

public class Operador extends Usuario implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public Operador(String nome, String cpf, String email, String senha){
        super(nome, cpf, email, senha);

    }

    @Override
    public String getPerfilUsuario(){
        return "Operador";

    }

}
