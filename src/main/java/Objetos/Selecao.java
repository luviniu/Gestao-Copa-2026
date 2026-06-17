package Objetos;

import java.util.*;

public class Selecao {
    private String pais;
    private String grupo;
    private String tecnico;
    private final List<Jogador> time;

    public Selecao(String pais, String grupo, String tecnico, List<Jogador> time){
        this.pais = pais;
        this.grupo = grupo;
        this.tecnico = tecnico;
        this.time = new ArrayList<>();

        if (time != null) {
            this.time.addAll(time);
        }
    }

    public String getPais(){
        return pais;
    }

    public void setPais(String pais){
        this.pais = pais;
    }

    public String getGrupo(){
        return grupo;
    }

    public void setGrupo(String grupo){
        this.grupo = grupo;
    }

    public String getTecnico(){
        return tecnico;
    }

    public void setTecnico(String tecnico){
        this.tecnico = tecnico;
    }

    public List<Jogador> getTime(){
        return time;
    }

    public void addTime(Jogador jogador){
        if (jogador != null && !this.time.contains(jogador)){
            this.time.add(jogador);
        }
    }

    public void delTime(Jogador jogador){
        this.time.remove(jogador);
    }

    public boolean isElegivel() {
        int totalJogadores = this.time.size();
        int jogadoresAtivos = 0;

        for (Jogador j : this.time) {
            if (j.getStatus() != null && j.getStatus().equalsIgnoreCase("Ativo")) {
                jogadoresAtivos++;
            }
        }

        boolean dentroDoLimiteElenco = (totalJogadores >= 18 && totalJogadores <= 26);
        boolean minimoAtivosSuficiente = (jogadoresAtivos >= 18);

        return dentroDoLimiteElenco && minimoAtivosSuficiente;
    }

    public boolean tamanhoTime(){
        return this.time.size() >= 18 && this.time.size() <= 26;
    }
}