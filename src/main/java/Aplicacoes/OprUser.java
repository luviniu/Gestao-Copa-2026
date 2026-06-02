package Aplicacoes;
import java.util.*;

import Objetos.*;

public class OprUser {
    private static OprUser instancia;
    private List<Usuario> usuarios;

    private OprUser() {
        this.usuarios = new ArrayList<>();

    }

    public static OprUser getInstancia() {
        if (instancia == null) {
            instancia = new OprUser();

        }

        return instancia;

    }

    public void registrarUsuario(Usuario novoUsuario) {
        if(usuarios.isEmpty()){
            Usuario admin= new Administrador(novoUsuario.getNome(),novoUsuario.getCpf(),novoUsuario.getEmail(),novoUsuario.getSenha());
            if(!novoUsuario.getEmail().matches("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                System.out.println("Email invalido!");
                return;

            }
            if(!novoUsuario.getSenha().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}")){
                System.out.println("Senha deve conter no minimo 8 caracteres; Numero, Letra maiuscula e Caractere especial");
                return;

            }
            int numadm=0;
            for(int i=0;i<novoUsuario.getCpf().length();i++){
                numadm=numadm+Character.getNumericValue(novoUsuario.getCpf().charAt(i));

            }
            if(numadm==33||numadm==44||numadm==55||numadm==66||numadm==0){
                usuarios.add(admin);
                System.out.println(admin.getNome()+" Administrador principal");

            }else{
                System.out.println("cpf invalido!");

            }
            return;

        }
        if(!novoUsuario.getEmail().matches("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            System.out.println("Email invalido!");
            return;

        }
        if(!novoUsuario.getSenha().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}")){
            System.out.println("Senha deve conter no minimo 8 caracteres; Numero, Letra maiuscula e Caractere especial");
            return;

        }
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

    public void listarUsuarios(){
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getNome()+usuario.getCpf()+usuario.getPerfilUsuario());

        }

    }
    public void editarUser(String cpf, String novoNome, String novoEmail, String novoSenha, String nacionalidade, String experiencia, String novoPerfil, Usuario usuarioLogado){
        if(!usuarioLogado.getPerfilUsuario().equals("Administrador")){
            return;

        }
        for(int i=0;i<usuarios.size();i++){
            Usuario usuario = usuarios.get(i);

            if(usuario.getCpf().equals(cpf)){
                if(usuario.getCpf().equals(usuarioLogado.getCpf())){
                    System.out.println("Nao pode editar seu proprio usuario!");
                    return;

                }
                if(usuario.getPerfilUsuario().equals(novoPerfil)){
                    usuario.setNome(novoNome);
                    usuario.setEmail(novoEmail);
                    usuario.setSenha(novoSenha);
                    if(usuario instanceof Arbitro){
                        Arbitro arbitro = (Arbitro)usuario;
                        arbitro.setNacionalidade(nacionalidade);
                        arbitro.setExperiencia(experiencia);

                    }

                    System.out.println(usuario.getNome()+" editado com sucesso!");

                }else{
                    Usuario usuarioMod;

                    if(novoPerfil.equals("Administrador")){
                        usuarioMod=new Administrador(novoNome, usuario.getCpf(), novoEmail, novoSenha);

                    }else if(novoPerfil.equals("Operador")){
                        usuarioMod=new Operador(novoNome, usuario.getCpf(), novoEmail, novoSenha);

                    }else if(novoPerfil.equals("Organizador")){
                        usuarioMod=new Organizador(novoNome, usuario.getCpf(), novoEmail, novoSenha);

                    }else{
                        usuarioMod=new Arbitro(novoNome, usuario.getCpf(), novoEmail, novoSenha, nacionalidade, experiencia);

                    }
                    usuarios.set(i,usuarioMod);

                }
                return;

            }

        }
        System.out.println("CPF invalido!");
    }
    public void excluirUser(String cpf, Usuario usuarioLogado){
        if(!usuarioLogado.getPerfilUsuario().equals("Administrador")){
            return;

        }
        for(Usuario usuario : usuarios){
            if(usuario.getCpf().equals(cpf)){
                if(usuario.getCpf().equals(usuarioLogado.getCpf())){
                    System.out.println("Nao pode excluir seu usuario!");
                    return;

                }
                usuarios.remove(usuario);
                System.out.println(usuario.getNome()+" excluado com sucesso!");
                return;

            }

        }
        System.out.println("CPF invalido!");

    }
    public void pesquisarUsuario(String pesquisa){
        boolean achou=false;
        for(Usuario usuario : usuarios){
            if(usuario.getNome().toLowerCase().contains(pesquisa.toLowerCase())||usuario.getPerfilUsuario().toLowerCase().contains(pesquisa.toLowerCase())){
                System.out.println(usuario.getNome()+" | "+usuario.getPerfilUsuario());
                achou=true;

            }

        }
        if(!achou){
            System.out.println("Nenhum usuario encontrado!");

        }

    }

}
