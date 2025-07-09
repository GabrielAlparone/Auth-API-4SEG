package api.segura.interfaces.rest.usuario;

public class LoginRequest {
    private String username;
    private String senha;
    private String captcha;

    // Getters e Setters
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getSenha() {
        return senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getcaptcha() {
        return captcha;
    }
    public void setcaptcha(String captcha) {
        this.captcha = captcha;
    }
}

