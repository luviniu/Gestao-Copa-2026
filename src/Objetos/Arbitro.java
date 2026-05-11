package Objetos;

public class Arbitro extends Usuario{
    String nacionalidade;
    String experiencia;

    public Arbitro(String nome, String cpf, String email, String senha, String nacionalidade, String experiencia){
        super(nome, cpf, senha, senha);
        this.nacionalidade = nacionalidade;
        this.experiencia = experiencia;

    }

    @Override
    public String getPerfilUsuario(){
        return "Arbitro";

    }

    public String getNacionalidade(){
        return nacionalidade;
    }
    public void setNacionalidade(String nacionalidade){
        this.nacionalidade = nacionalidade;

    }

    public String getExperiencia(){
        return experiencia;

    }
    public void setExperiencia(String experiencia){
        this.experiencia = experiencia;

    }

}
