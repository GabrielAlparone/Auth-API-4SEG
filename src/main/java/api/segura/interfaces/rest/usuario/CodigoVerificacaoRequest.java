package api.segura.interfaces.rest.usuario;

public class CodigoVerificacaoRequest {
    private String username;
    private String codigo;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}

