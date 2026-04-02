package com.webscrap.validation;

import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class InputValidator {

    // Solo permite letras, números, espacios y algunos caracteres
    private static final Pattern SAFE_INPUT = Pattern.compile("^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-]+$");
    private static final int MAX_LENGTH = 100;

    public String sanitize(String input) {
        if (input == null) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }

        String trimmed = input.trim();

        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException("El término de búsqueda no puede estar vacío");
        }

        if (trimmed.length() > MAX_LENGTH) {
            throw new IllegalArgumentException("El término de búsqueda es demasiado largo (máx " + MAX_LENGTH + " caracteres)");
        }

        if (!SAFE_INPUT.matcher(trimmed).matches()) {
            throw new IllegalArgumentException("El término de búsqueda contiene caracteres no permitidos");
        }

        return trimmed;
    }
}
