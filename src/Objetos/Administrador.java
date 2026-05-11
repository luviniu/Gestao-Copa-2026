package Objetos;

public class Administrador extends Usuario{
    public Administrador(String nome, String cpf, String email, String senha){
        super(nome, cpf, senha, senha);

    }

    @Override
    public String getPerfilUsuario(){
        return "Administrador";

    }

}
