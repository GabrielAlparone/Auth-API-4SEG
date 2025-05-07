package api.segura.util;

public class InputSanitizer {

    /**
     * Verifica se a string contém caracteres proibidos e lança exceção se contiver.
     */
    public static String sanitize(String input) {
        if (input == null) return null;

        // Define um padrão com os caracteres que não são permitidos
        if (input.matches(".*(['\";=<>]|--|(?i)or\\s+1=1|<script>).*")) {
            throw new IllegalArgumentException("Proibido caracteres especiais.");
        }

        return input.trim();
    }
}

