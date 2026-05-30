package Aplicacoes;
import java.util.*;

import Objetos.*;

public class OprUser {
    private static OprUser instancia;
    private List<Usuario> usuarios;

    private OprUser() {
        this.usuarios = new ArrayList<>();
        registrarUsuario(new Administrador("admin", "00000000000", "admin@copa.com", "admin"));

    }

    public static OprUser getInstancia() {
        if (instancia == null) {
            instancia = new OprUser();

        }

        return instancia;

    }

    public void registrarUsuario(Usuario novoUsuario) {
        for (Usuario usuario : usuarios) {
            if(usuario.getCpf().equals(novoUsuario.getCpf())){
                System.out.println("CPF JA CADASTRADO");
                return;

            }

        }
        int num=0;
        for(int i=0;i<novoUsuario.getCpf().length();i++){
            num=num+Character.getNumericValue(novoUsuario.getCpf().charAt(i));

        }
        if(num==33||num==44||num==55||num==66||num==0){
            usuarios.add(novoUsuario);
            System.out.println(novoUsuario.getCpf() + " cadastrado com sucesso!");

        }else{
            System.out.println("cpf invalido!");

        }

    }
    public Usuario login(String cpf, String senha) {
        for (Usuario usuario : usuarios) {
            if(usuario.getCpf().equals(cpf) && usuario.getSenha().equals(senha)){
                String perfil=usuario.getPerfilUsuario();
                System.out.println(usuario.getNome()+" logado");

                return usuario;

            }

        }
        System.out.println("CPF ou Senha invalido!");
        return null;

    }

}
