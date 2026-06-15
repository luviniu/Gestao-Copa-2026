package Aplicacoes;
import java.util.*;
import java.io.*;

import Objetos.*;

public class OprUser {
    private static OprUser instancia;
    private List<Usuario> usuarios;

    private OprUser() {
        this.usuarios = new ArrayList<>();
        carregarUsuarios();

    }

    public static OprUser getInstancia() {
        if (instancia == null) {
            instancia = new OprUser();

        }

        return instancia;

    }

    public boolean registrarUsuario(Usuario novoUsuario) throws ErrosException {
        if(usuarios.isEmpty()){
            Usuario admin= new Administrador(novoUsuario.getNome(),novoUsuario.getCpf(),novoUsuario.getEmail(),novoUsuario.getSenha());
            if(!novoUsuario.getEmail().matches("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
                throw new ErrosException("Email invalido!");

            }
            if(!novoUsuario.getSenha().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}")){
                throw new ErrosException("Senha deve conter no minimo 8 caracteres; Numero, Letra maiuscula e Caractere especial");

            }
            int numadm=0;
            for(int i=0;i<novoUsuario.getCpf().length();i++){
                numadm=numadm+Character.getNumericValue(novoUsuario.getCpf().charAt(i));

            }
            if(numadm==33||numadm==44||numadm==55||numadm==66||numadm==0){
                usuarios.add(admin);
                salvarUsuarios();
                return true;

            }else{
                throw new ErrosException("cpf invalido!");

            }

        }
        if(!novoUsuario.getEmail().matches("^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            throw new ErrosException("Email invalido!");

        }
        if(!novoUsuario.getSenha().matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[^a-zA-Z0-9]).{8,}")){
            throw new ErrosException("Senha deve conter no minimo 8 caracteres; Numero, Letra maiuscula e Caractere especial");

        }
        for (Usuario usuario : usuarios) {
            if(usuario.getCpf().equals(novoUsuario.getCpf())){
                throw new ErrosException("CPF JA CADASTRADO");

            }

        }
        int num=0;
        for(int i=0;i<novoUsuario.getCpf().length();i++){
            num=num+Character.getNumericValue(novoUsuario.getCpf().charAt(i));

        }
        if(num==33||num==44||num==55||num==66||num==0){
            usuarios.add(novoUsuario);
            salvarUsuarios();
            return true;

        }else{
            throw new ErrosException("cpf invalido!");

        }

    }

    public Usuario login(String login, String senha) {
        for (Usuario usuario : usuarios) {
            if ((usuario.getCpf().equals(login) || usuario.getEmail().equals(login))&&usuario.getSenha().equals(senha)){
                return usuario;

            }

        }
        return null;

    }

    public void listarUsuarios(){
        for (Usuario usuario : usuarios) {
            System.out.println(usuario.getNome()+usuario.getCpf()+usuario.getPerfilUsuario());

        }

    }

    public void editarUser(String cpf, String novoNome, String novoEmail, String novoSenha, String nacionalidade, String experiencia, String categoria, String novoPerfil, Usuario usuarioLogado){
        if(!usuarioLogado.getPerfilUsuario().equals("Administrador")){
            return;

        }
        for(int i=0;i<usuarios.size();i++){
            Usuario usuario = usuarios.get(i);

            if(usuario.getCpf().equals(cpf)){
                if(usuario.getCpf().equals(usuarioLogado.getCpf())){
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
                    salvarUsuarios();
                    return;

                }else{
                    Usuario usuarioMod;

                    if(novoPerfil.equals("Administrador")){
                        usuarioMod=new Administrador(novoNome, usuario.getCpf(), novoEmail, novoSenha);

                    }else if(novoPerfil.equals("Operador")){
                        usuarioMod=new Operador(novoNome, usuario.getCpf(), novoEmail, novoSenha);

                    }else if(novoPerfil.equals("Organizador")){
                        usuarioMod=new Organizador(novoNome, usuario.getCpf(), novoEmail, novoSenha);

                    }else{
                        usuarioMod=new Arbitro(novoNome, usuario.getCpf(), novoEmail, novoSenha, nacionalidade, experiencia, categoria);

                    }
                    usuarios.set(i,usuarioMod);
                    salvarUsuarios();
                    return;

                }

            }

        }

    }

    public void excluirUser(String cpf, Usuario usuarioLogado){
        if(!usuarioLogado.getPerfilUsuario().equals("Administrador")){
            return;

        }
        for(Usuario usuario : usuarios){
            if(usuario.getCpf().equals(cpf)){
                if(usuario.getCpf().equals(usuarioLogado.getCpf())){
                    return;

                }
                usuarios.remove(usuario);
                salvarUsuarios();
                return;

            }

        }

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

    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);

    }

    public void salvarUsuarios() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("usuarios.txt"))) {
            for (Usuario u : usuarios) {
                String linha = u.getPerfilUsuario() + ";" + u.getNome() + ";" + u.getCpf() + ";" + u.getEmail() + ";" + u.getSenha();
                if (u instanceof Arbitro) {
                    Arbitro a = (Arbitro) u;
                    linha += ";" + a.getNacionalidade() + ";" + a.getExperiencia();

                }
                writer.write(linha);
                writer.newLine();

            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void carregarUsuarios() {
        File arquivo = new File("usuarios.txt");
        if (!arquivo.exists()) {
            return;

        }
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            usuarios.clear();
            while ((linha = reader.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                String[] partes = linha.split(";");
                String perfil = partes[0];
                String nome = partes[1];
                String cpf = partes[2];
                String email = partes[3];
                String senha = partes[4];
                Usuario usuario;
                if (perfil.equals("Administrador")) {
                    usuario = new Administrador(nome, cpf, email, senha);

                } else if (perfil.equals("Operador")) {
                    usuario = new Operador(nome, cpf, email, senha);

                } else if (perfil.equals("Organizador")) {
                    usuario = new Organizador(nome, cpf, email, senha);

                } else if (perfil.equals("Arbitro")) {
                    String nacionalidade = partes.length > 5 ? partes[5] : "";
                    String experiencia = partes.length > 6 ? partes[6] : "";
                    String categoria = partes.length > 7 ? partes[7] : "";
                    usuario = new Arbitro(nome, cpf, email, senha, nacionalidade, experiencia, categoria);

                } else {
                    usuario = new Funcionario(nome, cpf, email, senha);

                }
                usuarios.add(usuario);

            }

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

}