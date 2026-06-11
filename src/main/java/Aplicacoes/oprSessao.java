
package Aplicacoes;

import Objetos.Usuario;

public class oprSessao {
    private static Usuario usuarioLogado;

    public static void setUsuario(Usuario u){
        usuarioLogado = u;

    }
    public static Usuario getUsuario(){
        return usuarioLogado;

    }
    public static void encerrar(){
        usuarioLogado = null;

    }

}