package Objetos;

public class Jogador{
    private String nome;
    private String numero;
    private int posicao;
    private int idade;
    private Selecao selecao;
    private String status;

    public Jogador(String nome, String numero, int posicao, int idade, Selecao selecao, String status){
        this.nome = nome;
        this.numero = numero;
        this.posicao = posicao;
        this.idade = idade;
        this.selecao = selecao;
        setStatus(status);

        if (selecao != null){
            selecao.addTime(this);

        }

    }

    public String getNome(){
        return nome;

    }
    public void setNome(String nome){
        this.nome = nome;

    }

    public String getNumero(){
        return numero;

    }
    public void setNumero(String numero){
        this.numero = numero;

    }

    public int getPosicao(){
        return posicao;

    }
    public void setPosicao(int posicao){
        this.posicao = posicao;

    }

    public int getIdade(){
        return idade;

    }
    public void setIdade(int idade){
        this.idade = idade;

    }

    public Selecao getSelecao(){
        return selecao;

    }
    public void setSelecao(Selecao selecao){
        this.selecao = selecao;
        if (selecao != null) {
            selecao.addTime(this);

        }

    }

    public String getStatus(){
        return status;

    }
    public void setStatus(String status){
        this.status = status;

    }


}
