package Objetos;

public class Partida{
    private String data;
    private String hora;
    private String fase;
    private String status;
    private Estadio estadio;
    private final Selecao timeCasa;
    private final Selecao timeVisita;
    private Arbitro arbitro;
    private int golCasa;
    private int golVisita;

    public Partida(String data, String hora, String fase, String status, Estadio estadio, Selecao timeCasa, Selecao timeVisita, Arbitro arbitro,  int golCasa, int golVisita){
        this.data = data;
        this.hora = hora;
        this.fase = fase;
        this.status = "Agendada";
        this.estadio = estadio;
        this.timeCasa = timeCasa;
        this.timeVisita = timeVisita;
        this.arbitro = arbitro;
        this.golCasa = 0;
        this.golVisita = 0;

    }

    public String getData(){
        return data;

    }
    public void setData(String data){
        this.data = data;

    }

    public String getHora(){
        return hora;

    }
    public void setHora(String hora){
        this.hora = hora;

    }

    public String getFase(){
        return fase;

    }

    public void setFase(String fase){
        this.fase = fase;

    }

    public String getStatus(){
        return status;

    }
    public void setStatus(String status){
        this.status = status;

    }

    public Estadio getEstadio(){
        return estadio;

    }
    public void setEstadio(Estadio estadio){
        this.estadio = estadio;

    }

    public Selecao getTimeCasa(){
        return timeCasa;

    }

    public Selecao getTimeVisita() {
        return timeVisita;

    }

    public Arbitro getArbitro() {
        return arbitro;

    }
    public void Arbitro(Arbitro arbitro) {
        this.arbitro = arbitro;

    }

    public int  getGolCasa() {
        return golCasa;
    }
    public void setGolCasa(int golCasa) {
        this.golCasa = golCasa;

    }

    public int getGolVisita(){
        return golVisita;

    }
    public void setGolVisita(int golVisita) {
        this.golVisita = golVisita;

    }

}

