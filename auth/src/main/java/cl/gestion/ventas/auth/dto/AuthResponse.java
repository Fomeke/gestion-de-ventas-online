package cl.gestion.ventas.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * DTO con datos basicos como token,nombre y duración del token (en ms),
 * es el formato de respuesta del controlador para un inicio de sesión
 * correcto.
 */

@Data
@AllArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String nombre;
    private Long expiresIn;
}
