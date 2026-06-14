package Objetos;

public class Organizador extends Usuario implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    public Organizador(String nome, String cpf, String email, String senha){
        super(nome, cpf, email, senha);

    }

    @Override
    public String getPerfilUsuario(){
        return "Organizador";

    }

}
