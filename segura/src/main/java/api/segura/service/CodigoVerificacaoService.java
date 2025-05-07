package api.segura.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CodigoVerificacaoService {

    private record CodigoInfo(String codigo, LocalDateTime expiracao) {}
    private final ConcurrentHashMap<String, CodigoInfo> codigos = new ConcurrentHashMap<>();

    public void gerarCodigo(String username) {
        String codigo = String.format("%06d", (int)(Math.random() * 1_000_000));
        codigos.put(username, new CodigoInfo(codigo, LocalDateTime.now().plusMinutes(5)));
    }

    public String getCodigo(String username) {
        CodigoInfo info = codigos.get(username);
        return (info != null) ? info.codigo : null;
    }

    public boolean validarCodigo(String username, String codigoDigitado) {
        CodigoInfo info = codigos.get(username);
        if (info == null || LocalDateTime.now().isAfter(info.expiracao())) {
            codigos.remove(username);
            return false;
        }
        boolean valido = info.codigo.equals(codigoDigitado);
        if (valido) codigos.remove(username);
        return valido;
    }
}
