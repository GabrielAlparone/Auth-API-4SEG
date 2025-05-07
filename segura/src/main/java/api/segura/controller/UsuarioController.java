package api.segura.controller;

import api.segura.model.Usuario;
import api.segura.model.LoginRequest;
import api.segura.service.usuarioService;
import api.segura.service.TentativaLoginService;
import api.segura.util.InputSanitizer;
import api.segura.service.EmailService;
import api.segura.service.CodigoVerificacaoService;
import api.segura.model.CodigoVerificacaoRequest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UsuarioController {

    @Autowired
    private usuarioService usuarioService;

    @Autowired
    private TentativaLoginService tentativaLoginService;

    @Autowired
    private CodigoVerificacaoService codigoVerificacaoService;

    @Autowired
    private EmailService emailService;


    @PostMapping("/register")
    public ResponseEntity<String> registrar(@RequestBody Usuario usuario, HttpServletRequest request) {
        try {
            usuario.setUsername(InputSanitizer.sanitize(usuario.getUsername()));
            usuario.setSenha(InputSanitizer.sanitize(usuario.getSenha()));
            String ipCliente = request.getRemoteAddr();
            usuario.setIpAutorizado(ipCliente);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        usuarioService.registrar(usuario);
        return ResponseEntity.ok("Usuário registrado com sucesso.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String username, senha, captcha = null;
        String ipCliente = request.getRemoteAddr();

        try {
            username = InputSanitizer.sanitize(loginRequest.getUsername());
            senha = InputSanitizer.sanitize(loginRequest.getSenha());

            if (tentativaLoginService.exigeCaptcha(username)) {
                captcha = InputSanitizer.sanitize(loginRequest.getcaptcha());
                if (captcha == null || !tentativaLoginService.validarCaptcha(username, captcha)) {
                    tentativaLoginService.gerarCaptcha(username);
                    String novoCaptcha = tentativaLoginService.getCaptcha(username);
                    return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Digite o Captcha: " + novoCaptcha);
                }
            }
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

        Optional<Usuario> usuarioOpt = usuarioService.buscarPorUsername(username);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não encontrado.");
        }

        Usuario usuario = usuarioOpt.get();

        if (!usuarioService.verificarSenha(senha, usuario.getSenha())) {
            tentativaLoginService.registrarTentativaInvalida(username);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Senha incorreta.");
        }

        if (!usuario.getIpAutorizado().equals(ipCliente)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("IP não autorizado.");
        }

        // Tudo certo, gerar e enviar código
        tentativaLoginService.zerarTentativas(username);
        codigoVerificacaoService.gerarCodigo(username);
        emailService.enviarCodigoVerificacao(usuario.getEmail(), codigoVerificacaoService.getCodigo(username));

        return ResponseEntity.ok("Código de verificação enviado para o e-mail.");
    }

    // Etapa 2: Verificação do código enviado ao e-mail
    @PostMapping("/verificar-codigo")
    public ResponseEntity<String> verificarCodigo(@RequestBody CodigoVerificacaoRequest request) {
        String username, codigo;
        try {
            username = InputSanitizer.sanitize(request.getUsername());
            codigo = InputSanitizer.sanitize(request.getCodigo());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    
        boolean valido = codigoVerificacaoService.validarCodigo(username, codigo);
        if (!valido) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Código inválido ou expirado.");
        }
    
        return ResponseEntity.ok("Login autorizado com dois fatores!");
    }
}
    




