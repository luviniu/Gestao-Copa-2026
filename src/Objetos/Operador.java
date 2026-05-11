package Objetos;

public class Operador extends Usuario{
    public Operador(String nome, String cpf, String email, String senha){
        super(nome, cpf, senha, senha);

    }

    @Override
    public String getPerfilUsuario(){
        return "Operador";

    }

}
