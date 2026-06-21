package cl.gestion.ventas.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description="token de JWT", example = "f38274r7384r8723q4rd2q3irg28q734r")
    private String token;

    @Schema(description="nombre de usuario que inició sesión", example = "admin")
    private String nombre;

    @Schema(description="duración del token en milisegundos", example = "3600000")
    private Long expiresIn;
}
