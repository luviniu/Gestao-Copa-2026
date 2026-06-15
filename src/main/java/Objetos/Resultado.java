package Objetos;

public class Resultado {

    private int golsCasa;
    private int golsVisitante;

    public Resultado(int golsCasa, int golsVisitante) {
        this.golsCasa = golsCasa;
        this.golsVisitante = golsVisitante;
    }

    public int getGolsCasa() {
        return golsCasa;
    }

    public void setGolsCasa(int golsCasa) {
        this.golsCasa = golsCasa;
    }

    public int getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(int golsVisitante) {
        this.golsVisitante = golsVisitante;
    }
}