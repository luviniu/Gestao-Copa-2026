package Objetos;

public class Organizador extends Usuario{
    public Organizador(String nome, String cpf, String email, String senha){
        super(nome, cpf, email, senha);

    }

    @Override
    public String getPerfilUsuario(){
        return "Organizador";

    }

}
