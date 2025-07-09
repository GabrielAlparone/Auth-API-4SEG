package api.segura.domain.usuario;

import api.segura.util.HashUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioDomainService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario registrar(Usuario usuario) {
        // Aplica SHA-256 antes de salvar
        usuario.setSenha(HashUtil.sha256(usuario.getSenha()));
        return usuarioRepository.save(usuario);
    }

    // Novo método: busca apenas pelo username
    public Optional<Usuario> buscarPorUsername(String username) {
        return usuarioRepository.findByUsername(username);
    }

    // Novo método: compara a senha informada com a salva (criptografada)
    public boolean verificarSenha(String senhaInformada, String senhaSalvaHash) {
        String senhaInformadaHash = HashUtil.sha256(senhaInformada);
        return senhaInformadaHash.equals(senhaSalvaHash);
    }
}


