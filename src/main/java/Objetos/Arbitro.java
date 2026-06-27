package Objetos;

public class Arbitro extends Usuario implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    String nacionalidade;
    String experiencia;
    String categoria;

    public Arbitro(String nome, String cpf, String email, String senha, String nacionalidade, String experiencia, String categoria){
        super(nome, cpf, email, senha);
        this.nacionalidade = nacionalidade;
        this.experiencia = experiencia;
        this.categoria = categoria;
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

    public  String getCategoria(){
        return categoria;
    }

    /*public void setCategoria(String categoria){
        this.categoria = categoria;
    }*/



}
