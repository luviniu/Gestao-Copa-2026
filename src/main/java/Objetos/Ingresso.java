package Objetos;

public class Ingresso{
    private String tipo;
    private Partida partida;
    private double valor;

    public Ingresso(String tipo, Partida partida, double valor){
        this.tipo = tipo.toUpperCase();
        this.partida = partida;
        this.valor = valor;
        this.valor = calcularValor();

    }

    public double calcularValor(){
        if(this.tipo.equals("VIP")){
            return this.valor*2;

        }else if(this.tipo.equals("MEIA")){
            return this.valor/2;

        }
        return this.valor;

    }

    public double getValor() {
        return valor;

    }
    public String getTipo() {
        return tipo;

    }
    public Partida getPartida() {
        return partida;

    }
}
