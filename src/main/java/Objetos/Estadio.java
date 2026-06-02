package Objetos;

public class Estadio{
    private String nome;
    private String local;
    private int vagas;

    public  Estadio(String nome, String local, int vagas){
        this.nome = nome;
        this.local = local;
        this.vagas = vagas;

    }

    public String getNome(){
        return nome;

    }
    public void setNome(String nome){
        this.nome = nome;

    }

    public String getLocal(){
        return local;

    }
    public void setLocal(String local){
        this.local = local;

    }
    public int getVagas(){
        return vagas;

    }
    public void setVagas(int vagas){
        this.vagas = vagas;

    }
}