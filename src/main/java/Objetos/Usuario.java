package Objetos;

public abstract class Usuario implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private String nome;
    private String cpf;
    private String email;
    private String senha;

    public Usuario(String nome, String cpf, String email, String senha){
        this.nome = nome;
        this.cpf = cpf;
        this.email = email;
        setSenha(senha);

    }

    public String getNome(){
        return nome;

    }
    public void setNome(String nome){
        this.nome = nome;

    }

    public String getCpf(){
        return cpf;

    }
    public void setCpf(String cpf){
        this.cpf = cpf;

    }

    public String getEmail() {
        return email;

    }
    public void setEmail(String email){
        this.email = email;

    }

    public String getSenha(){
        return senha;

    }
    public void setSenha(String senha){
        this.senha = senha;

    }

    public abstract String getPerfilUsuario();

    @Override
    public String toString() {
        return this.nome; // O ComboBox vai exibir o nome do usuário
    }
}
