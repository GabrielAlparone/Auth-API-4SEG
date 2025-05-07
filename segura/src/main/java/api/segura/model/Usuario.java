package api.segura.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "tb_usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, unique = true, length = 255)
    private String username;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(length = 255)
    private String nome;

    @Column(length = 255)
    private String email;

    @Column(length = 255)
    private String perfil;

    @Column(name = "ip_autorizado", length = 255)
    private String ipAutorizado;

    // Getters e setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPerfil() {
        return perfil;
    }

    public void setPerfil(String perfil) {
        this.perfil = perfil;
    }

    public String getIpAutorizado() {
        return ipAutorizado;
    }

    public void setIpAutorizado(String ipAutorizado) {
        this.ipAutorizado = ipAutorizado;
    }
}


