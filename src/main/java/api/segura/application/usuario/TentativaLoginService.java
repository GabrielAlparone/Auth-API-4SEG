package api.segura.application.usuario;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TentativaLoginService {

    private Map<String, Integer> tentativas = new HashMap<>();
    private Map<String, String> captchas = new HashMap<>();

    public void registrarTentativaInvalida(String username) {
        int tentativasAtual = tentativas.getOrDefault(username, 0);
        int novaTentativa = tentativasAtual + 1;
        tentativas.put(username, novaTentativa);
        System.out.println("Tentativas para " + username + ": " + novaTentativa);
        if (novaTentativa >= 1) {
            gerarCaptcha(username);
        }
    }

    public void zerarTentativas(String username) {
        tentativas.remove(username);
        captchas.remove(username);
    }

    public boolean exigeCaptcha(String username) {
        int tentativasUsuario = tentativas.getOrDefault(username, 0);
        System.out.println("Verificando CAPTCHA para " + username + ": tentativas = " + tentativasUsuario);
        return tentativasUsuario >= 1;
    }

    public void gerarCaptcha(String username) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder captchaBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = (int) (Math.random() * caracteres.length());
            captchaBuilder.append(caracteres.charAt(index));
        }
        String captcha = captchaBuilder.toString();
        captchas.put(username, captcha);
        System.out.println("CAPTCHA gerado para " + username + ": " + captcha);
    }

    public boolean validarCaptcha(String username, String resposta) {
        return captchas.containsKey(username) && captchas.get(username).equals(resposta);
    }

    public String getCaptcha(String username) {
        return captchas.getOrDefault(username, "N/A");
    }
    
    
} 


