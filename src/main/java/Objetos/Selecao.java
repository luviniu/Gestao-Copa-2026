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
        this.time  = new ArrayList<>();

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
        if (this.time.contains(jogador) == false){
            this.time.add(jogador);

        }

    }
    public void delTime(Jogador jogador){
        this.time.remove(jogador);

    }

    public boolean tamanhoTime(){
        return this.time.size()>=18 && this.time.size()<=24;

    }

    public boolean isElegivel() {
        int totalJogadores = time.size();

        // Se o elenco total desrespeitar o limite do campeonato, já barra aqui
        if (totalJogadores < 18 || totalJogadores > 26) {
            return false;
        }

        // Conta quantos atletas estão realmente aptos (Ativos)
        long ativos = time.stream()
                .filter(j -> "Ativo".equals(j.getStatus()))
                .count();

        // Verifica se há pelo menos 18 ativos
        return ativos >= 18;
    }

}
